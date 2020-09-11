package com.cgm.kube.account.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author cgm
 */
@Data
public class SysRolePermission {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private Integer roleId;

    private Integer permissionId;

    private Date createTime;

    private Date updateTime;
}
