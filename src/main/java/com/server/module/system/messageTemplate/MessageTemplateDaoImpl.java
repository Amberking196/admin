package com.server.module.system.messageTemplate;

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
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2019-01-07 11:21:04
 * 消息模板数据库访问类
 */
@Repository
public class MessageTemplateDaoImpl extends BaseDao<MessageTemplateBean> implements MessageTemplateDao {

	private static Logger log = LogManager.getLogger(MessageTemplateDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;
	/**
	 *
	 * 分页查询
	 * @param form 参数对象
	 * @return
	 */
	public ReturnDataUtil listPage(MessageTemplateForm form) {
		log.info("<MessageTemplateDaoImpl>-----<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select mt.id,mt.theme,mt.content,mt.state,mt.createTime,mt.createUser,mt.updateTime,mt.updateUser,mt.deleteFlag,mt.companyId,c.name  ");
		sql.append(" from message_template mt left join company c on mt.companyId=c.id where 1=1 and mt.deleteFlag=0 ");
		if(StringUtil.isNotBlank(form.getTheme())) {
			sql.append(" and theme like '%"+form.getTheme().trim()+"%' ");
		}
		if(form.getState()!=null) {
			sql.append(" and state= '"+form.getState()+"' ");
		}
		sql.append(" and mt.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" ");
		sql.append(" order by state asc,createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer num=0;
		log.info("查询短信模板信息SQL语句："+sql);
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			
			long off = (form.getCurrentPage() - 1) * form.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + form.getPageSize());

			rs = pst.executeQuery();
			List<MessageTemplateBean> list = Lists.newArrayList();
			while (rs.next()) {
				num++;
				MessageTemplateBean bean = new MessageTemplateBean();
				bean.setNum(num);
				bean.setId(rs.getLong("id"));
				bean.setTheme(rs.getString("theme"));
				bean.setContent(rs.getString("content"));
				bean.setState(rs.getInt("state"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCompanyName(rs.getString("name"));
				list.add(bean);
			}
			data.setCurrentPage(form.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MessageTemplateDaoImpl>-----<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<MessageTemplateDaoImpl>-----<listPage>-----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 删除方法
	 * @param id 消息模板id
	 * @return
	 */

	public boolean delete(Long id) {
		log.info("<MessageTemplateDaoImpl>-----<delete>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" update  message_template set deleteFlag=1 where  id="+id);
		Connection conn = null;
		PreparedStatement pst = null;
		boolean flag = false;
		log.info("删除短信模板信息SQL语句："+sql);
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			 int executeUpdate = pst.executeUpdate();
			if (executeUpdate>0) {
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(null, pst, conn);
		}
		log.info("<MessageTemplateDaoImpl>-----<delete>-----end");
		return flag;
	}
	/**
	 * 修改方法
	 * @param entity 消息模板对象
	 * @return
	 */
	public boolean update(MessageTemplateBean entity) {
		log.info("<MessageTemplateDaoImpl>-----<update>-----start");
		boolean update = super.update(entity);
		log.info("<MessageTemplateDaoImpl>-----<update>-----end");
		return update;
	}
	/**
	 * 添加方法
	 * @param entity 消息模板对象
	 * @return
	 */
	public MessageTemplateBean insert(MessageTemplateBean entity) {
		log.info("<MessageTemplateDaoImpl>------<insert>-----start");
		MessageTemplateBean bean = super.insert(entity);
		log.info("<MessageTemplateDaoImpl>------<insert>-----end");
		return bean;
	}

	
}
