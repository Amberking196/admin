package com.server.module.system.synthesizeManage.vendingLineManage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class VendingLineDaoImpl extends MySqlFuns implements VendingLineDao {

	public static Logger log = LogManager.getLogger(VendingLineDaoImpl.class);

	@Override
	public boolean addVendingLine(VendingLineBean line) {
		log.info("<VendingLineDaoImpl>--<addVendingLine>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into vending_line(name,dutyId,dutyName,companyId,areaId)");
		sql.append(" values(?,?,?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(line.getName());
		param.add(line.getDutyId());
		param.add(line.getDutyName());
		param.add(line.getCompanyId());
		param.add(line.getAreaId());
		int result = insert(sql.toString(), param);
		log.info("<VendingLineDaoImpl>--<addVendingLine>--end");
		if (result != 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateVendingLine(VendingLineBean line) {
		log.info("<VendingLineDaoImpl>--<updateVendingLine>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update vending_line set name =?,dutyId=?,dutyName=?,companyId=?,areaId=? where id =?");
		List<Object> param = new ArrayList<Object>();
		param.add(line.getName());
		param.add(line.getDutyId());
		param.add(line.getDutyName());
		param.add(line.getCompanyId());
		param.add(line.getAreaId());
		// param.add(line.getIsUsing());
		param.add(line.getId());
		int result = upate(sql.toString(), param);
		log.info("<VendingLineDaoImpl>--<updateVendingLine>--end");
		if (result != 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<VendingLineDto> findAllVendingLine() {
		log.info("<VendingLineDaoImpl>--<findAllVendingLine>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select vl.id,vl.name,vl.dutyId,vl.dutyName,vl.areaId,vl.isUsing,c.name as companyName");
		sql.append(" from vending_line as vl");
		sql.append(" left join company as c on c.id = vl.companyId where vl.deleteFlag=0 ");
		log.info("sql语句：" + sql);
		List<VendingLineDto> vendingLineList = new ArrayList<VendingLineDto>();
		VendingLineDto lineDto = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				lineDto = new VendingLineDto();
				lineDto.setAreaId(rs.getInt("areaId"));
				lineDto.setCompanyName(rs.getString("companyName"));
				lineDto.setDutyId(rs.getString("dutyId"));
				lineDto.setDutyName(rs.getString("dutyName"));
				lineDto.setId(rs.getInt("id"));
				lineDto.setName(rs.getString("name"));
				vendingLineList.add(lineDto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		log.info("<VendingLineDaoImpl>--<findAllVendingLine>--end");
		return vendingLineList;
	}

	@Override
	public boolean changeLineStatus(Integer id, Integer isUsing) {
		log.info("<VendingLineDaoImpl>--<changeLineStatus>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update vending_line set isUsing=" + isUsing);
		sql.append(" where id = " + id);
		log.info("sql语句：" + sql);
		int result = upate(sql.toString());
		if (result != 0) {
			return true;
		}
		log.info("<VendingLineDaoImpl>--<changeLineStatus>--end");
		return false;
	}

	@Override
	public List<VendingLineDto> findLineByForm(VendingLineForm vendingLineForm) {
		log.info("<VendingLineDaoImpl>--<findLineByForm>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select vl.id,vl.name,vl.dutyName,vl.dutyId,va.id as areaId,");
		sql.append(" c.name as companyName,c.id as companyId,va.name as areaName from vending_line as vl");
		sql.append(" left join company as c on vl.companyId = c.id");
		sql.append(" left join vending_area as va on va.id = vl.areaId where 1=1 and vl.deleteFlag=0 ");
		if (StringUtils.isNotBlank(vendingLineForm.getLineName())) {
			sql.append(" and vl.name like '%" + vendingLineForm.getLineName() + "%'");
		}
		if (StringUtils.isNotBlank(vendingLineForm.getDutyName())) {
			sql.append(" and vl.dutyName like '%" + vendingLineForm.getDutyName() + "%'");
		}
		if (vendingLineForm.getCompanyId() != null) {
			sql.append(" and vl.companyId =" + vendingLineForm.getCompanyId());
		} else if (StringUtils.isNotBlank(vendingLineForm.getCompanyIds())) {
			sql.append(" and vl.companyId in (" + vendingLineForm.getCompanyIds() + ")");
		}
		if (vendingLineForm.getAreaId() != null) {
			sql.append(" and vl.areaId = " + vendingLineForm.getAreaId());
		}
		if (StringUtils.isNotBlank(vendingLineForm.getAreaName())) {
			sql.append(" and va.name like '%" + vendingLineForm.getAreaName() + "%'");
		}
		sql.append(" order by vl.id desc");
		if (vendingLineForm.getIsShowAll() == 0) {
			sql.append(" limit " + (vendingLineForm.getCurrentPage() - 1) * vendingLineForm.getPageSize() + ","
					+ vendingLineForm.getPageSize());
		}
		log.info("sql语句：" + sql);
		List<VendingLineDto> vendingLineList = new ArrayList<VendingLineDto>();
		VendingLineDto lineDto = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				lineDto = new VendingLineDto();
				lineDto.setAreaName(rs.getString("areaName"));
				lineDto.setCompanyName(rs.getString("companyName"));
				lineDto.setDutyId(rs.getString("dutyId"));
				lineDto.setDutyName(rs.getString("dutyName"));
				lineDto.setId(rs.getInt("id"));
				lineDto.setName(rs.getString("name"));
				lineDto.setCompanyId(rs.getInt("companyId"));
				lineDto.setAreaId(rs.getInt("areaId"));
				vendingLineList.add(lineDto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		log.info("<VendingLineDaoImpl>--<findLineByForm>--end");
		return vendingLineList;
	}

	@Override
	public Long findLineByFormNum(VendingLineForm vendingLineForm) {
		log.info("<VendingLineDaoImpl>--<findLineByFormNum>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) as total from vending_line as vl");
		sql.append(" left join company as c on vl.companyId = c.id");
		sql.append(" left join vending_area as va on va.id = vl.areaId where 1=1 and vl.deleteFlag=0 ");
		if (StringUtils.isNotBlank(vendingLineForm.getLineName())) {
			sql.append(" and vl.dutyName like '%" + vendingLineForm.getLineName() + "%'");
		}
		if (StringUtils.isNotBlank(vendingLineForm.getDutyName())) {
			sql.append(" and vl.dutyName like '%" + vendingLineForm.getDutyName() + "%'");
		}
		if (vendingLineForm.getCompanyId() != null) {
			sql.append(" and vl.companyId =" + vendingLineForm.getCompanyId());
		} else if (StringUtils.isNotBlank(vendingLineForm.getCompanyIds())) {
			sql.append(" and vl.companyId in (" + vendingLineForm.getCompanyIds() + ")");
		}
		if (vendingLineForm.getAreaId() != null) {
			sql.append(" and vl.areaId = " + vendingLineForm.getAreaId());
		}
		if (StringUtils.isNotBlank(vendingLineForm.getAreaName())) {
			sql.append(" and va.name like '%" + vendingLineForm.getAreaName() + "%'");
		}
		log.info("sql语句：" + sql);
		Long total = 0L;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				total = rs.getLong("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		log.info("<VendingLineDaoImpl>--<findLineByFormNum>--end");
		return total;
	}

	/**
	 * 根据地区id 查询该地区下所有的线路
	 */
	@Override
	public List<VendingLineBean> findLine(Integer areaId) {
		log.info("<VendingLineDaoImpl>--<findLine>--start");
		StringBuffer sql = new StringBuffer();
		List<VendingLineBean> list = new ArrayList<VendingLineBean>();
		sql.append(" select id,name,dutyName,companyId,dutyId,areaId from vending_line WHERE areaId='" + areaId + "'  and deleteFlag=0 " );
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				VendingLineBean bean = new VendingLineBean();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setDutyName(rs.getString("dutyName"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setDutyId(rs.getString("dutyId"));
				bean.setAreaId(rs.getInt("areaId"));
				bean.setIsShow(1);
				bean.setIfShow("+");
				bean.setShowin(true);
				list.add(bean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		log.info("<VendingLineDaoImpl>--<findLine>--end");
		return list;
	}

	@Override
	public boolean isOnlyOne(String lineName) {
		log.info("<VendingLineDaoImpl>--<isOnlyOne>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from vending_line where name = '" + lineName + "' and deleteFlag=0 " );
		log.info("sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		log.info("<VendingLineDaoImpl>--<isOnlyOne>--end");
		return true;
	}

	/**
	 * 查询线路下的负责人
	 */
	@Override
	public List<VendingLineBean> findDuty(int lineId) {
		log.info("<VendingLineDaoImpl>--<findDuty>--start");
		StringBuffer sql = new StringBuffer();
		sql.append(" select dutyId,dutyName from vending_line where id='" + lineId + "' and deleteFlag=0 ");
		log.info("查询线路下负责人的sql语句：" + sql);
		List<VendingLineBean> list = new ArrayList<VendingLineBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VendingLineBean dto=null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				//Map<String, String> map = new HashMap<String, String>();
				String datyId = rs.getString("dutyId");
				String dutyName = rs.getString("dutyName");
				if (datyId != null && dutyName != null) {
					if (datyId.contains(",")) {
						String[] split = datyId.split(",");
						String[] split2 = dutyName.split(",");
						for (int i = 0; i < split.length; i++) {
							dto=new VendingLineBean();
							dto.setDutyId(split[i]);
							dto.setDutyName(split2[i]);
							list.add(dto);
						}
					} else {
						dto=new VendingLineBean();
						dto.setDutyId(datyId);
						dto.setDutyName(dutyName);
						list.add(dto);
					}
				}
				
				return list;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		log.info("<VendingLineDaoImpl>--<findDuty>--end");
		return null;
	}

	/**
	 * 删除线路
	 */
	@Override
	public boolean delete(Object id) {
		log.info("<VendingLineDaoImpl>----<delete>----start");
		StringBuffer sql = new StringBuffer();
		sql.append(" update vending_line set deleteFlag=1 where id in ("+id+") ");
		log.info("删除线路-sql语句：" + sql);
		int result = upate(sql.toString());
		if (result != 0) {
			return true;
		}
		log.info("<VendingLineDaoImpl>----<delete>----end");
		return false;
	}

	/**
	 * 查询是否是线路负责人
	 */
	@Override
	public List<VendingLineBean> findLineBean(Long id) {
		log.info("<VendingLineDaoImpl>--<findLineBean>--start");
		StringBuffer sql = new StringBuffer();
		List<VendingLineBean> list = new ArrayList<VendingLineBean>();
		sql.append(" select id,name,dutyName,companyId,dutyId,areaId from vending_line where deleteFlag=0 and  dutyId='"+id+"'" );
		log.info("查询是否是线路负责人 sql语句：" + sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				VendingLineBean bean = new VendingLineBean();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setDutyName(rs.getString("dutyName"));
				bean.setCompanyId(rs.getInt("companyId"));
				bean.setDutyId(rs.getString("dutyId"));
				bean.setAreaId(rs.getInt("areaId"));
				list.add(bean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
		log.info("<VendingLineDaoImpl>--<findLineBean>--end");
		return list;
	}


}
