package com.server.module.system.officialManage.officialArticle;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
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
 * author name: why create time: 2018-08-01 15:07:48
 */
@Repository
public class OfficialArticleDaoImpl extends BaseDao<OfficialArticleBean> implements OfficialArticleDao {

	private static Logger log = LogManager.getLogger(OfficialArticleDaoImpl.class);

	/**
	 * 文章列表查询
	 */
	public ReturnDataUtil listPage(OfficialArticleForm officialArticleForm) {
		log.info("<OfficialArticleDaoImpl>----<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select oa.id,sid,title,author,remark,content,num,level,oa.url,oa.createTime,oa.updateTime,oa.createUser,oa.updateUser,oa.deleteFlag,os.name ");
		sql.append(" from official_article  oa LEFT JOIN official_section os on oa.sid=os.id where oa.deleteFlag=0  ");
		if(StringUtil.isNotBlank(officialArticleForm.getTitle())) {
			sql.append(" and title like '%"+officialArticleForm.getTitle()+"%' ");
		}
		if(officialArticleForm.getSid()!=null) {
			sql.append(" and sid = '"+officialArticleForm.getSid()+"' ");
		}
		sql.append(" order by createTime desc,level desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("文章列表 SQL语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (officialArticleForm.getCurrentPage() - 1) * officialArticleForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + officialArticleForm.getPageSize());
			rs = pst.executeQuery();
			List<OfficialArticleBean> list = Lists.newArrayList();
			int number=0;
			while (rs.next()) {
				
				number++;
				OfficialArticleBean bean = new OfficialArticleBean();
				bean.setNumber(number);
				bean.setId(rs.getLong("id"));
				bean.setSid(rs.getLong("sid"));
				bean.setTitle(rs.getString("title"));
				bean.setAuthor(rs.getString("author"));
				bean.setRemark(rs.getString("remark"));
				bean.setContent(rs.getString("content"));
				bean.setNum(rs.getLong("num"));
				bean.setLevel(rs.getInt("level"));
				bean.setUrl(rs.getString("url"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setSectionName(rs.getString("name"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				//bean.setType(rs.getInt("type"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(officialArticleForm.getCurrentPage());
			data.setPageSize(officialArticleForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<OfficialArticleDaoImpl>----<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<OfficialArticleDaoImpl>----<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 文章预览
	 */
	public OfficialArticleBean get(Object id) {
		log.info("<OfficialArticleDaoImpl>----<get>------start");
		OfficialArticleBean officialArticleBean = super.get(id);
		log.info("<OfficialArticleDaoImpl>----<get>------end");
		return officialArticleBean;
	}

	/**
	 * 文章删除
	 */
	public boolean delete(Object id) {
		log.info("<OfficialArticleDaoImpl>-----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  official_article set deleteFlag= 1 where id in ("+id+") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("文章删除sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<OfficialArticleDaoImpl>-----<delete>-----end");
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
	 * 编辑文章
	 */
	public boolean update(OfficialArticleBean entity) {
		log.info("<OfficialArticleDaoImpl>----<update>------start");
		boolean update = super.update(entity);
		log.info("<OfficialArticleDaoImpl>----<update>------end");
		return update;
	}

	/**
	 * 文章发布
	 */
	public OfficialArticleBean insert(OfficialArticleBean entity) {
		log.info("<OfficialArticleDaoImpl>----<insert>------start");
		OfficialArticleBean officialArticleBean = super.insert(entity);
		log.info("<OfficialArticleDaoImpl>----<insert>------end");
		return officialArticleBean;
	}

	/**
	 * 官网详情
	 */
	@Override
	public OfficialArticleDto findDto(Long id) {
		log.info("<OfficialArticleDaoImpl>----<findDto>------start");
		OfficialArticleDto bean = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select id,title,content,url,createTime from official_article  where id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("文章官网详情 SQL语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new OfficialArticleDto();
				bean.setId(rs.getLong("id"));
				bean.setTitle(rs.getString("title"));
				bean.setContent(rs.getString("content"));
				bean.setUrl(rs.getString("url"));
				bean.setCreateTime(rs.getDate("createTime"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<OfficialArticleDaoImpl>----<findDto>------end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<OfficialArticleDaoImpl>----<findDto>------end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil machineListPage( OfficialArticleForm officialArticleForm ) {
		log.info("<OfficialArticleDaoImpl>----<machinelistPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select oa.id,sid,title,author,remark,content,num,level,oa.url,oa.createTime,oa.updateTime,oa.createUser,oa.updateUser,oa.deleteFlag,oa.type,os.name ");
		sql.append(" from official_article  oa LEFT JOIN official_section os on oa.sid=os.id where oa.deleteFlag=0 and oa.type=1 ");
		if(StringUtil.isNotBlank(officialArticleForm.getTitle())) {
			sql.append(" and title like '%"+officialArticleForm.getTitle()+"%' ");
		}
		if(officialArticleForm.getSid()!=null) {
			sql.append(" and sid = '"+officialArticleForm.getSid()+"' ");
		}
		sql.append(" order by createTime desc,level desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("文章列表 SQL语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (officialArticleForm.getCurrentPage() - 1) * officialArticleForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + officialArticleForm.getPageSize());
			rs = pst.executeQuery();
			List<OfficialArticleBean> list = Lists.newArrayList();
			int number=0;
			while (rs.next()) {

				number++;
				OfficialArticleBean bean = new OfficialArticleBean();
				bean.setNumber(number);
				bean.setId(rs.getLong("id"));
				bean.setSid(rs.getLong("sid"));
				bean.setTitle(rs.getString("title"));
				bean.setAuthor(rs.getString("author"));
				bean.setRemark(rs.getString("remark"));
				bean.setContent(rs.getString("content"));
				bean.setNum(rs.getLong("num"));
				bean.setLevel(rs.getInt("level"));
				bean.setUrl(rs.getString("url"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setSectionName(rs.getString("name"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				bean.setType(rs.getInt("type"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(officialArticleForm.getCurrentPage());
			data.setPageSize(officialArticleForm.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<OfficialArticleDaoImpl>----<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<OfficialArticleDaoImpl>----<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}


}
