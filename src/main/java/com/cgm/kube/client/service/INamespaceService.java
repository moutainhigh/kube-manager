package com.cgm.kube.client.service;

import io.kubernetes.client.openapi.ApiException;

public interface INamespaceService {
    void initNamespace(Long organizationId) throws ApiException;
}
