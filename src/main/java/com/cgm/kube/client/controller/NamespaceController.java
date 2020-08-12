package com.cgm.kube.client.controller;

import com.cgm.kube.base.ResponseData;
import com.cgm.kube.client.service.INamespaceService;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@Api(tags = "命名空间")
@RequestMapping("/api/{organizationId}/namespace")
public class NamespaceController {
    @Resource
    private INamespaceService namespaceService;

    @ApiOperation("创建默认命名空间")
    @PostMapping("/init")
    public ResponseData initNamespace(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId)
            throws ApiException {
        namespaceService.initNamespace(organizationId);
        return new ResponseData();
    }
}
