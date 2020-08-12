package com.cgm.kube.account.controller;

import com.cgm.kube.account.entity.User;
import com.cgm.kube.account.service.IUserService;
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
    private IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById (@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
