package com.server.module.customer.userInfo.userWxInfo;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.customer.CustomerUtil;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: why create time: 2018-11-15 15:05:14
 */
@Repository
public class TblCustomerWxDaoImpl extends BaseDao<TblCustomerWxBean> implements TblCustomerWxDao {

	private static Logger log = LogManager.getLogger(TblCustomerWxDaoImpl.class);

	@Autowired
	private UserUtils userUtils;
	
	public ReturnDataUtil listPage(TblCustomerWxForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,companyId,customerId,openId,nickname,sex,province,city,country,headimgurl,privilege,unionid,createTime,updateTime from tbl_customer_wx where 1=1 ");
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
			List<TblCustomerWxBean> list = Lists.newArrayList();
			while (rs.next()) {
				TblCustomerWxBean bean = new TblCustomerWxBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setOpenId(rs.getString("openId"));
				bean.setNickname(rs.getString("nickname"));
				bean.setSex(rs.getLong("sex"));
				bean.setProvince(rs.getString("province"));
				bean.setCity(rs.getString("city"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadimgurl(rs.getString("headimgurl"));
				bean.setPrivilege(rs.getString("privilege"));
				bean.setUnionid(rs.getString("unionid"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
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

	/**
	 * 查询微信用户信息
	 */
	public TblCustomerWxBean get(Long customerId) {
		log.info("<TblCustomerWxDaoImpl>------<get>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select tcw.id,tcw.customerId,tcw.openId,tcw.nickname,tcw.sex,tcw.province,tcw.city,tcw.country,tcw.headimgurl,tcw.privilege,  ");
		sql.append(" tcw.unionid,tcw.createTime,tcw.updateTime,tc.phone,tc.userBalance,tc.integral,vmi.companyId  from tbl_customer_wx  tcw left join  tbl_customer tc on tcw.customerId=tc.id ");
		sql.append(" left join vending_machines_info vmi on tc.vmCode=vmi.code and FIND_IN_SET(vmi.companyId,getChildList("+CompanyEnum.YOUSHUI.getIndex()+")) ");
		sql.append(" where 1=1 and tcw.customerId='"+customerId+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TblCustomerWxBean bean=null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new TblCustomerWxBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setOpenId(rs.getString("openId"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setSex(rs.getLong("sex"));
				bean.setProvince(rs.getString("province"));
				bean.setCity(rs.getString("city"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadimgurl(rs.getString("headimgurl"));
				bean.setPrivilege(rs.getString("privilege"));
				bean.setUnionid(rs.getString("unionid"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setPhone(rs.getString("phone"));
				bean.setUserBalance(rs.getBigDecimal("userBalance"));
				bean.setIntegral(rs.getInt("integral"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<TblCustomerWxDaoImpl>------<get>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerWxDaoImpl>------<get>-----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public boolean delete(Object id) {
		TblCustomerWxBean entity = new TblCustomerWxBean();
		return super.del(entity);
	}

	public boolean update(TblCustomerWxBean entity) {
		return super.update(entity);
	}

	public TblCustomerWxBean insert(TblCustomerWxBean entity) {
		return super.insert(entity);
	}

	public List<TblCustomerWxBean> list(TblCustomerWxForm condition) {
		return null;
	}

	@Override
	public String findCusteomerAddress() {
		log.info("<TblCustomerWxDaoImpl>------<findCusteomerAddress>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select  locatoinName from vending_machines_info where code =( select vmCode from tbl_customer where id="+CustomerUtil.getCustomerId()+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String addreess=null;
		log.info("查询用户注册机器地址sql："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				addreess=rs.getString("locatoinName");
			}
			
			log.info("<TblCustomerWxDaoImpl>------<findCusteomerAddress>-----end");
			return addreess;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerWxDaoImpl>------<findCusteomerAddress>-----end");
			return addreess;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	public List<TblCustomerWxBean> myInviteRewards() {
		log.info("<TblCustomerWxDaoImpl>----<myInviteRewards>----start");
		List<TblCustomerWxBean> list = Lists.newArrayList();
		Long customerId=userUtils.getSmsUser().getId();
		StringBuilder sql = new StringBuilder();
		sql.append(" select tcw.nickname,tc.phone,tc.createTime,tc.id from tbl_customer tc ");
		sql.append("left join tbl_customer_wx  tcw  on tc.id=tcw.customerId where tc.inviterId='"+customerId+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TblCustomerWxBean bean = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new TblCustomerWxBean();
				bean.setId(rs.getLong("id"));
				bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setPhone(rs.getString("phone"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				list.add(bean);
			}
			log.info("<TblCustomerWxDaoImpl>----<myInviteRewards>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerWxDaoImpl>----<myInviteRewards>----end");
			return list;
		} finally {
			this.closeConnection(rs,pst,conn);
		}
	}
}
