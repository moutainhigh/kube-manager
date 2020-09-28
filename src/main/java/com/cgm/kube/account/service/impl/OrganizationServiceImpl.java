package com.cgm.kube.account.service.impl;

import com.cgm.kube.account.entity.Organization;
import com.cgm.kube.account.mapper.OrganizationMapper;
import com.cgm.kube.account.service.IOrganizationService;
import com.cgm.kube.account.service.ISysUserService;
import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.base.UserUtils;
import com.cgm.kube.client.service.INamespaceService;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@Service
public class OrganizationServiceImpl implements IOrganizationService {
    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private INamespaceService namespaceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrganization(Organization organization) throws ApiException {
        Assert.isTrue(UserUtils.isSystemAdmin(), ErrorCode.USER_PERMISSION_DENIED);

        organizationMapper.insertSelective(organization);
        Assert.notNull(organization.getId(), ErrorCode.SYS_ORG_ADD_FAILED);
        namespaceService.initNamespace(organization.getId());
    }
}
