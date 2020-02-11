package com.server.module.system.synthesizeManage.vendingAreaManage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.module.system.adminMenu.AdminMenuBean;
import com.server.util.sqlUtil.MySqlFuns;
@Repository
public class VendingAreaDaoImpl extends MySqlFuns implements VendingAreaDao {

	public static Logger log = LogManager.getLogger(VendingAreaDaoImpl.class);
	@Override
	public boolean addVendingArea(VendingAreaBean areaBean) {
		log.info("<VendingAreaDaoImpl>--<addVendingArea>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into vending_area(name,pid,companyId,isUsing)");
		sql.append(" values(?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(areaBean.getName());
		param.add(areaBean.getPid());
		param.add(areaBean.getCompanyId());
		param.add(areaBean.getIsUsing());
		int result = insert(sql.toString(),param);
		log.info("<VendingAreaDaoImpl>--<addVendingArea>--end");
		if(result!=0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateVendingArea(VendingAreaBean areaBean) {
		log.info("<VendingAreaDaoImpl>--<updateVendingArea>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update vending_area set name = ?,isUsing=? where id = ?");
		List<Object> param = new ArrayList<Object>();
		param.add(areaBean.getName());
		param.add(areaBean.getIsUsing());
		param.add(areaBean.getId());;
		int result = upate(sql.toString(),param);
		log.info("<VendingAreaDaoImpl>--<updateVendingArea>--end");
		if(result !=0){
			return true;
		}
		return false;
	}

	@Override
	public boolean changeAreaStatus(Integer areaId, Integer isUsing) {
		log.info("<VendingAreaDaoImpl>--<changeAreaStatus>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update vending_area set isUsing = "+isUsing+" where id = "+areaId);
		log.info("sql语句："+sql);
		int result = upate(sql.toString());
		log.info("<VendingAreaDaoImpl>--<changeAreaStatus>--end");
		if(result !=0){
			return true;
		}
		return false;
	}

	@Override
	public List<VendingAreaDto> findAllVendingArea() {
		log.info("<VendingAreaDaoImpl>--<findAllVendingArea>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,name,pid,isUsing,companyId from vending_area where deleteFlag=0  ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingAreaDto areaDto = null;
		List<VendingAreaDto> areaList = new ArrayList<VendingAreaDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				areaDto = new VendingAreaDto();
				areaDto.setId(rs.getInt("id"));
				areaDto.setIsUsing(rs.getInt("isUsing"));
				areaDto.setName(rs.getString("name"));
				areaDto.setPid(rs.getInt("pid"));
				areaDto.setCompanyId(rs.getInt("companyId"));
				areaList.add(areaDto);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingAreaDaoImpl>--<findAllVendingArea>--end");
		return areaList;
	}

	@Override
	public List<VendingAreaDto> findAreaByPid(Integer pid) {
		log.info("<VendingAreaDaoImpl>--<findAreaByPid>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,name,pid,isUsing,companyId from vending_area where pid = "+pid + " and deleteFlag=0 "  );
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingAreaDto areaDto = null;
		List<VendingAreaDto> areaList = new ArrayList<VendingAreaDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				areaDto = new VendingAreaDto();
				areaDto.setId(rs.getInt("id"));
				areaDto.setIsUsing(rs.getInt("isUsing"));
				areaDto.setName(rs.getString("name"));
				areaDto.setPid(rs.getInt("pid"));
				areaDto.setCompanyId(rs.getInt("companyId"));
				areaList.add(areaDto);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingAreaDaoImpl>--<findAreaByPid>--end");
		return areaList;
	}

	@Override
	public VendingAreaDto findAreaById(Integer id) {
		log.info("<VendingAreaDaoImpl>--<findAreaByPid>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,name,pid,isUsing,companyId from vending_area where id = "+id+" and deleteFlag=0 ");
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingAreaDto areaDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				areaDto = new VendingAreaDto();
				areaDto.setId(rs.getInt("id"));
				areaDto.setIsUsing(rs.getInt("isUsing"));
				areaDto.setName(rs.getString("name"));
				areaDto.setPid(rs.getInt("pid"));
				areaDto.setCompanyId(rs.getInt("companyId"));
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingAreaDaoImpl>--<findAreaByPid>--end");
		return areaDto;
	}

	@Override
	public List<VendingAreaDto> findAreaByForm(VendingAreaForm areaForm) {
		log.info("<VendingAreaDaoImpl>--<findAreaByForm>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select va.id,va.name,va.pid,va.isUsing,va.companyId,c.name as companyName from vending_area as va");
		sql.append(" left join company as c on c.id = va.companyId where 1=1 and va.deleteFlag=0 ");
		if(StringUtils.isNotBlank(areaForm.getAreaName())){
			sql.append(" and va.name like '%"+areaForm.getAreaName()+"%'");
		}
		if(areaForm.getCompanyId()!=null){
			sql.append(" and companyId =" +areaForm.getCompanyId());
		}else if(StringUtils.isNotBlank(areaForm.getCompanyIds())){
			sql.append(" and companyId in ("+areaForm.getCompanyIds()+")");
		}
		if(areaForm.getIsUsing()!=null){
			sql.append(" and isUsing = "+areaForm.getIsUsing());
		}
		sql.append(" order by va.id desc");
		if(areaForm.getIsShowAll()==0){
			sql.append(" limit "+(areaForm.getCurrentPage()-1)*areaForm.getPageSize()+","+areaForm.getPageSize());
		}
		
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingAreaDto areaDto = null;
		List<VendingAreaDto> areaList = new ArrayList<VendingAreaDto>();
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				areaDto = new VendingAreaDto();
				areaDto.setId(rs.getInt("id"));
				areaDto.setIsUsing(rs.getInt("isUsing"));
				areaDto.setName(rs.getString("name"));
				areaDto.setPid(rs.getInt("pid"));
				areaDto.setCompanyId(rs.getInt("companyId"));
				areaDto.setCompanyName(rs.getString("companyName"));
				areaList.add(areaDto);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingAreaDaoImpl>--<findAreaByForm>--end");
		return areaList;
	}
	
	public Long findAreaByFormNum(VendingAreaForm areaForm){
		log.info("<VendingAreaDaoImpl>--<findAreaByFormNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) as total from vending_area as va");
		sql.append(" left join company as c on c.id = va.companyId where 1=1 and va.deleteFlag=0 ");
		if(StringUtils.isNotBlank(areaForm.getAreaName())){
			sql.append(" and va.name like '%"+areaForm.getAreaName()+"%'");
		}
		if(areaForm.getCompanyId()!=null){
			sql.append(" and companyId =" +areaForm.getCompanyId());
		}else if(StringUtils.isNotBlank(areaForm.getCompanyIds())){
			sql.append(" and companyId in ("+areaForm.getCompanyIds()+")");
		}
		if(areaForm.getIsUsing()!=null){
			sql.append(" and isUsing = "+areaForm.getIsUsing());
		}
		log.info("sql语句："+sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long total = 0L;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				total = rs.getLong("total");
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingAreaDaoImpl>--<findAreaByFormNum>--end");
		return total;
	}

	@Override
	public List<String> findAllAreaName() {
		log.info("<VendingAreaDaoImpl>--<findAllAreaName>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select name from vending_area where deleteFlag=0 ");
		log.info("sql语句："+sql);
		List<String> areaNameList = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				areaNameList.add(rs.getString("name"));
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<VendingAreaDaoImpl>--<findAllAreaName>--end");
		return areaNameList;
	}

	/**
	 * 删除区域
	 */
	@Override
	public boolean delete(Object id) {
		log.info("<VendingAreaDaoImpl>--<delete>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update vending_area set deleteFlag = 1 where id in ("+id+") ");
		log.info("删除区域-sql语句："+sql);
		int result = upate(sql.toString());
		log.info("<VendingAreaDaoImpl>--<delete>--end");
		if(result !=0){
			return true;
		}
		return false;
	}

}
