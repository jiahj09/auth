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

import java.net.URLEncoder;
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

    @Override
    public void login(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
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
        if (code != null ){
            taskUtils.setStatusDone(task_id,"登录成功~");
        }

    }
}
