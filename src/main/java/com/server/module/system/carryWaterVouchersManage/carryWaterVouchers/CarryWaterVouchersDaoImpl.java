package com.server.module.system.carryWaterVouchersManage.carryWaterVouchers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.product.ShoppingGoodsVmCodeForm;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.ShoppingGoodsTypeEnum;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
@Repository
public class CarryWaterVouchersDaoImpl extends BaseDao<CarryWaterVouchersBean> implements CarryWaterVouchersDao {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;

	/**
	 * 提水券列表
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersForm carryWaterVouchersForm) {
		log.info("<CarryWaterVouchersDaoImpl>-------<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select w.id,w.name as carryWaterName,w.target,w.companyId,w.areaId,w.areaName,w.companyName,w.vmCode,w.bindProduct,w.sendMax,w.startTime, ");
		sql.append(" w.endTime,w.createTime,w.createUser,w.updateTime,w.updateUser,w.deleteFlag,w.pic,w.remark,w.periodType,w.periodDay,w.type, ");
		sql.append(" s.name,s.state,s.quantity,s.isHomeShow,s.id shoppingGoodsId,s.quantity,s.costPrice,s.salesPrice,s.details,s.commodityParameters,s.purchaseNotes,s.isHelpOneself,s.purchaseLimitation ");
		sql.append(" from carry_water_vouchers  w  left join shopping_goods s on w.id=s.vouchersId where w.deleteFlag=0 ");
		
		if(StringUtil.isNotBlank(carryWaterVouchersForm.getName())) {
			sql.append(" and w.name like '%"+carryWaterVouchersForm.getName().trim()+"%' ");
		}
		if(carryWaterVouchersForm.getCompanyId()!=null) {
			sql.append(" and w.companyId ='"+carryWaterVouchersForm.getCompanyId()+"' ");
		}
		if(StringUtil.isNotBlank(carryWaterVouchersForm.getVmCode())) {
			sql.append(" and w.vmCode='"+carryWaterVouchersForm.getVmCode()+"' ");
		}
		if(carryWaterVouchersForm.getState()!=null) {
			sql.append(" and s.state='"+carryWaterVouchersForm.getState()+"' ");
		}
		if(carryWaterVouchersForm.getType()!=null) {
			sql.append(" and w.type ='"+carryWaterVouchersForm.getType()+"' ");
		}
		// 权限控制
		sql.append(" and (w.target = 1 and w.companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or w.target = 2 and w.companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or w.target = 3 and w.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");

		sql.append(" order by w.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("提水券列表SQL语句：" + sql.toString());
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (carryWaterVouchersForm.getCurrentPage() - 1) * carryWaterVouchersForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + carryWaterVouchersForm.getPageSize());
			rs = pst.executeQuery();
			List<CarryWaterVouchersBean> list = Lists.newArrayList();
			while (rs.next()) {
				CarryWaterVouchersBean bean = new CarryWaterVouchersBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCarryWaterName(rs.getString("carryWaterName"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setPic(rs.getString("pic"));
				bean.setRemark(rs.getString("remark"));
				bean.setIsHomeShow(rs.getInt("isHomeShow"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setState(rs.getLong("state"));
				bean.setShoppingGoodsId(rs.getLong("shoppingGoodsId"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setDetails(rs.getString("details"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setPeriodType(rs.getInt("periodType"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setType(rs.getInt("type"));
				bean.setPurchaseLimitation(rs.getInt("purchaseLimitation"));
				list.add(bean);
			}
			data.setCurrentPage(carryWaterVouchersForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CarryWaterVouchersDaoImpl>-------<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersDaoImpl>-------<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public CarryWaterVouchersBean get(Object id) {
		log.info("<CarryWaterVouchersDaoImpl>-------<get>------start");
		StringBuilder sql = new StringBuilder();
		sql.append("select w.id,w.name,w.target,w.companyId,w.areaId,w.areaName,w.companyName,w.vmCode,w.bindProduct,w.sendMax,w.startTime, ");
		sql.append(" w.endTime,w.createTime,w.createUser,w.updateTime,w.updateUser,w.deleteFlag,w.pic,w.remark,w.periodType,w.periodDay ");
		sql.append(" from  carry_water_vouchers  w  where w.id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		CarryWaterVouchersBean bean=null;
		log.info("获取提水券信息sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new CarryWaterVouchersBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setPic(rs.getString("pic"));
				bean.setRemark(rs.getString("remark"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setPeriodType(rs.getInt("periodType"));
			}
			log.info("<CarryWaterVouchersDaoImpl>-------<get>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersDaoImpl>-------<get>----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 提水券删除
	 */
	public boolean delete(Object id) {
		log.info("<CarryWaterVouchersDaoImpl>-----<delete>-----end");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  carry_water_vouchers set deleteFlag= 1 where id='" + id + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("提水券删除sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<CarryWaterVouchersDaoImpl>-----<delete>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}

	/**
	 * 提水券修改
	 */
	public boolean update(CarryWaterVouchersBean entity) {
		log.info("<CarryWaterVouchersDaoImpl>-------<update>------start");
		boolean update = super.update(entity);
		log.info("<CarryWaterVouchersDaoImpl>-------<update>------end");
		return update;
	}

	/**
	 * 增加提水券
	 */
	public CarryWaterVouchersBean insert(CarryWaterVouchersBean entity) {
		log.info("<CarryWaterVouchersDaoImpl>-------<insert>------start");
		CarryWaterVouchersBean bean = super.insert(entity);
		log.info("<CarryWaterVouchersDaoImpl>-------<insert>------end");
		return bean;
	}

	public List<CarryWaterVouchersBean> list(CarryWaterVouchersForm condition) {
		return null;
	}
	
	public ReturnDataUtil queryBindCarryWater(ShoppingGoodsVmCodeForm shoppingGoodsVmCodeForm) {
		log.info("<CarryWaterVouchersDaoImpl>-------<queryBindCarryWater>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		if(shoppingGoodsVmCodeForm.getTypeId()!=ShoppingGoodsTypeEnum.MEAL.getIndex()) {//除套餐类型
			if(shoppingGoodsVmCodeForm.getIsBind()==1) {
				sql.append(" select w.id,w.name,w.target,w.companyId,w.areaId,w.areaName,w.companyName,w.vmCode,w.bindProduct,w.sendMax,w.startTime, ");
				sql.append(" w.endTime,w.createTime,w.createUser,w.updateTime,w.updateUser,w.deleteFlag,w.pic,w.remark,w.periodType,w.periodDay,w.type");
				sql.append(" from shopping_goods sg  inner join carry_water_vouchers w on sg.vouchersId=w.id where 1=1 ");
				if(StringUtils.isNotBlank(shoppingGoodsVmCodeForm.getName())) {
					sql.append(" and w.name like '%"+shoppingGoodsVmCodeForm.getName()+"%' ");
				}
				sql.append(" and sg.id = "+shoppingGoodsVmCodeForm.getId()+" order by createTime desc");
			}else if(shoppingGoodsVmCodeForm.getIsBind()==0) {
				sql.append(" select * from carry_water_vouchers w where 1=1 and deleteFlag = 0 and w.type=1");
				if(StringUtils.isNotBlank(shoppingGoodsVmCodeForm.getName())) {
					sql.append(" and w.name like '%"+shoppingGoodsVmCodeForm.getName()+"%'" );
				}
				sql.append(" and id not in (select id from carry_water_vouchers where find_in_set(id,(select vouchersId from shopping_goods where id="+shoppingGoodsVmCodeForm.getId()+")))  order by createTime desc ");
			}
		}else {
			if(shoppingGoodsVmCodeForm.getIsBind()==1) {
				sql.append(" select * from carry_water_vouchers w where 1=1 ");
				sql.append(" and FIND_IN_SET(id,(select vouchersIds from shopping_goods where id="+shoppingGoodsVmCodeForm.getId()+")) ");
				if(StringUtils.isNotBlank(shoppingGoodsVmCodeForm.getName())) {
					sql.append(" and w.name like '%"+shoppingGoodsVmCodeForm.getName()+"%'"  );
				}
				sql.append(" order by createTime desc");
			}
			else if(shoppingGoodsVmCodeForm.getIsBind()==0) {
				sql.append(" select * from carry_water_vouchers w where 1=1 and deleteFlag = 0 and w.type=1 ");
				if(StringUtils.isNotBlank(shoppingGoodsVmCodeForm.getName())) {
					sql.append(" and w.name like '%"+shoppingGoodsVmCodeForm.getName()+"%'");
				}
				sql.append(" and id not in (select id from carry_water_vouchers where find_in_set(id,(select vouchersIds from shopping_goods where id="+shoppingGoodsVmCodeForm.getId()+")))  order by createTime desc");
			}
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("绑定提水券列表SQL语句：" + sql.toString());
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (shoppingGoodsVmCodeForm.getCurrentPage() - 1) * shoppingGoodsVmCodeForm.getPageSize();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<CarryWaterVouchersBean> list = Lists.newArrayList();
			while (rs.next()) {
				CarryWaterVouchersBean bean = new CarryWaterVouchersBean();
				bean.setId(rs.getLong("id"));
				bean.setCarryWaterName(rs.getString("name"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setPeriodType(rs.getInt("periodType"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setPic(rs.getString("pic"));
				bean.setRemark(rs.getString("remark"));
				bean.setType(rs.getInt("type"));
				bean.setIsBind(shoppingGoodsVmCodeForm.getIsBind());
				list.add(bean);
			}
			data.setCurrentPage(shoppingGoodsVmCodeForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CarryWaterVouchersDaoImpl>-------<queryBindCarryWater>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersDaoImpl>-------<queryBindCarryWater>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
}
