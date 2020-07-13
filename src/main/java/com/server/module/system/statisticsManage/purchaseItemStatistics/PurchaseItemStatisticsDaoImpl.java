package com.server.module.system.statisticsManage.purchaseItemStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.supplierManage.SupplierServiceImpl;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: yjr
 * create time: 2018-08-24 11:02:40
 */
@Repository
public class PurchaseItemStatisticsDaoImpl extends BaseDao<PurchaseItemStatisticsBean> implements PurchaseItemStatisticsDao {

	public static Logger log = LogManager.getLogger(SupplierServiceImpl.class); 	
	@Autowired
	private CompanyDao companyDaoImpl;

    public ReturnDataUtil listPage(PurchaseItemStatisticsCondition condition) {
		log.info("<PurchaseItemStatisticsDaoImpl>----<listPage>-------start");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(" select pis.id,p.id as itemId,p.barCode,p.name as itemName,si.name as unitName,pis.avgPrice,pis.sumQuantity,pis.createTime,pis.deleteFlag from Purchase_item_statistics pis  ");
        //sql.append(" left join supplier s on s.id = supplierId");
        sql.append(" left join item_basic p on p.id = pis.itemId");
        sql.append(" left join state_info si on si.state = p.unit");
        sql.append(" where pis.deleteFlag=0");
       /* if(StringUtil.isNotBlank(condition.getItemName())){
            sql.append(" and itemName like '%"+condition.getItemName()+"%'");
        }
        if(StringUtil.isNotBlank(condition.getSupplierName())){
            sql.append(" and supplierName like '%"+condition.getSupplierName()+"%'");
        }*/
        if(condition.getItemId()!=null){
            sql.append(" and pis.itemId="+condition.getItemId());
        }
        if(StringUtil.isNotBlank(condition.getBarCode())){
            sql.append(" and p.barCode="+condition.getBarCode());
        }
        if(StringUtil.isNotBlank(condition.getItemName())){
            sql.append(" and p.name like '%" + condition.getItemName() + "%'");
        }
//        if(condition.getSupplierId()!=null){
//            sql.append(" and pis.supplierId="+condition.getSupplierId());
//        }
        sql.append(" and pis.itemId in (select itemId from purchase_bill_item pbi left join supplier s on s.id=pbi.supplierId where s.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+")");
        sql.append(" order by pis.createTime desc");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("采购成本统计SQL语句："+sql.toString());
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
            List<PurchaseItemStatisticsBean> list = Lists.newArrayList();
            while (rs.next()) {
                PurchaseItemStatisticsBean bean = new PurchaseItemStatisticsBean();
                bean.setId(rs.getLong("id"));
                //bean.setSupplierId(rs.getLong("supplierId"));
                //bean.setSupplierName(rs.getString("supplierName"));

                bean.setItemId(rs.getLong("itemId"));
                bean.setBarCode(rs.getString("barCode"));
                bean.setItemName(rs.getString("itemName"));
                bean.setUnitName(rs.getString("unitName"));
                bean.setAvgPrice(rs.getDouble("avgPrice"));
                bean.setSumQuantity(rs.getLong("sumQuantity"));              
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setDeleteFlag(rs.getInt("deleteFlag"));
                list.add(bean);
            }
            data.setCurrentPage(condition.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
    		log.info("<PurchaseItemStatisticsDaoImpl>----<listPage>-------end");
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return data;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
    }

    public PurchaseItemStatisticsBean get(Long id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        PurchaseItemStatisticsBean entity = new PurchaseItemStatisticsBean();
        return super.del(entity);
    }

    public boolean update(PurchaseItemStatisticsBean entity) {
        return super.update(entity);
    }

    public PurchaseItemStatisticsBean insert(PurchaseItemStatisticsBean entity) {
        return super.insert(entity);
    }

    public List<PurchaseItemStatisticsBean> list(PurchaseItemStatisticsCondition condition) {
        return null;
    }

	@Override
	public PurchaseItemStatisticsBean getBeanByItemId(Long itemId) {
		log.info("<PurchaseItemStatisticsDaoImpl>----<listPage>-------start");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(" select pis.id,pis.itemId,pis.avgPrice,pis.sumQuantity,pis.createTime,pis.deleteFlag from Purchase_item_statistics pis  ");
        sql.append(" where pis.deleteFlag=0 and itemId="+itemId);


        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PurchaseItemStatisticsBean bean = new PurchaseItemStatisticsBean();

        log.info("采购成本统计SQL语句："+sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            List<PurchaseItemStatisticsBean> list = Lists.newArrayList();
            while (rs.next()) {
                bean.setId(rs.getLong("id"));
                //bean.setSupplierId(rs.getLong("supplierId"));
                //bean.setSupplierName(rs.getString("supplierName"));
                bean.setItemId(rs.getLong("itemId"));

                bean.setAvgPrice(rs.getDouble("avgPrice"));
                bean.setSumQuantity(rs.getLong("sumQuantity"));              
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setDeleteFlag(rs.getInt("deleteFlag"));
            }
    		log.info("<PurchaseItemStatisticsDaoImpl>----<listPage>-------end");
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return bean;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
	}
}

