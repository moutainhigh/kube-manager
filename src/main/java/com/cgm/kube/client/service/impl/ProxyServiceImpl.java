package com.cgm.kube.client.service.impl;

import com.cgm.kube.base.BaseException;
import com.cgm.kube.base.Constant;
import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.client.service.IProxyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author cgm
 */
@Service
@Slf4j
public class ProxyServiceImpl implements IProxyService {
    @Override
    public ResponseEntity<Object> proxy(HttpServletRequest request, String podHost) {
        String url = "http://" + podHost + request.getRequestURI().split(podHost)[1];
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String contentType = response.getFirstHeader(Constant.HEADER_CONTENT_TYPE).getValue();
            if (Constant.CONTENT_TYPE_HTML.equals(contentType)) {
                // 对html文本进行修改
                log.debug("Content-Type: text/html");
                String body = EntityUtils.toString(response.getEntity(), Constant.CHARSET_UTF8);
                body = replaceDomain(request, body);
                return ResponseEntity.ok().headers(getApacheHeaders(response)).body(body);
            } else {
                // 直接使用二进制数据
                byte[] body = EntityUtils.toByteArray(response.getEntity());
                log.debug(Arrays.toString(response.getAllHeaders()));
                // 复制响应头后返回
                return ResponseEntity.ok().headers(getApacheHeaders(response)).body(body);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(ErrorCode.PROXY_ERROR);
        }
    }

    /**
     * 获取并转换响应头
     *
     * @param response httpClient产生的response
     * @return spring响应头
     */
    private HttpHeaders getApacheHeaders(HttpResponse response) {
        Header[] headers = response.getAllHeaders();
        HttpHeaders httpHeaders = new HttpHeaders();
        for (Header header : headers) {
            httpHeaders.add(header.getName(), header.getValue());
        }
        return httpHeaders;
    }

    private String replaceDomain(HttpServletRequest request, String originHtml) {
        String backend = "http://localhost:8100";
        String baseUri = "http://100.65.110.40";

        Document document = Jsoup.parse(originHtml);

        // 修改href为代理链接
        Elements aList = document.getElementsByTag("a");
        for (Element a : aList) {
            // target未设置或为self时进行处理，否则跳过
            String target = a.attr("target");
            if (!StringUtils.isEmpty(target) && !"_self".equals(target)) {
                continue;
            }

            String href = a.attr("href");
            if (href.indexOf("/") == 0) {
                href = baseUri + href;
            }
            if (href.indexOf(baseUri) == 0 || href.contains("domain")) {
                String proxyHref = backend + "/proxy/100.65.110.40" + request.getRequestURI();
                a.attr("href", proxyHref);
            }
        }

        return document.toString();
    }
}
