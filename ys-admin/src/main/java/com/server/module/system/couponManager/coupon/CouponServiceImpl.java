package com.server.module.system.couponManager.coupon;

import com.google.common.collect.Lists;
import com.server.module.customer.CustomerUtil;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.couponManager.couponCustomer.CouponCustomerBean;
import com.server.module.system.couponManager.couponCustomer.CouponCustomerDao;
import com.server.module.system.couponManager.couponCustomer.IdAndVmCodeVo;
import com.server.module.system.couponManager.couponMachine.CouponMachineBean;
import com.server.module.system.couponManager.couponMachine.CouponMachineDao;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-06-28 09:01:06
 */
@Service
public class CouponServiceImpl implements CouponService {

	private static Logger log = LogManager.getLogger(CouponServiceImpl.class);
	@Autowired
	private CouponDao couponDaoImpl;
	@Autowired
	private CouponMachineDao couponMachineDao;
	@Autowired
	private CouponCustomerDao couponCustomerDao;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;

	public ReturnDataUtil listPage(CouponForm condition) {
		return couponDaoImpl.listPage(condition);
	}

	public ReturnDataUtil add(CouponAddVo vo) {
		log.info("vo----------------" + vo.getMoney() + "===" + vo.getDeductionMoney());
		ReturnDataUtil re = new ReturnDataUtil();
		CouponBean entity = new CouponBean();
		BeanUtils.copyProperties(vo, entity);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String temp = sf.format(entity.getEndTime());
		DateTime dt1 = new DateTime(entity.getEndTime().getTime());
		dt1 = dt1.withHourOfDay(23);
		dt1 = dt1.withMinuteOfHour(59);
		dt1 = dt1.withSecondOfMinute(59);
		entity.setEndTime(dt1.toDate());

		DateTime dt2 = new DateTime(entity.getStartTime().getTime());
		dt2 = dt2.withHourOfDay(0);
		dt2 = dt2.withMinuteOfHour(0);
		dt2 = dt2.withSecondOfMinute(0);
		entity.setStartTime(dt2.toDate());

		if (entity.getTarget() == null) {
			entity.setTarget(0);
		}
		if (entity.getVmCode() != null && StringUtil.isNotBlank(entity.getVmCode())
				&& !entity.getVmCode().equals("0")) {
			VendingMachinesInfoBean bean = vendingMachinesInfoService.findVendingMachinesByCode(entity.getVmCode());
			if (bean == null) {
				re.setStatus(-99);
				re.setMessage("输入有误，不存在的机器编码");
				return re;
			}
		}
		Long userId = UserUtils.getUser().getId();

		entity.setCreateUser(userId);
		return new ReturnDataUtil(couponDaoImpl.insert(entity));
	}

