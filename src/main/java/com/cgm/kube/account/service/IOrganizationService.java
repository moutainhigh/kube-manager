package com.cgm.kube.account.service;

import com.cgm.kube.account.entity.Organization;
import io.kubernetes.client.openapi.ApiException;

public interface IOrganizationService {
    void addOrganization(Organization organization) throws ApiException;
}
