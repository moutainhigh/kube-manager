package com.cgm.kube.account.controller;


import com.cgm.kube.account.entity.Organization;
import com.cgm.kube.account.service.IOrganizationService;
import com.cgm.kube.base.ResponseData;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
    @Resource
    private IOrganizationService organizationService;

    @ApiOperation("添加组织")
    @PostMapping
    public ResponseData addOrganization(Organization organization) throws ApiException {
        organizationService.addOrganization(organization);
        return new ResponseData(organization);
    }
}
