package com.cgm.kube.base;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import io.kubernetes.client.openapi.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * @author cgm
 */
@RestController
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private MessageSource messageSource;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * 处理控制层所有异常
     *
     * @param exception 未捕获的异常
     * @param request   HttpServletRequest
     * @return ResponseData(BaseException 被处理) 或者 ModelAndView(其他 Exception
     * ,500错误)
     */
    @ExceptionHandler(value = {Exception.class})
    public Object exceptionHandler(Exception exception, HttpServletRequest request) {
        logger.error(exception.getMessage(), exception);
        Throwable thr = Throwables.getRootCause(exception);
        if (notModelAndView(request)) {
            ResponseData res = new ResponseData(false);
            if (thr instanceof BaseException) {
                // 通用异常处理
                BaseException be = (BaseException) thr;
                Locale locale = RequestContextUtils.getLocale(request);
                String message = messageSource.getMessage(be.getCode(), null, locale);
                res.setCode(be.getCode());
                res.setMessage(message);
            } else if (thr instanceof ApiException) {
                // 来自k8s api-server的异常
                ApiException ae = (ApiException) thr;
                res.setCode(String.valueOf(ae.getCode()));
                Map<String, Object> map = JSON.parseObject(ae.getResponseBody());
                res.setMessage(ae.getMessage() + ": " + map.get("message"));
            } else {
                res.setMessage(thr.toString());
            }
            return res;
        } else {
            // 返回mv，本项目暂不存在mv
            ModelAndView view = new ModelAndView("500");
            if (thr instanceof BaseException) {
                BaseException be = (BaseException) thr;
                Locale locale = RequestContextUtils.getLocale(request);
                String message = messageSource.getMessage(be.getCode(), null, locale);
                view.addObject("message", message);
            }
            return view;
        }
    }


    private boolean notModelAndView(HttpServletRequest request) {
        String xr = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xr)) {
            return true;
        }
        if (request.getRequestURI().contains("/api/")) {
            return true;
        }
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }
}
