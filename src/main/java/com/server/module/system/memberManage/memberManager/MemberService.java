package com.server.module.system.memberManage.memberManager;

import java.util.List;

import com.server.util.ReturnDataUtil;

public interface MemberService {

	/**
	 * 会员列表
	 * @author why
	 * @date 2018年9月21日 上午8:50:21 
	 * @param memberForm
	 * @return
	 */
	public ReturnDataUtil listPage(MemberForm memberForm);
	
	/**
	 * 添加会员
	 * @author why
	 * @date 2018年10月8日 上午9:49:07 
	 * @param entity
	 * @param list
	 * @return
	 */
	public boolean add(MemberBean entity,List<MemberBean> list);
	
	/**
	 * 查询是否优水平台用户
	 * @author why
	 * @date 2018年9月21日 上午9:46:53 
	 * @param phone
	 * @return
	 */
	public List<MemberBean> getBean(String phone);
	
	/**
	 * 修改会员等级
	 * @author why
	 * @date 2018年9月21日 上午10:13:20 
	 * @param entity
	 * @return
	 */
	public boolean udpate(MemberBean entity);
	
	/**
	 * 删除会员
	 * @author why
	 * @date 2018年9月21日 上午10:26:33 
	 * @param phone
	 * @return
	 */
	public boolean deleteMember(String phone);
	
	/**
	 * 判断是否是会员
	 * @author why
	 * @date 2018年9月26日 上午11:23:24 
	 * @return
	 */
	public ReturnDataUtil judgeMember();
	
	/**
	 * 得到会员信息
	 * @author why
	 * @date 2018年10月8日 上午11:13:07 
	 * @param id
	 * @return
	 */
	public MemberBean findBean(Long id);
	
	/**
	 * 用户抽奖完成后 扣除本次需要积分
	 * @author why
	 * @date 2019年3月19日 上午11:50:32 
	 * @param customerId
	 * @param integral
	 * @return
	 */
	public boolean updateIntegral(Long customerId,Long integral);
}
