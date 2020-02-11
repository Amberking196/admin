package com.server.module.system.bargainManage;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.supplierManage.SupplierBean;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;
/**
 * author name: hjc
 * create time: 2018-12-21 16:20:26
 */ 
@Repository
public class  TblCustomerBargainDaoImpl extends BaseDao<TblCustomerBargainBean> implements TblCustomerBargainDao{

	private final static Logger log = LogManager.getLogger(TblCustomerBargainDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;
	
	public ReturnDataUtil listPage(TblCustomerBargainForm tblCustomerBargainForm) {
		log.info("<TblCustomerBargainDaoImpl>----<listPage>----start");

		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();

		sql.append(" SELECT tcb.id,tcb.goodsBargainId,tcb.state,tcb.createTime,tcb.sendMessage,sg.name as shoppingGoodsName,sg.salesPrice,tc.phone,tca.phone as receiverPhone,tca.receiver as receiver,so.ptCode  from tbl_customer_bargain tcb");
		sql.append(" left join goods_bargain gb on gb.id = tcb.goodsBargainId");
		sql.append(" left join tbl_customer_address tca on tca.id = tcb.addressId ");
		sql.append(" left join shopping_goods sg on sg.id = gb.goodsId");
		sql.append(" left join tbl_customer tc on tc.id = tcb.customerId");
		sql.append(" left join store_order so on so.customerBargainId  = tcb.id");

		sql.append(" where 1=1");
		if(StringUtils.isNotBlank(tblCustomerBargainForm.getPhone())) {
			sql.append(" and tc.phone= "+tblCustomerBargainForm.getPhone());
		}
		if(tblCustomerBargainForm.getState()!=null) {
			sql.append(" and tcb.state= "+tblCustomerBargainForm.getState());
		}
		if(StringUtils.isNotBlank(tblCustomerBargainForm.getShoppingGoodsName())) {
			sql.append(" and sg.name like '%"+tblCustomerBargainForm.getShoppingGoodsName()+"%'");
		}
		if(StringUtils.isNotBlank(tblCustomerBargainForm.getPtCode())) {
			sql.append(" and so.ptCode ='"+tblCustomerBargainForm.getPtCode()+"'");
		}
		if(tblCustomerBargainForm.getCreateStartTime()!=null) {
			sql.append(" and tcb.createTime  >= '"+DateUtil.formatYYYYMMDDHHMMSS(tblCustomerBargainForm.getCreateStartTime())+"'");
		}
		if(tblCustomerBargainForm.getCreateEndTime()!=null) {
			sql.append(" and tcb.createTime  <'"+DateUtil.formatYYYYMMDDHHMMSS(tblCustomerBargainForm.getCreateEndTime())+"'");
		}
		if(tblCustomerBargainForm.getSendMessage()!=null) {
			sql.append(" and tcb.sendMessage  = "+tblCustomerBargainForm.getSendMessage());
		}
		//权限控制
		sql.append(" and (sg.target = 1 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 2 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 0 ");
		sql.append(" or sg.target = 3 and sg.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
		sql.append(" order by tcb.createTime desc");
		log.info("砍价报名列表>>>:"+sql.toString());
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			conn=openConnection();
			pst=conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count=0;
			while(rs.next()){
				count=rs.getInt(1);
			}
			if(tblCustomerBargainForm.getIsShowAll()==0) {
				long off = (tblCustomerBargainForm.getCurrentPage() - 1) * tblCustomerBargainForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + tblCustomerBargainForm.getPageSize());
			}else {
				pst = conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<TblCustomerBargainBean> list=Lists.newArrayList();
			while(rs.next()){
				TblCustomerBargainBean bean = new TblCustomerBargainBean();
				bean.setId(rs.getInt("id"));
				bean.setGoodsBargainId(rs.getInt("goodsBargainId"));
				//bean.setCustomerId(rs.getLong("customerId"));
				//bean.setCurrPrice(rs.getBigDecimal("currPrice"));
				bean.setState(rs.getInt("state"));
				//bean.setAddressId(rs.getLong("addressId"));
				
				bean.setShoppingGoodsName(rs.getString("shoppingGoodsName"));
				bean.setSalesPrice(rs.getString("salesPrice"));
				bean.setPhone(rs.getString("phone"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setReceiverPhone(rs.getString("receiverPhone"));
				bean.setSendMessage(rs.getInt("sendMessage"));
				//bean.setEndTime(rs.getDate("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setPtCode(rs.getString("ptCode"));
				//bean.setUpdateTime(rs.getDate("updateTime"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(tblCustomerBargainForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setPageSize(tblCustomerBargainForm.getPageSize());
			log.info("<TblCustomerBargainDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally{
			this.closeConnection(rs, pst, conn);
		}
	
	}

	public List<EachStateNumDto> eachStateNum(TblCustomerBargainForm tblCustomerBargainForm) {
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		log.info("<TblCustomerBargainDaoImpl>----<eachStateNum>----start");

		sql.append(" SELECT tcb.state,count(*) as num FROM `tbl_customer_bargain` tcb ");
		sql.append(" inner join goods_bargain gb on gb.id = tcb.goodsBargainId");
		sql.append(" left join tbl_customer_address tca on tca.id = tcb.addressId ");
		sql.append(" left join shopping_goods sg on sg.id = gb.goodsId");
		sql.append(" left join tbl_customer tc on tc.id = tcb.customerId");
		sql.append(" left join store_order so on so.customerBargainId  = tcb.id");
		sql.append(" where 1=1");

		if(StringUtils.isNotBlank(tblCustomerBargainForm.getPhone())) {
			sql.append(" and tc.phone= "+tblCustomerBargainForm.getPhone());
		}
		if(tblCustomerBargainForm.getState()!=null) {
			sql.append(" and tcb.state= "+tblCustomerBargainForm.getState());
		}
		if(StringUtils.isNotBlank(tblCustomerBargainForm.getShoppingGoodsName())) {
			sql.append(" and sg.name like '%"+tblCustomerBargainForm.getShoppingGoodsName()+"%'");
		}
		if(StringUtils.isNotBlank(tblCustomerBargainForm.getPtCode())) {
			sql.append(" and so.ptCode ='"+tblCustomerBargainForm.getPtCode()+"'");
		}
		if(tblCustomerBargainForm.getCreateStartTime()!=null) {
			sql.append(" and tcb.createTime  >= '"+DateUtil.formatYYYYMMDDHHMMSS(tblCustomerBargainForm.getCreateStartTime())+"'");
		}
		if(tblCustomerBargainForm.getCreateEndTime()!=null) {
			sql.append(" and tcb.createTime  <'"+DateUtil.formatYYYYMMDDHHMMSS(tblCustomerBargainForm.getCreateEndTime())+"'");
		}
		//权限控制
		sql.append(" and (sg.target = 1 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 2 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 0 ");
		sql.append(" or sg.target = 3 and sg.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
		sql.append("  group by tcb.state");

		log.info("砍价报名列表>>>:"+sql.toString());
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<EachStateNumDto> list=Lists.newArrayList();
		try {
			conn=openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			int sum=0;
			while(rs.next()){
				EachStateNumDto bean = new EachStateNumDto();
				bean.setState(rs.getInt("state"));
				bean.setNum(rs.getInt("num"));
				sum=sum+rs.getInt("num");
				list.add(bean);
			}
			EachStateNumDto bean = new EachStateNumDto();
			bean.setState(3);//全部
			bean.setNum(sum);
			
			if (showSql){
				log.info(sql);
			}
			log.info("<TblCustomerBargainDaoImpl>----<eachStateNum>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	
	}
	
	public TblCustomerBargainDetailDto detail(TblCustomerBargainForm tblCustomerBargainForm) {
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		log.info("<TblCustomerBargainDaoImpl>----<detail>----start");

		sql.append(" SELECT tcb.id,tcb.goodsBargainId,tcb.state,tcb.createTime,tcb.endTime,tcb.updateTime,tcb.sendMessage,tcb.currPrice,sg.name as shoppingGoodsName,sg.salesPrice,tc.phone,tca.phone as receiverPhone,tca.receiver as receiver,tca.name as address,so.ptCode,gb.lowestPrice as lowestPrice,gb.hourLimit as hourLimit,sg.salesPrice,sg.pic  from tbl_customer_bargain tcb");
		sql.append(" left join goods_bargain gb on gb.id = tcb.goodsBargainId");
		sql.append(" left join tbl_customer_address tca on tca.id = tcb.addressId ");
		sql.append(" left join shopping_goods sg on sg.id = gb.goodsId");
		sql.append(" left join tbl_customer tc on tc.id = tcb.customerId");
		sql.append(" left join store_order so on so.customerBargainId  = tcb.id");

		sql.append(" where 1=1 and tcb.id= '"+tblCustomerBargainForm.getId()+"'");
		log.info("砍价详情>>>:"+sql.toString());
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		TblCustomerBargainDetailDto bean = new TblCustomerBargainDetailDto();
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				bean.setId(rs.getInt("id"));
				bean.setGoodsBargainId(rs.getInt("goodsBargainId"));
				bean.setCurrPrice(rs.getBigDecimal("currPrice"));
				bean.setState(rs.getInt("state"));				
				bean.setShoppingGoodsName(rs.getString("shoppingGoodsName"));
				bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
				bean.setPhone(rs.getString("phone"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setReceiverPhone(rs.getString("receiverPhone"));
				bean.setAddress(rs.getString("address"));
				bean.setSendMessage(rs.getInt("sendMessage"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setPtCode(rs.getString("ptCode"));
				bean.setPic(rs.getString("pic"));
				bean.setHourLimit(rs.getInt("hourLimit"));
				bean.setLowestPrice(rs.getBigDecimal("lowestPrice"));
			}
			if (showSql){
				log.info(sql);
			}
			log.info("<TblCustomerBargainDaoImpl>----<detail>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return bean;
		} finally{
			this.closeConnection(rs, pst, conn);
		}
	
	}

	@Override
	public List<TblCustomerBargainDetailBean> detailList(TblCustomerBargainForm tblCustomerBargainForm) {
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		log.info("<TblCustomerBargainDaoImpl>----<detailList>----start");

		sql.append(" SELECT  * from tbl_customer_bargain_detail tcbd");
		sql.append(" left join tbl_customer tc on tc.id = tcbd.customerId");

		sql.append(" where 1=1 and tcbd.customerBargainId= '"+tblCustomerBargainForm.getId()+"'");
		log.info("帮砍名单列表>>>:"+sql.toString());
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<TblCustomerBargainDetailBean> list=Lists.newArrayList();
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				TblCustomerBargainDetailBean bean = new TblCustomerBargainDetailBean();
				bean.setId(rs.getInt("id"));
				bean.setCutPrice(rs.getBigDecimal("cutPrice"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setPhone(rs.getString("phone"));
				bean.setName(EmojiUtil.getEmoji(rs.getString("nickname")));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			log.info("<TblCustomerBargainDaoImpl>----<detailList>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally{
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public Boolean updateSendMessage(String phone, Integer id) {
		log.info("<TblCustomerBargainDaoImpl>----<updateSendMessage>----start");

		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		
		sql.append(" update  tbl_customer_bargain tcb left join tbl_customer tc on tc.id= tcb.customerId");
		sql.append(" set sendMessage = 1 where tcb.id= '"+id+"'");	
		sql.append(" and phone ='"+phone+"'");
		log.info("砍价报名列表>>>:"+sql.toString());
		Connection conn=null;
		PreparedStatement pst=null;
		int rs=0;
		TblCustomerBargainDetailDto bean = new TblCustomerBargainDetailDto();
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeUpdate();
			if(rs>0) {
				return true;
			}
			if (showSql){
				log.info(sql);
			}
			log.info("<TblCustomerBargainDaoImpl>----<updateSendMessage>----end");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally{
			this.closeConnection(null, pst, conn);
			return false;
		}
	}


}

