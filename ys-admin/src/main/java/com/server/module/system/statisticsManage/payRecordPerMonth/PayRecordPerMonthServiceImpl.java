package com.server.module.system.statisticsManage.payRecordPerMonth;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.common.persistence.Page;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.statisticsManage.payRecordPerWeek.PayRecordPerWeekBean;
import com.server.module.system.statisticsManage.payRecordPerWeek.PayRecordPerWeekServiceImpl;
import com.server.util.ReturnDataUtil;
@Service
public class PayRecordPerMonthServiceImpl implements PayRecordPerMonthService{

	public static Logger log = LogManager.getLogger(PayRecordPerWeekServiceImpl.class);
	@Autowired
	PayRecordPerMonthDao payRecordPerMonthDao;
	@Autowired
	private CompanyDao companyDao;
	@Override
	public ReturnDataUtil findPayRecordPerMonth(PayRecordPerMonthForm payMonthForm) {
		log.info("<PayRecordPerMonthServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = payRecordPerMonthDao.listPage(payMonthForm);
		List<PayRecordPerMonthBean> payRecordPerMonthBeanList=(List<PayRecordPerMonthBean>) listPage.getReturnObject();
		CompanyBean company = companyDao.findCompanyById(payMonthForm.getCompanyId());
		payRecordPerMonthBeanList.stream().forEach(perMonthDto -> {
			perMonthDto.setCompanyName(company.getName());
		});

		long total = 0;
		total = payRecordPerMonthDao.findPayRecordPerMonthNum2(payMonthForm);
		listPage.setTotal(total);
		int totalPage = (int)(total%payMonthForm.getPageSize()==0?total/payMonthForm.getPageSize():total/payMonthForm.getPageSize()+1);
		listPage.setTotalPage(totalPage);
		log.info("<PayRecordPerMonthServiceImpl>------<listPage>-----end");
		return listPage;
	}

}
