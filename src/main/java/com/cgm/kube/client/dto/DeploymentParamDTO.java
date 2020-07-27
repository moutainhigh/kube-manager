package com.cgm.kube.client.dto;

import lombok.Data;

/**
 * @author cgm
 */
@Data
public class DeploymentParamDTO {
    private String name;
    private Integer replicas;
    private String availableStatus;
}
