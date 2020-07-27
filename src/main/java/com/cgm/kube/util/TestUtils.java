package com.cgm.kube.util;

import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.custom.QuantityFormatter;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;

import java.io.IOException;

/**
 * @author cgm
 */
public class TestUtils {

    private TestUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) throws IOException, ApiException {
        Quantity quantity = new QuantityFormatter().parse("500Gi");
        System.out.println(quantity.toSuffixedString());
        System.out.println(quantity.getNumber());
    }
}
