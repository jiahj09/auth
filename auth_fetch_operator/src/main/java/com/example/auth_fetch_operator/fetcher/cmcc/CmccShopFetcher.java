package com.example.auth_fetch_operator.fetcher.cmcc;

import com.alibaba.fastjson.JSONObject;
import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_fetch_operator.fetcher.BasicFetcher;
import org.springframework.stereotype.Component;
import webspider.WRequest;
import webspider.http.Method;
import webspider.http.Response;
import webspider.utils.JSEngineUtils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：
 * 中国移动商城抓取
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 10:35
 */
@Component("cmcc_shop")
public class CmccShopFetcher extends BasicFetcher {

    private Map<String, String> commHeader = new HashMap<String, String>() {{
        put("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        put("Referer", "https://login.10086.cn/");
        put("Sec-Fetch-Dest", "image");
        put("Sec-Fetch-Site", "same-origin");
        put("Host", "login.10086.cn");
        put("Accept-Encoding", "gzip, deflate, br");
        put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
        put("Sec-Fetch-Mode", "no-cors");
    }};

    private Map<String, String> dataHeader = new HashMap<String, String>() {{
        put("expires", "0");
        put("Accept", "application/json, text/javascript, */*; q=0.01");
        put("X-Requested-With", "XMLHttpRequest");
        put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        put("Sec-Fetch-Dest", "empty");
        put("Sec-Fetch-Site", "same-origin");
        put("Host", "shop.10086.cn");
        put("Accept-Encoding", "gzip, deflate, br");
        put("pragma", "no-cache");
        put("Sec-Fetch-Mode", "cors");
        put("Cache-Control", "no-store, must-revalidate");
        put("If-Modified-Since", "0");
        put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
        put("Content-Type", "*");
    }};


    @Override
    public void init(String task_id) {
        taskUtils.setStatusInput(task_id, new ArrayList<ParamEnum>() {{
            add(ParamEnum.PHONE);
            add(ParamEnum.LOGIN_PASSWORD);
        }});
        taskUtils.setNextStep(task_id, StepEnum.LOGIN);
    }

    @Override
    public void login(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
        WRequest.create(task_id)
                .connect("https://login.10086.cn/")
                .headers(commHeader)
                .execute();
        WRequest.create(task_id)
                .connect("https://login.10086.cn/loadSendflag.htm?timestamp=")
                .headers(commHeader)
                .execute();
        WRequest.create(task_id)
                .connect("https://login.10086.cn/genqr.htm")
                .headers(commHeader)
                .execute();
        WRequest.create(task_id)
                .connect("https://login.10086.cn/captchazh.htm?type=12")
                .headers(commHeader)
                .execute();
        WRequest.create(task_id)
                .connect("https://login.10086.cn/checkUidAvailable.action")
                .headers(commHeader)
                .method(Method.POST)
                .execute();
        WRequest.create(task_id)
                .connect("https://login.10086.cn/needVerifyCode.htm?accountType=01&account=" + phone + "&timestamp=" + System.currentTimeMillis())
                .headers(commHeader)
                .execute();
        boolean b = sendLoginSMS(task_id);
        if (b) {
            logger.info("task_id={},msg={}", task_id, "登录短信发送成功");

            taskUtils.setStatusInput(task_id, ParamEnum.SMS_CODE);
            taskUtils.setNextStep(task_id, StepEnum.LOGIN_SMS);
        } else {
            logger.info("task_id={},msg={}", task_id, "登录短信发送失败");
            taskUtils.setStatusError(task_id, "登录短信验证码发送失败，请稍后再试~");
        }

    }

    /**
     * 发送短信验证码
     *
     * @param task_id
     */
    public boolean sendLoginSMS(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
        WRequest.create(task_id)
                .connect("https://login.10086.cn/chkNumberAction.action")
                .headers(commHeader)
                .method(Method.POST)
                .form(new HashMap<String, String>() {{
                    put("userName", phone);
                    put("loginMode", "01");
                    put("channelID", "10000");
                }}).execute();

        Response response = WRequest.create(task_id)
                .connect("https://login.10086.cn/loadToken.action")
                .headers(commHeader)
                .method(Method.POST)
                .form("userName", phone)
                .execute();
        JSONObject jsonObject = JSONObject.parseObject(response.getResponseBody());
        String result = jsonObject.getString("result");

        Response execute = WRequest.create(task_id)
                .connect("https://login.10086.cn/sendRandomCodeAction.action")
                .headers(new HashMap<String, String>() {{
                    put("Origin", "https://login.10086.cn");
                    put("Accept", "application/json, text/javascript, */*; q=0.01");
                    put("X-Requested-With", "XMLHttpRequest");
                    put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
                    put("Referer", "https://login.10086.cn/");
                    put("Sec-Fetch-Dest", "empty");
                    put("Xa-before", result);
                    put("Sec-Fetch-Site", "same-origin");
                    put("Host", "login.10086.cn");
                    put("Accept-Encoding", "gzip, deflate, br");
                    put("Sec-Fetch-Mode", "cors");
                    put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
                    put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                }})
                .method(Method.POST)
                .form(new HashMap<String, String>() {{
                    put("userName", phone);
                    put("type", "01");
                    put("channelID", "12003");
                }}).execute();
        String responseBody = execute.getResponseBody();
        if (responseBody != null && responseBody.equalsIgnoreCase("0")) {
            return true;
        } else {
            logger.info("task_id={},resp={}", task_id, responseBody);
            return false;
        }
    }


    @Override
    public void login_sms(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
        String loginPassword = taskUtils.getInputParam(task_id, ParamEnum.LOGIN_PASSWORD);
        String smsCode = taskUtils.getInputParam(task_id, ParamEnum.SMS_CODE);
        String encrypt = JSEngineUtils.encryption("js/cmcc/cmcc_shop.js", "encrypt", loginPassword);

        Response response = WRequest.create(task_id)
                .connect("https://login.10086.cn/login.htm?" +
                        "accountType=01" +
                        "&account=" + phone +
                        "&password=" + URLEncoder.encode(encrypt) +
                        "&pwdType=01" +
                        "&smsPwd=" + smsCode +
                        "&inputCode=" +
                        "&backUrl=https%3A%2F%2Fshop.10086.cn%2Fi%2F" +
                        "&rememberMe=0" +
                        "&channelID=12003" +
                        "&loginMode=01" +
                        "&protocol=https%3A" +
                        "&timestamp=" + System.currentTimeMillis())
                .headers(commHeader)
                .execute();
        JSONObject resObj = JSONObject.parseObject(response.getResponseBody());
        String code = resObj.getString("code");
        if (code != null && code.equalsIgnoreCase("0000")) {
            //https://shop.10086.cn/i/v1/auth/getArtifact?backUrl=https%3A%2F%2Fshop.10086.cn%2Fi%2F&artifact=b5b350017c9f400cb0b8cce06e44b8a7&type=00
            //https://shop.10086.cn/i/v1/auth/getArtifact?backUrl=https://shop.10086.cn/i/&artifact=b5b350017c9f400cb0b8cce06e44b8a7&type=00
            String artifact = resObj.getString("artifact");
            String type = resObj.getString("type");
            String assertAcceptURL = resObj.getString("assertAcceptURL");
            String httpUrl = assertAcceptURL + "?backUrl=" + URLEncoder.encode("https://shop.10086.cn/i/") + "&artifact=" + artifact + "&type=" + type;
            Response execute = WRequest.create(task_id)
                    .connect(httpUrl)
                    .headers(commHeader)
                    .execute();
            URL currentURL = execute.getCurrentURL();
            fetchUtil.setField(task_id, "curr_url", currentURL.toString());

            taskUtils.setStatusDone(task_id, "登录成功~");

            startFetchBase(task_id);
            startFetchBill(task_id);
        } else {// TODO 错误消息的相关配置

        }

    }


    @Override
    public void bill_info(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
        String curr_url = fetchUtil.getField(task_id, "curr_url");
        //                    put("Referer", curr_url);
        Response response = WRequest.create(task_id)
                .connect("https://shop.10086.cn/i/v1/fee/billinfo/" + phone + "?_=" + System.currentTimeMillis())
                .headers(dataHeader)
                .header("Referer", curr_url)
                .execute();
        String responseBody = response.getResponseBody();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        //TODO parse origin data to object
    }


    @Override
    public void base_info(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
        String curr_url = fetchUtil.getField(task_id, "curr_url");
        Response response = WRequest.create(task_id)
                .connect("https://shop.10086.cn/i/v1/cust/mergecust/" + phone + "?_=" + System.currentTimeMillis())
                .headers(dataHeader)
                .header("Referer", curr_url)
                .execute();
        String responseBody = response.getResponseBody();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
    }


    @Override
    public void call_info(String task_id) {
        System.err.println("北京电信。进来啦~~~~~~~~~~~~~~");
        throw new RuntimeException("处理异常啦");
    }
}
