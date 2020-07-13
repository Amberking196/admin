package com.server.module.system.promotionManage.activityProduct;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.couponManager.couponProduct.CouponProductVo;
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
import java.util.List;

/**
 * author name: why create time: 2018-08-23 18:07:34
 */
@Repository
public class ActivityProductDaoImpl extends BaseDao<ActivityProductBean> implements ActivityProductDao {

	private static Logger log = LogManager.getLogger(ActivityProductDaoImpl.class);

	/**
	 * 活动商品列表查询
	 */
	public ReturnDataUtil listPage(ActivityProductForm activityProductForm) {
		log.info("<ActivityProductDaoImpl>----<listPage>------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,activityId,productId,createTime,createUser,updateTime,updateUser,deleteFlag ");
		sql.append(" from activity_product where 1=1 ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("活动商品列表查询 sql 语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (activityProductForm.getCurrentPage() - 1) * activityProductForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + activityProductForm.getPageSize());
			rs = pst.executeQuery();
			List<ActivityProductBean> list = Lists.newArrayList();
			while (rs.next()) {
				ActivityProductBean bean = new ActivityProductBean();
				bean.setId(rs.getLong("id"));
				bean.setActivityId(rs.getLong("activityId"));
				bean.setProductId(rs.getLong("productId"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(activityProductForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<ActivityProductDaoImpl>----<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<ActivityProductDaoImpl>----<listPage>-----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public ActivityProductBean get(Object id) {
		return super.get(id);
	}

	/**
	 * 解绑商品
	 */
	public boolean delete(Object id) {
		log.info("<ActivityProductDaoImpl>-----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  activity_product set deleteFlag= 1 where id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("解绑商品 sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<ActivityProductDaoImpl>-----<delete>-----end");
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

	public boolean update(ActivityProductBean entity) {
		return super.update(entity);
	}

	/**
	 * 绑定商品
	 */
	public ActivityProductBean insert(ActivityProductBean entity) {
		log.info("<ActivityProductDaoImpl>----<insert>------start");
		ActivityProductBean insert = super.insert(entity);
		log.info("<ActivityProductDaoImpl>----<insert>------end");
		return insert;
	}

	/**
	 * 商品列表
	 */
	public ReturnDataUtil list(ActivityProductForm activityProductForm) {
		ReturnDataUtil data = new ReturnDataUtil();
		 	StringBuilder sql = new StringBuilder();
	        int useWhere=activityProductForm.getUseWhere();
	        int activityId=activityProductForm.getActivityId();
	        int isBind=activityProductForm.getIsBind();
	        String name=activityProductForm.getName();

	        //1 机器活动   2 商城活动
	        if(useWhere==1){
	            sql.append("select k.* from (select c.id,c.activityId,g.name,g.id AS productId from item_basic g  left join (select * from activity_product where  deleteFlag=0 and activityId="+activityId+" ) c ON c.productId=g.id ) k where 1=1 ");
	        }else if(useWhere==2){
	            sql.append("select k.* from (select c.id,c.activityId,g.name,g.id AS productId from shopping_goods g  left join (select * from activity_product where  deleteFlag=0 and activityId="+activityId+" ) c ON c.productId=g.id ) k where 1=1 ");
	        }
	        
	        if(isBind==0){//未绑定
	            sql.append(" and k.activityId IS  NULL");
	        }else if(isBind==1){// 1 已绑定
	            sql.append(" and k.activityId IS not NULL");
	        }
	        
	        if(StringUtil.isNotBlank(name)){
	            sql.append(" and k.name like '%"+name.trim()+"%'");
	        }
	        Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
	        log.info("==商品列表==sql:"+sql.toString());
	        try {
	        	conn = openConnection();
				pst = conn.prepareStatement(super.countSql(sql.toString()));
				rs = pst.executeQuery();
				long count = 0;
				while (rs.next()) {
					count = rs.getInt(1);
				}
				long off = (activityProductForm.getCurrentPage() - 1) * activityProductForm.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + activityProductForm.getPageSize());
				rs = pst.executeQuery();
		        List<ActivityProductVo> list = Lists.newArrayList();
	            while (rs.next()) {
	            	ActivityProductVo bean = new ActivityProductVo();
	                bean.setActivityId(rs.getInt("activityId"));
	                bean.setProductId(rs.getLong("productId"));
	                bean.setProductName(rs.getString("name"));
	                bean.setId(rs.getInt("id"));
	                if(rs.getInt("activityId")==0) {
	                    bean.setBindLabel("未绑定");
	                }else  {
	                    bean.setBindLabel("已绑定");
	                }
	                list.add(bean);
	            }
	            data.setCurrentPage(activityProductForm.getCurrentPage());
				data.setTotal(count);
				data.setReturnObject(list);
				data.setStatus(1);
	            return data;
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            return data;
	        } finally {
	          this.closeConnection(rs, pst, conn);
	        }
	}
}
