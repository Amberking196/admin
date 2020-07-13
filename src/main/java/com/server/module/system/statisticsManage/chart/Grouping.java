package com.server.module.system.statisticsManage.chart;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

/**
 * 此类主要用来做分组，及相应的模板，只能加方法，不能修改
 */

public abstract class Grouping {

	public abstract boolean isGrouping(int preKey, int day);

	public abstract int genGroupKey(int day);

	public abstract int nextGroup(int group);

	public abstract String getXLabel(int group);

	public static Grouping getGroupingInstanceByType(int type) {
		Grouping grouping = new DayGrouping();
		if (type == 0)
			grouping = new DayGrouping();
		if (type == 1)
			grouping = new WeekGrouping();
		if (type == 2)
			grouping = new MonthGrouping();
		if (type == 3)
			grouping = new QuarterGrouping();

		return grouping;
	}

	/**
	 * 获取报表数据接口，供第三方类调用
	 * 
	 * @param grouping
	 * @param form     条件
	 * @param list     数据源
	 * @return
	 */
	public static ResponseData getResponseData(Grouping grouping, ChartForm form, List<DateCountVo> list) {
		List<DateCountVo> list1 = statistics(grouping, list, form.isSuperposition());
		return genResponseData(list1, form.getType());
	}

	/**
	 * 数据分组计算
	 * 
	 * @param grouping
	 * @param list
	 * @param superposition
	 * @return
	 */

	private static List<DateCountVo> statistics(Grouping grouping, List<DateCountVo> list, boolean superposition) {
		List<DateCountVo> list1 = Lists.newArrayList();
		int preKey = -1;
		for (int i = 0; i < list.size(); i++) {
			DateCountVo vo = list.get(i);
			if (preKey == -1) {
				preKey = grouping.genGroupKey(vo.getDay());
			}
			boolean isGroup = false;
			isGroup = grouping.isGrouping(preKey, vo.getDay());
			if (isGroup) {//
				DateCountVo vo1 = new DateCountVo();
				if (list1.size() == 0) {
					vo1.setCompanyId(vo.getCompanyId());
					vo1.setDay(grouping.genGroupKey(vo.getDay()));
					vo1.setCount(vo.getCount());
					list1.add(vo1);
				} else {
					vo1 = list1.get(list1.size() - 1);
					vo1.setCount(vo1.getCount() + vo.getCount());
				}
			} else {// 生成新记录，并做叠加
				DateCountVo vo1 = new DateCountVo();
				vo1.setDay(grouping.genGroupKey(vo.getDay()));
				vo1.setCompanyId(vo.getCompanyId());
				DateCountVo temp = list1.get(list1.size() - 1);
				if (superposition)
					vo1.setCount(temp.getCount() + vo.getCount());
				else
					vo1.setCount(vo.getCount());
				list1.add(vo1);
			}
			preKey = grouping.genGroupKey(vo.getDay());

		}
		return list1;
	}

