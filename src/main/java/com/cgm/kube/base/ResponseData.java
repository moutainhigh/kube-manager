package com.cgm.kube.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 数据返回对象
 * @author cgm
 */
public class ResponseData {

    /**
     * 状态编码
     */
    @JsonInclude(Include.NON_NULL)
    private String code;

    /**
     * 提示信息
     */
    @JsonInclude(Include.NON_NULL)
    private String message;

    /**
     * 数据
     */
    @JsonInclude(Include.NON_NULL)
    private Object result;

    /**
     * 成功标识
     */
    private boolean success = true;


    public ResponseData() {
    }

    public ResponseData(boolean success) {
        setSuccess(success);
    }

    public ResponseData(Object result) {
        this(true);
        setResult(result);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
