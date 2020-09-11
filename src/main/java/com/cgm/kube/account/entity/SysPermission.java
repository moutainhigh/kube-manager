package com.cgm.kube.account.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author cgm
 */
@Data
public class SysPermission {
    @Id
    @GeneratedValue(generator = "JDBC")
    private int id;

    private String code;

    private String path;

    private String method;

    private Date createTime;

    private Date updateTime;

    private String description;

    public SysPermission() {
    }

    public SysPermission(String code) {
        this.code = code;
    }
}
