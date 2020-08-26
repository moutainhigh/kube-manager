package com.cgm.kube.client.service.impl;

import com.cgm.kube.client.service.IIngressService;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author cgm
 */
@Service
@Slf4j
public class IngressServiceImpl implements IIngressService {
    @Override
    public void appendIngress(String namespace, String path, String serviceName, Integer servicePort) throws ApiException {
        log.info("Appending Ingress: {} {} {}", namespace, path, serviceName);
        String ingressName = namespace + "-igs";

        V1ObjectMeta metadata = new V1ObjectMeta()
                .name(ingressName)
                .namespace(namespace);

        ExtensionsV1beta1IngressBackend backend = new ExtensionsV1beta1IngressBackend()
                .serviceName(serviceName)
                .servicePort(new IntOrString(servicePort));
        ExtensionsV1beta1HTTPIngressPath pathConfig = new ExtensionsV1beta1HTTPIngressPath()
                .path(path)
                .backend(backend);
        List<ExtensionsV1beta1HTTPIngressPath> paths = Collections.singletonList(pathConfig);
        ExtensionsV1beta1HTTPIngressRuleValue http = new ExtensionsV1beta1HTTPIngressRuleValue()
                .paths(paths);
        ExtensionsV1beta1IngressRule rule = new ExtensionsV1beta1IngressRule()
                .host("node110")
                .http(http);
        List<ExtensionsV1beta1IngressRule> rules = Collections.singletonList(rule);
        ExtensionsV1beta1IngressSpec spec = new ExtensionsV1beta1IngressSpec()
                .rules(rules);

        ExtensionsV1beta1Ingress ingress = new ExtensionsV1beta1Ingress()
                .kind("Ingress")
                .apiVersion("extensions/v1beta1")
                .metadata(metadata)
                .spec(spec);

        ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();

        // 先查询有没有Ingress，没有的话创建一个新的
        api.createNamespacedIngress(namespace, ingress, "true", null, null);

        // 已经创建了Ingress，追加配置
        V1Patch patch = new V1Patch("");
        api.patchNamespacedIngress(ingressName, namespace, patch, "true", null, null, false);
    }
}
