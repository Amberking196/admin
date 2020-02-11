package com.server.module.customer.coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.CustomerUtil;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-07-07 14:49:52
 */
@Repository
public class CouponCustomerDao1Impl extends BaseDao<CouponCustomerBean> implements CouponCustomerDao {

    private static Logger log = LogManager.getLogger(CouponCustomerDao1Impl.class);

    public ReturnDataUtil listPage(CouponCustomerCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,couponId,customerId,state,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_customer where 1=1 ");
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
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            List<CouponCustomerBean> list = Lists.newArrayList();
            while (rs.next()) {
                CouponCustomerBean bean = new CouponCustomerBean();
                bean.setId(rs.getLong("id"));
                bean.setCouponId(rs.getLong("couponId"));
                bean.setCustomerId(rs.getLong("customerId"));
                bean.setState(rs.getInt("state"));
               // bean.setCreateTime(rs.getDate("createTime"));
                bean.setCreateUser(rs.getLong("createUser"));
                //bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setUpdateUser(rs.getLong("updateUser"));
                bean.setDeleteFlag(rs.getInt("deleteFlag"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
                log.info(plist.toString());
            }
            data.setCurrentPage(condition.getCurrentPage());
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

    public CouponCustomerBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        CouponCustomerBean entity = new CouponCustomerBean();
        return super.del(entity);
    }

    public boolean update(CouponCustomerBean entity) {
        return super.update(entity);
    }

    public CouponCustomerBean insert(CouponCustomerBean entity) {
        return super.insert(entity);
    }

    public List<CouponCustomerBean> list(CouponCustomerCondition condition) {

        return null;
    }

    @Override
    public List<CouponVo> usableCoupons(Long[] productIds, Double[] prices) {
    	log.info("<CouponCustomerDao1Impl>------<usableCoupons>----start");
        Long customerId= CustomerUtil.getCustomerId();
        String sql = " SELECT DISTINCT c.* FROM coupon_customer cc INNER JOIN coupon c ON cc.couponId=c.id WHERE  cc.state=1 AND cc.customerId='"+customerId+"'";
        log.info("用户可用优惠劵列表sql"+sql);
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<CouponVo> list = Lists.newArrayList();
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                CouponVo bean = new CouponVo();
                bean.setId(rs.getLong("id"));
                bean.setName(rs.getString("name"));
                bean.setUseWhere(rs.getInt("useWhere"));
                bean.setTarget(rs.getInt("target"));
                bean.setCompanyId(rs.getLong("companyId"));
                bean.setAreaId(rs.getLong("areaId"));
                bean.setAreaName(rs.getString("areaName"));
                bean.setCompanyName(rs.getString("companyName"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setType(rs.getInt("type"));
                bean.setWay(rs.getInt("way"));
                bean.setMoney(rs.getDouble("money"));
                bean.setDeductionMoney(rs.getDouble("deductionMoney"));
                bean.setSendMax(rs.getInt("sendMax"));
                bean.setStartTime(rs.getTimestamp("startTime"));
                bean.setEndTime(rs.getTimestamp("endTime"));
                bean.setPic(rs.getString("pic"));
                bean.setBindProduct(rs.getInt("bindProduct"));

                list.add(bean);
            }
            log.info("<CouponCustomerDao1Impl>------<usableCoupons>----end");
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("<CouponCustomerDao1Impl>------<usableCoupons>----end");
            return list;
        } finally {
           this.closeConnection(rs, pst, conn);
        }

    }
}

