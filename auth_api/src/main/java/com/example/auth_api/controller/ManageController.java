package com.example.auth_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能：
 * 用于管理授权系统的信息。
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 10:16
 */
@CrossOrigin
@RestController
@RequestMapping("manage")
public class ManageController {

    /**
     * 增加一个授权支持项目
     */
    public void addSupportType() {

    }

    /**
     * 删除一个授权支持项目
     */
    public void deleteSupportType() {

    }

    /**
     * 注册一个用户
     * <p>
     * 用户具有自己对应的授权支持项目列表
     */
    public void registerUser() {

    }

    /**
     * 更新用户权限
     */
    public void updateUserAuthority() {

    }

    @RequestMapping("test")
    public ResponseEntity<Boolean> test() {
        boolean result = true;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
