package com.cgm.kube.util;

import io.kubernetes.client.custom.QuantityFormatter;

import java.math.BigDecimal;

/**
 * @author cgm
 */
public class ResourceFormatter {
    private ResourceFormatter () {

    }

    public static Integer formatCpu (String cpuResource) {
        // k8s的CPU默认单位是核，最小管理单位是m（千分之一核），数据库内单位采用m，故乘以1000
        QuantityFormatter formatter = new QuantityFormatter();
        return formatter.parse(cpuResource).getNumber().multiply(BigDecimal.valueOf(1000)).intValue();
    }

    public static Integer formatMem (String memResource) {
        // k8s的内存默认单位是字节，数据库内单位采用Mi，故右移20位
        QuantityFormatter formatter = new QuantityFormatter();
        return formatter.parse(memResource).getNumber().intValue() >> 20;
    }

    public static Integer formatGpu (String gpuResource) {
        QuantityFormatter formatter = new QuantityFormatter();
        return formatter.parse(gpuResource).getNumber().intValue();
    }
}
