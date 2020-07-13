package com.server.module.system.refund;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.commonBean.TotalResultBean;
import com.server.module.system.refund.application.RefundApplicationDto;
import com.server.module.system.refund.application.RefundApplicationForm;
import com.server.module.system.refund.principal.RefundPrincipalBean;
import com.server.module.system.refund.principal.RefundPrincipalForm;

@Service
public class RefundServiceImpl implements RefundService{
	
	private final static Logger log = LogManager.getLogger(RefundServiceImpl.class);

	@Autowired
	private RefundDao refundDao;

	@Override
	public boolean insertRefundPrincipal(RefundPrincipalBean principal) {
		log.info("<RefundServiceImpl--insertRefundPrincipal--start>");
		boolean result = refundDao.insertRefundPrincipal(principal);
		log.info("<RefundServiceImpl--insertRefundPrincipal--end>");
		return result;
	}

	@Override
	public boolean updateRefundPrincipal(RefundPrincipalBean principal) {
		log.info("<RefundServiceImpl--updateRefundPrincipal--start>");
		boolean result = refundDao.updateRefundPrincipal(principal);
		log.info("<RefundServiceImpl--updateRefundPrincipal--end>");
		return result;
	}

	@Override
	public List<RefundRecordBean> findRefundInfo(RefundRecordForm form) {
		log.info("<RefundServiceImpl--findRefundInfo--start>");
		List<RefundRecordBean> refundList = refundDao.findRefundInfo(form);
		log.info("<RefundServiceImpl--findRefundInfo--end>");
		return refundList;
	}

	@Override
	public Long findRefundInfoNum(RefundRecordForm form) {
		log.info("<RefundServiceImpl--findRefundInfoNum--start>");
		Long total = refundDao.findRefundInfoNum(form);
		log.info("<RefundServiceImpl--findRefundInfoNum--end>");
		return total;
	}

	@Override
	public Long getRefundPrincipalNum(RefundPrincipalForm form) {
		log.info("<RefundServiceImpl--getRefundPrincipalNum--start>");
		Long total = refundDao.getRefundPrincipalNum(form);
		log.info("<RefundServiceImpl--getRefundPrincipalNum--end>");
		return total;
	}

	@Override
	public List<RefundPrincipalBean> getRefundPrincipalInfo(RefundPrincipalForm form) {
		log.info("<RefundServiceImpl--getRefundPrincipalInfo--start>");
		List<RefundPrincipalBean> refundList = refundDao.getRefundPrincipalInfo(form);
		log.info("<RefundServiceImpl--getRefundPrincipalInfo--end>");
		return refundList;
	}

	@Override
	public RefundOrderInfo findOrder(String payCode, Integer orderType) {
		log.info("<RefundServiceImpl--findOrder--start>");
		RefundOrderInfo findOrder = refundDao.findOrder(payCode,orderType);
		log.info("<RefundServiceImpl--findOrder--end>");
		return findOrder;
	}

	@Override
	public String getPrincipalInfoById(Long loginInfoId) {
		log.info("<RefundServiceImpl--getPrincipalInfoById--start>");
		String phone = refundDao.getPrincipalInfoById(loginInfoId);
		log.info("<RefundServiceImpl--getPrincipalInfoById--end>");
		return phone;
	}

	@Override
	public TotalResultBean<List<RefundApplicationDto>> findRefundApplication(RefundApplicationForm form) {
		log.info("<RefundServiceImpl--findRefundApplication--start>");
		TotalResultBean<List<RefundApplicationDto>> findRefundApplication = refundDao.findRefundApplication(form);
		log.info("<RefundServiceImpl--findRefundApplication--end>");
		return findRefundApplication;
	}

	@Override
	public boolean updateRefundApplication(String payCode, Integer state, String backReason,Long updateUser) {
		log.info("<RefundServiceImpl--updateRefundApplication--start>");
		boolean result = refundDao.updateRefundApplication(payCode,state,backReason,updateUser);
		log.info("<RefundServiceImpl--updateRefundApplication--end>");
		return result;
	}
	
	@Override
	public boolean updatePayRecordItem(RefundDto refundDto) {
		log.info("<RefundServiceImpl--updatePayRecordItem--start>");
		boolean result = refundDao.updatePayRecordItem(refundDto);
		log.info("<RefundServiceImpl--updatePayRecordItem--end>");
		return result;
	} 

	
}
