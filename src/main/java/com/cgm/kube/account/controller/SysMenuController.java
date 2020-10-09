package com.cgm.kube.account.controller;

import com.cgm.kube.account.entity.SysMenu;
import com.cgm.kube.account.service.ISysMenuService;
import com.cgm.kube.base.ResponseData;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@Api(tags = "菜单")
@RequestMapping("/api/menu")
public class SysMenuController {
    @Resource
    private ISysMenuService sysMenuService;

    @GetMapping("/user/{userId}")
    public ResponseData listMenuByUserId(@PathVariable Long userId) {
        List<SysMenu> menuList = sysMenuService.listMenuByUserId(userId);
        return new ResponseData(menuList);
    }

    @GetMapping("/role/{roleCode}")
    public ResponseData listMenuByRoleCode(@PathVariable String roleCode) {
        List<SysMenu> menuList = sysMenuService.listMenuByRoleCode(roleCode);
        return new ResponseData(menuList);
    }

}
