package com.server.module.system.companyManage;

import java.util.List;

public interface CompanyDao {

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
	/**
	 * 查询公司及其子公司id 拼装成 如(1,2,3)
	 * @param companyId
	 * @return
	 */
	public String findAllSonCompanyIdForInSql(Integer companyId);

	public String findAllSonCompanyIdForInSql1(Integer companyId);
	
	public String findAllFatherCompanyIdForInSql1(Integer companyId);


	/**
	 * 查询所有公司信息
	 * @return
	 */
	List<CompanyBean> findAllCompany();
	
	/**
	 * 添加公司
	 * @param company
	 * @return
	 */
	Integer createCompany(CompanyBean company);
	
	/**
	 *  通过 登录用户ID 查询出公司信息
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
	List<CompanyBean> findCompanyByForm(CompanyForm companyForm);
	/**
	 * 根据条件查询公司信息总数
	 * @author hebiting
	 * @date 2018年4月12日下午10:20:54
	 * @param companyForm
	 * @return
	 */
	Long findCompanyByFormNum(CompanyForm companyForm);
	/**
	 * 更新公司信息
	 * @author hebiting
	 * @date 2018年4月19日上午10:37:21
	 * @param company
	 * @return
	 */
	boolean updateCompany(CompanyBean company);
	/**
	 * 判断公司名是否重复
	 * @author hebiting
	 * @date 2018年5月9日下午5:13:00
	 * @param companyName
	 * @return
	 */
	boolean isOnlyOne(String companyName);
	/**
	 * 查询一二级公司
	 * @author hjc
	 * @date 2018年6月12日下午5:13:00
	 * @param companyName
	 * @return
	 */
	List<CompanyBean> findSecondCompanyByForm(CompanyForm companyForm);
	/**
	 * 查询一级公司和无子公司的二级公司
	 * @author hjc
	 * @date 2018年6月12日下午5:13:00
	 * @param companyName
	 * @return
	 */
	List<CompanyBean> findLimitSecondCompanyByForm(CompanyForm companyForm);
	/**
	 * 查询公司的父公司
	 * @author hjc
	 * @date 2018年6月12日下午5:13:00
	 * @param companyName
	 * @return
	 */
	List<CompanyBean> findFatherCompanyByForm(CompanyForm companyForm);
	
	/**
	 * 判断是否拥有子公司
	 * @return
	 */
	boolean checkIsSubsidiaries(Integer companyId);

	/**
	 * 根据公司id判断该公司是否为补货公司
	 *
	 */
	boolean checkIsReplenishCompany(Integer replenishId);
}
