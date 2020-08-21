package com.cgm.kube.account.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author cgm
 */
@Data
public class Organization {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String name;

    private String englishName;

    private String shortName;

    private Date createTime;

    private Date updateTime;
}
