package com.server.module.system.machineManage.machineReplenish;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.userManage.CustomerDaoImpl;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MachineReplenishDaoImpl extends BaseDao implements MachineReplenishDao {

    public static Logger log = LogManager.getLogger(CustomerDaoImpl.class);

    @Override
    public ReturnDataUtil findMachineReplenishSum(MachineReplenishForm machineReplenishForm) {
        log.info("<MachineReplenishDaoImpl--findMachineReplenishSum--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuffer sql = new StringBuffer();
        sql.append(" select temp.vmcode, temp.door1sum, temp.door2sum, temp.door3sum, temp.door4sum , group_concat(waynumbername order by temp.waynumbername) as name from ( select a.vendingmachinescode as vmcode, e.door1sum, e.door2sum, e.door3sum, e.door4sum , concat_ws(':', a.waynumber, c.name) as waynumbername from vending_machines_way a, vending_machines_item b, item_basic c, replenish_record d, ( select vm2.vmcode, sum(case  when vm2.waynumber = 1 then vm2.num - vm2.prenum else 0 end) as door1sum, sum(case  when vm2.waynumber = 2 then vm2.num - vm2.prenum else 0 end) as door2sum , sum(case  when vm2.waynumber = 3 then vm2.num - vm2.prenum else 0 end) as door3sum, sum(case  when vm2.waynumber = 4 then vm2.num - vm2.prenum else 0 end) as door4sum from replenish_record vm2,vending_machines_info i where vm2.vmCode=i.code and vm2.optype = 2    ");

       // if (machineReplenishForm.getCompanyId() == 1) {
        if (machineReplenishForm.getCompanyId() != null) {
        	//sql.append(" and vmCode in (SELECT vwl.vmCode FROM replenish_record AS vwl LEFT JOIN vending_machines_info AS vmi ON vwl.vmCode = vmi.code WHERE vmi.companyId IN(" + machineReplenishForm.getCompanyId() + "))");
        	sql.append(" and i.companyId="+machineReplenishForm.getCompanyId());
        } else {
        	sql.append(" and i.companyId in ("+machineReplenishForm.getCompany()+")");
            //sql.append(" and vmCode in (SELECT vwl.vmCode FROM replenish_record AS vwl LEFT JOIN vending_machines_info AS vmi ON vwl.vmCode = vmi.code WHERE vmi.companyId IN(" + machineReplenishForm.getCompany() + "))");
        }
        if(machineReplenishForm.getAreaId()!=null) {//根据区域id查询
        	sql.append(" and i.areaId="+machineReplenishForm.getAreaId());
        	//sql.append(" and vmCode in (SELECT vw2.vmCode FROM replenish_record AS vw2 LEFT JOIN vending_machines_info AS vmi1 ON vw2.vmCode = vmi1.code WHERE vmi1.areaId IN(" + machineReplenishForm.getAreaId() + "))");
        	
        }
        if (machineReplenishForm.getStartDate() != null) {
            sql.append(" and vm2.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(machineReplenishForm.getStartDate()) + "'");
        }
        if (machineReplenishForm.getEndDate() != null) {
            sql.append(" and vm2.createTime<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machineReplenishForm.getEndDate(), 1) + "'");
        }
        if (machineReplenishForm.getVmCode() != null) {
            sql.append(" and vm2.vmCode='" + machineReplenishForm.getVmCode() + "'");
        }
        sql.append("  group by vm2.vmcode ) e where (a.itemid = b.id and c.id = b.basicitemid and a.vendingmachinescode = d.vmcode and a.waynumber = d.waynumber and a.vendingmachinescode = e.vmcode and d.vmcode = e.vmcode) group by a.vendingmachinescode, d.waynumber ) temp where 1 = 1 group by temp.vmcode order by temp.vmcode");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("sql>>>:" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getRow();
            }
            long off = (machineReplenishForm.getCurrentPage() - 1) * machineReplenishForm.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + machineReplenishForm.getPageSize());
            rs = pst.executeQuery();
            List<MachineReplenishBean> list = Lists.newArrayList();

            while (rs.next()) {

                Map<Object, Object> itemNames = new HashMap();

                MachineReplenishBean bean = new MachineReplenishBean();

                bean.setVmCode(rs.getString("vmCode"));
                bean.setDoor1Num(rs.getInt("door1Sum"));
                bean.setDoor2Num(rs.getInt("door2Sum"));
                bean.setDoor3Num(rs.getInt("door3Sum"));
                bean.setDoor4Num(rs.getInt("door4Sum"));

                // 解析名字
                String names = rs.getString("name");
                String[] strings = names.split(",");

                for (String string : strings) {

                    String[] split = string.split(":");
                    switch (split[0]) {
                        case "1" :
                            split[0] = "one";
                            itemNames.put(split[0], split[1]);
                            break;
                        case "2" :
                            split[0] = "two";
                            itemNames.put(split[0], split[1]);
                            break;
                        case "3" :
                            split[0] = "three";
                            itemNames.put(split[0], split[1]);
                            break;
                        case "4" :
                            split[0] = "four";
                            itemNames.put(split[0], split[1]);
                            break;

                    }
                }

                bean.setMap(itemNames);
                list.add(bean);
            }
            data.setCurrentPage(machineReplenishForm.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            data.setMessage("查询成功");
            log.info("<MachineReplenishDaoImpl--findMachineReplenishSum----end>");
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    public ReturnDataUtil findMachineReplenishDetile(MachineReplenishForm machineReplenishForm) {
        log.info("<MachineReplenishDaoImpl--findMachineReplenishDetile--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuffer sql = new StringBuffer();
        sql.append(" select li.name  as operateName, vw.createTime,ib.name,(num-preNum) as cc from replenish_record as vw,item_basic as ib,login_info li where ib.id=vw.basicItemId and vw.opType=2 and li.id = vw.userId and (num-preNum)!=0 and vw.vmcode='" + machineReplenishForm.getVmCode() + "' and vw.wayNumber=" + machineReplenishForm.getWayNumber());
        if (machineReplenishForm.getStartDate() != null) {
            sql.append(" and vw.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(machineReplenishForm.getStartDate()) + "'");
        }
        if (machineReplenishForm.getEndDate() != null) {
            sql.append(" and vw.createTime<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machineReplenishForm.getEndDate(), 1) + "'");
        }
        sql.append(" GROUP BY vw.createTime order by vw.createTime desc ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("sql>>>:" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getRow();
            }
            long off = (machineReplenishForm.getCurrentPage() - 1) * machineReplenishForm.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + machineReplenishForm.getPageSize());
            rs = pst.executeQuery();
            List<MachineReplenishDto> list = Lists.newArrayList();
            int id = 0;
            while (rs.next()) {
                id++;
                MachineReplenishDto bean = new MachineReplenishDto();
                bean = new MachineReplenishDto();
                bean.setCreateTime(rs.getTimestamp("createTime"));
                bean.setName(rs.getString("name"));
                bean.setNum(rs.getInt("cc"));
                bean.setOperateName(rs.getString("operateName"));
                list.add(bean);
            }
            data.setCurrentPage(machineReplenishForm.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            data.setMessage("查询成功");
            log.info("<MachineReplenishDaoImpl--findMachineReplenishDetile----end>");
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    public ReturnDataUtil exportExcel(MachineReplenishForm machineReplenishForm) {

        log.info("<MachineReplenishDaoImpl--exportExcel--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuffer sql = new StringBuffer();
       // sql.append(" select vw.vmcode, vw.waynumber, vw.createtime, ib.name , num - prenum as cc from vending_waynum_log vw, item_basic ib where (ib.id = vw.basicitemid and vw.optype = 2 )  ");
        sql.append(" select vw.vmcode, vw.waynumber, vw.createtime, ib.name , num - prenum as cc from replenish_record vw, item_basic ib,login_info li where (ib.id = vw.basicitemid and vw.optype = 2 and li.id = vw.userId )  ");

        if (machineReplenishForm.getCompanyId() != null) {
        	sql.append(" and vmCode in (SELECT vwl.vmCode FROM replenish_record AS vwl LEFT JOIN vending_machines_info AS vmi ON vwl.vmCode = vmi.code WHERE vmi.companyId IN(" + machineReplenishForm.getCompanyId() + "))");
        	
        } else {
            sql.append(" and vmCode in (SELECT vwl.vmCode FROM replenish_record AS vwl LEFT JOIN vending_machines_info AS vmi ON vwl.vmCode = vmi.code WHERE vmi.companyId IN(" + machineReplenishForm.getCompany() + "))");
        }
        if(machineReplenishForm.getAreaId()!=null) {//根据区域id查询
        	sql.append(" and vmCode in (SELECT vw2.vmCode FROM replenish_record AS vw2 LEFT JOIN vending_machines_info AS vmi1 ON vw2.vmCode = vmi1.code WHERE vmi1.areaId IN(" + machineReplenishForm.getAreaId() + "))");
        	
        }
        if (machineReplenishForm.getStartDate() != null) {
            sql.append(" and vw.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(machineReplenishForm.getStartDate()) + "'");
        }
        if (machineReplenishForm.getEndDate() != null) {
            sql.append(" and vw.createTime<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(machineReplenishForm.getEndDate(), 1) + "'");
        }
        sql.append(" order by vmcode,waynumber,vw.createtime desc ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("sql>>>:" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();

            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();

            List<MachineReplenish2ExcelBean> list = Lists.newArrayList();
            while (rs.next()) {

                MachineReplenish2ExcelBean bean = new MachineReplenish2ExcelBean();

                bean.setVmCode(rs.getString("vmCode"));
                bean.setWayNumber(rs.getString("wayNumber"));
                bean.setItemName(rs.getString("name"));
                bean.setReplenishCount(rs.getString("cc"));
                bean.setReplenishTime(rs.getString("createTime"));
                list.add(bean);
            }

            data.setCurrentPage(machineReplenishForm.getCurrentPage());
            data.setTotal(Long.valueOf(list.size()));
            data.setReturnObject(list);
            data.setStatus(1);
            data.setMessage("查询成功");

            log.info("<MachineReplenishDaoImpl--exportExcel----end>");
            return data;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

}
