package com.cgm.kube.client.service;

import com.cgm.kube.client.dto.UserCronJobDTO;

/**
 * @author cgm
 */
public interface ICronJobService {
    /**
     * 创建job
     *
     * @param cronJob cronJob
     */
    void createCronJob(UserCronJobDTO cronJob);
}
