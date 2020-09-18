package com.cgm.kube.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author cgm
 */
public class ImageUtils {
    private static final String BREAK = ":";

    private static final String[] BACKGROUND_IMAGES = {"centos", "redhat", "ubuntu"};

    private static final String[] JUPYTER_IMAGES = {"jupyter/base-notebook"};

    private static final String[] PORT80_IMAGES = {"nginx", "httpd", "dorowu/ubuntu-desktop-lxde-vnc"};

    private static final String[] PORT6080_IMAGES = {};

    private static final String[] PORT7681_IMAGES = {"tsl0922/ttyd", "tydd2"};

    private static final String[] PORT8080_IMAGES = {"tomcat"};

    private static final String[] PORT8888_IMAGES = {"jupyter/base-notebook"};

    private ImageUtils() {

    }

    /**
     * 获取镜像命令
     */
    public static String[] determineCommands(String imageName, String uid) {
        String shortName = imageName.split(BREAK)[0];

        // Jupyter需要修改base_url
        if (ArrayUtils.contains(JUPYTER_IMAGES, shortName)) {
            return new String[]{"/opt/conda/bin/jupyter", "notebook", "--NotebookApp.base_url='/" + uid + "/'",
                    "--NotebookApp.token=''"};
        }

        // 需要维持后台运行的镜像
        if (ArrayUtils.contains(BACKGROUND_IMAGES, shortName)) {
            return new String[]{"/bin/bash", "-ce", "tail -f /dev/null"};
        }

        // 默认返回空数组
        return new String[0];
    }

    /**
     * 端口确定
     */
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

    /**
     * 路径保持
     */
    public static boolean keepTarget(String imageName) {
        String shortName = imageName.split(BREAK)[0];
        // 当前只有Jupyter需要保持target，如果有其他镜像，考虑定义数组KEEP_TARGET_IMAGES
        return ArrayUtils.contains(JUPYTER_IMAGES, shortName);
    }
}
