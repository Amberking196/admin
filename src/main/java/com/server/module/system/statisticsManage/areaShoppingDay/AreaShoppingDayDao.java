package com.server.module.system.statisticsManage.areaShoppingDay;
import com.google.common.collect.Lists;
import com.server.module.system.baseManager.china.ChinaBean;
import com.server.module.system.baseManager.china.ChinaDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class AreaShoppingDayDao extends MySqlFuns {
    public static Logger log = LogManager.getLogger(AreaShoppingDayDao.class);
   @Autowired
   private CompanyDao companyDao;
   public List<AreaShoppingDayDTO> list(AreaShoppingDayForm form){
       StringBuilder sb = new StringBuilder(" select DATE_FORMAT(p.payTime,'%Y%m%d') as day,count(distinct p.vendingMachinesCode) as machinesNum ,count(DISTINCT p.id) as orderNum,sum(p.num) as itemNum,sum(p.price) as totalMoney,max(p.price) as maxPrice,min(p.price) as minPrice ");
       sb.append(" from vending_machines_info i inner join pay_record p on i.code=p.vendingMachinesCode  ");
       sb.append("where 1=1  and p.state!=10002 ");
       if(StringUtil.isNotBlank(form.getStart())){
           sb.append(" and p.payTime>='"+form.getStart()+" 00:00:00'");
       }
       if(StringUtil.isNotBlank(form.getEnd())){
           sb.append(" and p.payTime<='"+form.getEnd()+" 23:59:59'");
       }
       if(StringUtil.isNotBlank(form.getCompanyId())){
           String sqlin=companyDao.findAllSonCompanyIdForInSql(Integer.parseInt(form.getCompanyId()));
           sb.append(" and i.companyId in "+sqlin);
       }
       if(StringUtil.isNotBlank(form.getCity()) && StringUtil.isBlank(form.getDistrict())){

           int city=Integer.parseInt(form.getCity());
           int city1=city+99;
           sb.append(" and i.district>="+city+" and  i.district<"+city1+" ");

       }
       if(StringUtil.isNotBlank(form.getDistrict())){
           sb.append(" and i.district="+form.getDistrict());
       }
       sb.append("  group by DATE_FORMAT(payTime,'%Y%m%d')");

       List<AreaShoppingDayDTO> list = Lists.newArrayList();
       System.out.println(sb.toString());
       Connection conn = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           conn = openConnection();
           ps = conn.prepareStatement(sb.toString());
           rs = ps.executeQuery();
           while (rs.next()) {
               AreaShoppingDayDTO dto=new AreaShoppingDayDTO();
               dto.setMinPrice(rs.getDouble("minPrice"));
               dto.setMaxPrice(rs.getDouble("maxPrice"));
               dto.setTotalMoney(rs.getDouble("totalMoney"));
               dto.setOrderNum(rs.getInt("orderNum"));
               dto.setItemNum(rs.getInt("itemNum"));
               dto.setMachinesNum(rs.getInt("machinesNum"));
               dto.setDay(rs.getString("day"));
               list.add(dto);
           }
       } catch (SQLException e) {
           e.printStackTrace();
           return list;
       } finally {
           closeConnection(rs, ps, conn);
       }
       return list;

   }


}
