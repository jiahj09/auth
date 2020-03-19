package com.example.auth_fetch_operator.domain;

import java.util.List;

/**
 * 功能：
 * 账单信息实体
 *
 * @Author:JIUNLIU
 * @data : 2020/3/19 15:24
 */
public class BillInfo {

    private String billMonth;
    private String billStartDate;
    private String billEndDate;
    private float billFee;
    private String remark;
    private List<BillMaterial> billMaterials;

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getBillStartDate() {
        return billStartDate;
    }

    public void setBillStartDate(String billStartDate) {
        this.billStartDate = billStartDate;
    }

    public String getBillEndDate() {
        return billEndDate;
    }

    public void setBillEndDate(String billEndDate) {
        this.billEndDate = billEndDate;
    }

    public float getBillFee() {
        return billFee;
    }

    public void setBillFee(float billFee) {
        this.billFee = billFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BillMaterial> getBillMaterials() {
        return billMaterials;
    }

    public void setBillMaterials(List<BillMaterial> billMaterials) {
        this.billMaterials = billMaterials;
    }
}
