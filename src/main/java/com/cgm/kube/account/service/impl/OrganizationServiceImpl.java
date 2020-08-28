package com.cgm.kube.account.service.impl;

import com.cgm.kube.account.entity.Organization;
import com.cgm.kube.account.entity.User;
import com.cgm.kube.account.mapper.OrganizationMapper;
import com.cgm.kube.account.service.IOrganizationService;
import com.cgm.kube.account.service.IUserService;
import com.cgm.kube.base.Constant;
import com.cgm.kube.base.ErrorCode;
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
    private IUserService userService;
    @Resource
    private INamespaceService namespaceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrganization(Organization organization) throws ApiException {
        User user = userService.findById(10000001L);
        Assert.isTrue(user.getRoles().contains(Constant.ROLE_SYSTEM_ADMIN), ErrorCode.PERMISSION_DENIED);

        organizationMapper.insertSelective(organization);
        Assert.notNull(organization.getId(), ErrorCode.ORG_ADD_FAILED);
        namespaceService.initNamespace(organization.getId());
    }
}
