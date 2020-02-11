package com.server.module.system.machineManage.machinesWayItem;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machinesWay.WayItem;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.server.common.persistence.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: yjr
 * create time: 2018-08-31 14:03:10
 */
@Repository
public class VendingMachinesWayItemDaoImpl extends BaseDao<VendingMachinesWayItemBean> implements VendingMachinesWayItemDao {

    private static Logger log = LogManager.getLogger(VendingMachinesWayItemDaoImpl.class);

    public ReturnDataUtil listPage(VendingMachinesWayItemCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,machineWayId,vmCode,wayNumber,basicItemId,weight,orderNumber,price,num,fullNum,updateTime,createTime,promotionPrice from vending_machines_way_item where 1=1 ");

        if(condition.getWayId()!=null){
            sql.append(" and machineWayId="+condition.getWayId());
        }

        sql.append(" order by orderNumber ");
        List<Object> plist = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            List<VendingMachinesWayItemBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachinesWayItemBean bean = new VendingMachinesWayItemBean();
                bean.setId(rs.getLong("id"));
                bean.setMachineWayId(rs.getLong("machineWayId"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setWayNumber(rs.getLong("wayNumber"));
                bean.setBasicItemId(rs.getLong("basicItemId"));
                bean.setPrice(rs.getDouble("price"));
                bean.setNum(rs.getLong("num"));
                bean.setFullNum(rs.getLong("fullNum"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setOrderNumber(rs.getInt("orderNumber"));
                bean.setWeight(rs.getInt("weight"));
                bean.setPromotionPrice(rs.getDouble("promotionPrice"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
                log.info(plist.toString());
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
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public ReturnDataUtil listPageForReplenish(VendingMachinesWayItemCondition condition) {
    	log.info("<VendingMachinesWayItemDaoImpl>------<listPageForReplenish>-----start");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        double rate=0.3;
        if(condition.getRate()!=null){
        	rate=condition.getRate()*0.01;
        }
        if(condition.getType()==1){
            sql.append("SELECT DISTINCT m.code as vmCode,m.locatoinName,m.companyId,m.areaId,m.lineId from  vending_machines_info m ,vending_machines_way i WHERE m.machineVersion=1 and m.code=i.vendingMachinesCode and m.state=20001 AND (i.fullNum-i.num)/i.fullNum>="+rate);
        }else {
            sql.append(" SELECT DISTINCT m.code as vmCode,m.companyId,m.locatoinName,m.areaId,m.lineId   from  `vending_machines_way_item` i INNER JOIN vending_machines_info m ON i.vmCode=m.code WHERE m.machineVersion=2 and m.state=20001 and (i.fullNum-i.num)/i.fullNum>="+rate);
        }
        if(condition.getCompanyId()!=null) {
            sql.append(" AND (m.companyId="+condition.getCompanyId());
		    sql.append(" or FIND_IN_SET( m.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.userId="+UserUtils.getUser().getId()+")))");
        }
        if(condition.getAreaId()!=null && condition.getAreaId()>0)
            sql.append(" AND m.areaId="+condition.getAreaId());
        if(condition.getLineId()!=null)
            sql.append(" AND m.lineId="+condition.getLineId());
        if(StringUtil.isNotBlank(condition.getVmCode())) {
        	sql.append(" AND m.code="+condition.getVmCode());
        }
		if (condition.getAreaId() != null && condition.getAreaId()>0) {
			sql.append(" and m.areaId = '" + condition.getAreaId() + "' ");
		}
        sql.append(" order by (i.fullNum-i.num)/i.fullNum,m.code ");

        List<Object> plist = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement("SELECT COUNT(a.vmCode) FROM ("+sql.toString()+") a");
            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            List<ReplenishVo> list = Lists.newArrayList();
            while (rs.next()) {
            	ReplenishVo bean = new ReplenishVo();
                bean.setVmCode(rs.getString("vmCode"));
                bean.setCompanyId(rs.getInt("companyId"));
                bean.setAreaId(rs.getInt("areaId"));
                bean.setLineId(rs.getInt("lineId"));
                bean.setLocatoinName(rs.getString("locatoinName"));
                list.add(bean);
            }
            if (showSql) {
                log.info("商品缺货管理sql语句："+sql);
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
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public List<ReplenishItemVo> listForReplenish(List<String> codes,Integer type) {
        StringBuilder sql = new StringBuilder();
       
        StringBuilder insql=new StringBuilder();
        for(int i=0;i<codes.size();i++){
        	insql.append("'").append(codes.get(i)).append("'");
        	if(i!=codes.size()-1)
        	insql.append(",");
        }
        if(type==1){
        	sql.append("SELECT i.vendingMachinesCode AS vmCode,i.num,i.fullNum,i.wayNumber,b.name,b.simpleName,(i.fullNum-i.num)/i.fullNum*100 AS percent,i.fullNum-i.num AS replenishNum from vending_machines_way i INNER JOIN vending_machines_item m ON i.itemId=m.id LEFT JOIN item_basic b ON m.basicItemId=b.id WHERE 1=1 ");
        }else{
            sql.append(" SELECT  i.vmCode,i.num,i.fullNum,i.wayNumber,b.name,b.simpleName,(i.fullNum-i.num)/i.fullNum*100 AS percent,i.fullNum-i.num as replenishNum from  `vending_machines_way_item` i LEFT JOIN item_basic b ON i.basicItemId=b.id WHERE 1=1 ");

        }
    
        if(codes.size()>0){
        	
        	if(type==1){
                sql.append(" and i.vendingMachinesCode in  ("+insql.toString()+")");
        	}else{     		
                sql.append(" and i.vmCode in  ("+insql.toString()+")");
        	}
        }
       // sql.append(" order by i.vmCode");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ReplenishItemVo> list=Lists.newArrayList();
        try {
            conn = openConnection();      
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
            	ReplenishItemVo bean = new ReplenishItemVo();
                bean.setVmCode(rs.getString("vmCode"));
                bean.setFullNum(rs.getInt("fullNum"));
                bean.setName(rs.getString("name"));
                bean.setNum(rs.getInt("num"));
                bean.setPercent(new Double(rs.getDouble("percent")).intValue());
                bean.setReplenishNum(rs.getInt("replenishNum"));
                bean.setSimpleName(rs.getString("simpleName"));
                bean.setWayNumber(rs.getInt("wayNumber"));     
                Integer outQuantity=rs.getInt("fullNum")-rs.getInt("num");
                if(outQuantity>=0) {
                	 bean.setOutQuantity(0);
                }else {
                	 bean.setOutQuantity(outQuantity);
                }
               
                list.add(bean);
                
            }
            if (showSql) {
                log.info(sql);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return list;
        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public VendingMachinesWayItemBean findItemBean( String vmCode ) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM vending_machines_way_item WHERE vmCode = '" + vmCode + "'  ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();

            if (showSql) {
                log.info(sql);
            }
            // 如果有数据，返回true，没数据，返回false
            if (rs.next()) {
                VendingMachinesWayItemBean bean = new VendingMachinesWayItemBean();
                bean.setId(rs.getLong("id"));
                bean.setMachineWayId(rs.getLong("machineWayId"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setWayNumber(rs.getLong("wayNumber"));
                bean.setBasicItemId(rs.getLong("basicItemId"));
                bean.setWeight(rs.getInt("weight"));
                bean.setOrderNumber(rs.getInt("orderNumber"));
                bean.setPrice(rs.getDouble("price"));
                bean.setNum(rs.getLong("num"));
                bean.setFullNum(rs.getLong("fullNum"));
                bean.setUpdateTime(rs.getTime("updateTime"));
                bean.setCreateTime(rs.getTime("createTime"));
                bean.setPromotionPrice(rs.getDouble("promotionPrice"));
                bean.setPicId(rs.getLong("picId"));
                bean.setMaxCapacity(rs.getInt("maxCapacity"));
                return bean;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());

        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ReturnDataUtil edit(  VendingMachinesWayItemBean bean)  {
        StringBuilder sql = new StringBuilder();
        ReturnDataUtil data = new ReturnDataUtil();
        sql.append("UPDATE vending_machines_way_item SET maxCapacity="+bean.getMaxCapacity() +" WHERE vmCode ="+bean.getVmCode()+"");
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            int num = pst.executeUpdate();
            if (showSql) {
                log.info(sql);
            }
            if (num > 0) {
                data.setStatus(1);
                data.setMessage("修改成功！");
            } else {
                data.setStatus(0);
                data.setMessage("修改失败！");
            }
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            data.setStatus(0);
            data.setMessage("修改失败！");
            return data;
        } finally {
            try {
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public VendingMachinesWayItemBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Long id) {
     
        VendingMachinesWayItemBean obj=new VendingMachinesWayItemBean();
        obj.setId(id);
        boolean flag=super.del(obj);       
        return flag;
    }

    public boolean update(VendingMachinesWayItemBean entity) {
        return super.update(entity);
    }

    public VendingMachinesWayItemBean insert(VendingMachinesWayItemBean entity) {
        return super.insert(entity);
    }
    
   

    public List<WayItem> listWayItem(Long wayId) {

    	 StringBuilder sql = new StringBuilder();
         sql.append("SELECT vmp.homeImg as topLeftPic,wi.picId,wi.id,wi.machineWayId,wi.vmCode,wi.wayNumber,wi.basicItemId,wi.weight,wi.orderNumber,wi.promotionPrice,"
         		+ "wi.price,wi.num,"
         		+ "wi.fullNum,b.name,b.pic FROM vending_machines_way_item wi LEFT JOIN item_basic b ON wi.basicItemId=b.id left join vending_machines_pic vmp on vmp.id=wi.picId WHERE  ");

         sql.append(" machineWayId="+wayId);
         sql.append(" order by wi.orderNumber");
         Connection conn = null;
         PreparedStatement pst = null;
         ResultSet rs = null;
         try {
             conn = openConnection();
             pst = conn.prepareStatement(sql.toString());
             rs = pst.executeQuery();
             List<WayItem> list = Lists.newArrayList();
             while (rs.next()) {
             	WayItem bean = new WayItem();
            
                 bean.setBasicItemId(rs.getLong("basicItemId"));
                 bean.setPrice(rs.getDouble("price"));
                 bean.setNum(rs.getLong("num"));
                 bean.setFullNum(rs.getLong("fullNum"));
                 bean.setItemPic(rs.getString("pic"));
                 bean.setItemName(rs.getString("name"));
                 bean.setOrderNumber(rs.getInt("orderNumber"));
                 bean.setWeight(rs.getInt("weight"));
                 bean.setId(rs.getLong("id"));
                 bean.setPromotionPrice(rs.getDouble("promotionPrice"));
                 bean.setPicId(rs.getLong("picId"));
                 bean.setTopLeftPic(rs.getString("topLeftPic"));
                 list.add(bean);
             }
             if (showSql) {
                 log.info(sql);
             }

             return list;
         } catch (SQLException e) {
             e.printStackTrace();
             log.error(e.getMessage());
             return Lists.newArrayList();
         } finally {
             try {
                 rs.close();
                 pst.close();
                 closeConnection(conn);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

    }


    public List<VendingMachinesWayItemBean> list(Long wayId) {

        StringBuilder sql = new StringBuilder();
        sql.append("select id,machineWayId,vmCode,wayNumber,weight,basicItemId,price,num,fullNum,updateTime,createTime,promotionPrice from vending_machines_way_item where 1=1 ");
        sql.append(" and machineWayId="+wayId);
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            List<VendingMachinesWayItemBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachinesWayItemBean bean = new VendingMachinesWayItemBean();
                bean.setId(rs.getLong("id"));
                bean.setMachineWayId(rs.getLong("machineWayId"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setWayNumber(rs.getLong("wayNumber"));
                bean.setBasicItemId(rs.getLong("basicItemId"));
                bean.setPrice(rs.getDouble("price"));
                bean.setNum(rs.getLong("num"));
                bean.setWeight(rs.getInt("weight"));
                bean.setFullNum(rs.getLong("fullNum"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setPromotionPrice(rs.getDouble("promotionPrice"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Lists.newArrayList();
        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

	@Override
	public List<VendingMachinesWayItemBean> list(VendingMachinesWayItemCondition condition) {
	        StringBuilder sql = new StringBuilder();
	        sql.append(" select vmi.locatoinName,vmwi.* from vending_machines_way_item vmwi  ");
	        sql.append(" left join vending_machines_info vmi on vmwi.vmCode=vmi.code where  ");
	        sql.append(" vmi.state=20001 and vmi.locatoinName!=1 and machineVersion=2");
	        sql.append(" ORDER BY vmCode,wayNumber");
	        List<Object> plist = Lists.newArrayList();
	        Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
            List<VendingMachinesWayItemBean> list = Lists.newArrayList();

	        try {
	            conn = openConnection();
	            if (plist != null && plist.size() > 0)
	                for (int i = 0; i < plist.size(); i++) {
	                    pst.setObject(i + 1, plist.get(i));
	            }
	            pst = conn.prepareStatement(sql.toString());
	            rs = pst.executeQuery();
	            while (rs.next()) {
	                VendingMachinesWayItemBean bean = new VendingMachinesWayItemBean();
	                bean.setId(rs.getLong("id"));
	                bean.setAddress(rs.getString("locatoinName"));

	                bean.setMachineWayId(rs.getLong("machineWayId"));
	                bean.setVmCode(rs.getString("vmCode"));
	                bean.setWayNumber(rs.getLong("wayNumber"));
	                bean.setBasicItemId(rs.getLong("basicItemId"));
	                bean.setPrice(rs.getDouble("price"));
	                bean.setNum(rs.getLong("num"));
	                bean.setFullNum(rs.getLong("fullNum"));
	                bean.setUpdateTime(rs.getDate("updateTime"));
	                bean.setCreateTime(rs.getDate("createTime"));
	                bean.setOrderNumber(rs.getInt("orderNumber"));
	                bean.setWeight(rs.getInt("weight"));
	                bean.setPromotionPrice(rs.getDouble("promotionPrice"));
	                if(StringUtils.isNotBlank(bean.getVmCode())) {
		                list.add(bean);
	                }
	            }
	            if (showSql) {
	                log.info(sql);
	                log.info(plist.toString());
	            }
	            return list;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            return list;
	        } finally {
	            try {
	                rs.close();
	                pst.close();
	                closeConnection(conn);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	}
   
	@Override	
	public boolean deleteSQL(Long id) {
		StringBuilder sql = new StringBuilder();
        sql.append("delete  from vending_machines_way_item where id ="+id);
        Connection conn = null;
        PreparedStatement pst = null;
        int rs = 0;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeUpdate();
            while (rs>0) {
            	return true;
            }
            if (showSql) {
                log.info(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        } finally {
            try {
            	pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return false;
	}

}

