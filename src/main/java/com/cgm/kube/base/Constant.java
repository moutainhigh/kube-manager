package com.cgm.kube.base;

/**
 * @author cgm
 */
public class Constant {
    private Constant() {
    }

    /**
     * 系统超级管理员角色，其他角色不得包含"system_admin"
     */
    public static final String ROLE_SYSTEM_ADMIN = "SYS_ADMIN";

    public static final String RESOURCE_CPU = "cpu";
    public static final String RESOURCE_MEM = "memory";
    public static final String RESOURCE_GPU = "nvidia.com/gpu";

    public static final String RESOURCE_NOT_SET = "未设置";
    public static final String RESOURCE_GPU_DEFAULT = "0";

    public static final String IMAGE_TYPE_TERMINAL = "terminal";

    /**
     * 在k8s状态中，以字符串“True”表示真
     */
    public static final String TRUE = "True";
    public static final String FALSE = "False";

    public static final String STATUS_READY = "Ready";
    public static final String STATUS_STARTING = "Starting";
    public static final String STATUS_FAILED = "Failed";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_TYPE_HTML = "text/html";

    public static final String CHARSET_UTF8 = "utf-8";
}
