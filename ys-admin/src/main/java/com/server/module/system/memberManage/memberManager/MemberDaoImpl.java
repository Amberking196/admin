package com.server.module.system.memberManage.memberManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.CustomerUtil;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;

@Repository
public class MemberDaoImpl extends BaseDao<MemberBean> implements MemberDao {

	private static Logger log = LogManager.getLogger(MemberDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;

	/**
	 * 用户信息列表
	 */
	@SuppressWarnings("resource")
	@Override
	public ReturnDataUtil listPage(MemberForm memberForm) {
		log.info("<MemberDaoImpl>-----<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.name as companyName,t.id customerId,t.phone,t.vmCode,t.userBalance,t.follow,t.isSend,t.createTime,ca.state  ");
		sql.append(" from tbl_customer t left join customer_analyze ca on t.id=ca.customerId left join vending_machines_info vmi on vmi.code = t.vmCode left join company  c on c.id=vmi.companyId where 1=1 ");
		if (StringUtil.isNotBlank(memberForm.getPhone())) {
			sql.append(" and t.phone ='" + memberForm.getPhone().trim() + "' ");
		}
		if(memberForm.getState()!=null) {
			sql.append(" and ca.state="+memberForm.getState()+" ");
		}
		if(memberForm.getFollow()!=null) {
			sql.append(" and t.follow="+memberForm.getFollow()+" ");
		}
		if(memberForm.getIsSend()!=null) {
			sql.append(" and t.isSend="+memberForm.getIsSend()+" ");
		} 
		if (StringUtils.isNotBlank(memberForm.getCompanyIds())) {
			//sql.append(" FIND_IN_SET("+memberForm.getCompanyId()+",getChildList(vmi.companyId)) ");
			sql.append(" and vmi.companyId in(" + memberForm.getCompanyIds() + ")");
		}
		sql.append(" order by t.updateTime desc "); 
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("用户信息列表查询 SQL语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(memberForm.getIsShowAll()==1) {
				pst = conn.prepareStatement(sql.toString());
			}else {
				long off = (memberForm.getCurrentPage() - 1) * memberForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + memberForm.getPageSize());
			}
			rs = pst.executeQuery();
			List<MemberBean> list = Lists.newArrayList();
			while (rs.next()) {
				MemberBean bean = new MemberBean();
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setPhone(rs.getString("phone"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setState(rs.getInt("state"));
				bean.setFollow(rs.getInt("follow"));
				bean.setIsSend(rs.getInt("isSend"));
				bean.setUserBalance(rs.getDouble("userBalance"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCompanyName(rs.getString("companyName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(memberForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MemberDaoImpl>-----<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MemberDaoImpl>-----<listPage>-----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 增加会员
	 */
	public boolean add(MemberBean entity) {
		log.info("<MemberDaoImpl>-------<add>------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  tbl_customer set isMember= 1 ,startTime='"+ entity.getStartTime() + "',endTime='" + entity.getEndTime() 
		+ "'  where id='" + entity.getCustomerId()
				+ "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("增加会员  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<MemberDaoImpl>-----<add>-----end");
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

	/**
	 * 查询是否优水平台用户
	 */
	public List<MemberBean> getBean(String phone) {
		log.info("<MemberDaoImpl>-----<getBean>-----start");
		List<MemberBean> list = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append(" select id,phone,isMember,startTime,endTime,userBalance from tbl_customer tbl left join vending_machines_info  vmi on tbl.vmCode=vmi.code  ");
		//目前会员只给优水到家使用 
		sql.append(" where vmi.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(CompanyEnum.YOUSHUIDAOJIA.getIndex()));
		sql.append(" and tbl.phone='" + phone + "'"); 
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询是否优水平台用户  SQL语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				MemberBean bean = new MemberBean();
				bean.setCustomerId(rs.getLong("id"));
				bean.setPhone(rs.getString("phone"));
				bean.setIsMember(rs.getInt("isMember"));
				if(rs.getString("startTime")!=null) {
					bean.setStartTimeLabel(rs.getTimestamp("startTime"));
				}
				if(rs.getString("endTime")!=null) {
					bean.setEndTimeLabel(rs.getTimestamp("endTime"));
				}
				bean.setUserBalance(rs.getDouble("userBalance"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<MemberDaoImpl>-----<getBean>-----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MemberDaoImpl>-----<getBean>-----end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 修改会员类型
	 */
	public boolean update(MemberBean entity) {
		log.info("<MemberDaoImpl>-------<update>------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  tbl_customer set memberTypeId=" + entity.getMemberTypeId() + ",startTime='"
				+ entity.getStartTime() + "',endTime='" + entity.getEndTime() + "'  where phone='" + entity.getPhone()
				+ "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("修改会员类型  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<MemberDaoImpl>-----<update>-----end");
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

	/**
	 * 删除会员
	 */
	public boolean deleteMember(String phone) {
		log.info("<MemberDaoImpl>-------<delete>------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  tbl_customer set isMember=0 ,startTime=null,endTime=null where phone='" + phone + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除会员  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<MemberDaoImpl>-----<delete>-----end");
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

	/**
	 * 判断是否是会员
	 */
	@Override
	public ReturnDataUtil judgeMember() {
		log.info("<MemberDaoImpl>-------<judgeMember>------start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		// 得到当前时间
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sf.format(new Date());
		// 得到商城用户id
		Long customerId = CustomerUtil.getCustomerId();
		log.info("(MemberDaoImpl)用户id==========" + customerId);
		StringBuilder sql = new StringBuilder();
		sql.append(" select  id,isMember,endTime from  tbl_customer tbl left join vending_machines_info  vmi on tbl.vmCode=vmi.code  ");
		//控制 在优水机器上面注册的用户才能参与会员
		sql.append(" where id='" + customerId + "'  and vmi.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(CompanyEnum.YOUSHUIDAOJIA.getIndex()));
		//sql.append(" or vmi.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(CompanyEnum.WUHANYOUSHUI.getIndex()));
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("判断是否可以参与会员  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			if (rs.next() && rs != null) {
				/*if (rs.getInt("isMember") == 0) { // 不是会员
					returnDataUtil.setStatus(-99);
					returnDataUtil.setMessage("非会员");
				} else { // 是会员 判断是否过期
					String endTime = rs.getString("endTime");
					String substring = endTime.substring(0, endTime.lastIndexOf("."));
					if (substring.compareTo(now) < 0) { // 已过期
						returnDataUtil.setStatus(-99);
						returnDataUtil.setMessage("会员已过期");
					}else {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("会员");
					}
				}*/
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("可以参与会员");
			}else {
				returnDataUtil.setStatus(-99);
				returnDataUtil.setMessage("非优水用户，不可以参与会员");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<MemberDaoImpl>-------<judgeMember>------end");
		return returnDataUtil;
	}
	
	

	/**
	 * 得到会员信息
	 */
	@Override
	public MemberBean findBean(Long id) {
		log.info("<MemberDaoImpl>-----<findBean>-----start");
		StringBuilder sql = new StringBuilder();
		MemberBean bean = null;
		sql.append(" select id,phone,startTime,endTime,userBalance,integral from tbl_customer where id='" + id + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询会员信息 SQL语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new MemberBean();
				bean.setCustomerId(rs.getLong("id"));
				bean.setPhone(rs.getString("phone"));
				if(rs.getString("startTime")!=null) {
					bean.setStartTimeLabel(rs.getTimestamp("startTime"));
				}
				if(rs.getString("endTime")!=null) {
					bean.setEndTimeLabel(rs.getTimestamp("endTime"));
				}
				bean.setUserBalance(rs.getDouble("userBalance"));
				bean.setIntegral(rs.getLong("integral"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<MemberDaoImpl>-----<findBean>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MemberDaoImpl>-----<findBean>-----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 更新用户余额
	 */
	@Override
	public boolean updateUserBalance(Long customerId, BigDecimal money) {
		log.info("<MemberDaoImpl>-------<updateUserBalance>------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  tbl_customer set userBalance=userBalance-"+money+" where id='" + customerId + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("更新用户余额  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<MemberDaoImpl>-----<updateUserBalance>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<MemberDaoImpl>-----<updateUserBalance>-----end");
		return false;
	}
	
	public Long insertMemberUseLog(MemberUseLog memberLog) {
		log.info("<MemberDaoImpl----<insertMemberUseLog>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO member_use_log(customerId,orderId,useMoney,createTime,orderType)");
		sql.append(" VALUES(?,?,?,NOW(),?)");
		List<Object> param = new ArrayList<Object>();
		param.add(memberLog.getCustomerId());
		param.add(memberLog.getOrderId());
		param.add(memberLog.getUseMoney());
		param.add(memberLog.getOrderType());
		int id = insertGetID(sql.toString(), param);
		log.info("<MemberDaoImpl----<insertMemberUseLog>----end>");
		if(id>0){
			return Long.valueOf(id);
		}
		return null;
	}

	@Override
	public boolean updateIntegral(Long customerId, Long integral) {
		log.info("<MemberDaoImpl>-------<updateIntegral>------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  tbl_customer set integral=integral-"+integral+" where id="+customerId);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("修改用户积分  sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<MemberDaoImpl>-----<updateIntegral>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<MemberDaoImpl>-----<updateIntegral>-----end");
		return false;
	}
}
