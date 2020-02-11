package com.server.module.system.carryWaterVouchersManage.carryWaterVouchers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsVmCodeForm;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.ShoppingGoodsTypeEnum;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
@Service
public class CarryWaterVouchersServiceImpl implements CarryWaterVouchersService {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersServiceImpl.class);
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;

	/**
	 * 提水券列表
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersForm carryWaterVouchersForm) {
		log.info("<CarryWaterVouchersServiceImpl>------<listPage>------start");
		ReturnDataUtil listPage = carryWaterVouchersDaoImpl.listPage(carryWaterVouchersForm);
		log.info("<CarryWaterVouchersServiceImpl>------<listPage>------end");
		return listPage;
	}

	/**
	 * 提水券增加
	 */
	public CarryWaterVouchersBean add(CarryWaterVouchersVo vo) {
		log.info("<CarryWaterVouchersServiceImpl>------<add>------start");
		CarryWaterVouchersBean entity = new CarryWaterVouchersBean();
		BeanUtils.copyProperties(vo, entity);
		entity.setName(vo.getCarryWaterName());
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
		
		Long userId = UserUtils.getUser().getId();
		entity.setCreateUser(userId);
		CarryWaterVouchersBean insert = carryWaterVouchersDaoImpl.insert(entity);
		log.info("<CarryWaterVouchersServiceImpl>------<add>------end");
		return insert;
	}

	/**
	 * 提水券修改
	 */
	public ReturnDataUtil update(CarryWaterVouchersBean entity) {
		log.info("<CarryWaterVouchersServiceImpl>------<update>------start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (entity.getVmCode() != null && StringUtil.isNotBlank(entity.getVmCode())
				&& !entity.getVmCode().equals("0")) {
			VendingMachinesInfoBean bean = vendingMachinesInfoService.findVendingMachinesByCode(entity.getVmCode());
			if (bean == null) {
				returnDataUtil.setStatus(-99);
				returnDataUtil.setMessage("不存在的机器编码");
				returnDataUtil.setReturnObject(false);
				return returnDataUtil;
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
		boolean update = carryWaterVouchersDaoImpl.update(entity);
		if (update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("提水券修改成功！");
			returnDataUtil.setReturnObject(update);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("提水券修改失败！");
			returnDataUtil.setReturnObject(update);
		}
		log.info("<CarryWaterVouchersServiceImpl>------<update>------end");
		return returnDataUtil;
	}

	/**
	 * 提水券删除
	 */
	public boolean del(Object id) {
		log.info("<CarryWaterVouchersServiceImpl>------<del>------start");
		 boolean delete = carryWaterVouchersDaoImpl.delete(id);
		 log.info("<CarryWaterVouchersServiceImpl>------<del>------end");
		return delete;
	}

	public List<CarryWaterVouchersBean> list(CarryWaterVouchersForm condition) {
		return null;
	}

	public CarryWaterVouchersBean get(Object id) {
		return carryWaterVouchersDaoImpl.get(id);
	}
	
	@Override
	public boolean bindVmCode(ShoppingGoodsVmCodeForm entity) {
		log.info("<CarryWaterVouchersServiceImpl>----<bindVmCode>----start");
		CarryWaterVouchersBean sgb=carryWaterVouchersDaoImpl.get(entity.getId());	
		sgb.setTarget(entity.getTarget());
		sgb.setCompanyId(entity.getCompanyId()==null?null:entity.getCompanyId().longValue());
		sgb.setAreaId(entity.getAreaId()==null?null:entity.getCompanyId().longValue());

	    boolean flag=false;
    	if(entity.getTarget()==3) {
    		List<String> newVmCodeList = new ArrayList(Arrays.asList(StringUtils.split(entity.getVmCode(),",")));

    		String vmCode=sgb.getVmCode();
    		List<String> oldVmCodeList = Lists.newArrayList();
    		if(StringUtils.isNotBlank(vmCode)) {
    			oldVmCodeList = new ArrayList(Arrays.asList(StringUtils.split(vmCode,",")));
    		}
			if(entity.getIsBind()==1){
			    oldVmCodeList.addAll(newVmCodeList);
			    oldVmCodeList = oldVmCodeList.stream().distinct().collect(Collectors.toList()); 
				String nowVmCode = StringUtils.join(oldVmCodeList,",");
				sgb.setVmCode(nowVmCode);
			    flag=carryWaterVouchersDaoImpl.update(sgb);
			}else if(entity.getIsBind()==0){
			    oldVmCodeList.removeAll(newVmCodeList);
			    oldVmCodeList = oldVmCodeList.stream().distinct().collect(Collectors.toList()); 
				String nowVmCode = StringUtils.join(oldVmCodeList,",");
				sgb.setVmCode(nowVmCode);
				flag=carryWaterVouchersDaoImpl.update(sgb);
			}
	    }else {
		    flag=carryWaterVouchersDaoImpl.update(sgb);
	    }


		log.info("<CarryWaterVouchersServiceImpl>----<bindVmCode>----end");
		return flag;
	}
	
	public ReturnDataUtil queryBindCarryWater(ShoppingGoodsVmCodeForm entity) {
		return carryWaterVouchersDaoImpl.queryBindCarryWater(entity);
	}

}
