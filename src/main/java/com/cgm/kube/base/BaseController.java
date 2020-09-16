package com.cgm.kube.base;

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

/**
 * @author cgm
 */
@RestController
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private MessageSource messageSource;

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
        Locale locale = RequestContextUtils.getLocale(request);

        if (isRestRequest(request)) {
            ResponseData res = new ResponseData(false);
            if (thr instanceof BaseException) {
                // 通用异常处理
                res = ResultUtils.handleBaseException((BaseException) thr);
            } else if (thr instanceof ApiException) {
                // 来自k8s api-server的异常
                res = ResultUtils.handleKubeException((ApiException) thr);
            } else {
                // 其他异常
                res.setMessage(thr.getMessage());
            }

            // 多语言提示
            res.setMessage(messageSource.getMessage(res.getCode(), null, locale));
            return res;
        } else {
            // 返回mv，本项目暂不存在mv
            ModelAndView view = new ModelAndView("500");
            if (thr instanceof BaseException) {
                BaseException be = (BaseException) thr;
                String message = messageSource.getMessage(be.getCode(), null, locale);
                view.addObject("message", message);
            }
            return view;
        }
    }


    private boolean isRestRequest(HttpServletRequest request) {
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
