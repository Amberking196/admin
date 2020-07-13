package com.server.module.system.warehouseManage.supplierManage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-05-21 16:58:08
 */ 
@Repository
public class  SupplierDaoImpl extends BaseDao<SupplierBean> implements SupplierDao{

	public static Logger log = LogManager.getLogger(SupplierServiceImpl.class);
	
	@Autowired
	private CompanyDao companyDaoImpl;
	
	public ReturnDataUtil listPage(SupplierForm supplierForm) {
		log.info("<SupplierDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select s.id,address,companyName,s.phone,s.name,s.createTime,remark,delFlag,c.name as companyName1,companyId from supplier s ");
		sql.append(" left join company c on c.id=s.companyId");
		sql.append(" where 1=1 and delFlag = 0");
		if(StringUtils.isNotEmpty(supplierForm.getName())) {
			sql.append(" and s.name = '"+supplierForm.getName()+"'");
		}
		if(StringUtils.isNotEmpty(supplierForm.getCompanyName())) {
			sql.append(" and s.companyName like '%"+supplierForm.getCompanyName()+"%'");
		}
		if(supplierForm.getStartDate()!=null){
			sql.append(" and s.createTime >= '"+DateUtil.formatYYYYMMDDHHMMSS(supplierForm.getStartDate())+"'");
		}
		if(supplierForm.getEndDate()!=null){
			sql.append(" and s.createTime < '"+DateUtil.formatLocalYYYYMMDDHHMMSS(supplierForm.getEndDate(),1)+"'");
		}
		
		sql.append(" and  companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" order by s.createTime desc");

		log.info("供应商列表>>>:"+sql.toString());
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
			long off = (supplierForm.getCurrentPage() - 1) * supplierForm.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + supplierForm.getPageSize());
			rs = pst.executeQuery();
			List<SupplierBean> list=Lists.newArrayList();
			while(rs.next()){
				SupplierBean bean = new SupplierBean();
				bean.setId(rs.getLong("id"));
				bean.setAddress(rs.getString("address"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setPhone(rs.getString("phone"));
				bean.setName(rs.getString("name"));
				bean.setCreateDate(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setDelFlag(rs.getInt("delFlag"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setCompanyName1(rs.getString("companyName1"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setCurrentPage(supplierForm.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			data.setPageSize(supplierForm.getPageSize());
			log.info("<SupplierDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
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

	public List<SupplierVoForSelect> listForSelect() {
		StringBuilder sql=new StringBuilder();
		sql.append("select s.id,address,companyName,s.phone,s.name,s.createTime,remark,delFlag,c.name as companyName1,companyId from supplier s ");
		sql.append(" left join company c on c.id=s.companyId");
		sql.append(" where 1=1 and delFlag = 0");


		log.info("供应商列表>>>:"+sql.toString());
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		List<SupplierVoForSelect> list=Lists.newArrayList();

		try {
			conn=openConnection();

			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				SupplierVoForSelect bean = new SupplierVoForSelect();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("companyName"));
				list.add(bean);
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

	public SupplierBean get(Object id) {
		return super.get(id);
	}
	
	public boolean delete(Object id) {
		SupplierBean entity=new SupplierBean();
		return super.del(entity);
	}
	
	public boolean update(SupplierBean entity) {
		return super.update(entity);
	}
	
	public SupplierBean insert(SupplierBean entity) {
		return super.insert(entity);
	}

	/**
	 * 模糊搜索供应商
	 */
	@Override
	public List<SupplierBean> findBean(String name) {
		log.info("<SupplierDaoImpl>----<findBean>----start");
		StringBuilder sql=new StringBuilder();
		List<SupplierBean> list=Lists.newArrayList();
		sql.append("select s.id,address,companyName,s.phone,s.name,s.createTime,remark,delFlag,companyId from supplier s ");
		sql.append(" where 1=1 and delFlag = 0");
		//sql.append(" and  companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" and s.companyName like '%"+name+"%'");
		log.info("模糊搜索供应商>>>:"+sql.toString());
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
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while(rs.next()){
				SupplierBean bean = new SupplierBean();
				bean.setId(rs.getLong("id"));
				bean.setAddress(rs.getString("address"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setPhone(rs.getString("phone"));
				bean.setName(rs.getString("name"));
				bean.setCreateDate(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setDelFlag(rs.getInt("delFlag"));
				bean.setCompanyId(rs.getInt("companyId"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			log.info("<SupplierDaoImpl>----<findBean>----end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<SupplierDaoImpl>----<findBean>----end");
			return list;
		} finally{
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public ReturnDataUtil findAll() {
		log.info("<SupplierDaoImpl>----<listPage>----start");
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuilder sql=new StringBuilder();
		sql.append("select s.id,address,companyName,s.phone,s.name,s.createTime,remark,delFlag,c.name as companyName1,companyId from supplier s ");
		sql.append(" left join company c on c.id=s.companyId");
		sql.append(" where 1=1 and delFlag = 0");
		sql.append(" and  s.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		sql.append(" order by s.createTime desc");

		log.info("供应商列表>>>:"+sql.toString());
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<SupplierBean> list=Lists.newArrayList();
			while(rs.next()){
				SupplierBean bean = new SupplierBean();
				bean.setId(rs.getLong("id"));
				bean.setAddress(rs.getString("address"));
				bean.setCompanyName(rs.getString("companyName"));
				bean.setPhone(rs.getString("phone"));
				bean.setName(rs.getString("name"));
				bean.setCreateDate(rs.getDate("createTime"));
				bean.setRemark(rs.getString("remark"));
				bean.setDelFlag(rs.getInt("delFlag"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setCompanyName1(rs.getString("companyName1"));
				list.add(bean);
			}
			if (showSql){
				log.info(sql);
			}
			data.setReturnObject(list);
			data.setStatus(0);
			log.info("<SupplierDaoImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			data.setStatus(-1);
			return data;
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
	

}

