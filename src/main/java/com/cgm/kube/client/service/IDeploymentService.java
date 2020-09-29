package com.cgm.kube.client.service;

import com.cgm.kube.client.dto.DeploymentParamDTO;
import com.cgm.kube.client.dto.UserDeploymentDTO;
import io.kubernetes.client.openapi.ApiException;

import java.io.IOException;
import java.util.List;

/**
 * @author cgm
 */
public interface IDeploymentService {

    /**
     * 根据名称查询deployment
     *
     * @param name 名称
     * @return deployment
     * @throws ApiException api异常
     */
    UserDeploymentDTO getDeploymentByName(String name) throws ApiException;

    /**
     * 查询deployment
     *
     * @param paramDTO 查询参数
     * @return deployment列表
     * @throws ApiException api异常
     */
    List<UserDeploymentDTO> listDeployment(DeploymentParamDTO paramDTO) throws ApiException;

    /**
     * 创建deployment
     *
     * @param deployment deployment
     * @throws ApiException api异常
     * @throws IOException  IO异常
     */
    void createDeployment(UserDeploymentDTO deployment) throws ApiException, IOException;

    /**
     * 更新deployment
     *
     * @param deployment deployment
     * @throws ApiException api异常
     */
    void updateDeployment(UserDeploymentDTO deployment) throws ApiException;

    /**
     * 缩放deployment
     *
     * @param deployment deployment
     * @throws ApiException api异常
     */
    void patchDeploymentScale(UserDeploymentDTO deployment) throws ApiException;


    /**
     * 删除deployment
     *
     * @param namespace 命名空间
     * @param name      名称
     * @throws ApiException api异常
     */
    void deleteDeployment(String namespace, String name) throws ApiException;


}
