package com.server.module.customer.product.itemPrice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.DateUtil;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class ItemPriceDaoImpl extends MySqlFuns implements ItemPriceDao{

	private final static Logger log = LogManager.getLogger(ItemPriceDaoImpl.class);

	@Override
	public ItemPriceBean insert(ItemPriceBean itemPrice) {
		log.info("ItemPriceDaoImpl--insert--start");
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO item_price(basicItemId,price,createUser,createTime,updateUser,updateTime)");
		sql.append("  VALUES(?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		String now = DateUtil.formatYYYYMMDDHHMMSS(itemPrice.getCreateTime());
		param.add(itemPrice.getBasicItemId());
		param.add(itemPrice.getPrice());
		param.add(itemPrice.getCreateUser());
		param.add(now);
		param.add(itemPrice.getCreateUser());
		param.add(now);
		int id = insertGetID(sql.toString(), param);
		log.info("ItemPriceDaoImpl--insert--end");
		if(id>0){
			itemPrice.setId(Long.valueOf(id));
			return itemPrice;
		}
		return null;
	}

	@Override
	public boolean update(ItemPriceBean itemPrice) {
		log.info("ItemPriceDaoImpl--update--start");
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE item_price SET price=?,deleteFlag=?,updateTime=?,updateUser=?");
		sql.append(" WHERE id =?");
		List<Object> param = new ArrayList<Object>();
		String now = DateUtil.formatYYYYMMDDHHMMSS(itemPrice.getUpdateTime());
		param.add(itemPrice.getPrice());
		param.add(itemPrice.getDeleteFlag());
		param.add(now);
		param.add(itemPrice.getUpdateUser());
		param.add(itemPrice.getId());
		int upate = upate(sql.toString(), param);
		log.info("ItemPriceDaoImpl--update--end");
		if(upate>0){
			return true;
		}
		return false;
	}

	@Override
	public List<ItemPriceDto> getPriceByBasicItemId(Long basicItemId) {
		log.info("ItemPriceDaoImpl--getPriceByBasicItemId--start");
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ip.id,ip.basicItemId,ib.`name` AS itemName,ip.price,ip.deleteFlag,ip.createTime,");
		sql.append(" li.name AS createName,ip.updateTime,li2.name AS updateName");
		sql.append(" FROM item_price AS ip ");
		sql.append(" INNER JOIN item_basic AS ib ON ip.`basicItemId` = ib.`id`");
		sql.append(" LEFT JOIN login_info AS li ON li.`id` = ip.`createUser` ");
		sql.append(" LEFT JOIN login_info AS li2 ON li2.`id` = ip.`updateUser`");
		sql.append(" WHERE ip.`deleteFlag` = 0 AND ip.basicItemId = '"+basicItemId+"'");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ItemPriceDto> itemPriceList = new ArrayList<ItemPriceDto>();
		ItemPriceDto price = null;
		try {	
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				price = new ItemPriceDto();
				price.setBasicItemId(basicItemId);
				price.setCreateName(rs.getString("createName"));
				price.setCreateTime(rs.getTimestamp("createTime"));
				price.setDeleteFlag(rs.getInt("deleteFlag"));
				price.setId(rs.getLong("id"));
				price.setItemName(rs.getString("itemName"));
				price.setPrice(rs.getBigDecimal("price"));
				price.setUpdateName(rs.getString("updateName"));
				price.setUpdateTime(rs.getTimestamp("updateTime"));
				itemPriceList.add(price);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("ItemPriceDaoImpl--getPriceByBasicItemId--end");
		return itemPriceList;
	}
	
}
