package com.server.module.system.statisticsManage.payRecordPerDay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;

@Service("payRecordPerDayService")
public class PayRecordPerDayServiceImpl implements PayRecordPerDayService {

	@Autowired
	private PayRecordPerDayDao payRecordPerDayDao;
	@Autowired
	private CompanyDao companyDao;
	@Override
	public ReturnDataUtil findPayRecordPerDay(PayRecordPerDayForm payDayForm){
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<PayRecordPerDayDto> perDayList = payRecordPerDayDao.findPayRecordPerDay(payDayForm);
		CompanyBean company = companyDao.findCompanyById(payDayForm.getCompanyId());
		//Integer normalNum = payRecordPerDayDao.findNormalMachines(payDayForm);

		perDayList.stream().forEach(perDayDto -> {
			perDayDto.setCompanyName(company.getName());
			perDayDto.setNormalNum(payRecordPerDayDao.findNormalMachines(payDayForm,perDayDto.getCreateTime()));
			if(perDayDto.getNormalNum()==0) {
				perDayDto.setAverageAll(0.0);
			}else {
				perDayDto.setAverageAll(perDayDto.getFinishedItemNum().divide(new BigDecimal(perDayDto.getNormalNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		});
		long total = 0;
		total = payRecordPerDayDao.findPayRecordPerDayNum(payDayForm);
		returnData.setStatus(1);
		returnData.setMessage("查询成功");
		returnData.setCurrentPage(payDayForm.getCurrentPage());
		returnData.setReturnObject(perDayList);
		returnData.setTotal(total);
		int totalPage = (int)(total%payDayForm.getPageSize()==0?total/payDayForm.getPageSize():total/payDayForm.getPageSize()+1);
		returnData.setTotalPage(totalPage);
		return returnData;
	}

	public List<PayRecordPerDayDto> exportPayRecordPerDay(PayRecordPerDayForm payDayForm){
		List<PayRecordPerDayDto> perDayList = payRecordPerDayDao.findPayRecordPerDay(payDayForm);
		CompanyBean company = companyDao.findCompanyById(payDayForm.getCompanyId());
		//Integer normalNum = payRecordPerDayDao.findNormalMachines(payDayForm);
		perDayList.stream().forEach(perDayDto -> {
			perDayDto.setCompanyName(company.getName());
			perDayDto.setNormalNum(payRecordPerDayDao.findNormalMachines(payDayForm,perDayDto.getCreateTime()));
			if(perDayDto.getNormalNum()==0) {
				perDayDto.setAverageAll(0.0);
			}else {
				perDayDto.setAverageAll(perDayDto.getFinishedItemNum().divide(new BigDecimal(perDayDto.getNormalNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			}

		});
		return perDayList;
	}

	@Override
	public Integer findNormalMachines(PayRecordPerDayForm payDayForm,Date reportDate) {
		return payRecordPerDayDao.findNormalMachines(payDayForm,reportDate);
	}
	
	@Override
	public List<PayRecordPerDayDto> userBuyStation(PayRecordPerDayForm payDayForm){
		List<PayRecordPerDayDto> list=payRecordPerDayDao.userBuyStation(payDayForm);
		for(PayRecordPerDayDto p:list) {
			p.setNormalNum(payRecordPerDayDao.findNormalMachines(payDayForm,p.getCreateTime()));
		}
		return list;
	}
	
	@Override
	public ReturnDataUtil userBuyStationNum(PayRecordPerDayForm payDayForm){
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<PayRecordPerDayDto> list=payRecordPerDayDao.userBuyStation(payDayForm);
		for(PayRecordPerDayDto p:list) {
			p.setNormalNum(payRecordPerDayDao.findNormalMachines(payDayForm,p.getCreateTime()));
		}
		long total = 0;
		total = payRecordPerDayDao.userBuyStationNum(payDayForm);
		returnData.setStatus(1);
		returnData.setMessage("查询成功");
		returnData.setCurrentPage(payDayForm.getCurrentPage());
		returnData.setReturnObject(list);
		returnData.setTotal(total);
		int totalPage = (int)(total%payDayForm.getPageSize()==0?total/payDayForm.getPageSize():total/payDayForm.getPageSize()+1);
		returnData.setTotalPage(totalPage);
		return returnData;
	}
}
