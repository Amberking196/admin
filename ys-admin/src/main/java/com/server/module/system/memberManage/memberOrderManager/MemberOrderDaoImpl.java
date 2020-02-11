package com.server.module.system.memberManage.memberOrderManager;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: why create time: 2018-09-26 14:36:43
 */
@Repository
public class MemberOrderDaoImpl extends BaseDao<MemberOrderBean> implements MemberOrderDao {

	private static Logger log = LogManager.getLogger(MemberOrderDaoImpl.class);

	/**
	 * 会员订单列表
	 */
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(MemberOrderForm memberOrderForm) {
		log.info("<MemberOrderDaoImpl>-----<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select mo.id,mo.customerId,tbl.phone,mo.price,mo.state,mo.ptCode,mo.payCode,mo.createTime,mo.payTime,mo.updateTime,c.name from member_order mo ");
		sql.append(" left join tbl_customer tbl on mo.customerId=tbl.id ");
		sql.append(" left join vending_machines_info vmi on tbl.vmCode=vmi.code left join company c on vmi.companyId=c.id where 1=1  ");
		if(StringUtil.isNotBlank(memberOrderForm.getPhone())) {
			sql.append(" and tbl.phone='"+memberOrderForm.getPhone().trim()+"' ");
		}
		if(memberOrderForm.getState()!=null) {
			sql.append(" and mo.state='"+memberOrderForm.getState()+"' ");
		}
		if (memberOrderForm.getStartTime() != null) {
			sql.append(" and mo.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(memberOrderForm.getStartTime()) + "' ");
		}
		if (memberOrderForm.getEndTime() != null) {
			sql.append(" and mo.createTime < '" + DateUtil.formatLocalYYYYMMDDHHMMSS(memberOrderForm.getEndTime(), 1) + "' ");
		}
		sql.append(" order by mo.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("会员订单列表sql语句::="+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(memberOrderForm.getIsShowAll()==0) {
				long off = (memberOrderForm.getCurrentPage() - 1) * memberOrderForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + memberOrderForm.getPageSize());
			}else {
				pst = conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<MemberOrderBean> list = Lists.newArrayList();
			while (rs.next()) {
				MemberOrderBean bean = new MemberOrderBean();
				bean.setId(rs.getLong("id"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setPhone(rs.getString("phone"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setState(rs.getLong("state"));
				bean.setPtCode(rs.getString("ptCode"));
				bean.setPayCode(rs.getString("payCode"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setPayTime(rs.getDate("payTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setCreateTimeLabel(rs.getTimestamp("createTime"));
				bean.setPayTimeLabel(rs.getTimestamp("payTime"));
				bean.setCompanyName(rs.getString("name"));
				if (bean.getState() == 10001) {
					bean.setStateName("已支付");
				}
				if (bean.getState() == 10002) {
					bean.setStateName("未支付");
				}
				if(bean.getState() == 10006){
					bean.setStateName("退款成功");
				}
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(memberOrderForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MemberOrderDaoImpl>-----<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MemberOrderDaoImpl>-----<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public MemberOrderBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		MemberOrderBean entity = new MemberOrderBean();
		return super.del(entity);
	}

	public boolean update(MemberOrderBean entity) {
		return super.update(entity);
	}

	/**
	 * 增加会员订单
	 */
	public MemberOrderBean insert(MemberOrderBean entity) {
		log.info("<MemberOrderDaoImpl>-----<insert>------start");
		MemberOrderBean memberOrderBean = super.insert(entity);
		log.info("<MemberOrderDaoImpl>-----<insert>------end");
		return memberOrderBean;
	}

	public List<MemberOrderBean> list(MemberOrderForm condition) {
		return null;
	}
	
	@Override
	public boolean paySuccessMemberOrder(String outTradeNo, String transactionId) {
		log.info("<MemberOrderDaoImpl>-----<paySuccessMemberOrder>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update member_order set ptCode='" + transactionId + "' ,payTime=current_timestamp(),state="
				+ PayStateEnum.PAY_SUCCESS.getState() + "  where payCode='" + outTradeNo + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("更新会员订单状态 sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<MemberOrderDaoImpl>-----<paySuccessMemberOrder>-----end");
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
	
	@Override
	public MemberOrderBean getMemberOrder(String payCode) {
		log.info("<MemberOrderDaoImpl>-----<getMemberOrder>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select mo.id,mo.customerId,mo.price,mo.state,mo.ptCode,mo.payCode,mo.createTime,mo.payTime,mo.updateTime,mo.friendCustomerId,mo.friendPhone from member_order mo ");
		sql.append(" where payCode= '"+payCode+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("获取订单信息sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			MemberOrderBean bean=null;
			while(rs.next()) {
				bean = new MemberOrderBean();
				bean.setId(rs.getLong("id"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setState(rs.getLong("state"));
				bean.setPtCode(rs.getString("ptCode"));
				bean.setPayCode(rs.getString("payCode"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setPayTime(rs.getDate("payTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setFriendCustomerId(rs.getLong("friendCustomerId"));
				bean.setFriendPhone(rs.getString("friendPhone"));
			}
			log.info("<MemberOrderDaoImpl>-----<getMemberOrder>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return null;
	}
	
	@Override
	public boolean updateCustomerBalance(MemberOrderBean entity) {
		log.info("<MemberOrderDaoImpl>-----<updateCustomerBalance>-----start");
		//得到充值金额  计算本次充值最终金额
		BigDecimal price = entity.getPrice();
		BigDecimal sumPrice=new BigDecimal(0);
		// 金额单次  50<=price<100
		if(price.compareTo(BigDecimal.valueOf(50)) >= 0 && price.compareTo(BigDecimal.valueOf(100)) < 0){
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.04)));
		// 金额单次  100<=price<200
		} else if (price.compareTo(BigDecimal.valueOf(100)) >= 0 && price.compareTo(BigDecimal.valueOf(200)) < 0) {
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.06)));
		// 金额单次  200<=price<500
		}else if(price.compareTo(BigDecimal.valueOf(200)) >= 0 && price.compareTo(BigDecimal.valueOf(500)) < 0){
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.08)));
		//金额单次  price>=500
		}else if(price.compareTo(BigDecimal.valueOf(500)) >= 0){
			sumPrice=price.add(price.multiply(BigDecimal.valueOf(0.1)));
		}else {
			sumPrice=price;
		}
		StringBuilder sql = new StringBuilder();
		if(StringUtil.isNotBlank(entity.getFriendPhone())){
			sql.append(" update  tbl_customer set userBalance=userBalance+"+sumPrice+" where id='"+entity.getFriendCustomerId()+"' ");
		}else{
			sql.append(" update  tbl_customer set userBalance=userBalance+"+sumPrice+" where id='"+entity.getCustomerId()+"' ");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("修改用户余额  sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<MemberOrderDaoImpl>-----<updateCustomerBalance>-----end");
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
}
