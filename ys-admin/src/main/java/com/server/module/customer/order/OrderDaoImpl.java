package com.server.module.customer.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.CustomerUtil;
import com.server.module.customer.product.spellGroupShare.SpellGroupCustomerBean;
import com.server.module.system.statisticsManage.payRecord.PayRecordItemDto;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.PayStateEnum;

@Repository
public class OrderDaoImpl extends BaseDao<OrderBean> implements OrderDao {

	private static final Logger log = LogManager.getLogger(OrderDaoImpl.class);

	@Override
	public List<OrderDto> findOrderByOpenId(String openId) {
		log.info("<OrderDaoImpl--findOrderByOpenId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(
				" SELECT  pr.id AS orderId,pr.customerId as customerId, pr.ptCode,pr.state,pr.createTime,ib.`name` AS itemName,");
		sql.append(" (pr.price / pr.num) AS price,pr.price AS totalPrice,pr.num,pr.payCode as payCode,tc.phone");
		sql.append(" FROM pay_record pr");
		sql.append(" INNER JOIN item_basic ib ON pr.basicItemId = ib.id");
		sql.append(" INNER JOIN tbl_customer AS tc ON pr.customerId = tc.`id`");
		sql.append(" INNER JOIN tbl_customer_wx as tcw  on tcw.customerId = tc.id");
		sql.append(" where 1=1 and pr.fakeFlag=0 and tcw.openId='" + openId + "'");
		sql.append(" order by pr.createTime desc");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderDto order = null;
		List<OrderDto> orderList = new ArrayList<OrderDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				order = new OrderDto();
				order.setCreateTime(rs.getTimestamp("createTime"));
				order.setItemName(rs.getString("itemName"));
				order.setNum(rs.getInt("num"));
				order.setOrderId(rs.getLong("orderId"));
				order.setPrice(rs.getBigDecimal("price"));
				order.setPayCode(rs.getString("payCode"));
				order.setPtCode(rs.getString("ptCode"));
				order.setState(rs.getInt("state"));
				order.setTotalPrice(rs.getBigDecimal("totalPrice"));
				order.setCustomerId(rs.getLong("customerId"));
				order.setPhone(rs.getString("phone"));
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findOrderByOpenId--end>");
		return orderList;
	}

	@Override
	public double findSumPriceByProduct(String product) {
		log.info("<OrderDaoImpl--findSumPriceByProduct--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(price*num) as price FROM shopping_car WHERE id IN(" + product + ")");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		double price = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				price = rs.getDouble("price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findSumPriceByProduct--end>");
		return price;

	}

	@Override
	public String findOpenIdByCustomerId(Long customerId) {
		log.info("<OrderDaoImpl--findOpenIdByCustomerId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT openId FROM tbl_customer_wx WHERE customerId=" + customerId);
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String openid = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				openid = rs.getString("openId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findOpenIdByCustomerId--end>");
		return openid;

	}

	@Override
	public double findDeductionMoneyByCoupon(Integer coupon) {
		log.info("<OrderDaoImpl--findDeductionMoneyByCoupon--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT deductionMoney FROM coupon WHERE id=" + coupon);
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		double price = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				price = rs.getDouble("deductionMoney");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findDeductionMoneyByCoupon--end>");
		return price;
	}

	@Override
	public OrderBean insert(OrderBean entity) {

		return super.insert(entity);
	}

