package com.cgm.kube.client.service.impl;

import com.cgm.kube.account.entity.User;
import com.cgm.kube.account.service.IUserService;
import com.cgm.kube.client.constant.KubeConstant;
import com.cgm.kube.client.service.INamespaceService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Service
public class NamespaceServiceImpl implements INamespaceService {
    @Resource
    private IUserService userService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initNamespace(Long organizationId) throws ApiException {
        // 先判断当前登录用户是否是超级管理员
        User user = userService.findById(10000001L);
        Assert.isTrue(user.getRoles().contains(KubeConstant.ROLE_SYSTEM_ADMIN), "无权操作！");

        String name = "ns" + organizationId;
        this.initNamespace(name);
    }

    @Override
    public void initNamespace(String name) throws ApiException {
        CoreV1Api api = new CoreV1Api();
        V1ObjectMeta meta = new V1ObjectMeta()
                .name(name);
        V1Namespace namespace = new V1Namespace()
                .apiVersion("v1")
                .kind("Namespace")
                .metadata(meta);
        api.createNamespace(namespace, "true", null, null);

    }
}
