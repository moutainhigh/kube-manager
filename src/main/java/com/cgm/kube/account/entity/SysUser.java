package com.cgm.kube.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

/**
 * 用户实体类
 * 因实现了UserDetails接口，会与lombok发生冲突，所以不使用lombok注解
 *
 * @author cgm
 */
public class SysUser implements UserDetails, Serializable {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private Long organizationId;

    private String username;

    private String password;

    private Integer cpuLimits;

    private Integer memLimits;

    private Integer gpuCountLimits;

    private Integer gpuMemLimits;

    private Boolean enabled;

    private Date createTime;

    private Date updateTime;

    private Date lastLoginTime;

    private Long updatedBy;

    @Transient
    private transient List<SysRole> roles;

    public SysUser() {
    }

    public SysUser(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCpuLimits() {
        return cpuLimits;
    }

    public void setCpuLimits(Integer cpuLimits) {
        this.cpuLimits = cpuLimits;
    }

    public Integer getMemLimits() {
        return memLimits;
    }

    public void setMemLimits(Integer memLimits) {
        this.memLimits = memLimits;
    }

    public Integer getGpuCountLimits() {
        return gpuCountLimits;
    }

    public void setGpuCountLimits(Integer gpuCountLimits) {
        this.gpuCountLimits = gpuCountLimits;
    }

    public Integer getGpuMemLimits() {
        return gpuMemLimits;
    }

    public void setGpuMemLimits(Integer gpuMemLimits) {
        this.gpuMemLimits = gpuMemLimits;
    }

    public void setEnable(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysUser user = (SysUser) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
