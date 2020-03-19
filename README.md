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