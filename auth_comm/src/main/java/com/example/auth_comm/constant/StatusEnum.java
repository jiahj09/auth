package com.example.auth_comm.constant;

/**
 * 功能：
 * 状态相关枚举
 *
 * @Author:JIUNLIU
 * @data : 2020/3/6 14:57
 */
public enum StatusEnum {
    SUCCESS("接口访问成功"),     // 接口访问成功
    DOING("任务正在执行"), // 正在执行
    ERROR("任务处理异常"), // 任务错误
    INPUT("任务需要输入数据"), // 等待输入
    PROCESSING("抓取任务正在进行"), // 抓取任务正在进行
    FINISH("整个授权已经完成"),//整个授权已经完成
    DONE("交互结束"); // 交互结束

    String msg;

    StatusEnum(String msg) {
        this.msg = msg;
    }
}