	// @Override
	// public boolean insert(CouponHistory entity) {
	// log.info("<OrderDaoImpl>--<insert>--start");
	// StringBuffer sql = new StringBuffer();
	// sql.append("insert into coupon_use_history
	// values('"+entity.getCustomerId()+"','"+entity.getCouponId()+"','"+entity.getOrderId()+"','"+entity.getProduct()+"','"+DateUtil.formatYYYYMMDDHHMMSS(entity.getUserDate())+"')");
	// log.info("sql语句：" + sql);
	// Connection conn = null;
	// PreparedStatement ps = null;
	// try {
	// conn = openConnection();
	// ps = conn.prepareStatement(sql.toString());
	// ps.executeUpdate();
	// log.info("<OrderDaoImpl>--<insert>--end");
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error(e);
	// return false;
	// } finally {
	// this.closeConnection(null, ps, conn);
	// }
	//
	// }
	@Override
	public boolean del(String orderIdList) {
		log.info("<OrderDaoImpl>--<del>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from store_order where id in (" + orderIdList + ")");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			int sign = ps.executeUpdate();
			if (sign > 0) {
				log.info("<OrderDaoImpl>--<del>--end");
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
	public ReturnDataUtil storeOrderFind(OrderForm orderform) {
		log.info("<MachineReplenishDaoImpl--storeOrderFind--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		if (orderform.getFindType() == 0) {
			sql.append(
					"select so.state,so.product,so.id,so.state,so.nowprice,so.payCode,so.createTime,so.type,m.useMoney from store_order  so left join member_use_log m on  m.orderType=2 and  so.id=m.orderId ");
			sql.append(" where openid=(select openId from tbl_customer_wx where customerId=" + orderform.getCustomerId()
					+ ") ");
			sql.append(" GROUP BY so.id order by so.createTime  desc ,so.state desc  ");
		} else if (orderform.getFindType() == 1) {
			sql.append(
					"select so.state,so.product,so.id,so.state,so.nowprice,so.payCode,so.createTime,so.type,m.useMoney from store_order so left join member_use_log m on  m.orderType=2 and  so.id=m.orderId ");
			sql.append(" where openid=(select openId from tbl_customer_wx where customerId=" + orderform.getCustomerId()
					+ ")  and so.state=10001");
			sql.append(" GROUP BY so.id order by so.createTime desc ");
		} else if (orderform.getFindType() == 2) {
			sql.append(
					"select so.state,so.product,so.id,so.state,so.nowprice,so.payCode,so.createTime,so.type,m.useMoney from store_order so left join member_use_log m on  m.orderType=2 and  so.id=m.orderId ");
			sql.append(" where openid=(select openId from tbl_customer_wx where customerId=" + orderform.getCustomerId()
					+ ")  and so.state=10002 ");
			sql.append(" GROUP BY so.id order by so.createTime desc ");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("商城订单查询：sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			long off = (orderform.getCurrentPage() - 1) * orderform.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + orderform.getPageSize());
			rs = pst.executeQuery();
			List<OrderBean> list = Lists.newArrayList();
			while (rs.next()) {
				OrderBean bean = new OrderBean();
				bean = new OrderBean();
				bean.setProduct(rs.getString("product"));
				bean.setId(rs.getInt("id"));
				bean.setStateName(PayStateEnum.findStateName(rs.getInt("state")));
				bean.setNowprice(rs.getBigDecimal("nowprice"));
				String createTime = rs.getString("createTime");
				String time = createTime.substring(0, createTime.indexOf("."));
				bean.setTime(time);
				bean.setPayCode(rs.getString("payCode"));
				List<ShoppingBean> shoppingList = findOrderIdByShoppingBean(rs.getLong("id"), orderform.getOrderType());
				bean.setType(rs.getInt("type"));
				bean.setList(shoppingList);
				bean.setState(rs.getInt("state"));
				bean.setUseMoney(rs.getBigDecimal("useMoney"));
				list.add(bean);
			}
			data.setCurrentPage(orderform.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<MachineReplenishDaoImpl--storeOrderFind----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public boolean update(OrderBean orderbean) {
		log.info("<MachineReplenishDaoImpl--update--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("update store_order set location=" + orderbean.getLocation() + ",distributionModel="
				+ orderbean.getDistributionModel() + ",couponId=" + orderbean.getCoupon() + ",payType="
				+ orderbean.getPayType() + ",nowprice=" + orderbean.getNowprice() + ",couponPrice="
				+ orderbean.getCouponPrice() + " where id=" + orderbean.getId());
		Connection conn = null;
		PreparedStatement pst = null;
		log.info("sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int sign = pst.executeUpdate();
			if (sign > 0) {
				log.info("<MachineReplenishDaoImpl--update----end>");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}

	@Override
	public int findOrderIdByProduct(String product) {
		log.info("<MachineReplenishDaoImpl--findOrderIdByProduct--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("select id from store_order where product='" + product + "'");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		log.info("sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				id = rs.getInt("id");
			}
			log.info("<MachineReplenishDaoImpl--findOrderIdByProduct--end>");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return id;

	}

	@Override
	public Map<String, Object> findSomeMessByOrderId(Integer orderId) {
		log.info("<MachineReplenishDaoImpl--findSomeMessByOrderId--start>");
		StringBuffer sql = new StringBuffer();
		Map<String, Object> map = new HashMap<>();
		sql.append(
				"select so.product,so.nowprice,so.payCode,so.coupon,c.name,so.companyId from store_order so left join coupon c on c.id=so.coupon where so.id="
						+ orderId);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		log.info("sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				map.put("product", rs.getString("product"));
				map.put("nowprice", rs.getDouble("nowprice"));
				map.put("payCode", rs.getString("payCode"));
				map.put("couponId", rs.getInt("coupon"));
				map.put("couponName", rs.getString("name"));
				map.put("companyId", rs.getInt("companyId"));
			}
			log.info("<MachineReplenishDaoImpl--findSomeMessByOrderId--end>");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return map;
	}

	@Override
	public List<String> addressFind(OrderForm orderform) {
		log.info("<MachineReplenishDaoImpl--addressFind--start>");
		StringBuffer sql = new StringBuffer();
		if (orderform.getCustomerId() != null) {
			sql.append(
					"select distinct location from store_order where openid=(select openId from tbl_customer_wx where customerId="
							+ orderform.getCustomerId() + ") ");
			Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			List<String> list = new ArrayList<>();
			log.info("sql>>>:" + sql.toString());
			try {
				conn = openConnection();
				pst = conn.prepareStatement(super.countSql(sql.toString()));
				rs = pst.executeQuery();
				long count = 0;
				while (rs.next()) {
					count = rs.getRow();
				}
				long off = (orderform.getCurrentPage() - 1) * orderform.getPageSize();
				pst = conn.prepareStatement(sql.toString());
				rs = pst.executeQuery();
				while (rs != null && rs.next()) {
					list.add(rs.getString("location"));

				}
				log.info("<MachineReplenishDaoImpl--addressFind----end>");
				return list;
			} catch (SQLException e) {
				e.printStackTrace();
				return list;
			} finally {
				this.closeConnection(rs, pst, conn);
			}
		}
		return null;
	}

	@Override
	public List<OrderDto> findOrderById(Long customerId, Integer type) {
		log.info("<OrderDaoImpl--findOrderByOpenId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(
				"  select  pr.id AS orderId, pr.payCode,pr.state,pr.createTime,pri.num itemNum,pr.price allPrice,pr.itemName allItemName,");
		sql.append(" ib.name itemName ,ib.pic,(pri.price/pri.num) itemPrice,pr.num,m.useMoney  from pay_record pr ");
		sql.append(" left JOIN pay_record_item pri on pr.id=pri.payRecordId ");
		sql.append("  left JOIN item_basic ib on pri.basicItemId = ib.id ");
		sql.append("  left JOIN tbl_customer  tc on pr.customerId = tc.`id` ");
		sql.append(" left join member_use_log m on  m.orderType=1 and  pr.id=m.orderId ");
		sql.append(" where 1=1 and pri.num>0 and pr.fakeFlag=0 and pr.state!=10008 and tc.id='"+customerId+"'");
		if (type != null){
			 if (type == 1){
				sql.append(" and pr.state = 10001");
			} else if (type == 2) {
				sql.append(" and pr.state = 10002 ");
			}
		}
		sql.append("  order by pr.createTime desc limit 30 ");
		log.info("机器订单查询sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<Long, OrderDto> map = new HashMap<Long, OrderDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				long orderId = rs.getLong("orderId");
				OrderDto orderDto = map.get(orderId);
				if (orderDto == null) {
					orderDto = new OrderDto();
					orderDto.setType(2);
					orderDto.setOrderId(orderId);
					orderDto.setPayCode(rs.getString("payCode"));
					String createTime = rs.getString("createTime");
					String time = createTime.substring(0, createTime.indexOf("."));
					orderDto.setTime(time);
					orderDto.setStateName(PayStateEnum.findStateName(rs.getInt("state")));
					orderDto.setNowprice(rs.getBigDecimal("allPrice"));
					orderDto.setUseMoney(rs.getBigDecimal("useMoney"));
					map.put(orderId, orderDto);
				}
				ShoppingBean bean = new ShoppingBean();

				bean.setItemName(rs.getString("itemName"));
				bean.setOrderId(rs.getLong("orderId"));
				if (rs.getBigDecimal("itemPrice") != null) {
					bean.setPrice(rs.getBigDecimal("itemPrice").doubleValue());
				}
				bean.setNum(rs.getInt("itemNum"));
				bean.setPic(rs.getString("pic"));
				orderDto.getList().add(bean);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		ArrayList<OrderDto> arrayList = new ArrayList<>(map.values());
		// 进行排序
		Collections.sort(arrayList, new Comparator<OrderDto>() {
			@Override
			public int compare(OrderDto dt1, OrderDto dt2) {
				if (dt1.getOrderId() > dt2.getOrderId()) {
					return -1;
				} else if (dt1.getOrderId() < dt2.getOrderId()) {
					return 1;
				} else {// 相等
					return 0;
				}
			}
		});
		log.info("<OrderDaoImpl--findOrderByOpenId--end>");
		return arrayList;
	}

	@Override
	public ReturnDataUtil customerCoupon(OrderForm orderform) {
		log.info("<OrderDaoImpl--customerCoupon--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		sql.append(" SELECT c.id,c.name,c.money,c.deductionMoney FROM coupon AS c");
		sql.append(" INNER JOIN coupon_customer AS cc ON c.`id` = cc.`couponId`");
		sql.append(" LEFT JOIN coupon_product AS cp ON c.id = cp.`couponId`");
		sql.append(" WHERE cc.state = 1 AND cc.quantity > 0 AND cc.customerId = " + orderform.getCustomerId());
		sql.append(" AND c.useWhere=2 AND c.deleteFlag=0 and c.money<=" + orderform.getPrice());
		sql.append(" AND cc.`startTime`<='" + now + "' AND cc.`endTime`>='" + now + "'");
		sql.append(
				" AND (CASE  WHEN c.bindProduct= 1 AND cp.`deleteFlag` = 0 AND cp.`productId` in (select itemId from shopping_car where id in ( "
						+ orderform.getProduct() + ")) THEN 1");
		sql.append(" WHEN bindProduct=0 THEN 1");
		sql.append(" ELSE 0 END) = 1");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			long off = (orderform.getCurrentPage() - 1) * orderform.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + orderform.getPageSize());
			rs = pst.executeQuery();
			List<CustomerCoupon> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				CustomerCoupon bean = new CustomerCoupon();
				bean = new CustomerCoupon();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setMoney(rs.getDouble("money"));
				bean.setDeductionMoney(rs.getDouble("deductionMoney"));
				list.add(bean);
			}
			data.setCurrentPage(orderform.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<OrderDaoImpl--customerCoupon----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil storeOrderFind(Integer customerId) {
		log.info("<OrderDaoImpl--storeOrderFind--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select id,coupon,product,createTime from store_order where openid=(select openId from tbl_customer_wx where customerId="
						+ customerId + ") ");
		sql.append(" GROUP BY id order by createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<OrderBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				OrderBean bean = new OrderBean();
				bean = new OrderBean();
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setId(rs.getInt("id"));
				bean.setCoupon(rs.getInt("coupon"));
				bean.setProduct(rs.getString("product"));
				list.add(bean);
			}
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<OrderDaoImpl--storeOrderFind----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<ShoppingBean> findCustomerByProduce(String product) {
		log.info("<OrderDaoImpl--findCustomerByProduce--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT itemId,itemName,price,num FROM shopping_car WHERE id IN (" + product + ")");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ShoppingBean order = null;
		List<ShoppingBean> orderList = new ArrayList<ShoppingBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				order = new ShoppingBean();
				order.setItemName(rs.getString("itemName"));
				order.setNum(rs.getInt("num"));
				order.setPrice(rs.getDouble("price"));
				order.setItemId(rs.getInt("itemId"));
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findCustomerByProduce--end>");
		return orderList;
	}

	@Override
	public int findCouponIdByItemId(Integer itemId) {
		log.info("<OrderDaoImpl--findCouponIdByItemId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT couponId FROM coupon_product WHERE productId=" + itemId + " AND deleteFlag=0");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int couponId = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				couponId = rs.getInt("couponId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findCouponIdByItemId--end>");
		return couponId;
	}

	@Override
	public boolean insert(OrderDetile ob) {
		log.info("<OrderDaoImpl>--<insert>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into store_order_detile(orderId,itemId,itemName,price,num,createTime,customerId) values('"
				+ ob.getOrderId() + "','" + ob.getItemId() + "','" + ob.getItenName() + "','" + ob.getPrice() + "','"
				+ ob.getNum() + "','" + DateUtil.formatYYYYMMDDHHMMSS(ob.getCreateTime()) + "','" + ob.getCustomerId()
				+ "')");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
			log.info("<OrderDaoImpl>--<insert>--end");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			return false;
		} finally {
			this.closeConnection(null, ps, conn);
		}

	}

	@Override
	public List<Integer> findItemIdByCouponId(Integer s) {
		log.info("<OrderDaoImpl--findItemIdByCouponId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT productId FROM coupon_product WHERE couponId =" + s);
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Integer> list = new ArrayList<>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				list.add(rs.getInt("productId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findItemIdByCouponId--end>");
		return list;
	}

	@Override
	public List<Integer> findCouponByCustomerId(Integer customerId) {
		log.info("<OrderDaoImpl--findCouponByCustomerId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT couponId FROM coupon_customer WHERE customerId =" + customerId);
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Integer> list = new ArrayList<>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				list.add(rs.getInt("couponId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findCouponByCustomerId--end>");
		return list;
	}

	@Override
	public void updateState(Long customerId, Integer couponId) {
		log.info("<MachineReplenishDaoImpl--updateState--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE coupon_customer SET quantity = quantity - 1, ");
		sql.append(" state = (CASE WHEN quantity > 0 THEN 1 ELSE  2 END)");
		sql.append(" WHERE state = 1 AND couponId = " + couponId + " AND customerId =" + customerId);
		Connection conn = null;
		PreparedStatement pst = null;
		log.info("sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			pst.executeUpdate();
			log.info("<MachineReplenishDaoImpl--updateState----end>");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, pst, conn);
		}

	}

	@Override
	public void insert(CouponLog couponLog) {
		log.info("<OrderDaoImpl>--<insert>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(
				"insert into coupon_use_log(couponId,couponCustomerId,orderId,type,money,createTime,deductionMoney) values('"
						+ couponLog.getCouponId() + "','" + couponLog.getCouponCustomerId() + "','"
						+ couponLog.getOrderId() + "','" + couponLog.getType() + "','" + couponLog.getMoney() + "','"
						+ DateUtil.formatYYYYMMDDHHMMSS(couponLog.getCreateTime()) + "','"
						+ couponLog.getDeductionMoney() + "')");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
			log.info("<OrderDaoImpl>--<insert>--end");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, ps, conn);
		}

	}

	/**
	 * 根据openId 查询用户id
	 */
	@Override
	public Long findCustomerIdByOpenId(String openId) {
		log.info("<OrderDaoImpl>-----<findCustomerIdByOpenId>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT customerId FROM tbl_customer_wx WHERE openId='" + openId + "' ");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long customerId = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				customerId = rs.getLong("customerId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl>-----<findCustomerIdByOpenId>------end");
		return customerId;

	}

	/**
	 * 查询订单详情
	 */
	@Override
	public List<ShoppingBean> findOrderIdByShoppingBean(Long orderId, Integer orderType) {
		log.info("<OrderDaoImpl>----<findOrderIdByShopping>-----start>");
		StringBuffer sql = new StringBuffer();
		if (orderType == 1) {// 商城订单
			sql.append(" select itemId,itemName,price,num,pic  from store_order_detile sod ");
			sql.append(" left join shopping_goods sg on sod.itemId=sg.id where sod.orderId='" + orderId + "' ");
			sql.append(" order by sod.createTime desc ");
		}
		log.info("查询订单详情sql语句：" + sql.toString());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ShoppingBean order = null;
		List<ShoppingBean> orderList = new ArrayList<ShoppingBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				order = new ShoppingBean();
				order.setItemId(rs.getInt("itemId"));
				order.setItemName(rs.getString("itemName"));
				order.setNum(rs.getInt("num"));
				order.setPrice(rs.getDouble("price"));
				order.setPic(rs.getString("pic"));
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl>-----<findOrderIdByShopping>----end>");
		return orderList;
	}

	@Override
	public List<PayRecordItemDto> getPayRecordItemList(String payCode) {
		log.info("<OrderDaoImpl>-----<getPayRecordItemList>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pri.`itemName`,pri.`num`,ib.`pic`,pri.`price` AS originalPrice,");
		sql.append(" pr.`nowprice` AS sumPrice FROM `store_order` AS pr");
		sql.append(" INNER JOIN store_order_detile AS pri ON pri.`orderId` = pr.`id`");
		sql.append(" INNER JOIN `shopping_goods` AS ib ON ib.`id` = pri.`itemId`");
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
				item.setRealTotalPrice(item.getOriginalPrice());
				item.setSumPrice(rs.getBigDecimal("sumPrice"));
				itemList.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl>-----<getPayRecordItemList>----end>");
		return itemList;
	}

	@Override
	public boolean updateOrderStateById(Long orderId, PayStateEnum state) {
		log.info("<OrderDaoImpl>-----<updateOrderStateById>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE store_order SET state = '" + state.getState() + "' WHERE state = 10002 and id = '" + orderId
				+ "'");
		log.info("sql语句：" + sql);
		int upate = upate(sql.toString());
		log.info("<OrderDaoImpl>-----<updateOrderStateById>----end>");
		if (upate > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Long getItemSalesNum(Long itemId) {
		log.info("<OrderDaoImpl>-----<getItemSalesNum>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) as salesNum from store_order_detile as sod");
		sql.append(" INNER JOIN shopping_goods AS sg ON sod.`itemId` = sg.`id`");
		sql.append(" inner join store_order as so on so.`id` = sod.`orderId`");
		sql.append(" where so.state = '10001' and sg.`id` = '" + itemId + "'");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long salesNum = 0L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				salesNum = rs.getLong("salesNum");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl>-----<getItemSalesNum>----end>");
		return salesNum;
	}

	@Override
	public ReturnDataUtil mySpellgroupOrder(OrderForm orderForm) {
		log.info("<OrderDaoImpl>----<mySpellgroupOrder>----start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(
				" select so.id,so.state,so.product,so.state,so.nowprice,so.payCode,so.createTime,so.type,so.customerGroupId,so.spellgroupState,tcs.startCustomerId,sgs.minimumGroupSize,sgs.allowRefund  ");
		sql.append(
				" ,tcw.CustomerId,tcw.nickname,tcw.headimgurl from store_order  so left join tbl_customer_spellgroup tcs on so.customerGroupId=tcs.id ");
		sql.append(" left join shopping_goods_spellgroup sgs on tcs.spellGroupId=sgs.id ");
		sql.append(
				" left join tbl_customer_wx tcw on (tcs.startCustomerId=tcw.customerId or FIND_IN_SET(tcw.customerId,tcs.participationCustomerId)) ");
		sql.append(" where   so.type=3 and so.openid=(select openId from tbl_customer_wx where customerId="
				+ orderForm.getCustomerId() + " )  ");
		if (orderForm.getFindType() == 0) {// 全部订单
			sql.append(" ");
		} else if (orderForm.getFindType() == 1) {// 未支付
			sql.append(" and so.state=10002 ");
		} else if (orderForm.getFindType() == 2) {// 待分享
			sql.append(" and so.state=10001 and so.spellgroupState=1 ");
		} else if (orderForm.getFindType() == 3) {// 待取货
			sql.append(" and so.state=10001 and so.spellgroupState=2 ");
		} else if (orderForm.getFindType() == 4) {// 已完成
			sql.append(" and so.state=10001 and so.spellgroupState=4 ");
		} else if (orderForm.getFindType() == 5) {// 已关闭
			sql.append(" and (so.state=10006 or so.spellgroupState=3) ");
		}
		sql.append(" order by so.createTime  desc ,so.state desc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("我的拼团订单查询：sql>>>:" + sql.toString());
		Map<Long, OrderBean> map = new HashMap<Long, OrderBean>();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			long off = (orderForm.getCurrentPage() - 1) * orderForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + orderForm.getPageSize());
			rs = pst.executeQuery();
			List<OrderBean> list = Lists.newArrayList();
			while (rs.next()) {
				Long id = rs.getLong("id");
				OrderBean bean = map.get(id);
				if (bean == null) {
					bean = new OrderBean();
					bean.setProduct(rs.getString("product"));
					bean.setId(rs.getInt("id"));
					bean.setStateName(PayStateEnum.findStateName(rs.getInt("state")));
					bean.setNowprice(rs.getBigDecimal("nowprice"));
					String createTime = rs.getString("createTime");
					String time = createTime.substring(0, createTime.indexOf("."));
					bean.setTime(time);
					bean.setPayCode(rs.getString("payCode"));
					List<ShoppingBean> shoppingList = findOrderIdByShoppingBean(rs.getLong("id"), 1);
					bean.setType(rs.getInt("type"));
					bean.setList(shoppingList);
					bean.setState(rs.getInt("state"));
					bean.setSpellgroupState(rs.getInt("spellgroupState"));
					bean.setAllowRefund(rs.getInt("allowRefund"));
					bean.setCustomerGroupId(rs.getLong("customerGroupId"));
					// 成团总人数
					bean.setPeopleNum(rs.getInt("minimumGroupSize"));
					// 判断是否是拼团发起人 0是发起人 1 参与者
					Long startCustomerId = rs.getLong("startCustomerId");
					if (orderForm.getCustomerId().equals(startCustomerId)) {
						bean.setIsInitiator(0);
					} else {
						bean.setIsInitiator(1);
					}
					map.put(id, bean);
				}
				SpellGroupCustomerBean bean2 = new SpellGroupCustomerBean();
				bean2.setCustomerId(rs.getLong("customerId"));
				bean2.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean2.setHeadimgurl(rs.getString("headimgurl"));
				bean.getCustomerSpellGroupList().add(bean2);
			}
			ArrayList<OrderBean> arrayList = new ArrayList<>(map.values());
			// 进行排序
			Collections.sort(arrayList, new Comparator<OrderBean>() {
				@Override
				public int compare(OrderBean dt1, OrderBean dt2) {
					if (dt1.getId() > dt2.getId()) {
						return -1;
					} else if (dt1.getId() < dt2.getId()) {
						return 1;
					} else {// 相等
						return 0;
					}
				}
			});
			data.setCurrentPage(orderForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(arrayList);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<OrderDaoImpl>----<mySpellgroupOrder>----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil pickUpAddress(Long spellgroupId) {
		log.info("<OrderDaoImpl>----<pickUpAddress>----start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append(" select locatoinName from vending_machines_info where ");
		sql.append(
				" FIND_IN_SET(code,(select vmCode from shopping_goods_spellgroup where id=" + spellgroupId + " ) ) ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询团购商品取货地址：sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<Map<String, Object>> list = Lists.newArrayList();
			Integer id = 0;
			while (rs.next()) {
				id++;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("name", rs.getString("locatoinName"));
				list.add(map);
			}
			data.setReturnObject(list);
			data.setStatus(1);
			data.setMessage("查询成功");
			log.info("<OrderDaoImpl>----<pickUpAddress>----end>");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ShoppingDto orderParticulars(Long orderId) {
		log.info("<OrderDaoImpl>----<orderParticulars>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(
				" select s.id orderId, s.spellgroupState,s.state,s.payCode,s.ptCode,s.payType,s.spellgroupState,s.nowprice,s.customerGroupId,sod.itemId,sod.itemName, ");
		sql.append(
				" sod.price,sod.num,sg.pic,tcs.spellGroupId,tcs.startCustomerId,sgs.allowRefund,sgs.minimumGroupSize,tcw.CustomerId,tcw.nickname,tcw.headimgurl from  store_order s ");
		sql.append(
				" left join store_order_detile sod on s.id=sod.orderId left join  shopping_goods sg on sod.itemId=sg.id ");
		sql.append(
				" left join tbl_customer_spellgroup tcs  on s.customerGroupId=tcs.id left join shopping_goods_spellgroup sgs on tcs.spellGroupId=sgs.id ");
		sql.append(
				" left join tbl_customer_wx tcw on (tcs.startCustomerId=tcw.customerId or FIND_IN_SET(tcw.customerId,tcs.participationCustomerId)) ");
		sql.append(" where s.id=" + orderId);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ShoppingDto shoppingDto = null;
		Map<Long, ShoppingDto> map = new HashMap<Long, ShoppingDto>();
		log.info("拼团订单详情：sql>>>:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				Long id = rs.getLong("orderId");
				shoppingDto = map.get(id);
				if (shoppingDto == null) {
					shoppingDto = new ShoppingDto();
					shoppingDto.setOrderId(id);
					shoppingDto.setPayCode(rs.getString("payCode"));
					shoppingDto.setNewPrice(rs.getBigDecimal("nowprice"));
					shoppingDto.setCustomerGroupId(rs.getLong("customerGroupId"));
					shoppingDto.setItemId(rs.getLong("itemId"));
					shoppingDto.setItemName(rs.getString("itemName"));
					shoppingDto.setPrice(rs.getBigDecimal("price"));
					;
					shoppingDto.setNum(rs.getInt("num"));
					shoppingDto.setPic(rs.getString("pic"));
					shoppingDto.setSpellGroupId(rs.getLong("spellGroupId"));
					shoppingDto.setAllowRefund(rs.getInt("allowRefund"));
					shoppingDto.setState(rs.getLong("state"));
					shoppingDto.setSpellgroupState(rs.getInt("spellgroupState"));
					shoppingDto.setMinimumGroupSize(rs.getInt("minimumGroupSize"));
					shoppingDto.setPtCode(rs.getString("ptCode"));
					shoppingDto.setPayType(rs.getInt("payType"));
					// 判断是否是拼团发起人 0是发起人 1 参与者
					Long startCustomerId = rs.getLong("startCustomerId");
					if (CustomerUtil.getCustomerId().equals(startCustomerId)) {
						shoppingDto.setIsInitiator(0);
					} else {
						shoppingDto.setIsInitiator(1);
					}
					map.put(id, shoppingDto);
				}
				SpellGroupCustomerBean bean2 = new SpellGroupCustomerBean();
				bean2.setStartCustomerId(rs.getLong("startCustomerId"));
				bean2.setCustomerId(rs.getLong("customerId"));
				bean2.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean2.setHeadimgurl(rs.getString("headimgurl"));
				shoppingDto.getCustomerSpellGroupList().add(bean2);

			}
			log.info("<OrderDaoImpl>----<orderParticulars>----end>");
			return shoppingDto;
		} catch (SQLException e) {
			e.printStackTrace();
			return shoppingDto;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public boolean isPaymentAllowed(Long orderId) {
		log.info("<OrderDaoImpl>----<isPaymentAllowed>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.id orderId,s.customerGroupId,tcs.state from store_order s  ");
		sql.append(" left join tbl_customer_spellgroup tcs on s.customerGroupId=tcs.id ");
		sql.append(" where  tcs.state=1 and s.id='" + orderId + "'");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean flag = true;
		log.info("发起支付判断该团是否拼成功sql语句：" + sql);
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				flag = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl>----<isPaymentAllowed>----end>");
		return flag;
	}

	@Override
	public Integer getCompanyIdByPayCode(String payCode) {
		log.info("<OrderDaoImpl>-----<getCompanyIdByPayCode>--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select companyId from store_order where payCode='" + payCode + "' ");
		log.info("根据payCode查询公司id sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer companyId = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				companyId = rs.getInt("companyId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl>-----<getCompanyIdByPayCode>--end>");
		return companyId;
	}

	@Override
	public int paySuccessStroeOrder(String outTradeNo, String transactionId, Integer type) {
		log.info("<OrderDaoImpl>----<paySuccessStroeOrder>----start>");
		StringBuffer sql = new StringBuffer();
		if (type == 0) {
			sql.append(" update store_order set ptCode='" + transactionId
					+ "', payTime=current_timestamp() ,payType=1 , state=" + PayStateEnum.PAY_SUCCESS.getState() + " ");
			sql.append("  where payCode='" + outTradeNo + "'");
		} else {
			sql.append(" update store_order set ptCode='" + transactionId
					+ "',payTime=current_timestamp() ,payType=1 ,state=" + PayStateEnum.PAY_SUCCESS.getState() + " ");
			sql.append(" ,spellgroupState=" + type + "  where payCode='" + outTradeNo + "'");
		}
		log.info("微信完成商城订单后的回调，用以确认订单是否完成支付，并更新状态sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<OrderDaoImpl>----<paySuccessStroeOrder>----end>");
		return result;
	}

	@Override
	public int buyCount(Long customerId, Long goodsId) {
		log.info("<OrderDaoImpl--findOrderByOpenId--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) as count  from store_order so LEFT JOIN store_order_detile sod on so.id=sod.orderId ");
		sql.append(" where sod.customerId='"+customerId+"' and sod.itemId='"+goodsId+"'  ");
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				count=rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<OrderDaoImpl--findOrderByOpenId--end>");
		return count;
	}
}
