package com.cgm.kube.client.controller;

import com.cgm.kube.base.ResponseData;
import com.cgm.kube.client.service.IJobService;
import com.cgm.kube.client.dto.UserJobDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@RequestMapping("/api/{organizationId}/job")
public class JobController {
    @Resource
    private IJobService jobService;

    @ApiOperation("查询job")
    @GetMapping("/query")
    public ResponseData getJob(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name) {
        return new ResponseData();
    }

    @ApiOperation("创建job")
    @PostMapping("/create")
    public ResponseData createJob(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "配置", required = true) @RequestBody UserJobDTO job) {
        jobService.createJob(job);
        return new ResponseData();
    }

    @ApiOperation("更新job")
    @PutMapping("/update")
    public ResponseData updateJob(@PathVariable Long organizationId, @RequestBody UserJobDTO job) {
        return new ResponseData();
    }

    @ApiOperation("删除job")
    @DeleteMapping("/delete")
    public ResponseData deleteJob(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name){
        return new ResponseData();
    }
}
