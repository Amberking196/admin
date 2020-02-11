package com.server.module.system.replenishManage.machinesReplenishManage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserDao;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machineReplenish.ReplenishDetailVo;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * author name: why create time: 2018-04-23 16:19:47
 */
@Repository
public class MachinesReplenishDaoImpl extends BaseDao<MachinesReplenishBean> implements MachinesReplenishDao {

	public static Logger log = LogManager.getLogger(MachinesReplenishDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private StateInfoDao stateInfoDaoImpl;
	@Autowired
	private AdminUserDao adminUserDaoImpl;
    public ReturnDataUtil listPage1(MachinesReplenishCondition condition){
/*

		SELECT a.*,quenum/fullNum,c.name AS companyName,l.name AS lineName FROM (
				SELECT vmi.code,vmi.locatoinName,vmi.companyId,vmi.areaId,vmi.lineId,SUM(num) AS num,SUM(fullNum) AS fullNum,(SUM(fullNum)-SUM(num)) AS queNum FROM vending_machines_info vmi INNER  JOIN vending_machines_way_item vmw ON vmi.code=vmw.vmCode
		GROUP BY vmi.code,vmi.locatoinName,vmi.companyId,vmi.areaId,vmi.lineId
) a INNER JOIN company c ON a.companyId=c.id INNER JOIN vending_line l ON a.lineId=l.id LEFT JOIN replenish_company_machines rcm ON a.code=rcm.code WHERE 1=1 AND quenum/fullNum>0.5 AND c.id=113 AND rcm.companyId=141
*/
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.*,quenum/fullNum as rate,c.name AS companyName,l.name AS lineName from (");
//		sql.append("SELECT vmi.code,vmi.locatoinName,vmi.companyId,vmi.areaId,vmi.lineId,SUM(num) AS num,SUM(fullNum) AS fullNum,if((SUM(fullNum)-SUM(num))<0,0,(SUM(fullNum)-SUM(num))) AS queNum FROM vending_machines_info vmi INNER  JOIN vending_machines_way_item vmw ON vmi.code=vmw.vmCode WHERE vmi.machineVersion=2 ");
		sql.append("SELECT vmi.code,vmi.locatoinName,vmi.companyId,vmi.areaId,vmi.lineId,SUM(num) AS num, ");
		sql.append(" ( CASE WHEN vmi.machineType = 1 THEN SUM(fullNum) WHEN vmi.machineType = 2 THEN maxCapacity END ) AS fullNum, ");
		sql.append(" ( CASE WHEN vmi.machineType = 1 THEN IF ( (SUM(fullNum) - SUM(num)) < 0, 0, (SUM(fullNum) - SUM(num)) ) WHEN vmi.machineType = 2 THEN IF ( maxCapacity - SUM(num) < 0, 0, maxCapacity - SUM(num) ) END ) AS queNum ");
		sql.append(" FROM vending_machines_info vmi INNER  JOIN vending_machines_way_item vmw ON vmi.code=vmw.vmCode WHERE vmi.machineVersion=2 ");

		if (condition.getAreaId() != null && condition.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + condition.getAreaId() + "' ");
		}
		if(StringUtil.isNotBlank(condition.getVmCode())){
			sql.append(" and vmi.code='"+condition.getVmCode()+"'");
		}
		if(StringUtil.isNotBlank(condition.getAddress())){
			sql.append(" and vmi.locatoinName like '%"+condition.getAddress()+"%'");
		}

		sql.append(" GROUP BY vmi.code,vmi.locatoinName,vmi.companyId,vmi.areaId,vmi.lineId ");
		sql.append(" ) a INNER JOIN company c ON a.companyId=c.id left JOIN vending_line l ON a.lineId=l.id LEFT JOIN " +
				"replenish_company_machines rcm ON a.code=rcm.code WHERE 1=1 ");
		
        //AND quenum/fullNum>0.5 AND c.id=113 AND rcm.companyId=141
		if(condition.getRate()==null)
			condition.setRate(30);
		sql.append(" and quenum/fullNum>="+condition.getRate()*0.01);

        if(condition.getOtherCompanyId()!=null){
        	sql.append(" and rcm.companyId="+condition.getOtherCompanyId());
		}
		// 拿到登录用户信息  等级控制 查询判断
		User user = UserUtils.getUser();
		AdminUserBean adminUserBean = adminUserDaoImpl.findUserById(user.getId());
		if (adminUserBean.getLevel() != null && adminUserBean.getLevel() > 0) {
			// 公司级
			if (adminUserBean.getLevel() == 1) {
				sql.append(" and (c.id in"+ companyDaoImpl.findAllSonCompanyIdForInSql(adminUserBean.getCompanyId()));
				sql.append(" or FIND_IN_SET( a.code,(select GROUP_CONCAT(vmCode) from login_info_machine lim where lim.userId="+UserUtils.getUser().getId()+")))");

			} else if (adminUserBean.getLevel() == 2) {//区域级
				sql.append(" and c.id ="+adminUserBean.getCompanyId()+" and  a.areaId = '"+adminUserBean.getAreaId()+"' ");
			} else if (adminUserBean.getLevel() == 3) {//线路
				sql.append(" and c.id ="+adminUserBean.getCompanyId()+" and  a.areaId ="+adminUserBean.getAreaId()+" and a.lineId ="+adminUserBean.getLineId());
			}
		}
		if (condition.getCompanyId() != null) {
			sql.append(" and c.id ="+condition.getCompanyId());
		}else{

		}
		if (condition.getAreaId() != null) {
			sql.append(" and a.areaId ="+condition.getAreaId());
		}
		if (condition.getLineId() != null) {
			sql.append(" and a.lineId ="+condition.getLineId());
		}

		sql.append(" order by rate desc,code ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("补货管理列表查询sql语句>>>" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<MachinesReplenishBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				/*"ratio": 100,
						"companyName": "珠海优水网络",
						"lineName": "默认珠海",
						"code": "1988000325",
						"principal": "1",
						"locatoinName": "前山（未使用）",
						"lineId": 11,*/
						MachinesReplenishBean bean = new MachinesReplenishBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("code"));
				//bean.setPrincipal(rs.getString("linkman"));
				bean.setLocatoinName(rs.getString("locatoinName"));
				double rate=rs.getDouble("rate");
				rate=rate*100;
				bean.setRatio(new Double(rate).intValue());
				//bean.setLineId(rs.getInt("lineId"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setPageSize(condition.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}

	}
	/**
	 * 补货管理 的列表查询
	 */
	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(MachinesReplenishCondition condition) {
		log.info("<MachinesReplenishDaoImpl>----<listPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select vmi.code,linkman,locatoinName,c.name companyName,vl.name lineName,vl.id lineId,round(if((SUM(fullNum)-sum(num))<0,0,(SUM(fullNum)-sum(num)))/SUM(fullNum)*100 ) rate from vending_machines_info vmi join company  c on vmi.companyId=c.id inner join vending_machines_way vmw on vmi.code=vmw.vendingMachinesCode ");
		sql.append(" left join vending_line  vl on vmi.lineId=vl.id left join replenish_company_machines rcm ON vmi.code=rcm.code where 1=1 and fullNUm>0  and vmi.state=20001 and vmi.machineVersion=1 ");
        
		if(StringUtil.isNotBlank(condition.getVmCode())){
			sql.append(" and vmi.code='"+condition.getVmCode()+"'");
		}
		if(StringUtil.isNotBlank(condition.getAddress())){
			sql.append(" and vmi.locatoinName like '%"+condition.getAddress()+"%'");
		}
		// 拿到登录用户信息  等级控制 查询判断
		User user = UserUtils.getUser();
		AdminUserBean adminUserBean = adminUserDaoImpl.findUserById(user.getId());
		if (adminUserBean.getLevel() != null && adminUserBean.getLevel() > 0) {
			// 公司级
			if (adminUserBean.getLevel() == 1) {
				sql.append("and vmi.companyId in"+ companyDaoImpl.findAllSonCompanyIdForInSql(adminUserBean.getCompanyId()));
			} else if (adminUserBean.getLevel() == 2) {//区域级
				sql.append(" and vmi.companyId = '"+adminUserBean.getCompanyId()+"' and  vl.areaId = '"+adminUserBean.getAreaId()+"' ");
			} else if (adminUserBean.getLevel() == 3) {//线路
				sql.append(" and vmi.companyId = '"+adminUserBean.getCompanyId()+"' and  vl.areaId = '"+adminUserBean.getAreaId()+"' and vl.id = '"+adminUserBean.getLineId()+"' ");
			}
		}
		
		if (condition.getCompanyId() != null) {
			sql.append(" and c.id = '"+condition.getCompanyId()+"' ");
		}
		if (condition.getAreaId() != null) {
			sql.append(" and vl.areaId = '"+condition.getAreaId()+"' ");
		}
		if (condition.getLineId() != null) {
			sql.append(" and vl.id = '"+condition.getLineId()+"' ");
		}
		if(condition.getOtherCompanyId()!=null){
			sql.append(" and rcm.companyId="+condition.getOtherCompanyId());
		}

		// 如果传入了code值
		String[] code = condition.getCodes();
		if (code!= null && code.length > 0) {
			String codes = "";
			for (String s : code) {
				codes = codes +s+ ",";
			}
			// 去除最后一个逗号
			String s = codes.substring(0, codes.length() - 1);
			sql.append(" and vmi.code IN (" + s + " )");
		}

		sql.append(" group by code ");
		if(condition.getRate()==null)
			condition.setRate(30);
		if (condition.getRate() != null) {
			sql.append(" having round((SUM(fullNum)-sum(num))/SUM(fullNum)*100 ) >= '"+condition.getRate()+"' ");
		}
		sql.append(" order by rate desc,code ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("补货管理列表查询sql语句>>>" + sql.toString());
		try {
			conn = openConnection();
			String sqlCount="select count(*) from ("+sql.toString()+") as a";
			log.info(sqlCount);
			pst = conn.prepareStatement(sqlCount);
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<MachinesReplenishBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				MachinesReplenishBean bean = new MachinesReplenishBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				//bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("code"));
				bean.setPrincipal(rs.getString("linkman"));
				bean.setLocatoinName(rs.getString("locatoinName"));
				bean.setRatio(rs.getInt("rate"));
				//bean.setLineId(rs.getInt("lineId"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setPageSize(condition.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 根据 登录所属公司 查询 线路以及线路下的收货机编号
	 */
	@Override
	public List<MachinesReplenishBean> getLine() {
		log.info("<MachinesReplenishDaoImpl>----<getLine>----start");
		List<MachinesReplenishBean> list = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select vl.id lineId,vl.name lineName,code from vending_line vl JOIN vending_machines_info vmi  on vl.id=vmi.lineId ");
		sql.append("WHERE vmi.companyId in" + companyDaoImpl.findAllSonCompanyIdForInSql(1));
		log.info("查询所有的机器sql:" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				MachinesReplenishBean bean = new MachinesReplenishBean();
				bean.setLineId(rs.getInt("lineId"));
				bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("code"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<MachinesReplenishDaoImpl>----<getLine>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
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

	/**
	 * 根据售货机编号 查询该售货机详细缺货信息 补货详情
	 */
	@Override
	public List<ReplenishmentDetailsBean> getDetails(String code) {
		log.info("<MachinesReplenishDaoImpl>----<getDetails>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT vmw.wayNumber,ib.name itemName,fullNum,num,(fullNum-num) replenishment  from vending_machines_way vmw  join vending_machines_item vmit  on vmw.itemId=vmit.id inner join item_basic ib on vmit.basicItemId=ib.id where  vmw.vendingMachinesCode='"
						+ code + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("详情sql语句》》》" + sql.toString());
		List<ReplenishmentDetailsBean> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ReplenishmentDetailsBean bean = new ReplenishmentDetailsBean();
				bean.setWayNumber(rs.getInt(1));
				bean.setItemName(rs.getString(2));
				bean.setAcceptableQuantity(rs.getInt(3));
				bean.setCurrentQuantity(rs.getInt(4));
				bean.setSuggestedReplenishment(rs.getInt(5));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql.toString());
			}
			log.info("<MachinesReplenishDaoImpl>----<getDetails>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					closeConnection(conn);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
    //查询版本2 的详情
	public List<ReplenishmentDetailsBean> getDetails1(String code) {
		log.info("<MachinesReplenishDaoImpl>----<getDetails>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT vmw.wayNumber,ib.name itemName, fullNum,num,(fullNum-num) AS replenishment FROM  vending_machines_info vmi INNER  JOIN vending_machines_way_item vmw ON vmi.code=vmw.vmCode LEFT JOIN item_basic ib ON vmw.basicItemId=ib.id where vmi.machineVersion=2 and vmi.code='"
						+ code + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("详情sql语句》》》" + sql.toString());
		List<ReplenishmentDetailsBean> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ReplenishmentDetailsBean bean = new ReplenishmentDetailsBean();
				bean.setWayNumber(rs.getInt(1));
				bean.setItemName(rs.getString(2));
				bean.setAcceptableQuantity(rs.getInt(3));
				bean.setCurrentQuantity(rs.getInt(4));
				bean.setSuggestedReplenishment(rs.getInt(5));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql.toString());
			}
			log.info("<MachinesReplenishDaoImpl>----<getDetails>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					closeConnection(conn);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据线路ID 查询这条线路上的售货机的缺货信息 补货预览
	 */
	@Override
	public List<ReplenishmentDetailsBean> getPreview(String lineId, String code,int version) {
		log.info("<MachinesReplenishDaoImpl>----<getPreview>----start");
		StringBuilder sql = new StringBuilder();
		if(version==1) {
			sql.append(
					"select sum(num),(sum(fullNum)-sum(num)),ib.name itemName,vl.name,ib.barCode,ib.unit,ib.id itemId FROM vending_line vl JOIN vending_machines_info vmi ON vl.id=vmi.lineId ");
			sql.append(
					"INNER JOIN vending_machines_way vmw ON vmi.code=vmw.vendingMachinesCode INNER JOIN vending_machines_item vmit ON vmw.itemId=vmit.id  INNER JOIN item_basic ib on vmit.basicItemId=ib.id  ");
		}else if(version==2) {
			sql.append(
					"select sum(num),(sum(fullNum)-sum(num)),ib.name itemName,vl.name,ib.barCode,ib.unit,ib.id itemId FROM vending_line vl JOIN vending_machines_info vmi ON vl.id=vmi.lineId ");
			sql.append(
					"INNER JOIN vending_machines_way_item vmw ON vmi.code=vmw.vmCode  INNER JOIN item_basic ib on vmw.basicItemId=ib.id  ");
		}
		
		sql.append("WHERE   vmi.code in (" + code + ")  GROUP BY ib.name ");//lineId in (" + lineId + ") and
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ReplenishmentDetailsBean> list = Lists.newArrayList();
		log.info("补货预览的sql语句>>>" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs != null) {
				ReplenishmentDetailsBean bean = new ReplenishmentDetailsBean();
				bean.setLineName(rs.getString(4));
				bean.setCurrentQuantity(rs.getInt(1));
				bean.setSuggestedReplenishment(rs.getInt(2));
				bean.setItemName(rs.getString(3));
				bean.setBarCode(rs.getString(5));
				bean.setItemId(rs.getLong(7));
				String nameByState = null;
				if (rs.getString(6) != null) {
					nameByState = stateInfoDaoImpl.getNameByState(rs.getLong(6));
				}
				bean.setUnitName(nameByState);
				list.add(bean);
			}
			if (showSql) {
				log.info(sql.toString());
			}
			log.info("<MachinesReplenishDaoImpl>----<getPreview>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//版本二补货详情
	public List<ReplenishDetailVo> selectReplenishDetail1(List<MachinesReplenishBean> list) {
		StringBuilder sb = new StringBuilder();
		// and w.vendingMachinesCode in('1988000087','1988000099')
		if (list.size() > 0)
			sb.append(" and w.vmCode in(");
		for (int i = 0; i < list.size(); i++) {
			sb.append("'").append(list.get(i).getCode()).append("'");
			if (i != list.size() - 1)
				sb.append(",");

		}
		if (sb.length() > 0)
			sb.append(")");
		String sql = "select w.vmCode as code,w.wayNumber,w.num,w.fullNum,b.id as itemId,b.name as itemName from vending_machines_way_item  w,item_basic b WHERE  w.basicItemId=b.id  "
				+ sb.toString();
		sql = sql + " order by code,wayNumber";
		if (showSql) {
			log.info(sql.toString());
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ReplenishDetailVo> listVo = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ReplenishDetailVo vo = new ReplenishDetailVo();
				vo.setCode(rs.getString("code"));
				vo.setFullNum(rs.getInt("fullNum"));
				vo.setItemId(rs.getLong("itemId"));
				vo.setItemName(rs.getString("itemName"));
				vo.setNum(rs.getInt("num"));
				vo.setWayNumber(rs.getInt("wayNumber"));
				listVo.add(vo);
			}
			log.info("<MachinesReplenishDaoImpl>----<getPreview>----end");
			return listVo;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return listVo;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	@Override
	public List<ReplenishDetailVo> selectReplenishDetail(List<MachinesReplenishBean> list) {
		StringBuilder sb = new StringBuilder();
		// and w.vendingMachinesCode in('1988000087','1988000099')
		if (list.size() > 0)
			sb.append(" and w.vendingMachinesCode in(");
		for (int i = 0; i < list.size(); i++) {
			sb.append("'").append(list.get(i).getCode()).append("'");
			if (i != list.size() - 1)
				sb.append(",");

		}
		if (sb.length() > 0)
			sb.append(")");
		String sql = "select w.vendingMachinesCode as code,w.wayNumber,w.num,w.fullNum,b.id as itemId,b.name as itemName from vending_machines_way  w,vending_machines_item i ,item_basic b where w.itemId=i.id and i.basicItemId=b.id "
				+ sb.toString();
		sql = sql + " order by code,wayNumber";
		if (showSql) {
			log.info(sql.toString());
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ReplenishDetailVo> listVo = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ReplenishDetailVo vo = new ReplenishDetailVo();
				vo.setCode(rs.getString("code"));
				vo.setFullNum(rs.getInt("fullNum"));
				vo.setItemId(rs.getLong("itemId"));
				vo.setItemName(rs.getString("itemName"));
				vo.setNum(rs.getInt("num"));
				vo.setWayNumber(rs.getInt("wayNumber"));
				listVo.add(vo);
			}
			log.info("<MachinesReplenishDaoImpl>----<getPreview>----end");
			return listVo;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return listVo;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

    @Override
    public List<MachinesReplenishBean> listDetailVm(String[] code,int version) {
		log.info("<MachinesReplenishDaoImpl>----<listDetailVm>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();

		if(version==1) {
			sql.append("select code,linkman,locatoinName,c.name companyName,vl.name lineName,vl.id lineId,round((SUM(fullNum)-sum(num))/SUM(fullNum)*100 ) rate from vending_machines_info vmi join company  c on vmi.companyId=c.id inner join vending_machines_way vmw on vmi.code=vmw.vendingMachinesCode ");
			sql.append("inner join vending_line  vl on vmi.lineId=vl.id where 1=1 and fullNUm>0 ");
		}else if(version==2) {
			sql.append("select code,linkman,locatoinName,c.name companyName,vl.name lineName,vl.id lineId,round((SUM(fullNum)-sum(num))/SUM(fullNum)*100 ) rate from vending_machines_info vmi join company  c on vmi.companyId=c.id inner join vending_machines_way_item vmw on vmi.code=vmw.vmCode ");
			sql.append("inner join vending_line  vl on vmi.lineId=vl.id where 1=1 and fullNUm>0 ");
		}
		
		// 如果传入了code值
		if (code!= null && code.length > 0) {
			String codes = "";
			for (String s : code) {
				codes = codes +s+ ",";
			}
			// 去除最后一个逗号
			String s = codes.substring(0, codes.length() - 1);
			sql.append(" and vmi.code IN (" + s + " )");
		}
		sql.append(" group by code ");
		sql.append(" order by rate desc,code ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询出勾选出来的售货机信息>>>" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<MachinesReplenishBean> list = Lists.newArrayList();
			int id = 0;
			while (rs.next()) {
				id++;
				MachinesReplenishBean bean = new MachinesReplenishBean();
				bean.setId(id);
				bean.setCompanyName(rs.getString("companyName"));
				bean.setLineName(rs.getString("lineName"));
				bean.setCode(rs.getString("code"));
				bean.setPrincipal(rs.getString("linkman"));
				bean.setLocatoinName(rs.getString("locatoinName"));
				bean.setRatio(rs.getInt("rate"));
				bean.setLineId(rs.getInt("lineId"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally {
			this.closeConnection(rs, pst, conn);
		}


    }
    public List<ItemNumVo> getItemsByCode(List<String> listCode,int version){
    	
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		if(version==1)
		   sql.append("SELECT w.vendingMachinesCode AS vmCode,w.wayNumber,w.num,w.fullNum,b.name,b.simpleName FROM vending_machines_way w LEFT JOIN vending_machines_item i ON w.itemId=i.id LEFT JOIN item_basic b ON i.basicItemId=b.id ");
		else if(version==2)
			sql.append("SELECT w.vmCode AS vmCode,w.wayNumber,w.num,w.fullNum,b.name,b.simpleName FROM vending_machines_way_item w LEFT JOIN item_basic b ON w.basicItemId=b.id ");

		if(listCode.size()>0){
			StringBuilder insql=new StringBuilder();
			for(int i=0;i<listCode.size();i++){
				insql.append("'").append(listCode.get(i)).append("'");
				if(i!=listCode.size()-1){
					insql.append(",");
				}
			}
			if(version==1)
			  sql.append(" where  w.vendingMachinesCode in ("+insql.toString()+")");
			else
				sql.append(" where  w.vmCode in ("+insql.toString()+")");
		}
		sql.append(" order by w.wayNumber ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询>>>" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<ItemNumVo> list = Lists.newArrayList();
			while (rs.next()) {
				ItemNumVo bean = new ItemNumVo();
				bean.setFullNum(rs.getInt("fullNum"));
				bean.setNum(rs.getInt("num"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setWayNumber(rs.getInt("wayNumber"));
				bean.setItemName(rs.getString("name"));
				//bean.setSimpleName(rs.getString("simpleName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally {
			this.closeConnection(rs, pst, conn);
		}

    	
    	
    }

	/**
	 * 查询有故障的机器
	 * @return
	 */
	public Map<String,String> listAllErrorMachine(){
		StringBuilder sql = new StringBuilder();
		DateTime now = new DateTime();
		now=now.minusHours(1);
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sql.append("SELECT code,info FROM tbl_distorymachine_log WHERE createTime>='"+sf.format(now.toDate())+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询>>>" + sql.toString());
		Map<String,String> map= Maps.newHashMap();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
               map.put(rs.getString("code"),rs.getString("info"));
			}
			if (showSql) {
				log.info(sql);
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return map;
		} finally {
			this.closeConnection(rs, pst, conn);
		}

	}

	public static void main(String[] args) {
		DateTime now = new DateTime();
		now=now.minusHours(1);
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sf.format(now.toDate()));
	}
}
