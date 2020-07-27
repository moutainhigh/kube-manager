package com.cgm.kube.client.controller;

import com.cgm.kube.base.ResponseData;
import com.cgm.kube.client.service.ICronJobService;
import com.cgm.kube.client.dto.UserCronJobDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@RequestMapping("/api/{organizationId}/cron-job")
public class CronJobController {
    @Resource
    private ICronJobService cronJobService;

    @ApiOperation("查询cronJob")
    @GetMapping("/query")
    public ResponseData getCronJob(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name) {
        return new ResponseData();
    }

    @ApiOperation("创建cronJob")
    @PostMapping("/create")
    public ResponseData createCronJob(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "配置", required = true) @RequestBody UserCronJobDTO cronJob){
        cronJobService.createCronJob(cronJob);
        return new ResponseData();
    }

    @ApiOperation("更新cronJob")
    @PutMapping("/update")
    public ResponseData updateCronJob(@PathVariable Long organizationId, @RequestBody UserCronJobDTO cronJob) {
        return new ResponseData();
    }

    @ApiOperation("删除cronJob")
    @DeleteMapping("/delete")
    public ResponseData deleteCronJob(
            @ApiParam(value = "租户ID", required = true, example = "10") @PathVariable Long organizationId,
            @ApiParam(value = "名称", required = true, example = "name") @RequestParam String name){
        return new ResponseData();
    }
}
