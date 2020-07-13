package com.server.module.system.logsManager.messageLog;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.common.persistence.Page;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
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
 * author name: yjr create time: 2018-03-24 14:45:24
 */
@Repository
public class MessageLogDaoImpl extends BaseDao<MessageLogBean> implements MessageLogDao {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 	

	public Page listPage(Page page, String sql, List<Object> list) {
		return super.listPage(page, sql, list, MessageLogBean.class);
	}

	public List listEntity(String sql, List<Object> list) {
		return super.list(sql, list);
	}

	public MessageLogBean getEntity(Object id) {
		return super.get(id);
	}

	public boolean delEntity(MessageLogBean entity) {
		return super.del(entity);
	}

	public boolean updateEntity(MessageLogBean entity) {
		return super.update(entity);
	}

	@Autowired
	private CompanyDao companyDaoImpl;

	public ReturnDataUtil listPage(MessageLogCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,vmCode,payCode,sendTime,content from message_log m JOIN vending_machines_info vmi on m.vmCode=vmi.code where 1=1 ");
		Integer companyId =UserUtils.getUser().getCompanyId();
		if(companyId != null && companyId >1) {
			sql.append("and vmi.companyId="+companyId+"");
		}
		log.info(sql.toString()+"====");
		List<Object> plist = Lists.newArrayList();
		if (StringUtil.isNotBlank(condition.getVmCode())) {
			sql.append(" and vmCode like ?");
			plist.add("%"+condition.getVmCode()+"%");
		}
		if (StringUtil.isNotBlank(condition.getPayCode())) {
			sql.append(" and payCode like ?");
			plist.add("%"+condition.getPayCode()+"%");
		}
		if (StringUtil.isNotBlank(condition.getContent())) {
			sql.append(" and content like ?");
			plist.add("%"+condition.getContent()+"%");
		}
		sql.append(" ORDER BY sendTime DESC ");
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
			List<MessageLogBean> list = Lists.newArrayList();
			while (rs.next()) {
				MessageLogBean bean = new MessageLogBean();
				bean.setId(rs.getLong("id"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setPayCode(rs.getString("payCode"));
				bean.setSendTime(rs.getDate("sendTime"));
				bean.setContent(rs.getString("content"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setPageSize(condition.getPageSize());
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

}
