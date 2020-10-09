package com.cgm.kube.account.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author cgm
 */
@Data
public class SysMenu {

    @Id
    @GeneratedValue(generator = "JDBC")
    private int id;

    private String code;

    private String path;

    private Date createTime;

    private Date updateTime;

    private String description;

    public SysMenu() {
    }

    public SysMenu(String code) {
        this.code = code;
    }
}
