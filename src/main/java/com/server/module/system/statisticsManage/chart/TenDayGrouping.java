package com.server.module.system.statisticsManage.chart;

public class TenDayGrouping extends Grouping{

    @Override
    public boolean isGrouping(int preKey, int day) {

        //20180815  20180811
        //20180805  20180801
        //20180825  20180821
        int t=genGroupKey(day);
        return preKey==t ? true : false;
    }

    @Override
    public int genGroupKey(int day) {//20180815
        //20180815  20180811
        //20180805  20180801
        //20180825  20180821
        String str=String.valueOf(day);
        String s=str.substring(6,8);
        int d=Integer.parseInt(s);
        String temp=str.substring(0,6);
        if(d>=21){
            temp=temp+"21";
        }else if(d>=11){
            temp=temp+"11";
        }else{
            temp=temp+"01";
        }


        return Integer.parseInt(temp);
    }



    @Override
    public int nextGroup(int group) {
        //20180815  20180811
        //20180805  20180801
        //20180825  20180821
        String temp=String.valueOf(group);
        String t=temp.substring(6,8);
        String y=temp.substring(0,6);
        int i=Integer.parseInt(t);
        int yi=Integer.parseInt(y);
        if(i<21){//
            i=i+10;
            int ttt= Integer.parseInt(yi+""+i);
            System.out.println("next ten day="+ttt);
            return ttt;
        }else{//=21 下一个月的上旬
            String m=temp.substring(4,6);
            int mi=Integer.parseInt(m);
            if(mi==12){
                int yy=Integer.parseInt(temp.substring(0,4));
                yy=yy+1;
                return Integer.parseInt(yy+"0101");
            }else{
                mi=mi+1;
                String mm="";
                if(mi>9)
                    mm=mi+"";
                else
                    mm="0"+mi;
                return Integer.parseInt(temp.substring(0,4)+mm+"01");
            }
        }
    }

    @Override
    public String getXLabel(int group) {
        //20180815  20180811
        //20180805  20180801
        //20180825  20180821
        String temp=String.valueOf(group);
        int t=Integer.parseInt(temp.substring(6,8));
        String y=temp.substring(0,6);
        String x="";
        if(t==1){
            x="上旬";
        }else if(t==11){
            x="中旬";
        }else{
            x="下旬";
        }
        return y+x;
    }
}
