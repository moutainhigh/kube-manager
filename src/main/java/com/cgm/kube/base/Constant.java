package com.cgm.kube.base;

/**
 * @author cgm
 */
public class Constant {
    private Constant() {
    }

    /**
     * 系统超级管理员角色，为避免误用，其他角色不得包含"SYS_ADMIN"
     */
    public static final String ROLE_SYSTEM_ADMIN = "SYS_ADMIN";

    /**
     * 普通用户角色，所有登录用户都具有此角色，是除了匿名用户的最小权限集
     */
    public static final String ROLE_USER = "USER";

    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_CONFLICT = 409;

    public static final String RESOURCE_CPU = "cpu";
    public static final String RESOURCE_MEM = "memory";
    public static final String RESOURCE_NVIDIA_GPU = "nvidia.com/gpu";
    public static final String RESOURCE_ALI_GPU_MEM = "aliyun.com/gpu-mem";
    public static final String RESOURCE_ALI_GPU_COUNT = "aliyun.com/gpu-count";

    public static final String RESOURCE_NOT_SET = "未设置";
    public static final String RESOURCE_GPU_DEFAULT = "0";

    public static final String UNIT_GPU_MEM = "G";

    public static final String REGEX_NATURAL_NUMBER = "[0-9]+";

    public static final String IMAGE_TYPE_TERMINAL = "terminal";

    public static final String STATUS_READY = "Ready";
    public static final String STATUS_STARTING = "Starting";
    public static final String STATUS_FAILED = "Failed";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_TYPE_HTML = "text/html";

    public static final String CHARSET_UTF8 = "utf-8";

    public static final String INGRESS_HOST = "node59";
}
