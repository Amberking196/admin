package com.server.module.system.officialManage.officialArticle;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-01 15:07:48
 */
public interface OfficialArticleDao {

	/**
	 * 文章列表查询
	 * @param officialArticleForm
	 * @return
	 */
	public ReturnDataUtil listPage(OfficialArticleForm officialArticleForm);


	/**
	 * 编辑文章
	 * @param entity
	 * @return
	 */
	public boolean update(OfficialArticleBean entity);

	/**
	 * 删除文章
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

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
	public OfficialArticleBean insert(OfficialArticleBean entity);
	
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
