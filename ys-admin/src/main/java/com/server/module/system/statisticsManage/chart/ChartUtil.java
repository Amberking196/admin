package com.server.module.system.statisticsManage.chart;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class ChartUtil {

    public static Map<String,String> baseFunctionMap;

    static {

        Map<String,String> funMap= Maps.newHashMap();
		funMap.put("machine","上线设备量");
		funMap.put("user","新增会员数");
		funMap.put("orderCount","订单数量");
		funMap.put("goodsCount","商品销售量");
		funMap.put("costPrice","商品销售额");
		funMap.put("onlyOneNumber","台平均销量");
		//funMap.put("customerprice","客价单");
		//funMap.put("noSaleMachineRate","无销量机器占比");
        baseFunctionMap= funMap;
               
    }



    public static String getBaseFunName(String key) {
        return baseFunctionMap.get(key);
    }
    public static boolean need(String[] functions,String fun){
		if(functions!=null && functions.length>0){
			for (String function : functions) {
				if(function.equals(fun)){
					return true;
				}
			}
		}
		return false;
	}
}
