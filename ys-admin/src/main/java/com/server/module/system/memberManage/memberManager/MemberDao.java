package com.server.module.system.memberManage.memberManager;

import java.math.BigDecimal;
import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-09-20 17:45:20
 */
public interface MemberDao {

	/**
	 * 会员列表
	 * @author why
	 * @date 2018年9月20日 下午5:48:15 
	 * @param memberForm
	 * @return
	 */
	public ReturnDataUtil listPage(MemberForm memberForm);
	
	/**
	 * 增加会员
	 * @author why
	 * @date 2018年9月21日 上午9:02:35 
	 * @param entity
	 * @return
	 */
	public boolean add(MemberBean entity);
	
	/**
	 * 查询是否优水平台用户
	 * @author why
	 * @date 2018年9月21日 上午9:29:01 
	 * @param phone
	 * @return
	 */
	public List<MemberBean> getBean(String phone);
	
	/**
	 * 修改会员等级
	 * @author why
	 * @date 2018年9月21日 上午10:11:13 
	 * @param entity
	 * @return
	 */
	public boolean update(MemberBean entity);
	
	/**
	 * 删除会员
	 * @author why
	 * @date 2018年9月21日 上午10:23:52 
	 * @param phone
	 * @return
	 */
	public boolean  deleteMember(String phone);
	
	/**
	 * 判断是否是会员
	 * @author why
	 * @date 2018年9月26日 上午11:08:51 
	 * @return
	 */
	public ReturnDataUtil judgeMember();
	
	/**
	 * 得到会员信息
	 * @author why
	 * @date 2018年9月28日 上午10:23:01 
	 * @param id
	 * @return
	 */
	public MemberBean findBean(Long id);
	
	/**
	 * 	更新用户余额
	 * @author why
	 * @date 2018年11月8日 下午4:40:21 
	 * @param customerId
	 * @param money
	 * @return
	 */
	public boolean updateUserBalance(Long customerId,BigDecimal money);
	
	/**
	 * 插入使用会员余额记录
	 * @author why
	 * @date 2018年12月18日 下午4:01:28 
	 * @param memberLog
	 * @return
	 */
	public Long insertMemberUseLog(MemberUseLog memberLog);
	
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
