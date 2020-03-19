package webspider.http.downloader;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webspider.http.Method;
import webspider.http.Request;
import webspider.http.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author JIUN·LIU
 * @data 2020/2/13 15:19
 */
public class HttpClientDownloader implements Downloader {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);

    /**
     * 请求方式的转换，并进行下载。（简要的步骤如下）
     * 1、请求方式
     * 2、请求头
     * 3、请求体
     * 4、cookie设置
     * 5、请求的发送
     * 6、response 转换
     * 7、细节化配置
     * 7.1 重定向，交给的外部调用
     *
     * @param request
     * @return
     */
    @Override
    public Response download(Request request) {
        //1
        HttpRequestBase clientRequest = getClientRequest(request);
        //2
        setHeaders(clientRequest, request); // 头设置
        //3

        if (request.getRequestBody() != null) {
            setTextEntity(clientRequest, request);
        } else if (request.getRequestData() != null) {
            setNameValuePair(clientRequest, request);
        }


        //7
        RequestConfig.Builder custom = RequestConfig.custom();
        //7.1
        custom = custom.setRedirectsEnabled(false);

        RequestConfig config = custom.build();

        //4
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (request.getCookieStore() != null) builder.setDefaultCookieStore(request.getCookieStore()); // cookie 的设置
        builder.setDefaultRequestConfig(config);
        CloseableHttpClient client = builder.build();




        // 5
        CloseableHttpResponse execute = null;
        Integer retryTime = request.getRetryTime();
        while (retryTime > 0) {
            try {
                execute = execute(client, clientRequest);
                //TODO 相关字符集的设置
                logger.debug("task_id={} , 第 {} 次请求 , msg={}", request.getTaskId(), request.getRetryTime() - retryTime + 1, "httpclient 请求成功");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("task_id={} , msg={}", request.getTaskId(), "httpclient 请求异常，重试···");
                retryTime--;
            }
        }
        return parse(request, execute);
    }


    private static CloseableHttpResponse execute(CloseableHttpClient client, HttpRequestBase clientRequest) throws Exception {
        return client.execute(clientRequest);
    }


    private Response parse(Request request, CloseableHttpResponse response) {
        // TODO httpclient 的请求结果转换为自定义
        HttpEntity entity = response.getEntity();
        Response result = new Response();
        result.setTaskId(request.getTaskId());
        result.setCookieStore(request.getCookieStore());
        try {
            result.setCurrentURL(new URL(request.getUrl()));
            result.setResponseBytes(EntityUtils.toByteArray(entity));
            if (result.getResponseBytes() != null) {
                result.setResponseBody(new String(result.getResponseBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        result.setStatusCode(statusCode);
        Map<String, String> header2Map = header2Map(response.getAllHeaders());
        if (header2Map != null) result.setHeaders(header2Map);
        if (statusCode>299 && statusCode<400){
            String location = header2Map.get("Location");
            result.setLocation(location);
        }
        return result;
    }

    /**
     * 根据请求方式获取对应的httpclient 的请求类
     *
     * @param request
     * @return
     */
    private HttpRequestBase getClientRequest(Request request) {
        HttpRequestBase httpRequest;
        Method method = request.getMethod();
        String url = request.getUrl();
        if (method.equals(Method.GET)) {
            httpRequest = new HttpGet(url);
        } else if (method.equals(Method.POST)) {
            httpRequest = new HttpPost(url);
        } else if (method.equals(Method.DELETE)) {
            httpRequest = new HttpDelete(url);
        } else if (method.equals(Method.HEAD)) {
            httpRequest = new HttpHead(url);
        } else if (method.equals(Method.OPTIONS)) {
            httpRequest = new HttpOptions(url);
        } else if (method.equals(Method.PATCH)) {
            httpRequest = new HttpPatch(url);
        } else if (method.equals(Method.PUT)) {
            httpRequest = new HttpPut(url);
        } else if (method.equals(Method.TRACE)) {
            httpRequest = new HttpTrace(url);
        } else {
            httpRequest = new HttpGet(url);
        }
        return httpRequest;
    }

    private void setHeaders(HttpRequestBase clientRequest, Request request) {
        if (request.getHeaders() != null) {
            clientRequest.setHeaders(map2Header(request.getHeaders()));
        }
    }

    private void setNameValuePair(HttpRequestBase clientRequest, Request request) {
        if (request.getRequestData() != null) {
            HttpEntity entity = EntityBuilder.create().setParameters(map2NameValuePair(request.getRequestData())).build();
            ((HttpPost) clientRequest).setEntity(entity);
        }
    }

    private void setTextEntity(HttpRequestBase clientRequest, Request request) {
        if (request.getRequestBody() != null) {
            HttpEntity entity1 = EntityBuilder.create().setText(request.getRequestBody()).build();
            ((HttpPost) clientRequest).setEntity(entity1);
        }
    }

    private Header[] map2Header(Map<String, String> mapHeaders) {
        if (mapHeaders == null) return null;
        Header[] headers = new Header[mapHeaders.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : mapHeaders.entrySet()) {
            headers[index] = new BasicHeader(entry.getKey(), entry.getValue());
            index++;
        }
        return headers;
    }

    private Map<String, String> header2Map(Header[] headers) {
        if (headers == null) return null;
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            result.put(header.getName(), header.getValue());
        }
        return result;
    }

    private List<NameValuePair> map2NameValuePair(Map<String, String> mapForm) {
        if (mapForm == null) return null;
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : mapForm.entrySet()) {
            if (entry.getKey() == null) continue;
            if (entry.getValue() == null) nameValuePairs.add(new BasicNameValuePair(entry.getKey(), ""));
            else nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nameValuePairs;
    }
}
