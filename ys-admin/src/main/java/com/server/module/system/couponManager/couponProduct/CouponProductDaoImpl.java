package com.server.module.system.couponManager.couponProduct;

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
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: yjr
 * create time: 2018-07-10 10:28:12
 */
@Repository
public class CouponProductDaoImpl extends BaseDao<CouponProductBean> implements CouponProductDao {

    private static Log log = LogFactory.getLog(CouponProductDaoImpl.class);

    public ReturnDataUtil listPage(CouponProductCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,couponId,productId,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_product where 1=1 ");
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
            List<CouponProductBean> list = Lists.newArrayList();
            while (rs.next()) {
                CouponProductBean bean = new CouponProductBean();
                bean.setId(rs.getLong("id"));
                bean.setCouponId(rs.getLong("couponId"));
                bean.setProductId(rs.getLong("productId"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setCreateUser(rs.getLong("createUser"));
                bean.setUpdateTime(rs.getDate("updateTime"));
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

    @Override
    public List<CouponProductVo> list(CouponProductForm condition) {
        StringBuilder sql = new StringBuilder();

        int useWhere=condition.getUseWhere();
        int couponId=condition.getCouponId();
        int isBind=condition.getIsBind();
        String name=condition.getName();

        if(useWhere==1){//1 机器优惠劵   2 商城优惠劵
            sql.append("SELECT k.* FROM (SELECT c.id,c.couponId,g.name,g.id AS productId FROM item_basic g  LEFT JOIN (SELECT * FROM coupon_product WHERE  deleteFlag=0 and couponId="+couponId+" ) c ON c.productId=g.id ) k WHERE 1=1 ");
        }else if(useWhere==2){
            sql.append("SELECT k.* FROM (SELECT c.id,c.couponId,g.name,g.id AS productId FROM shopping_goods g  LEFT JOIN (SELECT * FROM coupon_product WHERE  deleteFlag=0 and couponId="+couponId+" ) c ON c.productId=g.id ) k WHERE 1=1 ");
        }
        if(isBind==0){//未绑定
            sql.append(" and k.couponId IS  NULL");
        }else{// 1已绑定
            sql.append(" and k.couponId IS not NULL");

        }
        if(StringUtil.isNotBlank(name)){
            sql.append(" and k.name like '%"+name+"%'");
        }
        if (showSql) {
            log.info(sql);
        }
        //k.coupon IS NOT NULL
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<CouponProductVo> list = Lists.newArrayList();

        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());

            rs = pst.executeQuery();
            while (rs.next()) {
                CouponProductVo bean = new CouponProductVo();
                bean.setCouponId(rs.getInt("couponId"));
                bean.setProductId(rs.getLong("productId"));
                bean.setProductName(rs.getString("name"));
                bean.setId(rs.getInt("id"));
                if(isBind==0)
                    bean.setBindLabel("未绑定");
                else
                    bean.setBindLabel("已绑定");

                list.add(bean);
            }


            return list;

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return list;
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

    public CouponProductBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        CouponProductBean entity = new CouponProductBean();
        return super.del(entity);
    }

    public boolean update(CouponProductBean entity) {
        return super.update(entity);
    }

    public CouponProductBean insert(CouponProductBean entity) {
        return super.insert(entity);
    }

    public List<CouponProductBean> list(CouponProductCondition condition) {
        return null;
    }
}

