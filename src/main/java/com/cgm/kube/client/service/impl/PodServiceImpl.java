package com.cgm.kube.client.service.impl;

import com.cgm.kube.client.service.IPodService;
import com.cgm.kube.client.dto.UserPodDTO;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.stereotype.Service;

/**
 * @author cgm
 */
@Service
public class PodServiceImpl implements IPodService {
    @Override
    public void createPod(UserPodDTO pod) throws ApiException {
        CoreV1Api api = new CoreV1Api();
        V1Pod kubePod = pod.toKube();
        String namespace = "user-" + pod.getNamespace();
        api.createNamespacedPod(namespace, kubePod, "true", null, null);
    }
}
