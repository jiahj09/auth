package com.example.auth_fetch_operator.fetcher.cmcc;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.auth_fetch_operator.domain.BillInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/18 15:50
 */
//@SpringBootTest
public class CmccShopFetcherTest {


    @Test
    public void testParseBillInfo() throws Exception{
        String responseBody = "{\"data\":[{\"remark\":null,\"billMonth\":\"202003\",\"billStartDate\":\"20200301\",\"billEndDate\":\"20200331\",\"billFee\":\"68.00\",\"billMaterials\":[{\"remark\":null,\"billEntriy\":\"01\",\"billEntriyValue\":\"108.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"02\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"03\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"04\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"05\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"06\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"09\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"11\",\"billEntriyValue\":\"-40.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"14\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"15\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]}]},{\"remark\":null,\"billMonth\":\"202002\",\"billStartDate\":\"20200201\",\"billEndDate\":\"20200229\",\"billFee\":\"68.00\",\"billMaterials\":[{\"remark\":null,\"billEntriy\":\"01\",\"billEntriyValue\":\"108.00\",\"billMaterialInfos\":[{\"remark\":null,\"itemName\":\"18元语音包\",\"itemValue\":\"18.00\"},{\"remark\":null,\"itemName\":\"家庭亲情网全国版\",\"itemValue\":\"10.00\"},{\"remark\":null,\"itemName\":\"畅享套餐80元流量包\",\"itemValue\":\"80.00\"}]},{\"remark\":null,\"billEntriy\":\"02\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"03\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"04\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"05\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"06\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"09\",\"billEntriyValue\":\"-40.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"14\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"15\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]}]},{\"remark\":null,\"billMonth\":\"202001\",\"billStartDate\":\"20200101\",\"billEndDate\":\"20200131\",\"billFee\":\"68.00\",\"billMaterials\":[{\"remark\":null,\"billEntriy\":\"01\",\"billEntriyValue\":\"108.00\",\"billMaterialInfos\":[{\"remark\":null,\"itemName\":\"18元语音包\",\"itemValue\":\"18.00\"},{\"remark\":null,\"itemName\":\"家庭亲情网全国版\",\"itemValue\":\"10.00\"},{\"remark\":null,\"itemName\":\"畅享套餐80元流量包\",\"itemValue\":\"80.00\"}]},{\"remark\":null,\"billEntriy\":\"02\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"03\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"04\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"05\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"06\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"09\",\"billEntriyValue\":\"-40.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"14\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"15\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]}]},{\"remark\":null,\"billMonth\":\"201912\",\"billStartDate\":\"20191201\",\"billEndDate\":\"20191231\",\"billFee\":\"68.00\",\"billMaterials\":[{\"remark\":null,\"billEntriy\":\"01\",\"billEntriyValue\":\"108.00\",\"billMaterialInfos\":[{\"remark\":null,\"itemName\":\"18元语音包\",\"itemValue\":\"18.00\"},{\"remark\":null,\"itemName\":\"家庭亲情网全国版\",\"itemValue\":\"10.00\"},{\"remark\":null,\"itemName\":\"畅享套餐80元流量包\",\"itemValue\":\"80.00\"}]},{\"remark\":null,\"billEntriy\":\"02\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"03\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"04\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"05\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"06\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"09\",\"billEntriyValue\":\"-40.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"14\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]},{\"remark\":null,\"billEntriy\":\"15\",\"billEntriyValue\":\"0.00\",\"billMaterialInfos\":[]}]},{\"remark\":null,\"billMonth\":\"201911\",\"billStartDate\":null,\"billEndDate\":null,\"billFee\":\"-0\",\"billMaterials\":[]},{\"remark\":null,\"billMonth\":\"201910\",\"billStartDate\":null,\"billEndDate\":null,\"billFee\":\"-0\",\"billMaterials\":[]}],\"retCode\":\"000000\",\"retMsg\":\"成功\",\"sOperTime\":\"20200319131418\"}";
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        //TODO parse origin data to object
        List<BillInfo> result = new ArrayList<>();
        BillInfo billInfo = new BillInfo();

        String data1 = jsonObject.getString("data");

        JSONArray data = jsonObject.getJSONArray("data");

        List<BillInfo> billInfos = JSONArray.parseArray(data1, BillInfo.class);
        System.out.println("");
    }

}