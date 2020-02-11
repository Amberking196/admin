package com.server.module.system.forumManage.forumFilterManage;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * 
 * @author why
 * @date: 2019年3月14日 上午11:29:06
 */
public interface ForumFilterDao {

	/**
	 * 过滤名词列表
	 * @author why
	 * @date 2019年3月14日 上午11:39:26 
	 * @param forumFilterForm
	 * @return
	 */
	public ReturnDataUtil listPage(ForumFilterForm forumFilterForm);

	public List<ForumFilterBean> list();

	/**
	 * 修改过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:34:36 
	 * @param entity
	 * @return
	 */
	public boolean update(ForumFilterBean entity);

	/**
	 * 删除过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:34:48 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 
	 * 查询过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:34:59 
	 * @param id
	 * @return
	 */
	public ForumFilterBean get(Object id);

	/**
	 * 新增过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:35:07 
	 * @param entity
	 * @return
	 */
	public ForumFilterBean insert(ForumFilterBean entity);
	
	/**
	 * 判断关键字是否存在
	 * @author why
	 * @date 2019年3月14日 下午3:25:11 
	 * @param noun
	 * @return
	 */
	public boolean isNounExists(String noun);
}
