package com.cgm.kube.account.mapper;

import com.cgm.kube.account.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository
public interface UserMapper extends Mapper<User> {
}
