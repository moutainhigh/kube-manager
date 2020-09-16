package com.cgm.kube.client.service.impl;

import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.client.entity.PortInfo;
import com.cgm.kube.client.mapper.PortInfoMapper;
import com.cgm.kube.client.service.IPortInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author cgm
 */
@Service
@Slf4j
public class PortInfoServiceImpl implements IPortInfoService {
    @Resource
    private PortInfoMapper basicInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int getFreePort() {
        PortInfo basicInfo = basicInfoMapper.selectByPrimaryKey(1);
        Assert.notNull(basicInfo, ErrorCode.SYS_QUERY_FAILED);

        int newPort = basicInfo.getCurrentPort() + 1;
        int tryTimes = 0;
        while (!checkPort(newPort) && tryTimes < basicInfo.getMaxRetry()) {
            tryTimes ++;
            newPort ++;
            if (newPort > basicInfo.getMaxPort()) {
                newPort = basicInfo.getMinPort();
            }
        }

        // 重试次数校验，更新数据库，返回新端口
        Assert.isTrue(tryTimes < basicInfo.getMaxRetry(), ErrorCode.SYS_NO_FREE_PORT);
        basicInfo.setCurrentPort(newPort);
        basicInfoMapper.updateByPrimaryKey(basicInfo);
        log.debug("Free port: {}", newPort);
        return newPort;
    }

    private boolean checkPort(int port) {
        try (Socket s = new Socket()) {
            s.bind(new InetSocketAddress("localhost", port));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
