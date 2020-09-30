package com.cgm.kube.account.service.impl;

import com.cgm.kube.account.entity.SysRole;
import com.cgm.kube.account.entity.SysUser;
import com.cgm.kube.account.mapper.SysRoleMapper;
import com.cgm.kube.account.mapper.SysUserMapper;
import com.cgm.kube.account.service.ISysUserService;
import com.cgm.kube.base.Constant;
import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.base.UserUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;


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
    public SysUser createUser(SysUser user) {
        // 仅允许超管创建，后续会允许组织管理员
        Assert.isTrue(UserUtils.isSystemAdmin(), ErrorCode.USER_PERMISSION_DENIED);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        sysUserMapper.insertSelective(user);
        return user;
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

        // 查询用户角色，查不到的默认赋予"USER"角色
        List<SysRole> roleList = sysRoleMapper.listRolesByUserId(user.getId());
        roleList = roleList.isEmpty() ? Collections.singletonList(new SysRole(Constant.ROLE_USER)) : roleList;
        user.setRoles(roleList);
        return user;
    }
}
