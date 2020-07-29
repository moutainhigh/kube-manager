package com.cgm.kube.client.constant;

/**
 * @author cgm
 */
public class KubeConstant {
    private KubeConstant() {
    }

    /**
     * 系统超级管理员角色，其他角色不得包含"system_admin"
     */
    public static final String ROLE_SYSTEM_ADMIN = "system_admin";

    public static final String RESOURCE_CPU = "cpu";

    public static final String RESOURCE_MEM = "memory";

    public static final String RESOURCE_GPU = "nvidia.com/gpu";

    public static final String RESOURCE_CPU_DEFAULT = "未设置";

    public static final String RESOURCE_MEM_DEFAULT = "未设置";

    public static final String RESOURCE_GPU_DEFAULT = "0";

    /**
     * 在k8s状态中，以字符串“True”表示真
     */
    public static final String TRUE = "True";

    /**
     * 在k8s状态中，以字符串“False”表示假
     */
    public static final String FALSE = "False";

    public static final String STATUS_READY = "Ready";
    public static final String STATUS_STARTING = "Starting";
    public static final String STATUS_FAILED = "Failed";

    public static class ErrorCode {
        private ErrorCode() {

        }

        public static final String NO_FIELD = "查询异常：结果中需要的字段为空值";

        public static final String NO_RESULT = "查询不到指定的内容";
    }
}
