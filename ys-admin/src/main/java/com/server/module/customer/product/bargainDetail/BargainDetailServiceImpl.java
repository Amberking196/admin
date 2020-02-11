package com.server.module.customer.product.bargainDetail;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.customer.order.OrderBean;
import com.server.module.customer.order.OrderCreator;
import com.server.module.customer.order.OrderDao;
import com.server.module.customer.product.customerBargain.CustomerBargainBean;
import com.server.module.customer.product.customerBargain.CustomerBargainDao;
import com.server.module.customer.userInfo.userWxInfo.TblCustomerWxBean;
import com.server.module.customer.userInfo.userWxInfo.TblCustomerWxDao;


@Service
public class BargainDetailServiceImpl implements BargainDetailService{
	
	private final static Logger log = LogManager.getLogger(BargainDetailServiceImpl.class);

	@Autowired
	private BargainDetailDao bargainDetailDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private CustomerBargainDao customerBargainDao;
	@Autowired
	private TblCustomerWxDao tblCustomerWxDao;

	@Override
	public Long insertBargainDetail(BargainDetailBean bargainDetail) {
		log.info("BargainDetailServiceImpl--insertBargainDetail--start");
		Long id = bargainDetailDao.insertBargainDetail(bargainDetail);
		log.info("BargainDetailServiceImpl--insertBargainDetail--end");
		return id;
	}

	@Override
	public List<BargainDetailDto> findBargainDetailList(Long customerBargainId) {
		log.info("BargainDetailServiceImpl--findBargainDetailList--start");
		List<BargainDetailDto> bargainDetailList = bargainDetailDao.findBargainDetailList(customerBargainId);
		log.info("BargainDetailServiceImpl--findBargainDetailList--end");
		return bargainDetailList;
	}

	@Override
	public BargainDto getBarginSomeInfo(Long customerBargianId) {
		log.info("BargainDetailServiceImpl--getBarginSomeInfo--start");
		BargainDto barginSomeInfo = bargainDetailDao.getBarginSomeInfo(customerBargianId);
		log.info("BargainDetailServiceImpl--getBarginSomeInfo--end");
		return barginSomeInfo;
	}

	@Override
	public boolean isBargained(Long customerBargainId, Long customerId) {
		log.info("BargainDetailServiceImpl--isBargained--start");
		boolean bargained = bargainDetailDao.isBargained(customerBargainId,customerId);
		log.info("BargainDetailServiceImpl--isBargained--end");
		return bargained;
	}

	@Override
	public boolean createBargainOrder(Long customerBargainId) {
		CustomerBargainBean cusBargain = customerBargainDao.getCustomerBargain(customerBargainId);
		BargainGoodsDto bargainGoods = bargainDetailDao.getBargainGoods(cusBargain.getGoodsBargainId());
		TblCustomerWxBean customer = tblCustomerWxDao.get(cusBargain.getCustomerId());
		OrderBean bargainOrder = OrderCreator.createBargainNewOrder(cusBargain, bargainGoods, customer.getOpenId());
		OrderBean order = orderDao.insert(bargainOrder);
		if(order.getId()>0){
			return true;
		}
		return false;
	}
}
