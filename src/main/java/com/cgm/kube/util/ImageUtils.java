package com.cgm.kube.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author cgm
 */
public class ImageUtils {
    private static final String BREAK = ":";

    private static final String[] LINUX_IMAGES = {"centos", "redhat", "ubuntu"};

    private static final String[] PORT80_IMAGES = {"nginx", "httpd"};

    private static final String[] PORT8080_IMAGES = {"tomcat"};

    private ImageUtils() {

    }

    public static String classifyImageType(String imageName) {
        // 终端化
        if (ArrayUtils.contains(LINUX_IMAGES, imageName.split(BREAK)[0])) {
            return "terminal";
        }
        // 仅暴露端口
        return "port";
    }

    public static int determineImagePort(String imageName) {
        String shortName = imageName.split(BREAK)[0];

        // 示意，非最终逻辑
        if (ArrayUtils.contains(PORT80_IMAGES, shortName)) {
            return 80;
        }
        if (ArrayUtils.contains(PORT8080_IMAGES, shortName)) {
            return 8080;
        }
        return 8443;
    }
}
