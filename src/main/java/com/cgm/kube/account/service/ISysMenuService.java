package com.cgm.kube.account.service;

import com.cgm.kube.account.entity.SysMenu;

import java.util.List;

/**
 * @author cgm
 */
public interface ISysMenuService {

    /**
     * 根据用户ID查询可以访问的菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> listMenuByUserId(Long userId);

    /**
     * 根据角色编码查询可以访问的菜单
     *
     * @param roleCode 角色编码
     * @return 菜单列表
     */
    List<SysMenu> listMenuByRoleCode(String roleCode);
}
