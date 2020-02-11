package com.server.module.customer.product.customerBargain;



import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.module.customer.CustomerUtil;
import com.server.module.customer.order.OrderBean;
import com.server.module.customer.order.OrderCreator;
import com.server.module.customer.order.OrderDao;
import com.server.module.customer.order.OrderDetile;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsDao;
import com.server.module.customer.product.bargainDetail.BargainDetailBean;
import com.server.module.customer.product.bargainDetail.BargainDetailDao;
import com.server.module.customer.product.bargainDetail.BargainDto;
import com.server.module.customer.product.bargainDetail.BargainGoodsDto;
import com.server.module.customer.product.goodsBargain.GoodsBargainBean;
import com.server.module.customer.product.goodsBargain.GoodsBargainDao;
import com.server.module.customer.userInfo.userWxInfo.TblCustomerWxBean;
import com.server.module.customer.userInfo.userWxInfo.TblCustomerWxDao;
import com.server.module.sys.model.User;
import com.server.redis.RedisClient;
import com.server.util.DateUtil;
import com.server.util.stateEnum.BargainStateEnum;


@Service
public class CustomerBargainServiceImpl implements CustomerBargainService{

	private final static Logger log = LogManager.getLogger(CustomerBargainServiceImpl.class);
	@Autowired
	private CustomerBargainDao customerBargainDao;
	@Autowired
	private GoodsBargainDao GoodsBargainDao;
	@Autowired
	private BargainDetailDao bargainDetailDao;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private TblCustomerWxDao tblCustomerWxDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ShoppingGoodsDao ShoppingGoodsDaoImpl;

	@Override
	public boolean isBargaining(Long id) {
		log.info("CustomerBargainServiceImpl--isEnd--start");
		boolean end = customerBargainDao.isBargaining(id);
		log.info("CustomerBargainServiceImpl--isEnd--end");
		return end;
	}
	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public BigDecimal cutPirce(Long customerBargainId,User user,BargainDto barginDto,Integer doneTimes) {
		log.info("CustomerBargainServiceImpl--cutPirce--start");
		BigDecimal cutPrice = barginDto.getOneBargainPrice();
		BigDecimal currPrice = barginDto.getCurrPrice();
		int compareTo = currPrice.subtract(cutPrice).compareTo(barginDto.getLowestPrice());
		if(compareTo<0){
			cutPrice = currPrice.subtract(barginDto.getLowestPrice());
		}
		BargainDetailBean bargainDetail = new BargainDetailBean();
		bargainDetail.setCustomerBargainId(customerBargainId);
		bargainDetail.setCustomerId(user.getId());
		bargainDetail.setCutPrice(cutPrice);
		Long id = bargainDetailDao.insertBargainDetail(bargainDetail);
		if(id>0){
			boolean cutResult = customerBargainDao.cutPrice(bargainDetail.getCustomerBargainId(),bargainDetail.getCutPrice());
			//说明已砍至最低价
			if(compareTo <= 0 && cutResult){
				//更新砍价状态 0失败 1成功 2砍价中
				customerBargainDao.updateBargainState(customerBargainId, BargainStateEnum.BARGAIN_SUCCESS.getState());
				//创建订单
				CustomerBargainBean cusBargain = customerBargainDao.getCustomerBargain(customerBargainId);
				BargainGoodsDto bargainGoods = bargainDetailDao.getBargainGoods(cusBargain.getGoodsBargainId());
				TblCustomerWxBean customer = tblCustomerWxDao.get(cusBargain.getCustomerId());
				OrderBean bargainOrder = OrderCreator.createBargainNewOrder(cusBargain, bargainGoods, customer.getOpenId());
				OrderBean insert = orderDao.insert(bargainOrder);
				OrderDetile ob = OrderCreator.createBargainOrderDetile(cusBargain, bargainGoods, insert.getId());
				orderDao.insert(ob);
			}
			//增加砍价次数
			int second = DateUtil.getTodayRemainSecond();
			doneTimes++;
			redisClient.set("bargain"+user.getId(),doneTimes.toString(),second);
		}
		log.info("CustomerBargainServiceImpl--cutPirce--end");
		return cutPrice;
	}
	@Override
	public CustomerBargainBean getCustomerBargain(Long id) {
		log.info("CustomerBargainServiceImpl--getCustomerBargain--start");
		CustomerBargainBean customerBargain = customerBargainDao.getCustomerBargain(id);
		log.info("CustomerBargainServiceImpl--getCustomerBargain--end");
		return customerBargain;
	}
	@Override
	public CustomerBargainDto getBargainsInfo(Long customerBargainId) {
		log.info("CustomerBargainServiceImpl--getBargainsInfo--start");
		CustomerBargainDto bargainsInfo = customerBargainDao.getBargainsInfo(customerBargainId);
		if(bargainsInfo!=null){
			Long itemSalesNum = orderDao.getItemSalesNum(bargainsInfo.getItemId());
			bargainsInfo.setBuyNum(itemSalesNum);
		}
		log.info("CustomerBargainServiceImpl--getBargainsInfo--end");
		return bargainsInfo;
	}
	@Override
	public BargainOrderDto getBargainOrderInfo(Long orderId) {
		log.info("CustomerBargainServiceImpl--getBargainOrderInfo--start");
		BargainOrderDto bargainOrderInfo = customerBargainDao.getBargainOrderInfo(orderId);
		log.info("CustomerBargainServiceImpl--getBargainOrderInfo--end");
		return bargainOrderInfo;
	}
	
	@Override
	public Map<String,Object> insert(CustomerBargainBean customerBargainBean) {
		log.info("<CustomerBargainServiceImpl>----<insert>----start");
		Map<String,Object> map=new HashMap<String, Object>();
		Long customerId = CustomerUtil.getCustomerId();
		//查询砍价活动信息
		GoodsBargainBean goodsBargainBean = GoodsBargainDao.get(customerBargainBean.getGoodsBargainId());
		//查询砍价活动商品信息
		ShoppingGoodsBean shoppingGoodsBean = ShoppingGoodsDaoImpl.get(goodsBargainBean.getGoodsId());
		//得到砍价时限
		Long hourLimit = goodsBargainBean.getHourLimit();
		//得到砍价金额
		BigDecimal oneBargainPrice = goodsBargainBean.getOneBargainPrice();
		//计算出砍后价格
		BigDecimal salesPrice = shoppingGoodsBean.getSalesPrice();
		BigDecimal currPrice = salesPrice.subtract(oneBargainPrice);
		customerBargainBean.setCustomerId(customerId);
		customerBargainBean.setCurrPrice(currPrice);
		//计算结束时间
		Calendar cal = Calendar.getInstance();   
        cal.setTime(new Date());   
        cal.add(Calendar.HOUR, hourLimit.intValue());// 24小时制   
        Date endTime = cal.getTime();   
		customerBargainBean.setEndTime(endTime);
		//增加砍价表
		Long insert = customerBargainDao.insert(customerBargainBean);
		if(insert>0) {
			BargainDetailBean bargainDetail=new BargainDetailBean();
			bargainDetail.setCustomerBargainId(insert);
			bargainDetail.setCustomerId(customerId);
			bargainDetail.setCutPrice(oneBargainPrice);
			//增加砍价明细表
			Long id = bargainDetailDao.insertBargainDetail(bargainDetail);
			if(id>0) {
				log.info("<CustomerBargainServiceImpl>----<insert>----end");
				map.put("privce", oneBargainPrice);
				map.put("customerBargainId", insert);
				return map;
			}
		}
		return null;
		
	}
	
}
