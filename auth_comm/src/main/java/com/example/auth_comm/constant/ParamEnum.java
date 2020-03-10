package com.example.auth_comm.constant;

import com.alibaba.fastjson.JSONObject;

/**
 * 功能：
 * 系统中所有参数的枚举
 *
 * @Author:JIUNLIU
 * @data : 2020/3/6 14:44
 */
public enum ParamEnum {

    /**
     * 一个参数所需要具备的要素：
     * key：用于解析参数
     * 正则：用于检测参数是否输入正确
     * 提示：用于提示用于进行输入
     * 描述：用于描述参数作用
     */
    USERNAME("username", null, "请输入用户名", "用户名，账号信息"),
    PHONE("phone", "^[1]([3-9])[0-9]{9}$", "请输入手机号码", "手机号码"),
    REAL_NAME("real_name", null, "请输入您的真实姓名", "用户真实姓名"),
    ID_CARD("id_card", "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", "请输入您的身份证号码", "用户身份证号码"),
    SMS_CODE("sms_code", null, "请输入您收到的短信验证码", "短信验证码"),
    CAPTCHA_CODE("captcha_code", null, "请输入对应的验证码信息", "图片验证码"),
    LOGIN_PASSWORD("login_password", null, "请输入密码", "登录密码");

    String key;
    String pattern;
    String prompt;
    String desc;

    ParamEnum(String key, String pattern, String prompt, String desc) {
        this.key = key;
        this.pattern = pattern;
        this.prompt = prompt;
        this.desc = desc;
    }

    public String getKey(){
        return this.key;
    }

    @Override
    public String toString() {
        return new JSONObject(true) {{
            put("key", key);
            put("pattern", pattern);
            put("prompt", prompt);
            put("desc", desc);
        }}.toString();
    }
}
