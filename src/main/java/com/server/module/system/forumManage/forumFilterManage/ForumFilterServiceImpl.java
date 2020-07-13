package com.server.module.system.forumManage.forumFilterManage;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * 
 * @author why
 * @date: 2019年3月14日 上午11:50:13
 */
@Service
public class ForumFilterServiceImpl implements ForumFilterService {

	private static Logger log = LogManager.getLogger(ForumFilterServiceImpl.class);
	@Autowired
	private ForumFilterDao forumFilterDaoImpl;

	public ReturnDataUtil listPage(ForumFilterForm forumFilterForm) {
		log.info("<ForumFilterServiceImpl>-------<listPage>------start");
		ReturnDataUtil listPage = forumFilterDaoImpl.listPage(forumFilterForm);
		log.info("<ForumFilterServiceImpl>-------<listPage>------end");
		return listPage;
	}

	public ForumFilterBean add(ForumFilterBean entity) {
		log.info("<ForumFilterServiceImpl>------<add>----start");
		ForumFilterBean bean = forumFilterDaoImpl.insert(entity);
		log.info("<ForumFilterServiceImpl>------<add>----end");
		return bean;
	}

	public boolean update(ForumFilterBean entity) {
		log.info("<ForumFilterServiceImpl>-------<update>-----start");
		boolean update = forumFilterDaoImpl.update(entity);
		log.info("<ForumFilterServiceImpl>-------<update>-----end");
		return update;
	}

	public boolean del(Object id) {
		log.info("<ForumFilterServiceImpl>-------<update>-----start");
		boolean delete = forumFilterDaoImpl.delete(id);
		log.info("<ForumFilterServiceImpl>-------<update>-----end");
		return delete;
	}

	public List<ForumFilterBean> list() {
		return null;
	}

	public ForumFilterBean get(Object id) {
		log.info("<ForumFilterServiceImpl>-------<get>------start");
		ForumFilterBean forumFilterBean = forumFilterDaoImpl.get(id);
		log.info("<ForumFilterServiceImpl>-------<get>------end");
		return forumFilterBean;
	}

	@Override
	public boolean isNounExists(String noun) {
		log.info("<ForumFilterServiceImpl>-------<isNounExists>------start");
		boolean nounExists = forumFilterDaoImpl.isNounExists(noun);
		log.info("<ForumFilterServiceImpl>-------<isNounExists>------end");
		return nounExists;
	}
}
