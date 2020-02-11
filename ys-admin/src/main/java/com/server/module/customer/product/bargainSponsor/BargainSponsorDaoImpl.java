package com.server.module.customer.product.bargainSponsor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.customer.CustomerUtil;
import com.server.module.customer.product.bargainDetail.BargainDetailBean;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.sqlUtil.MySqlFuns;

/**
 * 
 * @author why
 * @date: 2018年12月24日 上午9:19:14
 */
@Repository
public class BargainSponsorDaoImpl extends MySqlFuns implements BargainSponsorDao {

	private Logger log=LogManager.getLogger(BargainSponsorDaoImpl.class);

	@Override
	public List<BargainSponsorBean> bargainGoodsList() {
		log.info("<BargainSponsorDaoImpl>----<bargainGoodsList>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.id,s.goodsId,s.name,s.pic,s.salesPrice,c.purchaseNumber from ");
		sql.append(" ( select gb.id,sg.id goodsId,sg.name,sg.pic,sg.salesPrice,gb.createTime from goods_bargain  gb ");
		sql.append(" inner JOIN shopping_goods sg on gb.goodsId=sg.id and gb.state=0 and gb.endTime>now() and sg.state=5100  ) s ");
		sql.append(" left  join (select sg.id,count(sod.itemId) purchaseNumber  ");
		sql.append(" from shopping_goods sg left JOIN store_order_detile sod on sg.id = sod.itemId ");		
		sql.append(" left join store_order so on sod.orderId = so.id ");				
		sql.append(" where so.state = '10001'  group by sg.id ) c on s.goodsId=c.id ");				
		sql.append(" order by s.createTime desc ");
		log.info("砍价活动商品信息sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BargainSponsorBean> list=new ArrayList<BargainSponsorBean>();
		BargainSponsorBean bean=null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bean=new BargainSponsorBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setName(rs.getString("name"));
				bean.setPic(rs.getString("pic"));
				bean.setPrice(rs.getBigDecimal("salesPrice"));
				bean.setPurchaseNumber(rs.getLong("purchaseNumber"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<BargainSponsorDaoImpl>----<bargainGoodsList>-----end");
		return list;
	}

	@Override
	public List<BargainSponsorBean> bargainGoodsFindCustomerList() {
		log.info("<BargainSponsorDaoImpl>----<bargainGoodsFindCustomerList>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select  tcb.id,sg.id goodsId,sg.name,sg.pic,(sg.salesPrice-tcb.currPrice) price,tcb.endTime,tcb.state,so.id orderId from tbl_customer_bargain tcb ");
		sql.append(" left join goods_bargain gb on tcb.goodsBargainId=gb.id left join shopping_goods sg on gb.goodsId=sg.id left join store_order  so on so.customerBargainId=tcb.id ");
		sql.append(" where tcb.customerId='"+CustomerUtil.getCustomerId()+"'");
		log.info("查询用户的砍价商品信息sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BargainSponsorBean> list=new ArrayList<BargainSponsorBean>();
		BargainSponsorBean bean=null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bean=new BargainSponsorBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setName(rs.getString("name"));
				bean.setPic(rs.getString("pic"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				int state = rs.getInt("state");
				bean.setState(state);
				bean.setOrderId(rs.getLong("orderId"));
				Date endTime = rs.getTimestamp("endTime");
				String end = DateUtil.formatYYYYMMDDHHMMSS(endTime);
				String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
				int result = end.compareTo(now);
				if(state==1) {
					bean.setStateLabel("砍价成功");
				}else if(state==2&&result>0) {
					bean.setStateLabel("砍价中");
				}else{
					bean.setStateLabel("砍价失败");
				}
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<BargainSponsorDaoImpl>----<bargainGoodsFindCustomerList>-----end");
		return list;
	}

	@Override
	public BargainSponsorBean bargainDetails(Long id) {
		log.info("<BargainSponsorDaoImpl>----<bargainDetails>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select  tcb.id,sg.id goodsId,sg.name,sg.pic,(sg.salesPrice-tcb.currPrice) price,tcb.endTime,c.purchaseNumber,gb.bargainCount, ");
		sql.append(" tcb.currPrice,gb.lowestPrice from tbl_customer_bargain tcb left join goods_bargain gb on tcb.goodsBargainId=gb.id ");
		sql.append(" left join shopping_goods sg on gb.goodsId=sg.id left  join (select sg.id,count(sod.itemId) purchaseNumber ");
		sql.append(" from shopping_goods sg left JOIN store_order_detile sod on sg.id = sod.itemId left join store_order so on sod.orderId = so.id ");
		sql.append("  where so.state = '10001'  group by sg.id ) c on gb.goodsId=c.id where tcb.id='"+id+"' ");
		log.info("查询砍价详情商品信息sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BargainSponsorBean bean=null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bean=new BargainSponsorBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setName(rs.getString("name"));
				bean.setPic(rs.getString("pic"));
				bean.setPrice(rs.getBigDecimal("price"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setPurchaseNumber(rs.getLong("purchaseNumber"));
				bean.setLowestPrice(rs.getBigDecimal("lowestPrice"));
				bean.setSurplusPrice(rs.getBigDecimal("currPrice").subtract(rs.getBigDecimal("lowestPrice")));
				List<BargainDetailBean> list = customerBargainDetailList(id);
				bean.setAllNumber(rs.getInt("bargainCount")-list.size());
				bean.setList(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<BargainSponsorDaoImpl>----<bargainDetails>-----end");
		return bean;
	}
	
	public List<BargainDetailBean> customerBargainDetailList(Long id) {
		log.info("<BargainSponsorDaoImpl>----<customerBargainDetailList>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select tcbd.id,tcbd.cutPrice,tcw.nickname,tcw.headimgurl from tbl_customer_bargain_detail tcbd  ");
		sql.append(" left join tbl_customer_wx  tcw on tcbd.customerId=tcw.customerId where customerBargainId='"+id+"' ");
		log.info("查询砍价帮信息sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BargainDetailBean> list=new ArrayList<BargainDetailBean>();
		BargainDetailBean bean=null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bean=new BargainDetailBean();
				bean.setId(rs.getLong("id"));
				bean.setCutPrice(rs.getBigDecimal("cutPrice"));
				bean.setNickName(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setHeadimgurl(rs.getString("headimgurl"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<BargainSponsorDaoImpl>----<customerBargainDetailList>-----end");
		return list;
	}

	@Override
	public boolean isCanBargain(Long id) {
		log.info("<BargainSponsorDaoImpl>----<isCanBargain>-----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.goodsLimit,d.count  from (select id,goodsLimit from goods_bargain where id='"+id+"' ) c inner join  ");
		sql.append(" (select goodsBargainId,count(1) count from tbl_customer_bargain where (state=2 or state=1) and goodsBargainId='"+id+"'  ");
		sql.append(" and customerId='"+CustomerUtil.getCustomerId()+"') d on c.id=d.goodsBargainId ");
		log.info("用户是否可以继续发起砍价sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean flag=true;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				int count = rs.getInt("count");
				int goodsLimit = rs.getInt("goodsLimit");
				if(count>=goodsLimit) {
					flag=false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<BargainSponsorDaoImpl>----<customerBargainDetailList>-----end");
		return flag;
	}
}
