package com.server.module.system.statisticsManage.payRecordPerWeek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; import java.util.List;

import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.statisticsManage.payRecordPerHour.PayRecordPerHourBean;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-07-14 14:38:10
 */ 
@Service
public class  PayRecordPerWeekServiceImpl  implements PayRecordPerWeekService{

	public static Logger log = LogManager.getLogger(PayRecordPerWeekServiceImpl.class);
	@Autowired
	private PayRecordPerWeekDao payRecordPerWeekDaoImpl;
	@Autowired
	private CompanyDao companyDao;
	
	public ReturnDataUtil listPage(PayRecordPerWeekForm payRecordPerWeekForm){
		log.info("<PayRecordPerWeekServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = payRecordPerWeekDaoImpl.listPage(payRecordPerWeekForm);
		List<PayRecordPerWeekBean> payRecordPerWeekBeanList=(List<PayRecordPerWeekBean>) listPage.getReturnObject();
		CompanyBean company = companyDao.findCompanyById(payRecordPerWeekForm.getCompanyId());
		payRecordPerWeekBeanList.stream().forEach(perWeekDto -> {
			perWeekDto.setCompanyName(company.getName());
		});
		long total = 0;
		total = payRecordPerWeekDaoImpl.findPayRecordPerWeekNum(payRecordPerWeekForm);
		listPage.setTotal(total);
		int totalPage = (int)(total%payRecordPerWeekForm.getPageSize()==0?total/payRecordPerWeekForm.getPageSize():total/payRecordPerWeekForm.getPageSize()+1);
		listPage.setTotalPage(totalPage);
		log.info("<PayRecordPerWeekServiceImpl>------<listPage>-----end");
		return listPage;
	}

	public PayRecordPerWeekBean add(PayRecordPerWeekBean entity) {
		return payRecordPerWeekDaoImpl.insert(entity);
	}

	public boolean update(PayRecordPerWeekBean entity) {
		return payRecordPerWeekDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return payRecordPerWeekDaoImpl.delete(id);
	}

	public PayRecordPerWeekBean get(Object id) {
		return payRecordPerWeekDaoImpl.get(id);
	}
}

