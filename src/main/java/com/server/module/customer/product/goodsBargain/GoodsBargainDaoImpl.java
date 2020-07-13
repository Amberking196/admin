package com.server.module.customer.product.goodsBargain;

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
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-21 10:31:41
 */
@Repository
public class GoodsBargainDaoImpl extends BaseDao<GoodsBargainBean> implements GoodsBargainDao {

    private static Logger  log = LogManager.getLogger(GoodsBargainDaoImpl.class);

    public ReturnDataUtil listPage(GoodsBargainCondition condition) {
    	log.info("<GoodsBargainDaoImpl>------<listPage>----start");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,goodsId,oneBargainPrice,lowestPrice,bargainCount,goodsLimit,startTime,endTime,hourLimit,createTime,createUser,createUserName,updateTime,updateUser,state,deleteFlag from goods_bargain where 1=1 ");

        if(condition.getGoodsId()!=null){
            sql.append(" and goodsId="+condition.getGoodsId());
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
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            List<GoodsBargainBean> list = Lists.newArrayList();
            while (rs.next()) {
                GoodsBargainBean bean = new GoodsBargainBean();
                bean.setId(rs.getLong("id"));
                bean.setGoodsId(rs.getLong("goodsId"));
                bean.setOneBargainPrice(rs.getBigDecimal("oneBargainPrice"));
                bean.setLowestPrice(rs.getBigDecimal("lowestPrice"));
                bean.setBargainCount(rs.getLong("bargainCount"));
                bean.setStartTime(rs.getDate("startTime"));
                bean.setEndTime(rs.getDate("endTime"));
                bean.setHourLimit(rs.getLong("hourLimit"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setCreateUser(rs.getLong("createUser"));
                bean.setCreateUserName(rs.getString("createUserName"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setUpdateUser(rs.getLong("updateUser"));
                bean.setState(rs.getInt("state"));
                bean.setGoodsLimit(rs.getLong("goodsLimit"));
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
            log.info("<GoodsBargainDaoImpl>------<listPage>----end");
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("<GoodsBargainDaoImpl>------<listPage>----end");
            return data;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
    }

    public GoodsBargainBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        GoodsBargainBean entity = new GoodsBargainBean();
        return super.del(entity);
    }

    public boolean update(GoodsBargainBean entity) {
        return super.update(entity);
    }

    public GoodsBargainBean insert(GoodsBargainBean entity) {
        return super.insert(entity);
    }

    public List<GoodsBargainBean> list(GoodsBargainCondition condition) {
        return null;
    }
}

