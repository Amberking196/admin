package com.server.module.system.companyManage;

import java.util.List;

import com.server.module.system.companyManage.aliPayConfig.AliPayConfig;
import com.server.module.system.companyManage.wxPayConfig.MerchantInfo;
import com.server.util.ReturnDataUtil;

public interface CompanyService {

	/**
	 * 查询所有子公司包含母公司在内
	 * @param parentId 父级公司id
	 * @return 该公司及所有子公司
	 */
	List<CompanyBean> findAllSonCompany(Integer parentId);
	
	/**
	 * 查询该公司及子公司所有的companyId(不包含禁用公司)
	 * @param parentId
	 * @return
	 */
	List<Integer> findAllSonCompanyId(Integer parentId);
	
	/**
	 * 查询该公司及子公司所有的companyId
	 * @param parentId
	 * @return
	 */
	List<Integer> findAllSonCompanyIdWithoutState(Integer parentId);
	
	/**
	 * 根据id查询公司信息
	 * @param id 公司id
	 * @return CompanyBean
	 */
	CompanyBean findCompanyById(Integer id);
	
	
	public String findAllSonCompanyIdForInSql(Integer companyId);
	
	
	
	/**
	 * 更新公司信息
	 * @author hebiting
	 * @date 2018年4月19日上午10:37:21
	 * @param company
	 * @return
	 */
	boolean updateCompany(CompanyBean company);
	
	/**
	 * 添加公司
	 * @param company
	 * @return
	 */
	Integer createCompany(CompanyBean company);
	
	/**
	 *  通过用户ID 查询出公司信息
	 * @param userId 用户Id
	 * @return
	 */
	public CompanyBean getCompany(Integer userId);
	/**
	 * 根据条件查询公司信息
	 * @author hebiting
	 * @date 2018年4月12日下午10:20:54
	 * @param companyForm
	 * @return
	 */
	ReturnDataUtil findCompanyByForm(CompanyForm companyForm);


	/**
	 * 判断公司名是否重复
	 * @author hebiting
	 * @date 2018年5月9日下午5:13:00
	 * @param companyName
	 * @return
	 */
	boolean isOnlyOne(String companyName);
	
	/**
	 * 处理图片名字
	 * @return
	 */
	public String findImgName(String fileName);
	/**
	 * 查询一二级公司
	 * @return
	 */
	ReturnDataUtil findSecondCompanyByForm(CompanyForm companyForm);
	/**
	 * 查询一级和无子公司的二级公司
	 * @return
	 */
	ReturnDataUtil findLimitSecondCompanyByForm(CompanyForm companyForm);
	/**
	 * 查询父级公司
	 * @return
	 */
	ReturnDataUtil findFatherCompanyByForm(CompanyForm companyForm);
	/**
	 * 查询所有子公司和父公司名称
	 * @return
	 */
	List<CompanyBean> findAllSonCompanyAndFatherName(Integer parentId);
	
	
	/**
	 * 查看公司微信支付配置文件
	 * @author hebiting
	 * @date 2018年8月4日上午9:50:09
	 * @param companyId
	 * @return
	 */
	MerchantInfo getPayConfig(Integer companyId);
	
	/**
	 * 修改公司微信支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午10:16:29
	 * @param merchant
	 * @return
	 */
	boolean updatePayConfig(MerchantInfo merchant);
	
	/**
	 * 新增公司微信支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午11:25:14
	 * @param merchant
	 * @return
	 */
	Integer insertPayConfig(MerchantInfo merchant);
	
	
	/**
	 * 判断是否拥有子公司
	 * @return
	 */
	boolean checkIsSubsidiaries(Integer companyId);
	
	/**
	 * 查看公司支付宝支付配置文件
	 * @author hebiting
	 * @date 2018年8月4日上午9:50:09
	 * @param companyId
	 * @return
	 */
	AliPayConfig getAliPayConfig(Integer companyId);
	
	/**
	 * 修改公司支付宝支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午10:16:29
	 * @param merchant
	 * @return
	 */
	boolean updateAliPayConfig(AliPayConfig aliConfig);
	
	/**
	 * 新增公司支付宝支付配置信息
	 * @author hebiting
	 * @date 2018年8月4日上午11:25:14
	 * @param merchant
	 * @return
	 */
	boolean insertAliPayConfig(AliPayConfig aliConfig);

	/**
	 * 检测是否为补货公司
	 * @param replenishId
	 * @return
	 */
	boolean checkIsReplenishCompany(Integer replenishId);

}
