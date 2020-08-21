package com.cgm.kube.client.service;

/**
 * @author cgm
 */
public interface IPortInfoService {
    /**
     * 获取空闲端口
     * @return 空闲端口
     */
    int getFreePort();
}
