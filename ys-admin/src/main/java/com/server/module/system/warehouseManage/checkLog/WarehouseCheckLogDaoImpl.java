package com.server.module.system.warehouseManage.checkLog;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import jersey.repackaged.com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author name: yjr 
 * create time: 2018-06-02 15:10:23
 */
@Repository
public class WarehouseCheckLogDaoImpl extends BaseDao<WarehouseCheckLogBean> implements WarehouseCheckLogDao {

	private static Log log = LogFactory.getLog(WarehouseCheckLogDaoImpl.class);
	@Autowired
	private CompanyDao companyDao;
	public ReturnDataUtil listPage(WarehouseCheckLogForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		//sql.append("select id,basicItemId,itemName,balance,preMachinesNum,machinesNum,replenishNum,sellNum,outWarehouseNum,lineId,areaId,companyId,userId,startTime,endTime,createTime,time from warehouse_check_log where 1=1 ");
		sql.append("select distinct lineId,areaId,companyId,userId,startTime,endTime,time from warehouse_check_log where 1=1 ");

		if(StringUtils.isNotEmpty(condition.getCompanyId())){
			sql.append(" and companyId="+condition.getCompanyId());
		}else{
			Integer companyId= UserUtils.getUser().getCompanyId();
			if(companyId!=1){
				sql.append(" and companyId in "+companyDao.findAllSonCompanyIdForInSql(companyId));
			}
		}
		if(StringUtils.isNotEmpty(condition.getAreaId())){
			sql.append(" and areaId="+condition.getAreaId());
		}
		if(StringUtils.isNotEmpty(condition.getLineId())){
			sql.append(" and lineId="+condition.getLineId());
		}


		if(StringUtils.isNotEmpty(condition.getStartTime())){
			sql.append(" and endTime>='"+condition.getStartTime()+"'");
		}
		if(StringUtils.isNotEmpty(condition.getEndTime())){
			sql.append(" and endTime<='"+condition.getEndTime()+"'");
		}
		sql.append(" order by endTime desc ");

		if (showSql) {
			log.info(sql);
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			String sqlcount="select count(*) from ("+sql.toString()+") as c";
			System.out.println(sqlcount);
			pst = conn.prepareStatement(sqlcount);

			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());


			rs = pst.executeQuery();
			List<WarehouseCheckLogBean> list = Lists.newArrayList();
			Map<Long,Map<String,Object>> mapLine=Maps.newHashMap();

			while (rs.next()) {

				WarehouseCheckLogBean bean = new WarehouseCheckLogBean();
				//bean.setId(rs.getLong("id"));
				//bean.setBasicItemId(rs.getLong("basicItemId"));
				//bean.setItemName(rs.getString("itemName"));
				//bean.setPreMachinesNum(rs.getLong("preMachinesNum"));
				//bean.setBalance(rs.getLong("balance"));
				//bean.setMachinesNum(rs.getInt("machinesNum"));
				//bean.setReplenishNum(rs.getLong("replenishNum"));
				//bean.setSellNum(rs.getLong("sellNum"));
				//bean.setOutWarehouseNum(rs.getLong("outWarehouseNum"));getLong
				bean.setLineId(rs.getLong("lineId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTime(rs.getInt("time"));
				bean.setUserId(rs.getLong("userId"));
				//System.out.println(rs.getTimestamp("startTime"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				//bean.setCreateTime(rs.getTimestamp("createTime"));
				if(mapLine.get(bean.getLineId())==null){
					Map<String,Object> companyAreaLineMap=this.getLineAndAreaAndCompanyByLineId(bean.getLineId());
					mapLine.put(bean.getLineId(), companyAreaLineMap);
				}
				Map<String,Object> map=mapLine.get(bean.getLineId());
				bean.setAreaName((String)map.get("areaName"));
				bean.setCompanyName((String)map.get("companyName"));
				bean.setLineName((String)map.get("lineName"));
				list.add(bean);
			}

			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ReturnDataUtil listDetail(Long lineId,Integer time) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,basicItemId,itemName,balance,preMachinesNum,machinesNum,replenishNum,sellNum,outWarehouseNum,lineId,areaId,companyId,userId,startTime,endTime,createTime,time from warehouse_check_log where 1=1 ");
		
		/*if(StringUtils.isNotEmpty(condition.getCompanyId())){
			sql.append(" and companyId="+condition.getCompanyId());
		}else{
			Integer companyId= UserUtils.getUser().getCompanyId();
			if(companyId!=1){
				sql.append(" and companyId="+companyId);
			}
		}
		if(StringUtils.isNotEmpty(condition.getAreaId())){
			sql.append(" and areaId="+condition.getAreaId());
		}
		if(StringUtils.isNotEmpty(condition.getLineId())){
			sql.append(" and lineId="+condition.getLineId());
		}
		
		
		if(StringUtils.isNotEmpty(condition.getStartTime())){
			sql.append(" and createTime>='"+condition.getStartTime()+"'");
		}
		if(StringUtils.isNotEmpty(condition.getEndTime())){
			sql.append(" and createTime<='"+condition.getEndTime()+"'");
		}
		sql.append(" order by createTime desc ");*/
		sql.append(" and lineId="+lineId+" and time="+time);
		
		if (showSql) {
			log.info(sql);
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			
			//rs = pst.executeQuery();
			//long count = 0;
			///while (rs.next()) {
			//	count = rs.getInt(1);
			//}
			//long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() );

			
			rs = pst.executeQuery();
			List<WarehouseCheckLogBean> list = Lists.newArrayList();
			Map<Long,Map<String,Object>> mapLine=Maps.newHashMap();
			
			while (rs.next()) {
				WarehouseCheckLogBean bean = new WarehouseCheckLogBean();
				bean.setId(rs.getLong("id"));
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setPreMachinesNum(rs.getLong("preMachinesNum"));
				bean.setBalance(rs.getLong("balance"));
				bean.setMachinesNum(rs.getInt("machinesNum"));
				bean.setReplenishNum(rs.getLong("replenishNum"));
				bean.setSellNum(rs.getLong("sellNum"));
				bean.setOutWarehouseNum(rs.getLong("outWarehouseNum"));
				bean.setLineId(rs.getLong("lineId"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTime(rs.getInt("time"));
				bean.setUserId(rs.getLong("userId"));
				System.out.println(rs.getTimestamp("startTime"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				if(mapLine.get(bean.getLineId())==null){
					Map<String,Object> companyAreaLineMap=this.getLineAndAreaAndCompanyByLineId(bean.getLineId());
					mapLine.put(bean.getLineId(), companyAreaLineMap);
				}
				Map<String,Object> map=mapLine.get(bean.getLineId());
				bean.setAreaName((String)map.get("areaName"));
				bean.setCompanyName((String)map.get("companyName"));
				bean.setLineName((String)map.get("lineName"));
				list.add(bean);
			}
			
			data.setCurrentPage(1);
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public WarehouseCheckLogBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		WarehouseCheckLogBean entity = new WarehouseCheckLogBean();
		return super.del(entity);
	}

	public boolean update(WarehouseCheckLogBean entity) {
		return super.update(entity);
	}

	public WarehouseCheckLogBean insert(WarehouseCheckLogBean entity) {
		return super.insert(entity);
	}

	public List<WarehouseCheckLogBean> list(WarehouseCheckLogForm condition) {
		return null;
	}

	/**
	 * 盘点,记录某时间段商品的各个数量指标：上一次盘点机器内商品数、本次机器内商品数、销售数量、出货数（通过出货单取得）、
	 * 补货数量、差额。差额=上次机器内数量+出货数-销售数-本次机器内数量
	 * 注:每次盘点都是基于线路
	 * @param lineId
	 * @param areaId
	 * @param companyId
	 * @param endTime
	 */
	@SuppressWarnings("unchecked")
	public void checkStock(Long lineId, Long areaId, Long companyId,  Date endTime) {
		long userId=UserUtils.getUser().getId();
		//UserUtils.getUser().getId()
		//该次盘点开始时间
		Date startTime=getMaxDate(lineId);
		String start = "";
		String end = "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (startTime == null) {//如果属于第一次盘点，默认为该时间
			start = "2017-10-01 00:00:00";
			try {
				startTime=sf.parse(start);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			start=sf.format(startTime);
		}
		if(endTime==null){
			endTime=new Date();//盘点结束时间默认为当前时间
		}
		end = sf.format(endTime);
		
		log.info("start=" + start + "  end=" + end);
		log.info("lineId="+lineId+" areaId="+areaId+" companyId="+companyId);
		//根据线路id 获取地区id，公司id
		Map<String,Object> companyAreaLineMap=this.getLineAndAreaAndCompanyByLineId(lineId);
        areaId=((Integer)companyAreaLineMap.get("areaId")).longValue();
        companyId=((Integer)companyAreaLineMap.get("companyId")).longValue();
		log.info(" after lineId="+lineId+" areaId="+areaId+" companyId="+companyId);
		//货道商品数量
		String wayNumSql = "SELECT ib.id as basicItemId ,ib.name as itemName,SUM(w.num) AS num from vending_machines_way w INNER JOIN vending_machines_info i ON w.vendingMachinesCode=i.code INNER JOIN vending_machines_item b ON w.itemId=b.id INNER JOIN item_basic ib ON b.basicItemId=ib.id WHERE i.lineId="+lineId+" GROUP BY ib.id,ib.name";
		List<ItemInWayNumVo> wayNumList = super.list(wayNumSql, null, ItemInWayNumVo.class);

        //计算这是第几次盘点  由time标识
		String timeSql="SELECT time  FROM warehouse_check_log WHERE  lineId="+lineId+" ORDER BY time DESC LIMIT 1";
		List<WarehouseCheckLogBean> timeList = super.list(timeSql, null, WarehouseCheckLogBean.class);
        int time=1;
		if(timeList.size()>0){
			time=timeList.get(0).getTime().intValue();
			time=time+1;
		}
		timeList=null;

		//获取上一次盘点的机器内数量
		for (int i = 0; i <wayNumList.size() ; i++) {
			ItemInWayNumVo vo=wayNumList.get(i);
			String preWayNumSql="SELECT machinesNum,time  FROM warehouse_check_log WHERE basicItemId="+vo.getBasicItemId()+"   AND lineId="+lineId+" ORDER BY time DESC LIMIT 1";
			System.out.println(preWayNumSql);
			List<WarehouseCheckLogBean> tempList = super.list(preWayNumSql, null, WarehouseCheckLogBean.class);
            if(tempList.size()>0){
				WarehouseCheckLogBean temp=tempList.get(0);
				vo.setPreNum(new BigDecimal(temp.getMachinesNum()));
			}else{//
				vo.setPreNum(new BigDecimal(0l));

			}
		}



		//补货数量
		String replenishSql = "SELECT w.basicItemId,SUM((w.num-w.preNum)) AS num FROM replenish_record w INNER JOIN vending_machines_info i ON w.vmCode=i.code WHERE opType=2 AND i.lineId="+lineId+" and w.createTime>='"+start+"' and w.createTime<='"+end+"' GROUP BY w.basicItemId";
		List<ReplenishNumVo> replenishNumList = super.list(replenishSql, null, ReplenishNumVo.class);
		//销售数量
		String sellNumSql = "SELECT w.basicItemId,SUM((w.preNum-w.num)) AS num FROM vending_waynum_log w INNER JOIN vending_machines_info i ON w.vmCode=i.code WHERE opType=1 AND i.lineId="+lineId+" and w.createTime>='"+start+"' and w.createTime<='"+end+"' GROUP BY w.basicItemId";
		List<SellNumVo> sellNumList = super.list(sellNumSql, null, SellNumVo.class);
		//出库数量
		String outSql = "SELECT i.itemId as basicItemId,i.itemName,SUM(i.quantity) AS num FROM warehouse_output_bill b INNER JOIN warehouse_bill_item i ON b.id=i.billId WHERE b.output=1 AND state=60403 AND type=60405 and  lineId="+lineId+" and i.createTime>='"+start+"' and i.createTime<='"+end+"' GROUP BY i.itemId,i.itemName";
		List<OutNumVo> outNumList = super.list(outSql, null, OutNumVo.class);
		//返还等
		String inSql = "SELECT i.itemId as basicItemId,i.itemName,SUM(i.quantity) AS num FROM warehouse_output_bill b INNER JOIN warehouse_give_back i ON b.id=i.billId WHERE b.output=1 AND state=60403 and b.type=60405 AND lineId="+lineId+" and i.createTime>='"+start+"' and i.createTime<='"+end+"' GROUP BY i.itemId,i.itemName";
		List<OutNumVo> inNumList = super.list(inSql, null, OutNumVo.class);

		//出库减去返还的等于真正出库的
		for (OutNumVo vo: outNumList){
            if(inNumList.size()>0)
			for (int i = 0; i < inNumList.size(); i++) {
				OutNumVo temp=inNumList.get(i);
				if(vo.getBasicItemId().longValue()==temp.getBasicItemId().longValue()){
					vo.setNum(vo.getNum().subtract(temp.getNum()));
				}
			}
		}

		List<WarehouseCheckLogBean> checkLogList=Lists.newArrayList();


		for(int i=0;i<wayNumList.size();i++){
			WarehouseCheckLogBean checkLog=new WarehouseCheckLogBean();
			ItemInWayNumVo itemInWay=wayNumList.get(i);
			checkLog.setAreaId(areaId);
			checkLog.setBasicItemId(itemInWay.getBasicItemId().longValue());
			checkLog.setLineId(lineId);
			checkLog.setCompanyId(companyId);
			checkLog.setMachinesNum(itemInWay.getNum().intValue());
			checkLog.setPreMachinesNum(itemInWay.getPreNum().longValue());
			checkLog.setItemName(itemInWay.getItemName());
			checkLog.setEndTime(endTime);
			checkLog.setStartTime(startTime);
			checkLog.setUserId(userId);//UserUtils.getUser().getId()
			checkLog.setTime(time);
			ReplenishNumVo replenishNum=null;
			for(int j=0;j<replenishNumList.size();j++){
				if(replenishNumList.get(j).getBasicItemId().intValue()==checkLog.getBasicItemId().intValue()){
					replenishNum=replenishNumList.get(j);
				}
			}
			if(replenishNum!=null){
				checkLog.setReplenishNum(replenishNum.getNum().longValue());
			}else{
				checkLog.setReplenishNum(0l);
			}
			SellNumVo sellNum=null;
			for(int j=0;j<sellNumList.size();j++){
				if(sellNumList.get(j).getBasicItemId().intValue()==checkLog.getBasicItemId().intValue()){
					sellNum=sellNumList.get(j);
				}
			}
			if(sellNum!=null){
				checkLog.setSellNum(sellNum.getNum().longValue());
			}else{
				checkLog.setSellNum(0l);
			}
			OutNumVo outNum=null;
			for(int j=0;j<outNumList.size();j++){
				if(outNumList.get(j).getBasicItemId().intValue()==checkLog.getBasicItemId().intValue()){
					outNum=outNumList.get(j);
				}
			}
			if(outNum!=null){
				checkLog.setOutWarehouseNum(outNum.getNum().longValue());
			}
			if(checkLog.getOutWarehouseNum()==null){
				checkLog.setOutWarehouseNum(0l);
			}
			long balance=0l;//差额 preMachinesNum+outWarehouseNum-sellNum-machinesNum
			System.out.println(checkLog.getPreMachinesNum()+"   "+checkLog.getOutWarehouseNum()+"  "+checkLog.getSellNum()+"   "+checkLog.getMachinesNum());
			balance=checkLog.getPreMachinesNum()+checkLog.getOutWarehouseNum()-checkLog.getSellNum()-checkLog.getMachinesNum();
			checkLog.setBalance(balance);
			checkLogList.add(checkLog);
		}
		if(wayNumList.size()==0){//商品只出了库，不存在于机器中,极端的情况，tm的无语了，这样也要做盘点
			if(outNumList.size()>0){
				for (int i = 0; i < outNumList.size(); i++) {
					WarehouseCheckLogBean checkLog=new WarehouseCheckLogBean();
					OutNumVo outNumVo=outNumList.get(i);
					checkLog.setAreaId(areaId);
					checkLog.setBasicItemId(outNumVo.getBasicItemId().longValue());
					checkLog.setLineId(lineId);
					checkLog.setCompanyId(companyId);
					checkLog.setMachinesNum(0);
					checkLog.setPreMachinesNum(0l);
					checkLog.setItemName(outNumVo.getItemName());
					checkLog.setEndTime(endTime);
					checkLog.setStartTime(startTime);
					checkLog.setUserId(userId);//UserUtils.getUser().getId()
					checkLog.setTime(1);
					checkLog.setReplenishNum(0l);
					checkLog.setSellNum(0l);
					checkLog.setOutWarehouseNum(outNumVo.getNum().longValue());
					long balance=0l;//差额 preMachinesNum+outWarehouseNum-sellNum-machinesNum
					System.out.println(checkLog.getPreMachinesNum()+"   "+checkLog.getOutWarehouseNum()+"  "+checkLog.getSellNum()+"   "+checkLog.getMachinesNum());
					balance=checkLog.getPreMachinesNum()+checkLog.getOutWarehouseNum()-checkLog.getSellNum()-checkLog.getMachinesNum();
					checkLog.setBalance(balance);
					checkLogList.add(checkLog);

				}

			}
		}
		
		Connection conn=openConnection();
		try {
			conn.setAutoCommit(false);
			for (WarehouseCheckLogBean checkLog : checkLogList) {
				insert(conn,checkLog);
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			closeConnection(conn);
		}


	}

	private Map<String,Object> getLineAndAreaAndCompanyByLineId(Long id){
		String sql="SELECT l.id AS lineId,l.name AS lineName,a.id AS areaId,a.name AS areaName,c.id AS companyId,c.name AS companyName FROM vending_line  l INNER JOIN vending_area a ON l.areaId=a.id INNER JOIN company c ON a.companyId=c.id WHERE l.id="+id;
	    List<Map<String,Object>> list=super.list(sql, null);
	    if(list.size()>0){
	    	return list.get(0);
	    }
	    return null;
	}

	/**
	 * 获取上次盘点时间
	 * @param lineId
	 * @return
	 */
	@Override
	public Date getMaxDate(Long lineId) {
		String sql="SELECT MAX(endTime) AS endTime FROM warehouse_check_log WHERE lineId="+lineId;
		List<Map<String,Object>> list=super.list(sql, null);
		if(list.size()==0){
			sql="SELECT MIN(l.createTime) AS createTime FROM vending_waynum_log  l INNER JOIN vending_machines_info i ON i.code=l.vmCode WHERE i.lineId="+lineId;
			list=super.list(sql, null);
			if(list.size()>0){
				Map<String,Object> map=list.get(0);
				return (Date)map.get("createTime");
			}
			return null;
		}
		Map<String,Object> map=list.get(0);
		return (Date)map.get("endTime");
	}
	
	
}
