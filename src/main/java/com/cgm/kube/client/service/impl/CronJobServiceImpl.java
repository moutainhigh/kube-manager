package com.cgm.kube.client.service.impl;

import com.cgm.kube.client.service.ICronJobService;
import com.cgm.kube.client.dto.UserCronJobDTO;
import org.springframework.stereotype.Service;

/**
 * @author cgm
 */
@Service
public class CronJobServiceImpl implements ICronJobService {
    @Override
    public void createCronJob(UserCronJobDTO cronJob) {
    }
}
