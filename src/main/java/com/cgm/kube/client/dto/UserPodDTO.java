package com.cgm.kube.client.dto;

import com.cgm.kube.base.ErrorCode;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodBuilder;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * @author cgm
 */
@Data
@ApiModel("提供给用户的pod类")
public class UserPodDTO {
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private String uid;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("命名空间")
    private String namespace;

    @ApiModelProperty("镜像")
    private String image;

    public UserPodDTO() {

    }

    public UserPodDTO(V1Pod v1Pod) {
        Assert.notNull(v1Pod, ErrorCode.SYS_NO_FIELD);
        Assert.isTrue(v1Pod.getMetadata() != null, ErrorCode.SYS_NO_FIELD);
        Assert.isTrue(v1Pod.getSpec() != null, ErrorCode.SYS_NO_FIELD);

        this.uid = v1Pod.getMetadata().getUid();
        this.name = v1Pod.getMetadata().getName();
        this.namespace = v1Pod.getMetadata().getNamespace();

        V1PodSpec spec = v1Pod.getSpec();
        V1Container container = spec.getContainers().get(0);
        this.image = container.getImage();

    }


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
