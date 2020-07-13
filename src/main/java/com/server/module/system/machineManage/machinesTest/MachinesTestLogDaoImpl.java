package com.server.module.system.machineManage.machinesTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.server.module.commonBean.TotalResultBean;
import com.server.redis.RedisClient;
import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
import com.server.util.stateEnum.MachineStateEnum;
@Repository
public class MachinesTestLogDaoImpl extends MySqlFuns implements MachinesTestLogDao{

	private final static Logger log = LogManager.getLogger(MachinesTestLogDaoImpl.class);
	
	@Autowired
	private RedisClient redisClient;

	@Override
	public TotalResultBean<List<MachinesTestLogDto>> getMachinesTest(MachinesTestForm form) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SQL_CALC_FOUND_ROWS msl.id,msl.`vmCode`,li.`name` AS userName,msl.`preState`,msl.`currState`,");
		sql.append(" msl.`startTime`,msl.`endTime`,msl.`remark`,msl.`success`");
		sql.append(" FROM machines_test_log AS msl");
		sql.append(" INNER JOIN vending_machines_info AS vmi ON vmi.`code` = msl.`vmCode`");
		sql.append(" INNER JOIN login_info AS li ON li.`id` = msl.`createUser`");
		sql.append(" where 1=1 ");
		if(form.getCompanyId()!=null){
			sql.append(" AND FIND_IN_SET(vmi.`companyId`,getChildList("+form.getCompanyId()+"))");
		}
		if(StringUtils.isNotBlank(form.getVmCode())){
			sql.append(" AND msl.vmCode = '"+form.getVmCode()+"'");
		}
		if(form.getStartTime()!=null){
			sql.append(" AND msl.`startTime` >= '"+DateUtil.formatYYYYMMDDHHMMSS(form.getStartTime())+"'");
		}
		if(form.getEndTime()!=null){
			sql.append(" AND msl.`startTime` <= '"+DateUtil.formatYYYYMMDDHHMMSS(form.getEndTime())+"'");
		}
		if(form.getSuccess() != null){
			sql.append(" AND msl.`success` = "+form.getSuccess());
		}
		if(form.getIsShowAll() == 0){
			sql.append(" limit "+(form.getCurrentPage()-1)*form.getPageSize()+","+form.getPageSize());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement totalPs = null;
		ResultSet rs = null;
		ResultSet totalRs = null;
		List<MachinesTestLogDto> testLogList = new ArrayList<MachinesTestLogDto>();
		MachinesTestLogDto testDto = null;
		Long total = 0L;
		try {
			conn = openConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				testDto = new MachinesTestLogDto();
				testDto.setId(rs.getLong("id"));
				testDto.setEndTime(DateUtil.formatYYYYMMDDHHMMSS(rs.getTimestamp("endTime")));
				testDto.setRemark(rs.getString("remark"));
				testDto.setStartTime(DateUtil.formatYYYYMMDDHHMMSS(rs.getTimestamp("startTime")));
				MachineStateEnum preState = MachineStateEnum.getMachineStateEnum(rs.getInt("preState"));
				testDto.setTestName(preState.getName()+"测试");
				MachineStateEnum currState = MachineStateEnum.getMachineStateEnum(rs.getInt("currState"));
				String stateChange = preState.getName();
				if(currState != null){
					stateChange+="->"+currState.getName();
				}else{
					stateChange+="->"+preState.getName();
				}
				testDto.setStateChange(stateChange);
				testDto.setVmCode(rs.getString("vmCode"));
				int isSuccess = rs.getInt("success");
				if(isSuccess == 1){
					testDto.setSuccessOrFail("测试成功");
				}else if(isSuccess == 2){
					testDto.setSuccessOrFail("测试失败");
				}else if(isSuccess == 0 && redisClient.exists("startTest"+testDto.getVmCode())){
					testDto.setSuccessOrFail("测试进行中");
				}else{
					testDto.setSuccessOrFail("测试超时失败");
				}
				testDto.setUserName(rs.getString("userName"));
				testLogList.add(testDto);
			}
			totalPs = conn.prepareStatement(TotalResultBean.TOTAL_SQL);
			totalRs = totalPs.executeQuery();
			if(totalRs != null && totalRs.next()){
				total = totalRs.getLong("total");
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			this.closeConnection(totalRs, totalPs, null);
			this.closeConnection(rs, ps, conn);
		}
		return new TotalResultBean<List<MachinesTestLogDto>>(total,testLogList);
	}
	
