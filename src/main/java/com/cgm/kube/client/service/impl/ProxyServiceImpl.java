package com.cgm.kube.client.service.impl;

import com.cgm.kube.base.BaseException;
import com.cgm.kube.client.service.IProxyService;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author cgm
 */
@Service
public class ProxyServiceImpl implements IProxyService {
    @Override
    public HttpResponse proxy(HttpServletRequest request, String podHost) {
        String url = "http://" + podHost + request.getRequestURI().split(podHost)[1];
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // 对html进行domain修改
            if ("text/html".equals(response.getFirstHeader("Content-Type").getValue())) {
                return replaceDomain(request, response);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("代理异常");
        }
    }

    private HttpResponse replaceDomain(HttpServletRequest request, CloseableHttpResponse response) throws IOException {
        String backend = "http://localhost:8100";
        String baseUri = "http://100.65.110.40";

        String originHtml = EntityUtils.toString(response.getEntity(), "utf-8");

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

        return null;
    }
}
