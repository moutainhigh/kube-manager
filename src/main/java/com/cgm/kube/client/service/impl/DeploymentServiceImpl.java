package com.cgm.kube.client.service.impl;

import com.cgm.kube.account.entity.User;
import com.cgm.kube.account.service.IUserService;
import com.cgm.kube.base.BaseException;
import com.cgm.kube.client.constant.KubeConstant;
import com.cgm.kube.client.dto.DeploymentParamDTO;
import com.cgm.kube.client.service.IDeploymentService;
import com.cgm.kube.client.dto.UserDeploymentDTO;
import com.cgm.kube.util.ResourceFormatter;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cgm
 */
@Service
public class DeploymentServiceImpl implements IDeploymentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserService userService;

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
                null, null, null, 10, null, 5, false);
        Assert.notEmpty(list.getItems(), KubeConstant.ErrorCode.NO_RESULT);
        logger.debug("api-server返回对象:\n{}", list.getItems().get(0));
        return list.getItems().stream().map(UserDeploymentDTO::new).collect(Collectors.toList());
    }

    @Override
    public void createDeployment(UserDeploymentDTO deployment) throws ApiException {
        // 资源限制，即使是系统超管也要接受资源限制
        User user = userService.findById(10000001L);
        if (ResourceFormatter.formatCpu(deployment.getCpuLimits()) > user.getCpuLimits()
                || ResourceFormatter.formatMem(deployment.getMemLimits()) > user.getMemLimits()
                || ResourceFormatter.formatGpu(deployment.getGpuLimits()) > user.getGpuLimits()) {
            throw new BaseException("超过了资源限制");
        }

        V1Deployment kubeDeployment = deployment.toKube();
        logger.debug("转换结果:\n{}", kubeDeployment);
        AppsV1Api api = new AppsV1Api();
        // 普通用户不使用前端namespace参数而使用用户组织，此时跨组织的操作会因namespace不一致而被拦截，超管不会被拦截
        String namespace = user.getRoles().contains(KubeConstant.ROLE_SYSTEM_ADMIN) ?
                deployment.getNameSpace() : "ns" + user.getOrganizationId();
        api.createNamespacedDeployment(namespace, kubeDeployment, "true", null, null);
    }
}
