package com.cgm.kube.account.service.impl;

import com.cgm.kube.account.entity.User;
import com.cgm.kube.account.mapper.UserMapper;
import com.cgm.kube.account.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author cgm
 */
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User findById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
