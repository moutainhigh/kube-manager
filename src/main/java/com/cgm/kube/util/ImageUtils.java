package com.cgm.kube.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author cgm
 */
public class ImageUtils {
    private static final String BREAK = ":";

    private static final String[] LINUX_IMAGES = {"centos", "redhat", "ubuntu"};

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
}
