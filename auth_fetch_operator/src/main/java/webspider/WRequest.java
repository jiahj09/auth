package webspider;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import com.example.auth_comm.utils.ContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webspider.http.Method;
import webspider.http.Request;
import webspider.http.Response;
import webspider.http.downloader.Downloader;
import webspider.http.downloader.HttpClientDownloader;
import webspider.http.downloader.JsoupDownloader;
import com.example.auth_comm.utils.FetchUtil;

import java.util.*;

/**
 * 实际用于请求的类
 * 包含http、websocket、webdriver的发起
 * <p>
 * 一、首先梳理HTTP请求，HTTP请求报文包含内容：
 * 请求方式 url 协议
 * 请求头（cookie也是请求头）
 * <p>
 * 请求体
 * <p>
 * 进行http请求最重要的就是，封装请求。获取请求结果
 *
 * @author JIUN·LIU
 * @data 2020/2/12 12:58
 */
public class WRequest {

    static Logger logger = LoggerFactory.getLogger(WRequest.class);

    // 任务id，用于全局同一条任务标识
    private String taskId;
    // 任务处理开始时间
    private Date startTime;
    // 任务处理结束时间
    private Date endTime;
    // http 请求体封装信息
    private Request request;
    // http 下载器
    private Downloader downloader;

    private FetchUtil fetchUtil = ContextUtil.getObj(FetchUtil.class);

    private List<String> historyUrls;

    private int maxRedirectTime = 5;

    public static WRequest create(String taskId) {
        return new WRequest(taskId);
    }

    public WRequest(String taskId) {
        this.taskId = taskId;
    }

    public WRequest connect(String httpUrl) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        this.request.setUrl(httpUrl);
        return this;
    }

    public WRequest maxRedirect(int maxRedirectTime) {
        this.maxRedirectTime = maxRedirectTime;
        return this;
    }

    public WRequest method(Method method) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        this.request.setMethod(method);
        return this;
    }

    public WRequest headers(Map<String, String> headers) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        if (this.request.getHeaders() == null) this.request.setHeaders(new HashMap<>());
        this.request.getHeaders().putAll(headers);
        return this;
    }

    public WRequest header(String name, String value) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        if (this.request.getHeaders() == null) this.request.setHeaders(new HashMap<>());
        this.request.getHeaders().put(name, value);
        return this;
    }

    public WRequest retry(Integer retry) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        if (retry > 0) this.request.setRetryTime(retry);
        return this;
    }

    public WRequest followRedirect(boolean follow) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        this.request.setFollowRedirects(follow);
        return this;
    }

    public WRequest ignoreHttpErrors(boolean ignoreHttpErrors) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        this.request.setIgnoreHttpErrors(ignoreHttpErrors);
        return this;
    }

    public WRequest ignoreContentType(boolean ignoreContentType) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        this.request.setIgnoreContentType(ignoreContentType);
        return this;
    }

    public WRequest form(String... form) {
        if ((form.length % 2) == 0) {
            if (this.request == null) {
                this.request = new Request() {{
                    setTaskId(taskId);
                }};
            }
            return this.form(strForm2Map(form));
        } else {
            throw new RuntimeException("参数个数必须为偶数，如果为空，请输入为空字符串");
        }
    }

    public WRequest form(Map<String, String> form) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        this.request.setRequestData(form);
        return this;
    }

    /**
     * 超时时间设置，单位为毫秒
     *
     * @param millisecond
     * @return
     */
    public WRequest timeout(int millisecond) {
        if (this.request == null) this.request = new Request() {{
            setTaskId(taskId);
        }};
        if (millisecond > 500) this.request.setTimeOut(millisecond);
        return this;

    }

    public Response execute() {
        if (this.downloader == null) this.downloader = new HttpClientDownloader();
        Response response = exe();
        if (response != null) {
            if (response.getStatusCode() > 299 && response.getStatusCode() < 400) {
                if (this.maxRedirectTime > 0) { // 有剩余redirect 次数
                    this.maxRedirectTime--;
                    if (response.getLocation() != null) {
                        if (this.historyUrls == null) this.historyUrls = new ArrayList<>();
                        this.historyUrls.add(this.request.getUrl());

                        this.request.setUrl(response.getLocation());
                        this.request.setMethod(Method.GET);
                        return execute();
                    }
                } else {
                    return response;
                }
            }
        }
        if (this.historyUrls != null) this.historyUrls.add(this.request.getUrl());
        response.setHistoryUrls(this.historyUrls);
        return response;
    }


    private Response exe() {
        CookieStore cookieStore = fetchUtil.getCookies(taskId);
        if (cookieStore == null) cookieStore = new BasicCookieStore();
        this.request.setCookieStore(cookieStore);
        Response response = downloader.download(this.request);
        // 由于jsoup请求后，cookie为单独本次请求返回的cookie信息，HTTP client是所有cookie的融合体，所以，需要进行分开处理
        if (this.downloader instanceof JsoupDownloader) {
            // TODO JSOUP 的cookie信息处理
        } else if (this.downloader instanceof HttpClientDownloader) {
            fetchUtil.updateCookies(taskId, cookieStore);
        }
        if (response != null)
            logger.debug("task_id={},res={}", taskId, response.getResponseBody());
        else
            logger.debug("task_id={},res={}", taskId, null);
        return response;
    }


    public Map<String, String> strForm2Map(String... strings) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < strings.length; i += 2) {
            result.put(strings[i], strings[i + 1]);
        }
        return result;
    }

    public Map<String, String> json2Map(JSONObject cookies) {
        if (cookies == null) return null;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        cookies.keySet().forEach(k -> {
            hashMap.put(k, cookies.getString(k));
        });
        return hashMap;
    }
}
