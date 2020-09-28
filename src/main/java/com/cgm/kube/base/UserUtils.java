package com.cgm.kube.base;

import com.cgm.kube.account.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author cgm
 */
public class UserUtils {
    private UserUtils() {

    }

    /**
     * 获取当前请求的用户
     *
     * @return 用户
     */
    public static SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (SysUser) authentication.getPrincipal();
        }
        throw new BaseException(ErrorCode.USER_NOT_LOGIN);
    }

    /**
     * 判断当前请求的用户是否超管
     *
     * @return 是否超管
     */
    public static boolean isSystemAdmin() {
        // 因目前swagger无法登录，如需使用swagger测试，将本方法改为直接返回true
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.isEmpty()) {
            return false;
        }
        for (GrantedAuthority authority : authorities) {
            if (Constant.ROLE_SYSTEM_ADMIN.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
