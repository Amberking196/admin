package com.server.module.system.promotionManage.timeQuantum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-22 10:56:46
 */
@Service
public class TimeQuantumServiceImpl implements TimeQuantumService {

	private static Logger log = LogManager.getLogger(TimeQuantumServiceImpl.class);
	
	@Autowired
	private TimeQuantumDao timeQuantumDaoImpl;

	/**
	 * 时间段列表查询
	 */
	public ReturnDataUtil listPage(TimeQuantumForm timeQuantumForm) {
		log.info("<TimeQuantumServiceImpl>----<listPage>------start");
		ReturnDataUtil listPage = timeQuantumDaoImpl.listPage(timeQuantumForm);
		log.info("<TimeQuantumServiceImpl>----<listPage>------end");
		return listPage;
	}

	/**
	 * 时间段增加
	 */
	public TimeQuantumBean add(TimeQuantumBean entity) {
		log.info("<TimeQuantumServiceImpl>----<add>-------start");
		TimeQuantumBean timeQuantumBean = timeQuantumDaoImpl.insert(entity);
		log.info("<TimeQuantumServiceImpl>----<add>-------end");
		return timeQuantumBean;
	}

	/**
	 * 时间段列表修改
	 */
	public boolean update(TimeQuantumBean entity) {
		log.info("<TimeQuantumServiceImpl>----<update>------start");
		boolean update = timeQuantumDaoImpl.update(entity);
		log.info("<TimeQuantumServiceImpl>----<update>------end");
		return update;
	}

	/**
	 * 时间段 删除
	 */
	public boolean del(Object id) {
		log.info("<TimeQuantumServiceImpl>----<del>------start");
		boolean delete = timeQuantumDaoImpl.delete(id);
		log.info("<TimeQuantumServiceImpl>----<del>------end");
		return delete;
	}

	

	public TimeQuantumBean get(Object id) {
		return timeQuantumDaoImpl.get(id);
	}

	/**
	 * 通过多个id 查询时间段
	 */
	@Override
	public List<TimeQuantumBean> list(String ids) {
		log.info("<TimeQuantumServiceImpl>----<list>------start");
		List<TimeQuantumBean> list = timeQuantumDaoImpl.list(ids);
		log.info("<TimeQuantumServiceImpl>----<list>------end");
		return list;
	}

	/**
	 * 条件查询时间段信息
	 */
	@Override
	public List<TimeQuantumBean> findBean(TimeQuantumForm timeQuantumForm) {
		log.info("<TimeQuantumServiceImpl>----<findBean>------start");
		List<TimeQuantumBean> list = timeQuantumDaoImpl.findBean(timeQuantumForm);
		log.info("<TimeQuantumServiceImpl>----<findBean>------end");
		return list;
	}
}
