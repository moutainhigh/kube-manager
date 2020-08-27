package com.cgm.kube.client.service;

import io.kubernetes.client.openapi.ApiException;

/**
 * @author cgm
 */
public interface IServiceService {

    /**
     * 创建Service
     *
     * @param namespace   命名空间
     * @param serviceName Service名称
     * @param nameLabel   标签中name的值
     * @param port        集群内部访问端口
     * @param targetPort  容器端口
     * @throws ApiException api异常
     */
    void createService(String namespace, String serviceName, String nameLabel, int port, Integer targetPort)
            throws ApiException;

    /**
     * 删除Service
     *
     * @param namespace   命名空间
     * @param serviceName Service名称
     */
    void deleteService(String namespace, String serviceName);
}
