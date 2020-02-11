package com.server.module.system.purchase.purchaseApply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.mysql.jdbc.Statement;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 17:27:57
 */
@Repository
public class PurchaseApplyBillDaoImpl extends BaseDao<PurchaseApplyBillBean> implements PurchaseApplyBillDao {

	private static Log log = LogFactory.getLog(PurchaseApplyBillDaoImpl.class);
	@Autowired
	private CompanyDao companyManageDao;
	public ReturnDataUtil listPage(PurchaseApplyBillCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select pab.id,pab.warehouseId,pab.operator,pab.operatorName,pab.auditor,pab.auditorName,pab.number,pab.createTime,pab.state,pab.remark,pab.auditOpinion,pab.warehouseName,pab.deleteFlag,wi.companyId from purchase_apply_bill pab ");
		sql.append(" left join warehouse_info wi on pab.warehouseId = wi.id ");
		sql.append(" where 1=1  and deleteFlag=0");
		sql.append(" and  wi.companyId in "+companyManageDao.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if(condition.getState()!=null) {
			sql.append(" and pab.state= "+condition.getState());
		}
		if(StringUtils.isNotBlank(condition.getWarehouseName())) {
			sql.append(" and pab.warehouseName like '%"+condition.getWarehouseName()+"%' ");
		}
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
			sql.append(" order by pab.createTime desc");//排序
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<PurchaseApplyBillBean> list = Lists.newArrayList();
			while (rs.next()) {
				PurchaseApplyBillBean bean = new PurchaseApplyBillBean();
				bean.setId(rs.getLong("id"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setOperator(rs.getLong("operator"));
				bean.setOperatorName(rs.getString("operatorName"));
				bean.setAuditor(rs.getLong("auditor"));
				bean.setAuditorName(rs.getString("auditorName"));
				bean.setNumber(rs.getString("number"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setState(rs.getLong("state"));// '状态 0 未提交 1 已提交 2 未通过 3 已通过 ',
				bean.setRemark(rs.getString("remark"));
				bean.setAuditOpinion(rs.getString("auditOpinion"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
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

	public PurchaseApplyBillBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		PurchaseApplyBillBean entity = new PurchaseApplyBillBean();
		return super.del(entity);
	}

	public boolean update(PurchaseApplyBillBean entity) {
		return super.update(entity);
	}

	public PurchaseApplyBillBean insert(PurchaseApplyBillBean entity) {
		return super.insert(entity);
	}

	public List<PurchaseApplyBillBean> list(PurchaseApplyBillCondition condition) {
		return null;
	}

	@Override
	@SuppressWarnings("all")
	public ReturnDataUtil addPurchaseApply(PurchaseApplyBillBean bean, Connection conn) throws SQLException {
		log.info("<PurchaseApplyBillDaoImpl>------<addPurchaseApply>----start");
		// 对采购申请单进行添加，并返回自增id
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		PreparedStatement pst = null;
		ResultSet rs = null;
		sql.append(
				"insert into purchase_apply_bill (warehouseId,operator,operatorName,auditor,auditorName,number,state,auditOpinion,warehouseName,deleteFlag,remark) values(?,?,?,?,?,?,?,?,?,?,?)");
	
			pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			List plist = new ArrayList();
			plist.add(bean.getWarehouseId());
			plist.add(bean.getOperator());
			plist.add(bean.getOperatorName());
			plist.add(bean.getAuditor());
			plist.add(bean.getAuditorName());
			plist.add(bean.getNumber());
			plist.add(bean.getState());
			plist.add(bean.getAuditOpinion());
			plist.add(bean.getWarehouseName());
			plist.add(bean.getDeleteFlag());
			plist.add(bean.getRemark());
			for (int i = 0; i < plist.size(); i++) {
				pst.setObject(i + 1, plist.get(i));
			}
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			int id = 0;
			if (rs.next())
				id = rs.getInt(1);
			data.setStatus(0);
			data.setReturnObject(id);
		log.info("<PurchaseApplyBillDaoImpl>------<addPurchaseApply>----start");
		return data;
	}

	@Override
	public ReturnDataUtil submitPurchaseApply(Integer id) {
		log.info("<PurchaseApplyBillDaoImpl>------<submitPurchaseApply>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("update purchase_apply_bill set state=1 where id=" + id);

		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			// 执行更新
			int flag = pst.executeUpdate();
			if (flag != 0) {// 修改成功
				data.setStatus(0);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseApplyBillDaoImpl>------<submitPurchaseApply>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
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

	@Override
	public ReturnDataUtil delete(Integer id) {
		log.info("<PurchaseApplyBillDaoImpl>------<delete>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("update purchase_apply_bill set deleteFlag=1 where id=" + id);
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			// 执行更新
			int flag = pst.executeUpdate();
			if (flag != 0) {// 修改成功
				data.setStatus(0);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseApplyBillDaoImpl>------<delete>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			data.setStatus(-1);
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

	@Override
	public ReturnDataUtil checkFalse(PurchaseApplyBillBean bean) {
		log.info("<PurchaseApplyBillDaoImpl>------<checkFalse>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("update purchase_apply_bill set state=2,auditor=?,auditorName=? where id=" + bean.getId());
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			List plist=Lists.newArrayList();
			plist.add(bean.getAuditor());
			plist.add(bean.getAuditorName());
			for(int i=0;i<plist.size();i++) {
				pst.setObject(i+1, plist.get(i));
			}
			// 执行更新
			int flag = pst.executeUpdate();
			if (flag != 0) {// 修改成功
				data.setStatus(0);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseApplyBillDaoImpl>------<checkFalse>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			data.setStatus(-1);
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

	@Override
	@SuppressWarnings(value="all")
	public void update(PurchaseApplyAndItemBean bean, Connection conn) throws SQLException {
		log.info("<PurchaseApplyBillDaoImpl>------<update>----start");
		//更新采购单的信息
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		PreparedStatement pst = null;
		sql.append("update purchase_apply_bill set state=3,auditor=?,auditorName=? where id=?");
			pst = conn.prepareStatement(sql.toString());
			List plist = new ArrayList();
			plist.add(bean.getAuditor());
			plist.add(bean.getAuditorName());
			plist.add(bean.getId());
			for (int i = 0; i < plist.size(); i++) {
				pst.setObject(i + 1, plist.get(i));
			}
			pst.executeUpdate();
		log.info("<PurchaseApplyBillDaoImpl>------<update>----end");
		
	}

	@Override
	public PurchaseApplyBillBean getBeanById(Integer id) {
		log.info("<PurchaseApplyBillDaoImpl>------<getBeanById>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,warehouseId,operator,operatorName,auditor,auditorName,number,createTime,state,remark,auditOpinion,warehouseName,deleteFlag from purchase_apply_bill where 1=1  "
						+ "   and id ="+id);
		List<Object> plist = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		PurchaseApplyBillBean bean = new PurchaseApplyBillBean();
		try {
			conn = openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean.setId(rs.getLong("id"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setOperator(rs.getLong("operator"));
				bean.setOperatorName(rs.getString("operatorName"));
				bean.setAuditor(rs.getLong("auditor"));
				bean.setAuditorName(rs.getString("auditorName"));
				bean.setNumber(rs.getString("number"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setState(rs.getLong("state"));// '状态 0 未提交 1 已提交 2 未通过 3 已通过 ',
				bean.setRemark(rs.getString("remark"));
				bean.setAuditOpinion(rs.getString("auditOpinion"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseApplyBillDaoImpl>------<getBeanById>----end");
			return bean ;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseApplyBillDaoImpl>------<getBeanById>----end");
			return bean;
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

}
