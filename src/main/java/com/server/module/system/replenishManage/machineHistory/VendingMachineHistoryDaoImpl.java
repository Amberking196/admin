package com.server.module.system.replenishManage.machineHistory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.redis.RedisClient;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: yjr
 * create time: 2018-11-06 14:32:26
 */
@Repository
public class VendingMachineHistoryDaoImpl extends BaseDao<VendingMachineHistoryBean> implements VendingMachineHistoryDao {

    private static Log log = LogFactory.getLog(VendingMachineHistoryDaoImpl.class);

    @Autowired
    private CompanyDao companyDaoImpl;
    @Autowired
    private RedisClient redisClient;
    /**
     * 机器商品日志列表
     * @param condition
     * @return
     */
    public ReturnDataUtil listPage(VendingMachineHistoryCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
       // sql.append("select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ");

       sql.append(" SELECT h.id,h.vmCode,h.wayNumber,h.itemId,h.num,h.endNum,h.saleNum,h.replenishNum,h.balanceNum,h.recordTime,h.createTime,i.locatoinName AS address,ib.name AS itemName from vending_machines_info i INNER JOIN vending_machine_history h ON i.code=h.vmCode LEFT JOIN item_basic ib ON h.itemId=ib.id WHERE 1=1 ");
		if (condition.getAreaId() != null && condition.getAreaId()>0) {
			sql.append(" and i.areaId = '" + condition.getAreaId() + "' ");
		}
       if(StringUtil.isNotBlank(condition.getCompanyId())){
           sql.append(" and i.companyId="+condition.getCompanyId());
       }

        if(StringUtil.isNotBlank(condition.getVmCode())){
            sql.append(" and i.code = '"+condition.getVmCode()+"'");
        }
        if(condition.getBalance()!=0){
            sql.append(" and h.balanceNum!=0 ");
        }
        if(condition.isReplenish()){//有补货
           sql.append(" and h.replenishNum!=0 ");
        }
        if(condition.isSale()){
           sql.append(" and h.saleNum!=0 ");
        }
        if(StringUtil.isNotBlank(condition.getStartDay())){
            sql.append(" and h.recordTime >= '"+condition.getStartDay()+" 00:00:00'");
        }
        if(StringUtil.isNotBlank(condition.getEndDay())){
            sql.append(" and h.recordTime <= '"+condition.getEndDay()+" 23:59:59'");
        }
        sql.append(" and i.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" ");
        log.info("补货盘点SQL："+sql.toString());
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));

            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());


            rs = pst.executeQuery();
            List<VendingMachineHistoryBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachineHistoryBean bean = new VendingMachineHistoryBean();
                bean.setId(rs.getLong("id"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setWayNumber(rs.getLong("wayNumber"));
                bean.setItemId(rs.getLong("itemId"));
                bean.setNum(rs.getLong("num"));
                bean.setEndNum(rs.getLong("endNum"));
                bean.setSaleNum(rs.getLong("saleNum"));
                bean.setReplenishNum(rs.getLong("replenishNum"));
                bean.setBalanceNum(rs.getLong("balanceNum"));
                bean.setRecordTime(rs.getDate("recordTime"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setAddress(rs.getString("address"));
                bean.setItemName(rs.getString("itemName"));
                list.add(bean);
            }
            data.setCurrentPage(condition.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return data;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
    }

    
    public VendingMachineHistoryBean get(Object id) {
        return super.get(id);
    }
    /**
     * 机器商品日志删除
     * @param id
     * @return
     */
    public boolean delete(Object id) {
        VendingMachineHistoryBean entity = new VendingMachineHistoryBean();
        return super.del(entity);
    }
    /**
     * 机器商品日志修改
     * @param entity
     * @return
     */
    public boolean update(VendingMachineHistoryBean entity) {
        return super.update(entity);
    }
    /**
     * 机器商品日志添加
     * @param entity
     * @return
     */
    public VendingMachineHistoryBean insert(VendingMachineHistoryBean entity) {
        return super.insert(entity);
    }

    public List<VendingMachineHistoryBean> list(VendingMachineHistoryCondition condition) {
        return null;
    }

    public void  updateBean(String vmCode,Integer wayNumber){
        String sql="select w.num,i.basicItemId from vending_machines_way w inner join vending_machines_item i on w.itemId=i.id where w.vendingMachinesCode='"+vmCode+"' and w.wayNumber="+wayNumber;
        List<Map<String,Object>> list=this.list(sql,null);
        System.out.println(sql);
        sql=null;
        if(list.size()<=0){
            log.info("找不到货道绑定的商品，不用记录日志");
            return ;
        }
        Integer endNum=(Integer)((Map<String,Object>)list.get(0)).get("num");
        Long basicItemId=(Long)((Map<String,Object>)list.get(0)).get("basicItemId");
        list=null;
        System.out.println("endNum="+endNum+"  basicItemId="+basicItemId);

        String sql1="select id from vending_machine_history where vmCode='"+vmCode+"' and wayNumber="+wayNumber+" order by createTime desc limit 1";
        System.out.println(sql1);
        List<Map<String,Object>> list1=this.list(sql1,null);
        sql1=null;
        if(list1.size()<=0){
            log.info("找不到历史补货销售日志，不用修改");
            return ;
        }
        Long id=(Long)((Map<String,Object>)list1.get(0)).get("id");
        list1=null;
        DateTime exeTime=new DateTime();
        exeTime=exeTime.withHourOfDay(0);
        exeTime=exeTime.withMinuteOfHour(0);
        exeTime=exeTime.withSecondOfMinute(0);
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sf.format(exeTime.toDate());
        String sql2="select sum(num-preNum) as num from replenish_record where opType=2 and vmCode='"+vmCode+"' and basicItemId="+basicItemId+" and createTime>='"+start+"'";
        System.out.println(sql2);

        List<Map<String,Object>> list2=this.list(sql2,null);
        sql2=null;
        BigDecimal replenishNumBig =(BigDecimal)((Map<String,Object>)list2.get(0)).get("num");
        Long replenishNum=0l;
        if(replenishNumBig!=null)
        	replenishNum=replenishNumBig.longValue();
        list2=null;
        String sql3="SELECT sum(preNum-num) as num FROM vending_waynum_log WHERE opType=1 AND vmCode='"+vmCode+"' AND basicItemId="+basicItemId+" and createTime>='"+start+"'";
        System.out.println(sql3);
        List<Map<String,Object>> list3=this.list(sql3,null);
        sql3=null;
        BigDecimal saleNumBig =(BigDecimal)((Map<String,Object>)list3.get(0)).get("num");
        Long saleNum=0l;
        if(saleNumBig!=null)
        	saleNum=saleNumBig.longValue();
        list3=null;
        VendingMachineHistoryBean bean = this.get(id);

        bean.setEndNum(endNum.longValue());
        
        bean.setReplenishNum(replenishNum.longValue());
        bean.setSaleNum(saleNum.longValue());

        bean.setBalanceNum(bean.getNum()+bean.getReplenishNum()-bean.getSaleNum()-bean.getEndNum());

        this.update(bean);
    }
    /**
     * 修改机器商品日志相应数量
     * @param vmCode
     * @param wayNumber
     * @param basicItemId
     * @param num
     */
    public void  updateBean(String vmCode,Integer wayNumber,Integer basicItemId,Integer num){

        Integer endNum=num;
        System.out.println("endNum="+endNum+"  basicItemId="+basicItemId);

        String sql1="select id from vending_machine_history where vmCode='"+vmCode+"' and itemId="+basicItemId+" and wayNumber="+wayNumber+" order by createTime desc limit 1";
        System.out.println(sql1);
        List<Map<String,Object>> list1=this.list(sql1,null);
        sql1=null;
        if(list1.size()<=0){
            log.info("找不到历史补货销售日志，不用修改");
            return ;
        }
        Long id=(Long)((Map<String,Object>)list1.get(0)).get("id");
        list1=null;
        DateTime exeTime=new DateTime();
        exeTime=exeTime.withHourOfDay(0);
        exeTime=exeTime.withMinuteOfHour(0);
        exeTime=exeTime.withSecondOfMinute(0);
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sf.format(exeTime.toDate());
        String sql2="select sum(num-preNum) as num from replenish_record where opType=2 and vmCode='"+vmCode+"' and basicItemId="+basicItemId+" and createTime>='"+start+"'";
        System.out.println(sql2);

        List<Map<String,Object>> list2=this.list(sql2,null);
        sql2=null;
        Long replenishNum=0l;
        if(list2.size()>=0) {
            BigDecimal replenishNumBig = (BigDecimal) ((Map<String, Object>) list2.get(0)).get("num");
            if (replenishNumBig != null)
                replenishNum = replenishNumBig.longValue();
        }
        list2=null;
        String sql3="SELECT sum(preNum-num) as num FROM vending_waynum_log WHERE opType=1 AND vmCode='"+vmCode+"' AND basicItemId="+basicItemId+" and createTime>='"+start+"'";
        System.out.println(sql3);
        List<Map<String,Object>> list3=this.list(sql3,null);
        sql3=null;
        Long saleNum=0l;
        if(list3.size()>=0) {
            BigDecimal saleNumBig = (BigDecimal) ((Map<String, Object>) list3.get(0)).get("num");
            if (saleNumBig != null)
                saleNum = saleNumBig.longValue();
        }
        list3=null;
        VendingMachineHistoryBean bean = this.get(id);

        bean.setEndNum(endNum.longValue());//最后的数量

        bean.setReplenishNum(replenishNum.longValue());
        bean.setSaleNum(saleNum);

        bean.setBalanceNum(bean.getNum()+bean.getReplenishNum()-bean.getSaleNum()-bean.getEndNum());

        this.update(bean);
    }
    /**
     * 记录对应机器货道的相关机器商品日志
     * @param vmCode
     * @param wayNumber
     */
    public void  addBean(String vmCode,Integer wayNumber){
        String sql="select w.num,i.basicItemId from vending_machines_way w left join vending_machines_item i on w.itemId=i.id where w.vendingMachinesCode='"+vmCode+"' and w.wayNumber="+wayNumber;
        List<Map<String,Object>> list=this.list(sql,null);
        System.out.println(sql);
        sql=null;
        Integer num=(Integer)((Map<String,Object>)list.get(0)).get("num");
        Long basicItemId=(Long)((Map<String,Object>)list.get(0)).get("basicItemId");
        list=null;
        VendingMachineHistoryBean bean = new VendingMachineHistoryBean();
        bean.setItemId(basicItemId.longValue());
        bean.setNum(num.longValue());
        bean.setWayNumber(wayNumber.longValue());
        bean.setRecordTime(new Date());
        bean.setCreateTime(new Date());
        bean.setVmCode(vmCode);
        this.insert(bean);
    }

    public ReturnDataUtil listPageGroupByItemId(VendingMachineHistoryCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
       // sql.append("select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ");

       sql.append(" SELECT h.id,h.itemId,sum(h.num) sNum,sum(h.endNum) sEndNum,sum(h.saleNum) sSaleNum,sum(h.replenishNum) sReplenishNum,sum(h.balanceNum) sBalanceNum,h.recordTime,h.createTime,ib.name AS itemName from vending_machines_info i INNER JOIN vending_machine_history h ON i.code=h.vmCode LEFT JOIN item_basic ib ON h.itemId=ib.id WHERE 1=1 ");
		if (condition.getAreaId() != null && condition.getAreaId()>0) {
			sql.append(" and i.areaId = '" + condition.getAreaId() + "' ");
		}
       if(StringUtil.isNotBlank(condition.getCompanyId())){
           sql.append(" and i.companyId="+condition.getCompanyId());
       }

        if(StringUtil.isNotBlank(condition.getVmCode())){
            sql.append(" and i.code = '"+condition.getVmCode()+"'");
        }
        if(condition.getBalance()!=0){
            sql.append(" and h.balanceNum!=0 ");
        }
        if(condition.isReplenish()){//有补货
           sql.append(" and h.replenishNum!=0 ");
        }
        if(condition.isSale()){
           sql.append(" and h.saleNum!=0 ");
        }
        if(StringUtil.isNotBlank(condition.getStartDay())){
            sql.append(" and h.recordTime >= '"+condition.getStartDay()+" 00:00:00'");
        }
        if(StringUtil.isNotBlank(condition.getEndDay())){
            sql.append(" and h.recordTime <= '"+condition.getEndDay()+" 23:59:59'");
        }
        sql.append(" and i.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" ");
        
        sql.append(" GROUP BY itemId");
        log.info("补货盘点SQL："+sql.toString());
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));

            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
            rs = pst.executeQuery();
            List<VendingMachineHistoryBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachineHistoryBean bean = new VendingMachineHistoryBean();
                bean.setId(rs.getLong("id"));
                //bean.setWayNumber(rs.getLong("wayNumber"));
                bean.setItemId(rs.getLong("itemId"));
                bean.setNum(rs.getLong("sNum"));
                bean.setEndNum(rs.getLong("sEndNum"));
                bean.setSaleNum(rs.getLong("sSaleNum"));
                bean.setReplenishNum(rs.getLong("sReplenishNum"));
                bean.setBalanceNum(rs.getLong("sBalanceNum"));
                bean.setRecordTime(rs.getDate("recordTime"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setItemName(rs.getString("itemName"));
                list.add(bean);
            }
            data.setCurrentPage(condition.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return data;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
    }
    
    public List<VendingMachineHistoryBean> saleNumNowGroupByItem(VendingMachineHistoryCondition condition) {
    	 StringBuilder sql = new StringBuilder();
         // sql.append("select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ");

         sql.append(" SELECT\r\n" + 
         		"	SUM(i.num) AS num,\r\n" + 
         		"	i.basicItemId AS itemId\r\n" + 
         		"FROM\r\n" + 
         		"	pay_record_item i\r\n" + 
         		"INNER JOIN pay_record p ON i.payRecordId = p.id\r\n" + 
         		"INNER JOIN vending_machines_info vmi ON vmi.code = p.vendingMachinesCode ");
         sql.append(" where 1=1 and (p.state=10001 or p.state=10002)");
         if(StringUtil.isNotBlank(condition.getCompanyId())){
             //sql.append(" and i.companyId="+condition.getCompanyId());
             sql.append(" and vmi.companyId in ("+redisClient.get("childList"+condition.getCompanyId())+") ");
         }else {
             sql.append(" and vmi.companyId in ("+redisClient.get("childList"+UserUtils.getUser().getCompanyId())+") ");
         }
         if(StringUtil.isNotBlank(condition.getStartDay())){
             sql.append(" and p.createTime  >= '"+condition.getStartDay()+" 00:00:00'");
         }
         if(StringUtil.isNotBlank(condition.getEndDay())){
             sql.append(" and p.createTime <= '"+condition.getEndDay()+" 23:59:59'");
         }
          
          sql.append(" GROUP BY i.basicItemId");
          log.info("销售盘点SQL："+sql.toString());
          Connection conn = null;
          PreparedStatement pst = null;
          ResultSet rs = null;
          List<VendingMachineHistoryBean> list = Lists.newArrayList();

          try {
              conn = openConnection();
              pst = conn.prepareStatement(sql.toString());

              rs = pst.executeQuery();
              while (rs.next()) {
                  VendingMachineHistoryBean bean = new VendingMachineHistoryBean();

                  bean.setNum(rs.getLong("num"));
                  bean.setItemId(rs.getLong("itemId"));
                  list.add(bean);
              }

              return list;
          } catch (SQLException e) {
              e.printStackTrace();
              log.error(e.getMessage());
              return list;
          } finally {
              this.closeConnection(rs, pst, conn);
          }
    	
    	
    }

    public List<VendingMachineHistoryBean> replenishNumNowGroupByItem(VendingMachineHistoryCondition condition) {
   	 StringBuilder sql = new StringBuilder();
        // sql.append("select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ");

        sql.append(" select \r\n" + 
        		"	sum(num - preNum) as rrNum,\r\n" + 
        		"	basicItemId\r\n" + 
        		"FROM\r\n" + 
        		"	replenish_record rr\r\n" + 
        		"LEFT JOIN vending_machines_info vmi ON vmi. CODE = rr.vmCode\r\n" + 
        		"WHERE opType = 2 ");
        sql.append(" ");
        if(StringUtil.isNotBlank(condition.getCompanyId())){
            //sql.append(" and i.companyId="+condition.getCompanyId());
            sql.append(" and vmi.companyId in ("+redisClient.get("childList"+condition.getCompanyId())+") ");
        }else {
            sql.append(" and vmi.companyId in ("+redisClient.get("childList"+UserUtils.getUser().getCompanyId())+") ");
        }
        if(StringUtil.isNotBlank(condition.getStartDay())){
            sql.append(" and rr.createTime  >= '"+condition.getStartDay()+" 00:00:00'");
        }
        if(StringUtil.isNotBlank(condition.getEndDay())){
            sql.append(" and rr.createTime <= '"+condition.getEndDay()+" 23:59:59'");
        }
         
         sql.append(" GROUP BY basicItemId");
         log.info("补货盘点SQL："+sql.toString());
         Connection conn = null;
         PreparedStatement pst = null;
         ResultSet rs = null;
         List<VendingMachineHistoryBean> list = Lists.newArrayList();

         try {
             conn = openConnection();
             pst = conn.prepareStatement(sql.toString());

             rs = pst.executeQuery();
             while (rs.next()) {
                 VendingMachineHistoryBean bean = new VendingMachineHistoryBean();

                 bean.setNum(rs.getLong("rrNum"));
                 bean.setItemId(rs.getLong("basicItemId"));
                 list.add(bean);
             }

             return list;
         } catch (SQLException e) {
             e.printStackTrace();
             log.error(e.getMessage());
             return list;
         } finally {
             this.closeConnection(rs, pst, conn);
         }
   	
   	
   }

    
    public List<VendingMachineHistoryBean> saleNumNowGroupByVmCode(VendingMachineHistoryCondition condition) {
   	 StringBuilder sql = new StringBuilder();
        // sql.append("select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ");

        sql.append(" SELECT\r\n" + 
        		"	SUM(i.num) AS num,\r\n" + 
        		"	vmi.code as vmCode\r\n" + 
        		"FROM\r\n" + 
        		"	pay_record_item i\r\n" + 
        		"INNER JOIN pay_record p ON i.payRecordId = p.id\r\n" + 
        		"INNER JOIN vending_machines_info vmi ON vmi.code = p.vendingMachinesCode ");
        sql.append(" where 1=1 and (p.state=10001 or p.state=10002)");
        if(StringUtil.isNotBlank(condition.getCompanyId())){
            //sql.append(" and i.companyId="+condition.getCompanyId());
            sql.append(" and vmi.companyId in ("+getCompanyList(condition.getCompanyId())+") ");
        }else {
            sql.append(" and vmi.companyId in ("+getCompanyList(UserUtils.getUser().getCompanyId().toString())+") ");
        }
        if(StringUtil.isNotBlank(condition.getStartDay())){
            sql.append(" and p.createTime  >= '"+condition.getStartDay()+" 00:00:00'");
        }
        if(StringUtil.isNotBlank(condition.getEndDay())){
            sql.append(" and p.createTime <= '"+condition.getEndDay()+" 23:59:59'");
        }
        if(StringUtil.isNotBlank(condition.getVmCode())){
            sql.append(" and vmi.code = "+condition.getVmCode());
        }


         sql.append(" GROUP BY vmi.code");
         log.info("销售盘点SQL："+sql.toString());
         Connection conn = null;
         PreparedStatement pst = null;
         ResultSet rs = null;
         List<VendingMachineHistoryBean> list = Lists.newArrayList();

         try {
             conn = openConnection();
             pst = conn.prepareStatement(sql.toString());

             rs = pst.executeQuery();
             while (rs.next()) {
                 VendingMachineHistoryBean bean = new VendingMachineHistoryBean();

                 bean.setNum(rs.getLong("num"));
                 bean.setVmCode(rs.getString("vmCode"));
                 list.add(bean);
             }

             return list;
         } catch (SQLException e) {
             e.printStackTrace();
             log.error(e.getMessage());
             return list;
         } finally {
             this.closeConnection(rs, pst, conn);
         }
   	
   	
   }

   public List<VendingMachineHistoryBean> replenishNumNowGroupByVmCode(VendingMachineHistoryCondition condition) {
  	 StringBuilder sql = new StringBuilder();
       // sql.append("select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ");

       sql.append(" select \r\n" + 
       		"	sum(num - preNum) as rrNum,\r\n" + 
       		"	rr.vmCode\r\n" + 
       		"FROM\r\n" + 
       		"	replenish_record rr\r\n" + 
       		"LEFT JOIN vending_machines_info vmi ON vmi. CODE = rr.vmCode\r\n" + 
       		"WHERE opType = 2 ");
       sql.append(" ");
       if(StringUtil.isNotBlank(condition.getCompanyId())){
           //sql.append(" and i.companyId="+condition.getCompanyId());
           sql.append(" and vmi.companyId in ("+getCompanyList(condition.getCompanyId())+") ");
       }else {
           sql.append(" and vmi.companyId in ("+getCompanyList(UserUtils.getUser().getCompanyId().toString())+") ");
       }
       if(StringUtil.isNotBlank(condition.getStartDay())){
           sql.append(" and rr.createTime  >= '"+condition.getStartDay()+" 00:00:00'");
       }
       if(StringUtil.isNotBlank(condition.getEndDay())){
           sql.append(" and rr.createTime <= '"+condition.getEndDay()+" 23:59:59'");
       }
       if(StringUtil.isNotBlank(condition.getVmCode())){
           sql.append(" and vmi.code = "+condition.getVmCode());
       }
        sql.append(" GROUP BY rr.vmCode");
        log.info("补货盘点SQL："+sql.toString());
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<VendingMachineHistoryBean> list = Lists.newArrayList();

        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());

            rs = pst.executeQuery();
            while (rs.next()) {
                VendingMachineHistoryBean bean = new VendingMachineHistoryBean();

                bean.setNum(rs.getLong("rrNum"));
                bean.setVmCode(rs.getString("vmCode"));
                list.add(bean);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return list;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
  	
  	
  }
   
   public String getCompanyList(String companyId) {
	  	 StringBuilder sql = new StringBuilder();
	       // sql.append("select id,vmCode,wayNumber,itemId,num,endNum,saleNum,replenishNum,balanceNum,recordTime,createTime from vending_machine_history where 1=1 ");

	       sql.append(" select getChildList("+companyId+") a from dual ");

	        Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
	        String abc=null;
	        try {
	            conn = openConnection();
	            pst = conn.prepareStatement(sql.toString());

	            rs = pst.executeQuery();
	            while (rs.next()) {
	            	abc=rs.getString("a");
	            	return abc;
	            }	        
	        } catch (SQLException e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            return abc;
	        } finally {
	            this.closeConnection(rs, pst, conn);
	        }
			return abc;
   }
}

