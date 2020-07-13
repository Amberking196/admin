package com.server.module.system.statisticsManage.payRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;
import com.server.util.stateEnum.PayTypeEnum;

import jersey.repackaged.com.google.common.collect.Lists;

@Repository("payRecordDao")
public class PayRecordDaoImpl extends BaseDao implements PayRecordDao {

	public static Logger log = LogManager.getLogger(PayRecordDaoImpl.class);
	@Autowired
	CompanyDao companyDaoImpl;

	@Override
	public List<PayRecordDto> findPayRecord(PayRecordForm payRecordForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecord>--start");
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select A.*,");
		sql.append(" IF (");
		sql.append("	rr.refundPrice IS NOT NULL,");
		sql.append("	(totalPrice- rr.refundPrice),");
		sql.append("	totalPrice");
		sql.append(" ) AS realPrice,");
		sql.append(" IF (");
		sql.append("	rr.refundNum IS NOT NULL,");
		sql.append("	(num - rr.refundNum),");
		sql.append("     num");
		sql.append(" ) AS realNum,refundTime,refundName,refundNum,refundPrice from ( ");

		
		sql.append("select pr.id,tc.`phone`,pr.`basicItemId`,c.`name` as companyName,c.id as companyId, ");
		sql.append(" pr.price as totalPrice,pr.costPrice / pr.`num` as costPrice,");
		sql.append(" pr.createTime,pr.finishTime,pr.itemId,pr.itemName,");
		sql.append(" pr.itemTypeId,pr.num  as num,pr.payCode,pr.payTime,pr.payType,vmi.locatoinName,	");
		sql.append(" pr.pickupNum,pr.price / pr.`num` as price,pr.ptCode,pr.refundName,");
		sql.append(
				" pr.refundTime,pr.remark,pr.state,pr.vendingMachinesCode,pr.wayNumber,pr.customerId,pr.couponId,vmi.areaId as areaId,va.name as areaName,pri.id as id2,pri.price/pri.num as price2,count(*) as type,sum(pri.price)/sum(pri.num) as price3");
		sql.append(
				" ,cr.orderId,memberPay,tc.isLoginUser from pay_record as pr");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		//sql.append(" LEFT JOIN vending_line vl on vmi.lineId=vl.id ");
		sql.append(" left join vending_area va on vmi.areaId=va.id ");
		sql.append(" left join pay_record_item pri on pr.id=pri.payRecordId  ");
		sql.append(" left join (select * from carry_record group by id)cr on cr.orderId=pr.id ");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id` ");
		sql.append(" where 1=1");

		if (payRecordForm != null) {
			if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
				sql.append(" and (vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
				sql.append(" or FIND_IN_SET( vmi.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.way=pr.wayNumber and lim.userId="+UserUtils.getUser().getId()+")))");
			}
			if (payRecordForm.getItemId() != null) {
				sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
			}
			if (payRecordForm.getAreaId() != null && payRecordForm.getAreaId()>0) {
				sql.append("and vmi.areaId = '" + payRecordForm.getAreaId() + "' ");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPayCode())) {
				sql.append(" and pr.payCode like '%" + payRecordForm.getPayCode() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPtCode())) {
				sql.append(" and pr.ptCode like '%" + payRecordForm.getPtCode() + "%'");
			}
			if (payRecordForm.getPayType() != null) {
				sql.append(" and pr.payType =" + payRecordForm.getPayType());
			}
			if (payRecordForm.getState() != null) {
				if (payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
					sql.append(" and (pr.state  = 10001 or  pr.state  = 10003) ");
 				}else{
					sql.append(" and pr.state  = '" + payRecordForm.getState() + "'");
				}
			}
			if (StringUtils.isNotEmpty(payRecordForm.getVendingMachinesCode())) {
				sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVendingMachinesCode() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getRemark())) {
				sql.append(" and pr.remark like '%" + payRecordForm.getRemark() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPhone())) {
				sql.append(" and tc.phone=" + payRecordForm.getPhone() + "");
			}
			if (payRecordForm.getState() != null
					&& payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
				if (payRecordForm.getStartDate() != null) {
					sql.append(
							" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
				}
				if (payRecordForm.getEndDate() != null) {
					sql.append(" and pr.payTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
				}
			} else {
				if (payRecordForm.getStartDate() != null) {
					sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate())
							+ "'");
				}
				if (payRecordForm.getEndDate() != null) {
					sql.append(
							" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
				}
			}
			if (payRecordForm.getWayNumber() != null) {
				sql.append(" and pr.wayNumber = " + payRecordForm.getWayNumber() + " ");
			}
			if (payRecordForm.getOrderType() != null) {
				sql.append(" and pr.orderType = " + payRecordForm.getOrderType() + " ");
			}
		}
		sql.append(" group by pr.id");
		sql.append(" order by pr.createTime desc");
		if (payRecordForm.getIsShowAll() == 0) {
			sql.append(" limit " + (payRecordForm.getCurrentPage() - 1) * payRecordForm.getPageSize() + ","
					+ payRecordForm.getPageSize());
		}
		sql.append(" )A left join refund_record rr on rr.ptCode=A.ptCode and rr.state=1 order by createTime desc ");
		log.info("查询语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecordDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				payRecordDto = new PayRecordDto();
				payRecordDto.setOrderId(rs.getLong("orderId"));
				payRecordDto.setId(rs.getLong("id"));
				payRecordDto.setPhone(rs.getString("phone"));
				payRecordDto.setBasicItemId(rs.getLong("basicItemId"));
				// payRecordDto.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecordDto.setAreaId(rs.getInt("areaId"));
				payRecordDto.setAreaName(rs.getString("areaName"));
				String createTime = rs.getString("createTime");
				if (createTime != null) {
					createTime = createTime.substring(0, createTime.length() - 2);
				}
				payRecordDto.setCreateTime(createTime);
				String finishTime = rs.getString("finishTime");
				if (finishTime != null) {
					finishTime = finishTime.substring(0, finishTime.length() - 2);
				}
				payRecordDto.setFinishTime(finishTime);
				payRecordDto.setItemId(rs.getLong("itemId"));
				payRecordDto.setItemName(rs.getString("itemName"));
				payRecordDto.setItemTypeId(rs.getInt("itemTypeId"));
				payRecordDto.setNum(rs.getInt("num"));
				payRecordDto.setPayCode(rs.getString("payCode"));
				payRecordDto.setRefundName(rs.getString("refundName"));
				String refundTime = rs.getString("refundtime");
				if (refundTime != null) {
					refundTime = refundTime.substring(0, refundTime.length() - 2);
				}
				payRecordDto.setRefundTime(refundTime);
				payRecordDto.setPayTypeId(rs.getInt("payType"));
				
				payRecordDto.setRemark(rs.getString("remark"));
				if(StringUtils.isNotBlank(payRecordDto.getRemark()) && payRecordDto.getRemark().equals("空白订单")) {
					payRecordDto.setRemark("未购买");
				}
				payRecordDto.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecordDto.setWayNumber(rs.getInt("wayNumber"));
				payRecordDto.setPickupNum(rs.getInt("pickupNum"));
				payRecordDto.setPtCode(rs.getString("ptCode"));
				// 商品原价 旧版无明细/多商品price/num
				// 新版单商品 pri.price/pri.num
				// 更改为 有明细商品单价就用 price3 sum(pri.price)/sum(pri.num)
				// 没明细优水余额 sum(pri.price)/sum(pri.num)
				if (rs.getLong("id2") == 0) {
					payRecordDto.setPrice(rs.getBigDecimal("price"));
				} else {
					payRecordDto.setPrice(
							rs.getBigDecimal("price3") == null ? BigDecimal.ZERO : rs.getBigDecimal("price3"));
				}

				String payTime = rs.getString("payTime");
				if (payTime != null) {
					payTime = payTime.substring(0, payTime.length() - 2);
				}
				payRecordDto.setPayTime(payTime);
				payRecordDto.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecordDto.setRealNum(rs.getInt("realNum"));
//				if(rs.getInt("type") != 1) {//sum(count(*)) 无法计算
//					log.info("--------------------------------"+rs.getLong("id"));
//					payRecordDto.setTotalPrice(payRecordDto.getTotalPrice().divide(new BigDecimal(rs.getInt("type"))));
//					payRecordDto.setNum(payRecordDto.getNum()/(rs.getInt("type")));
//
//				}
				payRecordDto.setRealPrice(rs.getBigDecimal("realPrice"));
				payRecordDto.setIsLoginUser(rs.getInt("isLoginUser"));
				if(payRecordDto.getIsLoginUser()==1) {
					payRecordDto.setLoginUser("员工订单");
				}
				payRecordDto.setCompanyId(rs.getInt("companyId"));
				payRecordDto.setCompanyName(rs.getString("companyName"));
				payRecordDto.setStateId(rs.getInt("state"));
				payRecordDto.setCustomerId(rs.getLong("customerId"));
				payRecordDto.setCouponIds(rs.getString("couponId"));
				payRecordDto.setMemberPay(
						rs.getBigDecimal("memberPay") == null ? BigDecimal.ZERO : rs.getBigDecimal("memberPay"));
				payRecordDto.setRefundPrice(
						rs.getBigDecimal("refundPrice") == null ? BigDecimal.ZERO : rs.getBigDecimal("refundPrice"));
				payRecordDto.setRefundNum(rs.getInt("refundNum"));
				payRecordDto.setLocatoinName(rs.getString("locatoinName"));
				payRecordDto.setOrderType(1);
				payRecordList.add(payRecordDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecord>--end");
		return payRecordList;
	}

	@Override
	public List<PayRecordDto> findVisionPayRecord(PayRecordForm payRecordForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecord>--start");
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select A.*,");
		sql.append(" IF (");
		sql.append("	rr.refundPrice IS NOT NULL,");
		sql.append("	(totalPrice- rr.refundPrice),");
		sql.append("	totalPrice");
		sql.append(" ) AS realPrice,");
		sql.append(" IF (");
		sql.append("	rr.refundNum IS NOT NULL,");
		sql.append("	(num - rr.refundNum),");
		sql.append("     num");
		sql.append(" ) AS realNum,refundTime,refundName,refundNum,refundPrice from ( ");

		
		sql.append("select pr.id,tc.`phone`,pr.`basicItemId`,c.`name` as companyName,c.id as companyId, ");
		sql.append(" pr.price as totalPrice,pr.costPrice / pr.`num` as costPrice,");
		sql.append(" pr.createTime,pr.finishTime,pr.itemId,pr.itemName,");
		sql.append(" pr.itemTypeId,pr.num  as num,pr.payCode,pr.payTime,pr.payType,vmi.locatoinName,	");
		sql.append(" pr.pickupNum,pr.price / pr.`num` as price,pr.ptCode,pr.refundName,");
		sql.append(
				" pr.refundTime,pr.remark,pr.state,pr.vendingMachinesCode,pr.wayNumber,pr.customerId,pr.couponId,vl.areaId as areaId,va.name as areaName,pri.id as id2,pri.price/pri.num as price2,count(*) as type,sum(pri.price)/sum(pri.num) as price3");
		sql.append(
				" ,cr.orderId,memberPay,tc.isLoginUser from pay_record_vision as pr");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		sql.append(" LEFT JOIN vending_line vl on vmi.lineId=vl.id ");
		sql.append(" left join vending_area va on vmi.areaId=va.id ");
		sql.append(" left join pay_record_item_vision pri on pr.id=pri.payRecordId  ");
		sql.append(" left join (select * from carry_record group by id)cr on cr.orderId=pr.id ");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id` ");
		sql.append(" where 1=1");

		if (payRecordForm != null) {
			if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
				sql.append(" and vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
			}
			if (payRecordForm.getItemId() != null) {
				sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
			}
			if (payRecordForm.getAreaId() != null) {
				sql.append("and vl.areaId = '" + payRecordForm.getAreaId() + "' ");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPayCode())) {
				sql.append(" and pr.payCode like '%" + payRecordForm.getPayCode() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPtCode())) {
				sql.append(" and pr.ptCode like '%" + payRecordForm.getPtCode() + "%'");
			}
			if (payRecordForm.getPayType() != null) {
				sql.append(" and pr.payType =" + payRecordForm.getPayType());
			}
			if (payRecordForm.getState() != null) {
				if (payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
					sql.append(" and (pr.state  = 10001 or  pr.state  = 10003) ");
 				}else{
					sql.append(" and pr.state  = '" + payRecordForm.getState() + "'");
				}
			}
			if (StringUtils.isNotEmpty(payRecordForm.getVendingMachinesCode())) {
				sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVendingMachinesCode() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getRemark())) {
				sql.append(" and pr.remark like '%" + payRecordForm.getRemark() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPhone())) {
				sql.append(" and tc.phone=" + payRecordForm.getPhone() + "");
			}
			if (payRecordForm.getState() != null
					&& payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
				if (payRecordForm.getStartDate() != null) {
					sql.append(
							" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
				}
				if (payRecordForm.getEndDate() != null) {
					sql.append(" and pr.payTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
				}
			} else {
				if (payRecordForm.getStartDate() != null) {
					sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate())
							+ "'");
				}
				if (payRecordForm.getEndDate() != null) {
					sql.append(
							" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
				}
			}
			if (payRecordForm.getWayNumber() != null) {
				sql.append(" and pr.wayNumber = " + payRecordForm.getWayNumber() + " ");
			}
		}
		sql.append(" group by pr.id");
		sql.append(" order by pr.createTime desc");
		if (payRecordForm.getIsShowAll() == 0) {
			sql.append(" limit " + (payRecordForm.getCurrentPage() - 1) * payRecordForm.getPageSize() + ","
					+ payRecordForm.getPageSize());
		}
		sql.append(" )A left join refund_record rr on rr.ptCode=A.ptCode and rr.state=1 order by createTime desc ");
		log.info("查询语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecordDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				payRecordDto = new PayRecordDto();
				payRecordDto.setOrderId(rs.getLong("orderId"));
				payRecordDto.setId(rs.getLong("id"));
				payRecordDto.setPhone(rs.getString("phone"));
				payRecordDto.setBasicItemId(rs.getLong("basicItemId"));
				// payRecordDto.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecordDto.setAreaId(rs.getInt("areaId"));
				payRecordDto.setAreaName(rs.getString("areaName"));
				String createTime = rs.getString("createTime");
				if (createTime != null) {
					createTime = createTime.substring(0, createTime.length() - 2);
				}
				payRecordDto.setCreateTime(createTime);
				String finishTime = rs.getString("finishTime");
				if (finishTime != null) {
					finishTime = finishTime.substring(0, finishTime.length() - 2);
				}
				payRecordDto.setFinishTime(finishTime);
				payRecordDto.setItemId(rs.getLong("itemId"));
				payRecordDto.setItemName(rs.getString("itemName"));
				payRecordDto.setItemTypeId(rs.getInt("itemTypeId"));
				payRecordDto.setNum(rs.getInt("num"));
				payRecordDto.setPayCode(rs.getString("payCode"));
				payRecordDto.setRefundName(rs.getString("refundName"));
				String refundTime = rs.getString("refundtime");
				if (refundTime != null) {
					refundTime = refundTime.substring(0, refundTime.length() - 2);
				}
				payRecordDto.setRefundTime(refundTime);
				payRecordDto.setPayTypeId(rs.getInt("payType"));
				
				payRecordDto.setRemark(rs.getString("remark"));
				if(StringUtils.isNotBlank(payRecordDto.getRemark()) && payRecordDto.getRemark().equals("空白订单")) {
					payRecordDto.setRemark("未购买");
				}
				payRecordDto.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecordDto.setWayNumber(rs.getInt("wayNumber"));
				payRecordDto.setPickupNum(rs.getInt("pickupNum"));
				payRecordDto.setPtCode(rs.getString("ptCode"));
				// 商品原价 旧版无明细/多商品price/num
				// 新版单商品 pri.price/pri.num
				// 更改为 有明细商品单价就用 price3 sum(pri.price)/sum(pri.num)
				// 没明细优水余额 sum(pri.price)/sum(pri.num)
				if (rs.getLong("id2") == 0) {
					payRecordDto.setPrice(rs.getBigDecimal("price"));
				} else {
					payRecordDto.setPrice(
							rs.getBigDecimal("price3") == null ? BigDecimal.ZERO : rs.getBigDecimal("price3"));
				}

				String payTime = rs.getString("payTime");
				if (payTime != null) {
					payTime = payTime.substring(0, payTime.length() - 2);
				}
				payRecordDto.setPayTime(payTime);
				payRecordDto.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecordDto.setRealNum(rs.getInt("realNum"));
				payRecordDto.setRealPrice(rs.getBigDecimal("realPrice"));
				payRecordDto.setIsLoginUser(rs.getInt("isLoginUser"));
				if(payRecordDto.getIsLoginUser()==1) {
					payRecordDto.setLoginUser("员工订单");
				}
				payRecordDto.setCompanyId(rs.getInt("companyId"));
				payRecordDto.setCompanyName(rs.getString("companyName"));
				payRecordDto.setStateId(rs.getInt("state"));
				payRecordDto.setCustomerId(rs.getLong("customerId"));
				payRecordDto.setCouponIds(rs.getString("couponId"));
				payRecordDto.setMemberPay(
						rs.getBigDecimal("memberPay") == null ? BigDecimal.ZERO : rs.getBigDecimal("memberPay"));
				payRecordDto.setRefundPrice(
						rs.getBigDecimal("refundPrice") == null ? BigDecimal.ZERO : rs.getBigDecimal("refundPrice"));
				payRecordDto.setRefundNum(rs.getInt("refundNum"));
				payRecordDto.setLocatoinName(rs.getString("locatoinName"));
				payRecordDto.setOrderType(6);
				payRecordList.add(payRecordDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findVisionPayRecord>--end");
		return payRecordList;
	}
	
	@Override
	public SumPayRecordDto findPayRecordNum(PayRecordForm payRecordForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecordNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select count(1) as total,SUM(if(re.refundPrice is not NULL,pr.price - re.refundPrice,pr.price)) as sumPrice, SUM(costPrice) as sumCostPrice from pay_record pr ");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		sql.append(" left join vending_area va on vmi.areaId=va.id ");
		sql.append(" left join refund_record re on pr.ptCode=re.ptCode and re.state=1 ");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id`  where 1=1");
		if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
			sql.append(" and (vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
			sql.append(" or FIND_IN_SET( vmi.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.way=pr.wayNumber and lim.userId="+UserUtils.getUser().getId()+")))");

		}
		if (payRecordForm.getAreaId() != null && payRecordForm.getAreaId()>0) {
			sql.append("and vmi.areaId = '" + payRecordForm.getAreaId() + "' ");
		}
		if (payRecordForm.getItemId() != null) {
			sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
		}
		if (payRecordForm.getItemId() != null) {
			sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPayCode())) {
			sql.append(" and pr.payCode like '%" + payRecordForm.getPayCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPtCode())) {
			sql.append(" and pr.ptCode like '%" + payRecordForm.getPtCode() + "%'");
		}
		if (payRecordForm.getPayType() != null) {
			sql.append(" and pr.payType =" + payRecordForm.getPayType());
		}
		if (payRecordForm.getState() != null) {
			if (payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
				sql.append(" and (pr.state  = 10001 or  pr.state  = 10003) ");
			}else{
				sql.append(" and pr.state  = '" + payRecordForm.getState() + "'");
			}
		}
		if (StringUtils.isNotEmpty(payRecordForm.getVendingMachinesCode())) {
			sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVendingMachinesCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getRemark())) {
			sql.append(" and pr.remark like '%" + payRecordForm.getRemark() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPhone())) {
			sql.append(" and tc.phone=" + payRecordForm.getPhone() + "");
		}
		if (payRecordForm.getState() != null && payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
			if (payRecordForm.getStartDate() != null) {
				sql.append(" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
			}
			if (payRecordForm.getEndDate() != null) {
				sql.append(" and pr.payTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
			}
		} else {
			if (payRecordForm.getStartDate() != null) {
				sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
			}
			if (payRecordForm.getEndDate() != null) {
				sql.append(" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
			}
			if (payRecordForm.getWayNumber() != null) {
				sql.append(" and pr.wayNumber = " + payRecordForm.getWayNumber() + " ");
			}
		}
		if (payRecordForm.getWayNumber() != null) {
			sql.append(" and pr.wayNumber = " + payRecordForm.getWayNumber() + " ");
		}
		if (payRecordForm.getOrderType()!=null) {
			sql.append(" and orderType = "+payRecordForm.getOrderType());
		}else {
			sql.append(" and orderType = 1");
		}
		log.info("查询语句：" + sql.toString());
		SumPayRecordDto sumPayRecordDto = new SumPayRecordDto();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				sumPayRecordDto.setTotal(rs.getLong("total"));
				sumPayRecordDto.setSumPrice(rs.getDouble("sumPrice"));
				// sumPayRecordDto.setSumCostPrice(rs.getDouble("sumCostPrice"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecordNum>--end");
		return sumPayRecordDto;
	}

	@Override
	public SumPayRecordDto findVisionPayRecordNum(PayRecordForm payRecordForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecordNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select count(1) as total,SUM(if(re.refundPrice is not NULL,pr.price - re.refundPrice,pr.price)) as sumPrice, SUM(costPrice) as sumCostPrice from pay_record_vision pr ");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		sql.append(" left join vending_line vl on vmi.lineId=vl.id ");
		sql.append(" left join refund_record re on pr.ptCode=re.ptCode and re.state=1 ");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id`  where 1=1");
		if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
			sql.append(" and vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
		}
		if (payRecordForm.getAreaId() != null) {
			sql.append("and vl.areaId = '" + payRecordForm.getAreaId() + "' ");
		}
		if (payRecordForm.getItemId() != null) {
			sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
		}
		if (payRecordForm.getItemId() != null) {
			sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPayCode())) {
			sql.append(" and pr.payCode like '%" + payRecordForm.getPayCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPtCode())) {
			sql.append(" and pr.ptCode like '%" + payRecordForm.getPtCode() + "%'");
		}
		if (payRecordForm.getPayType() != null) {
			sql.append(" and pr.payType =" + payRecordForm.getPayType());
		}
		if (payRecordForm.getState() != null) {
			if (payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
				sql.append(" and (pr.state  = 10001 or  pr.state  = 10003) ");
			}else{
				sql.append(" and pr.state  = '" + payRecordForm.getState() + "'");
			}
		}
		if (StringUtils.isNotEmpty(payRecordForm.getVendingMachinesCode())) {
			sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVendingMachinesCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getRemark())) {
			sql.append(" and pr.remark like '%" + payRecordForm.getRemark() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPhone())) {
			sql.append(" and tc.phone=" + payRecordForm.getPhone() + "");
		}
		if (payRecordForm.getState() != null && payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
			if (payRecordForm.getStartDate() != null) {
				sql.append(" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
			}
			if (payRecordForm.getEndDate() != null) {
				sql.append(" and pr.payTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
			}
		} else {
			if (payRecordForm.getStartDate() != null) {
				sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
			}
			if (payRecordForm.getEndDate() != null) {
				sql.append(" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
			}
			if (payRecordForm.getWayNumber() != null) {
				sql.append(" and pr.wayNumber = " + payRecordForm.getWayNumber() + " ");
			}
		}
		if (payRecordForm.getWayNumber() != null) {
			sql.append(" and pr.wayNumber = " + payRecordForm.getWayNumber() + " ");
		}
		if (payRecordForm.getOrderType()!=null) {
			sql.append(" and orderType = "+payRecordForm.getOrderType());
		}else {
			sql.append(" and orderType = 6");
		}
		log.info("查询语句：" + sql.toString());
		SumPayRecordDto sumPayRecordDto = new SumPayRecordDto();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				sumPayRecordDto.setTotal(rs.getLong("total"));
				sumPayRecordDto.setSumPrice(rs.getDouble("sumPrice"));
				// sumPayRecordDto.setSumCostPrice(rs.getDouble("sumCostPrice"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecordNum>--end");
		return sumPayRecordDto;
	}

	
	@Override
	public boolean deletePayRecord(List<Integer> payRecordIds) {
		log.info("<PayRecordDaoImpl>--<deletePayRecord>--start");
		StringBuffer sql = new StringBuffer();
		String payIds = StringUtils.join(payRecordIds, ",");
		sql.append("delete from pay_record where id in (" + payIds + ")");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			int sign = ps.executeUpdate();
			if (sign == payRecordIds.size()) {
				log.info("<PayRecordDaoImpl>--<deletePayRecord>--end");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, ps, conn);
		}
		return false;
	}

	@Override
	public boolean updateOrderState(Integer state, Integer id,Integer orderType) {
		log.info("<PayRecordDaoImpl>--<updateOrderState>--start");
		String sql="";
		if(orderType==null || orderType==1) {
			 sql = "update pay_record set state = " + state + " where id =" + id;
		}else {
			 sql = "update pay_record_vision set state = " + state + " where id =" + id;
		}
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			int sign = ps.executeUpdate();
			log.info("<PayRecordDaoImpl>--<updateOrderState>--end");
			if (sign == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, ps, conn);
		}
		return false;
	}

