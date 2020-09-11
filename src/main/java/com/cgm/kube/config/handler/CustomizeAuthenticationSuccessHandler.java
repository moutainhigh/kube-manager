package com.cgm.kube.config.handler;

import com.alibaba.fastjson.JSON;
import com.cgm.kube.account.entity.SysUser;
import com.cgm.kube.account.service.ISysUserService;
import com.cgm.kube.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author Hutengfei
 * @author cgm
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        SysUser userDetails = (SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(userDetails.getUsername());
        Date now = new Date();
        sysUser.setLastLoginTime(now);
        sysUser.setUpdateTime(now);
        sysUser.setUpdatedBy(sysUser.getId());
        sysUserService.updateUser(sysUser);

        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展

        //返回json数据
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(new ResponseData(true)));
    }
}
