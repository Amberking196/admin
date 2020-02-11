package com.server.module.system.machineManage.machinesAdvertising;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
@Repository
public class VendingMachinesAdvertisingDaoImpl extends BaseDao<VendingMachinesAdvertisingBean>
		implements VendingMachinesAdvertisingDao {

	private static Logger log = LogManager.getLogger(VendingMachinesAdvertisingDaoImpl.class);

	/**
	 * 查询售货机广告
	 */
	public ReturnDataUtil listPage(VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm) {
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<listPage>-------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select vma.id,vma.companyId,vma.homeImg,vma.themeImg,vma.homeUrl,vma.themeUrlOne,vma.themeUrlTwo,vma.createTime,vma.createUser,vma.updateTime,vma.updateUser,vma.deleteFlag,c.name as companyName ");
		sql.append(",vma.state,vma.staySecond,vma.showStyle,vma.height,vma.width,vma.advType,vma.dateType,vma.startTime,vma.endTime ");
		sql.append("  from vending_machines_advertising vma left join company c ");
		sql.append(" on vma.companyId=c.id where vma.deleteFlag=0 ");
		if(vendingMachinesAdvertisingForm.getCompanyId()!=null) {
			sql.append(" and vma.companyId='"+vendingMachinesAdvertisingForm.getCompanyId()+"'");
		}
		sql.append(" order by vma.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("查询售货机广告sql语句："+sql.toString());
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (vendingMachinesAdvertisingForm.getCurrentPage() - 1) * vendingMachinesAdvertisingForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + vendingMachinesAdvertisingForm.getPageSize());
			rs = pst.executeQuery();
			List<VendingMachinesAdvertisingBean> list = Lists.newArrayList();
			while (rs.next()) {
				VendingMachinesAdvertisingBean bean = new VendingMachinesAdvertisingBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setHomeImg(rs.getString("homeImg"));
				bean.setThemeImg(rs.getString("themeImg"));
				bean.setHomeUrl(rs.getString("homeUrl"));
				bean.setThemeUrlOne(rs.getString("themeUrlOne"));
				bean.setThemeUrlTwo(rs.getString("themeUrlTwo"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setCompanyName(rs.getString("companyName"));

				bean.setHeight(rs.getInt("height"));
				bean.setWidth(rs.getInt("width"));
				bean.setState(rs.getInt("state"));
				bean.setShowStyle(rs.getInt("showStyle"));
				bean.setAdvType(rs.getInt("advType"));
				bean.setStaySecond(rs.getInt("staySecond"));
				bean.setDateType(rs.getInt("dateType"));
				bean.setStartTime(rs.getString("startTime"));
				bean.setEndTime(rs.getString("endTime"));
				list.add(bean);
			}
			data.setCurrentPage(vendingMachinesAdvertisingForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<VendingMachinesAdvertisingDaoImpl>----------<listPage>-------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<VendingMachinesAdvertisingDaoImpl>----------<listPage>-------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public VendingMachinesAdvertisingBean get(Object id) {
		return super.get(id);
	}

	/**
	 * 删除售货机广告
	 */
	public boolean delete(Object id) {
		log.info("<VendingMachinesAdvertisingDaoImpl>-----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  vending_machines_advertising set deleteFlag= 1 where id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除售货机广告sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<VendingMachinesAdvertisingDaoImpl>-----<delete>-----end");
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

	/**
	 * 修改售货机广告
	 */
	public boolean update(VendingMachinesAdvertisingBean entity) {
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<update>-------start");
		boolean update = super.update(entity);
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<update>-------end");
		return update;
	}

	/**
	 * 增加售货机广告
	 */
	public VendingMachinesAdvertisingBean insert(VendingMachinesAdvertisingBean entity) {
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<insert>-------start");
		VendingMachinesAdvertisingBean bean = super.insert(entity);
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<insert>-------end");
		return bean;
	}

	/**
	 * 手机端广告页查询
	 */
	public List<VendingMachinesAdvertisingBean> list(VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm) {
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<list>-------start");
		List<VendingMachinesAdvertisingBean> list = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append("select vma.id,vma.companyId,vma.homeImg,vma.themeImg,vma.homeUrl,vma.themeUrlOne,vma.themeUrlTwo,vma.createTime,vma.createUser,vma.updateTime,vma.updateUser,vma.deleteFlag ");
		sql.append("  from vending_machines_advertising vma  ");
		sql.append("  where  vma.state=0 and vma.deleteFlag=0 ");
		if(StringUtil.isNotBlank(vendingMachinesAdvertisingForm.getVmCode())) {
			sql.append("  and FIND_IN_SET((select companyId from vending_machines_info where code='"+vendingMachinesAdvertisingForm.getVmCode()+"'),getChildList(vma.companyId)) ");
		}
		sql.append(" order by vma.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("手机端广告页查询sql语句："+sql.toString());
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				VendingMachinesAdvertisingBean bean = new VendingMachinesAdvertisingBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setHomeImg(rs.getString("homeImg"));
				bean.setThemeImg(rs.getString("themeImg"));
				bean.setHomeUrl(rs.getString("homeUrl"));
				bean.setThemeUrlOne(rs.getString("themeUrlOne"));
				bean.setThemeUrlTwo(rs.getString("themeUrlTwo"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			log.info("<VendingMachinesAdvertisingDaoImpl>----------<list>-------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<VendingMachinesAdvertisingDaoImpl>----------<list>-------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public String findVendingSlideshow(String vmCode) {
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<findVendingSlideshow>-------start");
		StringBuilder sql = new StringBuilder();
		sql.append("select vma.id,vma.companyId,vma.homeImg,vma.themeImg,vma.homeUrl,vma.themeUrlOne,vma.themeUrlTwo,vma.createTime,vma.createUser,vma.updateTime,vma.updateUser,vma.deleteFlag ");
		sql.append("  from vending_machines_advertising vma  ");
		sql.append("  where vma.deleteFlag=0  and vma.state=0 and vma.advType=1");
		sql.append("  and FIND_IN_SET((select companyId from vending_machines_info where code='"+vmCode+"'),getChildList(vma.companyId)) ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询机器首页的充值图片sql语句："+sql.toString());
		String homeImg=null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				homeImg=rs.getString("homeImg");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingMachinesAdvertisingDaoImpl>----------<findVendingSlideshow>-------end");
		return homeImg;
	}
	
	@Override
	public List<VendingMachinesAdvertisingBean> findVendingAdvertisingMachinesBean(String vmCode) {
		log.info("<VendingMachinesAdvertisingDaoImpl>-----<findVendingAdvertisingMachinesBean>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select v.vmCode,a.id advertisingId ,a.homeImg,a.themeImg,a.staySecond,a.showStyle,a.height,a.width ");
		sql.append(" from vending_advertising_machines v left join vending_machines_advertising a on v.advertisingId=a.id ");
		sql.append(" where a.state=0  and a.deleteFlag=0 and a.advType=0 and v.vmCode='"+vmCode+"' ");
		sql.append(" order by a.createTime desc  ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("查询售货机广告sql语句"+sql.toString());
        List<VendingMachinesAdvertisingBean> list= Lists.newArrayList();
        VendingMachinesAdvertisingBean bean=null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs!=null&&rs.next()) {
            	bean=new VendingMachinesAdvertisingBean();
            	bean.setHomeImg(rs.getString("homeImg"));
            	bean.setThemeImg(rs.getString("themeImg"));
            	bean.setStaySecond(rs.getInt("staySecond"));
            	bean.setShowStyle(rs.getInt("showStyle"));
            	bean.setHeight(rs.getInt("height"));
            	bean.setWidth(rs.getInt("width"));
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.closeConnection(rs, pst, conn);
        }
        log.info("<VendingMachinesAdvertisingDaoImpl>-----<findVendingAdvertisingMachinesBean>-----end");
		return list;
	}

	@Override
	public boolean findAdvertisingMachinesBeanByCompanyId(Long companyId) {
		log.info("<VendingMachinesAdvertisingDaoImpl>-----<findAdvertisingMachinesBeanByCompanyId>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select id,companyId,homeImg,themeImg,homeUrl,createTime,state,deleteFlag,advType  from vending_machines_advertising ");
		sql.append(" where  state=0 and deleteFlag=0 and  FIND_IN_SET(companyId,getChildList("+companyId+")) ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("查询公司下  是否已经存在公司弹窗了"+sql.toString());
        boolean flag=false;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs!=null&&rs.next()) {
            	flag=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.closeConnection(rs, pst, conn);
        }
        log.info("<VendingMachinesAdvertisingDaoImpl>-----<findAdvertisingMachinesBeanByCompanyId>-----end");
		return flag;
	}
}
