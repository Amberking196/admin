package com.server.module.system.statisticsManage.chart;

public class QuarterGrouping extends Grouping{
    @Override
    public boolean isGrouping(int preKey, int day) {
        int key=genGroupKey(day);

        return preKey==key ? true : false;
    }

    @Override
    public int genGroupKey(int day) {
        // 20180815  201808
        // 201801  201804  201807 201810

        //提取月份
        int t=day;
        String str= new Integer(t).toString();
        String yy=str.substring(0,4);
        String tt=str.substring(4,6);
        int m=Integer.valueOf(tt);
        int q=0;
        if(m>=10)
            q=10;
        else if(m>=7)
            q=7;
        else if(m>=4)
            q=4;
        else
            q=1;
        String temp="";
        if(q>=10){
            temp=yy+String.valueOf(q);
        }else{
            temp=yy+"0"+q;
        }
        return Integer.valueOf(temp);
    }

    @Override
    public int nextGroup(int group) {
        // 201701  201704 201707 201710 201801
        String temp=String.valueOf(group);
        String t=temp.substring(4,6);
        int m=Integer.parseInt(t);
        String next="";
        if(m<10){
            m=m+3;
            if(m==10)
              next=temp.substring(0,4)+"10";
            else
                next=temp.substring(0,4)+"0"+m;
        }else{//第二年的一季度
            String year=temp.substring(0,4);
            int y=Integer.parseInt(year)+1;
            next=y+"01";
        }


        return Integer.parseInt(next);
    }

    @Override
    public String getXLabel(int group) {
        String temp=String.valueOf(group);
        String t=temp.substring(4,6);
        String y=temp.substring(0,4);
        int i=Integer.parseInt(t);
        int q=1;
        if(i==1){
            q=1;
        }else if(i==4){
            q=2;
        }else if(i==7){
            q=3;
        }else if(i==10){
            q=4;
        }

        return y+"年"+q+"季度";
    }

    public static void main(String[] args) {

    }
}
