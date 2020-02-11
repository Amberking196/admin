package com.server.module.system.companyManage.aliPayConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class AliPayDaoImpl extends MySqlFuns implements AliPayDao {

	private final static Logger log = LogManager.getLogger(AliPayDaoImpl.class);

	@Override
	public boolean updateAliConfig(AliPayConfig aliConfig) {
		log.info("<AliPayDaoImpl-updateAliConfig-start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ali_info SET appId =?,app_private_key =?,app_public_key=?,alipay_public_key=?,partner=? WHERE companyId = ?");
		param.add(aliConfig.getAppId());
		param.add(aliConfig.getApp_private_key());
		param.add(aliConfig.getApp_public_key());
		param.add(aliConfig.getAlipay_public_key());
		param.add(aliConfig.getPartner());
		param.add(aliConfig.getCompanyId());
		int insertGetID = upate(sql.toString(), param);
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sqls = new StringBuffer();
		sqls.append("UPDATE ali_merchant_info SET partner = ?,`key` = ?,template_id = ? WHERE companyId = ?");
		params.add(aliConfig.getPartner());
		params.add(aliConfig.getKey());
		params.add(aliConfig.getTemplate_id());
		params.add(aliConfig.getCompanyId());
		int insertGetIDs = upate(sqls.toString(), params);
		
		log.info("<AliPayDaoImpl-updateAliConfig-end>");
		if(insertGetID>0 && insertGetIDs>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean insertAliConfig(AliPayConfig aliConfig) {
		log.info("<AliPayDaoImpl-insertAliConfig-start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ali_info (companyId,appId,`app_private_key`,app_public_key,alipay_public_key,partner) VALUES(?,?,?,?,?,?)");
		param.add(aliConfig.getCompanyId());
		param.add(aliConfig.getAppId());
		param.add(aliConfig.getApp_private_key());
		param.add(aliConfig.getApp_public_key());
		param.add(aliConfig.getAlipay_public_key());
		param.add(aliConfig.getPartner());
		int insertGetID = insertGetID(sql.toString(), param);
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sqls = new StringBuffer();
		sqls.append(" INSERT INTO ali_merchant_info (companyId,partner,`key`,template_id) VALUES(?,?,?,?)");
		params.add(aliConfig.getCompanyId());
		params.add(aliConfig.getPartner());
		params.add(aliConfig.getKey());
		params.add(aliConfig.getTemplate_id());
		int insertGetIDs = insertGetID(sqls.toString(), params);
		log.info("<AliPayDaoImpl-insertAliConfig-end>");
		if(insertGetID>0 && insertGetIDs >0){
			return true;
		}
		return false;
	}

	@Override
	public AliPayConfig findAliConfig(Integer companyId) {
		log.info("<AliPayDaoImpl-findAliConfig-start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ai.companyId,ai.appId,ai.app_private_key,ai.app_public_key,ai.alipay_public_key,");
		sql.append(" ai.partner,ami.key,ami.template_id FROM ali_info AS ai");
		sql.append(" INNER JOIN ali_merchant_info AS ami ON ai.companyId = ami.companyId");
		sql.append(" WHERE ai.companyId = "+companyId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AliPayConfig alipayConfig = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				alipayConfig = new AliPayConfig();
				alipayConfig.setAlipay_public_key(rs.getString("alipay_public_key"));
				alipayConfig.setApp_private_key(rs.getString("app_private_key"));
				alipayConfig.setApp_public_key(rs.getString("app_public_key"));
				alipayConfig.setAppId(rs.getString("appId"));
				alipayConfig.setCompanyId(rs.getInt("companyId"));
				alipayConfig.setKey(rs.getString("key"));
				alipayConfig.setPartner(rs.getString("partner"));
				alipayConfig.setTemplate_id(rs.getString("template_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<AliPayDaoImpl-findAliConfig-end>");
		return alipayConfig;
	}

}
