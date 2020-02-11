package com.server.module.system.game;

import java.util.List;

import com.server.module.system.itemManage.itemBasic.ItemBasicBean;
import com.server.module.system.machineManage.machineList.MachinesLAC;
import com.server.util.ReturnDataUtil;


public interface GameService {


	/**
	 * 新增游戏
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:05
	 * @param game
	 * @return
	 */
	Integer insertGame(GameBean game);
	
	/**
	 * 新增游戏奖品
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:12
	 * @param gamePrize
	 * @return
	 */
	Long insertGamePrize(GamePrizeBean gamePrize);
	
	/**
	 * 用户奖品分发
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:28
	 * @param gamePrizeReceive
	 * @return
	 */
	Long insertGameReceive(GamePrizeReceiveBean gamePrizeReceive);
	
	/**
	 * 更新游戏信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:45
	 * @param game
	 * @return
	 */
	boolean updateGame(GameBean game);
	
	/**
	 * 更新奖品信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:49:53
	 * @param gamePrize
	 * @return
	 */
	boolean updateGamePrize(GamePrizeBean gamePrize);
	/**
	 * 更新领取信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:50:01
	 * @param gamePrizeReceive
	 * @return
	 */
	boolean updateGameReceive(GamePrizeReceiveBean gamePrizeReceive);
	
	/**
	 * 获取游戏所有奖品
	 * @author hebiting
	 * @date 2018年8月20日下午2:53:52
	 * @param gameId
	 * @return
	 */
	List<GamePrizeBean> getGamePrize(Integer gameId);
	
	/**
	 * 获取游戏信息
	 * @author hebiting
	 * @date 2018年8月21日上午9:21:59
	 * @param gameId
	 * @return
	 */
	GameBean getGame(Integer gameId);
	
	/**
	 * 更新奖品剩余数量
	 * @author hebiting
	 * @date 2018年8月21日下午3:05:39
	 * @param gamePrize
	 * @return
	 */
	boolean updatePrizeTotal(GamePrizeBean gamePrize);
	
	/**
	 * 获取游戏信息
	 * @author hebiting
	 * @date 2018年8月21日上午9:21:59
	 * @param gameId
	 * @return
	 */
	GamePrizeReceiveBean getGamePrizeReceive(Integer prizeReceiveId);
	
	/**
	 * 获取奖品信息
	 * @author hebiting
	 * @date 2018年8月20日下午2:53:52
	 * @param gamePrizeId
	 * @return
	 */
	GamePrizeBean getPrizeById(Long gamePrizeId);
	
	
	/**
	 * 获取游戏信息  分页
	 * @author hebiting
	 * @date 2018年8月21日上午9:21:59
	 * @param gameId
	 * @return
	 */
	List<GameBean> getGameByForm(GameForm gameForm);
	
	/**
	 * 获取总数
	 * @author hebiting
	 * @date 2018年8月27日下午5:37:52
	 * @param gameForm
	 * @return
	 */
	Long getGameNumByForm(GameForm gameForm);
	
	/**
	 * 获取可赠送商品
	 * @author hebiting
	 * @date 2018年8月30日上午11:38:49
	 * @return
	 */
	List<GamePrizeDto> getGameGift(PrizeForm form);
	
	/**
	 * 根据条件获取中奖信息
	 * @author hebiting
	 * @date 2018年9月3日上午8:42:48
	 * @param form
	 * @return
	 */
	List<ReceiveDto> getReceiveByForm(PrizeReceiveForm form);

	/**
	 * 设置奖品
	 * @param rewardId
	 * @param id
	 * @param goodsName
	 * @return 
	 */
	public boolean setGoodsToGame(GamePrizeBean gamePrize);
	
	/**
	 * 查询商品
	 * @param GamePrizeForm gamePrizeForm
	 * @return 
	 */
	public ReturnDataUtil queryPrizeDetail(GamePrizeForm gamePrizeForm);

	
	/**
	 * 获取用户奖品信息
	 * @author why
	 * @date 2019年3月5日 上午9:18:47 
	 * @param customerId
	 * @return
	 */
	List<PrizeReceiveDto> getGamePrizeReceive(Long customerId);
	
	/**
	 * 获取可玩游戏
	 * @author hebiting
	 * @date 2018年8月30日上午8:56:40
	 * @param machinesLAC
	 * @return 仅返回游戏id 游戏名称name
	 */
	List<GameBean> getAvailableGame(MachinesLAC machinesLAC);

}
