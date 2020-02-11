package com.server.module.system.companyManage.companyMachines;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;
import java.util.Map;

/**
 * author name: yjr create time: 2018-09-14 15:10:24
 */
@Repository
public class ReplenishCompanyMachinesDaoImpl extends BaseDao<ReplenishCompanyMachinesBean>
		implements ReplenishCompanyMachinesDao {

	private static Log log = LogFactory.getLog(ReplenishCompanyMachinesDaoImpl.class);

	public ReturnDataUtil listPage(ReplenishCompanyMachinesCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT i.code vmCode, i.locatoinName,r.id,r.companyId,r.code,r.create_time from vending_machines_info i LEFT JOIN replenish_company_machines r ON i.code=r.code "); 
        sql.append(" WHERE 1=1 ");
        if(condition.getCompanyId()!=null){
        	sql.append(" and i.companyId="+condition.getCompanyId());
        }
        
        if(condition.getAreaId()!=null){
        	sql.append(" and i.areaId="+condition.getAreaId());
        }
        if(condition.getLineId()!=null){
        	sql.append(" and i.lineId="+condition.getLineId());
        }
        if(condition.getOtherCompanyId()!=null){
        	sql.append(" AND (r.companyId="+condition.getOtherCompanyId()+" OR r.companyId IS NULL)");
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
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			
			rs = pst.executeQuery();
			List<CompanyMachinesVo> list = Lists.newArrayList();
			while (rs.next()) {
				CompanyMachinesVo bean = new CompanyMachinesVo();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCode(rs.getString("code"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setCreateTime(rs.getDate("create_time"));
				bean.setLocatoinName(rs.getString("locatoinName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
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
	public boolean isNew(String code) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id from replenish_company_machines "); 
        //sql.append(" WHERE companyId="+bean.getCompanyId());
        sql.append(" WHERE code='"+code+"'");
        
        if (showSql) {
			log.info(sql);
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<CompanyMachinesVo> list = Lists.newArrayList();
			while (rs.next()) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
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
	public ReplenishCompanyMachinesBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Long id) {
		ReplenishCompanyMachinesBean entity = new ReplenishCompanyMachinesBean();
		entity.setId(id);
		return super.del(entity);
	}

	public boolean update(ReplenishCompanyMachinesBean entity) {
		return super.update(entity);
	}

	public ReplenishCompanyMachinesBean insert(ReplenishCompanyMachinesBean entity) {
		
		String sql="select id from replenish_company_machines where code='"+entity.getCode()+"' and companyId="+entity.getCompanyId();
		log.info(sql);
		List<Map<String,Object>> list=super.list(sql, null);
		if(list.size()==0){
			return super.insert(entity);
		}else{
			return null;
		}
		 
	}

	public List<ReplenishCompanyMachinesBean> list(ReplenishCompanyMachinesCondition condition) {
		return null;
	}


	public List<ReplenishCompanyMachinesBean> listOtherCompanyForSelect() {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT cm.companyId,c.name AS companyName FROM replenish_company_machines cm LEFT JOIN company c ON cm.companyId=c.id ");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ReplenishCompanyMachinesBean> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ReplenishCompanyMachinesBean bean = new ReplenishCompanyMachinesBean();
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
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
	public Integer check(String code) {
		String sql=" select id from replenish_company_machines where code='"+code+"'";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer id=null;
		try {
			log.info(sql);
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				id=rs.getInt("id");
			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally {
			closeConnection(rs, pst, conn);
		}
		
	}
	@Override
	public boolean update(ReplenishCompanyMachinesBean bean, Connection conn) {
		return super.update(conn, bean);
	}
	@Override
	public boolean insert(ReplenishCompanyMachinesBean bean, Connection conn) {
		// TODO Auto-generated method stub
		return ((ReplenishCompanyMachinesBean) super.insert(conn, bean)).getId()==null?false:true;
	}
	@Override
	public boolean delete(long longValue, Connection conn) throws SQLException {
		String sql=" delete from replenish_company_machines where id="+longValue;
		PreparedStatement pst = null;
		pst=conn.prepareStatement(sql);
		boolean result=pst.execute();
		if(pst!=null) {
			pst.close();
		}
		return result;
	}

}
