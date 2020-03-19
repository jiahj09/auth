package com.example.auth_fetch_operator.fetcher;

import com.example.auth_comm.utils.ContextUtil;
import com.example.auth_comm.utils.TaskUtils;
import com.example.auth_fetch_operator.utils.MobileSegmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.example.auth_comm.utils.FetchUtil;

/**
 * 功能：
 * 运营商默认的方法
 *
 * @Author:JIUNLIU
 * @data : 2020/3/8 11:08
 */
@Component
public class BasicFetcher {
    public static Logger logger = LoggerFactory.getLogger(BasicFetcher.class);

    @Autowired
    public TaskUtils taskUtils;
    @Autowired
    public MobileSegmentUtil mobileSegmentUtil;
    @Autowired
    public FetchUtil fetchUtil;
    @Autowired
    public MongoTemplate mongoTemplate;
    @Value("${operator.channel.collname}")
    public String channel_coll_name;

    /**
     * 初始化，默认为需要手机号码和密码
     *
     * @param task_id
     */
    public void init(String task_id) {

    }

    /**
     * 登录步骤
     *
     * @param task_id
     */
    public void login(String task_id) {
    }

    /**
     * 登录的短信校验
     *
     * @param task_id
     */
    public void login_sms(String task_id) {
    }

    /**
     * 基本信息
     *
     * @param task_id
     */
    public void base_info(String task_id) {
    }

    /**
     * 基本信息的短信校验
     *
     * @param task_id
     */
    public void base_sms(String task_id) {
    }

    /**
     * 账单
     *
     * @param task_id
     */
    public void bill_info(String task_id) {
    }

    /**
     * 账单的短信校验
     *
     * @param task_id
     */
    public void bil_sms(String task_id) {
    }

    /**
     * 详单
     *
     * @param task_id
     */
    public void call_info(String task_id) {
    }

    /**
     * 详单的短信校验
     *
     * @param task_id
     */
    public void call_sms(String task_id) {
    }

    /**
     * 自定义的处理方式。
     *
     * @param task_id
     */
    public void custom_deal(String task_id) {
    }

    /**
     * 开始抓取基础信息
     *
     * @param task_id
     */
    @Async("owen_executor")
    public void startFetchBase(String task_id) {
        BasicFetcher obj = ContextUtil.getObj(this.getClass());
        obj.base_info(task_id);
    }

    /**
     * 开始抓取账单信息
     *
     * @param task_id
     */
    @Async("owen_executor")
    public void startFetchBill(String task_id) {
        BasicFetcher obj = ContextUtil.getObj(this.getClass());
        obj.bill_info(task_id);
    }

    /**
     * 开始抓取详单信息
     *
     * @param task_id
     */
    @Async("owen_executor")
    public void startFetchCall(String task_id) {
        BasicFetcher obj = ContextUtil.getObj(this.getClass());
        obj.call_info(task_id);
    }
}
