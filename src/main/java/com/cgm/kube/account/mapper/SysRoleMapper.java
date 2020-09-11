package com.cgm.kube.account.mapper;

import com.cgm.kube.account.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author cgm
 */
@Repository
public interface SysRoleMapper extends Mapper<SysRole> {
    /**
     * 根据用户id查询用户具有的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<SysRole> listRolesByUserId(@Param("userId") Long userId);
}
