package com.cgm.kube.account.service.impl;

import com.cgm.kube.account.entity.SysUser;
import com.cgm.kube.account.mapper.SysRoleMapper;
import com.cgm.kube.account.mapper.SysUserMapper;
import com.cgm.kube.account.service.ISysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author cgm
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public void updateUser(SysUser user) {
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser user = this.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        user.setRoles(sysRoleMapper.listRolesByUserId(user.getId()));
        return user;
    }
}
