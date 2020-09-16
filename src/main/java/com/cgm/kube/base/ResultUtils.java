package com.cgm.kube.base;

import com.alibaba.fastjson.JSON;
import io.kubernetes.client.openapi.ApiException;

import java.util.Map;


/**
 * @author cgm
 */
public class ResultUtils {
    private ResultUtils() {

    }

    public static ResponseData success() {
        return new ResponseData(true);
    }

    public static ResponseData success(Object rows) {
        return new ResponseData(ErrorCode.RESULT_SUCCESS, "success", rows, true);
    }

    public static ResponseData created(Object rows) {
        return new ResponseData(ErrorCode.RESULT_CREATED, "success", rows, true);
    }

    public static ResponseData failed(String errorCode) {
        return new ResponseData(errorCode, errorCode, null, false);
    }

    /**
     * 处理通用异常
     *
     * @param exception 通用异常
     * @return 数据返回对象
     */
    public static ResponseData handleBaseException(BaseException exception) {
        return new ResponseData(exception.getCode(), exception.getCode(), exception.getTrace(), false);
    }

    /**
     * 将kubernetes api返回的异常进行转义
     *
     * @param exception api异常
     * @return 数据返回对象
     */
    public static ResponseData handleKubeException(ApiException exception) {
        ResponseData responseData = new ResponseData(false);
        responseData.setCode(handleKubeErrorCode(exception));
        Map<String, Object> map = JSON.parseObject(exception.getResponseBody());
        responseData.setRows(exception.getCode() + " " + exception.getMessage() + ": " + map.get("message"));
        return responseData;
    }

    /**
     * 处理ApiException的错误编码，以便提供友好的错误信息
     *
     * @param exception Kubernetes ApiException
     * @return 字母错误编码
     */
    private static String handleKubeErrorCode(ApiException exception) {
        String errorCode;
        // 之后遇到其他错误码，将持续补充
        switch (exception.getCode()) {
            case Constant.CODE_NOT_FOUND:
                errorCode = ErrorCode.KUBE_API_NOT_FOUND;
                break;
            case Constant.CODE_CONFLICT:
                errorCode = ErrorCode.KUBE_API_CONFLICT;
                break;
            default:
                errorCode = ErrorCode.KUBE_API_EXCEPTION;
        }
        return errorCode;
    }
}
