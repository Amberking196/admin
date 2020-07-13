package com.server.module.system.companyManage;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.server.module.system.companyManage.aliPayConfig.AliPayConfig;
import com.server.module.system.companyManage.aliPayConfig.AliPayDao;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfo;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfoDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
@Service
public class CompanyServiceImpl implements CompanyService {

	public static Logger log = LogManager.getLogger(CompanyServiceImpl.class); 	    
	@Autowired
	private CompanyDao companyManageDao;
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	@Autowired
	private AliPayDao alipayDao;
	
	@Override
	public List<CompanyBean> findAllSonCompany(Integer parentId) {
		log.info("<CompanyServiceImpl>--<findAllSonCompany>--start"); 
		if(parentId!=null) {
			List<CompanyBean> relist=companyManageDao.findAllSonCompany(parentId);
			log.info("<CompanyServiceImpl>--<findAllSonCompany>--end"); 
			return relist;
		}
		else {
			return null;
		}
	}

	@Override
	public List<Integer> findAllSonCompanyId(Integer parentId) {
		log.info("<CompanyServiceImpl>--<findAllSonCompanyId>--start"); 
		if (parentId !=null) {
			List<Integer> relist= companyManageDao.findAllSonCompanyId(parentId);
			log.info("<CompanyServiceImpl>--<findAllSonCompanyId>--end"); 
			return relist;
		}else { 
			return null;
		}
	}
	
	@Override
	public List<Integer> findAllSonCompanyIdWithoutState(Integer parentId) {
		log.info("<CompanyServiceImpl>--<findAllSonCompanyIdWithoutState>--start"); 
		if (parentId !=null) {
			List<Integer> relist= companyManageDao.findAllSonCompanyIdWithoutState(parentId);
			log.info("<CompanyServiceImpl>--<findAllSonCompanyIdWithoutState>--end"); 
			return relist;
		}else { 
			return null;
		}
	}

	@Override
	public CompanyBean findCompanyById(Integer id) {
		log.info("<CompanyServiceImpl>--<findCompanyById>--start"); 
		CompanyBean re=companyManageDao.findCompanyById(id);
		log.info("<CompanyServiceImpl>--<findCompanyById>--end"); 
		return re;
	}
	
	
	public String findAllSonCompanyIdForInSql(Integer companyId){	
		log.info("<CompanyServiceImpl>--<findAllSonCompanyIdForInSql>--start"); 
		String re=companyManageDao.findAllSonCompanyIdForInSql1(companyId);
		log.info("<CompanyServiceImpl>--<findAllSonCompanyIdForInSql>--end"); 		
		return re;
	
	}


//	@Override
//	public List<CompanyBean> findAllCompany() {
//		return companyManageDao.findAllCompany();
//	}

	@Override
	public Integer createCompany(CompanyBean company) {
		log.info("<CompanyServiceImpl>--<createCompany>--start"); 
		Integer id = companyManageDao.createCompany(company);
		log.info("<CompanyServiceImpl>--<createCompany>--end"); 
		return id;
	}

	@Override
	public CompanyBean getCompany(Integer userId) {
		log.info("<CompanyServiceImpl>--<getCompany>--start"); 
		CompanyBean re=companyManageDao.getCompany(userId);
		log.info("<CompanyServiceImpl>--<getCompany>--end"); 
		return re;
	}

