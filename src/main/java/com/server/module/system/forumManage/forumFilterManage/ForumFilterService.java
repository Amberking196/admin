package com.server.module.system.forumManage.forumFilterManage;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * 
 * @author why
 * @date: 2019年3月14日 上午11:49:42
 */
public interface ForumFilterService {

	/**
	 * 名词过滤列表
	 * @author why
	 * @date 2019年3月14日 上午11:49:48 
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(ForumFilterForm condition);

	public List<ForumFilterBean> list();

	/**
	 * 修改过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:55:54 
	 * @param entity
	 * @return
	 */
	public boolean update(ForumFilterBean entity);

	/**
	 * 删除过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:56:16 
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	/**
	 * 查询过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:57:20 
	 * @param id
	 * @return
	 */
	public ForumFilterBean get(Object id);

	/**
	 * 新增过滤名词
	 * @author why
	 * @date 2019年3月14日 下午2:57:28 
	 * @param entity
	 * @return
	 */
	public ForumFilterBean add(ForumFilterBean entity);
	
	/**
	 * 判断关键字是否存在
	 * @author why
	 * @date 2019年3月14日 下午3:38:24 
	 * @param noun
	 * @return
	 */
	public boolean isNounExists(String noun);
}
