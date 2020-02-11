package com.server.module.system.game.send;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerDao;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.system.game.GameDto;
import com.server.module.system.game.GamePrizeBean;
import com.server.util.ReturnDataUtil;

/**
 * 提水券发送
 * 
 * @author why
 * @date: 2019年3月7日 上午10:20:47
 */
@Component
public class SendCarryWaterProcessor implements SendPrizeProcessor {

	private static Logger log = LogManager.getLogger(SendCarryWaterProcessor.class);
	@Autowired
	private CarryWaterVouchersCustomerDao carryWaterVouchersCustomerDaoImpl;
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;

	@Override
	public ReturnDataUtil send(GamePrizeBean prize, GameDto gameDto) {
		log.info("<SendCarryWaterProcessor>-----<send>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		if (prize.getRewardId() != null && prize.getRewardId() > 0) {
			//得到提水券信息
			CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersDaoImpl.get(prize.getRewardId());
			//得到本次购买数量和下发张数  最终为下发张数
			Integer num=carryWaterVouchersBean.getSendMax()*prize.getAmount();
			log.info("最终下发张数======"+num);
			CarryWaterVouchersCustomerBean entity=new CarryWaterVouchersCustomerBean();
			entity.setCarryId(prize.getRewardId());
			entity.setCustomerId(UserUtils.getUser().getId());
			entity.setStartTime(carryWaterVouchersBean.getLogicStartTime());
			entity.setEndTime(carryWaterVouchersBean.getLogicEndTime());
			entity.setQuantity(num.longValue());
			entity.setCreateUser(UserUtils.getUser().getId());
			entity.setOrderId(null);
			carryWaterVouchersCustomerDaoImpl.insert(entity);
			returnDataUtil.setMessage("恭喜您抽中" + prize.getAmount() + "张提水券，请前往我的信息→我的提水券 查看！");
			returnDataUtil.setReturnObject(gameDto);
		} else {
			returnDataUtil.setMessage("谢谢惠顾");
			returnDataUtil.setReturnObject(gameDto);
		}
		log.info("<SendCarryWaterProcessor>-----<send>-----end");
		return returnDataUtil;
	}
}
