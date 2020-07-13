package com.server.module.customer.questionnaire;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.server.util.SmsSendRequest;
import com.server.util.SmsSendUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class QuestionnaireUtils {

    public static Map<Integer,String> userTypeMap= Maps.newHashMap();
    private static Map<Integer,String> sendFlagMap=Maps.newHashMap();

    static {

        userTypeMap.put(0,"新用户");
        userTypeMap.put(1,"一次");
        userTypeMap.put(2,"活跃");
        userTypeMap.put(3,"忠诚");
        userTypeMap.put(4,"低频");
        userTypeMap.put(5,"流失");
        userTypeMap.put(6,"回流");


        sendFlagMap.put(0,"全部");
        sendFlagMap.put(1,"已发");
        sendFlagMap.put(2,"未发");
    }

    public static Map getUserTypeMap(){
        return userTypeMap;
    }

    public static Map getSendFlagMap(){
        return sendFlagMap;
    }


    public  static void sendMessage(){
        String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
        // 手机号码
        SmsSendRequest smsSingleRequest = new SmsSendRequest("这是优水到家", "13570339593");

        String requestJson = JSON.toJSONString(smsSingleRequest);

        System.out.println(requestJson);
        String response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
        if (response == null) {
            response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
        }
        System.out.println(response);

    }

    public static void main(String[] args) {
        sendMessage();
        //【优水到家】你好，优水到家想请你帮忙做个问卷调查，提交答案后会获得2块钱优惠劵。http://yos.youshuidaojia.com/wenjuandiaocha?id=123
        //
    }
}
