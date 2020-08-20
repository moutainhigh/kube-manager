package com.cgm.kube.client.service.impl;

import com.cgm.kube.base.BaseException;
import com.cgm.kube.base.Constant;
import com.cgm.kube.base.ErrorCode;
import com.cgm.kube.client.service.IProxyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.http.HttpServletRequest;

/**
 * @author cgm
 */
@Service
@Slf4j
public class ProxyServiceImpl implements IProxyService {
    private static final String PROXY_PREFIX = "/proxy/";

    @Override
    public ResponseEntity<Object> proxy(HttpServletRequest request, String podHost) {
        log.debug("Request URI: {}", request.getRequestURI());
        String url = request.getRequestURI().replace(PROXY_PREFIX, "");
        // 没有指定http/https的默认使用http
        url = url.startsWith("http") ? url : "http://" + url;
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String contentType = response.getFirstHeader(Constant.HEADER_CONTENT_TYPE).getValue();
            if (contentType.contains(Constant.CONTENT_TYPE_HTML)) {
                // 对html文本进行修改
                log.debug("Content-Type: text/html");
                String body = EntityUtils.toString(response.getEntity(), Constant.CHARSET_UTF8);
                body = replaceDomain(url, body);
                return ResponseEntity.ok().headers(getApacheHeaders(response)).body(body);
            } else {
                // 直接使用二进制数据
                byte[] body = EntityUtils.toByteArray(response.getEntity());
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

    private String replaceDomain(String url, String originHtml) {
        String backend = "http://localhost:8100";
        String baseUri = url.split("//")[0] + "//" + url.split("/")[2];
        String domain = baseUri.split("//")[1];
        Document document = Jsoup.parse(originHtml);

        // 添加<base>标签及href属性
        Elements base = document.getElementsByTag("base");
        if (base.isEmpty()) {
            Elements title = document.getElementsByTag("title");
            title.after("<base href='/proxy/" + baseUri + "/'>");
        } else {
            base.get(0).attr("href", PROXY_PREFIX + domain + "/");
        }

        // 获取需要修改url的标签
        Elements elements = new Elements();
        elements.addAll(document.getElementsByTag("a"));
        elements.addAll(document.getElementsByTag("link"));
        elements.addAll(document.getElementsByTag("img"));
        elements.addAll(document.getElementsByTag("script"));
        elements.addAll(document.getElementsByTag("form"));

        // 修改为代理链接
        String attributeKey;
        for (Element e : elements) {
            log.debug("origin: {}", e.toString());
            if (StringUtils.isNotEmpty(e.attr("href"))) {
                attributeKey = "href";
            } else if (StringUtils.isNotEmpty(e.attr("src"))) {
                attributeKey = "src";
            } else if (StringUtils.isNotEmpty(e.attr("action"))) {
                attributeKey = "action";
            } else {
                continue;
            }

            String href = e.attr(attributeKey);
            if (href.startsWith("//")) {
                // 使用原来页面的协议
                href = url.split("//")[0] + href;
            }
            if (href.startsWith("/")) {
                // 站内地址进行补全
                href = baseUri + href;
            }
            if (href.indexOf(baseUri) == 0 || href.contains(domain)) {
                // 站内地址替换为代理
                String proxyHref = backend + PROXY_PREFIX + href;
                e.attr(attributeKey, proxyHref);
            }
            log.debug("result: {}", e.attr(attributeKey));
        }

        return document.toString();
    }
}
