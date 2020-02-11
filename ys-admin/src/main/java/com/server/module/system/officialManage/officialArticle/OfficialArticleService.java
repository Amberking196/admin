package com.server.module.system.officialManage.officialArticle;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-01 15:07:48
 */
public interface OfficialArticleService {

	/**
	 * 文章列表查询
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(OfficialArticleForm condition);


	/**
	 * 编辑文章
	 * @param entity
	 * @return
	 */
	public boolean update(OfficialArticleBean entity);
	/**
	 * 文章删除
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	/**
	 * 文章预览
	 * @param id
	 * @return
	 */
	public OfficialArticleBean get(Object id);

	/**
	 * 文章发布
	 * @param entity
	 * @return
	 */
	public OfficialArticleBean add(OfficialArticleBean entity);
	
	/**
	 * 官网详情
	 * @param id
	 * @return
	 */
	public OfficialArticleDto findDto(Long id);

	/**
	 * 机器信息列表查询
	 * @param officialArticleForm
	 * @return
	 */
    ReturnDataUtil machineListPage( OfficialArticleForm officialArticleForm );
}
