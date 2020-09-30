package com.cgm.kube.account.controller;

import com.cgm.kube.account.entity.SysUser;
import com.cgm.kube.account.service.ISysUserService;
import com.cgm.kube.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@Api(tags = "用户")
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private ISysUserService sysUserService;

    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    public ResponseEntity<SysUser> findById (@PathVariable Long id) {
        return ResponseEntity.ok(sysUserService.getById(id));
    }

    @ApiOperation("创建用户")
    @PostMapping()
    public ResponseData createUser(@RequestBody SysUser user) {
        sysUserService.createUser(user);
        return new ResponseData(user);
    }
}
