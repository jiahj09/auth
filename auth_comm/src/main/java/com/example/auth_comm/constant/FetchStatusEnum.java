package com.example.auth_comm.constant;

/**
 * 功能：
 * 记录数据抓取的状态。
 *
 * @Author:JIUNLIU
 * @data : 2020/3/19 11:06
 */
public enum FetchStatusEnum {
    START("开始数据抓取"),     // 开始数据抓取
    END("结束数据抓取"),//结束数据抓取
    SAVED("数据已保存"),//数据已保存
    ERROR("数据抓取异常");//数据抓取异常
    String msg;

    FetchStatusEnum(String msg) {
        this.msg = msg;
    }
}
