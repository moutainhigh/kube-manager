package com.cgm.kube.client.controller;


import com.cgm.kube.base.BaseController;
import com.cgm.kube.client.service.IProxyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@Api(tags = "代理")
@RequestMapping("/proxy")
public class ProxyController extends BaseController {
    @Resource
    private IProxyService proxyService;


    @ApiOperation("代理对pod的请求（swagger无法调试此接口）")
    @GetMapping("/{podHost}/**")
    public ResponseEntity<Object> proxy(HttpServletRequest request, @PathVariable String podHost) {
        return proxyService.proxy(request, podHost);
    }
}
