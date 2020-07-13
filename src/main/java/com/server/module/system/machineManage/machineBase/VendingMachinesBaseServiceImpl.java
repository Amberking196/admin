package com.server.module.system.machineManage.machineBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;

import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemBean;
import com.server.util.ReturnDataUtil;
import com.server.util.ReviseUtil;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author name: yjr create time: 2018-03-29 17:44:47
 */
@Service
public class VendingMachinesBaseServiceImpl implements VendingMachinesBaseService {

	public static Logger log = LogManager.getLogger(VendingMachinesBaseDaoImpl.class);
	@Autowired
	private VendingMachinesBaseDao vendingMachinesBaseDaoImpl;

	public ReturnDataUtil listPage(VendingMachinesBaseCondition condition) {
		return vendingMachinesBaseDaoImpl.listPage(condition);
	}

	public VendingMachinesBaseBean add(VendingMachinesBaseBean entity) {
		return vendingMachinesBaseDaoImpl.insert(entity);
	}

	public boolean update(VendingMachinesBaseBean entity) {
		return vendingMachinesBaseDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return vendingMachinesBaseDaoImpl.delete(id);
	}

	public List<VendingMachinesBaseBean> list(VendingMachinesBaseCondition condition) {
		return null;
	}

	public VendingMachinesBaseBean get(Object id) {
		return vendingMachinesBaseDaoImpl.get(id);
	}

	@Override
	public VendingMachinesBaseBean getBase(Object factoryNumber, Integer companyId) {
		// TODO Auto-generated method stub
		return vendingMachinesBaseDaoImpl.getBase(factoryNumber, companyId);
	}

	/**
	 * 校验出厂编号 是否存在
	 */
	public boolean checkFactoryNumber(String factoryNumber) {
		return vendingMachinesBaseDaoImpl.checkFactoryNumber(factoryNumber);
	}

	/**
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 */
	public String findItemStandard(String factnum) {
		String result = "g:";
		List<String> findItemStandard = vendingMachinesBaseDaoImpl.findItemStandard(factnum);
		if (findItemStandard != null) {
			List<Integer> numList = new ArrayList<Integer>();
			for (int i = 0; i < findItemStandard.size(); i++) {
				int num = Integer.parseInt(sumString(findItemStandard.get(i)));
				numList.add(num);
				result = result.concat(findItemStandard.get(i));
				if (i!=findItemStandard.size()-1) {
					System.out.println("i==="+i);
					result = result.concat(",");
				}
			}
			int sum = 0;
			for (int i = 0; i < numList.size(); i++) {
				sum += numList.get(i);
			}
			result = result.concat("&" + sum + "$");
			return result;
		} else {
			return null;
		}
	}

