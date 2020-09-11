package com.cgm.kube.account.service.impl;

import com.cgm.kube.account.entity.SysPermission;
import com.cgm.kube.account.mapper.SysPermissionMapper;
import com.cgm.kube.account.mapper.SysRolePermissionMapper;
import com.cgm.kube.account.service.ISysPermissionService;
import com.cgm.kube.base.Constant;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 对于接口权限采用宽松策略，只拦截经过配置的请求，未配置的全部通过
 *
 * @author cgm
 */
@Service
public class SysPermissionServiceImpl implements ISysPermissionService {
    private static final String API_NORMAL = "/api/";
    private static final String API_PUBLIC = "/api/public/";

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public List<String> listPermissionRoles(String requestUrl, String httpMethod) {
        // 仅对/api开头的接口进行过滤，标记了/public的接口不拦截
        if (!requestUrl.startsWith(API_NORMAL) || requestUrl.startsWith(API_PUBLIC)) {
            return new ArrayList<>();
        }

        // 查询数据库中相同http方法所有的权限
        List<SysPermission> permissionList = sysPermissionMapper.listPermissionsByMethod(httpMethod);
        AntPathMatcher matcher = new AntPathMatcher();
        for (SysPermission permission : permissionList) {
            if (matcher.match(permission.getPath(), requestUrl)) {
                // 没有为该路径配置角色时，默认仅系统超管可访问
                List<String> allowRoles = sysRolePermissionMapper.listRolesByPermissionId(permission.getId());
                if (allowRoles.isEmpty()) {
                    allowRoles.add(Constant.ROLE_SYSTEM_ADMIN);
                }
                return allowRoles;
            }
        }

        // 没有匹配到则不拦截，返回空列表
        return new ArrayList<>();
    }
}
