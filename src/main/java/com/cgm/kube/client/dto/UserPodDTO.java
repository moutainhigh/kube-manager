package com.cgm.kube.client.dto;

import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cgm
 */
@Data
@ApiModel("提供给用户的pod类")
public class UserPodDTO {
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 命名空间
     */
    @ApiModelProperty("命名空间")
    private String namespace;

    /**
     * 镜像
     */
    @ApiModelProperty("镜像")
    private String image;


    public V1Pod toKube() {
        return new V1PodBuilder()
                .withNewMetadata()
                .withName(this.getName())
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName(this.getName() + "-container")
                .withImage(this.getImage())
                .endContainer()
                .endSpec()
                .build();
    }
}
