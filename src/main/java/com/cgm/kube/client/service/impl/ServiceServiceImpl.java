package com.cgm.kube.client.service.impl;

import com.cgm.kube.client.service.IPortInfoService;
import com.cgm.kube.client.service.IServiceService;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServicePort;
import io.kubernetes.client.openapi.models.V1ServiceSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cgm
 */
@Service
@Slf4j
public class ServiceServiceImpl implements IServiceService {
    @Resource
    private IPortInfoService portInfoService;

    /**
     * 结构：
     * kind: Service
     * apiVersion: v1
     * metadata:
     *   name: nginx-service
     * spec:
     *   type: NodePort
     *   ports:
     *     - name: http
     *       port: 80
     *       nodePort: 31000
     *   selector:
     *     name: nginx01
     */
    @Override
    public void createService(String namespace, String serviceName, String nameLabel, int targetPort) throws ApiException {
        log.info("Creating Service: {} {}", namespace, serviceName);

        // 获取空闲端口
        int freePort = portInfoService.getFreePort();

        // 第三层赋值
        V1ServicePort servicePort = new V1ServicePort()
                .port(freePort)
                .targetPort(new IntOrString(targetPort))
                .protocol("TCP");
        List<V1ServicePort> ports = Collections.singletonList(servicePort);
        Map<String, String> selector = new HashMap<>(1);
        selector.put("name", nameLabel);

        // 第二层赋值
        V1ObjectMeta metaData = new V1ObjectMeta()
                .name(serviceName);
        V1ServiceSpec spec = new V1ServiceSpec()
                .type("ClusterIP")
                .ports(ports)
                .selector(selector);

        V1Service service = new V1Service()
                .kind("Service")
                .apiVersion("v1")
                .metadata(metaData)
                .spec(spec);

        // 创建
        CoreV1Api api = new CoreV1Api();
        api.createNamespacedService(namespace, service, "true", null, null);
    }

    @Override
    public void createService(String namespace, String nameLabel, int targetPort) throws ApiException {
        this.createService(namespace, nameLabel + "-svc", nameLabel, targetPort);
    }
    @Override
    public void deleteService(String namespace, String serviceName) {
        log.info("Deleting Service: {} {}", namespace, serviceName);
    }
}
