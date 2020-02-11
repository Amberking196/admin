package com.server.module.system.visionManage.visionCheck;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;
import java.util.List;
/**
 * author name: yjr
 * create time: 2019-10-10 10:54:34
 */ 
@Repository
public class  VisionMachinesCheckDaoImpl extends BaseDao<VisionMachinesCheckBean> implements VisionMachinesCheckDao{

private static Log log = LogFactory.getLog(VisionMachinesCheckDaoImpl.class);
public ReturnDataUtil listPage(VisionMachinesCheckCondition condition) {
ReturnDataUtil data=new ReturnDataUtil();
StringBuilder sql=new StringBuilder();
sql.append("select id,vmCode,createTime,checkId,remark from vision_machines_check where 1=1 ");
if(StringUtils.isNotBlank(condition.getVmCode())) {
	sql.append(" and vmCode like '%"+condition.getVmCode()+"%'");
}
sql.append(" order by id desc ");

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
List<VisionMachinesCheckBean> list=Lists.newArrayList();
while(rs.next()){
VisionMachinesCheckBean bean = new VisionMachinesCheckBean();
bean.setId(rs.getLong("id"));
bean.setVmCode(rs.getString("vmCode"));
bean.setCreateTime(rs.getDate("createTime"));
bean.setCheckId(rs.getString("checkId"));
bean.setRemark(rs.getString("remark"));
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



public ReturnDataUtil detail(VisionMachinesCheckCondition condition) {
ReturnDataUtil data=new ReturnDataUtil();
StringBuilder sql=new StringBuilder();
sql.append("select * from vision_machines_check vmc left join vision_machines_check_item vmci on vmc.id=vmci.visionId where vmc.id ="+condition.getId());
Connection conn=null;
PreparedStatement pst=null;
ResultSet rs=null;
try {
conn=openConnection();
pst=conn.prepareStatement(sql.toString());
rs = pst.executeQuery();
List<VisionMachinesCheckItemBean> list=Lists.newArrayList();
while(rs.next()){
	VisionMachinesCheckItemBean bean = new VisionMachinesCheckItemBean();
bean.setName(rs.getString("name"));
bean.setNumber(rs.getInt("number"));
bean.setCheckId(rs.getString("checkId"));
 list.add(bean);
}
 if (showSql){
log.info(sql);
}
data.setCurrentPage(condition.getCurrentPage());
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
public VisionMachinesCheckBean get(Object id) {
return super.get(id);
}
public boolean delete(Object id) {
VisionMachinesCheckBean entity=new VisionMachinesCheckBean();
return super.del(entity);
}
public boolean update(VisionMachinesCheckBean entity) {
return super.update(entity);
}
public VisionMachinesCheckBean insert(VisionMachinesCheckBean entity) {
return super.insert(entity);
}
public List<VisionMachinesCheckBean> list(VisionMachinesCheckCondition condition) {
return null;
}}