	@Override
	public List<MachinesTestResultDto> getPurchaseTestResult(MachinesTestLogBean testLog) {
		log.info("MachinesTestLogDaoImpl--getPurchaseTestResult--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tc.`phone`,pr.`wayNumber`,pri.`itemName`,pri.`num`,pr.`createTime` FROM pay_record AS pr");
		sql.append(" INNER JOIN tbl_customer AS tc ON pr.`customerId` = tc.`id`");
		sql.append(" INNER JOIN pay_record_item AS pri ON pr.`id` = pri.`payRecordId`");
		sql.append(" WHERE pr.vendingMachinesCode = '"+testLog.getVmCode()+"'");
		sql.append(" AND pr.createTime >= '"+DateUtil.formatYYYYMMDDHHMMSS(testLog.getStartTime())+"'");
		if(testLog.getEndTime() != null){
			sql.append(" AND pr.createTime <= '"+DateUtil.formatYYYYMMDDHHMMSS(testLog.getEndTime())+"'");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MachinesTestResultDto> testList = new ArrayList<MachinesTestResultDto>();
		MachinesTestResultDto testResult = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				testResult = new MachinesTestResultDto();
				testResult.setCreateTime(DateUtil.formatYYYYMMDDHHMMSS(rs.getTimestamp("createTime")));
				testResult.setItemName(rs.getString("itemName"));
				testResult.setNum(rs.getInt("num"));
				testResult.setPhone(rs.getString("phone"));
				testResult.setRemark("购买测试");
				testResult.setWayNumber(rs.getInt("wayNumber"));
				testList.add(testResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("MachinesTestLogDaoImpl--getPurchaseTestResult--end");
		return testList;
	}

	@Override
	public List<MachinesTestResultDto> getReplenishTestResult(MachinesTestLogBean testLog) {
		log.info("MachinesTestLogDaoImpl--getReplenishTestResult--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT li.`phone`,rr.`wayNumber`,ib.`name` AS itemName,rr.`createTime`,rr.`num`-rr.`preNum` AS num");
		sql.append(" FROM replenish_record AS rr INNER JOIN item_basic AS ib ON ib.`id` = rr.`basicItemId`");
		sql.append(" INNER JOIN login_info AS li ON li.`id` = rr.`userId`");
		sql.append(" WHERE rr.opType = 2 AND rr.vmCode = '"+testLog.getVmCode()+"'");
		sql.append(" AND rr.createTime >= '"+DateUtil.formatYYYYMMDDHHMMSS(testLog.getStartTime())+"'");
		if(testLog.getEndTime() != null){
			sql.append(" AND rr.createTime <= '"+DateUtil.formatYYYYMMDDHHMMSS(testLog.getEndTime())+"'");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MachinesTestResultDto> testList = new ArrayList<MachinesTestResultDto>();
		MachinesTestResultDto testResult = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				testResult = new MachinesTestResultDto();
				testResult.setCreateTime(DateUtil.formatYYYYMMDDHHMMSS(rs.getTimestamp("createTime")));
				testResult.setItemName(rs.getString("itemName"));
				testResult.setNum(rs.getInt("num"));
				testResult.setPhone(rs.getString("phone"));
				testResult.setRemark("补货测试");
				testResult.setWayNumber(rs.getInt("wayNumber"));
				testList.add(testResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("MachinesTestLogDaoImpl--getReplenishTestResult--end");
		return testList;
	}


	@Override
	public MachinesTestLogBean getTestLog(Long id) {
		log.info("MachinesTestLogDaoImpl--getTestLog--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,vmCode,preState,currState,remark,startTime,endTime,createUser");
		sql.append(" FROM machines_test_log WHERE id = '"+id+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MachinesTestLogBean testLog = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				testLog = new MachinesTestLogBean();
				testLog.setId(rs.getLong("id"));
				testLog.setCreateUser(rs.getLong("createUser"));
				testLog.setCurrState(rs.getInt("currState"));
				testLog.setEndTime(rs.getTimestamp("endTime"));
				testLog.setPreState(rs.getInt("preState"));
				testLog.setRemark(rs.getString("remark"));
				testLog.setStartTime(rs.getTimestamp("startTime"));
				testLog.setVmCode(rs.getString("vmCode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("MachinesTestLogDaoImpl--getTestLog--end");
		return testLog;
	}
}
