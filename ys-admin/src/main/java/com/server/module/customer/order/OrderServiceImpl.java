package com.server.module.customer.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.customer.CustomerUtil;
import com.server.module.customer.car.ShoppingCarBean;
import com.server.module.customer.car.ShoppingCarDao;
import com.server.module.customer.coupon.CouponBean;
import com.server.module.customer.coupon.CouponCustomerBean;
import com.server.module.customer.coupon.CouponDao;
import com.server.module.customer.coupon.CouponService;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsDao;
import com.server.module.customer.product.shoppingGoodsProduct.ShoppingGoodsProductBean;
import com.server.module.customer.product.shoppingGoodsProduct.ShoppingGoodsProductDao;
import com.server.module.customer.product.shoppingGoodsSpellGroup.ShoppingGoodsSpellGroupBean;
import com.server.module.customer.product.shoppingGoodsSpellGroup.ShoppingGoodsSpellGroupDao;
import com.server.module.customer.product.tblCustomerSpellGroup.TblCustomerSpellGroupBean;
import com.server.module.customer.product.tblCustomerSpellGroup.TblCustomerSpellGroupDao;
import com.server.module.customer.userInfo.address.AddressBean;
import com.server.module.customer.userInfo.address.AddressService;
import com.server.module.customer.userInfo.stock.CustomerStockBean;
import com.server.module.customer.userInfo.stock.OrderDetailBean;
import com.server.module.customer.userInfo.stock.StoreOrderDao;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerDao;
import com.server.module.system.memberManage.memberManager.MemberBean;
import com.server.module.system.memberManage.memberManager.MemberDao;
import com.server.module.system.memberManage.memberManager.MemberUseLog;
import com.server.module.system.statisticsManage.payRecord.PayRecordItemDto;
import com.server.redis.RedisClient;
import com.server.util.IDUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CouponEnum;
import com.server.util.stateEnum.PayStateEnum;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger log = LogManager.getLogger(OrderServiceImpl.class);
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ShoppingCarDao shoppingCarDaoImpl;
	@Autowired
	private ShoppingGoodsDao shoppingGoodsDaoImpl;
	@Autowired
	private StoreOrderDao storeOrderDao;
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponService couponService;
	@Autowired
	private ShoppingGoodsProductDao shoppingGoodsProductDaoImpl;
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;
	@Autowired
	private CarryWaterVouchersCustomerDao carryWaterVouchersCustomerDaoImpl;
	@Autowired
	private MemberDao memberDaoImpl;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ShoppingGoodsSpellGroupDao shoppingGoodsSpellGroupDaoImpl;
	@Autowired
	private TblCustomerSpellGroupDao tblCustomerSpellGroupDaoImpl;

	@Override
	public List<OrderDto> findOrderByOpenId(String openId) {
		log.info("<OrderServiceImpl--findOrderByOpenId--start>");
		List<OrderDto> findOrderByOpenId = orderDao.findOrderByOpenId(openId);
		log.info("<OrderServiceImpl--findOrderByOpenId--end>");
		return findOrderByOpenId;
	}

	@Override
	public ReturnDataUtil add(OrderForm orderform) {
		log.info("<OrderServiceImpl--add--start>");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		User user = UserUtils.getUser();
		orderform.setCustomerId(user.getId());
		String openId = "";
		if (orderform.getCustomerId() != null) {
			openId = orderDao.findOpenIdByCustomerId(orderform.getCustomerId());
			if (StringUtils.isBlank(openId)) {
				returnDataUtil.setStatus(2);
				returnDataUtil.setMessage("非微信用户，订单创建失败！");
			}
		}
		Long companyId = null;
		Date date = new Date();
		String[] split = orderform.getProduct().split(",");
		for (String s : split) {
			ShoppingCarBean shoppingCarBean = shoppingCarDaoImpl.get(s);
			if (shoppingCarBean != null) {
				ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(shoppingCarBean.getItemId());
				companyId = shoppingGoodsBean.getCompanyId();
				if (shoppingGoodsBean.getState() == 5101) {
					returnDataUtil.setReturnObject(false);
					returnDataUtil.setStatus(2);
					returnDataUtil.setMessage("亲，不好意思，" + shoppingGoodsBean.getName() + "已暂停销售，请选择其他商品购买！给您带来不便，深感抱歉！");
					return returnDataUtil;
				} else {
					if (shoppingCarBean.getNum() > shoppingGoodsBean.getQuantity()) {// 库存不足
						returnDataUtil.setReturnObject(false);
						returnDataUtil.setStatus(2);
						returnDataUtil.setMessage(shoppingGoodsBean.getName() + "库存数量不足");
						return returnDataUtil;
					}
					if (shoppingGoodsBean.getVouchersId() > 0) {// 提水券过期
						CarryWaterVouchersBean bean = carryWaterVouchersDaoImpl.get(shoppingGoodsBean.getVouchersId());
						if (date.after(bean.getEndTime())) {
							returnDataUtil.setReturnObject(false);
							returnDataUtil.setStatus(2);
							returnDataUtil.setMessage(shoppingGoodsBean.getName() + "已下架");
							return returnDataUtil;
						}
					}
					if (StringUtils.isNotBlank(shoppingGoodsBean.getVouchersIds())) {// 套餐提水券过期
						List<String> vouchers = Arrays
								.asList(StringUtils.split(shoppingGoodsBean.getVouchersIds(), ","));
						for (String v : vouchers) {
							CarryWaterVouchersBean bean = carryWaterVouchersDaoImpl.get(Long.valueOf(v));
							if (date.after(bean.getEndTime())) {
								returnDataUtil.setReturnObject(false);
								returnDataUtil.setStatus(2);
								returnDataUtil.setMessage(shoppingGoodsBean.getName() + "已下架");
								return returnDataUtil;
							}
						}
					}
					if (shoppingGoodsBean.getIsHelpOneself() == 0) {// 是否自提
						List<AddressBean> addressList = addressService.select(user.getId());
						if (addressList == null || addressList.size() < 1) {
							returnDataUtil.setReturnObject(false);
							returnDataUtil.setStatus(2);
							returnDataUtil.setMessage("请先填写收货地址");
							return returnDataUtil;
						}
					}

					boolean changeInventory = shoppingGoodsDaoImpl.changeInventory(shoppingCarBean.getNum(),
							shoppingGoodsBean.getId());
					if (!changeInventory) {
						returnDataUtil.setReturnObject(false);
						returnDataUtil.setStatus(2);
						returnDataUtil.setMessage(shoppingGoodsBean.getName() + "库存数量不足!");
						return returnDataUtil;
					}

					if (shoppingGoodsBean.getPurchaseLimitation() != null && shoppingGoodsBean.getPurchaseLimitation()>0) {// 判断购买次数是否限制
						int buyCount = orderDao.buyCount(orderform.getCustomerId(), shoppingGoodsBean.getId());
						if (shoppingGoodsBean.getPurchaseLimitation() <= buyCount) {
							returnDataUtil.setReturnObject(false);
							returnDataUtil.setStatus(2);
							returnDataUtil.setMessage("亲，不好意思，" + shoppingGoodsBean.getName() + "限购"+shoppingGoodsBean.getPurchaseLimitation()+"件，请选择其他商品购买！给您带来不便，深感抱歉！");
							return returnDataUtil;
						}
					}

				}
			}

		}
		List<Long> list = new ArrayList<>();
		for (int a = 0; a < split.length; a++) {
			list.add(Long.parseLong(split[a]));
		}
		OrderBean ob = new OrderBean();
		ob.setNum(split.length);
		if (orderform != null) {
			if (StringUtils.isNotBlank(orderform.getProduct())) {
				ob.setProduct(orderform.getProduct());
			}
			if (StringUtils.isNotBlank(orderform.getLocation())) {
				ob.setLocation(orderform.getLocation());
			}
			if (orderform.getDistributionModel() != null) {
				ob.setDistributionModel(orderform.getDistributionModel());
			}
			if (orderform.getPayType() != null) {
				ob.setPayType(orderform.getPayType());
			}
			if (orderform.getAddressId() != null) {
				ob.setAddressId(orderform.getAddressId());
			}
		}
		ob.setCompanyId(companyId);
		ob.setOpenid(openId);
		ob.setType(1);
		ob.setState(PayStateEnum.NOT_PAY.getState());
		BigDecimal price = new BigDecimal(String.valueOf(orderDao.findSumPriceByProduct(orderform.getProduct())));
		ob.setPrice(price);
		String payCode = IDUtil.getPayCode();
		ob.setPayCode(payCode);
		ob.setCustomerId(orderform.getCustomerId());
		ob.setCreateTime(new Date());
		if (orderform.getCoupon() != null) {
			ob.setCoupon(orderform.getCoupon());
			// 直接减?
			BigDecimal deductionMoney = new BigDecimal(
					String.valueOf(orderDao.findDeductionMoneyByCoupon(orderform.getCoupon())));
			if (price.compareTo(deductionMoney) >= 0) {
				ob.setNowprice(price.subtract(deductionMoney));
				// 记录满减/固定券抵扣金额
				redisClient.set("SumDeductionMoney_" + openId, deductionMoney.toString(), 60 * 5);
			} else {
				redisClient.set("SumDeductionMoney_" + openId, ob.getNowprice().toString(), 60 * 5);
				ob.setNowprice(new BigDecimal(0));
				ob.setState(PayStateEnum.PAY_SUCCESS.getState());
			}
			ob.setCouponPrice(deductionMoney);
			redisClient.set("CouponId_" + openId, orderform.getCoupon().toString(), 60 * 5);
			CouponBean cb = couponService.get(orderform.getCoupon());
			redisClient.set("CouponName_" + openId, cb.getName(), 60 * 5);
		} else {
			ob.setCoupon(0);
			ob.setNowprice(price);
			ob.setCouponPrice(new BigDecimal(0));
		}
		// 判断用户是否有余额查询用户是否有余额
		BigDecimal money = null;
		MemberBean memberBean = memberDaoImpl.findBean(orderform.getCustomerId());
		// 支付金额大于余额 直接减去余额
		if (memberBean.getUserBalance() > 0) {
			log.info("===========用户余额抵扣============");
			if (price.compareTo(new BigDecimal(memberBean.getUserBalance())) <= 0) {// 支付金额小于余额 直接减去支付金额
				redisClient.set("MemberMoney_" + openId, price.toString(), 60 * 5);
				redisClient.set("PayType_" + openId, "1", 300);// 1 余额支付标识
				ob.setNowprice(new BigDecimal(0));
				ob.setState(PayStateEnum.PAY_SUCCESS.getState());
				ob.setPayType(3);//余额支付
				ob.setPayTime(new Date());
				money = price;
			}
		} else {
			ob.setCoupon(0);
			ob.setNowprice(price);
			ob.setCouponPrice(new BigDecimal(0));
		}

		OrderBean bean = orderDao.insert(ob);
		if (bean != null) {
			OrderDetile obs = new OrderDetile();
			obs.setCustomerId(orderform.getCustomerId());
			obs.setCreateTime(bean.getCreateTime());
			obs.setOrderId(bean.getId());
			List<ShoppingBean> newlist = orderDao.findCustomerByProduce(bean.getProduct());
			for (int b = 0; b < newlist.size(); b++) {
				obs.setItemId(newlist.get(b).getItemId());
				obs.setItenName(newlist.get(b).getItemName());
				obs.setPrice(newlist.get(b).getPrice());
				obs.setNum(newlist.get(b).getNum());
				orderDao.insert(obs);
			}
			if ((PayStateEnum.PAY_SUCCESS.getState()).equals(bean.getState())) {
				// 更新用户余额
				memberDaoImpl.updateUserBalance(orderform.getCustomerId(), money);
				// 插入使用会员余额记录
				MemberUseLog memberLog = new MemberUseLog();
				memberLog.setCustomerId(orderform.getCustomerId());
				memberLog.setOrderId(bean.getId());
				memberLog.setUseMoney(money);
				memberLog.setOrderType(2);
				memberDaoImpl.insertMemberUseLog(memberLog);
				// 给商城顾客增加存水

				List<OrderDetailBean> orderDetail = storeOrderDao.getOrderDetail(payCode);
				for (OrderDetailBean orderDetailBean : orderDetail) {
					ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(orderDetailBean.getItemId());
					// 购买优惠券类型以及提水券 用户不增加存水
					if (shoppingGoodsBean.getTypeId().equals(25l)) {
						CouponBean couponBean = couponDao.getCouponInfoByProduct(shoppingGoodsBean);
						CouponCustomerBean couponCustomerBean = couponDao
								.getCouponCustomerBean(UserUtils.getUser().getId(), couponBean.getId());
						if (couponCustomerBean != null) {
							couponCustomerBean.setQuantity(couponCustomerBean.getQuantity() + orderDetailBean.getNum());
							couponCustomerBean
									.setSumQuantity(couponCustomerBean.getSumQuantity() + orderDetailBean.getNum());// 券的总数
							couponDao.updateCouponCustomerBean(couponCustomerBean);
						} else {
							CouponCustomerBean couCusBean = new CouponCustomerBean();
							couCusBean.setCouponId(couponBean.getId());
							couCusBean.setCustomerId(UserUtils.getUser().getId());
							couCusBean.setStartTime(couponBean.getLogicStartTime());
							couCusBean.setEndTime(couponBean.getLogicEndTime());
							couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
							couCusBean.setQuantity(orderDetailBean.getNum() * 1l);
							couCusBean.setSumQuantity(orderDetailBean.getNum() * 1l);// 总券数
							couponService.insertCouponCustomer(couCusBean);
						}
					} else if (shoppingGoodsBean.getTypeId().equals(26l) || shoppingGoodsBean.getTypeId().equals(27l)) {// 提水券
																														// 不增加存水
						// 得到提水券信息
						CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl
								.get(shoppingGoodsBean.getVouchersId());
						// 得到本次购买数量和下发张数 最终为下发张数
						Integer num = carryWaterVouchersBean.getSendMax() * orderDetailBean.getNum();
						log.info("最终下发张数======" + num);
						CarryWaterVouchersCustomerBean entity = new CarryWaterVouchersCustomerBean();
						entity.setCarryId(shoppingGoodsBean.getVouchersId());
						entity.setCustomerId(UserUtils.getUser().getId());
						entity.setStartTime(carryWaterVouchersBean.getLogicStartTime());
						entity.setEndTime(carryWaterVouchersBean.getLogicEndTime());
						entity.setQuantity(num.longValue());
						entity.setCreateUser(UserUtils.getUser().getId());
						entity.setOrderId(bean.getId());
						carryWaterVouchersCustomerDaoImpl.insert(entity);
					} else {
						// 判断用户选择的配送方式 1自取 0非自取
						if (orderform.getDistributionModel() != null) {
							if (orderform.getDistributionModel() == 1) {// 自取 直接去给用户存水
								CustomerStockBean stock = storeOrderDao.getStock(orderDetailBean.getItemId(),
										orderDetailBean.getCustomerId());
								if (stock == null) {
									stock = new CustomerStockBean();
									stock.setBasicItemId(shoppingGoodsBean.getBasicItemId());
									stock.setItemId(orderDetailBean.getItemId());
									stock.setItemName(orderDetailBean.getItemName());
									stock.setCustomerId(orderDetailBean.getCustomerId());
									stock.setStock(orderDetailBean.getNum());
									stock.setPickNum(0);
									storeOrderDao.insert(stock);
								} else {
									stock.setStock(stock.getStock() + orderDetailBean.getNum());
									storeOrderDao.update(stock);
								}
							} else {// 非自取 判断用户购买的商品订单是否有绑定套餐 如果有 加入存水
									// 根据商城商品id 查询绑定商品信息
								List<ShoppingGoodsProductBean> shoppingGoodsProductBeanList = shoppingGoodsProductDaoImpl
										.getShoppingGoodsProductBean(orderDetailBean.getItemId().longValue());
								if (shoppingGoodsProductBeanList != null && shoppingGoodsProductBeanList.size() > 0) {
									for (ShoppingGoodsProductBean shoppingGoodsProductBean : shoppingGoodsProductBeanList) {
										// 获取用户 拥有当前商品的存量 如果没有就新增 存在就修改
										CustomerStockBean stock = storeOrderDao.getStock(
												shoppingGoodsProductBean.getGoodsId().intValue(),
												orderDetailBean.getCustomerId());
										if (stock == null) {
											stock = new CustomerStockBean();
											stock.setBasicItemId(shoppingGoodsProductBean.getItemId());
											stock.setItemId(shoppingGoodsProductBean.getGoodsId().intValue());
											stock.setItemName(shoppingGoodsProductBean.getItemName());
											stock.setCustomerId(orderDetailBean.getCustomerId());
											stock.setStock(orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue());
											stock.setPickNum(0);
											storeOrderDao.insert(stock);
										} else {
											int num = orderDetailBean.getNum()
													* shoppingGoodsProductBean.getQuantity().intValue();
											stock.setStock(stock.getStock() + num);
											storeOrderDao.update(stock);
										}
									}

								}
							}
						}

					}
				}
			}
			if (bean.getCoupon() != 0) {
				CouponLog couponLog = new CouponLog();
				couponLog.setCouponCustomerId(orderform.getCustomerId());
				couponLog.setOrderId(bean.getId());
				couponLog.setCouponId(bean.getCoupon());
				couponLog.setCreateTime(bean.getCreateTime());
				couponLog.setDeductionMoney(bean.getCouponPrice().doubleValue());
				couponLog.setMoney(bean.getPrice().doubleValue());
				couponLog.setType(2);
				orderDao.insert(couponLog);
				orderDao.updateState(orderform.getCustomerId(), bean.getCoupon());
			}
			shoppingCarDaoImpl.updateAllFlag(list);
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("订单创建成功！");
			Map<String, Object> returnData = new HashMap<String, Object>();
			returnData.put("orderId", bean.getId());
			returnData.put("orderState", bean.getState());
			returnDataUtil.setReturnObject(returnData);
		} else {
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage("订单创建失败！");
		}
		log.info("<OrderServiceImpl--add--end>");
		return returnDataUtil;
	}

	@Override
	public ReturnDataUtil del(String orderIdList) {
		log.info("<OrderServiceImpl--del--start>");
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (orderIdList != null && orderDao.del(orderIdList)) {
			returnData.setStatus(1);
			returnData.setMessage("删除成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("删除失败");
		}
		log.info("<OrderServiceImpl--del--end>");
		return returnData;
	}

	@Override
	public ReturnDataUtil storeOrderFind(OrderForm orderform) {
		log.info("<OrderServiceImpl--storeOrderFind--start>");
		ReturnDataUtil storeOrderFind = orderDao.storeOrderFind(orderform);
		List<OrderBean> list = (List<OrderBean>) storeOrderFind.getReturnObject();
		Iterator<OrderBean> it = list.iterator();
		Date date = new Date();
		log.info("before" + storeOrderFind.getTotal());// 42->21 25->25 17->8 pageSize=30
		while (it.hasNext()) {
			OrderBean bo = it.next();
			if (bo.getState().equals(10002)) {// 未支付订单过滤掉过期提水券
				String[] split = bo.getProduct().split(",");
				for (String s : split) {
					ShoppingCarBean shoppingCarBean = shoppingCarDaoImpl.get(s);
					if (shoppingCarBean != null) {
						ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(shoppingCarBean.getItemId());
						if (shoppingGoodsBean != null) {
							if (shoppingGoodsBean.getVouchersId() > 0) {// 提水券过期
								CarryWaterVouchersBean bean = carryWaterVouchersDaoImpl
										.get(shoppingGoodsBean.getVouchersId());
								if (date.after(bean.getEndTime())) {
									it.remove();
									break;
								}
							}
						}
					}
				}
			}
		}

		storeOrderFind.setTotal((long) list.size());
		log.info("after" + storeOrderFind.getTotal());
		log.info("<OrderServiceImpl--storeOrderFind--end>");
		return storeOrderFind;
	}

	@Override
	public boolean update(OrderForm orderform) {
		log.info("<OrderServiceImpl--update--start>");
		OrderBean ob = new OrderBean();
		if (orderform != null) {
			if (StringUtils.isNotBlank(orderform.getProduct())) {
				ob.setProduct(orderform.getProduct());
			}
			if (StringUtils.isNotBlank(orderform.getLocation())) {
				ob.setLocation(orderform.getLocation());
			}
			if (orderform.getDistributionModel() != null) {
				ob.setDistributionModel(orderform.getDistributionModel());
			}
			if (orderform.getPayType() != null) {
				ob.setPayType(orderform.getPayType());
			}
			if (orderform.getOrderId() != null) {
				ob.setId(orderform.getOrderId());
			}

		}
		BigDecimal price = new BigDecimal(String.valueOf(orderDao.findSumPriceByProduct(orderform.getProduct())));
		ob.setPrice(price);
		if (orderform.getCoupon() != null) {
			ob.setCoupon(orderform.getCoupon());
			BigDecimal deductionMoney = new BigDecimal(
					String.valueOf(orderDao.findDeductionMoneyByCoupon(orderform.getCoupon())));
			ob.setNowprice(price.subtract(deductionMoney));
			ob.setCouponPrice(deductionMoney);
		}
		boolean bean = orderDao.update(ob);
		log.info("<OrderServiceImpl--update--end>");
		return bean;

	}

	@Override
	public int findOrderIdByProduct(OrderForm orderform) {
		log.info("<OrderServiceImpl--findOrderIdByProduct--start>");
		int id = orderDao.findOrderIdByProduct(orderform.getProduct());
		log.info("<OrderServiceImpl--findOrderIdByProduct--end>");
		return id;
	}

	@Override
	public Map<String, Object> findSomeMessByOrderId(OrderForm orderForm) {
		log.info("<OrderServiceImpl--findSomeMessByOrderId--start>");
		Map<String, Object> map = orderDao.findSomeMessByOrderId(orderForm.getOrderId());
		log.info("<OrderServiceImpl--findSomeMessByOrderId--end>");
		return map;
	}

	@Override
	public List<String> addressFind(OrderForm orderform) {
		log.info("<OrderServiceImpl--findSomeMessByOrderId--start>");
		List<String> addressFind = orderDao.addressFind(orderform);
		log.info("<OrderServiceImpl--findSomeMessByOrderId--end>");
		return addressFind;
	}

	@Override
	public List<OrderDto> findOrderById(Long customerId, Integer type) {
		log.info("<OrderServiceImpl--findOrderById--start>");
		List<OrderDto> findOrderById = orderDao.findOrderById(customerId, type);
		log.info("<OrderServiceImpl--findOrderById--end>");
		return findOrderById;
	}

	@Override
	public ReturnDataUtil customerCoupon(OrderForm orderform) {
		log.info("<OrderServiceImpl--customerCoupon--start>");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		double price = orderDao.findSumPriceByProduct(orderform.getProduct());
		orderform.setPrice(price);
		ReturnDataUtil customerCoupon = orderDao.customerCoupon(orderform);
		List<CustomerCoupon> list = (List<CustomerCoupon>) customerCoupon.getReturnObject();
		if (list.size() > 0) {
			returnDataUtil.setReturnObject(list);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据");
		}
		log.info("<OrderServiceImpl--customerCoupon--end>");
		return returnDataUtil;
	}

	/**
	 * 拼团订单增加
	 */
	@Override
	public ReturnDataUtil addSpellGroup(OrderForm orderForm) {
		log.info("<OrderServiceImpl>-------<addSpellGroup>-------start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		OrderBean ob = new OrderBean();
		User user = CustomerUtil.getUser();
		Date date = new Date();
		Long customerId = null;
		if (StringUtils.isNotBlank(user.getOpenId())) {
			customerId = orderDao.findCustomerIdByOpenId(user.getOpenId());
			if (customerId == null) {
				returnDataUtil.setStatus(2);
				returnDataUtil.setMessage("非微信用户，订单创建失败！");
			}
		}
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(orderForm.getProduct());
		if (orderForm.getQuantity() > shoppingGoodsBean.getQuantity()) {
			returnDataUtil.setReturnObject(false);
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage(shoppingGoodsBean.getName() + "库存数量不足");
			return returnDataUtil;
		}
		if (shoppingGoodsBean.getVouchersId() > 0) {// 提水券过期
			CarryWaterVouchersBean bean = carryWaterVouchersDaoImpl.get(shoppingGoodsBean.getVouchersId());
			if (date.after(bean.getEndTime())) {
				returnDataUtil.setReturnObject(false);
				returnDataUtil.setStatus(2);
				returnDataUtil.setMessage(shoppingGoodsBean.getName() + "已下架");
				return returnDataUtil;
			}
		}
		if (StringUtils.isNotBlank(shoppingGoodsBean.getVouchersIds())) {// 套餐提水券过期
			List<String> vouchers = Arrays.asList(StringUtils.split(shoppingGoodsBean.getVouchersIds(), ","));
			for (String v : vouchers) {
				CarryWaterVouchersBean bean = carryWaterVouchersDaoImpl.get(Long.valueOf(v));
				if (date.after(bean.getEndTime())) {
					returnDataUtil.setReturnObject(false);
					returnDataUtil.setStatus(2);
					returnDataUtil.setMessage(shoppingGoodsBean.getName() + "已下架");
					return returnDataUtil;
				}
			}
		}
		if (shoppingGoodsBean.getIsHelpOneself() == 0) {// 收货地址为空
			List<AddressBean> addressList = addressService.select(user.getId());
			if (addressList == null || addressList.size() < 1) {
				returnDataUtil.setReturnObject(false);
				returnDataUtil.setStatus(2);
				returnDataUtil.setMessage("请先填写收货地址");
				return returnDataUtil;
			}
		}
		boolean changeInventory = shoppingGoodsDaoImpl.changeInventory(orderForm.getQuantity(),
				shoppingGoodsBean.getId());
		if (!changeInventory) {
			returnDataUtil.setReturnObject(false);
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage(shoppingGoodsBean.getName() + "库存数量不足!");
			return returnDataUtil;
		}

		if (orderForm != null) {
			if (StringUtils.isNotBlank(orderForm.getProduct())) {
				ob.setProduct(orderForm.getProduct());
			}
			if (StringUtils.isNotBlank(orderForm.getLocation())) {
				ob.setLocation(orderForm.getLocation());
			}
			if (orderForm.getDistributionModel() != null) {
				ob.setDistributionModel(orderForm.getDistributionModel());
			}
			if (orderForm.getPayType() != null) {
				ob.setPayType(orderForm.getPayType());
			}
			if (orderForm.getCustomerGroupId() != null) {
				// 检测参与者参加的团购是否已成功/已失败
				TblCustomerSpellGroupBean tcs = tblCustomerSpellGroupDaoImpl.get(orderForm.getCustomerGroupId());
				if (("拼团成功").equals(tcs.getState())) {
					returnDataUtil.setStatus(2);
					returnDataUtil.setMessage("拼团已被抢先一步！请参与其他的拼团");
					return returnDataUtil;
				} else if (("拼团失败").equals(tcs.getState())) {
					returnDataUtil.setStatus(2);
					returnDataUtil.setMessage("拼团已超时！请参与其他的拼团");
					return returnDataUtil;
				} else {
					ob.setCustomerGroupId(orderForm.getCustomerGroupId());
				}
			} else {
				TblCustomerSpellGroupBean tcGroupBean = new TblCustomerSpellGroupBean();
				tcGroupBean.setStartCustomerId(customerId);
				tcGroupBean.setGoodsId(Long.valueOf(orderForm.getProduct()));
				tcGroupBean.setSpellGroupId(orderForm.getSpellGroupId());
				tcGroupBean.setStartState(3);// 未开始
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				ShoppingGoodsSpellGroupBean sgs = shoppingGoodsSpellGroupDaoImpl.get(orderForm.getSpellGroupId());
				cal.add(Calendar.HOUR, sgs.getTimeLimit());
				tcGroupBean.setEndTime(cal.getTime());// 24小时制 拼团结束时间
				// TblCustomerSpellGroupBean
				// tcs=tblCustomerSpellGroupDaoImpl.insert(tcGroupBean);
				// getState()
				Integer tcsId = tblCustomerSpellGroupDaoImpl.insertSQL(tcGroupBean);
				ob.setCustomerGroupId(tcsId.longValue());// 用户团与订单关联
			}
			if (orderForm.getAddressId() != null) {
				ob.setAddressId(orderForm.getAddressId());
			}
			if (orderForm.getSpellGroupId() != null) {
				// 得到图购设置信息
				ShoppingGoodsSpellGroupBean sgs = shoppingGoodsSpellGroupDaoImpl.get(orderForm.getSpellGroupId());
				// 用户团购类型 0代表不限制 1新用户 2老用户 3 必须有一个新用户'
				Integer userType = sgs.getUserType();
				// 查询当前下单用户是否是新用户
				Integer checkNewCustomer = tblCustomerSpellGroupDaoImpl.checkNewCustomer(null);
				if (userType == 1) { // 新用户团
					if (checkNewCustomer > 0) {
						returnDataUtil.setStatus(2);
						returnDataUtil.setMessage("亲，本次拼团只针对注册过的新用户，你可以参加我们其他拼团活动，谢谢!");
						return returnDataUtil;
					}
				} else if (userType == 2) { // 老用户团
					if (checkNewCustomer == 0) {
						returnDataUtil.setStatus(2);
						returnDataUtil.setMessage("亲，本次拼团只针对注册过的老用户，你可以参加我们其他拼团活动，谢谢!");
						return returnDataUtil;
					}
				} else if (userType == 3) {
					boolean flag = false;
					TblCustomerSpellGroupBean bean = tblCustomerSpellGroupDaoImpl.get(ob.getCustomerGroupId());
					ArrayList<Long> list = new ArrayList<Long>();
					list.add(bean.getStartCustomerId());
					if (bean.getParticipationCustomerId() != null) {
						if (bean.getParticipationCustomerId().contains(",")) {
							String[] split = bean.getParticipationCustomerId().split(",");
							for (String s : split) {
								list.add(Long.parseLong(s));
							}
						} else {
							list.add(Long.parseLong(bean.getParticipationCustomerId()));
						}
						for (Long id : list) {
							Integer check = tblCustomerSpellGroupDaoImpl.checkNewCustomer(id);
							if (check == 0) {
								flag = true;
							}
						}
						if (!flag) {
							returnDataUtil.setStatus(2);
							returnDataUtil.setMessage("亲，本次拼团必须有一名新用户才能完成拼团，你可以参加我们其他拼团活动，谢谢!");
							return returnDataUtil;
						}
					}

				}
			}
		}
		ob.setCompanyId(shoppingGoodsBean.getCompanyId());
		ob.setOpenid(user.getOpenId());
		ob.setType(3);
		ob.setState(PayStateEnum.NOT_PAY.getState());
		// 计算本次购买金额 价格乘以数量
		BigDecimal b = new BigDecimal(orderForm.getPrice() * orderForm.getQuantity());
		ob.setPrice(b);
		ob.setNowprice(b);
		String payCode = IDUtil.getPayCode();
		ob.setPayCode(payCode);
		ob.setCustomerId(customerId);
		ob.setCreateTime(new Date());
		ob.setNum(orderForm.getQuantity());
		OrderBean bean = orderDao.insert(ob);
		if (bean != null) {
			// 增加订单详情
			OrderDetile obs = new OrderDetile();
			obs.setCustomerId(customerId);
			obs.setCreateTime(bean.getCreateTime());
			obs.setOrderId(bean.getId());
			obs.setItemId(Integer.parseInt(orderForm.getProduct()));
			obs.setItenName(orderForm.getItemName());
			obs.setPrice(bean.getPrice().doubleValue());
			obs.setNum(orderForm.getQuantity());
			boolean insert = orderDao.insert(obs);
			if (insert) {
				/*
				 * if((PayStateEnum.PAY_SUCCESS.getState()).equals(bean.getState())){ //更新用户余额
				 * memberDaoImpl.updateUserBalance(customerId, money); }
				 */
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("订单创建成功！");
				Map<String, Object> returnData = new HashMap<String, Object>();
				returnData.put("orderId", bean.getId());
				returnData.put("orderState", bean.getState());
				returnDataUtil.setReturnObject(returnData);
			} else {
				returnDataUtil.setStatus(2);
				returnDataUtil.setMessage("订单创建失败！");
			}
		} else {
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage("订单创建失败！");
		}
		log.info("<OrderServiceImpl>-------<addSpellGroup>-------end");
		return returnDataUtil;
	}

	/**
	 * 根据openID 查询用户id
	 */
	@Override
	public Long findCustomerIdByOpenId(String openId) {
		log.info("<OrderServiceImpl>-------<findCustomerIdByOpenId>-------start");
		Long customerId = orderDao.findCustomerIdByOpenId(openId);
		log.info("<OrderServiceImpl>-------<findCustomerIdByOpenId>-------end");
		return customerId;
	}

	@Override
	public List<PayRecordItemDto> getPayRecordItemList(String payCode) {
		log.info("<OrderServiceImpl>-------<getPayRecordItemList>-------start");
		List<PayRecordItemDto> payRecordItemList = orderDao.getPayRecordItemList(payCode);
		log.info("<OrderServiceImpl>-------<getPayRecordItemList>-------end");
		return payRecordItemList;
	}

	@Override
	public boolean updateOrderStateById(Long orderId, PayStateEnum state) {
		log.info("<OrderServiceImpl>-------<getPayRecordItemList>-------start");
		boolean result = orderDao.updateOrderStateById(orderId, state);
		log.info("<OrderServiceImpl>-------<getPayRecordItemList>-------end");
		return result;
	}

	@Override
	public ReturnDataUtil mySpellgroupOrder(OrderForm orderForm) {
		log.info("<OrderServiceImpl>-------<mySpellgroupOrder>-------start");
		ReturnDataUtil returnDataUtil = orderDao.mySpellgroupOrder(orderForm);
		log.info("<OrderServiceImpl>-------<mySpellgroupOrder>-------end");
		return returnDataUtil;
	}

	@Override
	public ReturnDataUtil pickUpAddress(Long spellgroupId) {
		log.info("<OrderServiceImpl>-------<pickUpAddress>-------start");
		ReturnDataUtil returnDataUtil = orderDao.pickUpAddress(spellgroupId);
		log.info("<OrderServiceImpl>-------<pickUpAddress>-------end");
		return returnDataUtil;
	}

	@Override
	public ShoppingDto orderParticulars(Long orderId) {
		log.info("<OrderServiceImpl>-------<orderParticulars>-------start");
		ShoppingDto dto = orderDao.orderParticulars(orderId);
		log.info("<OrderServiceImpl>-------<orderParticulars>-------end");
		return dto;
	}

	@Override
	public boolean isPaymentAllowed(Long orderId) {
		log.info("<OrderServiceImpl>-------<orderParticulars>-------start");
		boolean flag = orderDao.isPaymentAllowed(orderId);
		log.info("<OrderServiceImpl>-------<orderParticulars>-------end");
		return flag;
	}

	@Override
	public Integer getCompanyIdByPayCode(String payCode) {
		log.info("<OrderServiceImpl>----<getCompanyIdByPayCode>----start>");
		Integer companyId = orderDao.getCompanyIdByPayCode(payCode);
		log.info("<OrderServiceImpl>----<getCompanyIdByPayCode>----end>");
		return companyId;
	}

	@Override
	public int paySuccessStroeOrder(String outTradeNo, String transactionId, Integer type) {
		log.info("<OrderServiceImpl>-------<paySuccessStroeOrder>-------start");
		int result = orderDao.paySuccessStroeOrder(outTradeNo, transactionId, type);
		log.info("<OrderServiceImpl>-------<paySuccessStroeOrder>-------end");
		return result;
	}
}