	@Override
	public ReturnDataUtil findCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyServiceImpl>--<findCompanyByForm>--start"); 
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<CompanyBean> companyList = companyManageDao.findCompanyByForm(companyForm);
		Long total = companyManageDao.findCompanyByFormNum(companyForm);
		if(companyList!=null && companyList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(companyList);
			returnData.setTotal(total);
			returnData.setTotalPage(returnData.getTotalPage());
			returnData.setCurrentPage(companyForm.getCurrentPage());
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
			returnData.setReturnObject(null);
		}
		log.info("<CompanyServiceImpl>--<findCompanyByForm>--end"); 
		return returnData;
	}

	@Override
	public boolean updateCompany(CompanyBean company) {
		log.info("<CompanyServiceImpl>--<updateCompany>--start"); 
		boolean re= companyManageDao.updateCompany(company);
		log.info("<CompanyServiceImpl>--<updateCompany>--end"); 
		return re;
	}

	@Override
	public boolean isOnlyOne(String companyName) {
		log.info("<CompanyServiceImpl>--<isOnlyOne>--start"); 
		boolean result = companyManageDao.isOnlyOne(companyName);
		log.info("<CompanyServiceImpl>--<isOnlyOne>--end"); 
		return result;
	}

	/**
	 * 处理图片名字
	 * @return
	 */
	public String findImgName(String fileName) {
		String imgName=null;
		String date = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		String[] split = date.split(" ");
		String[] split2 = split[0].split("-");
		String[] split3 = split[1].split(":");
		String name=fileName.substring(fileName.length()-4, fileName.length());
		imgName=split2[0]+split2[1]+split2[2]+"_"+split3[0]+split3[1]+split3[2]+name;
		return imgName;
	}

	@Override
	public ReturnDataUtil findSecondCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyServiceImpl>--<findSecondCompanyByForm>--start"); 
		ReturnDataUtil returnData = new ReturnDataUtil();
		//根据公司id 返回全部公司或此公司
