package com.cgm.kube.account.service;

import com.cgm.kube.account.entity.User;

public interface IUserService {
    /**
     * 根据用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    User findById (Long id);
}
