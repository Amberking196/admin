package com.server.module.system.couponManager.coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.CustomerUtil;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CouponEnum;

/**
 * author name: yjr create time: 2018-06-28 09:01:06
 */
@Repository
public class CouponDaoImpl extends BaseDao<CouponBean> implements CouponDao {

	private static Logger log = LogManager.getLogger(CouponDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;

	public ReturnDataUtil listPage(CouponForm condition) {
		log.info("<CouponDaoImpl>------<listPage>--------------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,name,way,type,periodType,periodDay,canSend,bindProduct,useWhere,target,companyId,companyName,areaId,areaName,vmCode,money,deductionMoney,sendMax,remark,startTime,endTime,createTime,createUser,updateTime,updateUser,deleteFlag,pic,formulaMode,maximumDiscount from coupon where deleteFlag=0 ");
		List<Object> plist = Lists.newArrayList();

		if (StringUtil.isNotBlank(condition.getName())) {
			sql.append(" and name like '%" + condition.getName().trim() + "%'");
		}
		if (StringUtil.isNotBlank(condition.getCouponIds())) {
			sql.append(" and id in(" + condition.getCouponIds() + ")");
		}
		// 除去可购买优惠券
		if (condition.getWay() == null) {
			sql.append(" and way != "+CouponEnum.BUY_COUPON.getState());
		} else {
			sql.append(" and way = " + condition.getWay());
		}

		// private Integer state;// 0 所有状态 1 已开始 2 未开始 3 已结束
		if (condition.getState() != null) {
			// DateTime dt1 = new DateTime();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sf.format(new Date());

			if (condition.getState() == 1) {
				sql.append(" and startTime<'" + date + "' and endTime>='" + date + "'");
			}
			if (condition.getState() == 2) {
				sql.append(" and startTime>'" + date + "'");
			}
			if (condition.getState() == 3) {
				sql.append(" and endTime<'" + date + "'");
			}
		}
		
		// 权限控制
		sql.append(" and (target = 1 and companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 2 and companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		//判断是否是总部或者优水到家才能查看商城优惠券
		if(UserUtils.getUser().getCompanyId().equals(1)||UserUtils.getUser().getCompanyId().equals(76)) {
			sql.append(" or useWhere=2 ");
		}
		sql.append(" or target = 3 and vmCode in (select code from vending_machines_info vmi where vmi.companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()) + "))");
		

		sql.append(" order by createTime desc ");
		log.info("优惠券列表SQL语句======"+sql.toString());
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
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<CouponBean> list = Lists.newArrayList();
			while (rs.next()) {
				CouponBean bean = new CouponBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setType(rs.getInt("type"));
				bean.setWay(rs.getInt("way"));
				bean.setUseWhere(rs.getInt("useWhere"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setMoney(rs.getDouble("money"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setDeductionMoney(rs.getDouble("deductionMoney"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setRemark(rs.getString("remark"));
				bean.setPic(rs.getString("pic"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setPeriodType(rs.getInt("periodType"));
				bean.setCanSend(rs.getInt("canSend"));
				bean.setFormulaMode(rs.getInt("formulaMode"));
				bean.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				list.add(bean);
			}

			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CouponDaoImpl>------<listPage>--------------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CouponDaoImpl>------<listPage>--------------end");
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

	public CouponBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		CouponBean entity = super.get(id);
		entity.setDeleteFlag(1);
		return super.update(entity);
	}

	public boolean update(CouponBean entity) {
		return super.update(entity);
	}

	public CouponBean insert(CouponBean entity) {
		return super.insert(entity);
	}

	public List<CouponBean> list(CouponForm condition) {
		return null;
	}

	/**
	 * 获取优惠券信息
	 */
	@Override
	public List<CouponBean> getPresentCoupon(CouponForm couponForm) {
		log.info("<CouponDaoImpl>----<getPresentCoupon>----start>");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode,c.maximumDiscount FROM coupon AS c ");
		sql.append(" WHERE c.`way` = "+couponForm.getWay()+" AND c.deleteFlag = 0");
		if(couponForm.getUseWhere()!=null){
			sql.append("  AND c.useWhere = "+couponForm.getUseWhere());
		}
		sql.append(" AND c.canSend = 0");
		sql.append(" AND c.`createTime` <= '"+now+"' AND c.`endTime` >= '"+now+"'");
		log.info("获取优惠券信息 sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		List<CouponBean> couponList = new ArrayList<CouponBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDaoImpl>----<getPresentCoupon>----end>");
		return couponList;
	}

	/**
	 * 获取亚运城活动 5折优惠券信息
	 */
	@Override
	public List<CouponBean> getAsianCoupon() {
		log.info("<CouponDaoImpl>----<getAsianCoupon>----start>");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode,c.maximumDiscount FROM coupon AS c ");
		sql.append(" WHERE c.`way` = 9  AND c.deleteFlag = 0");
		sql.append("  AND c.canSend = 0 ");
		sql.append(" AND c.`createTime` <= '"+now+"' AND c.`endTime` >= '"+now+"'");
		log.info("获取亚运城活动 优惠券信息 sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		List<CouponBean> couponList = new ArrayList<CouponBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDaoImpl>----<getAsianCoupon>----end>");
		return couponList;
	}
	
	/**
	 * 用户是否已经领取
	 */
	@Override
	public boolean isReceive(Long customerId, Long couponId) {
		log.info("<CouponDaoImpl>-----<isReceive>------start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT 1 AS times FROM `coupon_customer` WHERE customerId = "+customerId+" AND couponId = "+couponId);
		log.info("用户是否已经领取sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReceive = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				isReceive = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDaoImpl>-----<isReceive>-----end>");
		return isReceive;
	}
}
