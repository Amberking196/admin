package com.server.module.system.machineManage.machineList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;


@Component
public class CommandParser {


	private final static Logger log = LogManager.getLogger(CommandParser.class);
	/**
	 * 解析版本1关门后机器返回命令参数
	 * @author hebiting
	 * @date 2018年11月20日上午10:45:32
	 * @param params
	 * @return
	 */
	public static MachinesInfo parseVer1(String params) {
		// t:2;n:a00;d:1,1,1,1;s:0;p:0;w:3700,3500,2500,3650&45
		//t:2;n:111380310098;d:0,0,0,0;s:0;p:1;w:10492,0,62584,0&79
		if(params == null){
			return null;
		}
		MachinesInfo machinesInfo = new MachinesInfo();
		machinesInfo.setVersion(1);
		machinesInfo.setParams(params);
		//log.info("waiting parseVer1 command = "+params);
		String[] arr1 = params.split("&");
		String[] ps = arr1[0].split(";");
		for (String str : ps) {
			String[] temp = str.split(":");
			if (temp[0].equals("t")) {
				
			} else if (temp[0].equals("n")) {
				machinesInfo.setFactoryNumber(temp[1]);
			} else if (temp[0].equals("d")) {
				String[] ways = temp[1].split(",");
				machinesInfo.setTotalWayNum(ways.length);
				for (int i = 0; i < ways.length; i++) {
					ItemChangeDto itemChange = new ItemChangeDto();
					itemChange.setWayNum(i+1);
					itemChange.setOrderNum(1);
					itemChange.setState(ways[i]);
					machinesInfo.getItemChangeList().add(itemChange);
				}
			} else if (temp[0].equals("s")) {
				machinesInfo.setState(temp[1]);
			} else if (temp[0].equals("p")) {
				machinesInfo.setPower(temp[1]);
			} else if (temp[0].equals("w")) {
				String[] weights = temp[1].split(",");
				for (int i = 0; i < weights.length; i++) {
					ItemChangeDto itemChange = machinesInfo.getItemChangeList().get(i);
					itemChange.setWayWeight(Integer.valueOf(weights[i]));
				}
			} else if (temp[0].equals("c")) {
				machinesInfo.setTemperature(temp[1]);
			} else if (temp[0].equals("g")) {
				//规格暂不处理
			} else {
				//log.info(temp[0]+"参数格式有误");
			}
		}
        return machinesInfo;
	}
	
	
	
//	/**
//	 * 解析多商品机器关门后机器返回参数命令
//	 * @author hebiting
//	 * @date 2018年11月20日上午10:47:39
//	 * @param params
//	 * @return
//	 */
//	public static MachinesInfo moreGoodsParse(String params){
//		//factNum:*****;goods:1;1:-1,2:-2;goods:2;1:-1,2:-2; goods:3;1:-1,2:-2; goods:4;1:-1,2:-2;all_closed
//		return moreGoodsAllPaser(params);
//	}
//	
//	/**
//	 * 单货道多商品机器在重量变化时的返回参数解析
//	 * @author hebiting
//	 * @date 2018年11月20日上午10:50:31
//	 * @param params
//	 * @return
//	 */
//	public static List<ItemChangeDto> itemChangeParse(String params,String vmCode){
//		log.debug("waiting itemChangeParse command = "+params);
//		MachinesInfo moreGoodsAllPaser = moreGoodsAllPaser(params);
//		if(moreGoodsAllPaser!=null){
//			return moreGoodsAllPaser.getItemChangeList();
//		}
//		return new ArrayList<ItemChangeDto>();
//	}
//	
//	
//	public static MachinesInfo moreGoodsAllPaser(String params){
//		//factNum:*****;goods:1;1:-1,2:-2;goods:2;1:-1,2:-2; goods:3;1:-1,2:-2; goods:4;1:-1,2:-2;all_closed
//		//factNum:*****;goods:3;1:-1000,2:-1000;end
//		if(StringUtils.isBlank(params)){
//			return null;
//		}
//		MachinesInfo machinesInfo = new MachinesInfo();
//		int currWayNum = 1;
//		int totalWayNum = 0;
//		machinesInfo.setVersion(CommandVersionEnum.VER2.getState());
//		String[] split = params.split(";");
//		for (String param : split) {
//			String currParam = param.trim();
//			if(currParam.contains("factNum:") && StringUtils.isBlank(machinesInfo.getFactoryNumber())){
//				machinesInfo.setFactoryNumber(currParam.split(":")[1].trim());
//			}else if(currParam.contains("goods:")){
//				currWayNum = Integer.valueOf(currParam.split(":")[1].trim());
//				totalWayNum++;
//			}else if(currParam.contains("1:")){
//				String[] wayInfo = currParam.split(",");
//				for(String change : wayInfo){
//					String[] oneChange = change.split(":");
//					if(oneChange.length == 2){
//						Integer orderNum = Integer.valueOf(oneChange[0].trim());
//						Integer changeNum = Integer.valueOf(oneChange[1].trim());
//						ItemChangeDto changeInfo = new ItemChangeDto();
//						changeInfo.setWayNum(currWayNum);
//						changeInfo.setChangeNum(changeNum);
//						changeInfo.setOrderNum(orderNum);
//						changeInfo.setAbsChangeNum(Math.abs(changeNum));
//						changeInfo.setWaitPayNum(changeInfo.getAbsChangeNum());
//						machinesInfo.getItemChangeList().add(changeInfo);
//					}
//				}
//			}else if(currParam.contains("all_closed")){
//				//门全开
//				machinesInfo.setTotalWayNum(totalWayNum);
//			}else if(currParam.contains("end")){
//				//开一门
//			}else{
//				log.error(params+"-错误的命令："+currParam);
//			}
//		}
//		return machinesInfo;
//	}
//	
//	public static MachinesInfo parseVer3(String params) {
//		//status:1;order:111111;factoryNumber:***
//		MachinesInfo machinesInfo = new MachinesInfo();
//		machinesInfo.setVersion(CommandVersionEnum.VER3.getState());
//		String param[]=params.split(";");
//		for(String s : param) {
//			String finalParam[]=s.split(":");
//			if(finalParam[0].equals("factoryNumber")) {
//				machinesInfo.setFactoryNumber(finalParam[1].trim());
//			}else if(finalParam[0].equals("order")) {
//				machinesInfo.setOrderId(finalParam[1].trim());
//			}else {
//				
//			}
//		}
//		return machinesInfo;
//	}
////	public static Map<String,String> explainNum(String params){
////		//factNum:*****;goods:1;1:-1,2:-2;goods:2;1:-1,2:-2; goods:3;1:-1,2:-2; goods:4;1:-1,2:-2;all_closed
////		//"nongfushanquan5000ml":"2","dayibao4500ml","3",...
////		//'nongfushanquan5000ml': '2','dayibao4500ml', '3',...
////		Map<String,String> goodsNumMap=new HashMap<String,String> ();
////		params=params.substring(1,params.length()-1);
////		String[] split = params.split(",");
////		for (String param : split) {
////			String currParam = param.trim();currParam=currParam.trim();
////			if(currParam.contains(":")){
////				String[] wayInfo = currParam.split(":");
////				goodsNumMap.put(wayInfo[0].replace("'", ""),wayInfo[1].replace("'", "").trim());
////			}else {
////				log.error(params+"-错误的命令："+currParam);
////			}
////		}
////		return goodsNumMap;
////	}
//	
//	public  Map<String, String> explainNum(List<VmwayItemBean> itemInfoDtos,String params){
//		//factNum:*****;goods:1;1:-1,2:-2;goods:2;1:-1,2:-2; goods:3;1:-1,2:-2; goods:4;1:-1,2:-2;all_closed
//		//"nongfushanquan5000ml":"2","dayibao4500ml","3",...
//		//'nongfushanquan5000ml': '2','dayibao4500ml', '3',...
//		List<Integer> machineItemIds = Lists.newArrayList();
//		boolean match = false;
//		Map<String,String> goodsNumMap=new HashMap<String,String> ();
//		if(StringUtils.isBlank(params)) {
//			return goodsNumMap;
//		}
//		params=params.substring(1,params.length()-1);
//		String[] split = params.split(",");
//		for (VmwayItemBean itemInfoDto : itemInfoDtos) {					
//			machineItemIds.add(itemInfoDto.getBasicItemId().intValue());
//		}
//		// key 循环  机器商品    
//		for (String param : split) {
//			match = false;
//			String currParam = param.trim();
//			currParam=currParam.trim();
//			if(currParam.contains(":")){
//				String[] wayInfo = currParam.split(":");
//				String key=wayInfo[0].replace("'", "").trim();
//				String value=wayInfo[1].replace("'", "").trim();
//				log.info("key:value"+key+":"+value);
//				for (VmwayItemBean itemInfoDto : itemInfoDtos) {	
//					if(key.equals(itemInfoDto.getExtraName())) {
//						log.info(key+"+++"+itemInfoDto.getExtraName()+"----"+value);
//						match = true;
//						if(goodsNumMap.containsKey(key)) {
//							log.info(key+"叠加数量"+String.valueOf(Integer.parseInt(goodsNumMap.get(key))+Integer.parseInt(value)));
//							goodsNumMap.put(key, String.valueOf(Integer.parseInt(goodsNumMap.get(key))+Integer.parseInt(value)));
//						}else {
//							goodsNumMap.put(key,value);
//						}
//					}
//				}
//				if(!match) {
//					log.info("开始寻找相似商品"+key);
//					Map<String,ItemBasicBean> map=configFactory.getExtraNameMap();
//					ItemBasicBean ib=map.get(key);
//					if(ib==null) {
//						ib = itemBasicDao.getItemByExtraName(key);
//						log.info(ib+"调整ib");
//						map.put(key, ib);
//					}
//					List<Integer> itemIds=itemBasicDao.getItemConnect(ib.getId());
//					//机器商品ids 包含  此商品别名
//					for (Integer machineItemId:machineItemIds) {
//						for(Integer itemId:itemIds) {
//							if(machineItemId.equals(itemId)) {
//								log.info("机器商品id命中"+itemId);
//								//id 转换 视觉名称
//								ItemBasicBean iBasicBean=itemBasicDao.getItemBasic(itemId);
//								
//								if(goodsNumMap.containsKey(iBasicBean.getExtraName())) {
//									log.info(iBasicBean.getExtraName()+"修正后的数量"+String.valueOf(Integer.parseInt(goodsNumMap.get(iBasicBean.getExtraName()))+Integer.parseInt(value)));
//									goodsNumMap.put(iBasicBean.getExtraName(), String.valueOf(Integer.parseInt(goodsNumMap.get(iBasicBean.getExtraName()))+Integer.parseInt(value))    );
//								}else {
//									log.info(iBasicBean.getExtraName()+"修正后的数量"+value);
//									goodsNumMap.put(iBasicBean.getExtraName(),value);
//								}
//							}
//							break;
//						}
//					}
//				}
//			}else {
//				log.error(params+"-错误的命令："+currParam);
//			}
//		}
//        for(Map.Entry<String, String> g:goodsNumMap.entrySet()){
//        	log.info(g.getKey()+"-计算结果-"+g.getValue());
//        }
//		return goodsNumMap;
//	}
		
//	public static void main(String[] args) {
//		String jsonString = JSON.toJSONString(itemChangeParse("goods:4;1:-1,2:-5;end","1988000080"));
//		System.out.println(jsonString);
//	}
}
