package com.cgm.kube.base;

/**
 * 错误编码
 * 排序：USER->SYS->KUBE->
 * 字段名规则：USER/SYS/第三方系统开头，全部大写下划线
 * 编码规则：user/sys/第三方系统开头，层级用小数点分隔，短语用下划线连接，全部小写
 *
 * @author cgm
 */
public class ErrorCode {

    private ErrorCode() {

    }

    public static final String RESULT_SUCCESS = "result.success";
    public static final String RESULT_CREATED = "result.created";


    public static final String USER_ACCOUNT_EXPIRED = "user.account_expired";
    public static final String USER_CREDENTIALS_ERROR = "user.credentials_error";
    public static final String USER_CREDENTIALS_EXPIRED = "user.credentials_expired";
    public static final String USER_DISABLE = "user.disable";
    public static final String USER_NOT_EXIST = "user.not_exist";
    public static final String USER_LOCKED = "user.locked";
    public static final String USER_NOT_LOGIN = "user.not_login";
    public static final String USER_SESSION_EXPIRED = "user.session_expired";

    public static final String USER_PERMISSION_DENIED = "user.permission_denied";

    public static final String USER_RESOURCE_LIMIT_EXCEEDED = "user.resource_limit_exceeded";

    public static final String SYS_INTERNAL_ERROR = "sys.internal_error";

    public static final String SYS_QUERY_FAILED = "sys.query_failed";
    public static final String SYS_NO_FIELD = "sys.no_field";

    public static final String SYS_PROXY_ERROR = "sys.proxy_error";

    public static final String SYS_NO_FREE_PORT = "sys.no_free_port";

    public static final String SYS_ORG_ADD_FAILED = "sys.org_add_failed";

    public static final String KUBE_API_EXCEPTION = "kube.api.exception";

    public static final String KUBE_API_NOT_FOUND = "kube.api.not_found";
    public static final String KUBE_API_CONFLICT = "kube.api.conflict";

}
