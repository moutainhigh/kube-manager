package com.cgm.kube.client.service;

import io.kubernetes.client.openapi.ApiException;

/**
 * @author cgm
 */
public interface IIngressService {
    /**
     * 创建Ingress配置
     *
     * @param namespace   命名空间
     * @param uid         Deployment uid
     * @param serviceName Service名称
     * @param image       镜像
     * @param servicePort Service端口
     * @throws ApiException api异常
     */
    void createIngress(String namespace, String uid, String serviceName, String image, Integer servicePort) throws ApiException;

    /**
     * 追加Ingress配置，没有时新建
     *
     * @param namespace   命名空间
     * @param uid         Deployment uid
     * @param serviceName Service名称
     * @param image       镜像
     * @param servicePort Service端口
     * @throws ApiException api异常
     */
    void appendIngress(String namespace, String uid, String serviceName, String image, Integer servicePort) throws ApiException;
}
