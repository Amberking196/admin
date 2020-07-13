package com.server.module.system.statisticsManage.chart;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.system.companyManage.CompanyDao;

@Repository
public class ChartDaoImpl extends BaseDao<ChartBean> implements ChartDao {
	@Autowired
	private CompanyDao companyManageDao;

	public List<DateCountVo> listMachinesInfo(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT companyId,DATE_FORMAT(createTime,'%Y%m%d') AS day FROM vending_machines_info WHERE createTime IS NOT NULL ");

		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND areaId=" + form.getAreaId());
			}
		}
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND createTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND createTime<'" + form.getEnd() + "'");
		}
		sql.append(" ORDER BY createTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setDay(rs.getInt("day"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

	public List<DateCountVo> listUserInfo(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT DATE_FORMAT(c.createTime,'%Y%m%d') AS DAY FROM tbl_customer c,vending_machines_info vmi  WHERE c.vmCode=vmi.code AND c.phone is not null and  c.createTime>'2016-01-01 00:00:00' ");
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND c.createTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND c.createTime<'" + form.getEnd() + "'");
		}
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND c.vmCode ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(" AND vmi.companyId in "
							+ companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND vmi.companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND vmi.areaId=" + form.getAreaId());
			}
		}
		sql.append("  ORDER BY c.createTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setDay(rs.getInt("day"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

	public List<DateCountVo> listOrderInfo(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS day FROM pay_record pr inner join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code WHERE  pr.payTime is not null ");//pr.state=10001 and
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND pr.payTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND pr.payTime<='" + form.getEnd() + "'");
		}

		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND vmi.code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(" AND vmi.companyId in "
							+ companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND vmi.companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND vmi.areaId=" + form.getAreaId());
			}
		}
		sql.append("  ORDER BY pr.payTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setDay(rs.getInt("day"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

	public List<DateCountVo> listOrderByHourInfo(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DATE_FORMAT(pr.payTime,'%H') AS day FROM pay_record pr  WHERE pr.payTime IS NOT NULL  ");
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND pr.payTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND pr.payTime<='" + form.getEnd() + "'");
		}
		/*
		 * if(form.getCustomerId()>0) {
		 * sql.append(" AND pr.customerId ="+form.getCustomerId()); }
		 */
		sql.append("  ORDER BY day ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setDay(rs.getInt("day"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<SaleGoodsDateCountVo> listGoodsSaleInfo(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				" SELECT p.basicItemId,p.num,DATE_FORMAT(p.payTime,'%Y%m%d') AS day,i.companyId,i.areaId FROM pay_record p INNER JOIN vending_machines_info i ON p.vendingMachinesCode=i.code where 1=1 and p.state=10001 and p.payTime is not null  ");

		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() > 0) {// 区域条件
				sql.append(" AND areaId=" + form.getAreaId());
			}
		}
		if (StringUtils.isNotBlank(form.getStart()) && StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND payTime>='" + form.getStart() + "'");
			sql.append(" AND payTime<='" + form.getEnd() + "'");
		}
		sql.append(" ORDER BY payTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<SaleGoodsDateCountVo> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				SaleGoodsDateCountVo vo = new SaleGoodsDateCountVo();
				vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(rs.getInt("num"));
				vo.setDay(rs.getInt("day"));
				vo.setAreaId(rs.getInt("areaId"));
				vo.setItemId(rs.getInt("basicItemId"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 获取所有商品
	 * 
	 * @return
	 */
	public Map<Integer, String> listGoods() {

		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT DISTINCT basicItemId,i.name FROM pay_record p LEFT JOIN item_basic i ON p.basicItemId=i.id WHERE p.state=10001 ");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<Integer, String> m = Maps.newHashMap();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			m.put(0, "所有商品");
			while (rs.next()) {
				if (rs.getString("name") == null)
					break;
				m.put(rs.getInt("basicItemId"), rs.getString("name"));
			}
			if (showSql) {
				log.info(sql);
			}
			return m;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return m;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<DateCountVo> listGoodsSaleCount(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS DAY,pri.num AS number FROM pay_record pr INNER JOIN pay_record_item pri ON pr.id=pri.payRecordId INNER JOIN  vending_machines_info vmi ON pr.vendingMachinesCode=vmi.code WHERE pr.payTime IS NOT NULL ");
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND pr.payTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND pr.payTime<='" + form.getEnd() + "'");
		}
		if (form.getCompanyId() != null && form.getCompanyId() > 1) {
			// 添加查询条件
			if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
				sql.append(
						" AND vmi.companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
			} else {
				sql.append(" AND vmi.companyId =" + form.getCompanyId());
			}
		}
		if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
			sql.append(" AND vmi.areaId=" + form.getAreaId());
		}
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND vmi.code ='" + form.getVmCode() + "'");
		}
		sql.append("  ORDER BY pr.payTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(rs.getInt("number"));
				vo.setDay(rs.getInt("day"));
				list.add(vo);
			}
			if (showSql) {
				log.info(sql);
			}
			//return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			//return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		//  list 内容是每天的销量

		//List<DateCountVo> listMachine = this.listMachinesStartUsingByForm(form);





		return list;
	}

	@Override
	public List<DateCountVo> listCostSalePrice(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS day,pr.price AS number FROM pay_record pr inner join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code WHERE pr.payTime IS NOT NULL  ");
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND pr.payTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND pr.payTime<='" + form.getEnd() + "'");
		}
		if (form.getCompanyId() != null && form.getCompanyId() > 1) {
			// 添加查询条件
			if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
				sql.append(
						" AND vmi.companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
			} else {
				sql.append(" AND vmi.companyId =" + form.getCompanyId());
			}
		}
		if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
			sql.append(" AND vmi.areaId=" + form.getAreaId());
		}
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND vmi.code ='" + form.getVmCode() + "'");
		}
		sql.append("  ORDER BY pr.payTime ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
//				int price=(int) (rs.getDouble("number")*100);
				BigDecimal bigDecimal = rs.getBigDecimal("number");
				BigDecimal newDecimal = new BigDecimal(100);

				int price = bigDecimal.multiply(newDecimal).intValue();
				vo.setCount(price);
				vo.setDay(rs.getInt("day"));
				list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<DateCountVo> listCustomerNumber(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		// 按日查询
		if (form.getType() == 0) {// 0 日 1 旬 2 月 3 季度
			sql.append(
					"SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS day,DATE_FORMAT(pr.payTime,'%Y%m%d') AS orderNum,DATE_FORMAT(pr.payTime,'%Y%m%d') AS time,pr.customerId  FROM pay_record pr inner join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code WHERE pr.payTime IS NOT NULL  ");
		}
		// 按旬查询
		if (form.getType() == 1) {// 0 日 1 旬 2 月 3 季度
			sql.append(
					"SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS day,DATE_FORMAT(pr.payTime,'%Y%m%d') AS orderNum,DATE_FORMAT(pr.payTime,'%Y%m') AS time,pr.customerId  FROM pay_record pr inner join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code WHERE pr.payTime IS NOT NULL  ");
		}
		// 按月查询
		if (form.getType() == 2) {// 0 日 1 旬 2 月 3 季度
			sql.append(
					"SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS day,DATE_FORMAT(pr.payTime,'%Y%m') AS orderNum,DATE_FORMAT(pr.payTime,'%Y%m') AS time,pr.customerId  FROM pay_record pr inner join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code WHERE pr.payTime IS NOT NULL  ");
		}
		// 按 季度查询
		if (form.getType() == 3) {// 0 日 1 旬 2 月 3 季度
			sql.append(
					"SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS day,DATE_FORMAT(pr.payTime,'%Y%m') AS orderNum,DATE_FORMAT(pr.payTime,'%Y') AS time,pr.customerId  FROM pay_record pr inner join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code WHERE pr.payTime IS NOT NULL  ");
		}
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND pr.payTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND pr.payTime<='" + form.getEnd() + "'");
		}
		if (form.getCompanyId() != null && form.getCompanyId() > 1) {
			// 添加查询条件
			if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
				sql.append(
						" AND vmi.companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
			} else {
				sql.append(" AND vmi.companyId =" + form.getCompanyId());
			}
		}
		if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
			sql.append(" AND vmi.areaId=" + form.getAreaId());
		}
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND vmi.code ='" + form.getVmCode() + "'");
		}
		sql.append("  ORDER BY orderNum ,pr.customerId ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();
		HashSet<String> set1 = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		HashSet<String> set3 = new HashSet<String>();
		HashSet<String> set4 = new HashSet<String>();
		Set set[] = new Set[4];
		set[0] = set1;
		set[1] = set2;
		set[2] = set3;
		set[3] = set4;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setCode(rs.getString("customerId"));
				vo.setDay(rs.getInt("day"));
				vo.setOrderNum(rs.getInt("orderNum"));
				vo.setTime(rs.getInt("time"));
				Grouping.ResultData(vo, list, set, form);

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
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<DateCountVo> listMachineCount(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		// 按日查询
		sql.append(
				"SELECT DATE_FORMAT(pr.payTime,'%Y%m%d') AS day,DATE_FORMAT(pr.payTime,'%Y%m%d') AS orderNum,DATE_FORMAT(pr.payTime,'%Y%m%d') AS time,pr.vendingMachinesCode  FROM pay_record pr inner join vending_machines_info vmi on pr.vendingMachinesCode=vmi.code WHERE pr.payTime IS NOT NULL  ");

		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND pr.payTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND pr.payTime<='" + form.getEnd() + "'");
		}
		if (form.getCompanyId() != null && form.getCompanyId() > 1) {
			// 添加查询条件
			if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
				sql.append(
						" AND vmi.companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
			} else {
				sql.append(" AND vmi.companyId =" + form.getCompanyId());
			}
		}
		if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
			sql.append(" AND vmi.areaId=" + form.getAreaId());
		}
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND vmi.code ='" + form.getVmCode() + "'");
		}

		sql.append("  ORDER BY orderNum ,pr.vendingMachinesCode ");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
				vo.setCount(1);
				vo.setCode(rs.getString("vendingMachinesCode"));
				vo.setDay(rs.getInt("day"));
				vo.setOrderNum(rs.getInt("orderNum"));
				vo.setTime(rs.getInt("time"));
				if (list.size() > 0) {
					DateCountVo temp = list.get(list.size() - 1);
					if (vo.getDay() == temp.getDay() && vo.getCode().equals(temp.getCode())) {
						// 该list中有该数据不添加
					} else {
						list.add(vo);// 添加该数据
					}

				} else {// 直接添加
					list.add(vo);
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
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public Integer MachineCountByFrom(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(DISTINCT code)as number FROM vending_machines_info WHERE createTime IS NOT NULL ");

		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND areaId=" + form.getAreaId());
			}
		}
		// 把起始时间当成条件
		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND createTime<'" + form.getStart() + "'");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer number = 0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				number = rs.getInt("number");
			}
			if (showSql) {
				log.info(sql);
			}
			return number;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return number;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public Integer UserCountByFrom(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT count(DISTINCT c.id)as number FROM tbl_customer c,vending_machines_info vmi  WHERE c.vmCode=vmi.code AND c.createTime>'2016-01-01 00:00:00' ");

		if (StringUtils.isNotBlank(form.getStart())) {
			sql.append(" AND c.createTime<'" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND c.vmCode ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(" AND vmi.companyId in "
							+ companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND vmi.companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND vmi.areaId=" + form.getAreaId());
			}
		}

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer number = 0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				number = rs.getInt("number");
			}
			if (showSql) {
				log.info(sql);
			}
			return number;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return number;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	@Override
	//机器开始上线时间
	public List<DateCountVo> listMachinesStartUsingByForm(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT day,SUM(num) as num FROM (");
		sql.append(" SELECT DATE_FORMAT(startUsingTime,'%Y%m%d') AS day,1 as num FROM vending_machines_info WHERE state=20001 and startUsingTime IS NOT NULL  ");
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND areaId=" + form.getAreaId());
			}
		}

		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND startUsingTime<='" + form.getEnd() + "'");
		}
		sql.append(" ORDER BY day ");
		sql.append(") t GROUP BY DAY");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();

			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				vo.setDay(rs.getInt("day"));
				vo.setCount(rs.getInt("num"));
				list.add(vo);
			}
			int len = list.size()-1;
			for(int i = len;i>=0;i--){
				DateCountVo vo = list.get(i);
				int count = 0;
				for(int j = 0;j<=i;j++){
					count = count+list.get(j).getCount();
				}
				vo.setCount(count);
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
			this.closeConnection(rs, pst, conn);
		}
	}
	@Override//不要了，太复杂，看不懂,换成listMachinesCountByForm
	public List<DateCountVo> listMachinesInfoByForm(ChartForm form) {
		StringBuilder sql = new StringBuilder();
		int number=0;
		sql.append(
				" SELECT companyId,DATE_FORMAT(startUsingTime,'%Y%m%d') AS day FROM vending_machines_info WHERE startUsingTime IS NOT NULL  ");
		if (StringUtils.isNotBlank(form.getVmCode())) {
			sql.append(" AND code ='" + form.getVmCode() + "'");
		} else {
			if (form.getCompanyId() != null && form.getCompanyId() > 1) {
				// 添加查询条件
				if (companyManageDao.checkIsSubsidiaries(form.getCompanyId())) {// 判断是否拥有子公司
					sql.append(
							" AND companyId in " + companyManageDao.findAllSonCompanyIdForInSql(form.getCompanyId()));
				} else {
					sql.append(" AND companyId =" + form.getCompanyId());
				}
			}
			if (form.getAreaId() != null && form.getAreaId() > 0) {// 区域条件
				sql.append(" AND areaId=" + form.getAreaId());
			}
		}
		if (StringUtils.isNotBlank(form.getStart())) {
			//查询之前的上线总数
			number=MachineCountByFrom(form);
			sql.append(" AND startUsingTime>='" + form.getStart() + "'");
		}
		if (StringUtils.isNotBlank(form.getEnd())) {
			sql.append(" AND startUsingTime<='" + form.getEnd() + "'");
		}
		sql.append(" ORDER BY day ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<DateCountVo> list = Lists.newArrayList();

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			int time=0;//记录了第一天的个数
			DayGrouping day=new DayGrouping();
			while (rs.next()) {
				DateCountVo vo = new DateCountVo();
				// vo.setCompanyId(rs.getInt("companyId"));
//				int price=(int) (rs.getDouble("number")*100);
				vo.setCount(1);
				vo.setDay(rs.getInt("day"));
				if(list.size()>0) {
					DateCountVo temp=list.get(list.size()-1);
					if(time==vo.getDay()) {//同一天的
						list.add(vo);
					}else {
						//判断是否有下一天
						if(day.nextGroup(time)==vo.getDay()) {//有下一天，直接加入list中
							//time++;
							time=day.nextGroup(time);
							list.add(vo);
						}else {
							//循环补齐缺的天数
							for(;time<vo.getDay();) {
							//time++;
							time=day.nextGroup(time);
							//创建一个对象放入list集合中
							DateCountVo newVo=new DateCountVo();
							newVo.setCount(0);
							newVo.setDay(time);
							list.add(newVo);
							}
							//添加vo到list中
							list.add(vo);
						}
					}
				}else {
					list.add(vo);
					time=vo.getDay();
				}
			}
			//补充list集合的天的个数
			listWraper(form,list,day,number);
			if (showSql) {
				log.info(sql);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	public void listWraper(ChartForm form,List<DateCountVo> list,DayGrouping day,int number) {
		if (StringUtils.isNotBlank(form.getStart())) {
			//对字符串进行处理
			String startStr=formDateToDay(form.getStart());
			if(list.size()>0) {
				List newList=new ArrayList();
				int start=Integer.parseInt(startStr);//2018-8-18 00:00:00  2018-10-18 00:00:00 2018-10-8 00:00:00
				int index=list.get(0).getDay();
				if(start==index) {//时间查询的第一天有上线设备
					list.get(0).setCount(list.get(0).getCount()+number);
				}else {//起始时间跟list中的第一个时间不匹配.补齐数据
					for(;start<index;) {
						DateCountVo newVo=new DateCountVo();
						newVo.setDay(start);
						newVo.setCount(0);
						start=day.nextGroup(start);
						newList.add(newVo);
					}
					list.addAll(0, newList);
					list.get(0).setCount(list.get(0).getCount()+number);//设置第一天个数相加之前的个数
				}
				//结尾时间也要补齐
				if(StringUtils.isNotBlank(form.getEnd())) {
					//对字符串进行处理
					String endStr=formDateToDay(form.getEnd());
					int end=Integer.parseInt(endStr);
					int endIndex=list.get(list.size()-1).getDay();
					//时间加1
					endIndex=day.nextGroup(endIndex);
					for(;endIndex<end;) {
						DateCountVo newVo=new DateCountVo();
						newVo.setDay(endIndex);
						endIndex=day.nextGroup(endIndex);
						newVo.setCount(0);
						//直接加到list尾部
						list.add(newVo);
					}
				}else {//没结束时间截止到今天
					int endIndex=list.get(list.size()-1).getDay();
					SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
					endIndex=day.nextGroup(endIndex);
					int end=Integer.parseInt(sf.format(new Date()));
					for(;endIndex<=end;) {//循环补齐每天有一条记录
						DateCountVo newVo=new DateCountVo();
						newVo.setDay(endIndex);
						newVo.setCount(0);
						endIndex=day.nextGroup(endIndex);
						list.add(newVo);
					}
				}
			}else {//list中没有元素
				//补齐元素
				int start=Integer.parseInt(startStr);
				if(StringUtils.isNotBlank(form.getEnd())) {
					//对字符串进行处理
					String endStr=formDateToDay(form.getEnd());
					
					int end=Integer.parseInt(endStr);
					for(;start<=end;) {//循环补齐每天有一条记录
						DateCountVo newVo=new DateCountVo();
						newVo.setDay(start);
						newVo.setCount(0);
						start=day.nextGroup(start);
						list.add(newVo);
					}
				}else {//没有结束时间，取当前时间
					SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
					int end=Integer.parseInt(sf.format(new Date()));
//					start++;
					//start=day.nextGroup(start);
					System.err.println(start);
					System.err.println(end);
					for(;start<=end;) {//循环补齐每天有一条记录
						DateCountVo newVo=new DateCountVo();
						newVo.setDay(start);
						newVo.setCount(0);
						start=day.nextGroup(start);
						list.add(newVo);
					}
				}
				list.get(0).setCount(number);
			}
			
		}
		//没有起始时间查询
		if(StringUtils.isNotBlank(form.getEnd())) {//有结束时间
			int start=list.get(list.size()-1).getDay();
			String endStr=formDateToDay(form.getEnd());
			int end=Integer.parseInt(endStr);
//			start++;
			start=day.nextGroup(start);
			for(;start<=end;) {//循环补齐每天有一条记录
				DateCountVo newVo=new DateCountVo();
				newVo.setDay(start);
				newVo.setCount(0);
				start=day.nextGroup(start);
				list.add(newVo);
			}
		}else {//没有结束时间
			int start=list.get(list.size()-1).getDay();
//			start++;
			start=day.nextGroup(start);
			SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
			int end=Integer.parseInt(sf.format(new Date()));
			for(;start<=end;) {//循环补齐每天有一条记录
				DateCountVo newVo=new DateCountVo();
				newVo.setDay(start);
				newVo.setCount(0);
				start=day.nextGroup(start);
				list.add(newVo);
			}
		}
	}
	public String formDateToDay(String str) {//把2018-8-18 00:00:00转化为20180818的字符
		String[] splitEnd = str.split(" ");
		String strEnd=splitEnd[0];
		String[] splitEnd2 = strEnd.split("-");
		String strEnd0=splitEnd2[0];
		String strEnd1=splitEnd2[1];
		String strEnd2=splitEnd2[2];
		if(strEnd1.length()==1) {//拼割字符串
			strEnd1=0+strEnd1;
		}
		if(strEnd2.length()==1) {//拼割字符串
			strEnd2=0+strEnd2;
		}
		String endStr=strEnd0+strEnd1+strEnd2;
		return endStr;
	}
}
