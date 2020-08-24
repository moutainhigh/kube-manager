package com.cgm.kube.client.service;

import io.kubernetes.client.openapi.ApiException;

/**
 * @author cgm
 */
public interface IServiceService {

    /**
     * 创建Service
     *
     * @param namespace 命名空间
     * @param nameLabel 标签中name的值
     * @param port      容器端口
     * @throws ApiException api异常
     */
    void createService(String namespace, String nameLabel, int port) throws ApiException;

}
