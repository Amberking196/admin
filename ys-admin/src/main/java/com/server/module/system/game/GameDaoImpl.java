package com.server.module.system.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machineList.MachinesLAC;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.GamePrizeTypeEnum;

@Repository
public class GameDaoImpl extends MySqlFuns implements GameDao{

	private final static Logger log = LogManager.getLogger(GameDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	
	@Override
	public Integer insertGame(GameBean game) {
		log.info("<GameDaoImpl--insertGame--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		Date date = new Date();
		sql.append(" INSERT INTO game(`name`,times,target,companyId,areaId,vmCode,needGo,`type`,`startTime`,`endTime`,canReceive,createUser,createTime,updateUser,updateTime,prizeNum,integral)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		param.add(game.getName());
		param.add(game.getTimes());
		param.add(game.getTarget());
		param.add(game.getCompanyId());
		param.add(game.getAreaId());
		param.add(game.getVmCode());
		param.add(game.getNeedGo());
		param.add(game.getType());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getEndTime()));
		param.add(game.getCanReceive());
		param.add(game.getCreateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(date));
		param.add(game.getUpdateUser());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(date));
		param.add(game.getPrizeNum());
		param.add(game.getIntegral());
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<GameDaoImpl--insertGame--end>");
		return insertGetID;
	}

	@Override
	public Long insertGamePrize(GamePrizeBean gamePrize) {
		log.info("<GameDaoImpl--insertGamePrize--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO game_prize(`name`,`type`,`weight`,total,amount,rewardId,gameId,deleteFlag,defaultFlag)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?)");
		param.add(gamePrize.getName());
		param.add(gamePrize.getType());
		param.add(gamePrize.getWeight());
		param.add(gamePrize.getTotal());
		param.add(gamePrize.getAmount());
		param.add(gamePrize.getRewardId());
		param.add(gamePrize.getGameId());
		param.add(gamePrize.getDeleteFlag());
		param.add(gamePrize.getDefaultFlag());
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<GameDaoImpl--insertGamePrize--end>");
		return (long)insertGetID;
	}

