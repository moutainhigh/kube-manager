package com.cgm.kube.client.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author cgm
 */
@Data
public class PortInfo {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private Integer currentPort;

    private Integer minPort;

    private Integer maxPort;

    private Integer maxRetry;
}
