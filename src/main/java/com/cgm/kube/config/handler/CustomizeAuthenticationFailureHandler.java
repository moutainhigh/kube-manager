package com.cgm.kube.config.handler;

import com.alibaba.fastjson.JSON;
import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hutengfei
 * @author cgm
 */
@Component
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException {
        // 错误代码
        String errorCode;
        if (e instanceof AccountExpiredException) {
            // 账号过期
            errorCode = ErrorCode.USER_ACCOUNT_EXPIRED;
        } else if (e instanceof BadCredentialsException) {
            // 密码错误
            errorCode = ErrorCode.USER_CREDENTIALS_ERROR;
        } else if (e instanceof CredentialsExpiredException) {
            // 密码过期
            errorCode = ErrorCode.USER_CREDENTIALS_EXPIRED;
        } else if (e instanceof DisabledException) {
            // 账号不可用
            errorCode = ErrorCode.USER_DISABLE;
        } else if (e instanceof InternalAuthenticationServiceException) {
            // 用户不存在
            errorCode = ErrorCode.USER_NOT_EXIST;
        } else if (e instanceof LockedException) {
            // 账号锁定
            errorCode = ErrorCode.USER_LOCKED;
        } else {
            // 其他错误
            errorCode = ErrorCode.SYS_OTHER_ERROR;
        }
        String localeMessage = messageSource.getMessage(errorCode, null, RequestContextUtils.getLocale(request));
        ResponseData responseData = new ResponseData(errorCode, localeMessage, null, false);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseData));
    }
}