	public ReturnDataUtil update(CouponBean entity) {
		ReturnDataUtil re = new ReturnDataUtil();
		if (entity.getVmCode() != null && StringUtil.isNotBlank(entity.getVmCode())
				&& !entity.getVmCode().equals("0")) {
			VendingMachinesInfoBean bean = vendingMachinesInfoService.findVendingMachinesByCode(entity.getVmCode());
			if (bean == null) {
				re.setStatus(-99);
				re.setMessage("不存在的机器编码");
				re.setReturnObject(false);
				return re;
			}
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String temp = sf.format(entity.getEndTime());
		DateTime dt1 = new DateTime(entity.getEndTime().getTime());
		dt1 = dt1.withHourOfDay(23);
		dt1 = dt1.withMinuteOfHour(59);
		dt1 = dt1.withSecondOfMinute(59);
		entity.setEndTime(dt1.toDate());

		DateTime dt2 = new DateTime(entity.getStartTime().getTime());
		dt2 = dt2.withHourOfDay(0);
		dt2 = dt2.withMinuteOfHour(0);
		dt2 = dt2.withSecondOfMinute(0);
		entity.setStartTime(dt2.toDate());
		entity.setUpdateTime(new Date());
		entity.setUpdateUser(UserUtils.getUser().getId());
		return new ReturnDataUtil(couponDaoImpl.update(entity));
	}

	public boolean del(Object id) {

		return couponDaoImpl.delete(id);
	}

	public List<CouponBean> list(CouponForm condition) {
		return null;
	}

	public CouponBean get(Object id) {
		return couponDaoImpl.get(id);
	}

	@Override
	public ReturnDataUtil addMachine(String[] codes, Long couponId) {
		Long userId = 1l;
		List<String> addedCodeList = couponMachineDao.selectAllVmCodeByCoupon(couponId);

		for (String code : codes) {
			CouponMachineBean cm = new CouponMachineBean();
			cm.setCouponId(couponId);
			cm.setVmCode(code);
			cm.setCreateUser(userId);
			if (!addedCodeList.contains(code))
				couponMachineDao.insert(cm);
		}
		ReturnDataUtil re = new ReturnDataUtil();
		re.setMessage("添加机器成功");
		return re;
	}

	@Override
	public ReturnDataUtil addAllMachine(AddAllMachineForm condition) {
		Long userId = 1l;
		ReturnDataUtil re = new ReturnDataUtil();
		if (StringUtil.isBlank(condition.getCompanyId())) {
			re.setMessage("必须要选择公司");
			return re;
		}
		if (condition.getCouponId() == null || condition.getCouponId() == 0) {
			re.setMessage("必须要有优惠券");
			return re;
		}

		List<String> codeList = couponMachineDao.allVmCodeByCompanyIdOrAreaId(condition.getCompanyId(),
				condition.getAreaId());

		String[] codes = new String[codeList.size()];
		codeList.toArray(codes);
		List<String> addedCodeList = couponMachineDao.selectAllVmCodeByCoupon(condition.getCouponId());

		for (String code : codes) {
			CouponMachineBean cm = new CouponMachineBean();
			cm.setCouponId(condition.getCouponId());
			cm.setVmCode(code);
			cm.setCreateUser(userId);
			if (!addedCodeList.contains(code))
				couponMachineDao.insert(cm);
		}
		re.setMessage("添加机器成功");
		return re;
	}

	@Override
	public ReturnDataUtil addCustomer(Long[] customerIds, Long couponId) {
		Long userId = UserUtils.getUser().getId();
		ReturnDataUtil re = new ReturnDataUtil();
		CouponBean coupon = couponDaoImpl.get(couponId);
		if (coupon.getCanSend() == 1) {
			re.setStatus(-99);
			re.setMessage("已禁用不能发送优惠劵");
			return re;
		}

		for (Long customerId : customerIds) {
			CouponCustomerBean cc = new CouponCustomerBean();
			cc.setCouponId(couponId);
			cc.setCustomerId(customerId);
			cc.setState(CouponCustomerBean.STATE_GET);
			cc.setCreateUser(userId);
			cc.setQuantity(coupon.getSendMax().longValue());
			cc.setStartTime(coupon.getLogicStartTime());
			cc.setEndTime(coupon.getLogicEndTime());
			couponCustomerDao.insert(cc);

		}

		re.setMessage("添加优惠劵成功");
		return re;
	}

	@Override
	public ReturnDataUtil addAllCustomer(AddAllCustomerForm condition) {
		Long userId = UserUtils.getUser().getId();
		ReturnDataUtil re = new ReturnDataUtil();
		/*
		 * if(StringUtil.isBlank(condition.getCompanyId())){ re.setMessage("必须要选择公司");
		 * return re; }
		 */
		if (condition.getCouponId() == null || condition.getCouponId() == 0) {
			re.setMessage("必须要有优惠券");
			return re;
		}
		CouponBean coupon = couponDaoImpl.get(condition.getCouponId());

		if (coupon.getCanSend() == 1) {
			re.setStatus(-99);
			re.setMessage("已禁用不能发送优惠劵");
			return re;
		}
		List<Long> customerLists = couponCustomerDao.getAllCustomerId(condition);

		/*
		 * List<String> sqls= Lists.newArrayList(); for (int i = 0; i <
		 * customerLists.size(); i++) { StringBuilder sb=new StringBuilder(); sb.
		 * append("insert into coupon_customer(couponId,customerId,vmCode,state,createUser) values ("
		 * ); sb.append(condition.getCouponId()+","); IdAndVmCodeVo
		 * vo=customerLists.get(i); sb.append(vo.getId()+",");
		 * sb.append("'"+vo.getVmCode()+"',");
		 * sb.append(CouponCustomerBean.STATE_GET+","); sb.append(userId+")");
		 * System.out.println(sb.length()); sqls.add(sb.toString());
		 * 
		 * }
		 */

		/// couponCustomerDao.batchInsertSql(sqls);

		int result = couponCustomerDao.batchInsert(customerLists, condition.getCouponId());

		if (result == 0) {
			re.setStatus(-1);
			re.setMessage("批量插入报错");
		}
		return re;
	}

	@Override
	public List<CouponBean> getPresentCoupon(CouponForm couponForm) {
		log.info("<CouponServiceImpl>----<getPresentCoupon>-----start");
		List<CouponBean> list = couponDaoImpl.getPresentCoupon(couponForm);
		log.info("<CouponServiceImpl>----<getPresentCoupon>-----end");
		return list;
	}

	/**
	 * 亚运城用户下发优惠券
	 */
	@Override
	public ReturnDataUtil addAsianCustomer() {
		log.info("<CouponServiceImpl>----<addAsianCustomer>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		List<CouponBean> list = couponDaoImpl.getAsianCoupon();
		if (list.size() > 0 && list != null) {
			for (CouponBean couponBean : list) {
				// 判断用户已经领取该优惠券
				if (couponDaoImpl.isReceive(CustomerUtil.getCustomerId(), couponBean.getId())) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("该优惠券您已经领取了，请勿重复领取！");
				} else {
					CouponCustomerBean cc = new CouponCustomerBean();
					cc.setCouponId(couponBean.getId());
					cc.setCustomerId(CustomerUtil.getCustomerId());
					cc.setState(CouponCustomerBean.STATE_GET);
					cc.setCreateUser(UserUtils.getUser().getId());
					cc.setQuantity(couponBean.getSendMax().longValue());
					cc.setStartTime(couponBean.getLogicStartTime());
					cc.setEndTime(couponBean.getLogicEndTime());
					CouponCustomerBean insert = couponCustomerDao.insert(cc);
					if (insert != null) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("优惠券领取成功！");
						returnDataUtil.setReturnObject(insert);
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("优惠券领取失败！");
						returnDataUtil.setReturnObject(insert);
					}
				}
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("优惠券领取失败！");
		}
		return returnDataUtil;
	}
}