//		Boolean flag=false;
		if(companyForm.getCompanyId().intValue()!=1) {
			List<CompanyBean> userCompanyList = companyManageDao.findCompanyByForm(companyForm);
			if(userCompanyList!=null && userCompanyList.size()>0) {
				returnData.setStatus(1);
				returnData.setMessage("查询此公司信息成功");
				returnData.setReturnObject(userCompanyList);
				returnData.setTotal(null);
				returnData.setTotalPage(returnData.getTotalPage());
				returnData.setCurrentPage(companyForm.getCurrentPage());
				return returnData;
			}
			else{
				returnData.setStatus(0);
				returnData.setMessage("查询失败");
				returnData.setReturnObject(null);
			}
		}
		else {
			companyForm = new CompanyForm();
			companyForm.setIsShowAll(1);
			List<CompanyBean> companyList = companyManageDao.findCompanyByForm(companyForm);
			if(companyList!=null && companyList.size()>0){
				returnData.setStatus(1);
				returnData.setMessage("查询全部公司成功");
				returnData.setReturnObject(companyList);
				returnData.setTotal((long)companyList.size());
				returnData.setTotalPage(returnData.getTotalPage());
				returnData.setCurrentPage(companyForm.getCurrentPage());
			}else{
				returnData.setStatus(0);
				returnData.setMessage("查询失败");
				returnData.setReturnObject(null);
			}
		}
		
		
		log.info("<CompanyServiceImpl>--<findSecondCompanyByForm>--end"); 
		return returnData;
	}


	@Override
	public ReturnDataUtil findLimitSecondCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyServiceImpl>--<findLimitSecondCompanyByForm>--start"); 
		ReturnDataUtil returnData = new ReturnDataUtil();
		//List<CompanyBean> userCompanyList = companyManageDao.findCompanyByForm(companyForm);
		List<CompanyBean> companyList =Lists.newArrayList();
		if(companyForm.getThisCompanyId().intValue()==1) {
			companyList = null;
		}
		else{
			companyList = companyManageDao.findCompanyByForm(companyForm);
		}
		
		if(companyList!=null && companyList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询其他所有公司成功");
			returnData.setReturnObject(companyList);
			returnData.setTotal((long)companyList.size());
			returnData.setTotalPage(returnData.getTotalPage());
			returnData.setCurrentPage(companyForm.getCurrentPage());
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
			returnData.setReturnObject(null);
		}
		log.info("<CompanyServiceImpl>--<findLimitSecondCompanyByForm>--end"); 
		return returnData;
	}
	
	@Override
	public ReturnDataUtil findFatherCompanyByForm(CompanyForm companyForm) {
		log.info("<CompanyServiceImpl>--<findFatherCompanyByForm>--start"); 
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<CompanyBean> companyList = companyManageDao.findFatherCompanyByForm(companyForm);
		//Long total = companyManageDao.findCompanyByFormNum(companyForm);
		if(companyList!=null && companyList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询父公司成功");
			returnData.setReturnObject(companyList);
			returnData.setTotal((long)companyList.size());
			returnData.setTotalPage(returnData.getTotalPage());
			returnData.setCurrentPage(companyForm.getCurrentPage());
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询失败");
			returnData.setReturnObject(null);
		}
		log.info("<CompanyServiceImpl>--<findFatherCompanyByForm>--end"); 
		return returnData;
	}
	
	@Override
	public List<CompanyBean> findAllSonCompanyAndFatherName(Integer parentId) {
		log.info("<CompanyServiceImpl>--<findAllSonCompanyAndFatherName>--start"); 
		if(parentId!=null) {
			List<CompanyBean> relist=companyManageDao.findAllSonCompany(parentId);
			if(parentId.intValue()!=1) {
				CompanyForm companyForm=new CompanyForm();
				companyForm.setCompanyId(parentId);
				List<CompanyBean> companyList = companyManageDao.findFatherCompanyByForm(companyForm);
				//添加父公司姓名
				for(CompanyBean cb:companyList) {
					for(CompanyBean cbr:relist) {
						if(cb.getId().intValue()==cbr.getParentId().intValue()) {
							cbr.setParentName(cb.getName());
							break;
						}
					}
				}
			}
			log.info("<CompanyServiceImpl>--<findAllSonCompanyAndFatherName>--end"); 
			return relist;
		}
		else {
			return null;
		}
	}

	@Override
	public MerchantInfo getPayConfig(Integer companyId) {
		log.info("<CompanyServiceImpl>--<getPayConfig>--start"); 
		MerchantInfo payConfig = merchantInfoDao.getPayConfig(companyId);
		log.info("<CompanyServiceImpl>--<getPayConfig>--end"); 
		return payConfig;
	}

	@Override
	public boolean updatePayConfig(MerchantInfo merchant) {
		log.info("<CompanyServiceImpl>--<updatePayConfig>--start");
		boolean updatePayConfig = merchantInfoDao.updatePayConfig(merchant);
		log.info("<CompanyServiceImpl>--<updatePayConfig>--end");
		return updatePayConfig;
	}

	@Override
	public Integer insertPayConfig(MerchantInfo merchant) {
		log.info("<CompanyServiceImpl>--<insertPayConfig>--start");
		Integer insertPayConfig = merchantInfoDao.insertPayConfig(merchant);
		log.info("<CompanyServiceImpl>--<insertPayConfig>--end");
		return insertPayConfig;
	}
	
	/**
	 * 判断是否拥有子公司
	 */
	@Override
	public boolean checkIsSubsidiaries(Integer companyId) {
		log.info("<CompanyServiceImpl>--<checkIsSubsidiaries>--start");
		boolean flag = companyManageDao.checkIsSubsidiaries(companyId);
		log.info("<CompanyServiceImpl>--<checkIsSubsidiaries>--end");
		return flag;
	}

	@Override
	public AliPayConfig getAliPayConfig(Integer companyId) {
		log.info("<CompanyServiceImpl>--<getAliPayConfig>--start");
		AliPayConfig findAliConfig = alipayDao.findAliConfig(companyId);
		log.info("<CompanyServiceImpl>--<getAliPayConfig>--end");
		return findAliConfig;
	}

	@Override
	public boolean updateAliPayConfig(AliPayConfig aliConfig) {
		log.info("<CompanyServiceImpl>--<updateAliPayConfig>--start");
		boolean updateAliConfig = alipayDao.updateAliConfig(aliConfig);
		log.info("<CompanyServiceImpl>--<updateAliPayConfig>--end");
		return updateAliConfig;
	}

	@Override
	public boolean insertAliPayConfig(AliPayConfig aliConfig) {
		log.info("<CompanyServiceImpl>--<insertAliPayConfig>--start");
		boolean insertAliConfig = alipayDao.insertAliConfig(aliConfig);
		log.info("<CompanyServiceImpl>--<insertAliPayConfig>--end");
		return insertAliConfig;
	}
	
	@Override
	public boolean checkIsReplenishCompany(Integer replenishId){
		log.info("<CompanyServiceImpl>--<checkIsReplenishCompany>--start");
		boolean checkIsReplenishCompany = companyManageDao.checkIsReplenishCompany(replenishId);
		log.info("<CompanyServiceImpl>--<checkIsReplenishCompany>--end");
		return checkIsReplenishCompany;
	}

}
