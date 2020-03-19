# auth
授权crawler常用异步框架

## 框架及流程图
![image](https://github.com/JIUNLIU233/auth/blob/master/pic/%E6%8E%88%E6%9D%83%E6%B5%81%E7%A8%8B%E5%9B%BE.png)

## task_util 不可用字段（系统占用）
```aidl
    task_id
    status
    input_fields
    input_params
    next_step
    type
    msg
    fetching_status:processing/finish
```

## fetch_util 不可用字段（系统占用）
```$xslt
    fetch_bean  用于记录本次任务所采用的bean 名称，eg：cmcc_shop
    carrier     用于记录手机号码所属详细归属      eg：北京移动
    cat_name     用于记录运营商类型              eg：中国移动，中国电信，中国联通
    custom_step 用于自定义步骤是，输入的下一步方法  参考中国移动商城
```

## 测试用Mongo 数据配置
```
数据库名称：auth
项目支持项目表：auth_config
配置数据：
{
    "_id" : ObjectId("5e63557501e4cc6c63001ef9"),
    "config_name" : "type_config",
    "create_by" : "JIUN·LIU",
    "update_time" : ISODate("2020-03-07T08:04:05.722Z"),
    "last_update_by" : "JIUN·LIU",
    "create_time" : ISODate("2020-03-07T08:04:05.722Z"),
    "prop" : [ 
        {
            "name" : "运营商",
            "type" : "operator",
            "child_prop" : [ 
                {
                    "name" : "北京移动"
                }, 
                {
                    "name" : "北京电信"
                }
            ],
            "child_flag" : false
        }, 
        {
            "name" : "社保",
            "type" : "insurance",
            "child_prop" : [ 
                {
                    "name" : "北京市",
                    "type" : "beijing"
                }, 
                {
                    "name" : "山西省",
                    "type" : "shanxi",
                    "child_prop" : [ 
                        {
                            "name" : "太原市",
                            "type" : "taiyuan"
                        }, 
                        {
                            "name" : "晋中市",
                            "type" : "jinzhong"
                        }
                    ],
                    "child_flag" : true
                }
            ],
            "child_flag" : true
        }
    ]
}

爬虫渠道配置：channel_config
测试数据用例：

所有支持列表：
{
    "_id" : ObjectId("5e6f0447fea90961e10d8b50"),
    "catName" : "中国移动",
    "unity" : [ 
        {
            "name" : "移动商城",
            "bean" : "cmcc_shop"
        }
    ],
    "北京移动" : [ 
        {
            "name" : "web",
            "bean" : "cmcc_beijing_web"
        }
    ],
    "channel_type" : "operator",
    "desc" : "中国移动所有可用通道列表"
}
按照渠道配置可用列表
{
    "_id" : ObjectId("5e6f0a49fea90961e10d8c0f"),
    "carrier" : "北京移动",
    "beans" : [ 
        "cmcc_shop"
    ]
}
单独配置手机号码可用列表
{
    "_id" : ObjectId("5e6f0cbbfea90961e10d8c61"),
    "phone" : "这个字段请输入对应的手机号码",
    "bean" : "cmcc_shop"
}
```