package com.server.module.system.purchase.purchaseBill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.mysql.jdbc.Statement;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: yjr create time: 2018-09-03 16:23:53
 */
@Repository
public class PurchaseBillDaoImpl extends BaseDao<PurchaseBillBean> implements PurchaseBillDao {

	private static Logger log = LogManager.getLogger(PurchaseBillDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	/**
	 * 采购单列表查询
	 */
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(PurchaseBillForm purchaseBillForm) {
		log.info("<PurchaseBillDaoImpl>----<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select pb.id,pb.applyId,pb.applyNumber,pb.warehouseId,pb.auditor,pb.auditorName,pb.number,pb.createTime,pb.state,pb.storageState,pb.remark,pb.auditOpinion,pb.warehouseName,pb.operator,pb.operatorName ");
		sql.append(" from purchase_bill pb where 1=1 ");
		if(StringUtil.isNotBlank(purchaseBillForm.getNumber())) {
			sql.append(" and pb.number like '%"+purchaseBillForm.getNumber()+"%' ");
		}
		if(purchaseBillForm.getWarehouseId()!=null) {
			sql.append(" and pb.warehouseId ='"+purchaseBillForm.getWarehouseId()+"' ");
		}
		sql.append(" and pb.warehouseId in ( select id from warehouse_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") ");
		sql.append(" order by pb.createTime desc ");
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
			long off = (purchaseBillForm.getCurrentPage() - 1) * purchaseBillForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + purchaseBillForm.getPageSize());
			rs = pst.executeQuery();
			List<PurchaseBillBean> list = Lists.newArrayList();
			Integer num=0;
			while (rs.next()) {
				num++;
				PurchaseBillBean bean = new PurchaseBillBean();
				bean.setNum(num);
				bean.setId(rs.getLong("id"));
				bean.setApplyId(rs.getLong("applyId"));
				bean.setApplyNumber(rs.getString("applyNumber"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setAuditor(rs.getLong("auditor"));
				bean.setAuditorName(rs.getString("auditorName"));
				bean.setNumber(rs.getString("number"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setState(rs.getLong("state"));
				bean.setStorageState(rs.getInt("storageState"));
				bean.setRemark(rs.getString("remark"));
				bean.setAuditOpinion(rs.getString("auditOpinion"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setOperator(rs.getLong("operator"));
				bean.setOperatorName(rs.getString("operatorName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(purchaseBillForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<PurchaseBillDaoImpl>----<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseBillDaoImpl>----<listPage>------end");
			return data;
		} finally {
				this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 根据采购单号 查询 采购单的商品
	 */
	public PurchaseBillBean getItemBean(String number) {
		log.info("<PurchaseBillDaoImpl>----<get>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		PurchaseBillBean bean = null;
		sql.append("select pb.id,pb.applyId,pb.applyNumber,pb.warehouseId,pb.auditor,pb.auditorName,pb.number,pb.createTime,pb.state,pb.storageState,pb.remark,pb.auditOpinion,pb.warehouseName,pb.operator,pb.operatorName ");
		sql.append("  from   purchase_bill pb  where pb.number='"+number+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean =new PurchaseBillBean();
				bean.setId(rs.getLong("id"));
				bean.setApplyId(rs.getLong("applyId"));
				bean.setApplyNumber(rs.getString("applyNumber"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setAuditor(rs.getLong("auditor"));
				bean.setAuditorName(rs.getString("auditorName"));
				bean.setNumber(rs.getString("number"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setState(rs.getLong("state"));
				bean.setStorageState(rs.getInt("storageState"));
				bean.setRemark(rs.getString("remark"));
				bean.setAuditOpinion(rs.getString("auditOpinion"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setOperator(rs.getLong("operator"));
				bean.setOperatorName(rs.getString("operatorName"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseBillDaoImpl>----<get>------end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseBillDaoImpl>----<get>------end");
			return bean;
		} finally {
				this.closeConnection(rs, pst, conn);
		}
	}
	/**
	 * 根据采购单号 查询 采购单的商品
	 */
	public List<PurchaseBillBean> getItemBean(Integer id) {
		log.info("<PurchaseBillDaoImpl>----<get>------start");
		
		StringBuilder sql = new StringBuilder();
		PurchaseBillBean bean = null;
		sql.append("select pb.id,pb.applyId,pb.applyNumber,pb.warehouseId,pb.auditor,pb.auditorName,pb.number,pb.createTime,pb.state,pb.storageState,pb.remark,pb.auditOpinion,pb.warehouseName,pb.operatorName ");
		sql.append("  from   purchase_bill pb  where pb.applyId= "+id);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<PurchaseBillBean>list=new ArrayList<PurchaseBillBean>();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				bean =new PurchaseBillBean();
				bean.setId(rs.getLong("id"));
				bean.setApplyId(rs.getLong("applyId"));
				bean.setApplyNumber(rs.getString("applyNumber"));
				bean.setWarehouseId(rs.getLong("warehouseId"));
				bean.setAuditor(rs.getLong("auditor"));
				bean.setAuditorName(rs.getString("auditorName"));
				bean.setNumber(rs.getString("number"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setState(rs.getLong("state"));
				bean.setStorageState(rs.getInt("storageState"));
				bean.setRemark(rs.getString("remark"));
				bean.setAuditOpinion(rs.getString("auditOpinion"));
				bean.setWarehouseName(rs.getString("warehouseName"));
				bean.setOperatorName(rs.getString("operatorName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseBillDaoImpl>----<get>------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseBillDaoImpl>----<get>------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	
	public PurchaseBillBean get(Object id) {
		return super.get(id);
	}

	/**
	 * 删除采购单
	 */
	public boolean delete(Object id) {
		log.info("<PurchaseBillDaoImpl>----<delete>------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  purchase_bill set deleteFlag= 1 where id in ("+id+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除采购单sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<PurchaseBillDaoImpl>-----<delete>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}

	public boolean update(PurchaseBillBean entity) {
		log.info("<PurchaseBillDaoImpl>------<update>----start");
		boolean update = super.update(entity);
		log.info("<PurchaseBillDaoImpl>------<update>----end");
		return update;
	}
	/**
	 * 事务控制
	 */
	public boolean updateTransaction(Connection conn,PurchaseBillBean entity) {
		log.info("<PurchaseBillDaoImpl>------<update>----start");
		boolean update = super.update(conn,entity);
		log.info("<PurchaseBillDaoImpl>------<update>----end");
		return update;
	}

	public PurchaseBillBean insert(PurchaseBillBean entity) {
		return super.insert(entity);
	}

	public List<PurchaseBillBean> list(PurchaseBillForm condition) {
		return null;
	}

	@Override
	public ReturnDataUtil addPurchaseBill(PurchaseBillBean purchaseBean, Connection conn) throws SQLException {
		log.info("<PurchaseBillDaoImpl>------<addPurchaseBill>----start");
		// 对采购申请单进行添加，并返回自增id
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		PreparedStatement pst = null;
		ResultSet rs = null;
		sql.append(
				"insert into purchase_bill (applyId,applyNumber,warehouseId,auditor,auditorName,number,state,storageState,remark,auditOpinion,warehouseName,operator,operatorName) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			List plist = new ArrayList();
			plist.add(purchaseBean.getApplyId());
			plist.add(purchaseBean.getApplyNumber());
			plist.add(purchaseBean.getWarehouseId());
			plist.add(purchaseBean.getAuditor());
			plist.add(purchaseBean.getAuditorName());
			plist.add(purchaseBean.getNumber());
			plist.add(purchaseBean.getState());
			plist.add(purchaseBean.getStorageState());
			plist.add(purchaseBean.getRemark());
			plist.add(purchaseBean.getAuditOpinion());
			plist.add(purchaseBean.getWarehouseName());
			plist.add(purchaseBean.getOperator());
			plist.add(purchaseBean.getOperatorName());
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
		log.info("<PurchaseBillDaoImpl>------<addPurchaseBill>----start");
		return data;
	}

	@Override
	public boolean checkOnlyOne(String newStr) {
		log.info("<PurchaseBillDaoImpl>------<checkOnlyOne>----start");
		StringBuilder sql = new StringBuilder();
		PurchaseBillBean bean = null;
		sql.append(" select id,applyId,applyNumber,warehouseId,auditor,auditorName,number from purchase_bill where number='"+newStr+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean falg=false;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			if (rs.next()) {//说明查询有结果，该单号已经存在
				falg=true;
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<PurchaseBillDaoImpl>------<checkOnlyOne>----end");
			return falg;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PurchaseBillDaoImpl>------<checkOnlyOne>----end");
			return falg;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}
