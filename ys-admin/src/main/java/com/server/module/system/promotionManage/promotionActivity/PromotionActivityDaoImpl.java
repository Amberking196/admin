package com.server.module.system.promotionManage.promotionActivity;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.couponManager.couponProduct.CouponProductVo;
import com.server.module.system.promotionManage.timeQuantum.TimeQuantumBean;
import com.server.module.system.promotionManage.timeQuantum.TimeQuantumDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.Date;
import java.util.List;

/**
 * author name: why create time: 2018-08-22 16:51:38
 */
@Repository
public class PromotionActivityDaoImpl extends BaseDao<PromotionActivityBean> implements PromotionActivityDao {

	private static Logger log = LogManager.getLogger(PromotionActivityDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;

	/**
	 * 促销活动列表 查询
	 */
	public ReturnDataUtil listPage(PromotionActivityForm promotionActivityForm) {
		log.info("<PromotionActivityDaoImpl>----<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,name,type,timeQuantumId,startTime,endTime,discountType,money,deductionMoney,target,companyId,areaId,areaName,companyName,vmCode,bindProduct,useWhere,pic,remark,createTime,createUser,updateTime,updateUser,deleteFlag,timeFrame  ");
		sql.append(" from promotion_activity where deleteFlag=0 ");

		if (StringUtil.isNotBlank(promotionActivityForm.getName())) {
			sql.append(" and name like '%" + promotionActivityForm.getName().trim() + "%'");
		}
		if(promotionActivityForm.getCompanyId()!=null) {
			sql.append(" and companyId = '"+promotionActivityForm.getCompanyId()+"' ");
		}
		if(StringUtil.isNotBlank(promotionActivityForm.getVmCode())) {
			sql.append(" and vmCode like '%" + promotionActivityForm.getVmCode().trim() + "%' ");
		}
		// private Integer state;// 0 所有状态 1 已开始 2 未开始 3 已结束
		if (promotionActivityForm.getState() != null) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sf.format(new Date());
			if (promotionActivityForm.getState() == 1) {
				sql.append(" and startTime<'" + date + "' and endTime>='" + date + "'");
			}
			if (promotionActivityForm.getState() == 2) {
				sql.append(" and startTime>'" + date + "'");
			}
			if (promotionActivityForm.getState() == 3) {
				sql.append(" and endTime<'" + date + "'");
			}
		}
		// 权限控制
		sql.append(" and (target = 1 and companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 2 and companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		// 判断是否是总部或者优水到家才能查看商城活动
		if (UserUtils.getUser().getCompanyId().equals(1) || UserUtils.getUser().getCompanyId().equals(76)) {
			sql.append(" or useWhere=2 ");
		}
		sql.append(" or target = 3 and vmCode in (select code from vending_machines_info vmi where vmi.companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()) + "))");
		sql.append(" order by createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("促销活动列表查询 SQL语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (promotionActivityForm.getCurrentPage() - 1) * promotionActivityForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + promotionActivityForm.getPageSize());
			rs = pst.executeQuery();
			List<PromotionActivityBean> list = Lists.newArrayList();
			while (rs.next()) {
				PromotionActivityBean bean = new PromotionActivityBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setType(rs.getInt("type"));
				bean.setTimeQuantumId(rs.getString("timeQuantumId"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setDiscountType(rs.getInt("discountType"));
				bean.setMoney(rs.getDouble("money"));
				bean.setDeductionMoney(rs.getDouble("deductionMoney"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setUseWhere(rs.getInt("useWhere"));
				bean.setPic(rs.getString("pic"));
				bean.setRemark(rs.getString("remark"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setTimeFrame(rs.getString("timeFrame"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(promotionActivityForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<PromotionActivityDaoImpl>----<listPage>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<PromotionActivityDaoImpl>----<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public PromotionActivityBean get(Object id) {
		return super.get(id);
	}

	/**
	 * 删除促销活动
	 */
	public boolean delete(Object id) {
		log.info("<PromotionActivityDaoImpl>----<delete>------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  promotion_activity set deleteFlag= 1 where id='" + id + "' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除时间段sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<PromotionActivityDaoImpl>-----<delete>-----end");
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
	 * 促销活动 修改
	 */
	public boolean update(PromotionActivityBean entity) {
		log.info("<PromotionActivityDaoImpl>----<update>------start");
		boolean update = super.update(entity);
		log.info("<PromotionActivityDaoImpl>----<update>------end");
		return update;
	}

	/**
	 * 促销活动 增加
	 */
	public PromotionActivityBean insert(PromotionActivityBean entity) {
		log.info("<PromotionActivityDaoImpl>----<insert>------start");
		PromotionActivityBean promotionActivityBean = super.insert(entity);
		log.info("<PromotionActivityDaoImpl>----<insert>------end");
		return promotionActivityBean;
	}

	public List<PromotionActivityBean> list(PromotionActivityForm condition) {
		return null;
	}

	/**
	 * 活动商品列表
	 */
	@Override
	public List<CommodityVo> findItem(CommodityForm commodityForm) {
		StringBuilder sql = new StringBuilder();

		int useWhere = commodityForm.getUseWhere();
		int couponId = commodityForm.getActivityId();
		int isBind = commodityForm.getIsBind();
		String name = commodityForm.getName();

		if (useWhere == 1) {// 1 机器活动 2 商城活动
			sql.append("SELECT id,name FROM item_basic    WHERE 1=1 ");
		} else if (useWhere == 2) {
			sql.append("SELECT id,name FROM shopping_goods  WHERE 1=1 ");
		}
		if (isBind == 0) {// 未绑定
			sql.append(" and k.couponId IS  NULL");
		} else {// 1已绑定
			sql.append(" and k.couponId IS not NULL");

		}
		if (StringUtil.isNotBlank(name)) {
			sql.append(" and k.name like '%" + name + "%'");
		}
		if (showSql) {
			log.info(sql);
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<CommodityVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());

			rs = pst.executeQuery();
			while (rs.next()) {
				CommodityVo bean = new CommodityVo();
				bean.setItemId(rs.getLong("id"));
				bean.setItemName(rs.getString("name"));
				list.add(bean);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
}
