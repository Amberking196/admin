package com.server.module.customer.car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-06-29 11:03:25
 */ 
@Repository
public class  ShoppingCarDaoImpl extends BaseDao<ShoppingCarBean> implements ShoppingCarDao{

	public static Logger log = LogManager.getLogger(ShoppingCarDaoImpl.class);

	@Autowired
	private CompanyDao companyDaoImpl;

	@Override
	public ReturnDataUtil listPage(ShoppingCarForm shoppingCarForm) {
		log.info("<ShoppingCarDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append(" select sc.id,customerId,itemId,itemName,price,num,sc.createTime,sc.updateTime,sc.deleteFlag,sg.pic,sg.isHelpOneself from shopping_car sc ");
		sql.append(" inner join shopping_goods sg on sg.id=sc.itemId" );
		sql.append(" where 1=1 ");
		if(shoppingCarForm.getType()!=null) {
			sql.append(" and sc.deleteFlag = 1");
		}else {
			sql.append(" and sc.deleteFlag = 0");
		}
		if(shoppingCarForm.getIds()!=null) {
			sql.append(" and sc.id in ("+shoppingCarForm.getIds()+")");
		}
		if(shoppingCarForm.getCustomerId()!=null) {
			sql.append(" and customerId = "+shoppingCarForm.getCustomerId());
		}
		if(shoppingCarForm.getStartDate()!=null){
			sql.append(" and sc.createTime >= '"+DateUtil.formatYYYYMMDDHHMMSS(shoppingCarForm.getStartDate())+"'");
		}
		if(shoppingCarForm.getEndDate()!=null){
			sql.append(" and sc.createTime < '"+DateUtil.formatLocalYYYYMMDDHHMMSS(shoppingCarForm.getEndDate(),1)+"'");
		}
		//权限控制
		sql.append(" and (sg.target = 1 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 2 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 0 ");
		sql.append(" or sg.target = 3 and sg.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
		
		sql.append(" order by sc.createTime desc");
		Connection conn=null;
		PreparedStatement pst=null; 
		ResultSet rs=null;
		log.info("列表查询sql语句：》》" + sql.toString());
		try {
			conn=openConnection();
			pst=conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count=0;
			while(rs.next()){
				count=rs.getInt(1);
			}
			long off=(shoppingCarForm.getCurrentPage()-1)*shoppingCarForm.getPageSize();
			pst=conn.prepareStatement(sql.toString()+" limit "+off+","+shoppingCarForm.getPageSize());
			if(shoppingCarForm.getIsShowAll()==1) {
				pst=conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<ShoppingCarBean> list=Lists.newArrayList();
			while(rs.next()){
				ShoppingCarBean bean = new ShoppingCarBean();
				bean.setId(rs.getLong("id"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setItemId(rs.getInt("itemId"));
				bean.setItemName(rs.getString("itemName"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setNum(rs.getInt("num"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setPic(rs.getString("pic"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(shoppingCarForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ShoppingCarDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally{
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ShoppingCarBean get(Object id) {
		
		return super.get(id);
	}

	@Override
	public boolean delete(Object id) {
		ShoppingCarBean entity=new ShoppingCarBean();
		entity.setId((Long) id);
		return super.del(entity);
	}

	@Override
	public boolean update(ShoppingCarBean entity) {
		log.info("<ShoppingCarDaoImpl>----<update>----start");
		StringBuilder sql=new StringBuilder();
		sql.append(" update  shopping_car set num = "+entity.getNum());
		sql.append(" , updateTime = '"+DateUtil.formatYYYYMMDDHHMMSS(entity.getUpdateTime())+"'");
		sql.append(" , updateUser ="+UserUtils.getUser().getId());
		sql.append(" where id = " + entity.getId());
		sql.append(" and deleteFlag = 0");
		log.info(sql);
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int sign = pst.executeUpdate();
			if(sign>0) {
				log.info("<ShoppingCarDaoImpl>--<update>--end");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}

	@Override
	public boolean updatePriceAndName(ShoppingGoodsBean entity) {
		log.info("<ShoppingCarDaoImpl>----<updatePrice>----start");
		StringBuilder sql=new StringBuilder();
		sql.append(" update  shopping_car set price = "+entity.getSalesPrice());
		sql.append(" , updateTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
		sql.append(" , updateUser ="+UserUtils.getUser().getId());
		sql.append(" , itemName = '" +entity.getName()+"'");
		sql.append(" where itemId = " + entity.getId());
		sql.append(" and deleteFlag = 0");
		log.info("sql语句:"+sql);
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int sign = pst.executeUpdate();
			if(sign>0) {
				log.info("<ShoppingCarDaoImpl>--<updatePrice>--end");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}
	
	@Override
	public ShoppingCarBean insert(ShoppingCarBean entity) {
		return super.insert(entity);
	}


	@Override
	public boolean del(List<Long> shoppingCarIdList) {
		log.info("<ShoppingCarDaoImpl>--<del>--start");
		StringBuffer sql = new StringBuffer();
		String ids = StringUtils.join(shoppingCarIdList, ",");
		sql.append(" delete from shopping_car where id in (" + ids + ")");
		sql.append(" and deleteFlag = 0");
		sql.append(" and customerId = "+UserUtils.getUser().getId());

		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			int sign = ps.executeUpdate();
			if (sign == shoppingCarIdList.size()) {
				log.info("<ShoppingCarDaoImpl>--<del>--end");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, ps, conn);
		}
		return false;
	}
	
	@Override
	public boolean updateFlag(ShoppingCarBean entity) {
		log.info("<ShoppingCarDaoImpl>----<updateFlag>----start");
		StringBuilder sql=new StringBuilder();
		sql.append(" update  shopping_car set deleteFlag = 1");
		sql.append(" , updateTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
		sql.append(" , updateUser ="+UserUtils.getUser().getId());
		sql.append(" where id = " + entity.getId());
		sql.append(" and deleteFlag = 0");
		log.info(sql);
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int sign = pst.executeUpdate();
			if(sign>0) {
				log.info("<ShoppingCarDaoImpl>--<updateFlag>--end");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}
	
	@Override
	public boolean updateAllFlag(List<Long> shoppingCarIdList) {
		log.info("<ShoppingCarDaoImpl>----<updateAllFlag>----start");
		StringBuilder sql=new StringBuilder();
		String ids = StringUtils.join(shoppingCarIdList, ",");		
		sql.append(" update  shopping_car set deleteFlag = 1");
		sql.append(" , updateTime = '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
		sql.append(" , updateUser ="+UserUtils.getUser().getId());
		sql.append(" where id  in (" + ids + ")");
		sql.append(" and deleteFlag = 0");

		log.info(sql);
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int sign = pst.executeUpdate();
			if(sign>0) {
				log.info("<ShoppingCarDaoImpl>--<updateAllFlag>--end");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(null, pst, conn);
		}
		return false;
	}
}

