package com.cgm.kube.client.service.impl;

import com.cgm.kube.client.service.IIngressService;
import com.cgm.kube.util.ImageUtils;
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
     * <p>
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
    public void createIngress(String namespace, String uid, String serviceName, String image, Integer servicePort)
            throws ApiException {
        log.info("Creating Ingress: {} {} {}", namespace, uid, serviceName);
        String ingressName = serviceName.substring(0, serviceName.lastIndexOf("-svc")) + "-igs";

        // 构建metadata
        V1ObjectMeta metadata = buildMetadata(ingressName, namespace, image);
        // 构建spec
        ExtensionsV1beta1HTTPIngressPath pathConfig = buildPath(serviceName, servicePort, uid);
        ExtensionsV1beta1IngressSpec spec = buildSpec(pathConfig);

        ExtensionsV1beta1Ingress ingress = new ExtensionsV1beta1Ingress()
                .spec(spec)
                .kind("Ingress")
                .apiVersion("extensions/v1beta1")
                .metadata(metadata);

        ExtensionsV1beta1Api api = new ExtensionsV1beta1Api();

        api.createNamespacedIngress(namespace, ingress, "true", null, null);
    }

    @Override
    public void appendIngress(String namespace, String uid, String serviceName, String image, Integer servicePort)
            throws ApiException {
        log.info("Appending Ingress: {} {} {}", namespace, uid, serviceName);
        String ingressName = namespace + "-igs";

        // 先构建Ingress的spec，只是追加的话不需要metadata等配置
        ExtensionsV1beta1HTTPIngressPath pathConfig = buildPath(serviceName, servicePort, uid);
        ExtensionsV1beta1IngressSpec spec = buildSpec(pathConfig);
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
        V1ObjectMeta metadata = buildMetadata(ingressName, namespace, image);
        ingress.kind("Ingress")
                .apiVersion("extensions/v1beta1")
                .metadata(metadata);
        api.createNamespacedIngress(namespace, ingress, "true", null, null);
    }

    /**
     * 构建metadata
     */
    private V1ObjectMeta buildMetadata(String ingressName, String namespace, String image) {
        if (ImageUtils.keepTarget(image)) {
            // Jupyter因修改了base_url，不需要覆盖target路径
            return new V1ObjectMeta().name(ingressName).namespace(namespace);
        }

        Map<String, String> annotations = new HashMap<>(1);
        // 配合path传递的第二个参数，将路径改为不含/uid的
        annotations.put("nginx.ingress.kubernetes.io/rewrite-target", "/$2");
        return new V1ObjectMeta().name(ingressName).namespace(namespace).annotations(annotations);
    }

    /**
     * 构建path
     * 路径：spec/rules/0/http/paths/0/path
     */
    private ExtensionsV1beta1HTTPIngressPath buildPath(String serviceName, int servicePort, String uid) {
        ExtensionsV1beta1IngressBackend backend = new ExtensionsV1beta1IngressBackend()
                .serviceName(serviceName)
                .servicePort(new IntOrString(servicePort));
        // 因为路径多了一层/uid，需要配合rewrite-target将这一层去掉
        return new ExtensionsV1beta1HTTPIngressPath()
                .path("/" + uid + "(/|$)(.*)")
                .backend(backend);
    }

    /**
     * 构建spec
     */
    private ExtensionsV1beta1IngressSpec buildSpec(ExtensionsV1beta1HTTPIngressPath pathConfig) {
        List<ExtensionsV1beta1HTTPIngressPath> paths = Collections.singletonList(pathConfig);
        ExtensionsV1beta1HTTPIngressRuleValue http = new ExtensionsV1beta1HTTPIngressRuleValue()
                .paths(paths);
        ExtensionsV1beta1IngressRule rule = new ExtensionsV1beta1IngressRule()
                .host("node17")
                .http(http);
        List<ExtensionsV1beta1IngressRule> rules = Collections.singletonList(rule);
        return new ExtensionsV1beta1IngressSpec().rules(rules);
    }
}
