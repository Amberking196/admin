package com.server.module.system.officialManage.officialMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-08-02 10:24:08
 * 官网留言逻辑类
 */ 
@Service
public class  OfficialMessageServiceImpl  implements OfficialMessageService{

	public static Logger log = LogManager.getLogger(OfficialMessageServiceImpl.class);
	@Autowired
	private OfficialMessageDao officialMessageDaoImpl;

	/**
	 * 官网留言列表
	 * @param officialMessageForm 查询条件对象
	 * @return
	 */
	public ReturnDataUtil listPage(OfficialMessageForm officialMessageForm){
		log.info("<OfficialMessageServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = officialMessageDaoImpl.listPage(officialMessageForm);
		log.info("<OfficialMessageServiceImpl>------<listPage>-----end");
		return listPage;
	}

	/**
	 * 官网留言添加方法
	 * @param entity 官网留言值对象
	 * @return
	 */
	public OfficialMessageBean add(OfficialMessageBean entity) {
		return officialMessageDaoImpl.insert(entity);
	}

	/**
	 * 官网留言修改方法
	 * @param entity 官网留言值对象
	 * @return
	 */
	public boolean update(OfficialMessageBean entity) {
		return officialMessageDaoImpl.update(entity);
	}

	/**
	 * 官网留言删除方法
	 * @param id 官网留言id
	 * @return
	 */
	public boolean del(Object id) {
		return officialMessageDaoImpl.delete(id);
	}
	/**
	 * 官网留言获取方法
	 * @param id 官网留言id
	 * @return
	 */
	public OfficialMessageBean get(Object id) {
		return officialMessageDaoImpl.get(id);
	}
}

