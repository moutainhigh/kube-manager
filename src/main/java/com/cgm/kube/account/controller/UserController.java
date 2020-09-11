package com.cgm.kube.account.controller;

import com.cgm.kube.account.entity.SysUser;
import com.cgm.kube.account.service.ISysUserService;
import io.swagger.annotations.Api;
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

    @GetMapping("/{id}")
    public ResponseEntity<SysUser> findById (@PathVariable Long id) {
        return ResponseEntity.ok(sysUserService.getById(id));
    }
}
