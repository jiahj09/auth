package webspider.http.downloader;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webspider.http.Method;
import webspider.http.Request;
import webspider.http.Response;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author JIUN·LIU
 * @data 2020/2/12 16:30
 */
public class JsoupDownloader implements Downloader {
    private static Logger logger = LoggerFactory.getLogger(JsoupDownloader.class);

    @Override
    public Response download(Request request) {
        try {
            String taskId = request.getTaskId();
            logger.debug("task_id={} , msg={}", taskId, "采用jsoup下载器下载信息");
            Connection connect = Jsoup.connect(request.getUrl());
            if (request.getHeaders() != null) connect = connect.headers(request.getHeaders()); // 头
            if (request.getProxy() != null) connect = connect.proxy(request.getProxy()); //代理
            if (request.getTimeOut() != null) connect = connect.timeout(request.getTimeOut());// 超时
            if (request.getRequestData() != null) connect = connect.data(request.getRequestData());//请求体
            if (request.getRequestBody() != null) connect.requestBody(request.getRequestBody());//字符串请求体

            //TODO 关于 httpclient cookie 到jsoup cookie的转换

            boolean followRedirects = request.isFollowRedirects();
            connect = connect.followRedirects(followRedirects)
                    .ignoreHttpErrors(request.isIgnoreHttpErrors())
                    .ignoreContentType(request.isIgnoreContentType());

            if (request.getMethod().equals(Method.GET)) {
                connect.method(Connection.Method.GET);
            } else if (request.getMethod().equals(Method.POST)) {
                connect.method(Connection.Method.POST);
                if (request.getRequestCharset() != null) connect.postDataCharset(request.getRequestCharset());
            } else if (request.getMethod().equals(Method.PUT)) {
                connect.method(Connection.Method.PUT);
            } else if (request.getMethod().equals(Method.PATCH)) {
                connect.method(Connection.Method.PATCH);
            } else if (request.getMethod().equals(Method.HEAD)) {
                connect.method(Connection.Method.HEAD);
            } else if (request.getMethod().equals(Method.OPTIONS)) {
                connect.method(Connection.Method.OPTIONS);
            } else if (request.getMethod().equals(Method.TRACE)) {
                connect.method(Connection.Method.TRACE);
            }
            logger.debug("task_id={} , msg={}", request.getTaskId(), "jsoup 请求代码组装完毕");
            Connection.Response execute = null;
            Integer retryTime = request.getRetryTime();
            while (retryTime > 0) {
                try {
                    execute = execute(connect);
                    if (request.getResponseCharset() != null) execute.charset(request.getResponseCharset());
                    logger.debug("task_id={} , 第 {} 次请求 , msg={}", request.getTaskId(), request.getRetryTime() - retryTime + 1, "jsoup 请求结束");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("task_id={} , msg={}", request.getTaskId(), "jsoup 请求异常，重试···");
                    retryTime--;
                }
            }

            return parse(request.getTaskId(), execute);
        } catch (Exception e) {
            logger.info("task_id={} , msg={}", request.getTaskId(), "jsoup 下载器异常");
        }
        return null;
    }

    private static Connection.Response execute(Connection connection) throws Exception {
        return connection.execute();
    }

    public Response parse(String task_id, Connection.Response origin) {
        Response response = new Response() {{
            setTaskId(task_id);
        }};
        response.setStatusCode(origin.statusCode());
        response.setResponseBody(origin.body());
        response.setHeaders(origin.headers());
        response.setCharset(origin.charset());
        response.setResponseBytes(origin.bodyAsBytes());
        response.setCurrentURL(origin.url());
        try { // 封装返回cookie信息到对应的response 的 cookie 中
            Class<?> clazz = origin.getClass();
            Class<?> superclass = clazz.getSuperclass();
            Field headers = superclass.getDeclaredField("headers");
            headers.setAccessible(true);
            Map map = (Map) headers.get(origin);
            List<String> strings = (List<String>) map.get("Set-Cookie");
            // TODO 字符头到cookie的转换
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
