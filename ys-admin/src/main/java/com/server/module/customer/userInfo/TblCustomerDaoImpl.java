package com.server.module.customer.userInfo;

import com.server.common.persistence.BaseDao;
import com.server.util.EmojiUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TblCustomerDaoImpl extends BaseDao<TblCustomerBean> implements TblCustomerDao{
    public TblCustomerBean getCustomerById(Long customerId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT tcw.companyId,tc.id,tcw.openId,tc.type,tc.alipayUserId,tc.phone,");
        sql.append(" tc.UserName,tc.sexId,tc.city,tc.province,tc.country,");
        sql.append(" tc.latitude,tc.longitude,tc.isCertified,tc.isStudentCertified,tc.huafaAppOpenId,");
        sql.append(" tc.userStatus,tc.userType,tc.createTime,tc.createId,tc.updateTime,");
        sql.append(" tc.lastUpdateId,tc.deleteFlag,tc.inviterId,tc.userBalance,tc.integral,tcw.nickname,tcw.headimgurl,tc.vmCode FROM tbl_customer AS tc");
        sql.append(" LEFT JOIN tbl_customer_wx AS tcw ON tc.id = tcw.customerId ");
        if(customerId!=null) {
            sql.append(" where tc.id="+customerId);
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        TblCustomerBean bean = new TblCustomerBean();
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                bean.setId(rs.getLong("id"));
                bean.setOpenId(rs.getString("openId"));
                bean.setType(rs.getInt("type"));
                bean.setAlipayUserId(rs.getString("alipayUserId"));
                bean.setPhone(rs.getString("phone"));
                bean.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
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
                bean.setCreateTime(rs.getTimestamp("createTime"));
                bean.setCreateId(rs.getString("createId"));
                bean.setUpdateTime(rs.getTimestamp("updateTime"));
                bean.setLastUpdateId(rs.getString("lastUpdateId"));
                bean.setHuafaAppOpenId(rs.getString("huafaAppOpenId"));
                bean.setDeleteFlag(rs.getInt("deleteFlag"));
                bean.setCompanyId(rs.getInt("companyId"));
                bean.setInviterId(rs.getLong("inviterId"));
                bean.setIntegral(rs.getInt("integral"));
                bean.setUserBalance(rs.getBigDecimal("userBalance"));
                bean.setVmCode(rs.getString("vmCode"));
            }
            if (showSql) {
                log.info(sql);
            }
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return bean;
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
