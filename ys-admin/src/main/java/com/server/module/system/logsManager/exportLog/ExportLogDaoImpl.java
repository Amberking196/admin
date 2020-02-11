package com.server.module.system.logsManager.exportLog;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: yjr create time: 2018-09-14 10:31:38
 */
@Repository
public class ExportLogDaoImpl extends BaseDao<ExportLogBean> implements ExportLogDao {

	private static Log log = LogFactory.getLog(ExportLogDaoImpl.class);
	@Autowired
	private CompanyDao companyManageDao;
	public ReturnDataUtil listPage(ExportLogCondition condition) {
		log.info("---------<ExportLogServiceImpl>--------<ExportLogDaoImpl>--------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select el.id,el.operator,el.operatorName,el.companyId,el.content,el.createTime,c.name from export_log el left join company c on el.companyId=c.id where 1=1 ");
		//添加查询条件
		if(condition.getCompanyId()!=null) {
			sql.append(" and el.companyId in("+condition.getCompanyId()+")");
		}else {
			sql.append(" and el.companyId in "+companyManageDao.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		}
		if(StringUtils.isNotBlank(condition.getOperatorName())) {
			sql.append(" and el.operatorName like '%"+condition.getOperatorName()+"%'");
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
			sql.append(" order by el.createTime desc ");
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<ExportLogBean> list = Lists.newArrayList();
			while (rs.next()) {
				ExportLogBean bean = new ExportLogBean();
				bean.setId(rs.getLong("id"));
				bean.setOperator(rs.getLong("operator"));
				bean.setOperatorName(rs.getString("operatorName"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setContent(rs.getString("content"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCompanyName(rs.getString("name"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(0);
			log.info("---------<ExportLogServiceImpl>--------<ExportLogDaoImpl>--------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("---------<ExportLogServiceImpl>--------<ExportLogDaoImpl>--------end");
			data.setStatus(-1);
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

	public ExportLogBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		ExportLogBean entity = new ExportLogBean();
		return super.del(entity);
	}

	public boolean update(ExportLogBean entity) {
		return super.update(entity);
	}

	public ExportLogBean insert(ExportLogBean entity) {
		return super.insert(entity);
	}

	public List<ExportLogBean> list(ExportLogCondition condition) {
		return null;
	}
}
