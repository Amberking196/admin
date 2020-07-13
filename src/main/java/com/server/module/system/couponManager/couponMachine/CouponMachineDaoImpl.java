package com.server.module.system.couponManager.couponMachine;

import com.google.common.collect.Maps;
import com.server.common.persistence.BaseDao;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.server.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * author name: yjr
 * create time: 2018-06-28 09:11:41
 */
@Repository
public class CouponMachineDaoImpl extends BaseDao<CouponMachineBean> implements CouponMachineDao {

    private static Log log = LogFactory.getLog(CouponMachineDaoImpl.class);
    @Autowired
    private CompanyDao companyDao;
    public ReturnDataUtil listPage(CouponMachineForm condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,couponId,vmCode,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_machine where deleteFlag=0 ");
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
            List<CouponMachineBean> list = Lists.newArrayList();
            while (rs.next()) {
                CouponMachineBean bean = new CouponMachineBean();
                bean.setId(rs.getLong("id"));
                bean.setCouponId(rs.getLong("couponId"));
                bean.setVmCode(rs.getString("vmCode"));
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
    public ReturnDataUtil listPageForAddMachine(CouponMachineForm condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.code,i.`locatoinName`,i.companyId,i.`areaId`,cm.id as couponMachineId from vending_machines_info i LEFT JOIN coupon_machine cm ON i.code=cm.vmCode  WHERE 1=1 ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        if(StringUtil.isNotBlank(condition.getAreaId())){
            sql.append(" and i.areaId="+condition.getAreaId());
        }
        if(StringUtil.isNotBlank(condition.getCompanyId())){
            sql.append(" and i.companyId="+condition.getCompanyId());
        }

        if(StringUtil.isNotBlank(condition.getVmCode())){
            sql.append(" and i.code="+condition.getVmCode());
        }
        if(condition.getAddFlag()==CouponMachineForm.Added){
             sql.append(" AND  EXISTS( SELECT cc.id FROM coupon_customer cc WHERE cc.`couponId`=cm.couponId)");
        }else if(condition.getAddFlag()==CouponMachineForm.NOAdded){
            sql.append(" AND NOT EXISTS( SELECT cc.id FROM coupon_customer cc WHERE cc.`couponId`=cm.couponId)");
        }else{
        }

        if (showSql) {
            log.info(sql);
        }
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
            Map<Long,String> companyMap= Maps.newHashMap();
            List<CouponMachineInfoVo> list = Lists.newArrayList();
            while (rs.next()) {
                CouponMachineInfoVo vo = new CouponMachineInfoVo();
                //i.code,i.`locatoinName`,i.companyId,i.`areaId`,cm.id as couponMachineId
                vo.setCode(rs.getString("code"));
                vo.setCompanyId(rs.getLong("companyId"));
                vo.setAreaId(rs.getLong("areaId"));
                vo.setLocatoinName(rs.getString("locatoinName"));
                vo.setCouponMachineId(rs.getInt("couponMachineId"));
                if(companyMap.get(vo.getCompanyId())==null){
                    CompanyBean companyBean=companyDao.findCompanyById(vo.getCompanyId().intValue());
                    if(companyBean!=null)
                    companyMap.put(vo.getCompanyId(),companyBean.getName());
                    else{
                        companyMap.put(vo.getCompanyId(),"无名公司");
                    }
                }
                vo.setCompanyName(companyMap.get(vo.getCompanyId()));
                if(vo.getCouponMachineId()==0){
                    vo.setAddLabel("未添加");
                }else{
                    vo.setAddLabel("已添加");
                }
                list.add(vo);
            }

            data.setCurrentPage(condition.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            return data;
        } catch (Exception e) {
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

    public CouponMachineBean get(Integer id) {
        return super.get(id);
    }

    public boolean delete(Integer id) {
        CouponMachineBean entity = new CouponMachineBean();
        entity.setDeleteFlag(1);
        return super.update(entity);
    }

    public boolean update(CouponMachineBean entity) {
        return super.update(entity);
    }

    public CouponMachineBean insert(CouponMachineBean entity) {
        return super.insert(entity);
    }

    public List<CouponMachineBean> list(CouponMachineForm condition) {
        return null;
    }

    @Override
    public List<String> selectAllVmCodeByCoupon(Long couponId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select vmCode from coupon_machine where couponId="+couponId);
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        if (showSql) {
            log.info(sql);
        }
        List<String> list = Lists.newArrayList();

        try {
            conn = openConnection();
            pst=conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("vmCode"));
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

    @Override
    public List<String> allVmCodeByCompanyIdOrAreaId(String companyId, String areaId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select code from vending_machines_info where state=20001 and companyId="+companyId);

        if(StringUtil.isNotBlank(areaId)){
            sql.append(" and areaId="+areaId);
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        if (showSql) {
            log.info(sql);
        }
        List<String> list = Lists.newArrayList();

        try {
            conn = openConnection();
            pst=conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("code"));
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

