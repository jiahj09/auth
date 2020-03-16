package com.example.auth_fetch_operator.fetcher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_fetch_operator.AuthFetchOperatorApplication;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/8 11:13
 */
@Component
public class FetchFactory extends BasicFetcher {

    public BasicFetcher getFetcher(String task_id) {
        String resultBeanName;
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);

        String phoneOwenBeanName = getPhoneOwenBeanName(phone);
        if (phoneOwenBeanName != null) { // 手机号码被单独分配通道
            resultBeanName = phoneOwenBeanName;
        } else { //手机号码并没有被单独分配通道
            JSONObject segmentObj = mobileSegmentUtil.mobileSegment(phone);
            String carrier = segmentObj.getString("carrier");
            List<String> carrierBeans = getCarrierBeans(carrier);
            if (carrierBeans != null) { // 该省份已经被确定固定渠道，按照固定渠道选择即可。
                resultBeanName = randomBean(carrierBeans);
            } else {// 没有被设定渠道信息。
                String catName = segmentObj.getString("catName");
                List<String> catCarrierBeans = getCatCarrierBeans(catName, carrier);
                resultBeanName = randomBean(catCarrierBeans);
            }
        }
        if (resultBeanName == null) return null;
        return (BasicFetcher) AuthFetchOperatorApplication.applicationContext.getBean(resultBeanName);
    }


    public String randomBean(List<String> beans) {
        if (beans == null) return null;
        if (beans.size() == 1) {
            return beans.get(0);
        } else {
            return beans.get((int) (Math.random() * beans.size()));
        }
    }


    /**
     * 没有被单独配置的情况下，查询所有可以被支持的bean
     *
     * @param catName
     * @param carrier
     * @return
     */
    public List<String> getCatCarrierBeans(String catName, String carrier) {
        List<String> result = new ArrayList<>();
        Query query = new Query(Criteria.where("catName").is(catName));
        List<JSONObject> catChannelObj = mongoTemplate.find(query, JSONObject.class, channel_coll_name);
        if (catChannelObj == null) return null;
        else {
            JSONObject catObj = catChannelObj.get(0);
            JSONArray unity = catObj.getJSONArray("unity");
            for (int i = 0; i < unity.size(); i++) {
                JSONObject unityObj = unity.getJSONObject(i);
                String bean = unityObj.getString("bean");
                result.add(bean);
            }
            JSONArray carrierArray = catObj.getJSONArray(carrier);
            for (int i = 0; i < carrierArray.size(); i++) {
                JSONObject jsonObject = carrierArray.getJSONObject(i);
                String bean = jsonObject.getString("bean");
                result.add(bean);
            }
            return result;
        }
    }

    /**
     * 获取一个省份的运营商 已经被固定好的所有能走的渠道
     *
     * @param carrier
     * @return
     */
    public List<String> getCarrierBeans(String carrier) {
        Query query = new Query(Criteria.where("carrier").is(carrier));
        List<JSONObject> carrierChannelObj = mongoTemplate.find(query, JSONObject.class, channel_coll_name);
        if (carrierChannelObj == null) {
            return null;
        } else {
            JSONObject carrierChannel = carrierChannelObj.get(0);
            JSONArray beans = carrierChannel.getJSONArray("beans");
            List<String> result = new ArrayList<>();

            for (int i = 0; i < beans.size(); i++) {
                String string = beans.getString(i);
                result.add(string);
            }
            return result;
        }
    }


    /**
     * 用于返回手机固定通道配置
     * 手机号码如果被分配到固定的通道的话，按照固定的通道执行
     *
     * @param phone
     * @return
     */
    public String getPhoneOwenBeanName(String phone) {
        Query query = new Query(Criteria.where("phone").is(phone));
        List<JSONObject> phoneChannelObj = mongoTemplate.find(query, JSONObject.class, channel_coll_name);
        if (phoneChannelObj != null) { // 手机号码被单独分配通道
            JSONObject jsonObject = phoneChannelObj.get(0);
            String beanName = jsonObject.getString("bean");
            return beanName;
        }
        return null;
    }


    @Override
    public void init(String task_id) {
        taskUtils.setStatusInput(task_id, new ArrayList<ParamEnum>() {{
            add(ParamEnum.PHONE);
            add(ParamEnum.LOGIN_PASSWORD);
        }});
        taskUtils.setNextStep(task_id, StepEnum.LOGIN);
    }

    @Override
    public void login(String task_id) {
        BasicFetcher fetcher = getFetcher(task_id);
        if (fetcher == null) {
            taskUtils.setStatusError(task_id, "当前手机号码不支持");
            return;
        }
        fetcher.login(task_id);
    }

    @Override
    public void login_sms(String task_id) {
        getFetcher(task_id).login_sms(task_id);
    }

    @Override
    public void base(String task_id) {
        getFetcher(task_id).base(task_id);
    }

    @Override
    public void base_sms(String task_id) {
        getFetcher(task_id).base_sms(task_id);
    }

    @Override
    public void bill(String task_id) {
        getFetcher(task_id).bill(task_id);
    }

    @Override
    public void bil_sms(String task_id) {
        getFetcher(task_id).bil_sms(task_id);
    }

    @Override
    public void call(String task_id) {
        getFetcher(task_id).call(task_id);
    }

    @Override
    public void call_sms(String task_id) {
        getFetcher(task_id).call_sms(task_id);
    }
}
