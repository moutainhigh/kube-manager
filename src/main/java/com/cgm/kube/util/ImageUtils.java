package com.cgm.kube.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author cgm
 */
public class ImageUtils {
    private static final String BREAK = ":";

    private static final String[] LINUX_IMAGES = {"centos", "redhat", "ubuntu"};

    private static final String[] PORT80_IMAGES = {"nginx", "httpd", "dorowu/ubuntu-desktop-lxde-vnc"};

    private static final String[] PORT6080_IMAGES = {};

    private static final String[] PORT7681_IMAGES = {"tsl0922/ttyd"};

    private static final String[] PORT8080_IMAGES = {"tomcat"};

    private static final String[] PORT8888_IMAGES = {"jupyter/base-notebook"};

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
        if (ArrayUtils.contains(PORT6080_IMAGES, shortName)) {
            return 6080;
        }
        if (ArrayUtils.contains(PORT7681_IMAGES, shortName)) {
            return 7681;
        }
        if (ArrayUtils.contains(PORT8080_IMAGES, shortName)) {
            return 8080;
        }
        if (ArrayUtils.contains(PORT8888_IMAGES, shortName)) {
            return 8888;
        }

        // SSH默认端口
        return 22;
    }
}
