package com.server.module.customer.product.bargainDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class BargainDetailDaoImpl extends MySqlFuns implements BargainDetailDao{

	private final static Logger log = LogManager.getLogger(BargainDetailDaoImpl.class);

	@Override
	public Long insertBargainDetail(BargainDetailBean bargainDetail) {
		log.info("BargainDetailDaoImpl--insertBargainDetail--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `tbl_customer_bargain_detail` (customerBargainId,customerId,cutPrice,createTime)");
		sql.append(" VALUES(?,?,?,NOW())");
		List<Object> param = new ArrayList<Object>();
		param.add(bargainDetail.getCustomerBargainId());
		param.add(bargainDetail.getCustomerId());
		param.add(bargainDetail.getCutPrice());
		int id = insertGetID(sql.toString(), param);
		log.info("BargainDetailDaoImpl--insertBargainDetail--end");
		return Long.valueOf(id);
	}

	@Override
	public List<BargainDetailDto> findBargainDetailList(Long customerBargainId) {
		log.info("BargainDetailDaoImpl--findBargainDetailList--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tcw.`nickname`,tcw.`headimgurl`,tcw.`customerId`,tcbd.cutPrice");
		sql.append(" FROM `tbl_customer_bargain_detail` AS tcbd");
		sql.append(" INNER JOIN tbl_customer_wx AS tcw USING(customerId)");
		sql.append(" WHERE tcbd.customerBargainId = '"+customerBargainId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BargainDetailDto> bargainDetailList = new ArrayList<BargainDetailDto>();
		BargainDetailDto bargainDetail = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bargainDetail = new BargainDetailDto();
				bargainDetail.setCustomerId(rs.getLong("customerId"));
				bargainDetail.setCustomerName(rs.getString("nickname"));
				bargainDetail.setCutPrice(rs.getBigDecimal("cutPrice"));
				bargainDetail.setPic(rs.getString("headimgurl"));
				bargainDetailList.add(bargainDetail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("BargainDetailDaoImpl--findBargainDetailList--end");
		return bargainDetailList;
	}

	@Override
	public BargainDto getBarginSomeInfo(Long customerBargianId) {
		log.info("BargainDetailDaoImpl--getBarginSomeInfo--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT gb.`lowestPrice`,gb.`oneBargainPrice`,tcb.`currPrice` FROM goods_bargain AS gb");
		sql.append(" INNER JOIN tbl_customer_bargain AS tcb ON tcb.`goodsBargainId` = gb.`id`");
		sql.append(" WHERE tcb.`id` = '"+customerBargianId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BargainDto goodsBargain = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				goodsBargain = new BargainDto();
				goodsBargain.setLowestPrice(rs.getBigDecimal("lowestPrice"));
				goodsBargain.setOneBargainPrice(rs.getBigDecimal("oneBargainPrice"));
				goodsBargain.setCurrPrice(rs.getBigDecimal("currPrice"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("BargainDetailDaoImpl--getBarginSomeInfo--end");
		return goodsBargain;
	}

	@Override
	public boolean isBargained(Long customerBargainId, Long customerId) {
		log.info("BargainDetailDaoImpl--isBargained--start");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT 1 FROM tbl_customer_bargain_detail WHERE customerBargainId = '"+customerBargainId+"' AND customerId = '"+customerId+"'");
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isBargained = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				isBargained = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("BargainDetailDaoImpl--isBargained--end");
		return isBargained;
	}

	@Override
	public BargainGoodsDto getBargainGoods(Long goodsBargainId) {
		log.info("BargainDetailDaoImpl--getBargainGoods--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT sg.companyId,sg.salesPrice,sg.id,sg.name as itemName FROM goods_bargain AS gb ");
		sql.append(" INNER JOIN shopping_goods AS sg ON sg.`id` = gb.`goodsId` WHERE gb.id ='"+goodsBargainId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BargainGoodsDto goods = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				goods = new BargainGoodsDto();
				goods.setCompanyId(rs.getInt("companyId"));
				goods.setId(rs.getLong("id"));
				goods.setSalesPrice(rs.getBigDecimal("salesPrice"));
				goods.setItemName(rs.getString("itemName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("BargainDetailDaoImpl--getBargainGoods--end");
		return goods;
	}
	
	
	
}
