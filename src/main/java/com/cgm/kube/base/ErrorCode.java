package com.cgm.kube.base;

/**
 * @author cgm
 */
public class ErrorCode {
    private ErrorCode() {

    }

    public static final String QUERY_FAILED = "查询失败";

    public static final String NO_FIELD = "字段缺失";

    public static final String PROXY_ERROR = "代理异常";

    public static final String NO_FREE_PORT = "未找到空闲端口";

    public static final String PORT_NOT_SPECIFIED = "未指定端口";

    public static final String PERMISSION_DENIED = "没有权限";

    public static final String ORG_ADD_FAILED = "新增组织失败";

}
