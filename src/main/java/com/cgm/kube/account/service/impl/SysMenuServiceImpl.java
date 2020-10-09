package com.cgm.kube.account.service.impl;

import com.cgm.kube.account.entity.SysMenu;
import com.cgm.kube.account.mapper.SysMenuMapper;
import com.cgm.kube.account.service.ISysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cgm
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listMenuByUserId(Long userId) {
        return sysMenuMapper.listMenuByUserId(userId);
    }

    @Override
    public List<SysMenu> listMenuByRoleCode(String roleCode) {
        return sysMenuMapper.listMenuByRoleCode(roleCode);
    }
}
