package com.cgm.kube.account.service;

import com.cgm.kube.account.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author cgm
 */
public interface ISysUserService extends UserDetailsService {
    /**
     * 根据用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    SysUser getById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser getByUsername(String username);

    /**
     * 更新用户
     *
     * @param user 用户
     */
    void updateUser(SysUser user);
}