	@Override
	public Long insertGameReceive(GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameDaoImpl--insertGameReceive--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO game_prize_receive(gamePrizeId,customerId,createTime,endTime,receiveTime,addressId)");
		sql.append(" VALUES(?,?,?,?,?,?)");
		param.add(gamePrizeReceive.getGamePrizeId());
		param.add(gamePrizeReceive.getCustomerId());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getCreateTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getEndTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getReceiveTime()));
		param.add(gamePrizeReceive.getAddressId());
		int insertGetID = insertGetID(sql.toString(), param);
		log.info("<GameDaoImpl--insertGameReceive--end>");
		return (long)insertGetID;
	}

	@Override
	public boolean updateGame(GameBean game) {
		log.info("<GameDaoImpl--updateGame--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		Date date = new Date();
		sql.append(" UPDATE game SET target=?,companyId=?,areaId=?,vmCode=?,needGo=?,times=?,`name`=?,`type`=?,startTime=?,endTime=?,canReceive=?,deleteFlag=?,updateTime=?,updateUser=?,integral=?");
		sql.append(" WHERE id = ?");
		param.add(game.getTarget());
		param.add(game.getCompanyId());
		param.add(game.getAreaId());
		param.add(game.getVmCode());
		param.add(game.getNeedGo());
		param.add(game.getTimes());
		param.add(game.getName());
		param.add(game.getType());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(game.getEndTime()));
		param.add(game.getCanReceive());
		param.add(game.getDeleteFlag());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(date));
		param.add(game.getUpdateUser());
		param.add(game.getIntegral());
		param.add(game.getId());
		int upate = upate(sql.toString(), param);
		log.info("<GameDaoImpl--updateGame--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateGamePrize(GamePrizeBean gamePrize) {
		log.info("<GameDaoImpl--updateGamePrize--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE game_prize SET `name`=?,`type`=?,weight=?,total=?,amount=?,rewardId=?,gameId=?,deleteFlag=?,defaultFlag=?,goodsName=?");
		sql.append(" WHERE id = ?");
		param.add(gamePrize.getName());
		param.add(gamePrize.getType());
		param.add(gamePrize.getWeight());
		param.add(gamePrize.getTotal());
		param.add(gamePrize.getAmount());
		param.add(gamePrize.getRewardId());
		param.add(gamePrize.getGameId());
		param.add(gamePrize.getDeleteFlag());
		param.add(gamePrize.getDefaultFlag());
		param.add(gamePrize.getGoodsName());
		param.add(gamePrize.getId());
		int upate = upate(sql.toString(), param);
		log.info("<GameDaoImpl--updateGamePrize--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateGameReceive(GamePrizeReceiveBean gamePrizeReceive) {
		log.info("<GameDaoImpl--updateGameReceive--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE game_prize_receive SET receiveTime = ?,addressId=? WHERE id = ?");
		param.add(DateUtil.formatYYYYMMDDHHMMSS(gamePrizeReceive.getReceiveTime()));
		param.add(gamePrizeReceive.getAddressId());
		param.add(gamePrizeReceive.getId());
		int upate = upate(sql.toString(),param);
		log.info("<GameDaoImpl--updateGameReceive--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public List<GamePrizeBean> getGamePrize(Integer gameId) {
		log.info("<GameDaoImpl--getGamePrize--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT g.id,g.name,g.type,g.weight,g.total,g.amount,g.rewardId,g.gameId,g.deleteFlag,g.defaultFlag,g.goodsName as goodsName FROM game_prize g  WHERE g.deleteFlag = 0 AND g.gameId = "+gameId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamePrizeBean> prizeList = new ArrayList<GamePrizeBean>();
		GamePrizeBean gamePrize = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				gamePrize = new GamePrizeBean();
				gamePrize.setAmount(rs.getInt("amount"));
				gamePrize.setDeleteFlag(rs.getInt("deleteFlag"));
				gamePrize.setGameId(rs.getInt("gameId"));
				gamePrize.setId(rs.getLong("id"));
				gamePrize.setName(rs.getString("name"));
				gamePrize.setTotal(rs.getInt("total"));
				gamePrize.setRewardId(rs.getLong("rewardId"));
				gamePrize.setType(rs.getInt("type"));
				gamePrize.setWeight(rs.getInt("weight"));
				gamePrize.setDefaultFlag(rs.getInt("defaultFlag"));
				gamePrize.setGoodsName(rs.getString("goodsName"));
				prizeList.add(gamePrize);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGamePrize--end>");
		return prizeList;
	}

	@Override
	public GameBean getGame(Integer gameId) {
		log.info("<GameDaoImpl--getGame--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,target,companyId,areaId,vmCode,needGo,times,`name`,`type`,startTime,endTime,canReceive,createUser,createTime,deleteFlag,updateUser,updateTime,integral");
		sql.append(" FROM game WHERE deleteFlag = 0 AND id = "+gameId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GameBean game = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				game = new GameBean();
				game.setAreaId(rs.getInt("areaId"));
				game.setTarget(rs.getInt("target"));
				game.setCompanyId(rs.getInt("companyId"));
				game.setVmCode(rs.getString("vmCode"));
				game.setNeedGo(rs.getInt("needGo"));
				game.setTimes(rs.getInt("times"));
				game.setCanReceive(rs.getInt("canReceive"));
				game.setCreateTime(rs.getTimestamp("createTime"));
				game.setCreateUser(rs.getLong("createUser"));
				game.setDeleteFlag(rs.getInt("deleteFlag"));
				game.setEndTime(rs.getTimestamp("endTime"));
				game.setId(rs.getInt("id"));
				game.setName(rs.getString("name"));
				game.setStartTime(rs.getTimestamp("startTime"));
				game.setType(rs.getInt("type"));
				game.setUpdateTime(rs.getTimestamp("updateTime"));
				game.setUpdateUser(rs.getLong("updateUser"));
				game.setIntegral(rs.getInt("integral"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGame--end>");
		return game;
	}

	@Override
	public boolean updatePrizeTotal(GamePrizeBean gamePrize) {
		log.info("<GameDaoImpl--updatePrizeTotal--start>");
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE game_prize SET total = total - amount");
		sql.append(" WHERE total>amount and id = "+gamePrize.getId());
		int upate = upate(sql.toString());
		log.info("<GameDaoImpl--updatePrizeTotal--end>");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public GamePrizeReceiveBean getGamePrizeReceive(Integer prizeReceiveId) {
		log.info("<GameDaoImpl--getGamePrizeReceive--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,gamePrizeId,customerId,createTime,endTime,receiveTime,addressId FROM game_prize_receive WHERE id = "+prizeReceiveId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GamePrizeReceiveBean prizeReceive = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				prizeReceive = new GamePrizeReceiveBean();
				prizeReceive.setCreateTime(rs.getTimestamp("createTime"));
				prizeReceive.setCustomerId(rs.getLong("customerId"));
				prizeReceive.setEndTime(rs.getTimestamp("endTime"));
				prizeReceive.setGamePrizeId(rs.getLong("gamePrizeId"));
				prizeReceive.setId(rs.getLong("id"));
				prizeReceive.setReceiveTime(rs.getTimestamp("receiveTime"));
				prizeReceive.setAddressId(rs.getLong("addressId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGamePrizeReceive--end>");
		return prizeReceive;
	}

	@Override
	public GamePrizeBean getPrizeById(Long gamePrizeId) {
		log.info("<GameDaoImpl--getPrizeById--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,`name`,`type`,weight,total,amount,rewardId,gameId,deleteFlag,defaultFlag,goodsName FROM game_prize WHERE deleteFlag = 0 AND id = "+gamePrizeId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GamePrizeBean gamePrize = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				gamePrize = new GamePrizeBean();
				gamePrize.setAmount(rs.getInt("amount"));
				gamePrize.setDeleteFlag(rs.getInt("deleteFlag"));
				gamePrize.setGameId(rs.getInt("gameId"));
				gamePrize.setId(rs.getLong("id"));
				gamePrize.setName(rs.getString("name"));
				gamePrize.setTotal(rs.getInt("total"));
				gamePrize.setRewardId(rs.getLong("rewardId"));
				gamePrize.setType(rs.getInt("type"));
				gamePrize.setWeight(rs.getInt("weight"));
				gamePrize.setDefaultFlag(rs.getInt("defaultFlag"));
				gamePrize.setGoodsName(rs.getString("goodsName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getPrizeById--end>");
		return gamePrize;
	}


	@Override
	public List<GameBean> getGameByForm(GameForm gameForm) {
		log.info("<GameDaoImpl--getGameByForm--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT g.id,g.target,g.companyId,g.areaId,g.vmCode,g.needGo,g.times,g.`name`,g.`type`,g.startTime,");
		sql.append(" g.endTime,g.canReceive,g.createUser,g.createTime,g.deleteFlag,g.updateUser,g.updateTime,g.prizeNum,");
		sql.append(" va.`name` AS areaName , c.`name` AS companyName ,g.integral ");
		sql.append(" FROM game AS g LEFT JOIN company AS c ON c.`id` = g.`companyId`");
		sql.append(" LEFT JOIN vending_area AS va ON g.`areaId` = va.`id` WHERE 1=1 AND g.deleteFlag = 0");
		if(StringUtil.isNotBlank(gameForm.getName())){
			sql.append(" AND  g.`name` like '%"+gameForm.getName()+"%'");
		}
		//权限控制
		sql.append(" and (g.target = 1 and g.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or g.target = 2 and g.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or g.target = 3 and g.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
					
		sql.append(" ORDER BY createTime DESC ");
		if(gameForm.getIsShowAll() == 0){
			sql.append(" LIMIT "+(gameForm.getCurrentPage()-1)*gameForm.getPageSize() +","+gameForm.getPageSize());
		}
		
		log.info("游戏查询sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GameBean> gameList = new ArrayList<GameBean>();
		GameBean game = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				game = new GameBean();
				game.setAreaId(rs.getInt("areaId"));
				game.setTarget(rs.getInt("target"));
				game.setCompanyId(rs.getInt("companyId"));
				game.setVmCode(rs.getString("vmCode"));
				game.setNeedGo(rs.getInt("needGo"));
				game.setTimes(rs.getInt("times"));
				game.setCanReceive(rs.getInt("canReceive"));
				game.setCreateTime(rs.getTimestamp("createTime"));
				game.setCreateUser(rs.getLong("createUser"));
				game.setDeleteFlag(rs.getInt("deleteFlag"));
				game.setEndTime(rs.getTimestamp("endTime"));
				game.setId(rs.getInt("id"));
				game.setName(rs.getString("name"));
				game.setStartTime(rs.getTimestamp("startTime"));
				game.setType(rs.getInt("type"));
				game.setUpdateTime(rs.getTimestamp("updateTime"));
				game.setUpdateUser(rs.getLong("updateUser"));
				game.setCompanyName(rs.getString("companyName"));
				game.setAreaName(rs.getString("areaName"));
				game.setPrizeNum(rs.getInt("prizeNum"));
				game.setIntegral(rs.getInt("integral"));
				gameList.add(game);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGameByForm--end>");
		return gameList;
	}
	
	public Long getGameNumByForm(GameForm gameForm){
		log.info("<GameDaoImpl--getGameNumByForm--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(1) AS total FROM game WHERE 1=1 AND deleteFlag = 0");
		if(StringUtil.isNotBlank(gameForm.getName())){
			sql.append(" AND  `name` like '%"+gameForm.getName()+"%'");
		}
		//权限控制
		sql.append(" and (target = 1 and companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 2 and companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 3 and vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
					
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long total = 0;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				total = rs.getLong("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGameNumByForm--end>");
		return total;
	}

	@Override
	public List<GamePrizeDto> getGameGift(PrizeForm form) {
		log.info("<GameDaoImpl--getGameGift--start>");
		StringBuffer sql = new StringBuffer();
		if(form.getPrizeType().equals(GamePrizeTypeEnum.GOODS.getState())){
			sql.append(" SELECT DISTINCT ib.`id`,ib.`name` FROM item_basic AS ib");
			sql.append(" INNER JOIN vending_machines_item AS vmi ON vmi.`basicItemId` = ib.`id`");
			if(StringUtil.isNotBlank(form.getItemName())){
				sql.append(" WHERE ib.`name` LIKE '%"+form.getItemName()+"%'");
			}
		} else if(form.getPrizeType().equals(GamePrizeTypeEnum.CARRYWATER.getState())){
			sql.append(" SELECT sg.`id`,sg.`name` FROM shopping_goods AS sg");
			sql.append(" WHERE sg.`deleteFlag`= 0 ");
			if(StringUtil.isNotBlank(form.getItemName())){
				sql.append(" AND sg.`name` LIKE '%"+form.getItemName()+"%'");
			}
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GamePrizeDto> itemList = new ArrayList<GamePrizeDto>();
		GamePrizeDto item = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				item = new GamePrizeDto();
				item.setItemId(rs.getLong("id"));
				item.setItemName(rs.getString("name"));
				itemList.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getGameGift--end>");
		return itemList;
	}

	@Override
	public List<ReceiveDto> getReceiveByForm(PrizeReceiveForm form) {
		log.info("<GameDaoImpl--getReceiveByForm--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT g.`name` AS  gameName,gp.`name` AS prizeName,gpr.`gamePrizeId`,tc.`phone`,");
		sql.append(" gpr.`createTime`,gpr.`endTime`,gpr.`receiveTime`,gpr.`customerId`,gpr.`addressId`");
		sql.append(" FROM game_prize_receive AS gpr");
		sql.append(" INNER JOIN game_prize AS gp ON gpr.`gamePrizeId` = gp.`id`");
		sql.append(" INNER JOIN game AS g ON g.`id` = gp.`gameId`");
		sql.append(" INNER JOIN tbl_customer AS tc ON tc.`id` = gpr.`customerId`");
		sql.append(" WHERE 1 = 1 ");
		if(form.getPrizeType()!=null){
			sql.append(" AND gp.`type` = '"+form.getPrizeType()+"'");
		}
		if(StringUtil.isNotBlank(form.getPrizeName())){
			sql.append(" AND gp.`name` LIKE '%"+form.getPrizeName()+"%'");
		}
		if(StringUtil.isNotBlank(form.getGameName())){
			sql.append(" AND g.`name` LIKE '%"+form.getGameName()+"%'");
		}
		if(StringUtil.isNotBlank(form.getPhone())){
			sql.append(" AND tc.`phone` = '"+form.getPhone()+"'");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ReceiveDto receive = null;
		List<ReceiveDto> receiveList = new ArrayList<ReceiveDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				receive = new ReceiveDto();
				receive.setAddressId(rs.getLong("addressId"));
				receive.setCreateTime(rs.getTimestamp("createTime"));
				receive.setCustomerId(rs.getLong("customerId"));
				receive.setEndTime(rs.getTimestamp("endTime"));
				receive.setGameName(rs.getString("gameName"));
				receive.setGamePrizeId(rs.getLong("gamePrizeId"));
				receive.setPhone(rs.getString("phone"));
				receive.setPrizeName(rs.getString("prizeName"));
				receive.setReceiveTime(rs.getTimestamp("receiveTime"));
				receiveList.add(receive);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getReceiveByForm--end>");
		return receiveList;
	}
	/**
	 * 设置奖品
	 * @param rewardId
	 * @param id
	 * @param goodsName
	 */
	public boolean setGoodsToGame(GamePrizeBean gamePrize){
		StringBuffer sql = new StringBuffer();
		sql.append("update game_prize set rewardId="+gamePrize.getRewardId()+",goodsName='"+gamePrize.getGoodsName()+"' where id="+gamePrize.getId());
		int upate = upate(sql.toString());
		if(upate>0) {
			return true;
		}
		return false;
		
	}
	
	public ReturnDataUtil queryPrizeDetail(GamePrizeForm gamePrizeForm) {
		StringBuffer sql = new StringBuffer();
		ReturnDataUtil data = new ReturnDataUtil();

		if(gamePrizeForm.getType().equals(2)) {//优惠券
			sql.append("select * from coupon g where deleteFlag=0 and endTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(new Date()) + "'");
			
		}else if (gamePrizeForm.getType().equals(3)) {//商城
			sql.append("select g.target,g.companyId,g.name,g.id,g.areaName,g.areaId,g.vmCode,c.name as companyName from shopping_goods g left join company c on g.companyId=c.id where g.deleteFlag=0 ");
		}else if (gamePrizeForm.getType().equals(5)) {//提水券
			sql.append("select * from carry_water_vouchers g where deleteFlag=0 and endTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(new Date()) + "'");
		}
		if(gamePrizeForm.getTarget()!=null) {
			sql.append(" and g.target = "+gamePrizeForm.getTarget());
			if(gamePrizeForm.getTarget()==1) {
					sql.append(" and g.companyId = "+gamePrizeForm.getCompanyId());
			}else if(gamePrizeForm.getTarget()==2){
				sql.append(" and g.areaId = "+gamePrizeForm.getAreaId());
			}else if(gamePrizeForm.getTarget()==3){
				sql.append(" and g.vmCode = "+gamePrizeForm.getVmCode());
			}
		}
		if(StringUtils.isNotBlank(gamePrizeForm.getName())) {
			sql.append(" and g.name like '%"+gamePrizeForm.getName()+"%'");
		}
		sql.append(" order by g.id desc");
		
		
		if (gamePrizeForm.getIsShowAll() == 0) {
			sql.append(" limit " + (gamePrizeForm.getCurrentPage() - 1) * gamePrizeForm.getPageSize() + ","
					+ gamePrizeForm.getPageSize());
		}
		List<GamePrizeDetailDto> list = new ArrayList<GamePrizeDetailDto>();
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GamePrizeDetailDto bean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bean = new GamePrizeDetailDto();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setVmCode(rs.getString("vmCode"));
				list.add(bean);
			}
			data.setCurrentPage(gamePrizeForm.getCurrentPage());
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return data;
		
	}
	
	public Long queryPrizeDetailCount(GamePrizeForm gamePrizeForm) {
		StringBuffer sql = new StringBuffer();
		if(gamePrizeForm.getType().equals(2)) {//优惠券
			sql.append("select count(*) as total from coupon g where deleteFlag=0 and endTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(new Date()) + "'");
		}else if (gamePrizeForm.getType().equals(3)) {//商城
			sql.append("select count(*) as total from shopping_goods g left join company c on g.companyId=c.id where g.deleteFlag=0 ");
		}else if (gamePrizeForm.getType().equals(5)) {//提水券
			sql.append("select count(*) as total from carry_water_vouchers g where deleteFlag=0 and endTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(new Date()) + "'");
		}
		if(gamePrizeForm.getTarget()!=null) {
			sql.append(" and g.target = "+gamePrizeForm.getTarget());
			if(gamePrizeForm.getTarget()==1) {
					sql.append(" and g.companyId = "+gamePrizeForm.getCompanyId());
			}else if(gamePrizeForm.getTarget()==2){
				sql.append(" and g.areaId = "+gamePrizeForm.getAreaId());
			}else if(gamePrizeForm.getTarget()==3){
				sql.append(" and g.vmCode = "+gamePrizeForm.getVmCode());
			}
		}
		if(StringUtils.isNotBlank(gamePrizeForm.getName())) {
			sql.append(" and g.name like '%"+gamePrizeForm.getName()+"%'");
		}

		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long i=0l;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				i=rs.getLong("total");
			}
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		return i;
	}
	
	/**
	 * 获取用户奖品信息
	 */
	@Override
	public List<PrizeReceiveDto> getGamePrizeReceive(Long customerId) {
		log.info("<GameDaoImpl>----<getGamePrizeReceive>----start");
		StringBuffer sql = new StringBuffer();
		List<PrizeReceiveDto> receiveList = new ArrayList<PrizeReceiveDto>();
		sql.append(" SELECT gp.id AS prizeId,gp.name AS prizeName ,gp.type AS prizeType ,");
		sql.append(" gp.amount,gpr.id AS prizeReceiveId,gpr.customerId,gpr.endTime,gpr.createTime,sg.pic");
		sql.append(" FROM game_prize AS gp INNER JOIN game_prize_receive AS gpr ");
		sql.append(" ON gp.`id` = gpr.`gamePrizeId` LEFT JOIN shopping_goods sg on gp.rewardId=sg.id ");
		sql.append("  WHERE 1 = 1 and gp.type=3 and gp.defaultFlag=0");
		sql.append(" AND gpr.`customerId` = "+customerId);
		log.info("获取用户的奖品sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PrizeReceiveDto prizeReceive = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				prizeReceive = new PrizeReceiveDto();
				prizeReceive.setAmount(rs.getInt("amount"));
				prizeReceive.setCustomerId(rs.getLong("customerId"));
				prizeReceive.setPrizeId(rs.getLong("prizeId"));
				prizeReceive.setPrizeName(rs.getString("prizeName"));
				prizeReceive.setPrizeReceiveId(rs.getLong("prizeReceiveId"));
				prizeReceive.setPrizeType(rs.getInt("prizeType"));
				prizeReceive.setCreateTime(rs.getTimestamp("createTime"));
				prizeReceive.setPic(rs.getString("pic"));
				receiveList.add(prizeReceive);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl>----<getGamePrizeReceive>----end");
		return receiveList;
	}
	
	@Override
	public List<GameBean> getAvailableGame(MachinesLAC machinesLAC) {
		log.info("<GameDaoImpl--getAvailableGame--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT g.id,g.`name`,g.times,g.prizeNum,g.target,g.integral FROM game AS g");
		sql.append(" WHERE 1 = 1 AND g.startTime <= NOW() AND g.endTime > NOW()");
		sql.append(" AND g.DeleteFlag = 0");
		sql.append(" AND ( CASE ");
		sql.append("     WHEN g.target = 1 AND g.companyId = '"+machinesLAC.getCompanyId()+"' THEN 1");
		sql.append("     WHEN g.target = 2 AND g.areaId = '"+machinesLAC.getAreaId()+"' THEN 1");
		sql.append("     WHEN g.target = 3 AND g.vmCode = '"+machinesLAC.getVmCode()+"' THEN 1");
		sql.append("     ELSE 0 END ) = 1");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GameBean> gameList = new ArrayList<GameBean>();
		GameBean game = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				game = new GameBean();
				game.setId(rs.getInt("id"));
				game.setName(rs.getString("name"));
				game.setTimes(rs.getInt("times"));
				game.setPrizeNum(rs.getInt("prizeNum"));
				game.setTarget(rs.getInt("target"));
				game.setIntegral(rs.getInt("integral"));
				gameList.add(game);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<GameDaoImpl--getAvailableGame--end>");
		return gameList;
	}
}
