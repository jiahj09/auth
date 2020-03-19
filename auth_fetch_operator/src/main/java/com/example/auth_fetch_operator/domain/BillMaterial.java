package com.example.auth_fetch_operator.domain;

/**
 * 功能：
 * 账单详情
 *
 * @Author:JIUNLIU
 * @data : 2020/3/19 15:54
 */
public class BillMaterial {
    private String remark;
    private String billEntriy;
    private String billEntriyValue;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBillEntriy() {
        return billEntriy;
    }

    public void setBillEntriy(String billEntriy) {
        this.billEntriy = billEntriy;
    }

    public String getBillEntriyValue() {
        return billEntriyValue;
    }

    public void setBillEntriyValue(String billEntriyValue) {
        this.billEntriyValue = billEntriyValue;
    }
}
