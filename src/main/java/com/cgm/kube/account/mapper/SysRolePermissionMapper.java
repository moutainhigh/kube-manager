package com.cgm.kube.account.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cgm
 */
@Repository
public interface SysRolePermissionMapper {
    /**
     * 根据权限id列出具有该权限的角色
     *
     * @param permissionId 权限id
     * @return 角色代码列表
     */
    List<String> listRolesByPermissionId(int permissionId);
}
