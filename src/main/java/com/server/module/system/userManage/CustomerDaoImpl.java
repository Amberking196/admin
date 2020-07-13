package com.server.module.system.userManage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;

@Repository("customerDao")
public class CustomerDaoImpl extends BaseDao implements CustomerDao{

	public static Logger log = LogManager.getLogger(CustomerDaoImpl.class);
	@Override
	public CustomerBean findCustomerById(Long id) {
		log.info("<AliCustomerDaoImpl--queryByAliId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.id,tcw.openId,tc.`type`,tc.alipayUserId,tc.phone,tc.nickname,tc.userName,");
		sql.append(" tc.sexId,tc.city,tc.province,tc.country,tc.headImgUrl,tc.latitude,tc.longitude,");
		sql.append(" tc.isCertified,tc.isStudentCertified,tc.userStatus,tc.userType,tc.createTime,");
		sql.append(" tc.createId,tc.updateTime,tc.lastUpdateId,tc.deleteFlag,tc.vmCode,tc.inviterId,tc.userBalance,tc.integral FROM tbl_customer as tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId");
		sql.append(" where tc.id = "+id);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerBean cus = null;
		log.info("sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cus = new CustomerBean();
				cus.setAlipayUserId(rs.getString("alipayUserId"));
				cus.setCity(rs.getString("city"));
				cus.setCountry(rs.getString("country"));
				cus.setCreateId(rs.getString("createId"));
				cus.setCreateTime(rs.getTimestamp("createTime"));
				cus.setDeleteFlag(rs.getInt("deleteFlag"));
				cus.setHeadImgUrl(rs.getString("headImgUrl"));
				cus.setId(rs.getLong("id"));
				cus.setIsCertified(rs.getString("isCertified"));
				cus.setIsStudentCertified(rs.getString("isStudentCertified"));
				cus.setLastUpdateId(rs.getString("lastUpdateId"));
				cus.setLatitude(rs.getDouble("latitude"));
				cus.setLongitude(rs.getDouble("longitude"));
				cus.setNickName(EmojiUtil.getEmoji(rs.getString("nickname")));
				cus.setOpenId(rs.getString("openId"));
				cus.setPhone(rs.getString("phone"));
				cus.setProvince(rs.getString("province"));
				cus.setSexId(rs.getInt("sexId"));
				cus.setType(rs.getInt("type"));
				cus.setUpdateTime(rs.getTimestamp("updateTime"));
				cus.setUserName(rs.getString("userName"));
				cus.setUserStatus(rs.getString("userStatus"));
				cus.setUserType(rs.getString("userType"));
				cus.setVmCode(rs.getString("vmCode"));
				cus.setInviterId(rs.getLong("inviterId"));
				cus.setUserBalance(rs.getBigDecimal("userBalance"));
				cus.setIntegral(rs.getInt("integral"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliCustomerDaoImpl--queryByAliId--end>");
		return cus;
	}
	@Override
	public ReturnDataUtil findCustomer(UserManagerForm userManagerForm) {
		log.info("<CustomerDaoImpl--findCustomer--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(" select tc.id,tc.UserName,tc.createTime,tc.city,tc.phone,tc.province,tc.vmCode,tc.locationName,tc.integral from tbl_customer as tc where tc.phone is not null");
		if(userManagerForm.getStartDate()!=null){
			sql.append(" and tc.createTime>='"+DateUtil.formatYYYYMMDDHHMMSS(userManagerForm.getStartDate())+"'");
		}
		if(userManagerForm.getEndDate()!=null){
			sql.append(" and tc.createTime<'"+DateUtil.formatLocalYYYYMMDDHHMMSS(userManagerForm.getEndDate(),1)+"'");
		}
		sql.append(" GROUP BY tc.id order by tc.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			long off = (userManagerForm.getCurrentPage() - 1) * userManagerForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + userManagerForm.getPageSize());
			rs = pst.executeQuery();
			List<CustomerBean> list = Lists.newArrayList();
			int id=0;
			while (rs.next()) {
				id++;
				CustomerBean customer = new CustomerBean();
				customer = new CustomerBean();
				customer.setId(rs.getLong("id"));
				customer.setUserName(rs.getString("UserName"));
				customer.setCreateTime(rs.getTimestamp("createTime"));
                customer.setCity(rs.getString("city"));
                customer.setProvince(rs.getString("province"));
                customer.setPhone(rs.getString("phone"));
                customer.setVmCode(rs.getString("vmCode"));
                customer.setLocationName(rs.getString("locationName"));
                customer.setIntegral(rs.getInt("integral"));
				list.add(customer);
			}
			data.setCurrentPage(userManagerForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<CustomerDaoImpl--findCustomer----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	@Override
	public ReturnDataUtil queryDetail(UserManagerForm userManagerForm) {
		log.info("<CustomerDaoImpl--queryDetail--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(" select p.vendingMachinesCode,sum(p.num) as quantity,p.itemName as name from tbl_customer t");
		sql.append(" inner join pay_record p on t.id = p.customerId");
		sql.append(" where t.id= '"+userManagerForm.getUserId()+"'");
				
		if(userManagerForm.getStartDate()!=null){
			sql.append(" and p.payTime>='"+DateUtil.formatYYYYMMDDHHMMSS(userManagerForm.getStartDate())+"'");
		}
		if(userManagerForm.getEndDate()!=null){
			sql.append(" and p.payTime<'"+DateUtil.formatLocalYYYYMMDDHHMMSS(userManagerForm.getEndDate(),1)+"'");
		}
		sql.append(" and p.state = '10001'");
		sql.append(" group by p.customerId,p.basicItemId,p.vendingMachinesCode order by vendingMachinesCode desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			long off = (userManagerForm.getCurrentPage() - 1) * userManagerForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + userManagerForm.getPageSize());
			rs = pst.executeQuery();
			List<CustomerDto> list = Lists.newArrayList();
			while (rs.next()) {
				CustomerDto customer = new CustomerDto();
                customer.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                customer.setItemName(rs.getString("name"));
                customer.setQuantity(rs.getInt("quantity"));
				list.add(customer);
			}
			data.setCurrentPage(userManagerForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<CustomerDaoImpl--queryDetail----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	@Override
	public CustomerBean getCustomerByPhone(String phone) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tc.id,tcw.openId,tc.type,tc.alipayUserId,tc.phone,");
		sql.append(" tc.nickname,tc.UserName,tc.sexId,tc.city,tc.province,tc.country,");
		sql.append(" tc.headImgUrl,tc.latitude,tc.longitude,tc.isCertified,tc.isStudentCertified,");
		sql.append(" tc.userStatus,tc.userType,tc.createTime,tc.createId,tc.updateTime,");
		sql.append(" tc.lastUpdateId,tc.deleteFlag FROM tbl_customer AS tc");
		sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId where 1=1 ");
		List<Object> plist = Lists.newArrayList();
		sql.append(" and tc.phone=?");
		plist.add(phone);
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerBean bean = new CustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setOpenId(rs.getString("openId"));
				bean.setType(rs.getInt("type"));
				bean.setAlipayUserId(rs.getString("alipayUserId"));
				bean.setPhone(rs.getString("phone"));
				bean.setUserName(rs.getString("UserName"));
				bean.setSexId(rs.getInt("sexId"));
				bean.setCity(rs.getString("city"));
				bean.setProvince(rs.getString("province"));
				bean.setCountry(rs.getString("country"));
				bean.setHeadImgUrl(rs.getString("headImgUrl"));
				bean.setLatitude(rs.getDouble("latitude"));
				bean.setLongitude(rs.getDouble("longitude"));
				bean.setIsCertified(rs.getString("isCertified"));
				bean.setIsStudentCertified(rs.getString("isStudentCertified"));
				bean.setUserStatus(rs.getString("userStatus"));
				bean.setUserType(rs.getString("userType"));
				bean.setCreateId(rs.getString("createId"));
				bean.setLastUpdateId(rs.getString("lastUpdateId"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				return bean;
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	
	@Override
	public ReturnDataUtil queryInvite(UserManagerForm userManagerForm) {
		log.info("<CustomerDaoImpl--queryInvite----start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from tbl_customer tc inner join ");
		sql.append(" (select inviterId,count(id) as num from tbl_customer where inviterId is NOT NULL ");
		sql.append(" group by inviterId) A on tc.id=A.inviterId ");
		sql.append(" left join (select tc.id,count(customerId) as realNum from tbl_customer tc ");
		sql.append(" inner join coupon_customer cc on tc.id=cc.customerId  ");
		sql.append("  and couponId=451");//限定推荐券
		sql.append(" group by tc.id ) B on tc.id=B.id ");
		sql.append(" where 1=1");
		if(StringUtils.isNotBlank(userManagerForm.getPhone())) {
			sql.append(" and  phone = "+userManagerForm.getPhone());
		}
		if(userManagerForm.getStartDate()!=null) {
			sql.append(" and  tc.createTime >='" + DateUtil.formatYYYYMMDDHHMMSS(userManagerForm.getStartDate()) + "'");
		}
		if(userManagerForm.getEndDate()!=null) {
			sql.append(" and  tc.createTime <'" + DateUtil.formatLocalYYYYMMDDHHMMSS(userManagerForm.getEndDate(), 1) + "'");
		}
		sql.append(" ORDER BY A.num desc ");
		List<CustomerBean> list = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countGroupSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (userManagerForm.getCurrentPage() - 1) * userManagerForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + userManagerForm.getPageSize());
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerBean bean = new CustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setPhone(rs.getString("phone"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setNum(rs.getInt("num"));
				bean.setRealNum(rs.getInt("realNum"));
				list.add(bean);
			}
			data.setCurrentPage(userManagerForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			if (showSql) {
				log.info(sql);
			}
			log.info("<CustomerDaoImpl--queryInvite----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	
	@Override
	public ReturnDataUtil queryInviteDetail(UserManagerForm userManagerForm) {
		log.info("<CustomerDaoImpl--queryInviteDetail----start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select  tc.id,phone,tc.createTime,pr.state as prState,so.state as soState from tbl_customer tc ");
		sql.append(" left join  pay_record pr on tc.id = pr.customerId ");
		sql.append(" left join  store_order so on tc.id=so.customerId ");
		sql.append(" where  1=1 ");
		sql.append(" and inviterId = " + userManagerForm.getInviterId() );
		sql.append(" and (pr.state!=10002 or pr.state is NULL) and (so.state!=10002 or so.state is NULL) ");
		sql.append(" group by tc.id ");
		
		List<CustomerBean> list = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countGroupSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (userManagerForm.getCurrentPage() - 1) * userManagerForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + userManagerForm.getPageSize());
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerBean bean = new CustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setPhone(rs.getString("phone"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				if(rs.getInt("prState")!=0 || rs.getInt("soState")!=0) {
					bean.setState("已消费");
				}else {
					bean.setState("未消费");
				}
				list.add(bean);
			}
			data.setCurrentPage(userManagerForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			if (showSql) {
				log.info(sql);
			}
			log.info("<CustomerDaoImpl--queryInviteDetail----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	
	@Override
	public List<CustomerVo> customerVo() {
		log.info("<CustomerDaoImpl--customerVoExport----start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT tc.vmCode,c.name,tc.phone,tc.createTime,tc.id,GROUP_CONCAT(payTime) as time FROM tbl_customer tc");
		sql.append(" inner join  pay_record pr on tc.id=pr.customerId and payTime is not null");
		sql.append(" inner join  vending_machines_info vmi on vmi.code=tc.vmCode ");
		sql.append(" inner join  company c on vmi.companyId=c.id and payTime is not null");

		sql.append(" where  tc.phone is not null");
		sql.append(" group by customerId");
		sql.append(" ORDER BY createTime");
		List<CustomerVo> list = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();

			while (rs.next()) {
				CustomerVo bean = new CustomerVo();
				bean.setPhone(rs.getString("phone"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setPayTime(rs.getString("time"));
				bean.setCompanyName(rs.getString("name"));
				bean.setVmCode(rs.getString("vmCode"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<CustomerDaoImpl--customerVoExport----end>");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	@Override
	public boolean updateCustomerBean(Long customerId, Long rewards,Integer type) {
		StringBuilder sql = new StringBuilder();
		if(type==1) {
			sql.append(" update tbl_customer set integral =integral+"+rewards+"  where  id = "+customerId+" ");
		}else {
			sql.append(" update tbl_customer set userBalance =userBalance+"+rewards+"  where  id = "+customerId+" ");
		}
		log.info("游戏中奖后 更新用户积分或者余额---"+sql);
		Connection conn = null;
		PreparedStatement pst = null;
		boolean flag=false;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
		   int executeUpdate = pst.executeUpdate();
		   if(executeUpdate>0) {
			   flag=true;
		   }
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return flag;
	}
	
	@Override
	public Integer isFirstBuy(Long customerId) {
		log.info("<CustomerDaoImpl>----<isFirstBuy>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as count from pay_record where customerId =" +customerId);
		sql.append(" and state!=10002 and state!=10008");
		log.info("顾客是否第一次购买sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CustomerDaoImpl>----<isFirstBuy>----end>");
		return 1;
	}
	
	@Override
	public Integer isStoreFirstBuy(Long customerId) {
		log.info("<CustomerDaoImpl>----<isStoreFirstBuy>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) as count from store_order where customerId =" +customerId);
		sql.append(" and state!=10002 ");
		log.info("顾客是否商城第一次购买sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CustomerDaoImpl>----<isStoreFirstBuy>----end>");
		return 1;
	}
	
	public String getCustomerLastVmcode(Long customerId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select vendingmachinescode from pay_record where customerId = "+customerId);
		sql.append(" order by createTime desc");
		sql.append(" limit 1");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String vmCode=null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				vmCode = rs.getString("vendingmachinescode");
			}
			if (showSql) {
				log.info(sql);
			}
			return vmCode;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return vmCode;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}