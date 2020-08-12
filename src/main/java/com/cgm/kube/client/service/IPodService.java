package com.cgm.kube.client.service;

import com.cgm.kube.client.dto.UserPodDTO;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;

/**
 * @author cgm
 */
public interface IPodService {
    /**
     * 查询指定deployment的pod
     *
     * @param organizationId 组织ID
     * @param name           deployment名称
     * @return pod列表
     * @throws ApiException api异常
     */
    List<UserPodDTO> listDeploymentPod(Long organizationId, String name) throws ApiException;

    /**
     * 创建pod
     *
     * @param pod pod
     * @throws ApiException api异常
     */
    void createPod(UserPodDTO pod) throws ApiException;
}
