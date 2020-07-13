package com.server.module.system.statisticsManage.chart;

public class MonthGrouping extends Grouping{
    @Override
    public boolean isGrouping(int preKey, int day) {
        int t=genGroupKey(day);
        return preKey==t ? true : false;
    }

    @Override
    public int genGroupKey(int day) {
        //20180815  201808

        return day/100;
    }

    @Override
    public int nextGroup(int group) {

        String temp=String.valueOf(group);
        String t=temp.substring(4,6);
        String y=temp.substring(0,4);
        int i=Integer.parseInt(t);
        int yi=Integer.parseInt(y);
        if(i==12){//第二年
            yi=yi+1;
            return Integer.parseInt(yi+"01");
        }else{
            i=i+1;
            if(i<10){
                return Integer.parseInt(yi+"0"+i);
            }else{
                return Integer.parseInt(yi+""+i);
            }
        }

    }

    @Override
    public String getXLabel(int group) {
        return group+"";
    }
}
