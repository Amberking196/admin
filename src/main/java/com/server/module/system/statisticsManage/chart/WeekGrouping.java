package com.server.module.system.statisticsManage.chart;

import org.joda.time.DateTime;

public class WeekGrouping extends Grouping{

	@Override
	public boolean isGrouping(int preKey, int day) {
		
		if(preKey==genGroupKey(day))
			return true;
		return false;
	}

	@Override
	public int genGroupKey(int day) {
		String str=String.valueOf(day);
		int y=Integer.parseInt(str.substring(0, 4));
		int m=Integer.parseInt(str.substring(4, 6));
		int d=Integer.parseInt(str.substring(6, 8));
		DateTime now=new DateTime(y,m,d,0,0,1);
		int w=now.getWeekOfWeekyear();
		int wy=now.getWeekyear();
		// preKey  =201842   // 2018年 42 周
		String temp="";
		if(w<10){
			temp=wy+"0"+w;
		}else{
			temp=wy+""+w;
		}
		return Integer.parseInt(temp);
	}

	@Override
	public int nextGroup(int group) {
		String str=String.valueOf(group);
		int wy=Integer.parseInt(str.substring(0, 4));
		int w=Integer.parseInt(str.substring(4, 6));
		if(w!=52)
			w=w+1;
		else{
			wy=wy+1;
			w=1;
		}
		
		String temp="";
		if(w<10){
			temp=wy+"0"+w;
		}else{
			temp=wy+""+w;
		}
		return Integer.parseInt(temp);
	}

	@Override
	public String getXLabel(int group) {
		String str=String.valueOf(group);
		int wy=Integer.parseInt(str.substring(0, 4));
		int w=Integer.parseInt(str.substring(4, 6));
		return wy+"年第"+w+"周";
	}
	
	public static void main(String[] args) {
		int day=20181017;
		String str=String.valueOf(day);
		int y=Integer.parseInt(str.substring(0, 4));
		int m=Integer.parseInt(str.substring(4, 6));
		int d=Integer.parseInt(str.substring(6, 8));
		DateTime now=new DateTime(y,m,d,0,0,1);
		int wy=now.getWeekOfWeekyear();
		int w=now.getWeekyear();
		System.out.println("wy="+wy+"  w="+w);
	}

}
