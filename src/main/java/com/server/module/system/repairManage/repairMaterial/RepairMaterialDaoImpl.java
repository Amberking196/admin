package com.server.module.system.repairManage.repairMaterial;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import net.sf.ehcache.search.expression.And;

import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;
/**
 * author name: yjr
 * create time: 2019-08-13 14:38:18
 */ 
@Repository
public class  RepairMaterialDaoImpl extends BaseDao<RepairMaterialBean> implements RepairMaterialDao{

private static Log log = LogFactory.getLog(RepairMaterialDaoImpl.class);
public ReturnDataUtil listPage(RepairMaterialCondition condition) {
ReturnDataUtil data=new ReturnDataUtil();
StringBuilder sql=new StringBuilder();
sql.append("select id,name,price,standard from repair_material where 1=1 and deleteFlag=0 ");
if(StringUtils.isNotBlank(condition.getName())) {
	sql.append(" and name like '%+"+condition.getName()+"+%' ");
}
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
long off=(condition.getCurrentPage()-1)*condition.getPageSize();
pst=conn.prepareStatement(sql.toString()+" limit "+off+","+condition.getPageSize());
rs = pst.executeQuery();
List<RepairMaterialBean> list=Lists.newArrayList();
while(rs.next()){
RepairMaterialBean bean = new RepairMaterialBean();
bean.setId(rs.getLong("id"));
bean.setName(rs.getString("name"));
bean.setPrice(rs.getBigDecimal("price"));
bean.setStandard(rs.getString("standard"));
 list.add(bean);
}
 if (showSql){
log.info(sql);
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

public RepairMaterialBean get(Object id) {
return super.get(id);
}
public boolean delete(Object id) {
RepairMaterialBean entity=new RepairMaterialBean();
entity.setId((long)id);
entity.setDeleteFlag(1);
return super.update(entity);
}
public boolean update(RepairMaterialBean entity) {
return super.update(entity);
}
public RepairMaterialBean insert(RepairMaterialBean entity) {
return super.insert(entity);
}
public List<RepairMaterialBean> list(RepairMaterialCondition condition) {
return null;
}}

