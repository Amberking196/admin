package com.server.module.system.machineManage.machinesWayTemplateDetail;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.machineManage.machinesWayTemplate.WayItemDto;
import com.server.util.ReturnDataUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * author name: zfc
 * create time: 2018-08-03 10:35:22
 */
@Repository
public class VendingMachinesWayTemplateDelDaoImpl extends BaseDao<VendingMachinesWayTemplateDelBean> implements VendingMachinesWayTemplateDelDao {

    private static Log log = LogFactory.getLog(VendingMachinesWayTemplateDelDaoImpl.class);

    public ReturnDataUtil listPage(VendingMachinesWayTemplateDelCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,templateId,wayNumber,itemId,price,maxCapacity,curCapacity from vending_machines_way_template_del where 1=1 ");
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
            List<VendingMachinesWayTemplateDelBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachinesWayTemplateDelBean bean = new VendingMachinesWayTemplateDelBean();
                bean.setId(rs.getLong("id"));
                bean.setTemplateId(rs.getLong("templateId"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                bean.setItemId(rs.getLong("itemId"));
                bean.setPrice(rs.getDouble("price"));
                bean.setMaxCapacity(rs.getLong("maxCapacity"));
                bean.setCurCapacity(rs.getLong("curCapacity"));
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

    public VendingMachinesWayTemplateDelBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object entity) {


        return super.del((VendingMachinesWayTemplateDelBean) entity);
    }

    public boolean update(VendingMachinesWayTemplateDelBean entity) {
        return super.update(entity);
    }

    public VendingMachinesWayTemplateDelBean insert(VendingMachinesWayTemplateDelBean entity) {
        return super.insert(entity);
    }

    @Override
    public boolean addItemToWay(ItemToWayTemplateDetailDto dto) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            String bindSql = "update vending_machines_way_template_del set itemId=?,price=?,maxCapacity=?,curCapacity=?,costPrice=? where templateId=? and wayNumber=?";
            pst = conn.prepareStatement(bindSql);
            pst.setLong(1, dto.getBasicItemId());
            pst.setDouble(2, dto.getPrice());
            pst.setInt(3, dto.getFullNum());
            pst.setInt(4, dto.getNum());
            pst.setDouble(5, dto.getCostPrice());
            pst.setLong(6, dto.getTemplateId());
            pst.setInt(7, dto.getWayNumber());
            pst.execute();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return false;
        } finally {
            try {
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ReturnDataUtil checkWayNum(VendingMachinesWayTemplateDelBean entity) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        ReturnDataUtil dataUtil = new ReturnDataUtil();

        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            String sql = "select * from vending_machines_way_template_del where templateId = " + entity.getTemplateId() + " and wayNumber= " + entity.getWayNumber() + "  ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();


            // 如果有数据，说明就已经存在货道号了
            if (rs.next()) {
                dataUtil.setStatus(0);
                dataUtil.setMessage("该货道已创建，请添加其他的货道号");
                dataUtil.setReturnObject(false);
            }
            conn.commit();
            return dataUtil;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        } finally {
            try {
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dataUtil;

    }

    @Override
    public ReturnDataUtil updateDetailAndItem(WayItemDto entity) {

        Connection conn = null;
        PreparedStatement pst = null;
        ReturnDataUtil result = new ReturnDataUtil();
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            // 修改模板详情数据
            String bindSql = "update vending_machines_way_template_del set  costPrice=" + entity.getCostPrice() + ",  price=" + entity.getPrice() + " ,maxCapacity=" + entity.getMaxCapacity() + ", curCapacity=" + entity.getCurCapacity() + " where templateId=" + entity.getTemplateId() + " and wayNumber=" + entity.getWayNumber() + "";
            pst = conn.prepareStatement(bindSql);
            pst.execute();

            conn.commit();

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            result.setStatus(0);
            result.setMessage("更新该模板的通道数据失败");

            return result;

        } finally {
            try {
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<VendingMachinesWayTemplateDelBean> findDetailsByTemplateId(Long templateId) {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            // 修改模板详情数据
            String bindSql = "SELECT * FROM vending_machines_way_template_del WHERE templateId = " + templateId + "";
            pst = conn.prepareStatement(bindSql);
            rs = pst.executeQuery();

            List<VendingMachinesWayTemplateDelBean> beans = Lists.newArrayList();
            while (rs.next()) {

                VendingMachinesWayTemplateDelBean bean = new VendingMachinesWayTemplateDelBean();
                bean.setId(rs.getLong("id"));
                bean.setTemplateId(rs.getLong("templateID"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                bean.setItemId(Long.valueOf(rs.getInt("itemId")));
                bean.setPrice(rs.getDouble("price"));
                bean.setCostPrice(rs.getDouble("costPrice"));
                bean.setCurCapacity(rs.getLong("curCapacity"));
                bean.setMaxCapacity(rs.getLong("maxCapacity"));

                beans.add(bean);
            }

            conn.commit();
            return beans;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } finally {
            try {
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return null;
    }


    public List<VendingMachinesWayTemplateDelBean> list(VendingMachinesWayTemplateDelCondition condition) {
        return null;
    }

	@Override
	public ReturnDataUtil checkWayNum(ItemToWayTemplateDetailDto dto) {
		  Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;

	        ReturnDataUtil dataUtil = new ReturnDataUtil();

	        try {
	            conn = openConnection();
	            conn.setAutoCommit(false);

	            String sql = "select * from vending_machines_way_template_del where templateId = " + dto.getTemplateId() + " and wayNumber= " + dto.getWayNumber() + "  ";
	            pst = conn.prepareStatement(sql);
	            rs = pst.executeQuery();


	            // 如果有数据，说明就已经存在货道号了
	            if (rs.next()) {
	                dataUtil.setStatus(0);
	                dataUtil.setMessage("该货道存在，可以添加商品");
	                dataUtil.setReturnObject(true);
	            }
	            conn.commit();
	            return dataUtil;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            dataUtil.setStatus(-1);
	            dataUtil.setMessage("查询失败");
	            log.error(e.getMessage());
	            try {
	                conn.rollback();
	            } catch (SQLException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();

	            }
	        } finally {
	            try {
	                pst.close();
	                closeConnection(conn);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }

	        return dataUtil;
	}
}

