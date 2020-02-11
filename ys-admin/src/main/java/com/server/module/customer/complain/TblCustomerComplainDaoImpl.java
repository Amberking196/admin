package com.server.module.customer.complain;

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
import com.server.module.customer.CustomerUtil;
import com.server.module.customer.complain.reply.TblCustomerComplainReplyBean;
import com.server.module.customer.complain.reply.TblCustomerComplainReplyDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-08-17 08:48:16
 */
@Repository
public class TblCustomerComplainDaoImpl extends BaseDao<TblCustomerComplainBean> implements TblCustomerComplainDao {

	private static Logger log = LogManager.getLogger(TblCustomerComplainDaoImpl.class);

	@Autowired
	private TblCustomerComplainReplyDao tblCustomerComplainReplyDaoImpl;
	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * 用户投诉列表
	 */
	public ReturnDataUtil listPage(TblCustomerComplainForm tblCustomerComplainForm) {
		log.info("<TblCustomerComplainDaoImpl>----<listPage>-------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select tcc.id,tcc.vmCode,tcc.phone,tcc.content,tcc.picName,tcc.state,tcc.type,tcc.createTime,tcc.createUser,");
		sql.append(" tcc.updateTime,tcc.updateUser,tcc.deleteFlag,tcc.customerId,vmi.machineVersion  from tbl_customer_complain tcc ");
		sql.append(" left join vending_machines_info vmi on tcc.vmCode=vmi.code left join tbl_customer tc on tcc.customerId= tc.id  ");
		sql.append(" left join vending_machines_info vmm on vmm.`code`=tc.vmCode where tcc.deleteFlag=0 ");
		sql.append(" and vmm.companyId in  "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		if (StringUtil.isNotBlank(tblCustomerComplainForm.getVmCode())) {
			sql.append(" and tcc.vmCode = '" + tblCustomerComplainForm.getVmCode().trim() + "' ");
		}
		if (StringUtil.isNotBlank(tblCustomerComplainForm.getPhone())) {
			sql.append(" and tcc.phone = '" + tblCustomerComplainForm.getPhone().trim() + "' ");
		}
		if(tblCustomerComplainForm.getType()!=null) {
			sql.append(" and tcc.type = '" + tblCustomerComplainForm.getType() + "' ");
		}
		if(tblCustomerComplainForm.getState()!=null) {
			sql.append(" and tcc.state = '" + tblCustomerComplainForm.getState() + "' ");
		}
		if (tblCustomerComplainForm.getStartTime() != null) {
			sql.append(" and tcc.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(tblCustomerComplainForm.getStartTime()) + "' ");
		}
		if (tblCustomerComplainForm.getEndTime() != null) {
			sql.append(" and tcc.createTime < '" + DateUtil.formatLocalYYYYMMDDHHMMSS(tblCustomerComplainForm.getEndTime(), 1) + "' ");
		}
		sql.append(" order by tcc.state asc,tcc.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("故障申报查询列表 SQL:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(tblCustomerComplainForm.getIsShowAll()==0) {
				long off = (tblCustomerComplainForm.getCurrentPage() - 1) * tblCustomerComplainForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + tblCustomerComplainForm.getPageSize());
			}else {
				pst = conn.prepareStatement(sql.toString());
			}

			rs = pst.executeQuery();
			List<TblCustomerComplainBean> list = Lists.newArrayList();
			int num=0;
			while (rs.next()) {
				num++;
				TblCustomerComplainBean bean = new TblCustomerComplainBean();
				bean.setNum(num);
				bean.setId(rs.getLong("id"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setPhone(rs.getString("phone"));
				bean.setContent(EmojiUtil.getString(rs.getString("content")));
				bean.setPicName(rs.getString("picName"));
				bean.setType(rs.getInt("type"));
				bean.setState(rs.getInt("state"));
				String createTime = rs.getString("createTime");
				String substring = createTime.substring(0, createTime.indexOf("."));
				bean.setCreateTimes(substring);
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setMachineVersion(rs.getInt("machineVersion"));
				bean.getTypeLabel();
				bean.getStateLabel();
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(tblCustomerComplainForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<TblCustomerComplainDaoImpl>----<listPage>-------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerComplainDaoImpl>----<listPage>-------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public TblCustomerComplainBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		TblCustomerComplainBean entity = new TblCustomerComplainBean();
		return super.del(entity);
	}

	public boolean update(TblCustomerComplainBean entity) {
		return super.update(entity);
	}

	/**
	 * 用户留言增加
	 */
	public TblCustomerComplainBean insert(TblCustomerComplainBean entity) {
		log.info("<CustomerMessageDaoImpl>----<listPage>-------start");
		TblCustomerComplainBean tblCustomerComplainBean = super.insert(entity);
		log.info("<CustomerMessageDaoImpl>----<listPage>-------end");
		return tblCustomerComplainBean;
	}

	

	/**
	 *我的故障申报
	 */
	public List<TblCustomerComplainBean> myDeclaration(TblCustomerComplainForm tblCustomerComplainForm) {
		log.info("<TblCustomerComplainDaoImpl>----<myDeclaration>-------start");
		StringBuilder sql = new StringBuilder();
		List<TblCustomerComplainBean> list = Lists.newArrayList();
		Long customerId =CustomerUtil.getCustomerId();
		if(customerId==null) {
			customerId=userUtils.getSmsUser().getId();
		}
		sql.append(" select tcc.id,tcc.customerId,tcc.phone,tcc.content,tcc.picName,tcc.state,tcc.type,tcc.createTime,tcw.nickname from tbl_customer_complain  tcc ");
		sql.append("  left join tbl_customer_wx tcw on tcc.customerId=tcw.customerId where tcc.deleteFlag=0 and tcc.customerId='"+customerId+"' ");
		//state  故障申报回复状态  0 未回复 1 已回复
		if (tblCustomerComplainForm.getState()!=null) {
			sql.append(" and tcc.state='"+tblCustomerComplainForm.getState()+"'");
		}
		sql.append(" order by tcc.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		TblCustomerComplainBean bean = null;
		log.info("我的故障申报 SQL:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new TblCustomerComplainBean();
				long id = rs.getLong("id");
				bean.setId(id);
				bean.setPhone(rs.getString("phone"));
				bean.setContent(EmojiUtil.getString(rs.getString("content")));
				bean.setPicName(rs.getString("picName"));
				bean.setState(rs.getInt("state"));
				bean.setType(rs.getInt("type"));
				bean.setCreateTimes(rs.getString("createTime").substring(0, rs.getString("createTime").indexOf(".")));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setNickName(rs.getString("nickname"));
				//查询回复信息
				List<TblCustomerComplainReplyBean> list1 = tblCustomerComplainReplyDaoImpl.listReplyBean(id);
				bean.setListReply(list1);
				list.add(bean);
			}
			log.info("<TblCustomerComplainDaoImpl>----<listPage>-------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerComplainDaoImpl>----<listPage>-------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	public  Integer findComplaintsNumberById(){
		log.info("<TblCustomerComplainDaoImpl>-----<findComplaintsNumberById>-----start");
		StringBuilder sql = new StringBuilder();
		Long customerId = userUtils.getSmsUser().getId();
		sql.append(" select *  from  tbl_customer_complain where date(createTime) = curdate() and customerId='"+customerId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("用户每天申诉次数sql语句："+sql.toString());
		int number=0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs=pst.executeQuery();
			while (rs.next())  {
				number++;
			}
			log.info("<TblCustomerComplainDaoImpl>-----<findComplaintsNumberById>-----end");
			return number;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<TblCustomerComplainDaoImpl>-----<findComplaintsNumberById>-----end");
		return number;
	}
}
