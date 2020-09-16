package com.cgm.kube.client.service.impl;

import com.cgm.kube.account.entity.SysUser;
import com.cgm.kube.account.service.ISysUserService;
import com.cgm.kube.base.BaseException;
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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cgm
 */
@Service
@Slf4j
public class DeploymentServiceImpl implements IDeploymentService {

    private final ISysUserService sysUserService;
    private final IPortInfoService portInfoService;
    private final IServiceService serviceService;
    private final IIngressService ingressService;

    public DeploymentServiceImpl(ISysUserService sysUserService, IPortInfoService portInfoService,
                                 IServiceService serviceService, IIngressService ingressService) {
        this.sysUserService = sysUserService;
        this.portInfoService = portInfoService;
        this.serviceService = serviceService;
        this.ingressService = ingressService;
    }

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
        Assert.notNull(list.getItems(), ErrorCode.SYS_NO_FIELD);
        if (list.getItems().isEmpty()) {
            return new ArrayList<>();
        }

        // 在k8s api-server中没有找到排序参数，暂时按时间降序
        return list.getItems().stream().map(UserDeploymentDTO::new)
                .sorted((a, b) -> (int) (b.getCreationTimestamp() - a.getCreationTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public void createDeployment(UserDeploymentDTO deployment) throws ApiException {
        SysUser user = sysUserService.getById(10000001L);
        this.checkResource(deployment, user, false);

        V1Deployment kubeDeployment = deployment.toKube();
        log.debug("转换结果:\n{}", kubeDeployment);

        // 1.创建Deployment
        AppsV1Api api = new AppsV1Api();
        // 避免跨组织操作，同时允许超管跨组织
        String namespace = user.isSystemAdmin() ? deployment.getNamespace() : "ns" + user.getOrganizationId();
        kubeDeployment = api.createNamespacedDeployment(namespace, kubeDeployment, "true", null, null);

        // 2.创建Service
        int freePort = portInfoService.getFreePort();
        Integer targetPort = deployment.getTargetPort();
        String serviceName = deployment.getName() + "-svc";
        String image = deployment.getImage();
        // 前端可以指定端口，未指定时由后端判断
        targetPort = targetPort == null ? ImageUtils.determineImagePort(image) : targetPort;
        serviceService.createService(namespace, serviceName, deployment.getName(), freePort, targetPort);

        // 3.创建Ingress
        Assert.isTrue(kubeDeployment.getMetadata() != null, ErrorCode.SYS_NO_FIELD);
        String uid = kubeDeployment.getMetadata().getUid();
        ingressService.createIngress(namespace, uid, serviceName, image, freePort);

        // 4.按需添加command，选择在此时进行修改，是因为command可能会需要之前的信息
        String[] command = ImageUtils.determineCommands(image, uid);
        if (command.length > 0) {
            this.patchDeploymentCommand(deployment.getName(), namespace, command, api);
        }

        log.info("Deployment created successfully, UID: {}", uid);
    }

    @Override
    public void updateDeployment(UserDeploymentDTO deployment) throws ApiException {
        SysUser user = sysUserService.getById(10000001L);
        this.checkResource(deployment, user, true);

        V1Deployment kubeDeployment = deployment.toKube();
        AppsV1Api api = new AppsV1Api();
        // 避免跨组织操作，同时允许超管跨组织
        String namespace = user.isSystemAdmin() ? deployment.getNamespace() : "ns" + user.getOrganizationId();
        api.replaceNamespacedDeployment(deployment.getName(), namespace, kubeDeployment, "true", null, null);
    }

    @Override
    public void patchDeploymentScale(UserDeploymentDTO deployment) throws ApiException {
        SysUser user = sysUserService.getById(10000001L);
        this.checkResource(deployment, user, true);

        V1Deployment kubeDeployment = deployment.toKube();
        V1Patch patch = new V1Patch(new Gson().toJson(kubeDeployment, V1Deployment.class));
        AppsV1Api api = new AppsV1Api();
        // 避免跨组织操作，同时允许超管跨组织
        String namespace = user.isSystemAdmin() ? deployment.getNamespace() : "ns" + user.getOrganizationId();
        api.patchNamespacedDeploymentScale(deployment.getName(), namespace, patch, "true", null, null, false);
    }

    @Override
    public void deleteDeployment(Long organizationId, String name) throws ApiException {
        SysUser user = sysUserService.getById(10000001L);

        AppsV1Api appsV1Api = new AppsV1Api();
        // 避免跨组织操作，同时允许超管跨组织
        String namespace = user.isSystemAdmin() ? "ns" + organizationId : "ns" + user.getOrganizationId();
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
     * 修改deployment command
     *
     * @param command 命令
     */
    private void patchDeploymentCommand(String name, String namespace, String[] command, AppsV1Api api) throws ApiException {
        String commandJson = new Gson().toJson(command);
        V1Patch patch = new V1Patch("[{\"op\": \"add\", \"path\": \"/spec/template/spec/containers/0/command\", \"value\":"
                + commandJson + "}]");
        api.patchNamespacedDeployment(name, namespace, patch, "true", null, null, null);
    }

    /**
     * 检查用户剩余资源是否满足新deployment
     *
     * @param deployment deployment
     * @param user       用户
     * @param update     是否更新，否表示新增
     */
    private void checkResource(UserDeploymentDTO deployment, SysUser user, boolean update) {
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
