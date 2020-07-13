package com.server.module.system.statisticsManage.chart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
@Data
public class MultipleResponseData {
    int type;
    String x[];
    //List<Integer> xlist;
    List<ResponseData> list;
    @JsonIgnore
    Grouping grouping;
    @JsonIgnore
    public void formalize(){
        //找出最小，最大时间
        int min=20201010,max=0;
        for (ResponseData responseData : list) {
            type=responseData.getType();
            int[] x=responseData.getX();
            for (int i : x) {
                if(i<min)
                    min=i;
                if(i>max)
                    max=i;
            }
        }
        System.out.println("min="+min+" max="+max);
        //生成调整后的x
        List<Integer> listx= Lists.newArrayList();
        int group=min;

        Grouping grouping=Grouping.getGroupingInstanceByType(type);

        while(group<=max){
            listx.add(group);
            group=grouping.nextGroup(group);
        }
        Integer[] x=new Integer[listx.size()];
        listx.toArray(x);
        System.out.println("调整后的x=");
       /* for (Integer integer : x) {
            System.out.println(integer);
        }*/

        for (ResponseData responseData : list) {
            int[] x1=responseData.getX();
            Double[] y1=responseData.getY();

            List<Double> list=Lists.newArrayList();

            for(int t:x){
                boolean notIn=true;
                for (int i = 0; i < x1.length; i++) {
                    if(t==x1[i]){
                        list.add(y1[i]);
                        notIn=false;
                        break;
                    }
                }
                if(notIn){
                    list.add(0d);
                }
            }
            Double[] temp=new Double[x.length];
            Double[] ys=list.toArray(temp);
            responseData.setY(ys);

        }
        String[] xs=new String[listx.size()];
        for (int i=0;i<listx.size();i++) {
            xs[i]=grouping.getXLabel(listx.get(i).intValue());
        }
        this.setX(xs);

    }


}
