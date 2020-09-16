package com.cgm.kube.config.handler;

import com.alibaba.fastjson.JSON;
import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限拒绝处理逻辑
 *
 * @author Hutengfei
 * @author cgm
 */
@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private MessageSource messageSource;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String code = ErrorCode.USER_PERMISSION_DENIED;
        String localeMessage = messageSource.getMessage(code, null, RequestContextUtils.getLocale(request));
        ResponseData responseData = new ResponseData(code, localeMessage, null, false);

        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseData));
    }
}
