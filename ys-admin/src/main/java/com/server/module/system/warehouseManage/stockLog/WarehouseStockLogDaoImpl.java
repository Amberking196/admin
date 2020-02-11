package com.server.module.system.warehouseManage.stockLog;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.baseManager.stateInfo.StateInfoDto;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import jersey.repackaged.com.google.common.collect.Maps;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.server.common.persistence.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.List;
import java.util.Map;

/**
 * author name: yjr
 * create time: 2018-05-22 11:00:53
 */
@Repository
public class WarehouseStockLogDaoImpl extends BaseDao<WarehouseStockLogBean> implements WarehouseStockLogDao {

    private static Log log = LogFactory.getLog(WarehouseStockLogDaoImpl.class);
    @Autowired
    private StateInfoDao stateInfoDao;
    @Autowired
    private CompanyDao companyDaoImpl;
    public ReturnDataUtil listPage(WarehouseStockLogCondition condition) {
    	
    	List<StateInfoDto> stateInfos=stateInfoDao.findStateInfoByKeyName("warehouse_warrant");
    	Map<Integer,String> stateMap=Maps.newHashMap();
    	//获取状态信息
    	for (StateInfoDto stateInfoDto : stateInfos) {
			stateMap.put(stateInfoDto.getState(), stateInfoDto.getName());
		}
    	 stateInfos=stateInfoDao.findStateInfoByKeyName("warehouse_retype");
    	for (StateInfoDto stateInfoDto : stateInfos) {
			stateMap.put(stateInfoDto.getState(), stateInfoDto.getName());
		}
    	 stateInfos=stateInfoDao.findStateInfoByKeyName("warehouse_return");
     	for (StateInfoDto stateInfoDto : stateInfos) {
 			stateMap.put(stateInfoDto.getState(), stateInfoDto.getName());
 		}
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,stockId,warehouseId,billItemId,billId,itemId,itemName,warehouseName,quantity,preQuantity,num,type,createTime from warehouse_stock_log where 1=1 ");

        if(StringUtils.isNotEmpty(condition.getBillId())){//出入单id
            sql.append(" and billId="+condition.getBillId());
        }

        if(StringUtils.isNotEmpty(condition.getItemId())){//商品id
            sql.append(" and itemId="+condition.getItemId());
        }
        if(StringUtils.isNotEmpty(condition.getWarehouseId())){//仓库id
            sql.append(" and warehouseId="+condition.getWarehouseId());
        }else {
            Integer companyId= UserUtils.getUser().getCompanyId();
            if(companyId!=1){//用户只能查看自己公司下的日志
                sql.append(" and warehouseId in (select id from warehouse_info where companyId in"+companyDaoImpl.findAllSonCompanyIdForInSql(companyId)+")");//warehouse_info
            }

        }




        if(condition.getStartTime()!=null){
            sql.append(" and createTime>='"+ DateUtil.formatYYYYMMDDHHMMSS(condition.getStartTime())+" 00:00:00'");
        }
        if(condition.getEndTime()!=null){
            sql.append(" and createTime<='"+DateUtil.formatYYYYMMDDHHMMSS(condition.getEndTime())+" 23:59:59'");
        }
        if (showSql) {
            log.info(sql.toString());
        }
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
            sql.append(" order by createTime desc");//排序
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
            rs = pst.executeQuery();
            List<WarehouseStockLogBean> list = Lists.newArrayList();
            while (rs.next()) {
                WarehouseStockLogBean bean = new WarehouseStockLogBean();
                bean.setId(rs.getLong("id"));
                bean.setStockId(rs.getLong("stockId"));
                bean.setBillItemId(rs.getLong("billItemId"));
                bean.setBillId(rs.getLong("billId"));
                bean.setItemId(rs.getLong("itemId"));
                bean.setItemName(rs.getString("itemName"));
                bean.setWarehouseName(rs.getString("warehouseName"));
                bean.setQuantity(rs.getLong("quantity"));
                bean.setPreQuantity(rs.getLong("preQuantity"));
                bean.setNum(rs.getLong("num"));
                bean.setType(rs.getInt("type"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setTypeLabel(stateMap.get(bean.getType()));
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
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public WarehouseStockLogBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        WarehouseStockLogBean entity = new WarehouseStockLogBean();
        return super.del(entity);
    }

    public boolean update(WarehouseStockLogBean entity) {
        return super.update(entity);
    }

    public WarehouseStockLogBean insert(WarehouseStockLogBean entity) {
        return super.insert(entity);
    }
    public WarehouseStockLogBean insert(Connection conn,WarehouseStockLogBean entity) {

        return (WarehouseStockLogBean)super.insert(conn,entity);
    }

    public List<WarehouseStockLogBean> list(WarehouseStockLogCondition condition) {
        return null;
    }
}

