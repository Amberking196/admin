package com.server.module.system.officialManage.officialArticle;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-01 15:07:48
 */
@Service
public class OfficialArticleServiceImpl implements OfficialArticleService {

	private static Logger log = LogManager.getLogger(OfficialArticleServiceImpl.class);
	@Autowired
	private OfficialArticleDao officialArticleDaoImpl;

	/**
	 * 文章列表查询
	 */
	public ReturnDataUtil listPage(OfficialArticleForm condition) {
		log.info("<OfficialArticleServiceImpl>----<listPage>----start");
		ReturnDataUtil listPage = officialArticleDaoImpl.listPage(condition);
		log.info("<OfficialArticleServiceImpl>----<listPage>----end");
		return listPage;
	}

	/**
	 * 文章发布
	 */
	public OfficialArticleBean add(OfficialArticleBean entity) {
		log.info("<OfficialArticleServiceImpl>----<add>----start");
		OfficialArticleBean officialArticleBean = officialArticleDaoImpl.insert(entity);
		log.info("<OfficialArticleServiceImpl>----<add>----end");
		return officialArticleBean;
	}

	/**
	 * 编辑文章
	 */
	public boolean update(OfficialArticleBean entity) {
		log.info("<OfficialArticleServiceImpl>----<update>----start");
		boolean update = officialArticleDaoImpl.update(entity);
		log.info("<OfficialArticleServiceImpl>----<update>----start");
		return update;
	}

	/**
	 * 文章删除
	 */
	public boolean del(Object id) {
		log.info("<OfficialArticleServiceImpl>----<del>----start");
		boolean delete = officialArticleDaoImpl.delete(id);
		log.info("<OfficialArticleServiceImpl>----<del>----end");
		return delete;
	}

	/**
	 * 文章预览
	 */
	public OfficialArticleBean get(Object id) {
		log.info("<OfficialArticleServiceImpl>----<get>----start");
		OfficialArticleBean officialArticleBean = officialArticleDaoImpl.get(id);
		log.info("<OfficialArticleServiceImpl>----<get>----end");
		return officialArticleBean;
	}

	/**
	 * 官网详情
	 */
	@Override
	public OfficialArticleDto findDto(Long id) {
		log.info("<OfficialArticleServiceImpl>----<findDto>----start");
		OfficialArticleDto officialArticleDto = officialArticleDaoImpl.findDto(id);
		log.info("<OfficialArticleServiceImpl>----<findDto>----end");
		return officialArticleDto;
	}

    @Override
    public ReturnDataUtil machineListPage( OfficialArticleForm officialArticleForm ) {
		log.info("<OfficialArticleServiceImpl>----<listPage>----start");
		ReturnDataUtil listPage = officialArticleDaoImpl.machineListPage(officialArticleForm);
		log.info("<OfficialArticleServiceImpl>----<listPage>----end");
		return listPage;
    }
}
