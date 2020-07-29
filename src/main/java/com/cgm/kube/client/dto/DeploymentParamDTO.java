package com.cgm.kube.client.dto;

import lombok.Data;

/**
 * @author cgm
 */
@Data
public class DeploymentParamDTO {

    private String name;
    private String resourceType;
    private String status;

    private Integer page;
    private Integer limit;
    private String sort;

}
