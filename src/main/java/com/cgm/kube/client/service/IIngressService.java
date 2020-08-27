package com.cgm.kube.client.service;

import io.kubernetes.client.openapi.ApiException;

/**
 * @author cgm
 */
public interface IIngressService {
    /**
     * 追加Ingress配置
     *
     * @param namespace   命名空间
     * @param path        访问路径
     * @param serviceName Service名称
     * @param servicePort Service端口
     * @throws ApiException api异常
     */
    void appendIngress(String namespace, String path, String serviceName, Integer servicePort) throws ApiException;
}
