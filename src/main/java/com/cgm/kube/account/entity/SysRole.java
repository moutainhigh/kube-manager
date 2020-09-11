package com.cgm.kube.account.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author cgm
 */
@Data
public class SysRole implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String code;

    private String name;

    private Date createTime;

    private Date updateTime;

    public SysRole() {

    }

    public SysRole(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysRole role = (SysRole) o;
        return Objects.equals(this.code, role.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