	/**version 2 
	 * 根据售货机出厂编号 得到对应机器货道的商品规格 组成一个字符串 返回
	 */
	public String findItemWeight(String factnum) {
		//g:1-300,400,500;2-300;3-380,600;4-300,400&55$
		String result = "g:";
		int j = 1;
		List<VendingMachinesWayItemBean> vendingMachinesWayItemBeanList = vendingMachinesBaseDaoImpl.findItemWeight(factnum);
		//g:1-3200,200;3-230,399;4-230,450
		String sumWay=vendingMachinesWayItemBeanList.get(0).getAisleConfiguration();//货道数
		if (vendingMachinesWayItemBeanList != null && vendingMachinesWayItemBeanList.size()>0) {
			for (VendingMachinesWayItemBean vmwib:vendingMachinesWayItemBeanList) {
				//log.info("-------"+vmwib.getWayNumber()+"------------"+vmwib.getWeight());	
				//从货道j检索  不匹配时货道从j+1开始检索匹配    30为预设的货道数量	
				for(int i = j;i<=30;i++) {
					if(vmwib.getWayNumber().intValue()==i) {
						if(result.substring(result.length() - 1,result.length()).equals(":")){
							result=result.concat(i+"-"+vmwib.getWeight().toString());
						}
						else if(!result.contains(i+"-")) {
							result=result.concat(";"+i+"-"+vmwib.getWeight().toString());
						}else {
							result=result.concat(","+vmwib.getWeight().toString());
						}
						break;
					}else {
//						//2 1
//						//g:2-3200,200;3-230;4-230,450
//						int k=vmwib.getWayNumber().intValue();
//						while(k>i-1) {
//							if(result.substring(result.length() - 1,result.length()).equals(":")){
//								result=result.concat(k+"-0");
//							}
//							else if(result.contains(k+"-")) {
//								result=result.concat(",0");
//							}else if(!result.contains(k+"-")){
//								result=result.concat(";"+k+"-"+"0");
//							}
//							k--;
//						}
						j=j+1;
					}
				}
			}
			//g:1-300,400,500;2-300;4-300,400&55$
			//g:2-300  3-380,600  4-300,400 &55$
			int intWay=Integer.parseInt(sumWay);	
			String[] result1=result.split(";");
//			String[] result2=result.split(";");
//			for(int i=0;i<result2.length;i++) {
//				if(!result2[i].contains(",")) {
//					result2[i]=result2[i]+",0";
//					result.replace(result1[i],result2[i]);
//				}
//			}
			String finalResult="";
			boolean flag=false;
			for(int i=1;i<=intWay;i++) {
				if(!result.contains(i+"-")) {
					for(int m=0;m<result1.length;m++) {
						log.info(finalResult+"+m:"+m);
						if(i==1 && !flag) {
							finalResult="g:"+i+"-0;";
							String[] gresult=result1[0].split(":");
							finalResult=finalResult+gresult[1]+";";
							flag = true;
							continue;
						}
						if(m!=result.length()-1) {
							//g:1-300  3-380,600  4-300,400&55$
							if(!flag && !result1[m].substring(0, 1).equals("g") && Integer.parseInt(result1[m].substring(0, 1))>i ) {
								//log.info("原因");
								finalResult=finalResult+i+"-0;"+result1[m]+";";
								flag = true;
							}else {
								finalResult=finalResult+result1[m]+";";
							}
						}else {
							if(!flag && !result1[m].substring(0, 1).equals("g") && Integer.parseInt(result1[m].substring(0, 1))>i ) {
								finalResult=finalResult+i+"-0;"+result1[m];
								flag = true;
							}else {
								finalResult=finalResult+result1[m];
							}
						}
					}
					if(i==intWay) {
						finalResult=finalResult+i+"-0";
					}
				}
			}
			
			if(finalResult.equals("")) {
				finalResult=result;
			}
			
			int sum = 0;
			char[] ss = finalResult.toCharArray();
			for (char c : ss) {
				if(Character.isDigit(c)){
					Integer unitChar = Integer.valueOf(String.valueOf(c));
					sum+=unitChar;
				}
			}
			finalResult=finalResult.concat("&"+sum+"$");
			return finalResult;
		} else {
			return null;
		}
	}
	// 将传入的数据进行计算得到总和返回
	public static String sumString(String s) {
		int len = s.length();
		int sum = 0;
		for (int i = 0; i < len; i++) {
			StringBuffer sb = new StringBuffer();
			char c = s.charAt(i);
			sb.append(c);
			sum += Integer.parseInt(sb.toString());
		}
		return Integer.toString(sum);
	}

	@Override
	public String getMultilayerCommand(String factnum) {
		StringBuffer command = new StringBuffer("g:");
		List<WayStandardDto> wayStandardList = vendingMachinesBaseDaoImpl.getStandardInfo(factnum);
		if(wayStandardList == null || wayStandardList.size() == 0){
			return null;
		}
		Map<Integer, List<WayStandardDto>> wayMap = wayStandardList.stream().collect(Collectors.groupingBy(WayStandardDto::getWayNum));
		List<Integer> wayList = new ArrayList<Integer>(wayMap.keySet());
		Collections.sort(wayList,Integer::compareTo);
		for (int j=0;j<wayList.size();j++) {
			List<WayStandardDto> list = wayMap.get(wayList.get(j));
			Collections.sort(list,new Comparator<WayStandardDto>(){
				@Override
				public int compare(WayStandardDto o1, WayStandardDto o2) {
					
					return Integer.valueOf(o1.getStandard()).compareTo(Integer.valueOf(o2.getStandard()));
				}
			});
			command.append(wayList.get(j)+"-");
			for (int i = 0;i<list.size();i++) {
				if(i<list.size()-1){
					command.append(list.get(i).getStandard()+",");
				}else{
					command.append(list.get(i).getStandard());
				}
			}
			if(j<wayList.size()-1){
				command.append(";");
			}else{
				command.append("&");
			}
		}
		Integer sumString = ReviseUtil.checkCodeSum(command.toString());
		command.append(sumString);
		command.append("$");
		return command.toString();
	}


	@Override
	public Integer getVersionByFactoryNumber(String factnum) {
		return vendingMachinesBaseDaoImpl.getVersionByFactoryNumber(factnum);
	}

	@Override
	public boolean updateProgramVersion(String programVersion, String factoryNum) {
		return vendingMachinesBaseDaoImpl.updateProgramVersion(programVersion, factoryNum);
	}

	@Override
	public boolean updateCanOnlineUpdate(String factoryNum) {
		return vendingMachinesBaseDaoImpl.updateCanOnlineUpdate(factoryNum);
	}

}
