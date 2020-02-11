package com.server.module.customer.product;

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
import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.couponManager.coupon.CouponForm;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;


/**
 * author name: why create time: 2018-06-29 11:17:55
 */
@Repository
public class ShoppingGoodsDaoImpl extends BaseDao<ShoppingGoodsBean> implements ShoppingGoodsDao {

	private static Logger log = LogManager.getLogger(ShoppingGoodsDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;
	
	
	/**
	 * 后台查询商城商品列表
	 */
	@Override
	public ReturnDataUtil listPage(ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsDaoImpl>-----<listPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select sg.activityId,sg.id,sg.name,companyId,typeId,pic,barCode,sg.state,purchaseWay,brand,standard,unit,pack,details,sg.createTime,createUser,sg.updateTime,updateUser,isHelpOneself,");
		sql.append(" deleteFlag,quantity,purchaseNotes,commodityParameters,isHomeShow,it.name as typeName,c.name  as companyName,s.name as stateName ,s1.name as unitName,costPrice,salesPrice,preferentialPrice,basicItemId,advertisingPic,target,vmCode,sg.areaId,sg.areaName");
		sql.append(" from shopping_goods  sg left join item_type it on sg.typeId=it.id ");
		sql.append(" left join company c on sg.companyId=c.id left join state_info s on sg.state=s.state ");
		sql.append(" left join state_info s1 on sg.unit=s1.state ");
		sql.append(" where sg.deleteFlag=0 ");
		if (StringUtil.isNotBlank(shoppingGoodsForm.getName())) {
			sql.append(" and sg.name like '%" + shoppingGoodsForm.getName() + "%'");
		}
		if(shoppingGoodsForm.getState()!=null) {
			sql.append(" and sg.state='"+shoppingGoodsForm.getState()+"' ");
		}
		if(shoppingGoodsForm.getItemType()!=null) {
			sql.append(" and sg.activityId='"+shoppingGoodsForm.getItemType()+"' ");
		}
		if(shoppingGoodsForm.getIsHomeShow()!=null) {
			sql.append(" and sg.isHomeShow='"+shoppingGoodsForm.getIsHomeShow()+"' ");
		}
		if(shoppingGoodsForm.getCompanyId()!=null) {
			sql.append(" and sg.companyId='"+shoppingGoodsForm.getCompanyId()+"' ");
		}
		//对提水券等进行过滤
		if(shoppingGoodsForm.getTypeId()!=null) {
			sql.append(" and typeId != 25 and typeId != 26 and typeId !=27 ");
		}
		//权限控制
		sql.append(" and (target = 1 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 2 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 0 ");
		sql.append(" or sg.target = 3 and sg.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
	
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("商城商品查询列表sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			sql.append(" order by sg.createTime desc ");//排序
			long off = (shoppingGoodsForm.getCurrentPage() - 1) * shoppingGoodsForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + shoppingGoodsForm.getPageSize());
			rs = pst.executeQuery();
			List<ShoppingGoodsBean> list = Lists.newArrayList();
			int number = 0;
			while (rs.next()) {
				number++;
				ShoppingGoodsBean bean = new ShoppingGoodsBean();
				bean.setNumber(number);
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setStateName(rs.getString("stateName"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setTypeName(rs.getString("typeName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				String basicItemId = rs.getString("basicItemId");
				if (basicItemId != null) {
					bean.setIsRelevance(0);
					bean.setBasicItemId(rs.getLong("basicItemId"));
				} else {
					bean.setIsRelevance(1);
				}
				bean.setIsHomeShow(rs.getInt("isHomeShow"));
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setTarget(rs.getInt("target"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setActivityId(rs.getInt("activityId"));
				list.add(bean);
			}
			data.setCurrentPage(shoppingGoodsForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsDaoImpl>-----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<listPage>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 商城商品详情查看
	 */
	@Override
	public ShoppingGoodsBean get(Object id) {
		log.info("<ShoppingGoodsDaoImpl>-----<get>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select sg.activityId,sg.id,sg.name,companyId,typeId,pic,barCode,sg.state,purchaseWay,brand,standard,unit,pack,details,sg.createTime,createUser,sg.updateTime,updateUser,purchaseLimitation,");
		sql.append(
				" deleteFlag,quantity,purchaseNotes,commodityParameters,isHomeShow,vouchersId,it.name as typeName,c.name  as companyName,s.name as stateName ,s1.name as unitName,costPrice,salesPrice,preferentialPrice,basicItemId,advertisingPic,target,vmCode,sg.areaId,sg.areaName,sg.isHelpOneself ");
		sql.append(" ,sg.vouchersId,sg.vouchersIds from shopping_goods  sg left join item_type it on sg.typeId=it.id ");
		sql.append(" left join company c on sg.companyId=c.id left join state_info s on sg.state=s.state ");
		sql.append(" left join state_info s1 on sg.unit=s1.state ");
		sql.append(" where sg.id ='" + id + "'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ShoppingGoodsBean bean = null;
		log.info("商品详情查看sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new ShoppingGoodsBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setStateName(rs.getString("stateName"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setTypeName(rs.getString("typeName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				bean.setTarget(rs.getInt("target"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setVouchersId(rs.getLong("vouchersId"));
				bean.setVouchersIds(rs.getString("vouchersIds"));
				bean.setActivityId(rs.getInt("activityId"));
				bean.setPurchaseLimitation(rs.getInt("purchaseLimitation"));
				String basicItemId = rs.getString("basicItemId");
				if (basicItemId != null) {
					bean.setIsRelevance(0);
					bean.setBasicItemId(rs.getLong("basicItemId"));
				} else {
					bean.setIsRelevance(1);
				}
				bean.setIsHomeShow(rs.getInt("isHomeShow"));
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setIsConglomerateCommodity(0);
			}
			log.info("<ShoppingGoodsDaoImpl>-----<get>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<get>----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 删除商城商品
	 */
	@Override
	public boolean delete(Object id) {
		log.info("<ShoppingGoodsDaoImpl>-----<delete>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update shopping_goods set deleteFlag=1 where id in (" + id + ") ");
		Connection conn = null;
		PreparedStatement pst = null;
		log.info("删除商城商品》sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<ShoppingGoodsDaoImpl>-----<delete>----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(null, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<delete>----end");
		return false;
	}

	@Override
	public boolean update(ShoppingGoodsBean entity) {
		return super.update(entity);
	}

	@Override
	public ShoppingGoodsBean insert(ShoppingGoodsBean entity) {
		return super.insert(entity);
	}

	/**
	 * 判断商品名称是否存在
	 */
	@Override
	public boolean checkName(String sql) {
		log.info("<ShoppingGoodsDaoImpl>-----<checkName>----start");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("判断商城商品名称》sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs != null) {
				log.info("<ShoppingGoodsDaoImpl>-----<checkName>----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<checkName>----end");
		return false;
	}

	/**
	 * 判断商城商品条形码 是否存在
	 */
	@Override
	public boolean checkBarcode(String sql) {
		log.info("<ShoppingGoodsDaoImpl>-----<checkBarcode>----start");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("判断商城商品条形码 》sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next() && rs != null) {
				log.info("<ShoppingGoodsDaoImpl>-----<checkBarcode>----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<checkBarcode>----end");
		return false;
	}

	/**
	 * 查询商品信息 给手机端显示  首页展示
	 */
	@Override
	public List<ShoppingGoodsBean> list(ShoppingGoodsForm shoppingGoodsForm,VendingMachinesInfoBean vendingMachinesInfoBean) {
		log.info("<ShoppingGoodsDaoImpl>-----<list>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select sg.id,sg.name,sg.companyId,sg.typeId,sg.pic,sg.barCode,sg.state,sg.purchaseWay,sg.brand,sg.standard,sg.unit,sg.pack,sg.details,sg.createTime, ");
		sql.append(" sg.createUser,sg.updateTime,sg.updateUser,sg.deleteFlag,sg.costPrice,sg.salesPrice,sg.preferentialPrice,sg.basicItemId,sg.quantity,sg.purchaseNotes, ");
		sql.append(" sg.commodityParameters,sg.advertisingPic,sg.isHelpOneself,sg.activityId,sgs.id sId,sgs.theme,sgs.spellGroupPrice ");
		sql.append(" from shopping_goods sg left join shopping_goods_spellgroup sgs on sg.id=sgs.goodsId and endTime>now() and sgs.deleteFlag=0 where 1=1 and sg.deleteFlag=0  ");
		if(vendingMachinesInfoBean==null) {
			sql.append(" and target = 0");
		}else{
			sql.append(" and (target = 1 and FIND_IN_SET("+vendingMachinesInfoBean.getCompanyId()+",getChildList(companyId)) ");
			sql.append(" or target = 2 and FIND_IN_SET("+vendingMachinesInfoBean.getCompanyId()+",getChildList(companyId))  and areaId='"+vendingMachinesInfoBean.getAreaId()+"'  ");
			sql.append(" or target = 3 and  FIND_IN_SET("+vendingMachinesInfoBean.getCode()+",sg.vmCode)");
			sql.append(" or target = 0 )");
		}
		if (shoppingGoodsForm.getType() != null) {
			// 首页商品 
			if (shoppingGoodsForm.getType() == 1) {
				sql.append(" and state=5100 and (isHomeShow=0 or isHomeShow=4) ");
			}
			// 广告商品 
			if (shoppingGoodsForm.getType() == 2) {
				sql.append(" and state=5100 and isHomeShow=1 ");
			}
			// 全部商品
			if (shoppingGoodsForm.getType() == 3) {
				sql.append(" and state=5100   ");
			}
			//商城结算页显示商品
			if(shoppingGoodsForm.getType() == 4) {
				sql.append(" and sg.state=5100 and (isHomeShow=3 or isHomeShow=4) ");
			}
		}
		//商品活动类型 0拼团 1普通 
		if(shoppingGoodsForm.getItemType()!=null) {
			sql.append(" and sg.state=5100 and sg.activityId="+shoppingGoodsForm.getItemType()+" ");
			
		}
		sql.append(" order by sg.activityId asc, sg.createTime desc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ShoppingGoodsBean> list=new ArrayList<ShoppingGoodsBean>();
		log.info("商城商品 <手机端 >查询列表sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ShoppingGoodsBean  bean=new ShoppingGoodsBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				Long basicItemId = rs.getLong("basicItemId");
				if (basicItemId != null) {
					bean.setBasicItemId(rs.getLong("basicItemId"));
				}
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setSpellgroupId(rs.getLong("sId"));
				bean.setSpellgroupName(rs.getString("theme"));
				bean.setGroupPurchasePrice(rs.getBigDecimal("spellGroupPrice"));
				bean.setActivityId(rs.getInt("activityId"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<list>----end");
		return list;
	}

	/**
	 * 查询珠海华发商品信息 给手机端显示  首页展示
	 */
	@Override
	public List<ShoppingGoodsBean> huaFaAppList(ShoppingGoodsForm shoppingGoodsForm,VendingMachinesInfoBean vendingMachinesInfoBean) {
		log.info("<ShoppingGoodsDaoImpl>-----<huaFaAppList>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select sg.id,sg.name,sg.companyId,sg.typeId,sg.pic,sg.barCode,sg.state,sg.purchaseWay,sg.brand,sg.standard,sg.unit,sg.pack,sg.details,sg.createTime, ");
		sql.append(" sg.createUser,sg.updateTime,sg.updateUser,sg.deleteFlag,sg.costPrice,sg.salesPrice,sg.preferentialPrice,sg.basicItemId,sg.quantity,sg.purchaseNotes, ");
		sql.append(" sg.commodityParameters,sg.advertisingPic,sg.isHelpOneself,sg.activityId,sgs.id sId,sgs.theme,sgs.spellGroupPrice ");
		sql.append(" from shopping_goods sg left join shopping_goods_spellgroup sgs on sg.id=sgs.goodsId and endTime>now() and sgs.deleteFlag=0 where 1=1 and sg.deleteFlag=0  ");
		if(vendingMachinesInfoBean==null) {
			sql.append(" and target = 0");
		}else{
			sql.append(" and (target = 1 and FIND_IN_SET(84,getChildList(companyId)) ");
			sql.append("OR FIND_IN_SET(118, getChildList(companyId))");
			sql.append("OR FIND_IN_SET(131, getChildList(companyId))");
			sql.append(" or target = 2 and FIND_IN_SET("+vendingMachinesInfoBean.getCompanyId()+",getChildList(companyId))  and areaId='"+vendingMachinesInfoBean.getAreaId()+"'  ");
			sql.append(" or target = 3 and  FIND_IN_SET("+vendingMachinesInfoBean.getCode()+",sg.vmCode)");
			sql.append(" or target = 0 )");
		}
		if (shoppingGoodsForm.getType() != null) {
			// 首页商品
			if (shoppingGoodsForm.getType() == 1) {
				sql.append(" and state=5100 and (isHomeShow=0 or isHomeShow=4) ");
			}
			// 广告商品
			if (shoppingGoodsForm.getType() == 2) {
				sql.append(" and state=5100 and isHomeShow=1 ");
			}
			// 全部商品
			if (shoppingGoodsForm.getType() == 3) {
				sql.append(" and state=5100   ");
			}
			//商城结算页显示商品
			if(shoppingGoodsForm.getType() == 4) {
				sql.append(" and sg.state=5100 and (isHomeShow=3 or isHomeShow=4) ");
			}
		}
		//商品活动类型 0拼团 1普通
		if(shoppingGoodsForm.getItemType()!=null) {
			sql.append(" and sg.state=5100 and sg.activityId="+shoppingGoodsForm.getItemType()+" ");

		}
		sql.append("and ((sg.companyId = 83) or (sg.companyId = 84) or (sg.companyId = 131) or (sg.companyId = 118))");
		sql.append(" order by sg.activityId asc, sg.createTime desc  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<ShoppingGoodsBean> list=new ArrayList<ShoppingGoodsBean>();
		log.info("商城商品 <手机端 >查询列表sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ShoppingGoodsBean  bean=new ShoppingGoodsBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				Long basicItemId = rs.getLong("basicItemId");
				if (basicItemId != null) {
					bean.setBasicItemId(rs.getLong("basicItemId"));
				}
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setSpellgroupId(rs.getLong("sId"));
				bean.setSpellgroupName(rs.getString("theme"));
				bean.setGroupPurchasePrice(rs.getBigDecimal("spellGroupPrice"));
				bean.setActivityId(rs.getInt("activityId"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<huaFaAppList>----end");
		return list;
	}

	/**
	 * 查询售货机货道商品 下拉框 展示
	 */
	@Override
	public ReturnDataUtil itemBasicListPage() {
		log.info("<ShoppingGoodsDaoImpl>-----<itemBasicListPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select ib.id,ib.name,vmi.companyId from vending_machines_way vmw inner join vending_machines_info vm on vmw.vendingMachinesCode=vm.code ");
		sql.append(
				" inner join vending_machines_item vmi on vmw.itemId=vmi.id inner join item_basic ib on vmi.basicItemId=ib.id where ib.state=5100  ");
		sql.append(" and vm.state=20001 and vmi.companyId in "
				+ companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" group by ib.id  ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询售货机货道商品 下拉框 展示 sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<ShoppingGoodsBean> list = Lists.newArrayList();
			int number = 0;
			while (rs.next()) {
				number++;
				ShoppingGoodsBean bean = new ShoppingGoodsBean();
				bean.setNumber(number);
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsDaoImpl>-----<itemBasicListPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<itemBasicListPage>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 商品详情 给手机端显示
	 */
	@Override
	public ShoppingGoodsBean productDetails(Long goodsId,Long spellgroupId) {
		log.info("<ShoppingGoodsDaoImpl>-----<get>----start");
		ShoppingGoodsBean shoppingGoodsBean=null;
		if(spellgroupId!=null && spellgroupId>0) {//团购商品
			shoppingGoodsBean= phoneProductDetails(goodsId,spellgroupId);
		}else {//普通商品
			shoppingGoodsBean = get(goodsId);
		}
		StringBuilder sql = new StringBuilder();
		if(shoppingGoodsBean.getIsConglomerateCommodity()==1) {
			sql.append(" select count(1) salesQuantity,count(1) purchaseTime from tbl_customer_spellgroup where state=1 and goodsId='" + goodsId + "'  "); 
		}else {
		sql.append(" select sum(sod.num) salesQuantity ,count(sod.itemId) purchaseTime  ");
		sql.append(" from shopping_goods sg left JOIN store_order_detile sod on sg.id = sod.itemId ");
		sql.append("  left join store_order so on sod.orderId = so.id ");
		sql.append(" where so.state = '10001' and sg.id='" + goodsId + "' ");
		sql.append(" group by sg.id ");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("商品详情查看购买人数sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				//销售数量
				shoppingGoodsBean.setSalesQuantity(rs.getLong("salesQuantity"));
				//购买人数
				shoppingGoodsBean.setPurchaseTime(rs.getLong("purchaseTime"));
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<ShoppingGoodsDaoImpl>-----<productDetails>----end");
			return shoppingGoodsBean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<productDetails>----end");
			return shoppingGoodsBean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 增加图片素材
	 */
	@Override
	public boolean addPictureMaterial(PictureMaterialBean bean) {
		log.info("<ShoppingGoodsDaoImpl>-----<addPictureMaterial>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into picture_material(picName,createUser)  values('" + bean.getPicName() + "','"
				+ bean.getCreateUser() + "')");
		Connection conn = null;
		PreparedStatement pst = null;
		log.info("增加图片素材-sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if (executeUpdate > 0) {
				log.info("<ShoppingGoodsDaoImpl>-----<addPictureMaterial>----end");
				return true;
			}
			if (showSql) {
				log.info(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());

		} finally {
			this.closeConnection(null, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<addPictureMaterial>----end");
		return false;
	}

	/**
	 * 查询图片素材
	 * 
	 * @return
	 */
	@Override
	public ReturnDataUtil pictureListPage(PictureMaterialForm pictureMaterialForm) {
		log.info("<ShoppingGoodsDaoImpl>-----<pictureListPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,picName  from picture_material ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询图片素材---sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			sql.append(" order by createTime desc ");//排序
			long off = (pictureMaterialForm.getCurrentPage() - 1) * pictureMaterialForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + pictureMaterialForm.getPageSize());
			rs = pst.executeQuery();
			List<PictureMaterialBean> list = Lists.newArrayList();
			int number = 0;
			while (rs.next()) {
				number++;
				PictureMaterialBean bean = new PictureMaterialBean();
				bean.setId(rs.getLong("id"));
				bean.setNumber(number);
				bean.setPicName(rs.getString("picName"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(pictureMaterialForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsDaoImpl>-----<pictureListPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<pictureListPage>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 查询广告商品
	 */
	@Override
	public List<ShoppingGoodsBean> findAdvertisingShopping() {
		log.info("<ShoppingGoodsDaoImpl>-----<findAdvertisingShopping>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select sg.id,sg.name,sg.companyId,sg.typeId,pic,barCode,state,purchaseWay,brand,standard,unit,pack,details,createTime,createUser,updateTime,updateUser,deleteFlag  ");
		sql.append(
				" ,costPrice,salesPrice,preferentialPrice,basicItemId,quantity,purchaseNotes,commodityParameters from shopping_goods sg ");
		sql.append(" where sg.deleteFlag=0 and sg.state=5100 and isHomeShow=1  and advertisingPic  IS  NULL and typeId != 25 ");
		//权限控制
		sql.append(" and (target = 1 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 2 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or target = 0 ");
		sql.append(" or target = 3 and vmCode in (select code from vending_machines_info vmi where vmi.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+"))");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询广告商品 下拉列表sql:" + sql.toString());
		List<ShoppingGoodsBean> list = Lists.newArrayList();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				ShoppingGoodsBean bean = new ShoppingGoodsBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				Long basicItemId = rs.getLong("basicItemId");
				if (basicItemId != null) {
					bean.setBasicItemId(rs.getLong("basicItemId"));
				}
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<ShoppingGoodsDaoImpl>-----<findAdvertisingShopping>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<findAdvertisingShopping>----end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	@Override
	public boolean changeInventory(Integer num,Long id) {
		log.info("<ShoppingGoodsDaoImpl>-----<reduceInventory>----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `shopping_goods` SET quantity = quantity - "+num+"  WHERE id = "+id);
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<reduceInventory>----end");
		if(rs>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 查询商城商品列表
	 */
	@Override
	public ReturnDataUtil couponListPage(ShoppingGoodsForm shoppingGoodsForm, CouponForm couponForm) {
		log.info("<ShoppingGoodsDaoImpl>-----<couponListPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select sg.id,sg.name,sg.companyId,typeId,sg.pic,barCode,sg.state,purchaseWay,brand,standard,unit,pack,details,sg.createTime,sg.createUser,sg.updateTime,sg.updateUser,");
		sql.append(
				" sg.deleteFlag,quantity,purchaseNotes,commodityParameters,isHomeShow,it.name as typeName,c.name  as companyName,s.name as stateName ,s1.name as unitName,costPrice,salesPrice,preferentialPrice,basicItemId,advertisingPic,sg.target,sg.vmCode,sg.areaId");
		
		sql.append(" ,sg.areaName,cp.startTime,cp.endTime,cp.type,cp.id as couponId,cp.periodType,periodDay,canSend,way,useWhere,bindProduct,sendMax,money,deductionMoney,cp.pic as couponPic");
		
		sql.append(" from shopping_goods  sg left join item_type it on sg.typeId=it.id ");
		sql.append(" left join company c on sg.companyId=c.id left join state_info s on sg.state=s.state ");
		sql.append(" left join state_info s1 on sg.unit=s1.state ");
		sql.append(" inner join coupon cp on cp.name=sg.name ");

		sql.append(" where sg.deleteFlag=0 ");
		sql.append(" and cp.target=sg.target");
		sql.append(" and (case when sg.target=1 then cp.companyId=sg.companyId");
		sql.append(" when sg.target=2 then  cp.companyId=sg.companyId and cp.areaId=sg.areaId");
		sql.append(" when sg.target=0 then  cp.target=sg.target");
		sql.append(" when sg.target=3 then cp.vmCode=sg.vmCode end)");
		sql.append(" and cp.way=5");
		sql.append(" and sg.typeId=25");
		if (shoppingGoodsForm.getType() != null) {
			sql.append(" and isHomeShow=1 and sg.state=5100 ");
		}
		if (StringUtil.isNotBlank(shoppingGoodsForm.getName())) {
			sql.append(" and sg.name like '%" + shoppingGoodsForm.getName() + "%'");
		}
		if (shoppingGoodsForm.getId()!=null) {
			sql.append(" and sg.id="+shoppingGoodsForm.getId());
		}

		sql.append(" order by sg.createTime desc");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("商城商品查询列表sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (shoppingGoodsForm.getCurrentPage() - 1) * shoppingGoodsForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + shoppingGoodsForm.getPageSize());
			rs = pst.executeQuery();
			List<ShoppingGoodsCouponBean> list = Lists.newArrayList();
			int number = 0;
			while (rs.next()) {
				number++;
				ShoppingGoodsCouponBean bean = new ShoppingGoodsCouponBean();
				bean.setNumber(number);
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setStateName(rs.getString("stateName"));
				bean.setUnitName(rs.getString("unitName"));
				bean.setTypeName(rs.getString("typeName"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				
	            bean.setCouponPic(rs.getString("couponPic"));
                bean.setBindProduct(rs.getInt("bindProduct"));
                bean.setPeriodDay(rs.getInt("periodDay"));
                bean.setPeriodType(rs.getInt("periodType"));
                bean.setCanSend(rs.getInt("canSend"));
                bean.setCouponId(rs.getLong("couponId"));
                bean.setMoney(rs.getDouble("money"));
                bean.setSendMax(rs.getInt("sendMax"));
                bean.setDeductionMoney(rs.getDouble("deductionMoney"));
				bean.setType(rs.getInt("type"));
				bean.setStartTime(rs.getDate("startTime"));
				bean.setEndTime(rs.getDate("endTime"));
                String basicItemId = rs.getString("basicItemId");
				if (basicItemId != null) {
					bean.setIsRelevance(0);
					bean.setBasicItemId(rs.getLong("basicItemId"));
				} else {
					bean.setIsRelevance(1);
				}
				bean.setIsHomeShow(rs.getInt("isHomeShow"));
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setTarget(rs.getInt("target"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setUseWhere(rs.getInt("useWhere"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(shoppingGoodsForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingGoodsDaoImpl>-----<couponListPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<couponListPage>----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public Long checkBargainGoods(Object id) {
		log.info("<ShoppingGoodsDaoImpl>-----<checkBargainGoods>----start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id from goods_bargain  where endTime>=now() and state=0 and goodsId="+id);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		log.info("判断是否是砍价商品sql语句："+sql);
		Long result=null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null&&rs.next()) {
				result=rs.getLong("id");
				log.info("<ShoppingGoodsDaoImpl>-----<checkBargainGoods>----end");
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<checkBargainGoods>----end");
		return result;
	}

	

	public ShoppingGoodsBean phoneProductDetails(Long goodsId,Long spellgroupId) {
		log.info("<ShoppingGoodsDaoImpl>-----<phoneProductDetails>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select sg.id,sg.name,companyId,typeId,pic,barCode,sg.state,purchaseWay,brand,standard,unit,pack,details,sg.createTime, ");
		sql.append(" sg.createUser,sg.updateTime,sg.updateUser, sg.deleteFlag,quantity,purchaseNotes,commodityParameters,isHomeShow,");
		sql.append(" costPrice,salesPrice,preferentialPrice,basicItemId,advertisingPic,target,sg.vmCode,sg.areaId,sg.areaName,");
		sql.append(" sg.isHelpOneself,sg.vouchersId,sgs.id spellgroupId,sgs.spellGroupPrice,sgs.minimumGroupSize,sgs.numberLimit ");
		sql.append(" from shopping_goods  sg  left  join  shopping_goods_spellgroup sgs on sg.id=sgs.goodsId  ");
		sql.append(" where sg.id ='"+goodsId+"' and sgs.id='"+spellgroupId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ShoppingGoodsBean bean = null;
		log.info("拼团商品详情查看sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean=new ShoppingGoodsBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setTypeId(rs.getLong("typeId"));
				bean.setPic(rs.getString("pic"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setState(rs.getLong("state"));
				bean.setPurchaseWay(rs.getString("purchaseWay"));
				bean.setBrand(rs.getString("brand"));
				bean.setStandard(rs.getString("standard"));
				bean.setUnit(rs.getLong("unit"));
				bean.setPack(rs.getString("pack"));
				bean.setDetails(rs.getString("details"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCostPrice(rs.getBigDecimal("costPrice"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPreferentialPrice(rs.getBigDecimal("preferentialPrice"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setCommodityParameters(rs.getString("commodityParameters"));
				bean.setPurchaseNotes(rs.getString("purchaseNotes"));
				bean.setTarget(rs.getInt("target"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setAreaName(rs.getString("areaName"));
				String basicItemId = rs.getString("basicItemId");
				if (basicItemId != null) {
					bean.setIsRelevance(0);
					bean.setBasicItemId(rs.getLong("basicItemId"));
				} else {
					bean.setIsRelevance(1);
				}
				bean.setIsHomeShow(rs.getInt("isHomeShow"));
				bean.setAdvertisingPic(rs.getString("advertisingPic"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setVouchersId(rs.getLong("vouchersId"));
				bean.setIsConglomerateCommodity(1);
				bean.setSpellgroupId(rs.getLong("spellgroupId"));
				bean.setNumberLimit(rs.getInt("numberLimit"));
				bean.setNum(rs.getLong("minimumGroupSize"));
				bean.setGroupPurchasePrice(rs.getBigDecimal("spellGroupPrice"));
			}
			log.info("<ShoppingGoodsDaoImpl>-----<phoneProductDetails>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ShoppingGoodsDaoImpl>-----<phoneProductDetails>----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	

	@Override
	public ReturnDataUtil vmiList(ShoppingGoodsVmCodeForm shoppingGoodsVmCodeForm){
		log.info("<ShoppingGoodsDaoImpl>-----<vmiList>----start");
		StringBuffer sql = new StringBuffer();
		ReturnDataUtil data = new ReturnDataUtil();
		sql.append("select * from vending_machines_info where 1=1 ");
		if(shoppingGoodsVmCodeForm.getCompanyId()!=null) {
			sql.append(" and companyId ="+shoppingGoodsVmCodeForm.getCompanyId());
		}
		if(shoppingGoodsVmCodeForm.getAreaId()!=null) {
			sql.append(" and areaId ="+shoppingGoodsVmCodeForm.getAreaId());
		}
		if(StringUtils.isNotBlank(shoppingGoodsVmCodeForm.getAddress())) {
			sql.append(" and locatoinName like '%"+shoppingGoodsVmCodeForm.getAddress()+"%'");
		}
		if(StringUtils.isNotBlank(shoppingGoodsVmCodeForm.getVmCode())) {
			sql.append(" and code like '%"+shoppingGoodsVmCodeForm.getVmCode()+"%'");
		}
		
		if(shoppingGoodsVmCodeForm.getIsBind()==1 && shoppingGoodsVmCodeForm.getType()==0) {
			sql.append(" and  FIND_IN_SET(code, (select vmCode from shopping_goods where id="+shoppingGoodsVmCodeForm.getId()+"))");
		}else if (shoppingGoodsVmCodeForm.getIsBind()==0 && shoppingGoodsVmCodeForm.getType()==0) {
			sql.append(" and  code  not in (SELECT code FROM vending_machines_info" + 
					" WHERE FIND_IN_SET(code, (select vmCode from shopping_goods where  id="+shoppingGoodsVmCodeForm.getId()+")))");
		
		}else if(shoppingGoodsVmCodeForm.getIsBind()==1 && shoppingGoodsVmCodeForm.getType()==1) {
			sql.append(" and  FIND_IN_SET(code, (select vmCode from carry_water_vouchers where id="+shoppingGoodsVmCodeForm.getId()+"))");
		}else if(shoppingGoodsVmCodeForm.getIsBind()==0 && shoppingGoodsVmCodeForm.getType()==1) {
			sql.append(" and  code  not in (SELECT code FROM vending_machines_info" + 
					" WHERE FIND_IN_SET(code, (select vmCode from carry_water_vouchers where  id="+shoppingGoodsVmCodeForm.getId()+")))");
		
		}else if(shoppingGoodsVmCodeForm.getIsBind()==1 && shoppingGoodsVmCodeForm.getType()==2) {
			sql.append(" and  FIND_IN_SET(code, (select vmCode from shopping_goods_spellgroup where id="+shoppingGoodsVmCodeForm.getSpellGroupId()+"))");
		}else if(shoppingGoodsVmCodeForm.getIsBind()==0 && shoppingGoodsVmCodeForm.getType()==2) {
			sql.append(" and  code  not in (SELECT code FROM vending_machines_info" + 
					" WHERE FIND_IN_SET(code, (select vmCode from shopping_goods_spellgroup where  id="+shoppingGoodsVmCodeForm.getSpellGroupId()+")))");
		}
		
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<VendingMachinesInfoBean> list=Lists.newArrayList();
		log.info("查询已绑定/未绑定机器sql语句："+sql);
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				VendingMachinesInfoBean vmi=new VendingMachinesInfoBean();
				vmi.setCode(rs.getString("code"));
				vmi.setLocatoinName(rs.getString("locatoinName"));
				vmi.setIsBind(shoppingGoodsVmCodeForm.getIsBind());
				list.add(vmi);
			}
			data.setCurrentPage(shoppingGoodsVmCodeForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<vmiList>----end");
		return data;
	}
	
	@Override
	public boolean updateVouchersId(ShoppingGoodsBean entity) {
		
		log.info("<ShoppingGoodsDaoImpl>-----<updateVouchersId>----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE `shopping_goods` SET VouchersId = NULL WHERE id = "+entity.getId());
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<updateVouchersId>----end");
		if(rs>0){
			return true;
		}
		return false;
	}

	@Override
	public List<String> findItemName(String product) {
		log.info("<ShoppingGoodsDaoImpl>-----<findItemName>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select itemName from shopping_car where id in(" + product + ") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> list=new ArrayList<String>();
		log.info(" 根据商品id 查询购物车中商品名称sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
					list.add(rs.getString("itemName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<findItemName>----end");
		return list;
	}
	
	@Override
	public String findShoppingGoogsName(String product) {
		log.info("<ShoppingGoodsDaoImpl>-----<findShoppingGoogsName>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select name from shopping_goods where id in(" + product + ") ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info(" 根据商品id查询商品名称sql:" + sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs!=null && rs.next()) {
					return rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<ShoppingGoodsDaoImpl>-----<findShoppingGoogsName>----end");
		return null;
	}
}
