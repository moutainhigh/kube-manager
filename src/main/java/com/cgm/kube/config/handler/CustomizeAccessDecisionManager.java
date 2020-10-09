package com.cgm.kube.config.handler;

import com.cgm.kube.base.Constant;
import com.cgm.kube.base.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 访问决策管理器
 *
 * @author Hutengfei
 * @author cgm
 */
@Component
@Slf4j
public class CustomizeAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {
        for (ConfigAttribute ca : collection) {
            // 当前请求需要的权限
            String needRole = ca.getAttribute();
            if (Constant.ROLE_ANONYMOUS.equals(needRole)) {
                // 当需要的权限为匿名角色，直接返回
                return;
            }
            // 当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                log.debug(authority.getAuthority());
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException(ErrorCode.USER_PERMISSION_DENIED);
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
