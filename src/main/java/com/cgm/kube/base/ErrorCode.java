package com.cgm.kube.base;

/**
 * @author cgm
 */
public class ErrorCode {
    public static final String DEFAULT_FAIL = "失败";

    public static final String USER_ACCOUNT_EXPIRED = "账号过期";
    public static final String USER_CREDENTIALS_ERROR = "用户名或密码有误";
    public static final String USER_CREDENTIALS_EXPIRED = "密码过期";
    public static final String USER_DISABLE = "账号不可用";
    public static final String USER_NOT_EXIST = "账号不存在";
    public static final String USER_LOCKED = "账号已锁定";
    public static final String USER_NOT_LOGIN = "未登录";
    public static final String USER_SESSION_EXPIRED = "登录失效";

    public static final String QUERY_FAILED = "查询失败";

    public static final String NO_FIELD = "字段缺失";

    public static final String PROXY_ERROR = "代理异常";

    public static final String NO_FREE_PORT = "未找到空闲端口";

    public static final String PORT_NOT_SPECIFIED = "未指定端口";

    public static final String PERMISSION_DENIED = "没有权限";

    public static final String ORG_ADD_FAILED = "新增组织失败";

    private ErrorCode() {

    }
}
