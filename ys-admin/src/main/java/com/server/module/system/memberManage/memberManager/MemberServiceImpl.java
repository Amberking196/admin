package com.server.module.system.memberManage.memberManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.memberManage.memberTypeManage.MemberTypeBean;
import com.server.module.system.memberManage.memberTypeManage.MemberTypeDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;

@Service
public class MemberServiceImpl implements MemberService {

	private static Logger log = LogManager.getLogger(MemberServiceImpl.class);
	@Autowired
	private MemberDao memberDaoImpl;
	@Autowired
	private MemberTypeDao memberTypeDaoImpl;

	/**
	 * 会员列表
	 */
	@Override
	public ReturnDataUtil listPage(MemberForm memberForm) {
		log.info("<MemberServiceImpl>----<listPage>------start");
		ReturnDataUtil listPage = memberDaoImpl.listPage(memberForm);
		log.info("<MemberServiceImpl>----<listPage>------end");
		return listPage;
	}

	/**
	 * 添加会员
	 */
	@Override
	public boolean add(MemberBean entity, List<MemberBean> list) {
		log.info("<MemberServiceImpl>----<add>------start");
		if (list != null && list.size() > 0) {
			for (MemberBean memberBean : list) {
				if (memberBean.getIsMember() == 1) {// 已经是会员 进行累加有效期
					// 得到会员开始时间
					Date startTimeLabel = memberBean.getStartTimeLabel();
					String startTime = DateUtil.formatYYYYMMDDHHMMSS(startTimeLabel);
					entity.setStartTime(startTime);
					// 得到会员结束时间
					Date endTimeLabel = memberBean.getEndTimeLabel();
					Calendar cal = Calendar.getInstance();
					cal.setTime(endTimeLabel);
					cal.add(Calendar.MONTH, 1);
					String endTime = DateUtil.formatYYYYMMDDHHMMSS(cal.getTime());
					entity.setEndTime(endTime);
				} else {// 首次成为会员
						// 得到当前时间
					Date date = new Date();
					String startTime = DateUtil.formatYYYYMMDDHHMMSS(date);
					entity.setStartTime(startTime);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.MONTH, 1);
					String endTime = DateUtil.formatYYYYMMDDHHMMSS(cal.getTime());
					entity.setEndTime(endTime);
				}
			}
		}
		boolean add = memberDaoImpl.add(entity);
		log.info("<MemberServiceImpl>----<add>------end");
		return add;
	}

	/**
	 * 查询是否优水平台用户
	 */
	@Override
	public List<MemberBean> getBean(String phone) {
		log.info("<MemberServiceImpl>----<getBean>------start");
		List<MemberBean> list = memberDaoImpl.getBean(phone);
		log.info("<MemberServiceImpl>----<getBean>------end");
		return list;
	}

	/**
	 * 修改会员的类型
	 */
	@Override
	public boolean udpate(MemberBean entity) {
		log.info("<MemberServiceImpl>----<udpate>------start");
		log.info("<MemberServiceImpl-----------update>----会员类型=====" + entity.getValidity());
		// 得到当前时间
		Date date = new Date();
		String startTime = DateUtil.formatYYYYMMDDHHMMSS(date);
		entity.setStartTime(startTime);
		// 月会员
		if (entity.getValidity() == 1) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			String endTime = DateUtil.formatYYYYMMDDHHMMSS(cal.getTime());
			entity.setEndTime(endTime);
		}
		// 年会员
		if (entity.getValidity() == 2) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, 1);
			String endTime = DateUtil.formatYYYYMMDDHHMMSS(cal.getTime());
			entity.setEndTime(endTime);
		}
		boolean update = memberDaoImpl.update(entity);
		log.info("<MemberServiceImpl>----<udpate>------end");
		return update;
	}

	/**
	 * 删除会员
	 */
	@Override
	public boolean deleteMember(String phone) {
		log.info("<MemberServiceImpl>----<delete>------start");
		boolean delete = memberDaoImpl.deleteMember(phone);
		log.info("<MemberServiceImpl>----<delete>------end");
		return delete;
	}

	/**
	 * 判断是否是会员
	 */
	@Override
	public ReturnDataUtil judgeMember() {
		log.info("<MemberServiceImpl>----<judgeMember>------start");
		ReturnDataUtil returnDataUtil = memberDaoImpl.judgeMember();
		log.info("<MemberServiceImpl>----<judgeMember>------end");
		return returnDataUtil;
	}

	@Override
	public MemberBean findBean(Long id) {
		log.info("<MemberServiceImpl>----<findBean>------start");
		MemberBean bean = memberDaoImpl.findBean(id);
		log.info("<MemberServiceImpl>----<findBean>------end");
		return bean;
	}

	@Override
	public boolean updateIntegral(Long customerId, Long integral) {
		log.info("<MemberServiceImpl>----<updateIntegral>------start");
		 boolean flag = memberDaoImpl.updateIntegral(customerId, integral);
		log.info("<MemberServiceImpl>----<updateIntegral>------end");
		return flag;
	}

}