	/**
	 * 生成x y 族 数据
	 * 
	 * @param type  数据类型 可忽略
	 * @param list1 源数据
	 * @return
	 */
	private static ResponseData genResponseData(List<DateCountVo> list1, int type) {
		ResponseData data = new ResponseData();
		int[] x = new int[list1.size()];
		Double[] y = new Double[list1.size()];
		for (int i = 0; i < list1.size(); i++) {
			DateCountVo vo = list1.get(i);
			x[i] = vo.getDay();
			y[i] = vo.getCount()*1d;
		}
		data.setX(x);
		data.setY(y);
		data.setType(type);
		return data;

	}
	/**
	 * 把查询的结果按type的类型添加到list集合，去除重复的记录
	 * @param vo
	 * @param list
	 * @param set
	 * @param form
	 * @return
	 */
	public static List<DateCountVo> ResultData(DateCountVo vo,List<DateCountVo> list,Set<String>[]set,ChartForm form) {
		if (form.getType() == 0||form.getType()==2) {// 0 日 1 旬 2 月 3 季度
			if (list.size() > 0) {
				DateCountVo temp = list.get(list.size() - 1);
				if (temp.getTime() == vo.getTime()&& vo.getCode().equals(temp.getCode())) {// 去除同一天同一个用户的多个记录，只留一条记录
					// 不添加
				} else {
					// 添加
					list.add(vo);
				}
			} else {
				list.add(vo);
			}
		}
		//按季节查询
		if (form.getType() ==3) {// 0 日 1 旬 2 月 3 季度
			if (list.size() > 0) {
				DateCountVo temp = list.get(list.size() - 1);
				//判断是否是同一年的
				if(vo.getTime()==temp.getTime()) {//同一年的
					String str=Integer.toString(vo.getDay());//20181011
					String newStr=str.substring(4,6);//取月份10
					if(Integer.parseInt(newStr)<=3) {//1--3
						if(set[0].add(vo.getCode())) {//该售货机没有重复，添加到list中
							list.add(vo);
						}
					}else if(Integer.parseInt(newStr)<=6) {
						if(set[1].add(vo.getCode())) {//该售货机没有重复，添加到list中
							list.add(vo);
						}
					}else if(Integer.parseInt(newStr)<=9) {
						if(set[2].add(vo.getCode())) {//该售货机没有重复，添加到list中
							list.add(vo);
						}
					}else {
						if(set[3].add(vo.getCode())) {//该售货机没有重复，添加到list中
							list.add(vo);
						}
					}
				}else {
					//另一年的第一个
					list.add(vo);
					//清空set
					set[0].clear();
					set[1].clear();
					set[2].clear();
					set[3].clear();
					String str=Integer.toString(vo.getDay());//20181011
					String newStr=str.substring(4,6);//取月份10
					if(Integer.parseInt(newStr)<=3) {//1--3
						set[0].add(vo.getCode());
					}else if(Integer.parseInt(newStr)<=6) {
						set[1].add(vo.getCode());
					}else if(Integer.parseInt(newStr)<=9) {
						set[2].add(vo.getCode());
					}else {
						set[3].add(vo.getCode());
					}
				}
			} else {
				list.add(vo);
				String str=Integer.toString(vo.getDay());//20181011
				String newStr=str.substring(4,6);//取月份10
				if(Integer.parseInt(newStr)<=3) {//1--3
					set[0].add(vo.getCode());
				}else if(Integer.parseInt(newStr)<=6) {//4--6
					set[1].add(vo.getCode());
				}else if(Integer.parseInt(newStr)<=9) {//6--8
					set[2].add(vo.getCode());
				}else {
					set[3].add(vo.getCode());
				}
			}
		}
		//按旬查询
		if (form.getType() == 1) {// 0 日 1 旬 2 月 3 季度
			if(list.size()>0) {
				DateCountVo temp = list.get(list.size() - 1);
				//判断是否是同一个月的
				if(vo.getTime()==temp.getTime()) {
					
					String str=Integer.toString(vo.getDay());//20181011
					String newStr=str.substring(6,8);//11
					//01--10
					if(Integer.parseInt(newStr)<=10) {
						
						if(set[0].add(vo.getCode())) {//该售货机没有重复，添加到list中
							list.add(vo);
						}
					}else if(Integer.parseInt(newStr)<21) {//11--20
						if(set[1].add(vo.getCode())) {
							list.add(vo);
						}
					}else {//21--其他
						if(set[2].add(vo.getCode())) {
							list.add(vo);
						}
					}
				}else {//不是同一个月的
					//清空set的记录
					set[0].clear();
					set[1].clear();
					set[2].clear();
					list.add(vo);
					//添加另一个月的第一个售货机
					String str=Integer.toString(vo.getDay());//20181011
					String newStr=str.substring(6,8);//11
					if(Integer.parseInt(newStr)<=10) {
						set[0].add(vo.getCode());
					}else if((Integer.parseInt(newStr)<21)){
						set[1].add(vo.getCode());
					}else {
						set[2].add(vo.getCode());
					}
				}
			}else {
				list.add(vo);
				//往set里添加记录
				String str=Integer.toString(vo.getDay());//20181011
				String newStr=str.substring(6,8);//11
				if(Integer.parseInt(newStr)<=10) {
					set[0].add(vo.getCode());
				}else if((Integer.parseInt(newStr)<21)){
					set[1].add(vo.getCode());
				}else {
					set[2].add(vo.getCode());
				}
			}
		}
		return list;
	}

}
