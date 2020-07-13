package com.server.module.system.repairManage.repairRecord;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.HashMap;
import java.util.List;
/**
 * author name: yjr
 * create time: 2019-08-14 10:42:05
 */ 
@Repository
public class  RepairRecordDaoImpl extends BaseDao<RepairRecordBean> implements RepairRecordDao{

private static Log log = LogFactory.getLog(RepairRecordDaoImpl.class);
public ReturnDataUtil listPage(RepairRecordCondition condition) {
ReturnDataUtil data=new ReturnDataUtil();
StringBuilder sql=new StringBuilder();
sql.append("select id,remark,carPrice,createTime,updateTime,address,createUser,itemPrice,price from repair_record where 1=1 ");
List<Object> plist=Lists.newArrayList();
Connection conn=null;
PreparedStatement pst=null;
ResultSet rs=null;
try {
conn=openConnection();
pst=conn.prepareStatement(super.countSql(sql.toString()));
if (plist!=null && plist.size()>0)
for (int i=0;i<plist.size();i++){
pst.setObject(i+1, plist.get(i));
}
rs = pst.executeQuery();
long count=0;
while(rs.next()){
count=rs.getInt(1);
}
long off=(condition.getCurrentPage()-1)*condition.getPageSize();
pst=conn.prepareStatement(sql.toString()+" limit "+off+","+condition.getPageSize());

if (plist!=null && plist.size()>0)
for (int i=0;i<plist.size();i++){
pst.setObject(i+1, plist.get(i));}
rs = pst.executeQuery();
List<RepairRecordBean> list=Lists.newArrayList();
while(rs.next()){
RepairRecordBean bean = new RepairRecordBean();
bean.setId(rs.getLong("id"));
bean.setRemark(rs.getString("remark"));
bean.setCarPrice(rs.getBigDecimal("carPrice"));
bean.setCreateTime(rs.getDate("createTime"));
bean.setUpdateTime(rs.getDate("updateTime"));
bean.setAddress(rs.getString("address"));
bean.setCreateUser(rs.getLong("createUser"));
bean.setItemPrice(rs.getBigDecimal("itemPrice"));
bean.setPrice(rs.getBigDecimal("price"));
 list.add(bean);
}
 if (showSql){
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

	public RepairRecordBean get(Object id) {
		return super.get(id);
	}
	
	public boolean delete(Object id) {
		RepairRecordBean entity=new RepairRecordBean();
		return super.del(entity);
	}
	
	public boolean update(RepairRecordBean entity) {
		return super.update(entity);
	}
	
	public RepairRecordBean insert(RepairRecordBean entity) {
		return super.insert(entity);
	}
	
	public List<RepairRecordBean> list(RepairRecordCondition condition) {
		return null;
	}


	public List<RepairRecordDto> detail(RepairRecordCondition condition){
		List<RepairRecordDto> list=Lists.newArrayList();
		StringBuilder sql=new StringBuilder();
		//sql.append("select id,remark,carPrice,createTime,updateTime,address,createUser,itemPrice,price from repair_record where 1=1 ");
		sql.append(" SELECT rv.vmCode,rv.plan,rv.remark,rv.state,rv.reason,,rv.rid, ");
		sql.append(" rr.itemPrice,rr.price,rr.carPrice,rr.remark,");
		sql.append(" ri.number,rm.name,ri.vid");
		sql.append(" FROM `repair_record_vmCode` rv ");
		sql.append(" inner join repair_record rr on rr.id=rv.rid");
		sql.append(" inner join repair_record_item ri on ri.vid=rv.id");
		sql.append(" inner join repair_material rm on rm.id=ri.mid");
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql.toString());
			while(rs.next()){
				RepairRecordDto bean = new RepairRecordDto();
				bean.setVmCode(rs.getString("vmCode"));
				bean.setReason(rs.getString("reason"));
				bean.setPlan(rs.getString("plan"));
				bean.setState(rs.getInt("state"));
				bean.setRid(rs.getLong("rid"));
				
				bean.setNumber(rs.getInt("number"));
				bean.setVid(rs.getLong("vid"));
				bean.setName(rs.getString("name"));
				
				
				bean.setId(rs.getLong("id"));
				bean.setRemark(rs.getString("remark"));
				bean.setCarPrice(rs.getBigDecimal("carPrice"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setAddress(rs.getString("address"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setItemPrice(rs.getBigDecimal("itemPrice"));
				bean.setPrice(rs.getBigDecimal("price"));
				list.add(bean);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}

}

