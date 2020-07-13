package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersProduct;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;

/**
 * author name: why create time: 2018-11-03 16:25:36
 */
@Repository
public class CarryWaterVouchersProductDaoImpl extends BaseDao<CarryWaterVouchersProductBean>
		implements CarryWaterVouchersProductDao {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersProductDaoImpl.class);

	public ReturnDataUtil listPage(CarryWaterVouchersProductForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,carryId,itemId,createTime,createUser,updateTime,updateUser,deleteFlag from carry_water_vouchers_product where 1=1 ");
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
			List<CarryWaterVouchersProductBean> list = Lists.newArrayList();
			while (rs.next()) {
				CarryWaterVouchersProductBean bean = new CarryWaterVouchersProductBean();
				bean.setId(rs.getLong("id"));
				bean.setCarryId(rs.getLong("carryId"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
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

	public CarryWaterVouchersProductBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		CarryWaterVouchersProductBean entity = new CarryWaterVouchersProductBean();
		return super.del(entity);
	}

	public boolean update(CarryWaterVouchersProductBean entity) {
		return super.update(entity);
	}

	/**
	 * 提水券绑定商品
	 */
	public CarryWaterVouchersProductBean insert(CarryWaterVouchersProductBean entity) {
		log.info("<CarryWaterVouchersProductDaoImpl>--------<insert>------start");
		CarryWaterVouchersProductBean bean = super.insert(entity);
		log.info("<CarryWaterVouchersProductDaoImpl>--------<insert>------end");
		return bean;
	}

	/**
	 * 提水券商品列表
	 */
	public ReturnDataUtil list(CarryWaterVouchersProductForm carryWaterVouchersProductForm) {
		log.info("<CarryWaterVouchersProductDaoImpl>--------<list>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		int carryId = carryWaterVouchersProductForm.getCarryId();
        int isBind=carryWaterVouchersProductForm.getIsBind();
        String name=carryWaterVouchersProductForm.getName();
        sql.append(" select k.* from (SELECT c.id,c.carryId,g.name itemName,g.id AS itemId from item_basic g left join ");
        sql.append(" (select *  from  carry_water_vouchers_product where  deleteFlag=0 and carryId='"+carryId+"' ) c on c.itemId=g.id ) k where 1=1 "); 
        if(isBind==0){//未绑定
            sql.append(" and k.carryId is  null");
        }else{// 1已绑定
            sql.append(" and k.carryId is not null");

        }
        if(StringUtil.isNotBlank(name)){
            sql.append(" and k.itemName like '%"+name.trim()+"%'");
        }
        if (showSql) {
            log.info("提水券商品列表sql语句："+sql.toString());
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<CarryWaterVouchersProductBean> list = Lists.newArrayList();
        try {
        	conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (carryWaterVouchersProductForm.getCurrentPage() - 1) * carryWaterVouchersProductForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + carryWaterVouchersProductForm.getPageSize());
			rs = pst.executeQuery();
			
            while (rs.next()) {
            	CarryWaterVouchersProductBean bean=new CarryWaterVouchersProductBean();
                bean.setCarryId(rs.getLong("carryId"));
                bean.setItemName(rs.getString("itemName"));
                bean.setId(rs.getLong("id"));
                bean.setItemId(rs.getLong("itemId"));
                if(isBind==0)
                    bean.setBindLabel("未绑定");
                else
                    bean.setBindLabel("已绑定");
                list.add(bean);
            }
            data.setCurrentPage(carryWaterVouchersProductForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
            log.info("<CarryWaterVouchersProductDaoImpl>--------<list>------end");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("<CarryWaterVouchersProductDaoImpl>--------<list>------end");
            return data;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
	}
}
