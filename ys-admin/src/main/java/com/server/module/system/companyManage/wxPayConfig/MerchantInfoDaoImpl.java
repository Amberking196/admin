package com.server.module.system.companyManage.wxPayConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class MerchantInfoDaoImpl extends MySqlFuns implements MerchantInfoDao {

	private static final Logger log = LogManager.getLogger(MerchantInfoDaoImpl.class);

	@Override
	public List<MerchantInfo> findMerchantInfo(Integer type) {
		log.info("<MerchantInfoDaoImpl--findMerchantInfo--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT id,appId,mchId,mchKey,keyPath,`type`,companyId,planId,usingCompanyConfig,appAppId,appKey FROM `merchant_info` WHERE deleteFlag = 0 and `type` = "
						+ type);
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MerchantInfo> merchantList = new ArrayList<MerchantInfo>();
		MerchantInfo merchant = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				merchant = new MerchantInfo();
				merchant.setAppId(rs.getString("appId"));
				merchant.setId(rs.getInt("id"));
				merchant.setKeyPath(rs.getString("keyPath"));
				merchant.setMchId(rs.getString("mchId"));
				merchant.setMchKey(rs.getString("mchKey"));
				merchant.setType(rs.getInt("type"));
				merchant.setCompanyId(rs.getInt("companyId"));
				merchant.setPlanId(rs.getString("planId"));
				merchant.setUsingCompanyConfig(rs.getInt("usingCompanyConfig"));
				merchant.setUsingCompanyConfig(rs.getInt("usingCompanyConfig"));
				merchant.setAppAppId(rs.getString("appAppId"));
				merchant.setAppKey(rs.getString("appKey"));

				merchantList.add(merchant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<MerchantInfoDaoImpl--findMerchantInfo--end>");
		return merchantList;
	}

	@Override
	public MerchantInfo getPayConfig(Integer companyId) {
		log.info("<MerchantInfoDaoImpl--getPayConfig--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,appId,mchId,mchKey,keyPath,`type`,companyId,createTime,createUser,");
		sql.append(" deleteFlag,updateTime,updateUser,planId,usingCompanyConfig FROM `merchant_info` ");
		sql.append(" WHERE companyId = " + companyId + " AND `type` = 0");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MerchantInfo merchant = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				merchant = new MerchantInfo();
				merchant.setAppId(rs.getString("appId"));
				merchant.setCompanyId(rs.getInt("companyId"));
				merchant.setCreateTime(rs.getTimestamp("createTime"));
				merchant.setCreateUser(rs.getLong("createUser"));
				merchant.setDeleteFlag(rs.getInt("deleteFlag"));
				merchant.setId(rs.getInt("id"));
				merchant.setKeyPath(rs.getString("keyPath"));
				merchant.setMchId(rs.getString("mchId"));
				merchant.setMchKey(rs.getString("mchKey"));
				merchant.setPlanId(rs.getString("planId"));
				merchant.setType(rs.getInt("type"));
				merchant.setUpdateTime(rs.getTimestamp("updateTime"));
				merchant.setUpdateUser(rs.getLong("updateUser"));
				merchant.setUsingCompanyConfig(rs.getInt("usingCompanyConfig"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<MerchantInfoDaoImpl--getPayConfig--end>");
		return merchant;
	}

	@Override
	public boolean updatePayConfig(MerchantInfo merchant) {
		log.info("<MerchantInfoDaoImpl--updatePayConfig--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE merchant_info SET mchId=?,mchKey=?,keyPath=?,`type`=?,");
		sql.append(" deleteFlag=?,updateTime=?,updateUser=?,planId=?,usingCompanyConfig=?");
		sql.append(" WHERE companyId = ?");
		List<Object> param = new ArrayList<Object>();
		param.add(merchant.getMchId());
		param.add(merchant.getMchKey());
		param.add(merchant.getKeyPath());
		param.add(merchant.getType());
		param.add(merchant.getDeleteFlag());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		param.add(merchant.getUpdateUser());
		param.add(merchant.getPlanId());
		param.add(merchant.getUsingCompanyConfig());
		param.add(merchant.getCompanyId());
		int upate = upate(sql.toString(), param);
		log.info("<MerchantInfoDaoImpl--updatePayConfig--end>");
		if (upate > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Integer insertPayConfig(MerchantInfo merchant) {
		log.info("<MerchantInfoDaoImpl--insertPayConfig--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO merchant_info(appId,mchId,mchKey,keyPath,`type`,");
		sql.append(" companyId,createTime,createUser,deleteFlag,planId,usingCompanyConfig)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?)");
		param.add(merchant.getAppId());
		param.add(merchant.getMchId());
		param.add(merchant.getMchKey());
		param.add(merchant.getKeyPath());
		param.add(merchant.getType());
		param.add(merchant.getCompanyId());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
		param.add(merchant.getCreateUser());
		param.add(merchant.getDeleteFlag());
		param.add(merchant.getPlanId());
		param.add(merchant.getUsingCompanyConfig());
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<MerchantInfoDaoImpl--insertPayConfig--end>");
		if(insertGetID>0){
			return insertGetID;
		}
		return null;
	}

	@Override
	public MerchantInfo findMerchantInfoByCompanyId(Integer companyId) {
		log.info("<MerchantInfoDaoImpl--findMerchantInfoByCompanyId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,appId,mchId,mchKey,keyPath,`type`,companyId,planId,usingCompanyConfig ");
		sql.append(" FROM merchant_info WHERE companyId = '"+companyId+"' AND deleteFlag = 0 AND `type` = 0");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MerchantInfo merchant = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				merchant = new MerchantInfo();
				merchant.setAppId(rs.getString("appId"));
				merchant.setId(rs.getInt("id"));
				merchant.setKeyPath(rs.getString("keyPath"));
				merchant.setMchId(rs.getString("mchId"));
				merchant.setMchKey(rs.getString("mchKey"));
				merchant.setType(rs.getInt("type"));
				merchant.setCompanyId(rs.getInt("companyId"));
				merchant.setPlanId(rs.getString("planId"));
				merchant.setUsingCompanyConfig(rs.getInt("usingCompanyConfig"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<MerchantInfoDaoImpl--findMerchantInfoByCompanyId--end>");
		return merchant;
	}
}
