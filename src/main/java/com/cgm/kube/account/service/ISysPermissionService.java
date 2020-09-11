package com.cgm.kube.account.service;

import java.util.List;

/**
 * @author cgm
 */
public interface ISysPermissionService {
    /**
     * 根据请求查询有权限的角色列表
     *
     * @param requestUrl 请求url
     * @param httpMethod http方法
     * @return 角色列表
     */
    List<String> listPermissionRoles(String requestUrl, String httpMethod);
}