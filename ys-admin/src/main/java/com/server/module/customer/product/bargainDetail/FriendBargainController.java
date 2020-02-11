package com.server.module.customer.product.bargainDetail;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.app.home.ResultEnum;
import com.server.module.customer.CustomerUtil;
import com.server.module.customer.product.customerBargain.BargainOrderDto;
import com.server.module.customer.product.customerBargain.CustomerBargainDto;
import com.server.module.customer.product.customerBargain.CustomerBargainService;
import com.server.module.sys.model.User;
import com.server.redis.RedisClient;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
@RestController
@RequestMapping("/bargain")
public class FriendBargainController {
	
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private CustomerBargainService customerBargainService;
	@Autowired
	private BargainDetailService bargainDetailService;
	
	private Integer cutTimesLimit = 3;

	/**
	 * 砍价
	 * @author hebiting
	 * @date 2018年12月25日上午9:48:53
	 * @param customerBargainId
	 * @return
	 */
	@PostMapping("/do/{customerBargainId}")
	public ReturnDataUtil bargain(@PathVariable Long customerBargainId){
		User user = CustomerUtil.getUser();
		if(user == null){
			return ResultUtil.error(ResultEnum.NOT_LOGIN);
		}
		//判断该用户砍价商品是否存在
		BargainDto barginDto = bargainDetailService.getBarginSomeInfo(customerBargainId);
		if(barginDto == null){
			return ResultUtil.error(ResultEnum.BARGAIN_NOT_FOUNT);
		}
		String times = redisClient.get("bargain"+user.getId());
		Integer doneTimes = 0;
		if(StringUtils.isNotBlank(times)){
			doneTimes = Integer.valueOf(times);
		}
		//判断是否超过次数限制
		if(doneTimes>=cutTimesLimit){
			return ResultUtil.error(ResultEnum.BARGAIN_TIMES_NOT_ENOUGH);
		}
		//判断该订单是否超时
		if(!customerBargainService.isBargaining(customerBargainId)){
			return ResultUtil.error(ResultEnum.BARGAIN_END);
		}
		//判断该用户是否已经砍价成功
		if(bargainDetailService.isBargained(customerBargainId, user.getId())){
			return ResultUtil.error(ResultEnum.BARGAINED);
		}
		BigDecimal cutPrice = customerBargainService.cutPirce(customerBargainId,user,barginDto,doneTimes);
		if(cutPrice != null){
			return ResultUtil.success(cutPrice);
		}
		return ResultUtil.error(ResultEnum.BARGAIN_FAIL);
	}
	
	/**
	 * 获取分享商品的砍价信息
	 * @author hebiting
	 * @date 2018年12月24日下午6:50:09
	 * @param customerBargainId
	 * @return
	 */
	@PostMapping("/getBargainInfo/{customerBargainId}")
	public ReturnDataUtil getBarginInfo(@PathVariable Long customerBargainId){
		User user = CustomerUtil.getUser();
		boolean isBargained = false;
		if(user != null){
			isBargained = bargainDetailService.isBargained(customerBargainId, user.getId());
		}
		//获取砍价信息
		CustomerBargainDto bargainsInfo = customerBargainService.getBargainsInfo(customerBargainId);
		if(bargainsInfo != null){
			if(isBargained){
				return ResultUtil.success(ResultEnum.BARGAINED,bargainsInfo);
			}
			return ResultUtil.success(bargainsInfo);
		}
		return ResultUtil.error();
	}
	
	/**
	 * 获取砍价订单信息
	 * @author hebiting
	 * @date 2018年12月25日上午9:35:50
	 * @param orderId
	 * @return
	 */
	@PostMapping("/bargainOrder/{orderId}")
	public ReturnDataUtil findBargainOrderInfo(@PathVariable Long orderId){
		BargainOrderDto bargainOrderInfo = customerBargainService.getBargainOrderInfo(orderId);
		if(bargainOrderInfo != null){
			return ResultUtil.success(bargainOrderInfo);
		}
		return ResultUtil.error();
	}
}
