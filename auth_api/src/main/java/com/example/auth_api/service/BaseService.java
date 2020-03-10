package com.example.auth_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * 功能：
 * 基础service
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 16:08
 */
@Component
public class BaseService {

    static Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    MongoTemplate mongoTemplate;
    @Value("${auth.config.coll.name}")
    String config_coll_name;
}
