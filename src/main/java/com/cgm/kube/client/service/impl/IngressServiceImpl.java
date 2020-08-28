package com.cgm.kube.client.service.impl;

import com.cgm.kube.client.service.IIngressService;
import com.google.gson.Gson;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cgm
 */
@Service
@Slf4j
public class IngressServiceImpl implements IIngressService {
    /**
     * 追加Ingress配置，没有时新建
     *
     * Ingress结构：
     * #kind: Ingress
     * #apiVersion: extensions/v1beta1
     * #metadata:
     * #  name: ns100006-igs
     * #  namespace: ns100006
     * #  annotations:
     * #    nginx.ingress.kubernetes.io/rewrite-target: /$2
     * #    nginx.ingress.kubernetes.io/use-regex: 'true'
     * #spec:
     * #  rules:
     * #    - host: node110
     * #      http:
     * #        paths:
     * #          - path: /4b48a29d-c77f-4e6d-bc5c-7e39dfc3b433(/|$)(.*)
     * #            backend:
     * #              serviceName: desk02-svc
     * #              servicePort: 30044
     */
    @Override
    public void appendIngress(String namespace, String path, String serviceName, Integer servicePort)
            throws ApiException {
        log.info("Appending Ingress: {} {} {}", namespace, path, serviceName);
        String ingressName = namespace + "-igs";

        // 先构建Ingress的spec，只是追加的话不需要metadata等配置
        ExtensionsV1beta1IngressBackend backend = new ExtensionsV1beta1IngressBackend()
                .serviceName(serviceName)
                .servicePort(new IntOrString(servicePort));
        // 因为路径多了一层/uid，需要配合rewrite-target将这一层去掉
        ExtensionsV1beta1HTTPIngressPath pathConfig = new ExtensionsV1beta1HTTPIngressPath()
                .path(path + "(/|$)(.*)")
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
        ExtensionsV1beta1Ingress ingress = new ExtensionsV1beta1Ingress().spec(spec);

        // 先查询有没有Ingress，如果已经创建了Ingress，追加配置
        ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();
        ExtensionsV1beta1IngressList list = api.listNamespacedIngress(namespace, "true", null, null,
                null, null, null, null, null, null);
        if (!list.getItems().isEmpty()) {
            String pathJson = new Gson().toJson(pathConfig, ExtensionsV1beta1HTTPIngressPath.class);
            // patch三个参数：op，操作，可为add/replace/merge等；path，路径；value，要添加/修改的配置
            V1Patch patch = new V1Patch("[{\"op\": \"add\", \"path\": \"/spec/rules/0/http/paths/0\", \"value\":"
                    + pathJson + "}]");
            api.patchNamespacedIngress(ingressName, namespace, patch, "true", null, null, null);
            return;
        }

        // 没有的话创建一个新的，把metadata等配置补全
        Map<String, String> annotations = new HashMap<>(1);
        // 配合path传递的第二个参数，将路径改为不含/uid的
        annotations.put("nginx.ingress.kubernetes.io/rewrite-target", "/$2");
        V1ObjectMeta metadata = new V1ObjectMeta()
                .name(ingressName)
                .namespace(namespace)
                .annotations(annotations);
        ingress.kind("Ingress")
                .apiVersion("extensions/v1beta1")
                .metadata(metadata);
        api.createNamespacedIngress(namespace, ingress, "true", null, null);
    }
}
