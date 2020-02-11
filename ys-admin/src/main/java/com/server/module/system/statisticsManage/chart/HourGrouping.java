package com.server.module.system.statisticsManage.chart;

public class HourGrouping extends Grouping{

	@Override
	public boolean isGrouping(int preKey, int day) {
		// TODO Auto-generated method stub
		int temp=genGroupKey(day);
		return preKey==temp?true:false;
	}

	@Override
	public int genGroupKey(int day) {
		return day;
	}

	@Override
	public int nextGroup(int group) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getXLabel(int group) {
		// TODO Auto-generated method stub
		return null;
	}

}
