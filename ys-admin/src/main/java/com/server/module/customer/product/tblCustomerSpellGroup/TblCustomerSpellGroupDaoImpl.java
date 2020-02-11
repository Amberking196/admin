package com.server.module.customer.product.tblCustomerSpellGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.CustomerUtil;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-10-17 11:06:09
 */
@Repository
public class TblCustomerSpellGroupDaoImpl extends BaseDao<TblCustomerSpellGroupBean>
		implements TblCustomerSpellGroupDao {

	private static Logger log = LogManager.getLogger(TblCustomerSpellGroupDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;
	
	public ReturnDataUtil listPage(TblCustomerSpellGroupForm condition) {
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select tcs.id,tcs.goodsId,tcs.startCustomerId,tcs.participationCustomerId,tcs.createTime,tcs.state,tcs.endTime,tcs.updateTime,tc.phone,sg.name from tbl_customer_spellGroup tcs inner join tbl_customer tc on tcs.startCustomerId=tc.id  ");
		sql.append(" inner join shopping_goods sg on tcs.goodsId=sg.id where 1=1 ");
		if(StringUtils.isNotBlank(condition.getPhone())) {//手机号
			sql.append(" AND tc.phone like '%"+condition.getPhone()+"%'");
		}
		if(StringUtils.isNotBlank(condition.getProductName())) {//商品名称
			sql.append(" AND sg.name like '%"+condition.getProductName()+"%'");
		}
		if(condition.getState()!=null) {
			sql.append(" AND tcs.state="+condition.getState());
		}
		//权限控制
		sql.append(" and (sg.target = 1 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 2 and sg.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or sg.target = 0 ");
		sql.append(" or sg.target = 3 and sg.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
			
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			sql.append(" ORDER BY tcs.createTime desc ");//排序
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<TblCustomerSpellGroupBean> list = Lists.newArrayList();
			while (rs.next()) {
				TblCustomerSpellGroupBean bean = new TblCustomerSpellGroupBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setStartCustomerId(rs.getLong("startCustomerId"));
				bean.setParticipationCustomerId(rs.getString("participationCustomerId"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setState(rs.getInt("state"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setPhone(rs.getString("phone"));
				bean.setSpellProductName(rs.getString("name"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setMessage("查询成功");
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			data.setMessage("查询失败");
			data.setStatus(0);
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	public TblCustomerSpellGroupBean get(Object id) {
		return super.get(id);
	}

	public boolean delete(Object id) {
		TblCustomerSpellGroupBean entity = new TblCustomerSpellGroupBean();
		return super.del(entity);
	}

	public boolean update(TblCustomerSpellGroupBean entity) {
		return super.update(entity);
	}

	/**
	 * 发起拼团
	 */
	public TblCustomerSpellGroupBean insert(TblCustomerSpellGroupBean entity) {
		log.info("<TblCustomerSpellGroupDaoImpl>------<insert>-----------start");
		TblCustomerSpellGroupBean bean = super.insert(entity);
		log.info("<TblCustomerSpellGroupDaoImpl>------<insert>-----------end");
		return bean;
	}

	/**
	 * 查询商品下的用户拼团信息
	 */
	public List<TblCustomerSpellGroupBean> list(TblCustomerSpellGroupForm tblCustomerSpellGroupForm) {
		log.info("<TblCustomerSpellGroupDaoImpl>-------<list>-------------start");
		List<TblCustomerSpellGroupBean> list = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append(" select tcs.id,tcs.goodsId,tcs.startCustomerId,tcs.participationCustomerId,tcs.createTime,tcs.updateTime,tc.phone,tcs.endTime,");
		sql.append(" tcw.nickname,tcw.headimgurl from tbl_customer_spellGroup tcs left join tbl_customer tc on tcs.startCustomerId=tc.id  ");
		sql.append(" left join tbl_customer_wx tcw on tc.id=tcw.customerId  ");
		if(tblCustomerSpellGroupForm.getSpellgroupId()!=null) {
			sql.append(" where tcs.spellGroupId='"+tblCustomerSpellGroupForm.getSpellgroupId()+"' ");
		}
		if(tblCustomerSpellGroupForm.getCustomerId()!=null) {
			sql.append(" and tcs.startCustomerId= '"+tblCustomerSpellGroupForm.getCustomerId()+"' ");
		}
		sql.append(" and tcs.state=2 and tcs.endTime>now() ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询商品下的用户拼团信息:sql语句"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				TblCustomerSpellGroupBean bean = new TblCustomerSpellGroupBean();
				bean.setId(rs.getLong("id"));
				bean.setGoodsId(rs.getLong("goodsId"));
				bean.setStartCustomerId(rs.getLong("startCustomerId"));
				bean.setParticipationCustomerId(rs.getString("participationCustomerId"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setPhone(rs.getString("phone"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setUserName(EmojiUtil.getEmoji(rs.getString("nickname")));
				bean.setHeadimgurl(rs.getString("headimgurl"));
				//得到成团限制时间
				long time=rs.getDate("endTime").getTime();
				//得到当前时间
				long time2 = new Date().getTime();
				// 计算差多少小时
			    long hour = (time-time2) % (1000 * 24 * 60 * 60) / (1000 * 60 * 60);
				bean.setHour(hour);
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			log.info("<TblCustomerSpellGroupDaoImpl>-------<list>-------------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<TblCustomerSpellGroupDaoImpl>-------<list>-------------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil findCustomerByIds(String ids) {
		log.info("<TblCustomerSpellGroupDaoImpl>-------<findCustomerByIds>-------------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select tc.id,tc.phone,tc.province,tc.city,tc.UserName from tbl_customer tc where tc.id in ("+ids+")");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if (showSql) {
				log.info(sql);
			}
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			
			List<TblCustomerSpellGroupBean> list = Lists.newArrayList();
			while (rs.next()) {
				TblCustomerSpellGroupBean bean = new TblCustomerSpellGroupBean();
				bean.setId(rs.getLong("id"));
				bean.setPhone(rs.getString("phone"));
				bean.setProvince(rs.getString("province"));
				bean.setCity(rs.getString("city"));
				bean.setUserName(rs.getString("UserName"));
				list.add(bean);
			}
			data.setMessage("查询成功");
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<TblCustomerSpellGroupDaoImpl>-------<findCustomerByIds>-------------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("<TblCustomerSpellGroupDaoImpl>-------<findCustomerByIds>-------------end");
			log.error(e.getMessage());
			data.setStatus(0);
			data.setMessage("查询失败");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	/**
	 *  判断当前用户是否参与重复拼团
	 */
	@Override
	public boolean isRepeatedSpellGroup(Long id) {
		log.info("<TblCustomerSpellGroupDaoImpl>-------<isRepeatedSpellGroup>-------------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select tcs.id,tcs.goodsId,tcs.startCustomerId,tcs.participationCustomerId,tcs.createTime,tcs.updateTime ");
		sql.append("from tbl_customer_spellGroup tcs where ");
		sql.append(" ( FIND_IN_SET("+CustomerUtil.getCustomerId()+",tcs.participationCustomerId)   or  tcs.startCustomerId ='"+CustomerUtil.getCustomerId()+"') ");
		sql.append(" and tcs.id='"+id+"'");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if (showSql) {
				log.info("判断当前用户是否参与重复拼团sql语句："+sql);
			}
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()&&rs!=null) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}
	
	public Integer insertSQL(TblCustomerSpellGroupBean entity) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into tbl_customer_spellGroup (goodsId,startCustomerId,endTime,state,spellGroupId) ");
		sql.append(" VALUES(?,?,?,?,?)");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		List<Object> param = new ArrayList<Object>();
		param.add(entity.getGoodsId());
		param.add(entity.getStartCustomerId());
		param.add(entity.getEndTime());
		param.add(entity.getStartState());
		param.add(entity.getSpellGroupId());
		Integer insertGetID = insertGetID(sql.toString(),param);
		return insertGetID;
	}

	
	public Map<String,Integer> isStartSpellGroup(Long goodsId) {
		log.info("<TblCustomerSpellGroupDaoImpl>-------<isStartSpellGroup>-------------start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select  c.successLimit,c.userType,s.count from (select  goodsId,successLimit,userType from shopping_goods_spellgroup where id ="+goodsId+" ) c      ");
		sql.append(" left join (select goodsId,count(1) count from tbl_customer_spellgroup where spellGroupId="+goodsId+"  and (state=1 or state=2)  and startCustomerId ='"+CustomerUtil.getCustomerId()+"' ) s ");
		sql.append(" on c.goodsId = s.goodsId ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String,Integer> map=new HashMap<String,Integer>();
		log.info("判断当前用户是否可以发起拼团sql语句："+sql);
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()&&rs!=null) {
				int num = rs.getInt("successLimit");
				int count = rs.getInt("count");
				int userType = rs.getInt("userType");
				map.put("num", num);
				map.put("count", count);
				map.put("userType", userType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<TblCustomerSpellGroupDaoImpl>-------<isStartSpellGroup>-------------end");
		return map;
	}


	public  ReturnDataUtil listSpellOrders(SpellOrderCondition condition) {

		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT s.spellGroupPrice,s.timeLimit,o.createTime as orderTime,t.state as spellState,o.ptCode,s.id AS groupId,t.id AS spellId,o.id AS orderId,t.createTime ,g.name AS goodsName,o.nowprice*o.num AS money,o.spellgroupState ,g.isHelpOneself,c1.nickname,c1.phone,c.phone AS spellPhone ");
		sql.append(" from store_order o,tbl_customer_spellgroup t,shopping_goods_spellgroup s,shopping_goods g,tbl_customer c,tbl_customer c1  ");
		sql.append(" WHERE o.customerGroupId=t.id AND t.spellGroupId=s.id AND s.goodsId=g.id AND t.startCustomerId=c.id AND o.customerId=c1.id AND o.type=3 ");

		if(StringUtil.isNotEmpty(condition.getStart())){
			sql.append(" and o.createTime>='"+condition.getStart()+" 00:00:00'");
		}
		if(StringUtil.isNotEmpty(condition.getEnd())){
			sql.append(" and o.createTime<='"+condition.getEnd()+" 23:59:59'");
		}
		if(condition.getState()==null ){
			
		}else if(condition.getState()==0) {
			sql.append(" and o.spellgroupState  is null ");
		}else{
			sql.append(" and o.spellgroupState="+condition.getState());
		}


		if(StringUtil.isNotEmpty(condition.getGoodsName())){
			sql.append(" and g.name like '%"+condition.getGoodsName()+"%'");
		}
		if(StringUtil.isNotEmpty(condition.getPtCode())){
			sql.append(" and o.ptCode='"+condition.getPtCode()+"'");
		}
		if(StringUtil.isNotEmpty(condition.getPhone())){
			sql.append(" and c1.phone='"+condition.getPhone()+"'");
		}
		//权限控制
		sql.append(" and (g.target = 1 and g.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or g.target = 2 and g.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" or g.target = 0 ");
		sql.append(" or g.target = 3 and g.vmCode in (select code from vending_machines_info where companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+") )");
        sql.append(" and o.ptCode is not null ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			sql.append(" ORDER BY o.createTime desc ");//排序
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<SpellOrderVO> list = Lists.newArrayList();
			while (rs.next()) {
				SpellOrderVO bean = new SpellOrderVO();
				bean.setPtCode(rs.getString("ptCode"));
				bean.setGroupId(rs.getLong("groupId"));
				bean.setSpellId(rs.getLong("spellId"));
				bean.setOrderId(rs.getLong("orderId"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setGoodsName(rs.getString("goodsName"));
				bean.setMoney(rs.getDouble("money"));
				bean.setSpellGroupState(rs.getInt("spellGroupState"));
				bean.setIsHelpOneself(rs.getInt("isHelpOneself"));
				bean.setNickname(rs.getString("nickname"));
				bean.setPhone(rs.getString("phone"));
				bean.setSpellPhone(rs.getString("spellPhone"));
				bean.setSpellGroupPrice(rs.getDouble("spellGroupPrice"));
				bean.setTimeLimit(rs.getInt("timeLimit"));
				bean.setOrderTime(rs.getTimestamp("orderTime"));
				bean.setSpellState(rs.getInt("spellState"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setMessage("查询成功");
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			data.setMessage("查询失败");
			data.setStatus(0);
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}

	}
	public List<Map<String,Object>> listSpell(Integer spellId){

		StringBuilder sql=new StringBuilder();
		//sql.append("select id,ptCode,type,state,product,price,nowprice,location,createTime,payTime,deliverTime,affirmTime,refundTime,vendingMachinesCode,openid,distributionModel,coupon,couponPrice,payType,ip,wxCouponFee,wxCouponCount,wxCouponType,wxCouponId,wxCouponOneFee,payCode,customerId,customerGroupId,addressId,companyId,customerBargainId,carryId,spellgroupState,num from store_order where 1=1 ");
		sql.append("select i.name as state,s.payTime,s.ptCode,s.customerId,s.spellgroupState,t.phone,t.nickname,s.createTime from store_order s,tbl_customer t,state_info i where s.customerId=t.id and s.state=i.id ");

		sql.append(" and s.customerGroupId="+spellId);
		sql.append(" order by s.createTime asc");
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<Map<String,Object>> list=Lists.newArrayList();

		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while(rs.next()){
				Map<String,Object> map= Maps.newHashMap();
				map.put("nickname",rs.getString("nickname"));
				map.put("phone",rs.getString("phone"));
				String state=rs.getString("state");
				map.put("state",state);

				map.put("ptCode",rs.getString("ptCode"));
				Date payTime=rs.getTimestamp("payTime");
				if(payTime!=null)
				   map.put("payTime",sf.format(payTime));
				else{
					map.put("payTime","");
				}
				list.add(map);
			}
			if (showSql){
				log.info(sql);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	public List<Map<String,Object>> listSpellGoodsInfo(Integer orderId){

		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT o.num,o.nowprice,g.name,g.pic,g.salesPrice FROM store_order o,shopping_goods g  WHERE o.product=g.id ");
		sql.append(" and o.id="+orderId);
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<Map<String,Object>> list=Lists.newArrayList();

		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				Map<String,Object> map= Maps.newHashMap();
				map.put("name",rs.getString("name"));
				map.put("pic",rs.getString("pic"));

				map.put("prePrice",rs.getDouble("salesPrice"));
				map.put("price",rs.getDouble("nowprice"));
				map.put("num",rs.getInt("num"));
				Double total=rs.getDouble("nowprice")*rs.getInt("num");
				map.put("totalMoney",total);
				list.add(map);
			}
			if (showSql){
				log.info(sql);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}


	public List<Map<String,Object>> listRefundInfo(String ptCode){

		StringBuilder sql=new StringBuilder();
		sql.append("  SELECT state,refundPlatform,reason,refundGenre,createTime FROM refund_record r  WHERE  ");
		sql.append("  r.ptCode='"+ptCode+"'");
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<Map<String,Object>> list=Lists.newArrayList();

		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				Map<String,Object> map= Maps.newHashMap();
				int form=rs.getInt("refundPlatform");
				if(form==1){
					map.put("refundPlatform","微信");

				}else{
					map.put("refundPlatform","支付宝");
				}

				map.put("reason",rs.getString("reason"));
                int type=rs.getInt("refundGenre");
                if(type==0){
					map.put("refundType","自动退款");
				}else if(type==1){
					map.put("refundType","拍错了");
				}else{
					map.put("refundType","取货不方便");
				}
				Date date=rs.getTimestamp("createTime");
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("refundTime",sf.format(date));
				int state=rs.getInt("state");
				if(state==1)
				    map.put("state","成功");
				else{
					map.put("state","失败");
				}
				list.add(map);
			}
			if (showSql){
				log.info(sql);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<Map<String,Object>> listVouchersInfo(Integer orderId){

		StringBuilder sql=new StringBuilder();
		sql.append("  SELECT v.name,c.startTime,c.endTime,c.createTime,c.quantity,c.useQuantity FROM carry_water_vouchers_customer c ,carry_water_vouchers v WHERE c.carryId=v.id  ");
		sql.append("  and c.orderId="+orderId);
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<Map<String,Object>> list=Lists.newArrayList();

		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				Map<String,Object> map= Maps.newHashMap();
				map.put("name",rs.getString("name"));
				Date date=rs.getTimestamp("startTime");
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("startTime",sf.format(date));
				Date date1=rs.getTimestamp("endTime");
				map.put("endTime",sf.format(date1));
				Date date2=rs.getTimestamp("createTime");
				map.put("sendTime",sf.format(date2));
				map.put("quantity",rs.getInt("quantity"));
				map.put("useQuantity",rs.getInt("useQuantity"));

				list.add(map);
			}
			if (showSql){
				log.info(sql);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	public List<Map<String,Object>> listBaseSpellInfo(Integer orderId){

		StringBuilder sql=new StringBuilder();
		sql.append("  SELECT  s.spellGroupPrice,s.timeLimit,s.minimumGroupSize,o.createTime," +
				"o.payType ,i.name AS orderState,t.state AS groupState,o.ptCode," +
				"t.createTime,g.name AS goodsName,s.id AS spellId,o.spellgroupState,g.isHelpOneself,c1.nickname,c1.phone,c.phone AS spellPhone  ");
		sql.append(" FROM store_order o,tbl_customer_spellgroup t,shopping_goods_spellgroup s,shopping_goods g,tbl_customer c,tbl_customer c1,state_info i ");
		sql.append(" WHERE o.customerGroupId=t.id AND t.spellGroupId=s.id AND s.goodsId=g.id AND t.startCustomerId=c.id AND o.customerId=c1.id AND o.state=i.state");
		sql.append(" AND o.id="+orderId);
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<Map<String,Object>> list=Lists.newArrayList();

		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				Map<String,Object> map= Maps.newHashMap();
				map.put("spellGroupPrice",rs.getDouble("spellGroupPrice"));
				map.put("timeLimit",rs.getInt("timeLimit"));

				Date date=rs.getTimestamp("createTime");
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("orderTime",sf.format(date));
				int payType=rs.getInt("payType");
                if(payType==1){
                	map.put("payType","微信");
				}else{
					map.put("payType","支付宝");
				}
				map.put("orderState",rs.getString("orderState"));
				int gs=rs.getInt("groupState");
				//0失败 1成功 2拼团中 3未开始
				String groupState="";
				if(gs==0){
					groupState="失败";
				}
				if(gs==1){
					groupState="成功";
				}
				if(gs==2){
					groupState="拼团中";
				}
				if(gs==3){
					groupState="未开始";
				}
                map.put("groupState",groupState);
				map.put("orderNo",rs.getString("ptCode"));
				map.put("minPeople",rs.getInt("minimumGroupSize"));
				map.put("activeId",rs.getInt("spellId"));
				map.put("nickname",rs.getString("nickname"));
				map.put("phone",rs.getString("phone"));

				list.add(map);
			}
			if (showSql){
				log.info(sql);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return list;
		} finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	@Override
	public Integer checkNewCustomer(Long customerId) {
		log.info("<TblCustomerSpellGroupDaoImpl>-------<checkNewCustomer>-------------start");
		StringBuilder sql = new StringBuilder();
		if(customerId!=null) {
			sql.append(" select count(1) count from (select customerId from pay_record where customerId='"+customerId+"' ) a");
			sql.append(" left join (select customerId from store_order where customerId='"+customerId+"' ) b on a.customerId=b.customerId ");

		}else {
			sql.append(" select count(1) count from (select customerId from pay_record where customerId='"+CustomerUtil.getCustomerId()+"' ) a");
			sql.append(" left join (select customerId from store_order where customerId='"+CustomerUtil.getCustomerId()+"' ) b on a.customerId=b.customerId ");
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Integer count=-1;
		if (showSql) {
			log.info("判断该用户是否是新用户sql语句："+sql);
		}
		try {
			
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()&&rs!=null) {
				count= rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<TblCustomerSpellGroupDaoImpl>-------<checkNewCustomer>-------------end");
		return count;
	}
	
	@Override
	public boolean updateParCustomerIdAndState(Long customerGroupId, String participationCustomerId, Integer state) {
		log.info("<TblCustomerSpellGroupDaoImpl--updateParCustomerIdAndState--start>");
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(participationCustomerId)){
			sql.append(" UPDATE `tbl_customer_spellGroup` SET participationCustomerId = '"+participationCustomerId+"'");
			if(state!=null) {
				sql.append(" ,state = "+state);
			}
			sql.append(" WHERE id =  "+customerGroupId);
		}else {
			sql.append(" UPDATE `tbl_customer_spellGroup` SET state = "+state);
			sql.append(" WHERE id =  "+customerGroupId);
		}
		log.info("支付完成后更新用户拼团表sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			int rowCount = ps.executeUpdate();
			if(rowCount>0) {
				return true;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<TblCustomerSpellGroupDaoImpl--updateParCustomerIdAndState--end>");
		return false;
	}
}



