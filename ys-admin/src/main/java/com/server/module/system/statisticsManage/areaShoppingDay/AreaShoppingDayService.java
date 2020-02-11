package com.server.module.system.statisticsManage.areaShoppingDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AreaShoppingDayService {

    @Autowired
    private AreaShoppingDayDao dao;
    public List<AreaShoppingDayDTO> list(AreaShoppingDayForm form){
        List<AreaShoppingDayDTO> list = dao.list(form);
        if(list.size()==0){
            return list;
        }

        AreaShoppingDayDTO dto = new AreaShoppingDayDTO();
        dto.setDay("总计");
        double maxPrice=0d;
        double minPrice=100d;
        for (AreaShoppingDayDTO obj : list) {
            //obj.setDay(fomatDay(obj.getDay()));
            obj.setAverageItemNum(divide(obj.getItemNum()*1d,obj.getOrderNum()*1d));
            obj.setAverageOrderPrice(divide(obj.getTotalMoney(),obj.getOrderNum()*1d));
            dto.setTotalMoney(add(dto.getTotalMoney(),obj.getTotalMoney()));
            dto.setOrderNum(dto.getOrderNum()+obj.getOrderNum());
            dto.setItemNum(dto.getItemNum()+obj.getItemNum());
            if(obj.getMinPrice()<minPrice)
                minPrice=obj.getMinPrice();
            if(obj.getMaxPrice()>maxPrice)
                maxPrice=obj.getMaxPrice();
        }
        dto.setMinPrice(minPrice);
        dto.setMaxPrice(maxPrice);
        dto.setAverageItemNum(divide(dto.getItemNum()*1d,dto.getOrderNum()*1d));
        dto.setAverageOrderPrice(divide(dto.getTotalMoney(),dto.getOrderNum()*1d));
        list.add(dto);
        return list;
    }

    private String fomatDay(String day){
       return  day.substring(0,4)+"-"+day.substring(4,6)+"-"+day.substring(6,8);
    }

   public  double divide(double cs,double fcs){
       BigDecimal cs1=new BigDecimal(cs);
       BigDecimal bcs1=new BigDecimal(fcs);
       if(bcs1.doubleValue()==0) {
    	   return 0;
       }
       cs1=cs1.divide(bcs1,2, BigDecimal.ROUND_HALF_EVEN);
       double result=cs1.doubleValue();
       return result;
   }
   public double add(double cs,double fcs){
       BigDecimal cs1=new BigDecimal(cs);
       BigDecimal bcs1=new BigDecimal(fcs);
       MathContext mc=new MathContext(10, RoundingMode.HALF_EVEN);
       cs1=cs1.add(bcs1,mc);
       double result=cs1.doubleValue();
       return result;
   }
}
