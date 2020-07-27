package com.cgm.kube.account.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author cgm
 */
@Data
public class User {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private Long organizationId;

    private String username;

    private String password;

    private String roles;

    private Integer cpuLimits;

    private Integer memLimits;

    private Integer gpuLimits;

    private Date createTime;

    private Date updateTime;
}
