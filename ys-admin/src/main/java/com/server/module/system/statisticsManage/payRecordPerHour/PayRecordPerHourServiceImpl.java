package com.server.module.system.statisticsManage.payRecordPerHour;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; import java.util.List;

import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-07-13 09:23:01
 */ 
@Service
public class  PayRecordPerHourServiceImpl  implements PayRecordPerHourService{

	public static Logger log = LogManager.getLogger(PayRecordPerHourServiceImpl.class);
	@Autowired
	private PayRecordPerHourDao payRecordPerHourDaoImpl;
	@Autowired
	private CompanyDao companyDao;
	
	public ReturnDataUtil listPage(PayRecordPerHourForm perHourForm){
		log.info("<PerHourServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = payRecordPerHourDaoImpl.listPage(perHourForm);
		List<PayRecordPerHourBean> payRecordPerHourBeanList=(List<PayRecordPerHourBean>) listPage.getReturnObject();
		CompanyBean company = companyDao.findCompanyById(perHourForm.getCompanyId());
		payRecordPerHourBeanList.stream().forEach(perHourDto -> {
			perHourDto.setCompanyName(company.getName());
		});
		long total = 0;
		total = payRecordPerHourDaoImpl.findPayRecordPerHourNum(perHourForm);
		listPage.setTotal(total);
		int totalPage = (int)(total%perHourForm.getPageSize()==0?total/perHourForm.getPageSize():total/perHourForm.getPageSize()+1);
		listPage.setTotalPage(totalPage);
		log.info("<PerHourServiceImpl>------<listPage>----end");
		return listPage;
	}

	public PayRecordPerHourBean add(PayRecordPerHourBean entity) {
		return payRecordPerHourDaoImpl.insert(entity);
	}

	public boolean update(PayRecordPerHourBean entity) {
		return payRecordPerHourDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return payRecordPerHourDaoImpl.delete(id);
	}

	public PayRecordPerHourBean get(Object id) {
		return payRecordPerHourDaoImpl.get(id);
	}
}

