package com.server.module.system.machineManage.machinesWayTemplate;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * author name: zfc
 * create time: 2018-08-03 10:19:25
 */
@Repository
public class VendingMachinesWayTemplateDaoImpl extends BaseDao<VendingMachinesWayTemplateBean> implements VendingMachinesWayTemplateDao {

    private static Log log = LogFactory.getLog(VendingMachinesWayTemplateDaoImpl.class);
    @Autowired
    private CompanyDao companyDao;


    public ReturnDataUtil listPage(VendingMachinesWayTemplateCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.templatename, c.name as companyname, a.name as areaName, i.name as createname,t.createTime as createTime,t.updateTime as updateTime  ");
        sql.append("  from vending_machines_way_template t inner join company c on t.companyid = c.id inner join login_info i on t.userid = i.id left join vending_area a on t.areaid = a.id  ");
        sql.append(" where 1 = 1 ");
        // 查询出子公司
        String findSQL = companyDao.findAllSonCompanyIdForInSql(condition.getCompanyId());
        if (findSQL != null) {
            sql.append("and t.companyid in " + findSQL + "");
        }

        if (condition.getAreaId() != null) {
            sql.append(" and t.areaId = " + condition.getAreaId() + "");
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            sql.append(" order by t.createTime desc ");//排序创建时间
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            rs = pst.executeQuery();
            List<TemplateVo> list = Lists.newArrayList();
            while (rs.next()) {
                TemplateVo bean = new TemplateVo();
                bean.setId(rs.getLong("id"));
                bean.setTemplateName(rs.getString("templateName"));
                bean.setCompanyName(rs.getString("companyName"));
                bean.setAreaName(rs.getString("areaName"));
                bean.setCreatorName(rs.getString("createName"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCreateTime(rs.getDate("createTime"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
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

    public ReturnDataUtil listTemplateName(VendingMachinesWayTemplateCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,templateName,companyId,areaID,userId,updateTime,createTime from vending_machines_way_template where 1=1   ");

        // 查询出子公司
        String findSQL = companyDao.findAllSonCompanyIdForInSql(condition.getCompanyId());
        if (findSQL != null) {
            sql.append("and companyid in " + findSQL + "");
        }

        if (condition.getAreaId() != null) {
            sql.append(" and areaId = " + condition.getAreaId() + "");
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            rs = pst.executeQuery();
            List<VendingMachinesWayTemplateBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachinesWayTemplateBean bean = new VendingMachinesWayTemplateBean();
                bean.setId(rs.getLong("id"));
                bean.setTemplateName(rs.getString("templateName"));
                bean.setCompanyId(rs.getInt("companyId"));
                bean.setAreaID(rs.getInt("areaID"));
                bean.setUserId(rs.getLong("userId"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCreateTime(rs.getDate("createTime"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
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

    public VendingMachinesWayTemplateBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        return super.del((VendingMachinesWayTemplateBean) id);
    }

    public boolean update(VendingMachinesWayTemplateBean entity) {
        return super.update(entity);
    }

    public VendingMachinesWayTemplateBean insert(VendingMachinesWayTemplateBean entity) {
        return super.insert(entity);
    }

    @Override
    public ReturnDataUtil listDetailsById(Long templateId) {

        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select td.id as templatedetailid, t.id as templateid, td.waynumber as waynumber, ib.id as itemid, ib.name as itemname , td.maxcapacity as maxcapacity, td.curcapacity as curcapacity, td.costprice as costprice, td.price as price, ib.pic as pic  ");
        sql.append("  from vending_machines_way_template t left join vending_machines_way_template_del td on t.id = td.templateid left join item_basic ib on td.itemid = ib.id ");
        sql.append(" where td.templateid = " + templateId + "");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            pst = conn.prepareStatement(sql.toString());

            rs = pst.executeQuery();
            List<WayItemDto> list = Lists.newArrayList();
            while (rs.next()) {
                WayItemDto bean = new WayItemDto();
                bean.setTemplateDetailId(rs.getLong("templatedetailid"));
                bean.setTemplateId(rs.getLong("templateId"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                bean.setItemId(rs.getInt("itemId"));
                bean.setItemName(rs.getString("itemName"));
                bean.setMaxCapacity(rs.getInt("maxCapacity"));
                bean.setCurCapacity(rs.getInt("curCapacity"));
                bean.setCostPrice(rs.getDouble("costPrice"));
                bean.setPrice(rs.getDouble("price"));
                bean.setPic(rs.getString("pic"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
            }
            data.setCurrentPage(1);
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
    public boolean checkTemplateName(String templateName) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * from vending_machines_way_template where templateName='" + templateName + "'");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());

            rs = pst.executeQuery();

            // 如果有数据，直接返回true(说明名称已经存在了)
            if (showSql) {
                log.info(sql);
            }
            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public ReturnDataUtil listOwnTemplate(VendingMachinesWayTemplateCondition condition) {

        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,templateName,companyId,areaID,userId,updateTime,createTime from vending_machines_way_template where 1=1 and userId= " + condition.getUserId() + "  ");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            rs = pst.executeQuery();
            List<VendingMachinesWayTemplateBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachinesWayTemplateBean bean = new VendingMachinesWayTemplateBean();
                bean.setId(rs.getLong("id"));
                bean.setTemplateName(rs.getString("templateName"));
                bean.setCompanyId(rs.getInt("companyId"));
                bean.setAreaID(rs.getInt("areaID"));
                bean.setUserId(rs.getLong("userId"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCreateTime(rs.getDate("createTime"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
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


    public List<VendingMachinesWayTemplateBean> list(VendingMachinesWayTemplateCondition condition) {
        return null;
    }
}

