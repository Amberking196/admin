package com.server.module.system.onlineUpdate.versionManager;

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

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class VersionInfoDaoImpl extends MySqlFuns implements VersionInfoDao{

	private final static Logger log = LogManager.getLogger(VersionInfoDaoImpl.class);

	@Override
	public Integer insert(VersionInfoBean versionInfo) {
		log.info("<VersionInfoDaoImpl--insert--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO version_manager(versionInfo,pid,createTime,createUser,updateTime,updateUser,remark) VALUES(?,?,?,?,?,?,?)");
		param.add(versionInfo.getVersionInfo());
		param.add(versionInfo.getPid());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(versionInfo.getCreateTime()));
		param.add(versionInfo.getCreateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(versionInfo.getUpdateTime()));
		param.add(versionInfo.getUpdateUser());
		param.add(versionInfo.getRemark());
		int id = insertGetID(sql.toString(), param);
		log.info("<VersionInfoDaoImpl--insert--end>");
		return id;
	}

	@Override
	public boolean update(VersionInfoBean versionInfo) {
		log.info("<VersionInfoDaoImpl--update--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE version_manager SET versionInfo=?,pid=?,updateTime=?,updateUser=?,remark=?,deleteFlag=? WHERE id = ?");
		param.add(versionInfo.getVersionInfo());
		param.add(versionInfo.getPid());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(versionInfo.getUpdateTime()));
		param.add(versionInfo.getUpdateUser());
		param.add(versionInfo.getRemark());
		if(versionInfo.getDeleteFlag()==null) {
			versionInfo.setDeleteFlag(0);
		}
		param.add(versionInfo.getDeleteFlag());
		param.add(versionInfo.getId());
		int upate = upate(sql.toString(),param);
		log.info("<VersionInfoDaoImpl--update--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public List<VersionInfoBean> getCanUpdateVersion(VersionInfoForm v) {
		log.info("<VersionInfoDaoImpl--getCanUpdateVersion--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,versionInfo,pid,createTime,createUser,updateTime,updateUser,remark FROM version_manager ");
		sql.append(" WHERE deleteFlag = 0 ");
		if(v.getType()!=null) {
			if(v.getType()==0) {
				sql.append(" and FIND_IN_SET(id,getUpdateList("+v.getVersionId()+"))");
			}else {
				sql = new StringBuffer();
				sql.append(" select id,versionInfo,pid,createTime,createUser,updateTime,updateUser,remark from version_manager where pid="+v.getVersionId());
				sql.append(" union select id,versionInfo,pid,createTime,createUser,updateTime,updateUser,remark from version_manager where pid=(select id from version_manager where pid="+v.getVersionId()+")");
				//sql.append(" and FIND_IN_SET(id,getDegradeList("+v.getVersionId()+"))");
			}
		}

		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VersionInfoBean> versionList = new ArrayList<VersionInfoBean>();
		VersionInfoBean versionBean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				versionBean = new VersionInfoBean();
				versionBean.setCreateTime(rs.getTimestamp("createTime"));
				versionBean.setCreateUser(rs.getLong("createUser"));
				versionBean.setId(rs.getInt("id"));
				versionBean.setPid(rs.getInt("pid"));
				versionBean.setUpdateTime(rs.getTimestamp("updateTime"));
				versionBean.setUpdateUser(rs.getLong("updateUser"));
				versionBean.setVersionInfo(rs.getString("versionInfo"));
				versionBean.setRemark(rs.getString("remark"));
				versionList.add(versionBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VersionInfoDaoImpl--getCanUpdateVersion--end>");
		return versionList;
	}

	@Override
	public VersionInfoBean getById(Integer id) {
		log.info("<VersionInfoDaoImpl--getById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,versionInfo,pid,createTime,createUser,updateTime,updateUser,remark FROM version_manager ");
		sql.append(" WHERE deleteFlag = 0 and id = '"+id+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VersionInfoBean versionBean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				versionBean = new VersionInfoBean();
				versionBean.setCreateTime(rs.getTimestamp("createTime"));
				versionBean.setCreateUser(rs.getLong("createUser"));
				versionBean.setId(rs.getInt("id"));
				versionBean.setPid(rs.getInt("pid"));
				versionBean.setUpdateTime(rs.getTimestamp("updateTime"));
				versionBean.setUpdateUser(rs.getLong("updateUser"));
				versionBean.setVersionInfo(rs.getString("versionInfo"));
				versionBean.setRemark(rs.getString("remark"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VersionInfoDaoImpl--getById--end>");
		return versionBean;
	}

	@Override
	public VersionInfoBean getByVersionInfo(String versionInfo) {
		log.info("<VersionInfoDaoImpl--getByVersionInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,versionInfo,pid,createTime,createUser,updateTime,updateUser,remark FROM version_manager ");
		sql.append(" WHERE deleteFlag = 0 and versionInfo = '"+versionInfo+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VersionInfoBean versionBean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				versionBean = new VersionInfoBean();
				versionBean.setCreateTime(rs.getTimestamp("createTime"));
				versionBean.setCreateUser(rs.getLong("createUser"));
				versionBean.setId(rs.getInt("id"));
				versionBean.setPid(rs.getInt("pid"));
				versionBean.setUpdateTime(rs.getTimestamp("updateTime"));
				versionBean.setUpdateUser(rs.getLong("updateUser"));
				versionBean.setVersionInfo(rs.getString("versionInfo"));
				versionBean.setRemark(rs.getString("remark"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VersionInfoDaoImpl--getByVersionInfo--end>");
		return versionBean;
	}

	@Override
	public List<VersionInfoBean> getByForm(VersionInfoForm form) {
		log.info("<VersionInfoDaoImpl--getByForm--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SQL_CALC_FOUND_ROWS vm.`id`,vm.versionInfo,vm.`pid`,vm2.`versionInfo` as pversion,vm.`createTime`,");
		sql.append(" vm.`createUser`,vm.`updateTime`,vm.`updateUser`,vm.`remark`,vm.`deleteFlag`,li1.name as createUserName,li2.name as updateUserName");
		sql.append(" FROM version_manager AS vm");
		sql.append(" LEFT JOIN version_manager AS vm2 ON vm.`pid` = vm2.`id`");
		sql.append(" LEFT JOIN login_info AS li1 ON li1.id = vm.createUser");
		sql.append(" LEFT JOIN login_info AS li2 ON li2.id = vm.updateUser");
		if(StringUtils.isNotBlank(form.getPversion())){
			sql.append(" WHERE vm2.`versionInfo` = '"+form.getPversion()+"'");
		}
		sql.append(" ORDER BY vm.createTime DESC");
		if(form.getIsShowAll() == 0){
			sql.append(" LIMIT "+(form.getCurrentPage()-1)*form.getPageSize()+","+form.getPageSize());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VersionInfoBean> versionList = new ArrayList<VersionInfoBean>();
		VersionInfoBean version =  null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				version = new VersionInfoBean();
				version.setCreateTime(rs.getTimestamp("createTime"));
				version.setCreateUser(rs.getLong("createUser"));
				version.setDeleteFlag(rs.getInt("deleteFlag"));
				version.setId(rs.getInt("id"));
				version.setPid(rs.getInt("pid"));
				version.setPversion(rs.getString("pversion"));
				version.setRemark(rs.getString("remark"));
				version.setUpdateTime(rs.getTimestamp("updateTime"));
				version.setUpdateUser(rs.getLong("updateUser"));
				version.setVersionInfo(rs.getString("versionInfo"));
				version.setCreateUserName(rs.getString("createUserName"));
				version.setUpdateUserName(rs.getString("updateUserName"));
				versionList.add(version);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VersionInfoDaoImpl--getByForm--end>");
		return versionList;
	}

}
