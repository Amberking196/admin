package com.server.module.customer.refundApplication;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundApplicationServiceImpl implements RefundApplicationService{

	private final static Logger log = LogManager.getLogger(RefundApplicationDaoImpl.class);
	
	@Autowired
	private RefundApplicationDao refundApplicationDao;

	@Override
	public RefundApplicationDto judgeOrderRefund(String payCode, BigDecimal refundPrice,Integer orderType) {
		log.info("<RefundApplicationServiceImpl--judgeOrderRefund--start>");
		RefundApplicationDto judgeOrderRefund = refundApplicationDao.judgeOrderRefund(payCode, refundPrice,orderType);
		log.info("<RefundApplicationServiceImpl--judgeOrderRefund--end>");
		return judgeOrderRefund;
	}

	@Override
	public Long insertRefundApplication(RefundApplicationBean refundApp) {
		log.info("<RefundApplicationServiceImpl--insertRefundApplication--start>");
		Long id = refundApplicationDao.insertRefundApplication(refundApp);
		log.info("<RefundApplicationServiceImpl--insertRefundApplication--end>");
		return id;
	}

	@Override
	public List<RefundApplicationBean> getRefundApplication(String payCode) {
		log.info("<RefundApplicationServiceImpl--getRefundApplication--start>");
		List<RefundApplicationBean> refundApplication = refundApplicationDao.getRefundApplication(payCode);
		log.info("<RefundApplicationServiceImpl--getRefundApplication--end>");
		return refundApplication;
	}
	
	
}
