package com.server.module.system.game.send;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.customer.coupon.CouponBean;
import com.server.module.customer.coupon.CouponCustomerBean;
import com.server.module.customer.coupon.CouponDao;
import com.server.module.customer.coupon.CouponService;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsService;
import com.server.module.customer.userInfo.stock.CustomerStockBean;
import com.server.module.customer.userInfo.stock.StoreOrderService;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerDao;
import com.server.module.system.game.GameDto;
import com.server.module.system.game.GamePrizeBean;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CouponEnum;

/**
 * 商品奖励发送
 * @author why
 * @date: 2019年3月7日 上午10:21:00
 */
@Component
public class SendGoodsProcessor implements SendPrizeProcessor {

	private static Logger log = LogManager.getLogger(SendGoodsProcessor.class);
	@Autowired
	private CouponService couponServiceImpl;
	@Autowired
	private ShoppingGoodsService shoppingGoodsServiceImpl;
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;
	@Autowired
	private CarryWaterVouchersCustomerDao carryWaterVouchersCustomerDaoImpl;
	@Autowired
	private StoreOrderService storeOrderService;
	@Autowired
	private UserUtils userUtils;

	@Override
	public ReturnDataUtil send(GamePrizeBean prize, GameDto gameDto) {
		log.info("<SendGoodsProcessor>-----<send>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		Long customerId = userUtils.getSmsUser().getId();
		if (prize.getRewardId() != null && prize.getRewardId() > 0) {
			// 获取商品信息
			ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(prize.getRewardId());
			if (shoppingGoodsBean.getIsHelpOneself() == 0) { // 需要填写地址
				returnDataUtil.setStatus(66);
				returnDataUtil.setMessage("恭喜您抽中了商品" + shoppingGoodsBean.getName() + "，请填写收货地址，谢谢！");
				returnDataUtil.setReturnObject(gameDto);
			} else {
				if (shoppingGoodsBean.getTypeId().equals(25l)) {// 优惠券
					CouponBean couponBean = couponDao.getCouponInfoByProduct(shoppingGoodsBean);
					CouponCustomerBean couponCustomerBean = couponDao.getCouponCustomerBean(customerId,
							couponBean.getId());
					Integer quantity = couponBean.getSendMax() * prize.getAmount();
					log.info("中奖优惠券数量===" + quantity);
					if (couponCustomerBean != null) {
						couponCustomerBean.setQuantity(couponCustomerBean.getQuantity() + quantity);
						couponCustomerBean.setSumQuantity(couponCustomerBean.getSumQuantity() + quantity);// 券的总数
						couponDao.updateCouponCustomerBean(couponCustomerBean);
						returnDataUtil.setMessage("恭喜您抽中了商品" + shoppingGoodsBean.getName() + ",请前往我的信息→我的优惠券 查看！");
						returnDataUtil.setReturnObject(gameDto);
					} else {
						CouponCustomerBean couCusBean = new CouponCustomerBean();
						couCusBean.setCouponId(couponBean.getId());
						couCusBean.setCustomerId(customerId);
						couCusBean.setStartTime(couponBean.getLogicStartTime());
						couCusBean.setEndTime(couponBean.getLogicEndTime());
						couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
						couCusBean.setQuantity(quantity.longValue());
						couCusBean.setSumQuantity(quantity.longValue());// 总券数
						couponServiceImpl.insertCouponCustomer(couCusBean);
						returnDataUtil.setMessage("恭喜您抽中了商品" + shoppingGoodsBean.getName() + ",请前往我的信息→我的优惠券 查看！");
						returnDataUtil.setReturnObject(gameDto);
					}
				} else if (shoppingGoodsBean.getTypeId().equals(26l) || shoppingGoodsBean.getTypeId().equals(27l)) {
					//得到提水券信息
					CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl.get(prize.getRewardId());
					//得到本次购买数量和下发张数  最终为下发张数
					Integer num=carryWaterVouchersBean.getSendMax()*prize.getAmount();
					log.info("最终下发张数======"+num);
					CarryWaterVouchersCustomerBean entity=new CarryWaterVouchersCustomerBean();
					entity.setCarryId(prize.getRewardId());
					entity.setCustomerId(customerId);
					entity.setStartTime(carryWaterVouchersBean.getLogicStartTime());
					entity.setEndTime(carryWaterVouchersBean.getLogicEndTime());
					entity.setQuantity(num.longValue());
					entity.setCreateUser(customerId);
					entity.setOrderId(null);
					carryWaterVouchersCustomerDaoImpl.insert(entity);
					returnDataUtil.setMessage("恭喜您抽中了商品" + shoppingGoodsBean.getName() + ",请前往我的信息→我的提水券 查看！");
					returnDataUtil.setReturnObject(gameDto);
				} else if (shoppingGoodsBean.getTypeId().equals(28l)) {
					// 商品为套餐类型 绑定多张提水券
					String[] vouchers = StringUtils.split(shoppingGoodsBean.getVouchersIds(), ",");
					for (String v : vouchers) {
						//得到提水券信息
						CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl.get(Long.valueOf(v));
						//得到本次购买数量和下发张数  最终为下发张数
						Integer num=carryWaterVouchersBean.getSendMax()*prize.getAmount();
						log.info("最终下发张数======"+num);
						CarryWaterVouchersCustomerBean entity=new CarryWaterVouchersCustomerBean();
						entity.setCarryId(Long.valueOf(v));
						entity.setCustomerId(customerId);
						entity.setStartTime(carryWaterVouchersBean.getLogicStartTime());
						entity.setEndTime(carryWaterVouchersBean.getLogicEndTime());
						entity.setQuantity(num.longValue());
						entity.setCreateUser(customerId);
						entity.setOrderId(null);
						carryWaterVouchersCustomerDaoImpl.insert(entity);
					}
					returnDataUtil.setMessage("恭喜您抽中了商品" + shoppingGoodsBean.getName() + ",请前往我的信息→我的提水券 查看！");
					returnDataUtil.setReturnObject(gameDto);
				} else { // 存水
					CustomerStockBean stock = storeOrderService.getStock(shoppingGoodsBean.getId().intValue(),
							customerId);
					if (stock == null) {
						stock = new CustomerStockBean();
						stock.setBasicItemId(shoppingGoodsBean.getBasicItemId());
						stock.setItemId(shoppingGoodsBean.getId().intValue());
						stock.setItemName(shoppingGoodsBean.getName());
						stock.setCustomerId(customerId);
						stock.setStock(prize.getAmount());
						stock.setPickNum(0);
						storeOrderService.insert(stock);
					} else {
						stock.setStock(stock.getStock() + prize.getAmount());
						storeOrderService.update(stock);
					}
					returnDataUtil.setMessage("恭喜您抽中了商品" + shoppingGoodsBean.getName() + ",请前往我的信息→我的存水 查看！");
					returnDataUtil.setReturnObject(gameDto);
				}
			}
		} else {
			returnDataUtil.setMessage("谢谢惠顾！");
			returnDataUtil.setReturnObject(gameDto);
		}
		log.info("<SendGoodsProcessor>-----<send>-----end");
		return returnDataUtil;
	}

}
