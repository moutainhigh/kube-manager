package com.cgm.kube.config.handler;

import com.cgm.kube.account.service.ISysPermissionService;
import com.cgm.kube.base.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Hutengfei
 * @author cgm
 */
@Component
public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private ISysPermissionService sysPermissionService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {
        // 获取请求地址和http方法
        FilterInvocation filterInvocation = ((FilterInvocation) o);
        String requestUrl = filterInvocation.getRequest().getRequestURI();
        String httpMethod = filterInvocation.getRequest().getMethod();

        // 查询哪些角色具有权限，空列表表示不拦截
        List<String> allowRoles = sysPermissionService.listPermissionRoles(requestUrl, httpMethod);
        if (allowRoles.isEmpty()) {
            // 请求路径没有配置权限，表明该请求接口可以被任何登录用户访问，匿名用户不可访问
            return SecurityConfig.createList(Constant.ROLE_USER);
        }
        String[] attributes = new String[allowRoles.size()];
        for (int i = 0; i < allowRoles.size(); i++) {
            attributes[i] = allowRoles.get(i);
        }
        return SecurityConfig.createList(attributes);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
