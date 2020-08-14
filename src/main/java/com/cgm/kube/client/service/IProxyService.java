package com.cgm.kube.client.service;

import org.apache.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;

public interface IProxyService {
    /**
     * 代理
     *
     * @param request http请求
     * @param podHost pod ip + 端口
     * @return pod返回
     */
    HttpResponse proxy(HttpServletRequest request, String podHost);
}
