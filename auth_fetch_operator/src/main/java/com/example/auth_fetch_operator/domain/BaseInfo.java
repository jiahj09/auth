package com.example.auth_fetch_operator.domain;

/**
 * 功能：
 * 个人基础信息
 *
 * @Author:JIUNLIU
 * @data : 2020/3/19 15:23
 */
public class BaseInfo {

    private String phone;
    private String name; // 姓名
    private String inNetDate;// 入网时间 20191201150434  年月日，时分秒
    private String netAge;//网龄 计算月份个数即可。
    private String address; // 所在地
    private String zipCode;// 邮政编码
    private String contactNum; // 紧急联系人电话号码
    private String curPlanName; // 当前套餐名称
    private String idNum;// 身份证号码
    private String email;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInNetDate() {
        return inNetDate;
    }

    public void setInNetDate(String inNetDate) {
        this.inNetDate = inNetDate;
    }

    public String getNetAge() {
        return netAge;
    }

    public void setNetAge(String netAge) {
        this.netAge = netAge;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getCurPlanName() {
        return curPlanName;
    }

    public void setCurPlanName(String curPlanName) {
        this.curPlanName = curPlanName;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }
}
