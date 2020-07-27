package com.cgm.kube.client.controller;

import com.cgm.kube.base.ResponseData;
import com.cgm.kube.client.service.IPodService;
import com.cgm.kube.client.dto.UserPodDTO;
import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@RequestMapping("/api/{organizationId}/pod")
public class PodController {
    @Resource
    private IPodService podService;

    @ApiOperation("查询pod")
    @GetMapping("/query")
    public ResponseData getPod(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name) {
        return new ResponseData();
    }

    @ApiOperation("创建pod")
    @PostMapping("/create")
    public ResponseData createPod(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "配置", required = true) @RequestBody UserPodDTO pod) throws ApiException {
        podService.createPod(pod);
        return new ResponseData();
    }

    @ApiOperation("更新pod")
    @PutMapping("/update")
    public ResponseData updatePod(@PathVariable Long organizationId, @RequestBody UserPodDTO pod) {
        return new ResponseData();
    }

    @ApiOperation("删除pod")
    @DeleteMapping("/delete")
    public ResponseData deletePod(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name){
        return new ResponseData();
    }
}
