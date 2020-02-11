package com.server.module.customer.product.customerBargain;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.PayStateEnum;
import com.server.util.stateEnum.PayTypeEnum;
@Repository
public class CustomerBargainDaoImpl extends MySqlFuns implements CustomerBargainDao{

	private final static Logger log = LogManager.getLogger(CustomerBargainDaoImpl.class);

	@Override
	public boolean isBargaining(Long id) {
		log.info("CustomerBargainDaoImpl--isEnd--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 FROM tbl_customer_bargain WHERE id = '"+id+"' AND state = 2 AND endTime > NOW()");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("CustomerBargainDaoImpl--isEnd--end");
		return result;
	}

	@Override
	public boolean cutPrice(Long bargainId,BigDecimal cutPrice) {
		log.info("CustomerBargainDaoImpl--cutPirce--start");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE tbl_customer_bargain SET currPrice = currPrice - ? WHERE id = ?");
		param.add(cutPrice);
		param.add(bargainId);
		int upate = upate(sql.toString(),param);
		log.info("CustomerBargainDaoImpl--cutPirce--end");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public CustomerBargainBean getCustomerBargain(Long id) {
		log.info("CustomerBargainDaoImpl--getCustomerBargain--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,goodsBargainId,customerId,currPrice,state,addressId,");
		sql.append(" endTime,createTime,updateTime,sendMessage FROM tbl_customer_bargain WHERE id = '"+id+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerBargainBean cusBargain = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cusBargain = new CustomerBargainBean();
				cusBargain.setAddressId(rs.getLong("addressId"));
				cusBargain.setCreateTime(rs.getTimestamp("createTime"));
				cusBargain.setCurrPrice(rs.getBigDecimal("currPrice"));
				cusBargain.setCustomerId(rs.getLong("customerId"));
				cusBargain.setEndTime(rs.getTimestamp("endTime"));
				cusBargain.setGoodsBargainId(rs.getLong("goodsBargainId"));
				cusBargain.setId(rs.getLong("id"));
				cusBargain.setState(rs.getInt("state"));
				cusBargain.setUpdateTime(rs.getTimestamp("updateTime"));
				cusBargain.setSendMessage(rs.getInt("sendMessage"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("CustomerBargainDaoImpl--getCustomerBargain--end");
		return cusBargain;
	}

	@Override
	public CustomerBargainDto getBargainsInfo(Long customerBargainId) {
		log.info("CustomerBargainDaoImpl--getBargainsInfo--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  tcw.`customerId`,tcw.`openId`,tcw.`headimgurl`,sg.`pic`,");
		sql.append(" sg.`name` AS itemName,sg.`id` AS itemId,gb.`lowestPrice`,sg.`salesPrice`,");
		sql.append(" tcb.`endTime`,tcb.`currPrice`,gb.id AS goodsBargainId FROM tbl_customer_bargain AS tcb");
		sql.append(" INNER JOIN tbl_customer_wx AS tcw USING(customerId)");
		sql.append(" INNER JOIN goods_bargain AS gb ON tcb.`goodsBargainId` = gb.`id`");
		sql.append(" INNER JOIN shopping_goods AS sg ON sg.`id` = gb.`goodsId`");
		sql.append(" WHERE tcb.`id` = '"+customerBargainId+"' AND sg.deleteFlag = 0 AND tcb.`endTime`>NOW() AND gb.`deleteFlag` = 0");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerBargainDto cusBargainDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				cusBargainDto = new CustomerBargainDto();
				cusBargainDto.setCurrPrice(rs.getBigDecimal("currPrice"));
				cusBargainDto.setCustomerId(rs.getLong("customerId"));
				cusBargainDto.setEndTime(rs.getTimestamp("endTime"));
				cusBargainDto.setHeadimgurl(rs.getString("headimgurl"));
				cusBargainDto.setItemId(rs.getLong("itemId"));
				cusBargainDto.setItemName(rs.getString("itemName"));
				cusBargainDto.setLowestPrice(rs.getBigDecimal("lowestPrice"));
				cusBargainDto.setOpenId(rs.getString("openId"));
				cusBargainDto.setSalesPrice(rs.getBigDecimal("salesPrice"));
				cusBargainDto.setPic(rs.getString("pic"));
				cusBargainDto.setGoodsBargainId(rs.getLong("goodsBargainId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("CustomerBargainDaoImpl--getBargainsInfo--end");
		return cusBargainDto;
	}

	@Override
	public BargainOrderDto getBargainOrderInfo(Long orderId) {
		log.info("CustomerBargainDaoImpl--getBargainOrderInfo--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select tca.`receiver`,tca.`phone`,tca.`name` AS addressName,");
		sql.append(" sg.`name` AS itemName,sg.`pic`,so.`price`,so.`nowprice`,so.`payCode`,so.`createTime` AS orderCreateTime,");
		sql.append(" tcb.`createTime` AS bargainStartTime,so.`state`,so.`id` AS orderId");
		sql.append(" FROM store_order AS so");
		sql.append(" INNER JOIN tbl_customer_bargain AS tcb ON so.`customerBargainId` = tcb.`id`");
		sql.append(" INNER JOIN tbl_customer_address AS tca ON tca.`id` = so.`addressId`");
		sql.append(" INNER JOIN shopping_goods AS sg ON sg.`id` = so.`product`");
		sql.append(" WHERE so.`id` = '"+orderId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BargainOrderDto orderDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				orderDto = new BargainOrderDto();
				orderDto.setAddressName(rs.getString("addressName"));
				orderDto.setBargainStartTime(rs.getTimestamp("bargainStartTime"));
				orderDto.setItemName(rs.getString("itemName"));
				orderDto.setNowPrice(rs.getBigDecimal("nowprice"));
				orderDto.setNum(1);
				orderDto.setOrderCreateTime(rs.getTimestamp("orderCreateTime"));
				orderDto.setOrderId(rs.getLong("orderId"));
				orderDto.setPayCode(rs.getString("payCode"));
				orderDto.setPayType(PayTypeEnum.WEIXIN.getIndex());
				orderDto.setPayTypeName(PayTypeEnum.WEIXIN.getPayType());
				orderDto.setPic(rs.getString("pic"));
				orderDto.setPrice(rs.getBigDecimal("price"));
				orderDto.setReceiverName(rs.getString("receiver"));
				orderDto.setReceiverPhone(rs.getString("phone"));
				orderDto.setState(rs.getInt("state"));
				orderDto.setStateName(PayStateEnum.findStateName(orderDto.getState()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("CustomerBargainDaoImpl--getBargainOrderInfo--end");
		return orderDto;
	}

	@Override
	public boolean updateBargainState(Long customerBargainId, Integer state) {
		log.info("CustomerBargainDaoImpl--updateBargainState--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE tbl_customer_bargain SET state = '"+state+"' WHERE id = '"+customerBargainId+"'");
		int upate = upate(sql.toString());
		log.info("CustomerBargainDaoImpl--updateBargainState--end");
		if(upate>0){
			return true;
		}
		return false;
	}
	

	@Override
	public Long insert(CustomerBargainBean customerBargainBean) {
		log.info("<CustomerBargainDaoImpl>----<insert>----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `tbl_customer_bargain` (goodsBargainId,customerId,currPrice,addressId,endTime)");
		sql.append(" VALUES(?,?,?,?,?) ");
		List<Object> param = new ArrayList<Object>();
		param.add(customerBargainBean.getGoodsBargainId());
		param.add(customerBargainBean.getCustomerId());
		param.add(customerBargainBean.getCurrPrice());
		param.add(customerBargainBean.getAddressId());
		param.add(customerBargainBean.getEndTime());
		int id = insertGetID(sql.toString(), param);
		log.info("<CustomerBargainDaoImpl>----<insert>----end");
		return Long.valueOf(id);
	}
	
}

