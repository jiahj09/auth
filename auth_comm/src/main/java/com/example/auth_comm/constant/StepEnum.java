package com.example.auth_comm.constant;

/**
 * 功能：
 * 用于记录步骤的枚举类型
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 22:21
 */
public enum StepEnum {

    /**
     * 通用
     */
    INIT("INIT"),
    LOGIN("LOGIN"),
    LOGIN_SMS("LOGIN_SMS"),
    BASE("BASE"),
    BASE_SMS("BASE_SMS"),
    BILL("BILL"),
    BILL_SMS("BILL_SMS"),
    CALL("CALL"),
    CALL_SMS("CALL_SMS"),
    ;

    String code;

    StepEnum(String code) {
        this.code = code;
    }

    public static StepEnum getStepByValue(String code) {
        for (StepEnum stepEnum : values()) {
            if (stepEnum.code.equals(code)) return stepEnum;
        }
        return null;
    }

}
