package com.server.module.system.baseManager.china;

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
 * create time: 2018-12-10 09:02:14
 */
@Repository
public class ChinaDaoImpl extends BaseDao<ChinaBean> implements ChinaDao {

    private static Log log = LogFactory.getLog(ChinaDaoImpl.class);

    public ReturnDataUtil listPage(ChinaCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select Id,Name,Pid from china where 1=1 ");
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
            List<ChinaBean> list = Lists.newArrayList();
            while (rs.next()) {
                ChinaBean bean = new ChinaBean();
                bean.setId(rs.getLong("Id"));
                bean.setName(rs.getString("Name"));
                bean.setPid(rs.getLong("Pid"));
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

    public ChinaBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        ChinaBean entity = new ChinaBean();
        return super.del(entity);
    }

    public boolean update(ChinaBean entity) {
        return super.update(entity);
    }

    public ChinaBean insert(ChinaBean entity) {
        return super.insert(entity);
    }
    public List<ChinaBean> cityList() {
        String sql="SELECT * FROM china WHERE id IN(440100,440600,420100,440400,510100,440300)";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ChinaBean> list = Lists.newArrayList();
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                ChinaBean bean = new ChinaBean();
                bean.setId(rs.getLong("Id"));
                bean.setName(rs.getString("Name"));
                bean.setPid(rs.getLong("Pid"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
            }
            return list;
        } catch (SQLException e) {
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
    public List<ChinaBean> areaList(Long pid) {
        String sql="SELECT * FROM china WHERE Pid="+pid;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ChinaBean> list = Lists.newArrayList();
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                ChinaBean bean = new ChinaBean();
                bean.setId(rs.getLong("Id"));
                bean.setName(rs.getString("Name"));
                bean.setPid(rs.getLong("Pid"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
            }
            return list;
        } catch (SQLException e) {
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
    public List<ChinaBean> list(ChinaCondition condition) {
        return null;
    }

    public List<ChinaBean> list() {
        StringBuilder sql = new StringBuilder();
        sql.append("select Id,Name,Pid from china where 1=1 ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ChinaBean> list = Lists.newArrayList();
        try {
            conn = openConnection();
            pst=conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                ChinaBean bean = new ChinaBean();
                bean.setId(rs.getLong("Id"));
                bean.setName(rs.getString("Name"));
                bean.setPid(rs.getLong("Pid"));
                list.add(bean);
            }

            return list;
        } catch (SQLException e) {
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

}

