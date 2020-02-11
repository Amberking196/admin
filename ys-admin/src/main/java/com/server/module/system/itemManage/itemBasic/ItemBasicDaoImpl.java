package com.server.module.system.itemManage.itemBasic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesBean;
import com.server.module.system.machineManage.machinesAdvertising.VendingMachinesAdvertisingBean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.baseManager.stateInfo.StateInfoDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-04-10 14:22:54
 */
@Repository
public class ItemBasicDaoImpl extends BaseDao<ItemBasicBean> implements ItemBasicDao {

	public static Logger log = LogManager.getLogger(ItemBasicDaoImpl.class);
	@SuppressWarnings("unused")
	@Autowired
	private StateInfoDao stateInfoDaoImpl;
	@Autowired
	private CompanyDao companyDao;

	/**
	 * 查询商品基础信息列表
	 * 
	 * @return
	 */
	@SuppressWarnings("resource")
	@Override
	public ReturnDataUtil listPage(ItemBasicCondition condition) {
		log.info("<ItemBasicDaoImpl>--<listPage>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		Integer companyId=UserUtils.getUser().getCompanyId();
		String insql=companyDao.findAllSonCompanyIdForInSql1(companyId);
		sql.append(
				"select i.id,i.name,i.barCode,i.unit,i.pic,item_type.name,i.state,i.brand,i.standard,i.pack,i.purchaseWay,i.createTime,i.typeId,i.simpleName,i.companyId,i.companyName,i.wayCapacity,i.extraName from item_basic i left join item_type on   i.typeId=item_type.id  where 1=1  and i.companyId in "+insql+" ");
		if (condition.getId() != null) {
			sql.append(" and i.id ='" + condition.getId() + "' ");
		}
		if (StringUtil.isNotBlank(condition.getName())) {
			sql.append(" and i.name like '%" + condition.getName() + "%'");
		}
		if (StringUtil.isNotBlank(condition.getBarCode())) {
			sql.append(" and i.barCode = '" + condition.getBarCode() + "'");
		}
		if (condition.getTypeId() != null) {
			sql.append(" and item_type.id = '" + condition.getTypeId() + "'");
		}
		if (condition.getState() != null) {
			sql.append(" and i.state = '" + condition.getState() + "'");
		}
		if (condition.getStartDate() != null) {
			sql.append(" and i.createTime >= '" + DateUtil.formatYYYYMMDDHHMMSS(condition.getStartDate()) + "'");
		}
		if (condition.getEndDate() != null) {
			sql.append(" and i.createTime < '" + DateUtil.formatLocalYYYYMMDDHHMMSS(condition.getEndDate(), 1) + "'");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("<ItemBasicDaoImpl>--<listPage>--sql" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			sql.append(" ORDER BY i.createTime desc, i.id desc ");
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<ItemBasicBean> list = Lists.newArrayList();
			while (rs.next()) {
				ItemBasicBean bean = new ItemBasicBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setBarCode(rs.getString(3));
				bean.setUnit(rs.getLong(4));
				// 通过查询出来的商品单位的 unit 去数据字典得到 商品单位的名称
				String unitName = stateInfoDaoImpl.getNameByState(rs.getLong(4));
				bean.setUnitName(unitName);
				bean.setPic(rs.getString(5));
				bean.setType(rs.getString(6));
				bean.setState(rs.getLong(7));
				// 通过查询出来的商品状态的 编号 state 去数据字典得到 商品状态的名称
				String nameByState = stateInfoDaoImpl.getNameByState(rs.getLong(7));
				bean.setStateName(nameByState);
				bean.setBrand(rs.getString(8));
				bean.setStandard(rs.getString(9));
				bean.setPack(rs.getString(10));
				bean.setPurchaseWay(rs.getString(11));
				bean.setCreateTime(rs.getDate(12));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setSimpleName(rs.getString("simpleName"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setWayCapacity(rs.getInt("wayCapacity"));
				bean.setExtraName(rs.getString("extraName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ItemBasicDaoImpl>--<listPage>--end");
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

	/**
	 * 添加商品
	 */
	@Override
	public ItemBasicBean insert(ItemBasicBean bean) {
		log.info("<ItemBasicDaoImpl>--<insert>--start");
		ItemBasicBean re = super.insert(bean);
		log.info("<ItemBasicDaoImpl>--<insert>--end");
		return re;
	}

	/**
	 * 查询商品基础信息 for 我的商品界面
	 */
	public ReturnDataUtil myListPage(MyItemCondition condition) {
		log.info("<ItemBasicDaoImpl>--<myListPage>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id as basicItemId,name,standard,pic,wayCapacity from item_basic where state=5100  and typeId=11  ");
		List<Object> plist = Lists.newArrayList();
		if (StringUtil.isNotBlank(condition.getName())) {
			sql.append(" and name like ? ");
			plist.add("%" + condition.getName() + "%");
		}
		sql.append("  order by orderId desc ,id desc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			System.out.println("sql与句句句：" + sql.toString() + " limit " + off + "," + condition.getPageSize());
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<MyItemDto> list = Lists.newArrayList();
			while (rs.next()) {

				MyItemDto bean = new MyItemDto();
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setPic(rs.getString("pic"));
				bean.setName(rs.getString("name"));
				bean.setStandard(rs.getString("standard"));
				bean.setWayCapacity(rs.getInt("wayCapacity"));
				list.add(bean);

			}
			/*
			 * 禁用！！！！！ if (showSql) { log.info(sql); log.info(plist.toString()); }
			 */
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ItemBasicDaoImpl>--<myListPage>--end");
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

	/**
	 * 查询我公司及旗下所有子公司的商品
	 */
	@SuppressWarnings("resource")
	@Override
	public ReturnDataUtil myOwnListPage(MyItemCondition condition, String companyIds) {
		log.info("<ItemBasicDaoImpl>--<myOwnListPage>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select a.basicItemId,b.name,b.standard,b.pic,b.wayCapacity,a.price,costPrice,a.id as itemId,a.hot,a.endTime "
				+ " from vending_machines_item a "
				+ " left join item_basic b on a.basicItemId=b.id where  b.state=5100 and a.companyId in " + companyIds);
		List<Object> plist = Lists.newArrayList();
		if (StringUtil.isNotBlank(condition.getName())) {
			sql.append(" and name like ? ");
			plist.add("%" + condition.getName() + "%");
		}
		/*
		 * 禁用！！！！ if (showSql) { log.info(sql); log.info(plist.toString()); }
		 */
		sql.append(" order by orderId desc ");
		log.info(sql.toString() + "****");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<MyItemDto> list = Lists.newArrayList();
			while (rs.next()) {

				MyItemDto bean = new MyItemDto();
				bean.setBasicItemId(rs.getLong("basicItemId"));
				bean.setPic(rs.getString("pic"));
				bean.setName(rs.getString("name"));
				bean.setStandard(rs.getString("standard"));
				bean.setItemId(rs.getLong("itemId"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setHot(rs.getInt("hot"));
				bean.setEndTime(rs.getDate("endTime"));
				bean.setWayCapacity(rs.getInt("wayCapacity"));
				list.add(bean);
			}

			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ItemBasicDaoImpl>--<myOwnListPage>--end");
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

	/**
	 * 通过商品ID 得到商品信息
	 */
	@Override
	public ItemBasicBean getItemBasic(Object id) {
		log.info("<ItemBasicDaoImpl>--<getItemBasic>--start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select i.id,i.name,barCode,unit,brand,item_type.name,i.state,standard,pack,purchaseWay from item_basic i,item_type ");
		sql.append("where i.typeId=item_type.id and i.id=" + id + "");
		log.info("<ItemBasicDaoImpl>--<getItemBasic>--sql" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			ItemBasicBean bean = new ItemBasicBean();
			while (rs.next()) {
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setBarCode(rs.getString(3));
				// 通过查询出来的商品单位的 unit 去数据字典得到 商品单位的名称
				String unitName = stateInfoDaoImpl.getNameByState(Long.parseLong(rs.getString(4)));
				bean.setUnitName(unitName);
				bean.setStandard(rs.getString("standard"));
				bean.setType(rs.getString(6));
				// 通过查询出来的商品状态的 编号 state 去数据字典得到 商品状态的名称
				String nameByState = stateInfoDaoImpl.getNameByState(Long.parseLong(rs.getString(7)));
				bean.setStateName(nameByState);
				bean.setBrand(rs.getString(8));
				bean.setPack(rs.getString(9));
				bean.setPurchaseWay(rs.getString(10));
			}
			log.info("<ItemBasicDaoImpl>--<getItemBasic>--end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("<ItemBasicDaoImpl>--<getItemBasic>--SQLException" + e.getMessage());
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修改商品信息
	 */
	@Override
	public boolean updateEntity(ItemBasicBean entity) {
		// TODO Auto-generated method stub
		log.info("<ItemBasicDaoImpl>--<updateEntity>--start");
		boolean re = super.update(entity);
		log.info("<ItemBasicDaoImpl>--<updateEntity>--end");
		return re;
	}

	/**
	 * 判断商品条形码是否存在
	 */
	@Override
	public ItemBasicBean checkBarcode(String barCode) {
		log.info("<ItemBasicDaoImpl>--<checkBarcode>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,name,barCode from item_basic where barCode='" + barCode + "'");
		log.info("<ItemBasicDaoImpl>--<checkBarcode>--sql:" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			ItemBasicBean bean = new ItemBasicBean();
			while (rs.next()) {
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setBarCode(rs.getString(3));
			}
			log.info("<ItemBasicDaoImpl>--<checkBarcode>--end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
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
	 * 查询图片路径 进行下载
	 */
	@Override
	public List<String> getPic() {
		log.info("<ItemBasicDaoImpl>--<getPic>--start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select pic from item_basic ");
		log.info(" sql语句" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<String> list = Lists.newArrayList();
			while (rs.next()) {
				list.add(rs.getString("pic"));
			}
			log.info("<ItemBasicDaoImpl>--<getPic>--start");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
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
	 * 根据条形码 查询商品信息
	 */
	@Override
	public List<ItemBasicBean> findItemBasic(ItemBasicCondition condition) {
		log.info("<ItemBasicDaoImpl>--<findItemBasic>--start");
		StringBuilder sql = new StringBuilder();
		List<ItemBasicBean> list = Lists.newArrayList();
		// 入库
		if (condition.getOutPut() == 0) {
			sql.append("select id,name,unit,barCode from item_basic WHERE barCode like '%" + condition.getBarCode()
					+ "%' ");
		}
		// 出库
		if (condition.getOutPut() == 1) {
			sql.append(" select  ib.id,ib.name,unit,ib.barCode,ws.costPrice price,ws.warehouseId,ws.quantity from item_basic  ib  ");
			sql.append(" inner  join warehouse_stock ws on ib.id=ws.itemId  ");
			sql.append(" WHERE ws.warehouseId="+condition.getWarehouseId()+" and ib.barCode like '%"+ condition.getBarCode() + "%'");
		}
		log.info("<ItemBasicDaoImpl>--<findItemBasic>--sql" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ItemBasicBean bean = new ItemBasicBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setBarCode(rs.getString("barCode"));
				// 通过查询出来的商品单位的 unit 去数据字典得到 商品单位的名称
				String unitName = stateInfoDaoImpl.getNameByState(Long.parseLong(rs.getString("unit")));
				if (unitName != null) {
					bean.setUnitName(unitName);
				}
				if (condition.getOutPut() == 1) {

					bean.setPrice(rs.getDouble("price"));
					bean.setNumber(rs.getInt("quantity"));
				}
				list.add(bean);
			}
			log.info("<ItemBasicDaoImpl>--<findItemBasic>--end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("<ItemBasicDaoImpl>--<findItemBasic>--SQLException" + e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 查询所有商品 下拉框用
	 * 
	 * @return
	 */
	public List<Map<String, Object>> listAllItem() {
		String sql = "SELECT id,name FROM item_basic WHERE typeId=11";

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = Lists.newArrayList();
		log.info(sql);
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", rs.getLong("id"));
				map.put("name", rs.getString("name"));
				list.add(map);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return list;

	}

	/**
	 * 根据商品名 模糊查询
	 */
	@Override
	public List<ItemBasicBean> getItemBasic(ItemBasicCondition condition) {
		log.info("<ItemBasicDaoImpl>--<getItemBasic>--start");
		StringBuilder sql = new StringBuilder();
		List<ItemBasicBean> list = Lists.newArrayList();
		if (condition.getOutPut() == 0) {
			sql.append("select id,name,unit,barCode from item_basic WHERE name like '%" + condition.getName() + "%' ");
		}
		if (condition.getOutPut() == 1) {
			sql.append(" select  ib.id,ib.name,unit,ib.barCode,ws.costPrice price,ws.warehouseId,ws.quantity from item_basic  ib ");
			sql.append(" inner  join warehouse_stock ws on ib.id=ws.itemId ");
			sql.append(" WHERE ws.warehouseId="+condition.getWarehouseId()+" and ib.name like '%"+ condition.getName() + "%'");
		}
		log.info("<ItemBasicDaoImpl>--<getItemBasic>--sql" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ItemBasicBean bean = new ItemBasicBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setBarCode(rs.getString("barCode"));
				// 通过查询出来的商品单位的 unit 去数据字典得到 商品单位的名称
				String unitName = stateInfoDaoImpl.getNameByState(Long.parseLong(rs.getString("unit")));
				if (unitName != null) {
					bean.setUnitName(unitName);
				}
				if (condition.getOutPut() == 1) {
					bean.setPrice(rs.getDouble("price"));
					bean.setNumber(rs.getInt("quantity"));
				}
				list.add(bean);
			}
			log.info("<ItemBasicDaoImpl>--<getItemBasic>--end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("<ItemBasicDaoImpl>--<getItemBasic>--SQLException" + e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<ItemBasicBean> findItemBasicByCode(ItemBasicCondition condition) {
		log.info("<ItemBasicDaoImpl>--<findItemBasicByCode>--start");
		StringBuilder sql = new StringBuilder();
		List<ItemBasicBean> list = Lists.newArrayList();
			sql.append("select id,name,unit,barCode from item_basic WHERE barCode like '%" + condition.getBarCode()
					+ "%' ");
		log.info("<ItemBasicDaoImpl>--<findItemBasic>--sql" + sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ItemBasicBean bean = new ItemBasicBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setBarCode(rs.getString("barCode"));
				// 通过查询出来的商品单位的 unit 去数据字典得到 商品单位的名称
				String unitName = stateInfoDaoImpl.getNameByState(Long.parseLong(rs.getString("unit")));
				if (unitName != null) {
					bean.setUnitName(unitName);
				}
				list.add(bean);
			}
			log.info("<ItemBasicDaoImpl>--<findItemBasicByCode>--end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("<ItemBasicDaoImpl>--<findItemBasicByCode>--SQLException" + e.getMessage());
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public List<ItemBasicBean> getItemConnect(ItemBasicCondition condition) {
		log.info("<StateInfoDaoImpl>--<getItemConnect>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from item_basic where id ");
		
		if (condition.getIsBind()==1) {
			sql.append(" in( ");
		}else {
			sql.append(" not in( ");
		}
		sql.append(" SELECT( ");
		sql.append(" CASE ");
		sql.append(" WHEN firstBasicItemId = '"+condition.getId()+"' THEN SecondBasicItemId ");
		sql.append(" when SecondBasicItemId = '"+condition.getId()+"' THEN firstBasicItemId else 0.1 END ) FROM item_basic_vision ibv)");
		if (StringUtils.isNotBlank(condition.getName())) {
			sql.append(" and name like '%"+condition.getName()+"%'");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ItemBasicBean> list = Lists.newArrayList();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				ItemBasicBean itemBasicBean = new ItemBasicBean();
				itemBasicBean.setId(rs.getLong("id"));
				itemBasicBean.setName(rs.getString("name"));
				list.add(itemBasicBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getItemConnect>--end");
		return list;
	}
	
	@Override
    public void addAll(Integer basicItemId,List<Integer> basicItemIds) {
	    for (int i = 0; i < basicItemIds.size(); i++) {
	    	ItemBasicVisionBean  ivb = new ItemBasicVisionBean();
	    	ivb.setFirstBasicItemId(basicItemId);
	    	ivb.setSecondBasicItemId(basicItemIds.get(i));
	    	StringBuffer sql = new StringBuffer();
			sql.append("insert into  item_basic_vision (firstBasicItemId,secondBasicItemId) values("+ivb.getFirstBasicItemId()+","+ivb.getSecondBasicItemId()+")");
			
			log.info("sql语句："+sql);
			Connection conn = null;
			PreparedStatement ps = null;
			int rs = 0;
			try {
				conn = openConnection();
				ps = conn.prepareStatement(sql.toString());
				rs = ps.executeUpdate();
				if(rs>0){
					log.info("addAllTrue");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.closeConnection(null, ps, conn);
			}
        }
	}

	@Override
    public void delAll(Integer basicItemId,List<Integer> ids) {
	    for (int i = 0; i < ids.size(); i++) {
	    	StringBuffer sql = new StringBuffer();
			sql.append("delete from   item_basic_vision where ");
			sql.append(" (firstBasicItemId = "+basicItemId+" and secondBasicItemId = "+ids.get(i)+") ");
			sql.append(" OR ");
			sql.append(" (secondBasicItemId = "+basicItemId+" and firstBasicItemId = "+ids.get(i)+") ");
			log.info("sql语句："+sql);
			Connection conn = null;
			PreparedStatement ps = null;
			int rs = 0;
			try {
				conn = openConnection();
				ps = conn.prepareStatement(sql.toString());
				rs = ps.executeUpdate();
				if(rs>0){
					log.info("addAllTrue");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.closeConnection(null, ps, conn);
			}
        }
	}
}
