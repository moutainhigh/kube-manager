package com.cgm.kube.client.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cgm
 */
public interface IProxyService {
    /**
     * 代理
     *
     * @param request http请求
     * @param podHost pod ip + 端口
     * @return pod返回
     */
    ResponseEntity<Object> proxy(HttpServletRequest request, String podHost);
}
