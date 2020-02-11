package com.server.module.system.memberManage.memberTypeManage;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-09-20 16:08:54
 */
public interface MemberTypeDao {

	/**
	 * 会员类型列表查询
	 * @author why
	 * @date 2018年9月20日 下午4:15:57 
	 * @param memberTypeForm
	 * @return
	 */
	public ReturnDataUtil listPage(MemberTypeForm memberTypeForm);

	/**
	 * 会员类型下拉列表
	 * @author why
	 * @date 2018年9月20日 下午4:51:48 
	 * @return
	 */
	public List<MemberTypeBean> list();

	/**
	 * 会员类型修改
	 * @author why
	 * @date 2018年9月20日 下午4:44:50 
	 * @param entity
	 * @return
	 */
	public boolean update(MemberTypeBean entity);

	/**
	 * 会员类型删除
	 * @author why
	 * @date 2018年9月20日 下午4:48:29 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 查询某一个会员类型信息
	 * @author why
	 * @date 2018年9月20日 下午5:00:51 
	 * @param id
	 * @return
	 */
	public MemberTypeBean get(Object id);

	/**
	 * 会员类型增加
	 * @author why
	 * @date 2018年9月20日 下午4:42:43 
	 * @param entity
	 * @return
	 */
	public MemberTypeBean insert(MemberTypeBean entity);
	
	/**
	 * 校验会员类型不能重复
	 * @author why
	 * @date 2018年9月20日 下午4:36:13 
	 * @param type
	 * @return
	 */
	public boolean checkType(String type);
}
