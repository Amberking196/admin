package com.server.module.system.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lly835.bestpay.utils.JsonUtil;
import com.server.module.app.home.ResultEnum;
import com.server.module.customer.CustomerUtil;
import com.server.module.customer.coupon.CouponDto;
import com.server.module.customer.coupon.CouponForm;
import com.server.module.customer.coupon.CouponService;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.game.send.SendPrizeProcessor;
import com.server.module.system.game.send.SendPrizeProcessorFactory;
import com.server.module.system.machineManage.machineList.MachinesLAC;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.memberManage.memberManager.MemberService;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.redis.RedisClient;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.GamePrizeTypeEnum;

@RestController
@RequestMapping("/game")
public class GameController {

	private final static Logger log = LogManager.getLogger(GameController.class);
	
	@Autowired
	private GameService gameService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private SendPrizeProcessorFactory sendPrizeProcessorFactory;
	@Autowired
	private CustomerService customerServiceImpl;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoServiceImpl;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private MemberService  memberServiceImpl;
	
	
	/**
	 * 新增游戏
	 * @author hebiting
	 * @date 2018年8月20日下午5:08:15
	 * @param game
	 * @return
	 */
	@PostMapping("/createGame")
	public ReturnDataUtil createGame(@RequestBody GameBean game,HttpServletRequest request){
		log.info("<GameController--createGame--start>");
		if(game.getTarget().equals(Integer.valueOf(3))){
			VendingMachinesInfoBean machinesInfo = vendingMachinesInfoService.findVendingMachinesByCode(game.getVmCode());
			if(machinesInfo==null){
				return ResultUtil.error(0,"机器编码有误",null);
			}
		}
		Long userId = Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		game.setCreateUser(userId);
		game.setUpdateUser(userId);
		Integer insertGame = gameService.insertGame(game);
		if(insertGame>0){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	/**
	 * 修改游戏
	 * @author hebiting
	 * @date 2018年8月20日下午5:08:15
	 * @param game
	 * @return
	 */
	@PostMapping("/updateGame")
	public ReturnDataUtil updateGame(@RequestBody GameBean game,HttpServletRequest request){
		log.info("<GameController--createGame--start>");
		if(game.getTarget().equals(Integer.valueOf(3))){
			VendingMachinesInfoBean machinesInfo = vendingMachinesInfoService.findVendingMachinesByCode(game.getVmCode());
			if(machinesInfo==null){
				return ResultUtil.error(0,"机器编码有误",null);
			}
		}
		Long userId = Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		game.setUpdateUser(userId);
		boolean updateGame = gameService.updateGame(game);
		if(updateGame){
			if(game.getDeleteFlag() == 1){
				redisClient.del("game"+game.getId());
			}
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	/**
	 * 增加游戏奖品
	 * @author hebiting
	 * @date 2018年8月20日下午5:08:21
	 * @param gamePrize
	 * @return
	 */
	@PostMapping("/addPrize")
	public ReturnDataUtil addGamePrize(@RequestBody GamePrizeBean gamePrize){
		log.info("<GameController--addGamePrize--start>");
		gamePrize.setDefaultFlag(0);
		Long insertGamePrize = gameService.insertGamePrize(gamePrize);
		if(insertGamePrize>0){
			List<GamePrizeBean> prizeList = gameService.getGamePrize(gamePrize.getGameId());
			String json = JsonUtil.toJson(prizeList);
			redisClient.set("game"+gamePrize.getId(), json);
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	/**
	 * 修改游戏奖品信息
	 * @author hebiting
	 * @date 2018年8月20日下午5:08:21
	 * @param gamePrize
	 * @return
	 */
	@PostMapping("/updatePrize")
	public ReturnDataUtil updateGamePrize(@RequestBody GamePrizeBean gamePrize){
		log.info("<GameController--addGamePrize--start>");
		gamePrize.setDefaultFlag(0);
		GamePrizeBean gpb=gameService.getPrizeById(gamePrize.getId());
		if(gpb.getType().equals(gamePrize.getType())) {
			gamePrize.setGoodsName(gpb.getGoodsName());
		}
		boolean updateGamePrize = gameService.updateGamePrize(gamePrize);
		
		if(updateGamePrize){
			List<GamePrizeBean> prizeList = gameService.getGamePrize(gamePrize.getGameId());
			String json = JsonUtil.toJson(prizeList);
			redisClient.set("game"+gamePrize.getGameId(), json);
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	/**
	 * 获取游戏
	 * @author hebiting
	 * @date 2018年8月27日上午9:05:37
	 * @param gameForm
	 * @return
	 */
	@PostMapping("/getGame")
	public ReturnDataUtil getGame(@RequestBody(required=false) GameForm gameForm){
		log.info("<GameController--getGame--start>");
		if(gameForm == null){
			gameForm = new GameForm();
		}
		List<GameBean> gameList = gameService.getGameByForm(gameForm);
		Long total = gameService.getGameNumByForm(gameForm);
		if(gameList!=null && gameList.size()>0){
			return ResultUtil.success(gameList,gameForm.getCurrentPage(),total);
		}
		return ResultUtil.selectError(new ArrayList<GameBean>());
	}
	
	/**
	 * 获取游戏奖品内容
	 * @author hebiting
	 * @date 2018年8月20日下午5:10:32
	 * @param gameId
	 * @return
	 */
	@PostMapping("/getGamePrize")
	public ReturnDataUtil getGamePrize(Integer gameId){
		log.info("<GameController--getGamePrize--start>");
		List<GamePrizeBean> gamePrize = gameService.getGamePrize(gameId);
		if(gamePrize!=null && gamePrize.size()>0){
			return ResultUtil.success(gamePrize);
		}
		return ResultUtil.selectError(new ArrayList<GamePrizeBean>());	
	}
	
	/**
	 * 获取游戏可赠送优惠券
	 * @author hebiting
	 * @date 2018年8月24日上午10:02:44
	 * @return
	 */
	@PostMapping("/getGameCoupon")
	public ReturnDataUtil getGameCoupon(@RequestBody CouponForm couponForm){
		log.info("<GameController--getGameCoupon--start>");
		List<CouponDto> gameCoupon = couponService.getGameCoupon(couponForm);
		if(gameCoupon!=null && gameCoupon.size()>0){
			return ResultUtil.success(gameCoupon);
		}
		return ResultUtil.selectError(new ArrayList<CouponDto>());	
	}
	
	/**
	 * 获取可赠送游戏商品(废弃)
	 * @author hebiting
	 * @date 2018年8月30日上午11:30:27
	 * @return
	 */
	@PostMapping("/getGameGift")
	public ReturnDataUtil getGameGift(@RequestBody PrizeForm form){
		log.info("<GameController--getGameGift--start>");
		List<GamePrizeDto> gameGift = gameService.getGameGift(form);
		if(gameGift!=null && gameGift.size()>0){
			return ResultUtil.success(gameGift);
		}
		return ResultUtil.selectError();
	}
	/**
	 * 获取领奖信息
	 * @author hebiting
	 * @date 2018年9月1日下午5:23:07
	 * @param form
	 * @return
	 */
	@PostMapping("/getReceiveByForm")
	public ReturnDataUtil getReceiveByForm(@RequestBody(required=false) PrizeReceiveForm form){
		log.info("<GameController--getReceiveByForm--start>");
		if(form == null){
			form = new PrizeReceiveForm();
		}
		List<ReceiveDto> receiveByForm = gameService.getReceiveByForm(form);
		if(receiveByForm!=null && receiveByForm.size()>0){
			return ResultUtil.success(receiveByForm);
		}
		return ResultUtil.selectError();
	}

	/**
	 * 绑定具体的商品
	 * @param 奖品id,提水券/优惠券/商城商品rewardId,商品名称goodsName
	 */
	@PostMapping("/setGoodsToGame")
	public ReturnDataUtil setGoodsToGame(@RequestBody GamePrizeBean gamePrize){
		log.info("<GameController>-----<setGoodsToGame>----start");
		ReturnDataUtil returnDataUtil= new ReturnDataUtil(gameService.setGoodsToGame(gamePrize));
		log.info("<GameController>-----<setGoodsToGame>----end");
		return returnDataUtil;
	}
	

	/**
	 * 根据奖品类型查询具体奖品信息 
	 * @param 游戏id,奖品类型type
	 */
	@PostMapping("/queryPrizeDetail")
	public ReturnDataUtil queryPrizeDetail(@RequestBody(required=false) GamePrizeForm gamePrizeForm){
		log.info("<GameController>-----<queryPrizeDetail>----start");
		ReturnDataUtil returnDataUtil= gameService.queryPrizeDetail(gamePrizeForm);
		log.info("<GameController>-----<setGoodsToGame>----end");
		return returnDataUtil;
	}
	

	/**
	 * 查询用户奖品
	 * @author why
	 * @date 2019年3月5日 上午9:16:32 
	 * @return
	 */
	@PostMapping("/getCusPrize")
	public ReturnDataUtil getCusPrize(){
		log.info("<GameController>-----<getCusPrize>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		Long customerId = CustomerUtil.getCustomerId();
		if(customerId==null) {
			customerId=userUtils.getSmsUser().getId();
		}
		List<PrizeReceiveDto> gamePrizeReceive = gameService.getGamePrizeReceive(customerId);
		if(gamePrizeReceive!=null && gamePrizeReceive.size()>0){
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查询成功！");
			returnDataUtil.setReturnObject(gamePrizeReceive);
			log.info("<GameController>-----<getCusPrize>----end");
			return returnDataUtil;
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查询无数据！");
			returnDataUtil.setReturnObject(gamePrizeReceive);
			log.info("<GameController>-----<getCusPrize>----end");
			return returnDataUtil;
		}
	}
	
	/**
	 * 抽奖
	 * 
	 * @author hebiting
	 * @date 2018年8月20日下午5:32:15
	 * @param gameId
	 */
	@PostMapping("/lottery")
	public ReturnDataUtil lottery(Integer gameId) {
		log.info("<GameController>-----<lottery>----start");
		User user = userUtils.getSmsUser();
		GameBean game = gameService.getGame(gameId);
		Integer totalTimes = game.getTimes();
		// 获取用户的抽奖次数
		Integer times = redisClient.get("game" + gameId + "customer" + user.getId(), Integer.class);
		log.info("抽奖次数====" + times);
		boolean canDo = false;
		// 判断游戏 是否购买后可参与(0:不需要 1：购买后参与)
		if (game.getNeedGo() == 1) {
			if (redisClient.exists("orderUserId" + user.getId())) {
				canDo = true;
				redisClient.del("orderUserId" + user.getId());
			}
		} else {
			canDo = true;
		}
		log.info("canDo===="+canDo);
		if (canDo && (times == null || times < totalTimes)) {
			if (times == null) {
				times = 0;
			}
			DateTime today = new DateTime();
			DateTime tommrow = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 23, 59, 59);
			int expires = tommrow.getSecondOfDay() - today.getSecondOfDay();
			// 抽奖次数 写入redis
			redisClient.set("game" + gameId + "customer" + user.getId(), Integer.valueOf(++times), expires);
			// 获取游戏所有奖品
			List<GamePrizeBean> gamePrize = gameService.getGamePrize(gameId);
			// 获取奖励
			GamePrizeBean prize = getPrize(gamePrize);
			boolean updatePrizeTotal = false;
			// 谢谢惠顾 1 0 普通
			if (prize.getDefaultFlag() == 1) {
				updatePrizeTotal = true;
			} else {
				if (prize.getTotal() > 0) {
					updatePrizeTotal = gameService.updatePrizeTotal(prize);
				}
			}
			// 如果 奖品抽完了 默认谢谢惠顾
			if (!updatePrizeTotal) {
				List<GamePrizeBean> collect = gamePrize.stream().filter(prizes -> prizes.getDefaultFlag() == 1)
						.collect(Collectors.toList());
				if (collect != null && collect.size() > 0) {
					prize = collect.get(0);
				} else {
					return ResultUtil.error(ResultEnum.ILLEGAL_STATE, null);
				}
			}
			GamePrizeReceiveBean gpr = new GamePrizeReceiveBean();
			DateTime now = new DateTime();
			gpr.setCreateTime(now.toDate());
			gpr.setCustomerId(user.getId());
			gpr.setGamePrizeId(prize.getId());
			Integer canReceive = game.getCanReceive();
			gpr.setEndTime(now.plusDays(canReceive).toDate());
			// 奖品绑定用户
			Long insertGameReceive = gameService.insertGameReceive(gpr);
			if (insertGameReceive != null && insertGameReceive > 0) {
				GameDto gameDto = new GameDto();
				int indexOf = gamePrize.indexOf(prize);
				int time = totalTimes - times;
				gameDto.setId(insertGameReceive);
				gameDto.setIndexOf(indexOf);
				gameDto.setTimes(time);
				log.info("结果======" + indexOf);
				SendPrizeProcessor sendPrizeProcessor = sendPrizeProcessorFactory
						.getSendPrizeProcessor(GamePrizeTypeEnum.getType(prize.getType()));
				if (sendPrizeProcessor != null) {
					ReturnDataUtil returnDataUtil = sendPrizeProcessor.send(prize, gameDto);
					if(returnDataUtil.getStatus()==1) {
						memberServiceImpl.updateIntegral(user.getId(), game.getIntegral().longValue());
						CustomerBean bean = customerServiceImpl.findCustomerById(user.getId());
						gameDto.setIntegral(bean.getIntegral());
						returnDataUtil.setReturnObject(gameDto);
					}
					return returnDataUtil;
				}
			} else {
				return ResultUtil.error(ResultEnum.ADD_FAILED, null);
			}
		}
		return ResultUtil.error(ResultEnum.GAME_TIMES_NOT_ENOUGH, null);
	}

	// 获取奖励
	private GamePrizeBean getPrize(List<GamePrizeBean> gamePrize) {
		log.info("<GameController>-----<getPrize>----start");
		Random random = new Random();
		Integer total = 0;
		for (int i = 0; i < gamePrize.size(); i++) {
			total += gamePrize.get(i).getWeight();
		}
		int nextInt = random.nextInt(total);
		log.info("奖励随机号" + nextInt);
		for (int i = 0; i < gamePrize.size(); i++) {
			if (nextInt < gamePrize.get(i).getWeight()) {
				log.info("奖品名称:" + JsonUtil.toJson(gamePrize.get(i)));
				return gamePrize.get(i);
			} else {
				nextInt -= gamePrize.get(i).getWeight();
			}
		}
		return null;
	}

	@PostMapping("/updateAddress")
	public ReturnDataUtil updateAddress(@RequestBody GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameController>-----<updateAddress>----start");
		boolean updateGameReceive = gameService.updateGameReceive(gamePrizeReceive);
		if (updateGameReceive) {
			log.info("<GameController>-----<updateAddress>----end");
			return ResultUtil.success(updateGameReceive);
		}
		log.info("<GameController>-----<updateAddress>----end");
		return ResultUtil.error();
	}

	/**
	 * 根据vmCode获取可玩游戏
	 * 
	 * @author hebiting
	 * @date 2018年8月30日上午8:46:59
	 * @param vmCode
	 * @return
	 */
	@PostMapping("/getAvailableGame")
	public ReturnDataUtil getAvailableGame(String vmCode) {
		log.info("<GameController>-----<getAvailableGame>----start");
		Map<String, Object> resultData = new HashMap<String, Object>();
		// 根据机器编码获取该机器线路，区域，公司归属
		MachinesLAC machinesLAC = vendingMachinesInfoServiceImpl.getMachinesLAC(vmCode);
		// 获取机器可玩游戏
		List<GameBean> gameList = gameService.getAvailableGame(machinesLAC);
		if (gameList != null && gameList.size() > 0) {
			GameBean minGame = Collections.max(gameList, new Comparator<GameBean>() {
				@Override
				public int compare(GameBean a, GameBean b) {
					return a.getTarget().compareTo(b.getTarget());
				}
			});
			// 获取游戏所有奖品
			List<GamePrizeBean> gamePrize = gameService.getGamePrize(minGame.getId());
			// 获取用户的抽奖次数
			Integer times = redisClient.get("game" + minGame.getId() + "customer" + userUtils.getSmsUser().getId(),
					Integer.class);
			if ((times != null && times < minGame.getTimes())) {
				minGame.setTimes(minGame.getTimes() - times);
			}
			CustomerBean bean = customerServiceImpl.findCustomerById(userUtils.getSmsUser().getId());
			resultData.put("integral", bean.getIntegral());
			resultData.put("game", minGame);
			resultData.put("gamePrize", gamePrize);
			log.info("<GameController>-----<getAvailableGame>----end");
			return ResultUtil.success(resultData);
		}
		log.info("<GameController>-----<getAvailableGame>----end");
		return ResultUtil.selectError();
	}
	
	@PostMapping("/raffle")
	public ReturnDataUtil raffle(String vmCode) {
		log.info("<GameController>-----<raffle>----start");
		// 根据机器编码获取该机器线路，区域，公司归属
		MachinesLAC machinesLAC = vendingMachinesInfoServiceImpl.getMachinesLAC(vmCode);
		// 获取机器可玩游戏
		List<GameBean> gameList = gameService.getAvailableGame(machinesLAC);
		if (gameList != null && gameList.size() > 0) {
			GameBean minGame = Collections.max(gameList, new Comparator<GameBean>() {
				@Override
				public int compare(GameBean a, GameBean b) {
					return a.getTarget().compareTo(b.getTarget());
				}
			});
			//获取游戏所需积分
			Integer integral = minGame.getIntegral();
			CustomerBean bean = customerServiceImpl.findCustomerById(userUtils.getSmsUser().getId());
			if(bean.getIntegral()!=null && bean.getIntegral()>=integral) {
				return ResultUtil.success(ResultEnum.GAME_SUCCESS,null);
			}
		}
		log.info("<GameController>-----<raffle>----end");
		return ResultUtil.selectError();
	}

}
