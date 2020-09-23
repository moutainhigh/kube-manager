package com.cgm.kube.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

/**
 * 数据返回对象
 *
 * @author cgm
 */
public class ResponseData {

    private static final String PAGE_CLASS_NAME = "com.github.pagehelper.Page";

    /**
     * 状态编码
     */
    @JsonInclude(Include.NON_NULL)
    private String code;

    /**
     * 提示信息
     * 错误消息直接在前端展示，需要让用户可读
     */
    @JsonInclude(Include.NON_NULL)
    private String message;

    /**
     * 数据
     * 发生异常时，返回错误描述，需要开发人员可读
     * 调用外部接口时，使用外部接口的错误信息
     */
    @JsonInclude(Include.NON_NULL)
    private Object rows;

    /**
     * 异常堆栈
     */
    @JsonInclude(Include.NON_NULL)
    private List<String> trace;

    /**
     * 成功标识
     */
    private boolean success = true;

    /**
     * 总数
     */
    @JsonInclude(Include.NON_NULL)
    private Integer total;

    public ResponseData() {
    }

    public ResponseData(boolean success) {
        setSuccess(success);
    }

    public ResponseData(Object object) {
        this(true);
        setRows(object);
    }

    public ResponseData(boolean success, String message) {
        this(success);
        setMessage(message);
    }

    public ResponseData(String code, String message, Object rows, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
        setRows(rows);
    }

    public ResponseData(String code, String message, Object rows, List<String> trace) {
        this.code = code;
        this.message = message;
        this.success = false;
        setRows(rows);
        this.trace = trace;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getRows() {
        return rows;
    }

    public List<String> getTrace() {
        return trace;
    }

    public Integer getTotal() {
        return total;
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

    public void setRows(Object rows) {
        this.rows = rows;
        if (rows == null) {
            this.setTotal(0);
            return;
        }
        try {
            if (rows instanceof List) {
                setTotal((Integer) rows.getClass().getMethod("size").invoke(rows));
            } else if (PAGE_CLASS_NAME.equals(rows.getClass().getCanonicalName())) {
                // 暂未引入pageHelper，以上表达式恒为false
                setTotal((Integer) rows.getClass().getDeclaredMethod("getTotal").invoke(rows));
            } else {
                setTotal(1);
            }
        } catch (Exception e) {
            throw new BaseException("Get page total failed!", e);
        }
    }

    public void setTrace(List<String> trace) {
        this.trace = trace;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
