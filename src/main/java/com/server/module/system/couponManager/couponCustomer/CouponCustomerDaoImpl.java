package com.server.module.system.couponManager.couponCustomer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.couponManager.coupon.AddAllCustomerForm;
import com.server.module.system.couponManager.coupon.CouponBean;
import com.server.module.system.couponManager.coupon.CouponDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: yjr create time: 2018-06-28 09:15:49
 */
@Repository
public class CouponCustomerDaoImpl extends BaseDao<CouponCustomerBean> implements CouponCustomerDao {

	private static Logger log = LogManager.getLogger(CouponCustomerDaoImpl.class);
	@Autowired
	CouponDao couponDao;
	@Autowired
	private CompanyDao companyDao;

	public ReturnDataUtil listPage(CouponCustomerForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,couponId,customerId,state,createTime,createUser,updateTime,updateUser,deleteFlag from coupon_customer where deleteFlag=0 ");
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
			List<CouponCustomerBean> list = Lists.newArrayList();
			while (rs.next()) {
				CouponCustomerBean bean = new CouponCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setCouponId(rs.getLong("couponId"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setState(rs.getInt("state"));
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

	public CouponCustomerBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		CouponCustomerBean entity = new CouponCustomerBean();
		entity.setDeleteFlag(1);
		return super.update(entity);
	}

	public boolean update(CouponCustomerBean entity) {
		return super.update(entity);
	}

	public CouponCustomerBean insert(CouponCustomerBean entity) {
		return super.insert(entity);
	}

	public List<CouponCustomerBean> list(CouponCustomerForm condition) {
		return null;
	}

	@Override
	public ReturnDataUtil listPageForCustomer(CouponCustomerForm condition) {
		log.info("<CouponCustomerDaoImpl>-----<listPageForCustomer>------start");
		CouponBean coupon = couponDao.get(condition.getCouponId());
		List<Integer> companyList = companyDao.findAllSonCompanyId(coupon.getCompanyId().intValue());
		String companyIds = StringUtils.join(companyList, ",");
		int useWhere = coupon.getUseWhere();
		int target = coupon.getTarget();
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		String select = "SELECT c.phone,c.vmCode,c.createTime,c.id as customerId,cc.id as couponCustomerId from ";
		sql.append(select);
		if (useWhere == 1) {// 机器劵

			if (target == 1) {// 公司

				// sql.append(" tbl_customer c INNER JOIN tbl_customer_wx cw ON
				// c.id=cw.customerId LEFT JOIN coupon_customer cc ON (c.id=cc.customerId and
				// cc.couponId="+condition.getCouponId()+") WHERE cw.companyId in
				// ("+companyIds+")");
				sql.append(
						" vending_machines_info i INNER JOIN tbl_customer c ON i.code=c.vmCode LEFT JOIN coupon_customer cc ON (c.id=cc.customerId and cc.couponId="
								+ condition.getCouponId() + ") WHERE i.companyId in (" + companyIds + ")");

			} else if (target == 2) {//
				sql.append(
						" vending_machines_info i INNER JOIN tbl_customer c ON i.code=c.vmCode LEFT JOIN coupon_customer cc ON (c.id=cc.customerId and cc.couponId="
								+ condition.getCouponId() + ") WHERE i.areaId=" + coupon.getAreaId());
			} else {// 3
				sql.append(" tbl_customer c LEFT JOIN coupon_customer cc ON (c.id=cc.customerId and cc.couponId="
						+ condition.getCouponId() + ")   WHERE c.`vmCode`='" + coupon.getVmCode() + "'");
			}
			sql.append("");
		} else {// 商城劵
			sql.append(" tbl_customer c LEFT JOIN coupon_customer cc ON (c.id=cc.customerId and cc.couponId="
					+ condition.getCouponId() + ")  WHERE 1=1 ");
		}
		sql.append(" and c.phone is not null ");
		if (StringUtil.isNotBlank(condition.getPhone())) {
			sql.append(" and c.phone=" + condition.getPhone());
		}

		if (condition.getAddFlag() == CouponCustomerForm.Added) {
			sql.append(" AND EXISTS(SELECT m.id FROM coupon_customer m WHERE c.id=m.customerId AND m.`couponId`="
					+ condition.getCouponId() + ")");
		} else if (condition.getAddFlag() == CouponCustomerForm.NOAdded) {
			sql.append(" AND NOT EXISTS(SELECT m.id FROM coupon_customer m WHERE c.id=m.customerId AND m.`couponId`="
					+ condition.getCouponId() + ")");
		} else {
		}
		if (showSql) {
			log.info("会员列表：" + sql.toString());
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
			List<CustomerVo> list = Lists.newArrayList();
			while (rs.next()) {
				CustomerVo vo = new CustomerVo();
				vo.setCouponId(Long.parseLong(condition.getCouponId()));
				vo.setCustomerId(rs.getLong("customerId"));
				vo.setCreateTime(rs.getTimestamp("createTime"));
				vo.setPhone(rs.getString("phone"));
				vo.setCouponCustomerId(rs.getLong("couponCustomerId"));

				if (vo.getCouponCustomerId() == 0) {
					vo.setAddLabel("未发送");
				} else {
					vo.setAddLabel("已发送");
				}

				list.add(vo);
			}

			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CouponCustomerDaoImpl>-----<listPageForCustomer>------end");
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
	public List<Long> getAllCustomerId(AddAllCustomerForm condition) {
		// SELECT * FROM coupon_machine m INNER JOIN tbl_customer c ON m.vmCode=c.vmCode
		// WHERE couponId

		CouponBean coupon = couponDao.get(condition.getCouponId());
		List<Integer> companyList = companyDao.findAllSonCompanyId(coupon.getCompanyId().intValue());
		String companyIds = StringUtils.join(companyList, ",");
		int useWhere = coupon.getUseWhere();
		int target = coupon.getTarget();
		StringBuilder sql = new StringBuilder();
		String select = "SELECT c.id FROM tbl_customer c inner join vending_machines_info i on c.vmCode=i.code where 1=1 and  not exists (select cc.customerId from coupon_customer cc where c.id=cc.customerId and couponId='"
				+ condition.getCouponId() + "' )";
		sql.append(select);
		if (useWhere == 1) {// 机器劵

			if (target == 1) {// 公司
				sql.append(" and i.companyId in (" + companyIds + ") ");

			} else if (target == 2) {//
				sql.append(" and i.areaId=" + coupon.getAreaId());
			} else if (target == 3) {// 3
				sql.append(" and i.code='" + coupon.getVmCode() + "'");
			}
		} else {// 商城劵
			if (target == 1) {// 公司
				sql.append(" and i.companyId=" + coupon.getCompanyId());

			} else if (target == 2) {//
				sql.append(" and i.areaId=" + coupon.getAreaId());
			} else if (target == 3) {// 3
				sql.append(" and i.code='" + coupon.getVmCode() + "'");
			}
		}

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info(sql);
		}
		List<Long> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {

				list.add(rs.getLong("id"));
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
	public void batchInsertSql(List<String> sqls) {

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = openConnection();
			pst = conn.prepareStatement("");
			rs = pst.executeQuery();

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
	}

	@Override
	public int batchInsert(List<Long> customerLists, Long couponId) {

		Long userId = UserUtils.getUser().getId();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		long start = System.currentTimeMillis();
		CouponBean coupon = couponDao.get(couponId);

		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(
					"insert into coupon_customer(couponId,customerId,state,createUser,startTime,endTime,quantity) values (?,?,?,?,?,?,?)");
			int batchSize = 50;

			for (int i = 0; i < customerLists.size(); i++) {

				Long id = customerLists.get(i);

				pst.setLong(1, couponId);
				pst.setLong(2, id);
				pst.setInt(3, CouponCustomerBean.STATE_GET);
				pst.setLong(4, userId);
				pst.setTimestamp(5, new java.sql.Timestamp(coupon.getStartTime().getTime()));
				pst.setTimestamp(6, new java.sql.Timestamp(coupon.getEndTime().getTime()));
				pst.setLong(7, coupon.getSendMax().longValue());
				pst.addBatch();
				if (i % batchSize == 0) {
					pst.executeBatch();
					conn.commit();
					pst.clearBatch();
				}

			}

			pst.executeBatch();
			conn.commit();
			System.out.println("批量插入耗费时间：" + (System.currentTimeMillis() - start) + "   数据量：" + customerLists.size());
			return 1;

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			log.error(e.getMessage());
			return 0;
		} finally {
			try {
				// rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<CustomerCouponVo> listByCouponId(int couponId) {
		String sql = "SELECT c.state,cp.name,cp.money,cp.deductionMoney,cp.startTime,cp.endTime,cp.type,cp.way FROM coupon_customer c INNER JOIN coupon cp ON c.couponId=cp.id where cp.id="
				+ couponId + " order by c.createTime desc";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info(sql);
		}
		List<CustomerCouponVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerCouponVo vo = new CustomerCouponVo();
				vo.setDeductionMoney(rs.getDouble("deductionMoney"));
				vo.setEndTime(rs.getTimestamp("endTime"));
				vo.setMoney(rs.getDouble("money"));
				vo.setName(rs.getString("name"));
				vo.setState(rs.getInt("state"));
				vo.setWay(rs.getInt("way"));
				vo.setType(rs.getInt("type"));
				list.add(vo);
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
	public List<CustomerCouponVo> list(int customerId) {
		String sql = "SELECT c.state,cp.name,cp.money,cp.deductionMoney,c.startTime,c.endTime,cp.createTime,cp.type,cp.way,cp.maximumDiscount FROM coupon_customer c INNER JOIN coupon cp ON c.couponId=cp.id where  c.deleteFlag=0  and c.customerId="
				+ customerId + "   order by c.createTime desc";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info(sql);
		}
		List<CustomerCouponVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerCouponVo vo = new CustomerCouponVo();
				vo.setDeductionMoney(rs.getDouble("deductionMoney"));
				vo.setEndTime(rs.getTimestamp("endTime"));
				vo.setMoney(rs.getDouble("money"));
				vo.setName(rs.getString("name"));
				vo.setState(rs.getInt("state"));
				vo.setWay(rs.getInt("way"));
				vo.setType(rs.getInt("type"));
				vo.setStartTime(rs.getTimestamp("startTime"));
				vo.setCreateTime(rs.getTimestamp("createTime"));
				vo.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				list.add(vo);
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
	public List<CustomerCouponVo> list(String vmCode) {
		String sql = "SELECT c.vmCode,c.state,cp.name,cp.money,cp.deductionMoney,cp.startTime,cp.endTime,cp.type,cp.way FROM coupon_customer c INNER JOIN coupon cp ON c.couponId=cp.id where c.vmCode="
				+ vmCode + " order by c.createTime desc";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info(sql);
		}
		List<CustomerCouponVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				CustomerCouponVo vo = new CustomerCouponVo();
				vo.setDeductionMoney(rs.getDouble("deductionMoney"));
				vo.setEndTime(rs.getTimestamp("endTime"));
				vo.setMoney(rs.getDouble("money"));
				vo.setName(rs.getString("name"));
				vo.setState(rs.getInt("state"));
				vo.setWay(rs.getInt("way"));
				vo.setType(rs.getInt("type"));
				list.add(vo);
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
	public boolean isHaveCustomer(Long couponId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from coupon_customer where couponId=" + couponId);
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
			if (count > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
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

	public ReturnDataUtil conponCustomerNoteList(ConponCustomerNoteForm conponCustomerNoteForm) {
		log.info("<CouponCustomerDaoImpl>--------<conponCustomerNoteList>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		//查询条件进行查询
		StringBuilder sql = new StringBuilder();
		//查询总条数
		StringBuilder sql1 = new StringBuilder();
		long off = (conponCustomerNoteForm.getCurrentPage() - 1) * conponCustomerNoteForm.getPageSize();
		if (conponCustomerNoteForm.getType() == 1) {// 优惠券
			sql1.append(" select count(1) count from coupon_customer c ");
			sql1.append(" left join tbl_customer t on c.customerId=t.id left join vending_machines_info vmi on t.vmCode=vmi.code  ");
			sql1.append(" left join coupon cc on c.couponId=cc.id where c.endTime>=now() and c.deleteFlag=0 and  c.state<2 ");

			sql.append(" select c.id,c.couponId,c.customerId,c.startTime,c.endTime,c.createTime,c.isSend,c.quantity,t.phone,t.vmCode,cc.name,datediff(c.endTime, now()) days   from coupon_customer  c ");
			sql.append(" left join tbl_customer t on c.customerId=t.id left join vending_machines_info vmi on t.vmCode=vmi.code  ");
			sql.append(" left join coupon cc on c.couponId=cc.id where c.state<2");
			sql.append(" and c.customerId in(select customerId From ( select customerId from coupon_customer c  left join tbl_customer t on c.customerId=t.id  ");
			sql.append(" left join vending_machines_info vmi on t.vmCode=vmi.code  where c.endTime>=now() and c.state<2 and c.deleteFlag=0 ");
			if (conponCustomerNoteForm.getCompanyId() != null) {
				sql.append(" and vmi.companyId='" + conponCustomerNoteForm.getCompanyId() + "'  ");
			}

			if (StringUtil.isNotBlank(conponCustomerNoteForm.getVmCode())) {
				sql.append(" and t.vmCode like '%" + conponCustomerNoteForm.getVmCode().trim() + "%' ");
			}
			if (StringUtil.isNotBlank(conponCustomerNoteForm.getPhone())) {
				sql.append(" and t.phone like '%" + conponCustomerNoteForm.getPhone().trim() + "%' ");
			}
			if (conponCustomerNoteForm.getState() != null) {
				sql.append(" and c.isSend= '" + conponCustomerNoteForm.getState() + "' ");
			}
			if (conponCustomerNoteForm.getStartTime() != null) {
				sql.append(" and c.createTime >= '"+ DateUtil.formatYYYYMMDDHHMMSS(conponCustomerNoteForm.getStartTime()) + "' ");
			}
			if (conponCustomerNoteForm.getEndTime() != null) {
				sql.append(" and c.createTime < '"+ DateUtil.formatLocalYYYYMMDDHHMMSS(conponCustomerNoteForm.getEndTime(), 1) + "' ");
			}
			if (conponCustomerNoteForm.getDays() != null) {
				sql.append(" and DATEDIFF(c.endTime,now()) <=" + conponCustomerNoteForm.getDays() + " ");
			}
			if(conponCustomerNoteForm.getIsShowAll()==1) {
				sql.append(" GROUP BY customerId  ) s) ");
			}else {
				sql.append(" GROUP BY customerId limit " + off + "  , " + conponCustomerNoteForm.getPageSize() + " ) s) ");
			}
		}
		if (conponCustomerNoteForm.getType() == 2) {// 提水券
			sql1.append(" select count(1) count from carry_water_vouchers_customer c left join tbl_customer t on c.customerId=t.id ");
			sql1.append(" left join vending_machines_info vmi on t.vmCode=vmi.code left join carry_water_vouchers cc on c.carryId=cc.id ");
			sql1.append(" where c.endTime>=now() and c.deleteFlag=0 and c.useQuantity<c.quantity ");

			sql.append(" select c.id,c.carryId couponId,c.customerId,c.startTime,c.endTime,c.createTime,c.isSend,t.phone,t.vmCode,cc.name,datediff(c.endTime, now()) days,(c.quantity-c.useQuantity) quantity   from carry_water_vouchers_customer  c ");
			sql.append(" left join tbl_customer t on c.customerId=t.id  left join vending_machines_info vmi on t.vmCode=vmi.code ");
			sql.append(" left join carry_water_vouchers cc on c.carryId=cc.id where c.useQuantity<c.quantity ");
			sql.append(" and c.customerId in(select customerId From ( select customerId from carry_water_vouchers_customer c left join tbl_customer t on c.customerId=t.id   ");
			sql.append(" left join vending_machines_info vmi on t.vmCode=vmi.code ");
			sql.append(" where c.endTime>=now() and c.useQuantity<c.quantity and c.deleteFlag=0  ");
			if (conponCustomerNoteForm.getCompanyId() != null) {
				sql.append(" and vmi.companyId='" + conponCustomerNoteForm.getCompanyId() + "'  ");
			}

			if (StringUtil.isNotBlank(conponCustomerNoteForm.getVmCode())) {
				sql.append(" and t.vmCode like '%" + conponCustomerNoteForm.getVmCode().trim() + "%' ");
			}
			if (StringUtil.isNotBlank(conponCustomerNoteForm.getPhone())) {
				sql.append(" and t.phone like '%" + conponCustomerNoteForm.getPhone().trim() + "%' ");
			}
			if (conponCustomerNoteForm.getState() != null) {
				sql.append(" and c.isSend= '" + conponCustomerNoteForm.getState() + "' ");
			}
			if (conponCustomerNoteForm.getStartTime() != null) {
				sql.append(" and c.createTime >= '"+ DateUtil.formatYYYYMMDDHHMMSS(conponCustomerNoteForm.getStartTime()) + "' ");
			}
			if (conponCustomerNoteForm.getEndTime() != null) {
				sql.append(" and c.createTime < '"+ DateUtil.formatLocalYYYYMMDDHHMMSS(conponCustomerNoteForm.getEndTime(), 1) + "' ");
			}
			if (conponCustomerNoteForm.getDays() != null) {
				sql.append(" and DATEDIFF(c.endTime,now()) <=" + conponCustomerNoteForm.getDays() + " ");
			}
			if(conponCustomerNoteForm.getIsShowAll()==1) {
				sql.append(" GROUP BY customerId  ) s) ");
			}else {
				sql.append(" GROUP BY customerId limit " + off + "  , " + conponCustomerNoteForm.getPageSize() + " ) s) ");
			}
		}
		sql.append(" and c.deleteFlag=0 and c.endTime>=now() ");
		if (conponCustomerNoteForm.getCompanyId() != null) {
			sql.append(" and vmi.companyId='" + conponCustomerNoteForm.getCompanyId() + "'  ");
			sql1.append(" and vmi.companyId='" + conponCustomerNoteForm.getCompanyId() + "'  ");
		}

		if (StringUtil.isNotBlank(conponCustomerNoteForm.getVmCode())) {
			sql.append(" and t.vmCode like '%" + conponCustomerNoteForm.getVmCode().trim() + "%' ");
			sql1.append(" and t.vmCode like '%" + conponCustomerNoteForm.getVmCode().trim() + "%' ");
		}
		if (StringUtil.isNotBlank(conponCustomerNoteForm.getPhone())) {
			sql.append(" and t.phone like '%" + conponCustomerNoteForm.getPhone().trim() + "%' ");
			sql1.append(" and t.phone like '%" + conponCustomerNoteForm.getPhone().trim() + "%' ");
		}
		if (conponCustomerNoteForm.getState() != null) {
			sql.append(" and c.isSend= '" + conponCustomerNoteForm.getState() + "' ");
			sql1.append(" and c.isSend= '" + conponCustomerNoteForm.getState() + "' ");
		}
		if (conponCustomerNoteForm.getStartTime() != null) {
			sql.append(" and c.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(conponCustomerNoteForm.getStartTime())+ "' ");
			sql1.append(" and c.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(conponCustomerNoteForm.getStartTime())+ "' ");
		}
		if (conponCustomerNoteForm.getEndTime() != null) {
			sql.append(" and c.createTime < '"+ DateUtil.formatLocalYYYYMMDDHHMMSS(conponCustomerNoteForm.getEndTime(), 1) + "' ");
			sql1.append(" and c.createTime < '"+ DateUtil.formatLocalYYYYMMDDHHMMSS(conponCustomerNoteForm.getEndTime(), 1) + "' ");
		}
		if (conponCustomerNoteForm.getDays() != null) {
			sql.append(" and DATEDIFF(c.endTime,now()) <=" + conponCustomerNoteForm.getDays() + " ");
			sql1.append(" and DATEDIFF(c.endTime,now()) <=" + conponCustomerNoteForm.getDays() + " ");
		}
		sql.append(" and customerId in (select id from tbl_customer t left join vending_machines_info  vmi on  t.vmCode=vmi.code where vmi.companyId in "+companyDao.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" )");
		sql1.append(" and customerId in (select id from tbl_customer t left join vending_machines_info  vmi on  t.vmCode=vmi.code where vmi.companyId in "+companyDao.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" )");
		sql.append(" order by customerId asc,datediff(c.endTime, now()) asc   ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("券短信提醒列表SQL：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql1.toString());
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt("count");
			}
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<CouponCustomerVo> list = Lists.newArrayList();
			while (rs.next()) {
				CouponCustomerVo bean = new CouponCustomerVo();
				bean.setId(rs.getLong("id"));
				bean.setTicketId(rs.getLong("couponId"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setState(rs.getInt("isSend"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setStartTime(rs.getDate("startTime"));
				bean.setEndTime(rs.getDate("endTime"));
				bean.setPhone(rs.getString("phone"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setName(rs.getString("name"));
				bean.setDays(rs.getInt("days"));
				bean.setQuantity(rs.getInt("quantity"));
				bean.getStateLabel();
				list.add(bean);
			}
			data.setCurrentPage(conponCustomerNoteForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CouponCustomerDaoImpl>--------<conponCustomerNoteList>------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CouponCustomerDaoImpl>--------<conponCustomerNoteList>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public boolean updateIsSendState(ConponCustomerNoteForm conponCustomerNoteForm) {
		log.info("<CouponCustomerDaoImpl>--------<updateIsSendState>------start");
		StringBuilder sql = new StringBuilder();
		if (conponCustomerNoteForm.getType() == 1) {
			sql.append("update  coupon_customer set isSend= 1 where customerId in (" + conponCustomerNoteForm.getId()
					+ ")");
		}
		if (conponCustomerNoteForm.getType() == 2) {
			sql.append("update  carry_water_vouchers_customer set isSend= 1 where customerId in ("
					+ conponCustomerNoteForm.getId() + ")");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("券修改短信状态sql语句：" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<CouponCustomerDaoImpl>--------<updateIsSendState>------end");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CouponCustomerDaoImpl>--------<updateIsSendState>------end");
			return false;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<CouponCustomerDaoImpl>--------<updateIsSendState>------end");
		return false;
	}

}
