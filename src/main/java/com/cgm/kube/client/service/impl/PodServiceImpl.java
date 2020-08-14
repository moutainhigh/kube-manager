package com.cgm.kube.client.service.impl;

import com.cgm.kube.client.constant.KubeErrorCode;
import com.cgm.kube.client.service.IPodService;
import com.cgm.kube.client.dto.UserPodDTO;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cgm
 */
@Service
public class PodServiceImpl implements IPodService {
    @Override
    public List<UserPodDTO> listDeploymentPod(Long organizationId, String name) throws ApiException {
        CoreV1Api api = new CoreV1Api();
        V1PodList v1PodList = api.listNamespacedPod("ns" + organizationId, "true", null, null,
                null, null, null, null, null, null);
        Assert.notEmpty(v1PodList.getItems(), KubeErrorCode.QUERY_FAILED);

        // 为避免空指针异常，不使用lambda表达式
        List<UserPodDTO> podDTOList = new ArrayList<>();
        for (V1Pod v1Pod : v1PodList.getItems()) {
            Assert.isTrue(v1Pod.getMetadata() != null, KubeErrorCode.NO_FIELD);
            Assert.isTrue(v1Pod.getMetadata().getName() != null, KubeErrorCode.NO_FIELD);
            if (v1Pod.getMetadata().getName().startsWith(name)) {
                podDTOList.add(new UserPodDTO(v1Pod));
            }
        }
        return podDTOList;
    }

    @Override
    public void createPod(UserPodDTO pod) throws ApiException {
        CoreV1Api api = new CoreV1Api();
        V1Pod kubePod = pod.toKube();
        String namespace = pod.getNamespace();
        api.createNamespacedPod(namespace, kubePod, "true", null, null);
    }
}
