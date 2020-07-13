package com.server.module.system.machineManage.machinesWay;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machineItem.VendingMachinesItemBean;
import com.server.module.system.machineManage.machineItem.VendingMachinesItemDao;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * author name: yjr create time: 2018-04-12 14:04:38
 */
@Repository
public class VendingMachinesWayDaoImpl extends BaseDao<VendingMachinesWayBean> implements VendingMachinesWayDao {

    public static Logger log = LogManager.getLogger(VendingMachinesWayDaoImpl.class);
    @Autowired
    private VendingMachinesItemDao vendingMachinesItemDaoImpl;
    @Autowired
    private CompanyDao companyDaoImpl;

    @Autowired
    private VendingMachinesInfoDao vendingMachinesInfoDaoImpl;
    @Autowired
    private StateInfoDao StateInfoDaoImpl;
    
    public ReturnDataUtil listPage(VendingMachinesWayCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select id,vendingMachinesCode,wayNumber,itemId,state,num,fullNum,updateTime,createTime from vending_machines_way where 1=1 ");
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
            List<VendingMachinesWayBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingMachinesWayBean bean = new VendingMachinesWayBean();
                bean.setId(rs.getLong("id"));
                bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                bean.setItemId(rs.getLong("itemId"));
                bean.setState(rs.getInt("state"));
                bean.setNum(rs.getInt("num"));
                bean.setFullNum(rs.getInt("fullNum"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCreateTime(rs.getDate("createTime"));
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
    
    public List<VendingMachinesWayBean> listWay(String vmCode,Integer wayNumber) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select id,vendingMachinesCode,wayNumber,itemId,state,num,fullNum,updateTime,createTime from vending_machines_way where vendingMachinesCode='"+vmCode+"' and wayNumber="+wayNumber);
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection conn=null;
        List<VendingMachinesWayBean> list = Lists.newArrayList();
        try {
            conn = openConnection();
            pst=conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                VendingMachinesWayBean bean = new VendingMachinesWayBean();
                bean.setId(rs.getLong("id"));
                bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                bean.setItemId(rs.getLong("itemId"));
                bean.setState(rs.getInt("state"));
                bean.setNum(rs.getInt("num"));
                bean.setFullNum(rs.getInt("fullNum"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCreateTime(rs.getDate("createTime"));
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

    public VendingMachinesWayBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        VendingMachinesWayBean entity = new VendingMachinesWayBean();
        entity.setId((Long) id);
        return super.del(entity);
    }

    public boolean update(VendingMachinesWayBean entity) {
        return super.update(entity);
    }

    public VendingMachinesWayBean insert(VendingMachinesWayBean entity) {
        return super.insert(entity);
    }

    public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition) {
        return null;
    }

    @Override
    public List<WayDto> listAll(String vmCode) {
    	StringBuilder sql = new StringBuilder();
        sql.append(
                "select vmp.homeImg as topLeftPic,a.picId,b.basicItemId,vmi.state as vmiState,a.id as wayId,wayNumber,itemId,a.state,num,fullNum,b.price,b.costPrice,hot,c.name as itemName,c.pic,b.endTime,locatoinName  "
                        + " from vending_machines_way a left join vending_machines_item b on a.itemid=b.id "
                        + " left join item_basic c on b.basicItemId=c.id left join vending_machines_info vmi on a.vendingMachinesCode=vmi.code left join vending_machines_pic vmp on vmp.id = a.picId where vendingMachinesCode=?  ");
        //sql.append(" and vmi.companyId in " + companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
        sql.append("  order by wayNumber ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            pst.setObject(1, vmCode);
            rs = pst.executeQuery();
            List<WayDto> list = Lists.newArrayList();
            while (rs.next()) {

                WayDto bean = new WayDto();
                bean.setWayId(rs.getLong("wayId"));
                // bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                bean.setBasicItemId(rs.getLong("basicItemId"));
                bean.setItemId(rs.getLong("itemId"));
                bean.setState(rs.getInt("state"));
                String stateName = StateInfoDaoImpl.getNameByState(rs.getLong("state"));
                if (stateName != null) {
                    bean.setStateName(stateName);
                }
                String vmiStateName = StateInfoDaoImpl.getNameByState(rs.getLong("vmiState"));
                if (vmiStateName != null) {
                    bean.setVmiStateName(vmiStateName);
                }
                bean.setNum(rs.getInt("num"));
                bean.setFullNum(rs.getInt("fullNum"));
                bean.setPrice(rs.getBigDecimal("price"));
                bean.setCostPrice(rs.getBigDecimal("costPrice"));
                bean.setHot(rs.getInt("hot"));
                bean.setItemName(rs.getString("itemName"));
                bean.setPic(rs.getString("pic"));
                bean.setEndTime(rs.getDate("endTime"));
                bean.setLocatoinName(rs.getString("locatoinName"));
                bean.setPicId(rs.getLong("picId"));
                bean.setTopLeftPic(rs.getString("topLeftPic"));
                list.add(bean);
            }
            if (showSql) {
                //log.info(sql);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
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

    //
    public List<WayDto1> listAll1(String vmCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select a.id as wayId,a.wayNumber,a.state  "
                        + " from vending_machines_way a  left join vending_machines_info vmi on a.vendingMachinesCode=vmi.code "
                        + "  where vendingMachinesCode='"+vmCode+"' ");
       // sql.append(" and vmi.companyId in " + companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
        sql.append("  order by wayNumber ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            List<WayDto1> list = Lists.newArrayList();
            while (rs.next()) {

                WayDto1 bean = new WayDto1();
                bean.setWayId(rs.getLong("wayId"));
                // bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                bean.setState(rs.getInt("state"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
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


    /**
     * 生成价格库 并绑定到货道
     */
    public boolean bindItem(BindItemDto dto, VendingMachinesItemBean newItem) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            newItem = (VendingMachinesItemBean) super.insert(conn, newItem);
            String bindSql = "update vending_machines_way set itemId=? ,updateTime=?,num=?,fullNum=? where vendingMachinesCode=? and wayNumber=?";
            pst = conn.prepareStatement(bindSql);
            pst.setLong(1, newItem.getId());
            pst.setDate(2, new Date(System.currentTimeMillis()));
            pst.setInt(3, dto.getNum());
            pst.setInt(4, dto.getFullNum());
            pst.setString(5, dto.getVmCode());
            pst.setInt(6, dto.getWayNumber());
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

    /**
     * 编辑货道及商品
     */
    @SuppressWarnings("resource")
    public boolean editWayAndItem(BindItemDto dto) {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            String updateItemSql = "update vending_machines_item set price=? ,costPrice=?,updateTime=CURRENT_TIMESTAMP() where id=?";
//			String updateItemSql = "update vending_machines_item set price=? ,costPrice=?,hot=? ,updateTime=?,endTime=? where id=?";
            log.info(updateItemSql);
            pst = conn.prepareStatement(updateItemSql);
            pst.setBigDecimal(1, new BigDecimal(dto.getPrice()));
            pst.setBigDecimal(2, new BigDecimal(dto.getCostPrice()));
            //pst.setInt(3, dto.getHot());
            //pst.setDate(3, new java.sql.Date(System.currentTimeMillis()));

            //pst.setDate(5, new java.sql.Date(dto.getEndTime().getTime()));
            pst.setLong(3, dto.getId());
            pst.execute();
           // String bindSql = "update vending_machines_way set itemId=? ,updateTime=? ,num=?, fullNum=? where vendingMachinesCode=? and wayNumber=?";

            String bindSql = "update vending_machines_way set updateTime=? , fullNum=? , picId=? where vendingMachinesCode=? and wayNumber=?";
            log.info(bindSql);
            log.info(dto.getPicId());
            pst = conn.prepareStatement(bindSql);
            //pst.setLong(1, dto.getId());
            pst.setDate(1, new Date(System.currentTimeMillis()));
            pst.setInt(2, dto.getFullNum());
           // pst.setInt(4, dto.getFullNum());
            pst.setObject(3, dto.getPicId());//这样picId可以为null
            pst.setString(4, dto.getVmCode());
            pst.setInt(5, dto.getWayNumber());
            log.info(pst);
            boolean a=pst.execute();
            log.info(a);

            //加日志
            User user = UserUtils.getUser();
            VendingMachinesItemBean item = vendingMachinesItemDaoImpl.get(dto.getId());
            StringBuilder sqlLog = new StringBuilder("insert into vending_wayprice_log (itemId, vmCode,wayNumber, prePrice,price,operate, userId, createTime) values(");

            sqlLog.append(dto.getId() + ",");
            sqlLog.append("'" + dto.getVmCode() + "',");
            sqlLog.append(dto.getWayNumber() + ",");
            sqlLog.append(item.getPrice().doubleValue() + ",");
            sqlLog.append(dto.getPrice() + ",");
            sqlLog.append("' ',");
            sqlLog.append(user.getId() + ",");
            sqlLog.append(" CURRENT_TIMESTAMP() )");

            log.info(sqlLog.toString());
            pst = conn.prepareStatement(sqlLog.toString());
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

    /**
     * 统计货道的商品数量
     */
    public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition) {

        List<Object> plist = Lists.newArrayList();

        StringBuilder sb = new StringBuilder();
        sb.append(
                "SELECT a.code as vmCode,a.companyId,a.`locatoinName` as address,d.name as itemName ,b.wayNumber,b.num,b.fullNum,b.state,b.id ");
        /*
         * sb.
         * append(" MAX(CASE b.wayNumber WHEN 1 THEN IFNULL(b.num,0) ELSE 0 END ) '1num', "
         * ); sb.
         * append(" MAX(CASE b.wayNumber WHEN 1 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '1fulnum',"
         * ); sb.
         * append(" MAX(CASE b.wayNumber WHEN 2 THEN IFNULL(b.num,0) ELSE 0 END ) '2num', "
         * ); sb.
         * append(" MAX(CASE b.wayNumber WHEN 2 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '2fulnum', "
         * ); sb.
         * append(" MAX(CASE b.wayNumber WHEN 3 THEN IFNULL(b.num,0) ELSE 0 END ) '3num', "
         * ); sb.
         * append(" MAX(CASE b.wayNumber WHEN 3 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '3fulnum',"
         * ); sb.
         * append(" MAX(CASE b.wayNumber WHEN 4 THEN IFNULL(b.num,0) ELSE 0 END ) '4num', "
         * ); sb.
         * append(" MAX(CASE b.wayNumber WHEN 4 THEN IFNULL(b.fullNum,0) ELSE 0 END ) '4fulnum'"
         * );
         */
        sb.append(" FROM vending_machines_info a ");
        sb.append("INNER JOIN vending_machines_way b ");
        if (condition.getUnderNum() != null) {
            sb.append(" ON (a.code=b.vendingMachinesCode  AND b.num<? ) ");
            plist.add(condition.getUnderNum());
        } else {
            sb.append(" ON (a.code=b.vendingMachinesCode )");
        }
        sb.append(" LEFT JOIN vending_machines_item c ON c.id=b.`itemId`");
        sb.append(" JOIN item_basic d ON c.basicItemId=d.id");
        sb.append(" where 1=1 ");
        if (condition.getCompanyId() != null) {
            sb.append(" and  a.companyid=? ");
            plist.add(condition.getCompanyId());
        }
        if (condition.getIdIns() != null) {
            sb.append(" and  a.companyid in ? ");
            plist.add(condition.getIdIns());
        }

        if (condition.getState() != null) {
            sb.append(" and b.state=? ");
            plist.add(condition.getState());
        }
        sb.append("GROUP BY  a.code ");
        // SELECT * FROM company WHERE FIND_IN_SET(id, getChildCompany(66))
        ReturnDataUtil data = new ReturnDataUtil();

        List<StatisticsWayNumVo> list = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sb.toString());
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sb.toString() + " limit " + off + "," + condition.getPageSize());

            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            while (rs.next()) {
                StatisticsWayNumVo bean = new StatisticsWayNumVo();
                // bean.setWayId(rs.getLong("wayId"));
                // bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                bean.setWay(rs.getInt("wayNumber"));
                // .setItemId(rs.getLong("itemId"));
                bean.setState(rs.getInt("state"));
                bean.setNum(rs.getInt("num"));
                bean.setFullNum(rs.getInt("fullNum"));
                bean.setItemName(rs.getString("itemName"));
                bean.setAddress(rs.getString("address"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setId(rs.getLong("id"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sb.toString());
                log.info(plist);
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
    public long selectCount(String sql, List<Object> list) {
        return super.selectCountBySql(sql, list);
    }

    @Override
    public VendingMachinesWayBean findByVmCodeAndWayNumber(String vmCode, Integer wayNum) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM vending_machines_way WHERE vendingMachinesCode = '" + vmCode + "'  AND wayNumber = " + wayNum + " ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();

            if (showSql) {
                log.info(sql);
            }
            // 如果有数据，返回true，没数据，返回false
            if (rs.next()) {
                VendingMachinesWayBean bean = new VendingMachinesWayBean();
                bean.setId(rs.getLong("id"));
                bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
                bean.setWayNumber(rs.getInt("wayNumber"));
                return bean;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());

        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Integer findwayNumberByVmCode(String vmCode)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM vending_machines_way WHERE vendingMachinesCode = '" + vmCode + "' ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();

            if (showSql) {
                log.info(sql);
            }
            // 如果有数据，返回true，没数据，返回false
            while (rs.next()) {
                count++;
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());

        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);

                return count;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count;

    }

	@Override
	public Integer findItemCoutItem(String vmCode,Integer versions) {
		log.info("<VendingMachinesWayDaoImpl>-----<findItemCoutItem>-----start");
		StringBuilder sql = new StringBuilder();
		if(versions==1) {
			sql.append(" select * from  vending_machines_way  where  vendingMachinesCode='"+vmCode+"'  ");
		}
		if(versions==2) {
			 sql.append("select * from  vending_machines_way_item where vmCode='"+vmCode+"' "); 
		}
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    log.info("查询售货机商品种类 sql语句="+sql.toString());
	    int coutItem=0;
	    try {
	        conn = openConnection();
	        pst = conn.prepareStatement(sql.toString());
	        rs = pst.executeQuery();
	        while (rs.next()) {
	        	coutItem++;
	        }
	        log.info("<VendingMachinesWayDaoImpl>-----<findItemCoutItem>-----end");
        	return coutItem;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        log.error(e.getMessage());
	    } finally {
	        this.closeConnection(rs, pst, conn);
	    }
		log.info("<VendingMachinesWayDaoImpl>-----<findItemCoutItem>-----end");
	    return coutItem;
	}


}
