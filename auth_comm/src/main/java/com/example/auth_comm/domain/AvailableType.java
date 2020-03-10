package com.example.auth_comm.domain;

import java.util.List;

/**
 * 功能：
 * 支持授权项实体
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 10:55
 */
public class AvailableType {
    String type; // 类型
    String name; // 名称
    List<String> child; // 子名称列表

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChild() {
        return child;
    }

    public void setChild(List<String> child) {
        this.child = child;
    }
}
