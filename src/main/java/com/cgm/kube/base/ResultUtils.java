package com.cgm.kube.base;

import com.alibaba.fastjson.JSON;
import io.kubernetes.client.openapi.ApiException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author cgm
 */
public class ResultUtils {
    private static final String TRACE_BREAK = "\r\n\t";

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
     * 处理通用异常
     *
     * @param exception 通用异常
     * @return 数据返回对象
     */
    public static ResponseData handleBaseException(BaseException exception) {
        String[] traceArray = exception.getTrace().split(TRACE_BREAK);

        // 按包名过滤需要的堆栈信息, startsWith加速匹配，offset为开头的"at "长度
        String basePackage = ResultUtils.class.getPackage().getName().replace(".base", "");
        List<String> traceList = Arrays.stream(traceArray)
                .filter(e -> e.startsWith(basePackage, 3) || e.startsWith(basePackage))
                .collect(Collectors.toList());
        return new ResponseData(exception.getCode(), null, null, traceList);
    }

    /**
     * 处理其他异常
     *
     * @param exception 异常
     * @return 数据返回对象
     */
    public static ResponseData handleOtherException(Throwable exception) {
        // 堆栈转字符串数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        exception.printStackTrace(ps);
        ps.flush();
        String trace = new String(outputStream.toByteArray());
        String[] traceArray = trace.split(TRACE_BREAK);

        // 按包名过滤需要的堆栈信息, startsWith加速匹配，offset为开头的"at "长度
        String basePackage = ResultUtils.class.getPackage().getName().replace(".base", "");
        List<String> traceList = Arrays.stream(traceArray)
                .filter(e -> e.startsWith(basePackage, 3) || e.startsWith(basePackage))
                .collect(Collectors.toList());
        return new ResponseData(ErrorCode.SYS_INTERNAL_ERROR, null, exception.getMessage(), traceList);
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