	/**
	 * 统计销售量 公司 区域 线路
	 */
	public ReturnDataUtil listSellNumStatisticsPage(PayRecordForm1 condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select vendingMachinesCode,basicItemId,itemName,sum(num) as num,count(id) as count from pay_record p inner join vending_machines_info i on p.vendingMachinesCode=i.code ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotBlank(condition.getAreaId())) {
			sql.append(" and i.areaId=" + condition.getAreaId());
		}
		if (StringUtils.isNotBlank(condition.getCompanyId())) {
			sql.append(" and i.companyId=" + condition.getCompanyId());
		}
		if (StringUtils.isNotBlank(condition.getLineId())) {
			sql.append(" and i.lineId=" + condition.getLineId());
		}
		if (StringUtils.isNotBlank(condition.getVmCode())) {
			sql.append(" and i.code like '%" + condition.getVmCode() + "%'");
		}
		sql.append(" and  i.companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));

		if (StringUtils.isNotBlank(condition.getStartTime())) {
			sql.append(" and p.createTime>='" + condition.getStartTime() + " 00:00:00'");
		}
		if (StringUtils.isNotBlank(condition.getEndTime())) {
			sql.append(" and p.createTime<='" + condition.getEndTime() + " 23:59:59'");
		}
		sql.append(" group by vendingMachinesCode,basicItemId,itemName");

		if (showSql) {
			log.info(sql);
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			// pst = conn.prepareStatement(super.countSql(sql.toString()));
			pst = conn.prepareStatement("select count(*) from (" + sql.toString() + ") c");

			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			if (condition.getIsShowAll() == 1) {
				pst = conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<PayRecordBean> list = Lists.newArrayList();
			int num = 0;
			while (rs.next()) {
				num++;
				PayRecordBean bean = new PayRecordBean();

				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				bean.setNum(((Long) rs.getLong("num")).intValue());
				bean.setCount(((Long) rs.getLong("count")).intValue());
				// 导出Excel的序号
				bean.setCountNum(num);
				list.add(bean);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查询支付失败总价
	 */
	@Override
	public Double findFailurePrice(PayRecordForm payRecordForm) {
		double failurePrice = 0;
		log.info("<PayRecordDaoImpl>--<findFailurePrice>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(pr.price) as pNum from pay_record pr ");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id`  where   pr.state=10002 ");
		if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
			sql.append(" and (vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
			sql.append(" or FIND_IN_SET( vmi.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.way=pr.wayNumber and lim.userId="+UserUtils.getUser().getId()+")))");

		}
		if (payRecordForm.getItemId() != null) {
			sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPayCode())) {
			sql.append(" and pr.payCode like '%" + payRecordForm.getPayCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPtCode())) {
			sql.append(" and pr.ptCode like '%" + payRecordForm.getPtCode() + "%'");
		}
		if (payRecordForm.getPayType() != null) {
			sql.append(" and pr.payType =" + payRecordForm.getPayType());
		}
		if (StringUtils.isNotEmpty(payRecordForm.getVendingMachinesCode())) {
			sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVendingMachinesCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getRemark())) {
			sql.append(" and pr.remark like '%" + payRecordForm.getRemark() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPhone())) {
			sql.append(" and tc.phone=" + payRecordForm.getPhone() + "");
		}
		if (payRecordForm.getStartDate() != null) {
			sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
		}
		if (payRecordForm.getEndDate() != null) {
			sql.append(" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
		}
		if (payRecordForm.getOrderType()!=null) {
			sql.append(" and orderType = "+payRecordForm.getOrderType());
		}
		log.info("查询支付失败总价查询语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				failurePrice = rs.getDouble("pNum");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecordNum>--end");
		return failurePrice;
	}
	
	@Override
	public Double findVisionFailurePrice(PayRecordForm payRecordForm) {
		double failurePrice = 0;
		log.info("<PayRecordDaoImpl>--<findFailurePrice>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(pr.price) as pNum from pay_record_vision pr ");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id`  where   pr.state=10002 ");
		if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
			sql.append(" and vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
		}
		if (payRecordForm.getItemId() != null) {
			sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPayCode())) {
			sql.append(" and pr.payCode like '%" + payRecordForm.getPayCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPtCode())) {
			sql.append(" and pr.ptCode like '%" + payRecordForm.getPtCode() + "%'");
		}
		if (payRecordForm.getPayType() != null) {
			sql.append(" and pr.payType =" + payRecordForm.getPayType());
		}
		if (StringUtils.isNotEmpty(payRecordForm.getVendingMachinesCode())) {
			sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVendingMachinesCode() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getRemark())) {
			sql.append(" and pr.remark like '%" + payRecordForm.getRemark() + "%'");
		}
		if (StringUtils.isNotEmpty(payRecordForm.getPhone())) {
			sql.append(" and tc.phone=" + payRecordForm.getPhone() + "");
		}
		if (payRecordForm.getStartDate() != null) {
			sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
		}
		if (payRecordForm.getEndDate() != null) {
			sql.append(" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
		}
		if (payRecordForm.getOrderType()!=null) {
			sql.append(" and orderType = "+payRecordForm.getOrderType());
		}
		log.info("查询支付失败总价查询语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				failurePrice = rs.getDouble("pNum");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecordNum>--end");
		return failurePrice;
	}
	

	@Override
	public ReturnDataUtil findMachineHistory(CustomerMachineForm customerMachineForm) {
		log.info("<PayRecordDaoImpl>--<findMachineHistory>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();

		sql.append(
				" SELECT vmi.machineVersion,td.doorNO,td.preWeight,ROUND(td.preWeight * 1.0 / ib.standard) AS preNum,ROUND(td.posWeight * 1.0 / ib.standard) AS posNum,td.posWeight,pr.num,td.openedTime,td.closedTime FROM tbl_door_operate_history AS td LEFT JOIN pay_record pr ON pr.operateHistoryId = td.id ");

		sql.append(" left join pay_record_item pri on pr.id=pri.payrecordid");
		sql.append(" LEFT JOIN item_basic ib ON pri.basicItemId = ib.id");
		sql.append(" left join vending_machines_info vmi on vmi.code=pr.vendingMachinesCode");

		sql.append(" WHERE pr.payCode = '" + customerMachineForm.getPayCode() + "'");

		log.info("查询语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			long off = (customerMachineForm.getCurrentPage() - 1) * customerMachineForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + customerMachineForm.getPageSize());
			rs = pst.executeQuery();
			CustomerMachineHistoryBean bean = new CustomerMachineHistoryBean();
			while (rs.next()) {
				bean.setPosNum(rs.getInt("posNum"));
				bean.setPreNum(rs.getInt("preNum"));
				if (rs.getInt("machineVersion") == 2) {// 多商品无前后数量
					bean.setPosNum(0);
					bean.setPreNum(0);
				}
				bean.setDoorNO(rs.getInt("doorNO"));

				bean.setPreWeight(rs.getDouble("preWeight"));
				bean.setPosWeight(rs.getDouble("posWeight"));
				bean.setNum(rs.getInt("num"));
				bean.setOpenedTime(rs.getTimestamp("openedTime"));
				bean.setClosedTime(rs.getTimestamp("closedTime"));
			}

			data.setCurrentPage(customerMachineForm.getCurrentPage());
			data.setReturnObject(bean);
			data.setStatus(1);
			log.info("<CustomerDaoImpl>--<findMachineHistory>----end");
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Map<String, Object>> getBefore7Day(String companySqlStr, String before7Day) {
		String sql = "SELECT  DAY(payTime) as day,SUM(price) AS jiner FROM pay_record WHERE " + companySqlStr
				+ " payTime>='" + before7Day
				+ "' and (state = 10001 or state=10006) GROUP BY YEAR(payTime) DESC,MONTH(payTime) DESC,DAY(payTime) DESC";
		System.out.println(before7Day);
		List<Map<String, Object>> listday = (List<Map<String, Object>>) super.baseSelectMap(sql, null);
		return listday;
	}

	public List<Map<String, Object>> getToday(String companySqlStr, String todayStr) {

		String todayStrSql = "SELECT  SUM(price) AS jiner FROM pay_record WHERE " + companySqlStr + " payTime>='"
				+ todayStr + "' and (state = 10001 or state=10006)";

		List<Map<String, Object>> listToday = (List<Map<String, Object>>) super.baseSelectMap(todayStrSql, null);

		return listToday;
	}

	public List<Map<String, Object>> getYesterday(String companySqlStr, String yesterdayStr) {

		String yesterdaySql = "SELECT  SUM(price) AS jiner FROM pay_record WHERE " + companySqlStr + " payTime>='"
				+ yesterdayStr + "' and (state = 10001 or state=10006)";

		List<Map<String, Object>> listYesterday = (List<Map<String, Object>>) super.baseSelectMap(yesterdaySql, null);
		return listYesterday;
	}

	public List<Map<String, Object>> getMonth(String companySqlStr, String monthStr) {

		String monthStrSql = "SELECT  SUM(price) AS jiner FROM pay_record WHERE " + companySqlStr + " payTime>='"
				+ monthStr + "' and (state = 10001 or state=10006)";

		List<Map<String, Object>> listMonth = (List<Map<String, Object>>) super.baseSelectMap(monthStrSql, null);
		return listMonth;

	}

	@Override
	public ReturnDataUtil payRecordDetail(PayRecordForm payRecordForm) {
		log.info("<PayRecordDaoImpl>----<payRecordDetail>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select pri.realTotalPrice,pri.payRecordId as prid,pri.id,pri.num,pri.itemName,pri.price as totalPrice,pri.price/num as price,pri.basicItemId");
				
		if(payRecordForm.getOrderType()==null || payRecordForm.getOrderType()==1) {
			sql.append(" from pay_record_item pri where 1=1 ");
		}else {
			sql.append(" from pay_record_item_vision pri where 1=1 ");
		}
		if (payRecordForm.getId() != null) {
			sql.append(" and pri.payRecordId= " + payRecordForm.getId());
		}
		log.info("销售记录详情列表>>>:" + sql.toString());
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
			long off = (payRecordForm.getCurrentPage() - 1) * payRecordForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + payRecordForm.getPageSize());
			rs = pst.executeQuery();
			List<PayRecordItemBean> list = Lists.newArrayList();
			while (rs.next()) {
				PayRecordItemBean bean = new PayRecordItemBean();
				bean.setPayRecordId(rs.getLong("prid"));
				bean.setId(rs.getLong("id"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setItemName(rs.getString("itemName"));
				// bean.setCostPrice(rs.getDouble("costPrice"));
				bean.setPrice(rs.getDouble("price"));
				bean.setNum(rs.getInt("num"));
				bean.setTotalPrice(rs.getDouble("totalPrice"));
				bean.setRealTotalPrice(rs.getDouble("realTotalPrice"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(payRecordForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setPageSize(payRecordForm.getPageSize());
			log.info("<PayRecordDaoImpl>----<payRecordDetail>----end");
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
	public ReturnDataUtil findPayRecordForExport(PayRecordForm payRecordForm) {
		log.info("<PayRecordDaoImpl>--<findPayRecordForExport>--start");
		List<PayRecordDto> payRecordList = new ArrayList<PayRecordDto>();
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append("select A.*,");
		sql.append(" IF (");
		sql.append("	rr.refundPrice IS NOT NULL,");
		sql.append("	(totalPrice- rr.refundPrice),");
		sql.append("	totalPrice");
		sql.append(" ) AS totalPrice2,");
		sql.append(" IF (");
		sql.append("	rr.refundNum IS NOT NULL,");
		sql.append("	(num - rr.refundNum),");
		sql.append("     num");
		sql.append(" ) AS num2,refundTime,refundName,refundNum,refundPrice from ( ");
		
		sql.append("select pr.id,tc.`phone`,pr.`basicItemId`,c.`name` as companyName,c.id as companyId, ");
		sql.append(" pr.price  as totalPrice,pr.costPrice / pr.`num` as costPrice,");
		sql.append(" pr.createTime,pr.finishTime,pr.itemId,pr.itemName,");
		sql.append(" pr.itemTypeId,pr.num  as num,pr.payCode,pr.payTime,pr.payType,vmi.locatoinName,	");
		sql.append(" pr.pickupNum,pr.price / pr.`num` as price,pr.ptCode,");
		sql.append(
				"  pr.remark,pr.state,pr.vendingMachinesCode,pr.wayNumber,customerId,pr.couponId,vl.areaId as areaId,va.name as areaName, ");
		sql.append(
				" pri.id as id2,pri.basicItemId as basicItemId2,pri.itemId as itemId2,pri.price/pri.num as price2,pri.num as num2,pri.itemName as itemName2,pri.itemType as itemTypeId2,pri.realTotalPrice as totalPrice2,pri.costPrice as costPrice2,memberPay");
		sql.append("  from pay_record as pr");
		sql.append(" LEFT JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" LEFT JOIN vending_machines_info vmi ON vmi.`code`=pr.`vendingMachinesCode`");
		sql.append(" LEFT JOIN vending_line vl on vmi.lineId=vl.id ");
		sql.append(" left join vending_area va on vmi.areaId=va.id ");
		sql.append(" LEFT JOIN company c ON vmi.`companyId` = c.`id` ");
		sql.append(" LEFT JOIN pay_record_item pri ON pri.payRecordId = pr.id");
		sql.append("  where 1=1 ");
		if (payRecordForm != null) {
			if (StringUtils.isNotBlank(payRecordForm.getCompanyIds())) {
				sql.append(" and vmi.companyId in(" + payRecordForm.getCompanyIds() + ")");
			}
			if (payRecordForm.getItemId() != null) {
				sql.append(" and pr.itemId like '%" + payRecordForm.getItemId() + "%'");
			}
			if (payRecordForm.getAreaId() != null) {
				sql.append("and vl.areaId = '" + payRecordForm.getAreaId() + "' ");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPayCode())) {
				sql.append(" and pr.payCode like '%" + payRecordForm.getPayCode() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPtCode())) {
				sql.append(" and pr.ptCode like '%" + payRecordForm.getPtCode() + "%'");
			}
			if (payRecordForm.getPayType() != null) {
				sql.append(" and pr.payType =" + payRecordForm.getPayType());
			}
			if (payRecordForm.getState() != null) {
//				if (payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
//					sql.append(" and pr.state  = '" + PayStateEnum.NOT_PAY.getState() + "'");
//				} else {
					sql.append(" and pr.state  = '" + payRecordForm.getState() + "'");
//				}
			}
			if (StringUtils.isNotEmpty(payRecordForm.getVendingMachinesCode())) {
				sql.append(" and pr.vendingMachinesCode like '%" + payRecordForm.getVendingMachinesCode() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getRemark())) {
				sql.append(" and pr.remark like '%" + payRecordForm.getRemark() + "%'");
			}
			if (StringUtils.isNotEmpty(payRecordForm.getPhone())) {
				sql.append(" and tc.phone=" + payRecordForm.getPhone() + "");
			}
			if (payRecordForm.getState() != null
					&& payRecordForm.getState().equals(PayStateEnum.PAY_SUCCESS.getState())) {
				if (payRecordForm.getStartDate() != null) {
					sql.append(
							" and pr.payTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate()) + "'");
				}
				if (payRecordForm.getEndDate() != null) {
					sql.append(" and pr.payTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
				}
			} else {
				if (payRecordForm.getStartDate() != null) {
					sql.append(" and pr.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getStartDate())
							+ "'");
				}
				if (payRecordForm.getEndDate() != null) {
					sql.append(
							" and pr.createTime<='" + DateUtil.formatYYYYMMDDHHMMSS(payRecordForm.getEndDate()) + "'");
				}
			}
			if (payRecordForm.getWayNumber() != null) {
				sql.append(" and pr.wayNumber = " + payRecordForm.getWayNumber() + " ");
			}
		}
		sql.append(" order by pr.createTime desc");
		log.info("查询语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayRecordDto payRecordDto = null;
		try {
			conn = openConnection();
			long count = 0;
			// ps=conn.prepareStatement(super.countSql(sql.toString()));
			// rs = ps.executeQuery();
			// while(rs.next()){
			// count=rs.getInt(1);
			// }
			if (payRecordForm.getIsShowAll() == 0) {
				long off = (payRecordForm.getCurrentPage() - 1) * payRecordForm.getPageSize();
				sql.append(" limit " + off + "," + payRecordForm.getPageSize());
				sql.append(" )A left join refund_record rr on rr.ptCode=A.ptCode ");
				ps = conn.prepareStatement(sql.toString()  );
			} else {
				sql.append(" )A left join refund_record rr on rr.ptCode=A.ptCode ");
				ps = conn.prepareStatement(sql.toString());
			}
			rs = ps.executeQuery();

			while (rs != null && rs.next()) {
				payRecordDto = new PayRecordDto();
				payRecordDto.setId(rs.getLong("id"));
				payRecordDto.setPhone(rs.getString("phone"));
				payRecordDto.setBasicItemId(rs.getLong("basicItemId"));
				// payRecordDto.setCostPrice(rs.getBigDecimal("costPrice"));
				payRecordDto.setAreaId(rs.getInt("areaId"));
				payRecordDto.setAreaName(rs.getString("areaName"));
				String createTime = rs.getString("createTime");
				if (createTime != null) {
					createTime = createTime.substring(0, createTime.length() - 2);
				}
				payRecordDto.setCreateTime(createTime);
				String finishTime = rs.getString("finishTime");
				if (finishTime != null) {
					finishTime = finishTime.substring(0, finishTime.length() - 2);
				}
				payRecordDto.setFinishTime(finishTime);
				payRecordDto.setItemId(rs.getLong("itemId"));
				payRecordDto.setItemName(rs.getString("itemName"));
				payRecordDto.setItemTypeId(rs.getInt("itemTypeId"));
				payRecordDto.setNum(rs.getInt("num2"));
				payRecordDto.setPayCode(rs.getString("payCode"));
				payRecordDto.setRefundName(rs.getString("refundName"));
				String refundTime = rs.getString("refundtime");
				if (refundTime != null) {
					refundTime = refundTime.substring(0, refundTime.length() - 2);
				}
				payRecordDto.setRefundTime(refundTime);
				payRecordDto.setPayTypeId(rs.getInt("payType"));
				payRecordDto.setRemark(rs.getString("remark"));
				payRecordDto.setVendingMachinesCode(rs.getString("vendingMachinesCode"));
				payRecordDto.setWayNumber(rs.getInt("wayNumber"));
				payRecordDto.setPickupNum(rs.getInt("pickupNum"));
				payRecordDto.setPtCode(rs.getString("ptCode"));
				payRecordDto.setMemberPay(
						rs.getBigDecimal("memberPay") == null ? BigDecimal.ZERO : rs.getBigDecimal("memberPay"));
				if (rs.getLong("id2") == 0) {// 旧版商品 无明细表
					payRecordDto.setPrice(rs.getBigDecimal("price"));
				} else {
					payRecordDto.setPrice(rs.getBigDecimal("price2"));
				}

				String payTime = rs.getString("payTime");
				if (payTime != null) {
					payTime = payTime.substring(0, payTime.length() - 2);
				}
				payRecordDto.setPayTime(payTime);
				payRecordDto.setTotalPrice(rs.getBigDecimal("totalPrice"));
				payRecordDto.setCompanyId(rs.getInt("companyId"));
				payRecordDto.setCompanyName(rs.getString("companyName"));
				payRecordDto.setStateId(rs.getInt("state"));
				payRecordDto.setCustomerId(rs.getLong("customerId"));
				payRecordDto.setCouponIds(rs.getString("couponId"));

				if (rs.getLong("basicItemId") == 0 || rs.getLong("itemId") == 0) {
					payRecordDto.setBasicItemId(rs.getLong("basicItemId2"));
					payRecordDto.setItemId(rs.getLong("itemId2"));
					payRecordDto.setItemName(rs.getString("itemName2"));
					payRecordDto.setItemTypeId(rs.getInt("itemTypeId2"));
					// payRecordDto.setCostPrice(rs.getBigDecimal("costPrice2"));
					payRecordDto.setPrice(rs.getBigDecimal("Price2"));
					payRecordDto.setNum(rs.getInt("num2"));
					payRecordDto.setTotalPrice(rs.getBigDecimal("totalPrice2"));
				}
				payRecordDto.setPayType(PayTypeEnum.getPayTypeInfo(payRecordDto.getPayTypeId()));
				payRecordDto.setState(PayStateEnum.findStateName(payRecordDto.getStateId()));
				payRecordDto.setRefundNum(rs.getInt("refundNum"));
				payRecordDto.setRefundPrice(
						rs.getBigDecimal("refundPrice") == null ? BigDecimal.ZERO : rs.getBigDecimal("refundPrice"));
				payRecordDto.setLocatoinName(rs.getString("locatoinName"));
				// 过滤非空白订单的数量未0的订单明细
				if (!payRecordDto.getStateId().equals(PayStateEnum.BLANK_ORDER.getState())
						&& payRecordDto.getNum() == 0) {

				} else {
					payRecordList.add(payRecordDto);
				}
			}
			data.setCurrentPage(payRecordForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(payRecordList);
			data.setStatus(1);
			data.setPageSize(payRecordForm.getPageSize());
			log.info("<PayRecordDaoImpl>----<findPayRecordForExport>----end");

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl>--<findPayRecord>--end");
		return data;
	}

	/**
	 * 查询销售数量，聚合
	 * 
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listSaleNum(ListSaleNumCondition condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT p.vendingMachinesCode AS vmCode,p.wayNumber,i.basicItemId,i.itemName,SUM(i.num) AS num from pay_record_item i INNER JOIN pay_record p ON i.payRecordId=p.id INNER JOIN vending_machines_info mi ON p.vendingMachinesCode=mi.code "
						+ " WHERE 1=1 ");
		if (condition.getAreaId() != null && condition.getAreaId()>0) {
			sql.append(" and mi.areaId = '" + condition.getAreaId() + "' ");
		}
		if (StringUtil.isNotBlank(condition.getVmCode())) {
			sql.append(" and p.vendingMachinesCode='" + condition.getVmCode() + "'");
		}
		if (condition.getBasicItemId() != null) {
			sql.append(" and  i.basicItemId=" + condition.getBasicItemId());
		}
		if (condition.getCompanyId() != null) {
			sql.append(" and  mi.companyId=" + condition.getCompanyId());
		}
		if (StringUtil.isNotBlank(condition.getEndTime())) {
			sql.append(" and p.payTime<='" + condition.getEndTime() + " 23:59:59' ");
		}
		if (StringUtil.isNotBlank(condition.getStartTime())) {
			sql.append(" and p.payTime>='" + condition.getStartTime() + " 00:00:00' ");
		}
		if (StringUtil.isNotBlank(condition.getEndTime())) {
			sql.append(" and p.payTime<='" + condition.getEndTime() + " 23:59:59' ");
		}
		sql.append(" GROUP BY p.vendingMachinesCode,p.wayNumber,i.basicItemId,i.itemName "
				+ " ORDER BY p.vendingMachinesCode,p.wayNumber ");

		log.info(sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement("SELECT COUNT(*) FROM (" + sql.toString() + ") a");
			System.out.println(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<ListSaleNumDTO> list = Lists.newArrayList();
			while (rs.next()) {
				ListSaleNumDTO bean = new ListSaleNumDTO();
				bean.setItemName(rs.getString("itemName"));
				bean.setNum(rs.getInt("num"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setPageSize(condition.getPageSize());
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
	public List<PayRecordItemDto> getPayRecordItemList(String payCode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pri.`itemName`,pri.`num`,ib.`pic`,pri.`realTotalPrice`,pri.`price` AS originalPrice,");
		sql.append(" pr.`price` AS sumPrice FROM pay_record AS pr");
		sql.append(" INNER JOIN pay_record_item AS pri ON pri.`payRecordId` = pr.`id`");
		sql.append(" INNER JOIN item_basic AS ib ON ib.`id` = pri.`basicItemId`");
		sql.append(" WHERE pr.`payCode` = '" + payCode + "'");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PayRecordItemDto> itemList = new ArrayList<PayRecordItemDto>();
		PayRecordItemDto item = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				item = new PayRecordItemDto();
				item.setItemName(rs.getString("itemName"));
				item.setNum(rs.getInt("num"));
				item.setOriginalPrice(rs.getBigDecimal("originalPrice"));
				item.setPic(rs.getString("pic"));
				item.setRealTotalPrice(rs.getBigDecimal("realTotalPrice"));
				item.setSumPrice(rs.getBigDecimal("sumPrice"));
				itemList.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return itemList;
	}

	@Override
	public WeightNumDto getOrderWeightNum(String payCode) {
		log.info("<PayRecordDaoImpl--getOrderWeightNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pr.vendingMachinesCode AS vmCode,pr.`wayNumber`,");
		sql.append(" pr.num AS totalNum,tdoh.`createTime`,tdoh.`closedTime`,");
		sql.append(" tdoh.`preWeight`,tdoh.`posWeight`,pri.`basicItemId`,ib.`name` AS itemName,");
		sql.append(
				" ib.`standard` AS unitWeight,pri.num as buyNum,pr.createTime as orderCreateTime FROM pay_record AS pr ");
		sql.append(" INNER JOIN tbl_door_operate_history AS tdoh ON tdoh.`id` = pr.`operateHistoryId`");
		sql.append(" LEFT JOIN pay_record_item AS pri ON pri.`payRecordId` = pr.`id`");
		sql.append(" LEFT JOIN item_basic AS ib ON ib.`id` = pri.`basicItemId`");
		sql.append(" WHERE pr.`payCode` = '" + payCode + "'");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WeightNumDto weightNum = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				if (weightNum == null) {
					weightNum = new WeightNumDto();
					weightNum.setClosedDoorTime(rs.getString("closedTime"));
					weightNum.setOpenDoorTime(rs.getString("createTime"));
					if (rs.getObject("posWeight") != null) {
						weightNum.setPosWeight(rs.getInt("posWeight"));
					}
					if (rs.getObject("preWeight") != null) {
						weightNum.setPreWeight(rs.getInt("preWeight"));
					}
					weightNum.setTotalNum(rs.getInt("totalNum"));
					weightNum.setVmCode(rs.getString("vmCode"));
					weightNum.setWayNumber(rs.getInt("wayNumber"));
					weightNum.setCreateOrderTime(rs.getString("orderCreateTime"));
				}
				weightNum.setDiffWeight(weightNum.getPreWeight()-weightNum.getPosWeight());
				NumDto num = new NumDto();
				num.setBasicItemId(rs.getLong("basicItemId"));
				num.setItemName(rs.getString("itemName"));
				num.setUnitWeight(rs.getInt("unitWeight"));
				num.setBuyNum(rs.getInt("buyNum"));
				
				weightNum.getNumList().add(num);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl--getOrderWeightNum--end>");
		return weightNum;
	}

	@Override
	public WeightNumDto getOrderWeightNumVision(String payCode) {
		log.info("<PayRecordDaoImpl--getOrderWeightNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tdoh.id as tid,pr.vendingMachinesCode AS vmCode,pr.`wayNumber`,");
		sql.append(" pr.num AS totalNum,tdoh.`openedTime`,tdoh.`closedTime`,");
		sql.append(" tdoh.`preWeight`,tdoh.`posWeight`,pri.`basicItemId`,ib.`name` AS itemName,");
		sql.append(
				" ib.`standard` AS unitWeight,pri.num as buyNum,pr.createTime as orderCreateTime FROM pay_record_vision AS pr ");
		sql.append(" INNER JOIN tbl_door_operate_history_vision AS tdoh ON tdoh.`id` = pr.`operateHistoryId`");
		sql.append(" LEFT JOIN pay_record_item_vision AS pri ON pri.`payRecordId` = pr.`id`");
		sql.append(" LEFT JOIN item_basic AS ib ON ib.`id` = pri.`basicItemId`");
		sql.append(" WHERE pr.`payCode` = '" + payCode + "'");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		WeightNumDto weightNum = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				if (weightNum == null) {
					weightNum = new WeightNumDto();
					weightNum.setId(rs.getLong("tid"));
					weightNum.setClosedDoorTime(rs.getString("closedTime"));
					weightNum.setOpenDoorTime(rs.getString("openedTime"));
					if (rs.getObject("posWeight") != null) {
						weightNum.setPosWeight(rs.getInt("posWeight"));
					}
					if (rs.getObject("preWeight") != null) {
						weightNum.setPreWeight(rs.getInt("preWeight"));
					}
					weightNum.setTotalNum(rs.getInt("totalNum"));
					weightNum.setVmCode(rs.getString("vmCode"));
					weightNum.setWayNumber(rs.getInt("wayNumber"));
					weightNum.setCreateOrderTime(rs.getString("orderCreateTime"));
				}
				NumDto num = new NumDto();
				num.setBasicItemId(rs.getLong("basicItemId"));
				num.setItemName(rs.getString("itemName"));
				num.setUnitWeight(rs.getInt("unitWeight"));
				num.setBuyNum(rs.getInt("buyNum"));
				weightNum.getNumList().add(num);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl--getOrderWeightNum--end>");
		return weightNum;
	}

	
	@Override
	public NumDto getOrderNumChange(NumDto numInfo, String vmCode, Integer wayNumber, String createTime) {
		log.info("<PayRecordDaoImpl--getOrderNumChange--start>");
		StringBuffer sql = new StringBuffer();
		if (numInfo.getBuyNum() > 0) {
			sql.append(" SELECT preNum,num AS posNum");
			sql.append(" FROM vending_waynum_log WHERE id IN (");
			sql.append(" SELECT MIN(id) FROM vending_waynum_log WHERE vmCode = '" + vmCode + "'");
			sql.append(" AND wayNumber = '" + wayNumber + "' AND basicItemId = '" + numInfo.getBasicItemId() + "'");
			sql.append(" AND createTime >= SUBDATE('" + createTime + "',INTERVAL 3 SECOND))");
		} else {
			//sql.append(" SELECT num AS preNum,num AS posNum");
			sql.append(" SELECT id,createTime,num AS preNum,num AS posNum,opType FROM (");
			sql.append(" SELECT id,createTime,num,opType FROM vending_waynum_log  WHERE vmCode = '" + vmCode
					+ "' AND wayNumber = '" + wayNumber + "'");
			sql.append(" AND basicItemId = '" + numInfo.getBasicItemId() + "' AND createTime < '" + createTime
					+ "' AND opType = 1");
			sql.append(" UNION");
			sql.append(" SELECT id,createTime,num,opType FROM replenish_record WHERE vmCode = '" + vmCode + "' AND wayNumber = '"
					+ wayNumber + "'");
			sql.append(" AND basicItemId = '" + numInfo.getBasicItemId() + "' AND createTime < '" + createTime + "'");
			sql.append(" ORDER BY createTime DESC LIMIT 1) A ");
		}
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				numInfo.setPosNum(rs.getInt("posNum"));
				numInfo.setPreNum(rs.getInt("preNum"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl--getOrderNumChange--end>");
		return numInfo;
	}
	
	@Override
	public NumDto getOrderNumChangeVision(NumDto numInfo, String vmCode, Integer wayNumber, String createTime) {
		log.info("<PayRecordDaoImpl--getOrderNumChange--start>");
		StringBuffer sql = new StringBuffer();
		if (numInfo.getBuyNum() > 0) {
			sql.append(" SELECT preNum,num AS posNum");
			sql.append(" FROM vending_waynum_log WHERE id IN (");
			sql.append(" SELECT MIN(id) FROM vending_waynum_log WHERE vmCode = '" + vmCode + "'");
			sql.append(" AND wayNumber = '" + wayNumber + "' AND basicItemId = '" + numInfo.getBasicItemId() + "'");
			sql.append(" AND createTime >= SUBDATE('" + createTime + "',INTERVAL 3 SECOND))");
		} else {
			//sql.append(" SELECT num AS preNum,num AS posNum");
			sql.append(" SELECT id,createTime,num AS preNum,num AS posNum,opType FROM (");
			sql.append(" SELECT id,createTime,num,opType FROM vending_waynum_log  WHERE vmCode = '" + vmCode
					+ "' AND wayNumber = '" + wayNumber + "'");
			sql.append(" AND basicItemId = '" + numInfo.getBasicItemId() + "' AND createTime < '" + createTime
					+ "' AND opType = 1");
			sql.append(" UNION");
			sql.append(" SELECT id,createTime,num,opType FROM replenish_record_vision WHERE vmCode = '" + vmCode + "' AND wayNumber = '"
					+ wayNumber + "'");
			sql.append(" AND basicItemId = '" + numInfo.getBasicItemId() + "' AND createTime < '" + createTime + "'");
			sql.append(" ORDER BY createTime DESC LIMIT 1) A ");
		}
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				numInfo.setPosNum(rs.getInt("posNum"));
				numInfo.setPreNum(rs.getInt("preNum"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<PayRecordDaoImpl--getOrderNumChange--end>");
		return numInfo;
	}
}
