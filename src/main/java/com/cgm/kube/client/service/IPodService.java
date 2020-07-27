package com.cgm.kube.client.service;

import com.cgm.kube.client.dto.UserPodDTO;
import io.kubernetes.client.openapi.ApiException;

/**
 * @author cgm
 */
public interface IPodService {
    /**
     * 创建pod
     *
     * @param pod pod
     * @throws ApiException api异常
     */
    void createPod(UserPodDTO pod) throws ApiException;
}
