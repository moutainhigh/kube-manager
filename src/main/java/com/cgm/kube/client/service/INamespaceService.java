package com.cgm.kube.client.service;

import io.kubernetes.client.openapi.ApiException;

/**
 * @author angel
 */
public interface INamespaceService {
    /**
     * 根据组织ID初始化命名空间
     *
     * @param organizationId 组织ID
     * @throws ApiException api异常
     */
    void initNamespace(Long organizationId) throws ApiException;

    /**
     * 初始化命名空间，不对外暴露
     *
     * @param name 命名空间名称
     * @throws ApiException api异常
     */
    void initNamespace(String name) throws ApiException;
}
