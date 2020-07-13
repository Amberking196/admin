package com.server.module.system.machineManage.advertisingBindMachine;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: yjr
 * create time: 2018-12-24 15:45:31
 */
@Repository
public class VendingAdvertisingMachinesDaoImpl extends BaseDao<VendingAdvertisingMachinesBean> implements VendingAdvertisingMachinesDao {

    private static Log log = LogFactory.getLog(VendingAdvertisingMachinesDaoImpl.class);
    @Autowired
    private CompanyDao companyDao;
    public ReturnDataUtil listPage(VendingAdvertisingMachinesCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,advertisingId,vmCode from vending_advertising_machines where 1=1 ");
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
            List<VendingAdvertisingMachinesBean> list = Lists.newArrayList();
            while (rs.next()) {
                VendingAdvertisingMachinesBean bean = new VendingAdvertisingMachinesBean();
                bean.setId(rs.getLong("id"));
                bean.setAdvertisingId(rs.getLong("advertisingId"));
                bean.setVmCode(rs.getString("vmCode"));
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

    public VendingAdvertisingMachinesBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        VendingAdvertisingMachinesBean entity = new VendingAdvertisingMachinesBean();
        return super.del(entity);
    }

    public boolean update(VendingAdvertisingMachinesBean entity) {
        return super.update(entity);
    }

    public VendingAdvertisingMachinesBean insert(VendingAdvertisingMachinesBean entity) {
        return super.insert(entity);
    }

    @Override
    public void addAll(Long advertisingId, List<String> codeList) {
        for (int i = 0; i < codeList.size(); i++) {
            VendingAdvertisingMachinesBean entity = new VendingAdvertisingMachinesBean();
            entity.setAdvertisingId(advertisingId);
            entity.setVmCode(codeList.get(i));
            super.insert(entity);
        }
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            if(id==null)
                continue;
            VendingAdvertisingMachinesBean entity = new VendingAdvertisingMachinesBean();
            entity.setId(id);
            super.del(entity);
        }
    }

    public List<VendingAdvertisingMachinesBean> list(VendingAdvertisingMachinesCondition condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.code as vmCode,a.id,a.advertisingId,i.locatoinName as address FROM vending_machines_info i LEFT JOIN (select * from vending_advertising_machines where 1=1 and advertisingId="+condition.getAdvertisingId()+") a ON i.code=a.vmCode where 1=1 and i.state=20001 ");

        //(SELECT * FROM vending_advertising_machines WHERE advertisingId=1) a
        /*if(condition.getAdvertisingId()!=null){
            sql.append(" and advertisingId="+condition.getAdvertisingId());
        }*/
        if(StringUtil.isNotBlank(condition.getVmCode())){
            sql.append(" and i.code='"+condition.getVmCode()+"'");
        }else{
            if(condition.getAreaId()!=null){
                sql.append(" and i.areaId="+condition.getAreaId());
            }else{
                if(condition.getCompanyId()!=null){
                    String sqlIn=companyDao.findAllSonCompanyIdForInSql(condition.getCompanyId());
                    sql.append(" and i.companyId in "+sqlIn);
                }
            }
        }
        List<VendingAdvertisingMachinesBean> list = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                VendingAdvertisingMachinesBean bean = new VendingAdvertisingMachinesBean();
                bean.setId(rs.getLong("id"));
                bean.setAdvertisingId(rs.getLong("advertisingId"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setAddress(rs.getString("address"));
                if(condition.getRange()==0)
                   list.add(bean);
                else if(condition.getRange()==1){
                    if(bean.getId()!=0)
                        list.add(bean);
                }else{
                    if(bean.getId()==0){
                        list.add(bean);
                    }
                }
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
}

