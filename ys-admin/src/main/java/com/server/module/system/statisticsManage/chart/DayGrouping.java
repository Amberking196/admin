package com.server.module.system.statisticsManage.chart;

import org.joda.time.DateTime;

public class DayGrouping extends Grouping{
    @Override
    public boolean isGrouping(int preKey, int day) {
        return preKey==day ? true : false;
    }

    @Override
    public int genGroupKey(int day) {
         return day;
    }

    @Override
    public int nextGroup(int group) {
        //20180815  20180811
        //20180805  20180801
        //20180825  20180821
        String temp=String.valueOf(group);
        if(temp.length()<8){
            System.out.println("the error data:"+temp);
            temp=temp+"01";
        }
        String d=temp.substring(6,8);
        String y=temp.substring(0,4);
        String m=temp.substring(4,6);


        DateTime dateTime = new DateTime(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d), 0, 0, 0, 0);
        dateTime=dateTime.plusDays(1);
        int yi=dateTime.getYear();
        int mi=dateTime.getMonthOfYear();
        int di=dateTime.getDayOfMonth();
        String ms="";
        if(mi<10)
            ms="0"+mi;
        else
            ms=mi+"";
        String ds="";
        if(di<10)
            ds="0"+di;
        else
            ds=di+"";
        return Integer.parseInt(yi+""+ms+ds);

    }

    @Override
    public String getXLabel(int group) {
        return group+"";
    }

}
