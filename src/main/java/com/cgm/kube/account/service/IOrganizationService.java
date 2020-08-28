package com.cgm.kube.account.service;

import com.cgm.kube.account.entity.Organization;
import io.kubernetes.client.openapi.ApiException;

/**
 * @author cgm
 */
public interface IOrganizationService {
    /**
     * 添加组织
     *
     * @param organization 组织
     * @throws ApiException api异常
     */
    void addOrganization(Organization organization) throws ApiException;
}
