package com.cgm.kube.account.mapper;

import com.cgm.kube.account.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cgm
 */
@Repository
public interface SysPermissionMapper {
    /**
     * 查询指定http方法的所有权限
     *
     * @param httpMethod http方法
     * @return 权限列表
     */
    List<SysPermission> listPermissionsByMethod(@Param("httpMethod") String httpMethod);
}
