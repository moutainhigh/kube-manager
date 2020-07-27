package com.cgm.kube.client.service;

import com.cgm.kube.client.dto.UserJobDTO;

/**
 * @author cgm
 */
public interface IJobService {
    /**
     * 创建job
     *
     * @param job job
     */
    void createJob(UserJobDTO job);
}
