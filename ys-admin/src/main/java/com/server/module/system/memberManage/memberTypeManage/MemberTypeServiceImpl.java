package com.server.module.system.memberManage.memberTypeManage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.List;

import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-09-20 16:08:54
 */
@Service
public class MemberTypeServiceImpl implements MemberTypeService {

	private static Logger log = LogManager.getLogger(MemberTypeServiceImpl.class);
	@Autowired
	private MemberTypeDao memberTypeDaoImpl;

	/**
	 * 会员类型列表查询
	 */
	public ReturnDataUtil listPage(MemberTypeForm condition) {
		log.info("<MemberTypeServiceImpl>----<listPage>------start");
		ReturnDataUtil listPage = memberTypeDaoImpl.listPage(condition);
		log.info("<MemberTypeServiceImpl>----<listPage>------end");
		return listPage;
	}

	/**
	 * 会员类型增加
	 */
	public MemberTypeBean insert(MemberTypeBean entity) {
		log.info("<MemberTypeServiceImpl>----<insert>------start");
		MemberTypeBean insert = memberTypeDaoImpl.insert(entity);
		log.info("<MemberTypeServiceImpl>----<insert>------end");
		return insert;
	}

	/**
	 * 会员类型修改
	 */
	public boolean update(MemberTypeBean entity) {
		log.info("<MemberTypeServiceImpl>----<update>------start");
		boolean update = memberTypeDaoImpl.update(entity);
		log.info("<MemberTypeServiceImpl>----<update>------end");
		return update;
	}

	/**
	 * 会员类型删除
	 */
	public boolean del(Object id) {
		log.info("<MemberTypeServiceImpl>----<del>------start");
		boolean delete = memberTypeDaoImpl.delete(id);
		log.info("<MemberTypeServiceImpl>----<del>------end");
		return delete;
	}

	/**
	 * 会员类型下拉列表
	 */
	public List<MemberTypeBean> list() {
		log.info("<MemberTypeServiceImpl>----<list>------start");
		List<MemberTypeBean> list = memberTypeDaoImpl.list();
		log.info("<MemberTypeServiceImpl>----<list>------end");
		return list;
	}

	/**
	 * 查询某一个会员类型信息
	 */
	public MemberTypeBean get(Object id) {
		log.info("<MemberTypeServiceImpl>----<get>------start");
		MemberTypeBean memberTypeBean = memberTypeDaoImpl.get(id);
		log.info("<MemberTypeServiceImpl>----<get>------end");
		return memberTypeBean;
	}
	
	/**
	 * 校验会员类型不能重复
	 */
	@Override
	public boolean checkType(String type) {
		log.info("<MemberTypeServiceImpl>------<checkType>------start");
		boolean flag = memberTypeDaoImpl.checkType(type);
		log.info("<MemberTypeServiceImpl>------<checkType>------end");
		return flag;
	}
}
