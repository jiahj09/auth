package webspider.http;


import org.apache.http.cookie.Cookie;
import webspider.http.cookie.CookieStore;

import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http 返回体封装
 *
 * @author JIUN·LIU
 * @data 2020/2/12 13:58
 */
public class Response {
    // 任务id，用于全局同一条任务标识
    private String taskId;
    // http 返回状态码
    private Integer statusCode;
    // http 返回头信息
    private Map<String, String> headers;

    private CookieStore cookieStore;

    // http 返回体 字符串格式
    private String responseBody;
    // http 返回信息 流格式
    private OutputStream responseStream;
    // http 返回信息 二进制数据
    private byte[] responseBytes;

    //
    private String charset;
    private URL currentURL;


    public URL getCurrentURL() {
        return currentURL;
    }

    public void setCurrentURL(URL currentURL) {
        this.currentURL = currentURL;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public byte[] getResponseBytes() {
        return responseBytes;
    }



    public void setResponseBytes(byte[] responseBytes) {
        this.responseBytes = responseBytes;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }



    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public OutputStream getResponseStream() {
        return responseStream;
    }

    public void setResponseStream(OutputStream responseStream) {
        this.responseStream = responseStream;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }
}


