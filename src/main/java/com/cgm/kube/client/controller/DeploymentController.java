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
@Api(tags = "Deployment")
@RequestMapping("/api/deployment")
public class DeploymentController extends BaseController {

    @Resource
    private IDeploymentService deploymentService;

    @ApiOperation("按名称查询Deployment")
    @GetMapping("/{name}")
    public ResponseData getDeploymentByName(
            @ApiParam(value = "名称", required = true, example = "name")
            @PathVariable String name) throws ApiException {
        return new ResponseData(deploymentService.getDeploymentByName(name));
    }

    @ApiOperation("查询Deployment列表")
    @GetMapping
    public ResponseData listDeployment(DeploymentParamDTO paramDTO) throws ApiException {
        return new ResponseData(deploymentService.listDeployment(paramDTO));
    }

    @ApiOperation("创建Deployment")
    @PostMapping
    public ResponseData createDeployment(@RequestBody UserDeploymentDTO deployment) throws ApiException, IOException {
        deploymentService.createDeployment(deployment);
        return new ResponseData();
    }

    @ApiOperation("更新Deployment")
    @PutMapping
    public ResponseData updateDeployment(@RequestBody UserDeploymentDTO deployment) throws ApiException {
        deploymentService.updateDeployment(deployment);
        return new ResponseData();
    }

    @ApiOperation("缩放Deployment")
    @PatchMapping("/scale")
    public ResponseData patchDeploymentScale(@RequestBody UserDeploymentDTO deployment) throws ApiException {
        deploymentService.patchDeploymentScale(deployment);
        return new ResponseData();
    }

    @ApiOperation("删除Deployment")
    @DeleteMapping
    public ResponseData deleteDeployment(
            @ApiParam(value = "命名空间", required = true, example = "ns100001") @RequestParam String namespace,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name) throws ApiException {
        deploymentService.deleteDeployment(namespace, name);
        return new ResponseData();
    }


}
