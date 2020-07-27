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

    public static class ErrorCode {
        private ErrorCode() {

        }

        public static final String NO_FIELD = "需要的字段为空值";

        public static final String NO_RESULT = "查询不到指定的内容";
    }
}
