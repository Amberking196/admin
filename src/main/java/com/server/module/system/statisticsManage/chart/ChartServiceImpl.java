package com.server.module.system.statisticsManage.chart;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChartServiceImpl implements ChartService {
	@Autowired
	private ChartDao chartDao;

	@Override
	public MultipleResponseData baseStatistics(ChartForm form) {

		MultipleResponseData data = new MultipleResponseData();
		List<ResponseData> listResponse = Lists.newArrayList();
		// TODO Auto-generated method stub
		// type = 0 日 1 旬 2 月 3 季度
		List<DateCountVo> list = null;
		ResponseData temp = null;
		if (need(form.getFunctions(), "machine")) {
			list = chartDao.listMachinesInfo(form);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName(ChartUtil.getBaseFunName("machine"));
			temp.setUnit("台");//设置单位
			listResponse.add(temp);
		}

		if (need(form.getFunctions(), "user")) {
			list = chartDao.listUserInfo(form);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName(ChartUtil.getBaseFunName("user"));
			temp.setUnit("个");//设置单位
			listResponse.add(temp);
		}
		if (need(form.getFunctions(), "orderCount")) {
			list = chartDao.listOrderInfo(form);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName(ChartUtil.getBaseFunName("orderCount"));
			temp.setUnit("个");//设置单位
			listResponse.add(temp);
		}
		if (need(form.getFunctions(), "goodsCount")) {
			list = chartDao.listGoodsSaleCount(form);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setUnit("个");//设置单位
			temp.setName(ChartUtil.getBaseFunName("goodsCount"));
			listResponse.add(temp);
		}
		if (need(form.getFunctions(), "costPrice")) {// 销售额,数量*100
			list = chartDao.listCostSalePrice(form);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName(ChartUtil.getBaseFunName("costPrice"));
			temp.setUnit("元");//设置单位
			listResponse.add(temp);
		}
		if (need(form.getFunctions(), "noSaleMachineRate")) {// 无销售的机器的占比
			// 查询有销量的机器总数
			list = chartDao.listMachineCount(form);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName("saleMachie");
			temp.setUnit("%");//设置单位
			listResponse.add(temp);
			// 总的上线个数
			list = chartDao.listMachinesInfoByForm(form);
			form.setSuperposition(true);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName("MachieCount");
			listResponse.add(temp);
		}
		if (need(form.getFunctions(), "customerprice")) {// 客价单
			//销售额
			list = chartDao.listCostSalePrice(form);
			form.setSuperposition(false);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName("costPriceByCustomer");
			temp.setUnit("元");//设置单位
			listResponse.add(temp);
			//订单个数
			list = chartDao.listOrderInfo(form);
			form.setSuperposition(false);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName("orderCountByCustomer");
			listResponse.add(temp);
		}

		if (need(form.getFunctions(), "onlyOneNumber")) {// 平均每台销量
			// 机器销量
			list = chartDao.listGoodsSaleCount(form);// 销售数量
			form.setType(0);//这个只能按日算
			form.setSuperposition(false);
			temp = Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()), form, list);
			temp.setName("台平均销量");
			temp.setUnit("桶");//设置单位
			List<DateCountVo> listMachine = chartDao.listMachinesStartUsingByForm(form);
			int[] xs = temp.getX();
			Double[] ys = temp.getY();
			Double[] newYs = new Double[ys.length];
			for(int i=0;i<xs.length;i++){
				int t = xs[i];//天
				boolean flag=true;
				for(int j=0;j<listMachine.size();j++){
					DateCountVo vo=listMachine.get(j);
					if(vo.getDay()>=t){
						double d=0d;
						if(vo.getDay()==t){
							 d=divide(ys[i],vo.getCount());
						}else{
							 d=divide(ys[i],listMachine.get(j-1).getCount());
						}
						newYs[i]=new Double(d);
						flag=false;
						break;
					}
				}
				if(flag){
					newYs[i]=divide(ys[i],listMachine.get(listMachine.size()-1).getCount());
				}
			}
			temp.setY(newYs);
			listResponse.add(temp);

		}
		data.setList(listResponse);
		data.formalize();
		/*if (need(form.getFunctions(), "noSaleMachineRate")) {// 无销售的机器的占比
			getData(data, "saleMachie", "MachieCount", "noSaleMachineRate");
		}*/
		/*if (need(form.getFunctions(), "onlyOneNumber")) {// 平均每台销量
			getData(data, "saleCount", "MachieCountBySale", "onlyOneNumber");
		}*/
		/*if (need(form.getFunctions(), "customerprice")) {// 客价单
			getData(data, "costPriceByCustomer", "orderCountByCustomer", "customerprice");
		}*/
		return data;
	}

	public void getData(MultipleResponseData data, String removeStr, String removeStr2, String returnName) {
		List<ResponseData> listData = data.getList();
		Double y[] = null;
		Double y2[] = null;
		String unit="";
		// 循环遍历，取出指定的集合
		for (ResponseData responseData : listData) {
			if (removeStr.equals(responseData.getName())) {// 取出有销售的机器
				y = responseData.getY();
				unit=responseData.getUnit();
			}
			if (removeStr2.equals(responseData.getName())) {// 取出上线设备总数
				y2 = responseData.getY();
			}
		}
		for (int i = 0; i < listData.size(); i++) {
			if (removeStr.equals(listData.get(i).getName())) {// 移除有销售的机器
				listData.remove(i);
			}
			if (removeStr2.equals(listData.get(i).getName())) {// 移除上线设备总数
				listData.remove(i);
			}
		}
		for (int i = 0; i < listData.size(); i++) {
			if (removeStr.equals(listData.get(i).getName())) {// 移除有销售的机器
				listData.remove(i);
			}
			if (removeStr2.equals(listData.get(i).getName())) {// 移除上线设备总数
				listData.remove(i);
			}
		}
		// 处理无售货机机器的占比
		if("noSaleMachineRate".equals(returnName)){
			for (int i = 0; i < y.length; i++) {
				y[i] = (y2[i] - y[i]) * 10000 / y2[i];
			}
		}
		// 处理平均每台销量
		if("onlyOneNumber".equals(returnName)){
			for (int i = 0; i < y.length; i++) {
				y[i] = y[i] * 100 / y2[i];
			}
		}
		//处理客价单
		if("customerprice".equals(returnName)){
			for (int i = 0; i < y.length; i++) {
				if(y2[i]==0&&y[i]==0) {
					y[i]=0d;
				}else {
					y[i] = y[i] / y2[i];
				}
			}
		}
		ResponseData temp = new ResponseData();
		temp.setY(y);
		temp.setName(ChartUtil.getBaseFunName(returnName));
		temp.setUnit(unit);
		// 添加到容器中
		listData.add(temp);
	}

	@Override
	public MultipleResponseData goodsStatistics(ChartForm form) {
		MultipleResponseData data = new MultipleResponseData();
		List<ResponseData> listResponse = Lists.newArrayList();
		List<SaleGoodsDateCountVo> list1 = null;
		ResponseData temp = null;
		list1 = chartDao.listGoodsSaleInfo(form);
		Map<Integer, String> goodsMap = chartDao.listGoods();
		String[] functions = form.getFunctions();
		for (String function : functions) {
			int basicItemId = Integer.parseInt(function);
			List<DateCountVo> list = Lists.newArrayList();
			for (SaleGoodsDateCountVo vo : list1) {
				if (basicItemId == 0 || basicItemId == vo.getItemId()) {
					DateCountVo dateCountVo = new DateCountVo();
					dateCountVo.setCount(vo.getCount());
					dateCountVo.setDay(vo.getDay());
					list.add(dateCountVo);
				}
			}
			list = chartDao.listOrderInfo(form);// 待实现
			// temp=Grouping.getResponseData(form, list);
			// temp.setName(goodsMap.get(basicItemId));
			listResponse.add(temp);

		}

		data.setList(listResponse);
		data.formalize();
		return data;
	}

	private boolean need(String[] functions, String fun) {
		if (functions != null && functions.length > 0) {
			for (String function : functions) {
				if (function.equals(fun)) {
					return true;
				}
			}
		}
		return false;
	}

	public Map<Integer, String> listGoods() {
		return chartDao.listGoods();
	}

	/*public static void main(String[] args) {
		List<DateCountVo> list = Lists.newArrayList();

		for (int i = 1; i < 10; i++) {
			if (i == 5) {
				for (int j = 0; j < 3; j++) {
					DateCountVo vo = new DateCountVo();
					vo.setCompanyId(76);
					vo.setDay(5);
					vo.setCount(1);
					list.add(vo);
				}

			} else {
				DateCountVo vo = new DateCountVo();
				vo.setCompanyId(76);
				vo.setDay(i);
				vo.setCount(1);
				list.add(vo);
			}

		}

		*//*
		 * List<DateCountVo> list1=dayStatistics(list,false); for (int i = 0; i <
		 * list1.size(); i++) { DateCountVo vo=list1.get(i);
		 * System.out.println(vo.getDay()+"   "+vo.getCount()); }
		 *//*
	}*/

	@Override
	public MultipleResponseData orderByHour(ChartForm form) {
		MultipleResponseData data = new MultipleResponseData();
		List<ResponseData> listResponse = Lists.newArrayList();
		// TODO Auto-generated method stub
		// type = 0 日 1 旬 2 月 3 季度

		List<DateCountVo> list = chartDao.listOrderByHourInfo(form);
		Grouping grouping = new HourGrouping();
		ResponseData temp = Grouping.getResponseData(grouping, form, list);
		temp.setName("购买时段分布");
		listResponse.add(temp);
		String[] str = new String[temp.getX().length];
		for (int i = 0; i < str.length; i++) {
			str[i] = new String(Integer.toString(temp.getX()[i]));
		}
		data.setX(str);
		data.setList(listResponse);
		return data;
	}
	public static double divide(double x,double y){
		double z = x/y;
		double x1 = x%y;
		System.out.println(z+"  "+x1);
		BigDecimal b1=new BigDecimal(x);
		BigDecimal b2=new BigDecimal(y);
		return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {
		int i=2251;
		int h=1282;


		System.out.println(divide(i,h));
	}


}
