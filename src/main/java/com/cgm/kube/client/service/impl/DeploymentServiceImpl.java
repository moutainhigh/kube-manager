package com.cgm.kube.client.service.impl;

import com.cgm.kube.account.entity.User;
import com.cgm.kube.account.service.IUserService;
import com.cgm.kube.base.BaseException;
import com.cgm.kube.base.Constant;
import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.client.dto.DeploymentParamDTO;
import com.cgm.kube.client.service.IDeploymentService;
import com.cgm.kube.client.dto.UserDeploymentDTO;
import com.cgm.kube.client.service.IIngressService;
import com.cgm.kube.client.service.IPortInfoService;
import com.cgm.kube.client.service.IServiceService;
import com.cgm.kube.util.ImageUtils;
import com.cgm.kube.util.ResourceFormatter;
import com.google.gson.Gson;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cgm
 */
@Service
@Slf4j
public class DeploymentServiceImpl implements IDeploymentService {

    @Autowired
    private IUserService userService;
    @Autowired
    private IPortInfoService portInfoService;
    @Autowired
    private IServiceService serviceService;
    @Autowired
    private IIngressService ingressService;

    @Override
    public UserDeploymentDTO getDeploymentByName(Long organizationId, String name) throws ApiException {
        AppsV1Api api = new AppsV1Api();
        api.readNamespacedDeployment(name, "ns" + organizationId, "true", null, null);
        return null;
    }

    @Override
    public List<UserDeploymentDTO> listDeployment(Long organizationId, DeploymentParamDTO paramDTO) throws ApiException {
        AppsV1Api api = new AppsV1Api();

        V1DeploymentList list = api.listNamespacedDeployment("ns" + organizationId, "true", true,
                null, null, null, paramDTO.getLimit(), null, 5, false);
        Assert.notNull(list.getItems(), ErrorCode.NO_FIELD);
        if (list.getItems().isEmpty()) {
            return new ArrayList<>();
        }

        log.debug("api-server返回对象（第一条）:\n{}", list.getItems().get(0));
        // 在k8s api-server中没有找到排序参数，暂时按时间降序
        return list.getItems().stream().map(UserDeploymentDTO::new)
                .sorted((a, b) -> (int) (b.getCreationTimestamp() - a.getCreationTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public void createDeployment(UserDeploymentDTO deployment) throws ApiException {
        User user = userService.findById(10000001L);
        this.checkResource(deployment, user, false);

        V1Deployment kubeDeployment = deployment.toKube();
        log.debug("转换结果:\n{}", kubeDeployment);
        AppsV1Api api = new AppsV1Api();
        // 普通用户不使用前端namespace参数而使用用户组织，此时跨组织的操作会因namespace不一致而被拦截，超管不会被拦截
        String namespace = user.getRoles().contains(Constant.ROLE_SYSTEM_ADMIN) ?
                deployment.getNamespace() : "ns" + user.getOrganizationId();
        kubeDeployment = api.createNamespacedDeployment(namespace, kubeDeployment, "true", null, null);

        // 创建Service
        int freePort = portInfoService.getFreePort();
        Integer targetPort = deployment.getTargetPort();
        // 前端可以指定端口，未指定时由后端判断
        targetPort = targetPort == null ? ImageUtils.determineImagePort(deployment.getImage()) : targetPort;
        serviceService.createService(deployment.getNamespace(), deployment.getName() + "-svc",
                deployment.getName(), freePort, targetPort);

        // 添加Ingress配置
        Assert.isTrue(kubeDeployment.getMetadata() != null, ErrorCode.NO_FIELD);
        String uid = kubeDeployment.getMetadata().getUid();
        log.info("Deployment created successfully, UID: {}", uid);
        ingressService.createIngress(deployment.getNamespace(), uid, deployment.getName() + "-svc", freePort);
    }

    @Override
    public void updateDeployment(UserDeploymentDTO deployment) throws ApiException {
        User user = userService.findById(10000001L);
        this.checkResource(deployment, user, true);

        V1Deployment kubeDeployment = deployment.toKube();
        AppsV1Api api = new AppsV1Api();
        String namespace = user.getRoles().contains(Constant.ROLE_SYSTEM_ADMIN) ?
                deployment.getNamespace() : "ns" + user.getOrganizationId();
        api.replaceNamespacedDeployment(deployment.getName(), namespace, kubeDeployment, "true", null, null);
    }

    @Override
    public void patchDeploymentScale(UserDeploymentDTO deployment) throws ApiException {
        User user = userService.findById(10000001L);
        this.checkResource(deployment, user, true);

        V1Deployment kubeDeployment = deployment.toKube();
        V1Patch patch = new V1Patch(new Gson().toJson(kubeDeployment, V1Deployment.class));
        AppsV1Api api = new AppsV1Api();
        String namespace = user.getRoles().contains(Constant.ROLE_SYSTEM_ADMIN) ?
                deployment.getNamespace() : "ns" + user.getOrganizationId();
        api.patchNamespacedDeploymentScale(deployment.getName(), namespace, patch, "true", null, null, false);
    }

    @Override
    public void deleteDeployment(Long organizationId, String name) throws ApiException {
        User user = userService.findById(10000001L);
        // 仅作为权限控制的示意，非最终逻辑
        if (!user.getRoles().contains(Constant.ROLE_SYSTEM_ADMIN)) {
            throw new BaseException("权限不足");
        }

        AppsV1Api appsV1Api = new AppsV1Api();
        String namespace = user.getRoles().contains(Constant.ROLE_SYSTEM_ADMIN) ?
                "ns" + organizationId : "ns" + user.getOrganizationId();
        appsV1Api.deleteNamespacedDeployment(name, namespace, "true", null, null,
                null, null, null);

        // 清理Service
        CoreV1Api coreV1Api = new CoreV1Api();
        coreV1Api.deleteNamespacedService(name + "-svc", namespace, "true", null, null,
                null, null, null);
        // 清理Ingress
        ExtensionsV1beta1Api extensionsApi = new ExtensionsV1beta1Api();
        extensionsApi.deleteNamespacedIngress(name + "-igs", namespace, "true", null, null,
                null, null, null);
    }

    /**
     * 检查用户剩余资源是否满足新deployment
     *
     * @param deployment deployment
     * @param user       用户
     * @param update     是否更新，否表示新增
     */
    private void checkResource(UserDeploymentDTO deployment, User user, boolean update) {
        // 1.查询所有deployment，统计使用量，后续可能还要查询Job和CronJob

        // 2.如果是更新，从deployment列表中，找出当前deployment，使用量减去这一条
        if (update) {
            log.debug("更新操作");
        }

        // 3.查询用户资源限制，如果使用量 + 新deployment > 资源限制，抛出异常

        // 测试性代码，非最终逻辑
        if (ResourceFormatter.formatCpu(deployment.getCpuLimits()) > user.getCpuLimits()
                || ResourceFormatter.formatMem(deployment.getMemLimits()) > user.getMemLimits()
                || ResourceFormatter.formatGpu(deployment.getGpuLimits()) > user.getGpuLimits()) {
            throw new BaseException("超过了资源限制");
        }
    }
}
