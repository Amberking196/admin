package com.server.module.system.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
@Repository
public class CarryWaterVouchersCustomerDaoImpl extends BaseDao<CarryWaterVouchersCustomerBean>
		implements CarryWaterVouchersCustomerDao {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersCustomerDaoImpl.class);

	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;
	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private UserUtils userUtils;
	/**
	 * 提水券用户列表
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<listPage>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select c.id,c.carryId,c.customerId,c.quantity,c.useQuantity,c.startTime,c.endTime,c.createTime,c.createUser,c.updateTime,c.updateUser,c.deleteFlag ");
		sql.append(" ,t.phone,t.createTime userCrateTime,cw.name carryName ");
		sql.append(" from carry_water_vouchers_customer c left join tbl_customer  t on  c.customerId=t.id ");
		sql.append(" left join carry_water_vouchers cw on c.carryId=cw.id  where c.deleteFlag=0 ");
		if(carryWaterVouchersCustomerForm.getCarryId()!=null) {
			sql.append(" and c.carryId ='"+carryWaterVouchersCustomerForm.getCarryId()+"' ");
		}
		if(StringUtil.isNotBlank(carryWaterVouchersCustomerForm.getPhone())) {
			sql.append(" and t.phone='"+carryWaterVouchersCustomerForm.getPhone().trim()+"' ");
		}
		if(carryWaterVouchersCustomerForm.getCustomerId()!=null) {
			sql.append(" and t.id='"+carryWaterVouchersCustomerForm.getCustomerId()+"' ");
		}
		sql.append(" order by c.createTime desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("提水券用户列表sql语句："+sql);
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (carryWaterVouchersCustomerForm.getCurrentPage() - 1) * carryWaterVouchersCustomerForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + carryWaterVouchersCustomerForm.getPageSize());
			rs = pst.executeQuery();
			List<CarryWaterVouchersCustomerBean> list = Lists.newArrayList();
			while (rs.next()) {
				CarryWaterVouchersCustomerBean bean = new CarryWaterVouchersCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setCarryId(rs.getLong("carryId"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setStartTime(rs.getTimestamp("startTime"));
				bean.setEndTime(rs.getTimestamp("endTime"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setPhone(rs.getString("phone"));
				bean.setUserCreateTime(rs.getTimestamp("userCrateTime"));
				bean.setUseQuantity(rs.getLong("useQuantity"));
				bean.setCarryName(rs.getString("carryName"));
				bean.setSurplusQuantity(rs.getLong("quantity")-rs.getLong("useQuantity"));
				list.add(bean);
			}
			data.setCurrentPage(carryWaterVouchersCustomerForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<listPage>-----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<listPage>-----end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public CarryWaterVouchersCustomerBean get(Object id) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<get>-----start");
		 CarryWaterVouchersCustomerBean bean = super.get(id);
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<get>-----end");
		return bean;
	}

	public boolean delete(Object id) {
		CarryWaterVouchersCustomerBean entity = new CarryWaterVouchersCustomerBean();
		return super.del(entity);
	}

	/**
	 * 更新用户提水券
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<update>----start>");
		Date date = entity.getEndTime();
		String endTime = DateUtil.formatYYYYMMDDHHMMSS(date);
		StringBuffer sql = new StringBuffer();
		sql.append("update carry_water_vouchers_customer set quantity ="+entity.getQuantity()+" , endTime='"+endTime+"'  where id="+entity.getId());
		log.info("更新用户提水券sql语句："+sql);
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
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<update>----end>");
		return updateCouponCustomer;
	}

	/**
	 * 提水券绑定用户
	 */
	public CarryWaterVouchersCustomerBean insert(CarryWaterVouchersCustomerBean entity) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<insert>------start");
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into carry_water_vouchers_customer (customerId,startTime,endTime,carryId,quantity,createUser,orderId) ");
		sql.append(" values(?,?,?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(entity.getCustomerId());
		param.add(DateUtil.formatYYYYMMDDHHMMSS(entity.getStartTime()));
		param.add(DateUtil.formatYYYYMMDDHHMMSS(entity.getEndTime()));
		param.add(entity.getCarryId());
		param.add(entity.getQuantity());
		param.add(entity.getCreateUser());
		param.add(entity.getOrderId());
		insertGetID(sql.toString(),param);
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<insert>------end");
		return entity;
	}

	/**
	 * 提水券范围内用户列表
	 */
	@SuppressWarnings({ "resource", "unused" })
	public ReturnDataUtil listPageForCustomer(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-----<listPageForCustomer>------start");
       //得到提水券信息
		Long carryId = carryWaterVouchersCustomerForm.getCarryId();
		CarryWaterVouchersBean bean = carryWaterVouchersDaoImpl.get(carryId);
		//得到提水券公司id 查询出子公司
		String companyIds = companyDaoImpl.findAllSonCompanyIdForInSql(bean.getCompanyId().intValue());
        int target=bean.getTarget();
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        String select="select c.phone,c.createTime,c.id as customerId,cc.id as carryCustomerId from ";
        sql.append(select);
        if(target==1){//公司
        	sql.append(" vending_machines_info i inner join tbl_customer c on i.code=c.vmCode left join carry_water_vouchers_customer ");
            sql.append(" cc on (c.id=cc.customerId and cc.carryId="+carryId+") where i.companyId in "+companyIds+"");
        }else if(target==2){//区域
            sql.append(" vending_machines_info i inner join tbl_customer c on i.code=c.vmCode left join carry_water_vouchers_customer ");
            sql.append(" cc on (c.id=cc.customerId and cc.carryId="+carryId+") where i.areaId="+bean.getAreaId());
        }else{//3机器
            sql.append(" tbl_customer c left join carry_water_vouchers_customer ");
            sql.append(" cc on (c.id=cc.customerId and cc.carryId="+carryId+")  where c.vmCode in ("+bean.getVmCode()+")");
        }
        sql.append(" and c.phone is not null ");
        if(StringUtil.isNotBlank(carryWaterVouchersCustomerForm.getPhone())){
            sql.append(" and c.phone="+carryWaterVouchersCustomerForm.getPhone().trim());
        }
        //是否发送
        if(CarryWaterVouchersCustomerForm.ISSend==carryWaterVouchersCustomerForm.getIsSend()){
            sql.append(" and exists(select m.id from carry_water_vouchers_customer m where c.id=m.customerId and m.carryId="+carryId+")");
        }else if(CarryWaterVouchersCustomerForm.NOSend==carryWaterVouchersCustomerForm.getIsSend()){
            sql.append(" and not exists(select m.id from carry_water_vouchers_customer m where c.id=m.customerId and m.carryId="+carryId+")");
        }
        if (showSql) {
            log.info("提水券范围内用户列表："+sql.toString());
        }
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
            long off = (carryWaterVouchersCustomerForm.getCurrentPage() - 1) * carryWaterVouchersCustomerForm.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + carryWaterVouchersCustomerForm.getPageSize());
            rs = pst.executeQuery();
           List<CarryWaterVouchersCustomerBean> list = Lists.newArrayList();
            while (rs.next()) {
            	CarryWaterVouchersCustomerBean vo = new CarryWaterVouchersCustomerBean();
                vo.setCarryId(carryId);
                vo.setCustomerId(rs.getLong("customerId"));
                vo.setUserCreateTime(rs.getTimestamp("createTime"));
                vo.setPhone(rs.getString("phone"));
                vo.setId(rs.getLong("carryCustomerId"));
                if(vo.getId()==0){
                    vo.setSendState("未发送");
                }else{
                    vo.setSendState("已发送");
                }
                list.add(vo);
            }

            data.setCurrentPage(carryWaterVouchersCustomerForm.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            log.info("<CarryWaterVouchersCustomerDaoImpl>-----<listPageForCustomer>------end");
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("<CarryWaterVouchersCustomerDaoImpl>-----<listPageForCustomer>------end");
            return data;
        } finally {
            this.closeConnection(rs, pst, conn);
        }
	}

	/**
	 * 获取用户绑定提水券信息
	 */
	@Override
	public CarryWaterVouchersCustomerBean getCarryWaterVouchersCustomerBean(Long customerId, Long carryId) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<getCarryWaterVouchersCustomerBean>-----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		CarryWaterVouchersCustomerBean bean=null;
		sql.append("select c.id,c.carryId,c.customerId,c.quantity,c.startTime,c.endTime,c.createTime,c.createUser,c.updateTime,c.updateUser,c.deleteFlag ");
		sql.append(" from carry_water_vouchers_customer c where c.carryId='"+carryId+"' and c.customerId='"+customerId+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("获取用户绑定提水券信息sql语句："+sql);
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				bean = new CarryWaterVouchersCustomerBean();
				bean.setId(rs.getLong("id"));
				bean.setCarryId(rs.getLong("carryId"));
				bean.setCustomerId(rs.getLong("customerId"));
				bean.setQuantity(rs.getLong("quantity"));
				bean.setStartTime(rs.getDate("startTime"));
				bean.setEndTime(rs.getDate("endTime"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
			
			}
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<getCarryWaterVouchersCustomerBean>-----end");
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersCustomerDaoImpl>-------<getCarryWaterVouchersCustomerBean>-----end");
			return bean;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	/**
	 * 手机端 获取用户提水券信息
	 */
	@Override
	public List<CarryWaterVouchersCustomerDto> findCustomerIdByCarryWaterVouchersCustomerDto(
			CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<findCustomerIdByCarryWaterVouchersCustomerDto>-----start");
	    List<CarryWaterVouchersCustomerDto> list = Lists.newArrayList();
        Long customerId= CustomerUtil.getCustomerId();
        if(customerId==null) {
        	customerId=userUtils.getSmsUser().getId();
        }
        StringBuilder sql = new StringBuilder();
        // private int state  1 未使用   2 已使用   3  已过期   
        sql.append("select w.name carryName,w.remark,c.id,c.carryId,c.customerId,c.quantity,c.useQuantity,c.startTime,c.endTime  from carry_water_vouchers_customer  c  "); 
        sql.append(" inner join carry_water_vouchers w on c.carryId=w.id where c.deleteFlag=0  and c.customerId='"+customerId+"'");
        if(carryWaterVouchersCustomerForm.getState()==1){//未使用
        	sql.append(" and c.endTime > now() and useQuantity<quantity  ");
        }
        if(carryWaterVouchersCustomerForm.getState()==2){//已使用
        	sql.append(" and c.endTime >now() and c.useQuantity>0  ");
        }
        if(carryWaterVouchersCustomerForm.getState()==3){//已过期  
           sql.append(" and c.endTime < now() ");
        }
        sql.append(" order by c.createTime desc ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("我的提水券 SQL 语句："+sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
            	int id=0;
            	CarryWaterVouchersCustomerDto bean=new CarryWaterVouchersCustomerDto();
            	bean.setId(id);
            	bean.setCarryId(rs.getLong("carryId"));
            	bean.setCarryName(rs.getString("carryName"));
            	bean.setCustomerId(rs.getLong("customerId"));
            	bean.setStartTime(rs.getDate("startTime"));
            	bean.setEndTime(rs.getDate("endTime"));
            	bean.setQuantity(rs.getLong("quantity"));
            	bean.setUseQuantity(rs.getLong("useQuantity"));
            	bean.setSurplusQuantity(rs.getLong("quantity")-rs.getLong("useQuantity"));
            	bean.setRemark(rs.getString("remark"));
            	bean.setIsShow(1);
                list.add(bean);
                id++;
            }
            log.info("<CarryWaterVouchersCustomerDaoImpl>-------<findCustomerIdByCarryWaterVouchersCustomerDto>-----end");
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("<CarryWaterVouchersCustomerDaoImpl>-------<findCustomerIdByCarryWaterVouchersCustomerDto>-----end");
            return list;
        } finally {
          this.closeConnection(rs, pst, conn);
        }
	}
	
	/**
	 * 后台修改用户提水券数量
	 */
	public boolean updateQuantity(CarryWaterVouchersCustomerBean entity) {
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<updateQuantity>----start>");
		StringBuffer sql = new StringBuffer();
		sql.append("update carry_water_vouchers_customer set  quantity=useQuantity+"+entity.getQuantity()+"  where id="+entity.getId());
		log.info("后台修改用户提水券数量sql语句："+sql);
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
		log.info("<CarryWaterVouchersCustomerDaoImpl>------<updateQuantity>----end>");
		return updateCouponCustomer;
	}
	
	public List<CarryWaterVouchersCustomerDto> queryCarryWaterCustomerDto(Long orderId){
		log.info("<CarryWaterVouchersCustomerDaoImpl>-------<queryCarryWaterCustomerDto>-----start");
	    List<CarryWaterVouchersCustomerDto> list = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id,c.name carryName,c.remark,cc.customerId,cc.startTime,cc.endTime,cc.quantity,cc.useQuantity from carry_water_vouchers c  ");
        sql.append(" left join carry_water_vouchers_customer cc on c.id=cc.carryId ");
        sql.append(" where cc.orderId='"+orderId+"' and cc.customerId='"+CustomerUtil.getCustomerId()+"' ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("用户订单提水券信息 SQL 语句："+sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
            	CarryWaterVouchersCustomerDto bean=new CarryWaterVouchersCustomerDto();
            	bean.setCarryId(rs.getLong("id"));
            	bean.setCarryName(rs.getString("carryName"));
            	bean.setCustomerId(rs.getLong("customerId"));
            	bean.setStartTime(rs.getDate("startTime"));
            	bean.setEndTime(rs.getDate("endTime"));
            	bean.setQuantity(rs.getLong("quantity"));
            	bean.setUseQuantity(rs.getLong("useQuantity"));
            	bean.setSurplusQuantity(rs.getLong("quantity")-rs.getLong("useQuantity"));
            	bean.setRemark(rs.getString("remark"));
                list.add(bean);
            }
            log.info("<CarryWaterVouchersCustomerDaoImpl>-------<queryCarryWaterCustomerDto>-----end");
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.info("<CarryWaterVouchersCustomerDaoImpl>-------<queryCarryWaterCustomerDto>-----end");
            return list;
        } finally {
          this.closeConnection(rs, pst, conn);
        }
	}
}
