package com.cgm.kube.client.controller;

import com.cgm.kube.base.BaseController;
import com.cgm.kube.base.ResponseData;
import com.cgm.kube.client.dto.DeploymentParamDTO;
import com.cgm.kube.client.service.IDeploymentService;
import com.cgm.kube.client.dto.UserDeploymentDTO;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@Api(tags = "deployment")
@RequestMapping("/api/{organizationId}/deployment")
public class DeploymentController extends BaseController {

    @Resource
    private IDeploymentService deploymentService;

    @ApiOperation("查询deployment")
    @GetMapping("/{name}")
    public ResponseData getDeploymentByName(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @PathVariable String name) throws ApiException {
        return new ResponseData(deploymentService.getDeploymentByName(organizationId, name));
    }

    @ApiOperation("查询deployment列表")
    @GetMapping
    public ResponseData listDeployment(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            DeploymentParamDTO paramDTO) throws ApiException {
        return new ResponseData(deploymentService.listDeployment(organizationId, paramDTO));
    }

    @ApiOperation("创建deployment")
    @PostMapping
    public ResponseData createDeployment(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @RequestBody UserDeploymentDTO deployment) throws ApiException, IOException {
        deploymentService.createDeployment(deployment);
        return new ResponseData();
    }

    @ApiOperation("更新deployment")
    @PutMapping
    public ResponseData updateDeployment(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @RequestBody UserDeploymentDTO deployment) throws ApiException {
        deploymentService.updateDeployment(deployment);
        return new ResponseData();
    }

    @ApiOperation("缩放deployment")
    @PatchMapping("/scale")
    public ResponseData patchDeploymentScale(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @RequestBody UserDeploymentDTO deployment) throws ApiException {
        deploymentService.patchDeploymentScale(deployment);
        return new ResponseData();
    }

    @ApiOperation("删除deployment")
    @DeleteMapping
    public ResponseData deleteDeployment(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name) throws ApiException {
        deploymentService.deleteDeployment(organizationId, name);
        return new ResponseData();
    }


}
