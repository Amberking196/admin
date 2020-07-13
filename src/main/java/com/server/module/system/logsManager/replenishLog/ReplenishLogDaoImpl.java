package com.server.module.system.logsManager.replenishLog;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
/**
 * author name: yjr create time: 2018-03-23 11:24:53
 */
@Repository
public class ReplenishLogDaoImpl extends BaseDao<ReplenishLogBean> implements ReplenishLogDao {

	public static Logger log = LogManager.getLogger(ReplenishLogDaoImpl.class); 

	@Autowired
    private CompanyDao companyDaoImpl;
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(ReplenishLogCondition condition) {
		       ReturnDataUtil data=new ReturnDataUtil();
		       StringBuilder sql=new StringBuilder();
		        sql.append("select id,vmCode,wayNumber,newItemName,newBarCode,newStandard,newState,newNum,oldItemName,oldBarCode,oldStandard,oldState,oldNum,operator,createTime from replenish_log where 1=1 ");
				if (condition.getCompanyId() == null || condition.getCompanyId()<=1 ){ //总公司或超级管理员	
				} else {
					String insql=companyDaoImpl.findAllSonCompanyIdForInSql(condition.getCompanyId());
				    sql.append(" and vmCode in (select code from  vending_machines_info where companyId in "+insql+") ");
				}
				List<Object> plist=Lists.newArrayList();
				if (StringUtil.isNotBlank(condition.getVmCode())){
					sql.append(" and vmCode like ?");
					plist.add("%"+condition.getVmCode()+"%");
				}
				if (StringUtil.isNotBlank(condition.getItemName())){
					sql.append(" and newItemName like ?");
					plist.add("%"+condition.getItemName()+"%");
				}
				if (StringUtil.isNotBlank(condition.getBarCode())){
					sql.append(" and barCode like ?");
					plist.add("%"+condition.getBarCode()+"%");
				}
				if (StringUtil.isNotBlank(condition.getOperator())){
					sql.append(" and operator like ?");
					plist.add("%"+condition.getOperator()+"%");
				}
				if (condition.getWayNumber()!=null){
					sql.append(" and wayNumber = ?");
					plist.add(condition.getWayNumber());
				}
				if (condition.getStartTime()!=null){
					sql.append(" and createTime >= ?");
					plist.add(condition.getStartTime());
				}
				if (condition.getEndTime()!=null){
					sql.append(" and createTime <= ?");
					plist.add(condition.getEndTime());
				}
				Connection conn=null;
				PreparedStatement pst=null;
				ResultSet rs=null;
				try {
					conn=openConnection();
					pst=conn.prepareStatement(super.countSql(sql.toString()));
					//int count=//row.getListColumn().size();
					if (plist!=null && plist.size()>0)
					for (int i=0;i<plist.size();i++){
						pst.setObject(i+1, plist.get(i));
					}
					rs = pst.executeQuery();
					long count=0;
					while(rs.next()){
						count=rs.getInt(1);
					}
					/////
					long off=(condition.getCurrentPage()-1)*condition.getPageSize();			
					pst=conn.prepareStatement(sql.toString()+" limit "+off+","+condition.getPageSize());
					if (plist!=null && plist.size()>0)
					for (int i=0;i<plist.size();i++){
						pst.setObject(i+1, plist.get(i));
					}
					rs = pst.executeQuery();
					List<ReplenishLogBean> list=Lists.newArrayList();
					while(rs.next()){
						ReplenishLogBean bean = new ReplenishLogBean();
						bean.setId(rs.getLong("id"));
						bean.setVmCode(rs.getString("vmCode"));
						bean.setWayNumber(rs.getInt("wayNumber"));
						bean.setCreateTime(rs.getDate("createTime"));
						bean.setNewBarCode(rs.getString("newBarCode"));
						bean.setNewItemName(rs.getString("newItemName"));
						bean.setNewNum(rs.getInt("newNum"));
						bean.setNewStandard(rs.getString("newStandard"));
						bean.setNewState(rs.getInt("newState"));
						bean.setOldBarCode(rs.getString("oldBarCode"));
						bean.setOldState(rs.getInt("oldState"));
						bean.setOldNum(rs.getInt("oldNum"));
						bean.setOldItemName(rs.getString("oldItemName"));
						bean.setOldStandard(rs.getString("oldStandard"));
						bean.setOperator(rs.getString("operator"));
						list.add(bean);
					}
					if (showSql){
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
				} finally{
					try {
						rs.close();
						pst.close();
						closeConnection(conn);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
	}
	public List<ReplenishLogBean> listEntity(ReplenishLogCondition condition) {
		    StringBuilder sql=new StringBuilder();
	        sql.append("select id,vmCode,wayNumber,newItemName,newBarCode,newStandard,newState,newNum,oldItemName,oldBarCode,oldStandard,oldState,oldNum,operator,createTime from replenish_log where 1=1 ");
			if (condition.getCompanyId() == null || condition.getCompanyId()<=1 ){ //总公司或超级管理员	
			} else {
				String insql=companyDaoImpl.findAllSonCompanyIdForInSql(condition.getCompanyId());
			    sql.append(" and vmCode in (select code from  vending_machines_info where companyId in "+insql+") ");
			}
			List<Object> plist=Lists.newArrayList();
			if (StringUtil.isNotBlank(condition.getVmCode())){
				sql.append(" and vmCode like ?");
				plist.add("%"+condition.getVmCode()+"%");
			}
			if (StringUtil.isNotBlank(condition.getItemName())){
				sql.append(" and newItemName like ?");
				plist.add("%"+condition.getItemName()+"%");
			}
			if (StringUtil.isNotBlank(condition.getBarCode())){
				sql.append(" and barCode like ?");
				plist.add("%"+condition.getBarCode()+"%");
			}
			if (StringUtil.isNotBlank(condition.getOperator())){
				sql.append(" and operator like ?");
				plist.add("%"+condition.getOperator()+"%");
			}
			if (condition.getWayNumber()!=null){
				sql.append(" and wayNumber = ?");
				plist.add(condition.getWayNumber());
			}
			if (condition.getStartTime()!=null){
				sql.append(" and createTime >= ?");
				plist.add(condition.getStartTime());
			}
			if (condition.getEndTime()!=null){
				sql.append(" and createTime <= ?");
				plist.add(condition.getEndTime());
			}
			Connection conn=null;
			PreparedStatement pst=null;
			ResultSet rs=null;
			List<ReplenishLogBean> list=Lists.newArrayList();
			try {
				conn=openConnection();
				pst=conn.prepareStatement(sql.toString());
				if (plist!=null && plist.size()>0)
				for (int i=0;i<plist.size();i++){
					pst.setObject(i+1, plist.get(i));
				}
				rs = pst.executeQuery();
				while(rs.next()){
					ReplenishLogBean bean = new ReplenishLogBean();
					bean.setId(rs.getLong("id"));
					bean.setVmCode(rs.getString("vmCode"));
					bean.setWayNumber(rs.getInt("wayNumber"));
					bean.setCreateTime(rs.getDate("createTime"));
					bean.setNewBarCode(rs.getString("newBarCode"));
					bean.setNewItemName(rs.getString("newItemName"));
					bean.setNewNum(rs.getInt("newNum"));
					bean.setNewStandard(rs.getString("newStandard"));
					bean.setNewState(rs.getInt("newState"));
					bean.setOldBarCode(rs.getString("oldBarCode"));
					bean.setOldState(rs.getInt("oldState"));
					bean.setOldNum(rs.getInt("oldNum"));
					bean.setOldItemName(rs.getString("oldItemName"));
					bean.setOldStandard(rs.getString("oldStandard"));
					bean.setOperator(rs.getString("operator"));
					list.add(bean);
				}
				if (showSql){
					log.info(sql);
					log.info(plist.toString());
				}	
				return list;
				
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return list;
			} finally{
				try {
					rs.close();
					pst.close();
					closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}
	public ReplenishLogBean getEntity(Object id) {
		StringBuilder sql=new StringBuilder();
        sql.append("select id,vmCode,wayNumber,newItemName,newBarCode,newStandard,newState,newNum,oldItemName,oldBarCode,oldStandard,oldState,oldNum,operator,createTime from replenish_log where 1=1 ");
	    sql.append(" and id=?");
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		if (showSql){
			log.info(sql);
			log.info(id);
		}
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				ReplenishLogBean bean = new ReplenishLogBean();
				bean.setId(rs.getLong("id"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setNewBarCode(rs.getString("newBarCode"));
				bean.setNewItemName(rs.getString("newItemName"));
				bean.setNewNum(rs.getInt("newNum"));
				bean.setNewStandard(rs.getString("newStandard"));
				bean.setNewState(rs.getInt("newState"));
				bean.setOldBarCode(rs.getString("oldBarCode"));
				bean.setOldState(rs.getInt("oldState"));
				bean.setOldNum(rs.getInt("oldNum"));
				bean.setOldItemName(rs.getString("oldItemName"));
				bean.setOldStandard(rs.getString("oldStandard"));
				bean.setOperator(rs.getString("operator"));
				return bean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally{
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
	public boolean delEntity(Object id) {
				StringBuilder sql=new StringBuilder();
		        sql.append("delete from replenish_log where id=? ");
				Connection conn=null;
				PreparedStatement pst=null;
				if (showSql){
					log.info(sql);
					log.info(id);
				}
				try {
					conn=openConnection();
					pst=conn.prepareStatement(sql.toString());
					pst.setObject(1, id);
					boolean b = pst.execute();
					return b;
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
	}
	public boolean updateEntity(ReplenishLogBean entity) {
	   return super.update(entity);
	}
    	
}
