package com.server.module.customer.coupon;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.CustomerUtil;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.sys.utils.UserUtils;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: yjr
 * create time: 2018-07-06 14:05:39
 */
@Repository
public class CouponDao1Impl extends BaseDao<CouponBean> implements CouponDao {

    private static Logger log = LogManager.getLogger(CouponDao1Impl.class);

    @Autowired
    private UserUtils userUtils;
    /**
     * 手机端 查询用户优惠券
     */
    public ReturnDataUtil listPage(CouponCondition condition) {
        Long customerId= CustomerUtil.getCustomerId();
        if(customerId==null) {
			customerId=userUtils.getSmsUser().getId();
		}
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        //      private int state;// 1 待领取  2 未使用  3 已使用   4 已过期   5可领取
        if(condition.getState()==1){
            sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,c.startTime,c.endTime,c.pic,c.bindProduct,c.maximumDiscount from coupon c where ");
            sql.append(" c.useWhere=2 and way=2 and deleteFlag=0 and canSend=0 and c.endTime>now() and not exists (select  cc.id from coupon_customer cc where c.id=cc.`couponId` and cc.`customerId`="+customerId+")");
        }
        if(condition.getState()==2){//c.useWhere=2 and
            sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,cc.startTime,cc.endTime,c.pic,c.bindProduct,cc.quantity,sumQuantity,c.maximumDiscount from coupon c inner join coupon_customer cc on c.id=cc.couponId where ");
            sql.append(" cc.state<2 and cc.endTime>now() and cc.`customerId`="+customerId+"  and cc.deleteFlag=0 order by cc.createTime desc ");
        }
        if(condition.getState()==3){
            sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,cc.startTime,cc.endTime,c.pic,c.bindProduct,cc.quantity,sumQuantity,c.maximumDiscount from coupon c inner join coupon_customer cc on c.id=cc.couponId where ");
            sql.append("  cc.`customerId`="+customerId+" and c.sendMax > cc.quantity and cc.deleteFlag=0 order by cc.createTime desc " );
        }
        if(condition.getState()==4){
        	 sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,cc.startTime,cc.endTime,c.pic,c.bindProduct,cc.quantity,sumQuantity,c.maximumDiscount from coupon c inner join coupon_customer cc on c.id=cc.couponId where ");
             sql.append("  cc.state<2 and cc.endTime <now() and cc.`customerId`="+customerId+" and cc.deleteFlag=0 order by cc.createTime desc " );
        }
        if(condition.getState()==5){//未使用 商城券
            sql.append("select c.id,c.name,c.useWhere,c.target,c.companyId,c.areaId,c.areaName,c.companyName,c.vmCode,c.type,c.way,c.money,c.deductionMoney,c.sendMax,cc.startTime,cc.endTime,c.pic,c.bindProduct,cc.quantity,sumQuantity,c.maximumDiscount from coupon c inner join coupon_customer cc on c.id=cc.couponId where ");
            sql.append(" c.useWhere=2 and cc.state<2 and cc.endTime>now()  and cc.deleteFlag=0 and cc.`customerId`="+customerId+"");
        }
        //inner join coupon_customer cc on c.id=cc.couponId where c.useWhere=2 and cc.customerId="+customerId+" order by cc.createTime desc
        List<Object> plist = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("我的优惠券 SQL 语句："+sql.toString());
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
            List<CouponBean> list = Lists.newArrayList();
            while (rs.next()) {
                CouponBean bean = new CouponBean();
                bean.setId(rs.getLong("id"));
                bean.setName(rs.getString("name"));
                bean.setUseWhere(rs.getInt("useWhere"));
                bean.setTarget(rs.getInt("target"));
                bean.setCompanyId(rs.getLong("companyId"));
                bean.setAreaId(rs.getLong("areaId"));
                bean.setAreaName(rs.getString("areaName"));
                bean.setCompanyName(rs.getString("companyName"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setType(rs.getInt("type"));
                bean.setWay(rs.getInt("way"));
                bean.setMoney(rs.getDouble("money"));
                bean.setDeductionMoney(rs.getDouble("deductionMoney"));
                bean.setSendMax(rs.getInt("sendMax"));
                bean.setStartTime(rs.getTimestamp("startTime"));
                bean.setEndTime(rs.getTimestamp("endTime"));
                bean.setPic(rs.getString("pic"));
                bean.setBindProduct(rs.getInt("bindProduct"));
                bean.setMaximumDiscount(rs.getDouble("maximumDiscount"));
                if(condition.getState()!=1) {
                	bean.setQuantity(rs.getLong("quantity"));
                	bean.setUseQuantity(rs.getLong("sendMax")-rs.getLong("quantity"));
                	if(bean.getWay().equals(5))
                	{
                    	bean.setUseQuantity(rs.getLong("sumQuantity")-rs.getLong("quantity"));
                	}
                }
                
              //  bean.setCreateTime(rs.getDate("createTime"));
              //  bean.setCreateUser(rs.getLong("createUser"));
              //  bean.setUpdateTime(rs.getDate("updateTime"));
               // bean.setUpdateUser(rs.getLong("updateUser"));
               // bean.setDeleteFlag(rs.getInt("deleteFlag"));
                //bean.setRemark(rs.getString("remark"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
                log.info(plist.toString());
            }
            data.setCurrentPage(condition.getCurrentPage());
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

    public ReturnDataUtil shopListPage(Integer way,Integer state,Integer companyId) {
		log.info("<CouponDaoImpl>------<listPage>--------------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select * from coupon c where deleteFlag=0 ");
		List<Object> plist = Lists.newArrayList();
		if (way != null) {
			sql.append(" and way = " + way);
		}

		if (state != null) {
			// DateTime dt1 = new DateTime();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sf.format(new Date());

			if (state == 1) {
				sql.append(" and startTime<'" + date + "' and endTime>='" + date + "'");
			}
		}
		if (companyId != null) {
			sql.append(" and target = 1 AND c.companyId = "+companyId);
		}
		sql.append(" order by createTime desc ");
		log.info("优惠券列表SQL语句======"+sql.toString());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			if (showSql) {
				log.info(sql);
			}
			List<CouponBean> list = Lists.newArrayList();
			while (rs.next()) {
				CouponBean bean = new CouponBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setType(rs.getInt("type"));
				bean.setWay(rs.getInt("way"));
				bean.setUseWhere(rs.getInt("useWhere"));
				bean.setTarget(rs.getInt("target"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setAreaId(rs.getLong("areaId"));
				bean.setAreaName(rs.getString("areaName"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setMoney(rs.getDouble("money"));
				bean.setSendMax(rs.getInt("sendMax"));
				bean.setDeductionMoney(rs.getDouble("deductionMoney"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getTimestamp("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getTimestamp("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setRemark(rs.getString("remark"));
				bean.setPic(rs.getString("pic"));
				bean.setBindProduct(rs.getInt("bindProduct"));
				bean.setPeriodDay(rs.getInt("periodDay"));
				bean.setPeriodType(rs.getInt("periodType"));
				bean.setCanSend(rs.getInt("canSend"));
				bean.setFormulaMode(rs.getInt("formulaMode"));
				bean.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				list.add(bean);
			}
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CouponDaoImpl>------<listPage>--------------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CouponDaoImpl>------<listPage>--------------end");
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
    public CouponBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        CouponBean entity = new CouponBean();
        return super.del(entity);
    }

    public boolean update(CouponBean entity) {
        return super.update(entity);
    }

    public CouponBean insert(CouponBean entity) {
        return super.insert(entity);
    }

    

	@Override
	public List<CouponDto> getGameCoupon(CouponForm couponForm) {
		log.info("<CouponDao1Impl--getGameCoupon--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,`name` FROM coupon WHERE 1=1 ");
		sql.append(" AND ( CASE");
		sql.append(" WHEN target = 1 AND companyId = "+couponForm.getCompanyId()+" THEN 1");
		sql.append(" WHEN target = 2 AND areaId = "+couponForm.getAreaId()+" THEN 1");
		sql.append(" WHEN target = 3 AND vmCode = '"+couponForm.getVmCode()+"' THEN 1");
		sql.append(" ELSE 0 END   ) = 1");
		sql.append(" AND startTime <= '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"' AND endTime >= '"+DateUtil.formatYYYYMMDDHHMMSS(new Date())+"'");
		if(StringUtil.isNotBlank(couponForm.getCouponName())){
			sql.append(" AND `name` LIKE '%"+couponForm.getCouponName()+"%';");
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponDto coupon = null;
		List<CouponDto> couponList = new ArrayList<CouponDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponDto();
				coupon.setId(rs.getLong("id"));
				coupon.setName(rs.getString("name"));
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDao1Impl--getGameCoupon--end>");
		return couponList;
	}


    @Override
	public CouponBean getCouponInfoByProduct(ShoppingGoodsBean shoppingGoodsBean) {
		log.info("<CouponDaoImpl--getCoupon--start>");
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode FROM coupon AS c where 1=1 ");
	
		if(shoppingGoodsBean.getTarget()==0) {
			sql.append(" and  name = '"+shoppingGoodsBean.getName()+"'");
		}
		else if(shoppingGoodsBean.getTarget()==1){
			sql.append(" and name = '" + shoppingGoodsBean.getName() + "'  and companyId = '"
					+ shoppingGoodsBean.getCompanyId() + "' ");
		}
		else if(shoppingGoodsBean.getTarget()==2){
			sql.append(" and name = '" + shoppingGoodsBean.getName() + "'  and companyId = '"
					+ shoppingGoodsBean.getCompanyId() + "' and areaId = '" + shoppingGoodsBean.getAreaId() + "' ");
		}
		else if(shoppingGoodsBean.getTarget()==3) {
			sql.append(" and name = '" + shoppingGoodsBean.getName() + "'  and vmCode ="+shoppingGoodsBean.getVmCode());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDaoImpl--getCoupon--end>");
		return coupon;
	}
    
	@Override
	public Integer insertCouponCustomer(CouponCustomerBean couCusBean) {
		log.info("<CouponDaoImpl--insertCouponCustomer--end>");
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO `coupon_customer`(couponId,customerId,state,startTime,");
		sql.append(" endTime,createTime,createUser,updateTime,updateUser,deleteFlag,quantity,sumQuantity)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		List<Object> param = new ArrayList<Object>();
		param.add(couCusBean.getCouponId());
		param.add(couCusBean.getCustomerId());
		param.add(couCusBean.getState());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(couCusBean.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(couCusBean.getEndTime()));
		param.add(now);
		param.add(couCusBean.getCustomerId());
		param.add(now);
		param.add(couCusBean.getCustomerId());
		param.add(0);//1 删除，0 未删除
		param.add(couCusBean.getQuantity());
		param.add(couCusBean.getSumQuantity());
		int insertGetID = insertGetID(sql.toString(),param);
		log.info("<CouponDaoImpl--insertCouponCustomer--end>");
		return insertGetID;
	}
	
	@Override
	public CouponCustomerBean getCouponCustomerBean(Long customerId, Long couponId) {
		log.info("<CouponDaoImpl--getCouponCustomerBean--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM `coupon_customer` WHERE customerId = "+customerId+" AND couponId = "+couponId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReceive = false;
        CouponCustomerBean bean = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				bean =new CouponCustomerBean();
                bean.setId(rs.getLong("id"));
                bean.setCouponId(rs.getLong("couponId"));
                bean.setCustomerId(rs.getLong("customerId"));
                bean.setState(rs.getInt("state"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setCreateUser(rs.getLong("createUser"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setUpdateUser(rs.getLong("updateUser"));
                bean.setDeleteFlag(rs.getInt("deleteFlag"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDaoImpl--getCouponCustomerBean--end>");
		return bean;
	}
	@Override
    public Boolean updateCouponCustomerBean(CouponCustomerBean entity) {
		log.info("<CouponDaoImpl--CouponCustomerBean--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("update coupon_customer set quantity ="+entity.getQuantity()+" ,sumQuantity = "+entity.getSumQuantity()+" where id="+entity.getId());
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rs = null;
		boolean updateCouponCustomer= false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeUpdate();
			if(rs!=null && rs>0){
				updateCouponCustomer = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(null, ps, conn);
		}
		log.info("<CouponDaoImpl--CouponCustomerBean--end>");
		return updateCouponCustomer;
    }
	
	@Override
	public List<CouponBean> getPresentCoupon(CouponForm couponForm) {
		log.info("<CouponDaoImpl>----<getPresentCoupon>------start");
		String now = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT c.`id`,c.`name`,c.`useWhere`,c.`target`,c.`companyId`,c.`areaId`,");
		sql.append(" c.`vmCode`,c.`bindProduct`,c.`periodType`,c.`periodDay`,c.`canSend`,");
		sql.append(" c.`type`,c.`way`,c.`money`,c.`deductionMoney`,c.`sendMax`,c.`startTime`,c.`endTime`,");
		sql.append(" c.`deleteFlag`,c.`remark`,c.`pic`,c.formulaMode,c.maximumDiscount FROM coupon AS c ");
		sql.append(" WHERE c.`way` = "+couponForm.getWay()+" AND c.deleteFlag = 0");
		if(couponForm.getUseWhere()!=null){
			sql.append("  AND c.useWhere = "+couponForm.getUseWhere());
		}
		sql.append(" AND c.canSend = 0");
		sql.append(" AND c.`createTime` <= '"+now+"' AND c.`endTime` >= '"+now+"'");
		if(couponForm.getLimitRange()){
			sql.append(" AND (CASE WHEN c.useWhere = 2 THEN 1");
			sql.append(" WHEN c.useWhere = 1 AND c.target = 1 AND c.companyId = "+couponForm.getCompanyId()+" THEN 1");
			sql.append(" WHEN c.useWhere = 1 AND c.target = 2 AND c.areaId = "+couponForm.getAreaId()+" THEN 1");
			sql.append(" WHEN c.useWhere = 1 AND c.target = 3 AND c.vmCode = "+couponForm.getVmCode()+" THEN 1");
			sql.append(" ELSE 0 END) = 1");
		}
		log.info("获取优惠券信息 sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean coupon = null;
		List<CouponBean> couponList = new ArrayList<CouponBean>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				coupon = new CouponBean();
				coupon.setAreaId(rs.getLong("areaId"));
				coupon.setBindProduct(rs.getInt("bindProduct"));
				coupon.setCanSend(rs.getInt("canSend"));
				coupon.setCompanyId(rs.getLong("companyId"));
				coupon.setDeductionMoney(rs.getDouble("deductionMoney"));
				coupon.setDeleteFlag(rs.getInt("deleteFlag"));
				coupon.setId(rs.getLong("id"));
				coupon.setMoney(rs.getDouble("money"));
				coupon.setName(rs.getString("name"));
				coupon.setPeriodDay(rs.getInt("periodDay"));
				coupon.setPeriodType(rs.getInt("periodType"));
				coupon.setPic(rs.getString("pic"));
				coupon.setSendMax(rs.getInt("sendMax"));
				coupon.setUseWhere(rs.getInt("useWhere"));
				coupon.setTarget(rs.getInt("target"));
				coupon.setVmCode(rs.getString("vmCode"));
				coupon.setType(rs.getInt("type"));
				coupon.setWay(rs.getInt("way"));
				coupon.setStartTime(rs.getTimestamp("startTime"));
				coupon.setEndTime(rs.getTimestamp("endTime"));
				coupon.setRemark(rs.getString("remark"));
				coupon.setFormulaMode(rs.getInt("formulaMode"));
				coupon.setMaximumDiscount(rs.getDouble("maximumDiscount"));
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDaoImpl>----<getPresentCoupon>------end");
		return couponList;
	}
	
	@Override
	public boolean isReceive(Long customerId, Long couponId) {
		log.info("<CouponDaoImpl--receiveNum--start>");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT 1 AS times FROM `coupon_customer` WHERE customerId = "+customerId+" AND couponId = "+couponId);
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isReceive = false;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				isReceive = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<CouponDaoImpl--receiveNum--end>");
		return isReceive;
	}
}

