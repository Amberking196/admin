package com.server.module.customer.product.spellGroupShare;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author why
 * @date: 2019年1月16日 下午6:12:25
 */
@Repository
public class SpellGroupShareDaoImpl extends BaseDao<SpellGroupShareBean> implements SpellGroupShareDao {

	private static Logger log = LogManager.getLogger(SpellGroupShareDaoImpl.class);

	
	public SpellGroupShareBean list(Long customerSpellGroupId) {
		log.info("<SpellGroupShareDaoImpl>----<list>----start");
		StringBuffer sql=new StringBuffer();
		sql.append(" select tcs.id,sg.id goodsId,sg.name,sg.pic,sg.salesPrice,sgs.spellGroupPrice,sgs.minimumGroupSize,sgs.numberLimit, ");
		sql.append(" tcs.startCustomerId,tcs.spellGroupId,tcs.endTime,tcw.customerId,tcw.nickname,tcw.headimgurl ");
		sql.append(" from tbl_customer_spellgroup tcs left join shopping_goods_spellgroup sgs on tcs.spellGroupId=sgs.id ");
		sql.append(" left join shopping_goods sg on sgs.goodsId=sg.id left join tbl_customer_wx tcw ");
		sql.append(" on (tcw.customerId=tcs.startCustomerId or FIND_IN_SET(tcw.customerId,tcs.participationCustomerId)) ");
		sql.append(" where tcs.id='"+customerSpellGroupId+"' and tcs.state=2 and tcs.endTime>now() ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		SpellGroupShareBean bean=null;
		log.info("分享后用户点击页面查询sql:" + sql.toString());
		Map<Long, SpellGroupShareBean> map=new HashMap<Long, SpellGroupShareBean>();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
					Long id=rs.getLong("id");
					bean = map.get(id);
					if(bean==null) {
						bean=new SpellGroupShareBean();
						bean.setId(id);
						bean.setGoodsId(rs.getLong("goodsId"));
						bean.setGoodsName(rs.getString("name"));
						bean.setPic(rs.getString("pic"));
						bean.setEndTime(rs.getTimestamp("endTime"));
						bean.setMinimumGroupSize(rs.getInt("minimumGroupSize"));
						bean.setNumberLimit(rs.getInt("numberLimit"));
						bean.setSalesPrice(rs.getBigDecimal("salesPrice"));
						bean.setSpellGroupPrice(rs.getBigDecimal("spellGroupPrice"));
						bean.setSpellGroupId(rs.getLong("spellGroupId"));
						Integer findCount = findCount(rs.getLong("goodsId"));
						bean.setCount(findCount);
						map.put(id, bean);
					}
					SpellGroupCustomerBean bean2=new SpellGroupCustomerBean();
					bean2.setCustomerId(rs.getLong("customerId"));
					bean2.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
					bean2.setHeadimgurl(rs.getString("headimgurl"));
					bean2.setStartCustomerId(rs.getLong("startCustomerId"));
					bean.getList().add(bean2);
					
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<SpellGroupShareDaoImpl>----<list>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<SpellGroupShareDaoImpl>----<list>----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		
	}


	@Override
	public SpellGroupShareBean payFinishList(Long orderId) {
		log.info("<SpellGroupShareDaoImpl>----<payFinishList>----start");
		StringBuffer sql=new StringBuffer();
		sql.append(" select tcs.id,s.id orderId,s.nowprice,sg.id itemId,sg.name itemName,sg.pic,tcs.endTime,tcs.state,sgs.minimumGroupSize,tcw.customerId,tcw.nickname,tcw.headimgurl,tcs.startCustomerId ");
		sql.append(" from store_order  s left join shopping_goods sg on s.product=sg.id left join tbl_customer_spellgroup tcs on s.customerGroupId=tcs.id ");
		sql.append(" left join tbl_customer_wx  tcw on (tcw.customerId=tcs.startCustomerId or FIND_IN_SET(tcw.customerId,tcs.participationCustomerId)) ");
		sql.append(" left join shopping_goods_spellgroup sgs on tcs.spellGroupId=sgs.id where s.id='"+orderId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		SpellGroupShareBean bean=null;
		log.info("用户支付完成后 查询信息ql:" + sql.toString());
		Map<Long, SpellGroupShareBean> map=new HashMap<Long, SpellGroupShareBean>();
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
					Long id=rs.getLong("id");
					bean = map.get(id);
					if(bean==null) {
						bean=new SpellGroupShareBean();
						bean.setId(id);
						bean.setOrderId(rs.getLong("orderId"));
						bean.setGoodsId(rs.getLong("itemId"));
						bean.setGoodsName(rs.getString("itemName"));
						bean.setPic(rs.getString("pic"));
						bean.setEndTime(rs.getTimestamp("endTime"));
						bean.setPrice(rs.getBigDecimal("nowprice"));
						bean.setMinimumGroupSize(rs.getInt("minimumGroupSize"));
						bean.setState(rs.getInt("state"));
						map.put(id, bean);
					}
					SpellGroupCustomerBean bean2=new SpellGroupCustomerBean();
					bean2.setCustomerId(rs.getLong("customerId"));
					bean2.setNickname(EmojiUtil.getEmoji(rs.getString("nickname")));
					bean2.setHeadimgurl(rs.getString("headimgurl"));
					bean2.setStartCustomerId(rs.getLong("startCustomerId"));
					bean.getList().add(bean2);
					
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<SpellGroupShareDaoImpl>----<payFinishList>----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<SpellGroupShareDaoImpl>----<payFinishList>----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	
	/**
	 * 查询商品拼团件数
	 * @author why
	 * @date 2019年1月18日 下午3:13:40 
	 * @param goodsId
	 * @return
	 */
	public Integer findCount(Long goodsId) {
		log.info("<SpellGroupShareDaoImpl>-----<findCount>----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(s.num) count from  store_order s inner join tbl_customer_spellgroup tcs "); 
		sql.append(" on s.customerGroupId=tcs.id  and tcs.state=1 where s.product='"+goodsId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询拼团商品已购件数:" + sql.toString());
		Integer count=0;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				count=rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<SpellGroupShareDaoImpl>-----<findCount>----end");
		return count;
	}
}
