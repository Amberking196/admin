package com.server.module.system.baseManager.stateInfo;
import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

/**
 * author name: yjr 
 * create time: 2018-03-30 11:10:15
 */
@Repository
public class StateInfoDaoImpl extends BaseDao<StateInfoBean> implements StateInfoDao {


	public static Logger log = LogManager.getLogger(StateInfoDaoImpl.class); 	    

	@SuppressWarnings("resource")
	public ReturnDataUtil listPage(StateInfoCondition condition) {
		log.info("StateInfoDaoImpl---------listPage------ start"); 
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select keyName,state,name,id from state_info where 1=1 ");
		List<Object> plist = Lists.newArrayList();
		if(StringUtil.isNotBlank(condition.getKeyName())){
			sql.append(" and keyName like ? ");
			plist.add("%"+condition.getKeyName()+"%");
		}
		if(StringUtil.isNotBlank(condition.getName())){
			sql.append(" and name like ? ");
			plist.add("%"+condition.getName()+"%");
		}
		if(condition.getState()!=null){
			sql.append(" and state=?");
			plist.add(condition.getState());
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
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
			sql.append(" order by createTime desc ");//排序
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();
			List<StateInfoBean> list = Lists.newArrayList();
			while (rs.next()) {
				StateInfoBean bean = new StateInfoBean();
				bean.setKeyName(rs.getString("keyName"));
				bean.setState(rs.getLong("state"));
				bean.setName(rs.getString("name"));
				bean.setId(rs.getLong("id"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setPageSize(condition.getPageSize());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			log.info("StateInfoDaoImpl---------listPage------ end"); 
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public StateInfoBean get(Object id) {
		log.info("StateInfoDaoImpl---------get------ start"); 
		StateInfoBean re=super.get(id);
		log.info("StateInfoDaoImpl---------get------ end"); 
		return re;
	}

	public boolean delete(Object id) {
		log.info("StateInfoDaoImpl---------delete------ start"); 
		StateInfoBean entity = new StateInfoBean();
		boolean re=super.del(entity);
		log.info("StateInfoDaoImpl---------delete------ end"); 
		return re;
	}

	public boolean update(StateInfoBean entity) {
		log.info("StateInfoDaoImpl---------update------ start"); 
		boolean re=super.update(entity);
		log.info("StateInfoDaoImpl---------update------ end"); 
		return re;
	}

	public StateInfoBean insert(StateInfoBean entity) {
		log.info("StateInfoDaoImpl---------insert------ start"); 
		StateInfoBean re=super.insert(entity);
		log.info("StateInfoDaoImpl---------insert------ end"); 
		return re;
	}

	public List<StateInfoBean> list(StateInfoCondition condition) {
		return null;
	}
	
	public List<StateInfoDto> findStateInfoByKeyName(String keyName) {
		log.info("<StateInfoDaoImpl>--<findStateInfoByKeyName>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where keyName ='"+keyName+"'");
		List<StateInfoDto> stateList = new ArrayList<StateInfoDto>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StateInfoDto stateDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<findStateInfoByKeyName>--sql:"+sql.toString());
			while (rs != null && rs.next()) {
				stateDto = new StateInfoDto();
				stateDto.setId(rs.getInt("id"));
				stateDto.setKeyName(rs.getString("keyName"));
				stateDto.setName(rs.getString("name"));
				stateDto.setState(rs.getInt("state"));
				stateList.add(stateDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<findStateInfoByKeyName>--end");
		return stateList;
	}
	@Override
	public StateInfoBean getStateInfoByState(Long state) {
		log.info("StateInfoDaoImpl---------getStateInfoByState------ start"); 
		String sql="select id,keyName,name,state from state_info where state ='"+state+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StateInfoBean stateDto = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery(); 
			log.info("StateInfoDaoImpl---------getStateInfoByState------ sql:"+sql); 
			while (rs != null && rs.next()) {
				stateDto = new StateInfoBean();
				stateDto.setId(rs.getLong("id"));
				stateDto.setKeyName(rs.getString("keyName"));
				stateDto.setName(rs.getString("name"));
				stateDto.setState(rs.getLong("state"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getNameByState>--end");
		return stateDto;
	}
	
	
	/**
	 * 获取字典值字典的名称
	 */
	@Cacheable(value = "stateInfoName", key = "#state")
	public String getNameByState(Long state) {
		log.info("<StateInfoDaoImpl>--<getNameByState>--start");
		StateInfoBean bean= getStateInfoByState(state);
		log.info("<StateInfoDaoImpl>--<getNameByState>--end");
		if(bean!=null) {
			return bean.getName();
		}else {
		return null;
		}
	}

	/**
	 * 通过数据字典name 得到状态码
	 */
	@Override
	public Long getStateId(String name) {
		log.info("<StateInfoDaoImpl>--<getStateId>--start");
		String sql="select state from state_info where name =?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getStateId>--end");
		return null;
	}

	/**
	 *  查询仓库的状态
	 */
	@Override
	public List<StateInfoBean> getWarehouseState() {
		log.info("<StateInfoDaoImpl>--<getWarehouseState>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where keyName ='wharehouse_info'");
		List<StateInfoBean> list = new ArrayList<StateInfoBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<getWarehouseState>--sql:"+sql.toString());
			while (rs != null && rs.next()) {
				StateInfoBean  bean= new StateInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setKeyName(rs.getString("keyName"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getLong("state"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getWarehouseState>--end");
		return list;
	}
	
	/**
	 *  查询入库的状态
	 */
	public List<StateInfoBean> getWarehouseWarrantState(){
		log.info("<StateInfoDaoImpl>--<getWarehouseWarrantState>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where keyName ='warehouse_change'");
		List<StateInfoBean> list = new ArrayList<StateInfoBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<getWarehouseWarrantState>--sql:"+sql.toString());
			while (rs != null && rs.next()) {
				StateInfoBean  bean= new StateInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setKeyName(rs.getString("keyName"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getLong("state"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getWarehouseWarrantState>--end");
		return list;
	}

	/**
	 * 查询入库的类型
	 */
	@Override
	public List<StateInfoBean> getWarehouseWarrantType() {
		log.info("<StateInfoDaoImpl>--<getWarehouseWarrantType>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where keyName ='warehouse_warrant'");
		List<StateInfoBean> list = new ArrayList<StateInfoBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<getWarehouseWarrantType>--sql:"+sql.toString());
			while (rs != null && rs.next()) {
				StateInfoBean  bean= new StateInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setKeyName(rs.getString("keyName"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getLong("state"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getWarehouseWarrantType>--end");
		return list;
	}

	/**
	 * 查询出库的状态
	 */
	@Override
	public List<StateInfoBean> getWarehouseRemovalState() {
		log.info("<StateInfoDaoImpl>--<getWarehouseRemovalState>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where keyName ='warehouse_removal'");
		List<StateInfoBean> list = new ArrayList<StateInfoBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<getWarehouseRemovalState>--sql:"+sql.toString());
			while (rs != null && rs.next()) {
				StateInfoBean  bean= new StateInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setKeyName(rs.getString("keyName"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getLong("state"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getWarehouseRemovalState>--end");
		return list;
	}

	/**
	 * 查询出库的类型
	 */
	@Override
	public List<StateInfoBean> getWarehouseRemovalType() {
		log.info("<StateInfoDaoImpl>--<getWarehouseRemovalType>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where keyName ='warehouse_retype'");
		List<StateInfoBean> list = new ArrayList<StateInfoBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<getWarehouseRemovalType>--sql:"+sql.toString());
			while (rs != null && rs.next()) {
				StateInfoBean  bean= new StateInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setKeyName(rs.getString("keyName"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getLong("state"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getWarehouseRemovalType>--end");
		return list;
	}
	
	/**
	 * 查询归还的类型
	 */
	@Override
	public List<StateInfoBean> getWarehouseReturnType() {
		log.info("<StateInfoDaoImpl>--<getWarehouseReturnType>--start");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where keyName ='warehouse_return'");
		List<StateInfoBean> list = new ArrayList<StateInfoBean>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<getWarehouseReturnType>--sql:"+sql.toString());
			while (rs != null && rs.next()) {
				StateInfoBean  bean= new StateInfoBean();
				bean.setId(rs.getLong("id"));
				bean.setKeyName(rs.getString("keyName"));
				bean.setName(rs.getString("name"));
				bean.setState(rs.getLong("state"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getWarehouseReturnType>--end");
		return list;
	}

	@Override
	public ReturnDataUtil checkStateOnlyOne(StateInfoBean entity) {
		log.info("<StateInfoDaoImpl>--<checkStateOnlyOne>--start");
		//创建返回结果
		ReturnDataUtil data=new ReturnDataUtil();
		StringBuffer sql = new StringBuffer();
		sql.append("select id,keyName,name,state from state_info where state ="+entity.getState());
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			log.info("<StateInfoDaoImpl>--<checkStateOnlyOne>--sql:"+sql.toString());
			while (rs != null && rs.next()) {//字典的该状态码存在
				data.setStatus(-1);
				data.setMessage("该状态码存在，请选择其他状态码");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			this.closeConnection(rs, ps, conn);
		}
		log.info("<StateInfoDaoImpl>--<getWarehouseReturnType>--end");
		return data;
	}
}
