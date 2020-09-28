package com.cgm.kube.account.mapper;

import com.cgm.kube.account.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author cgm
 */
@Repository
public interface SysUserMapper extends Mapper<SysUser> {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser selectByUsername(@Param("username") String username);
}
