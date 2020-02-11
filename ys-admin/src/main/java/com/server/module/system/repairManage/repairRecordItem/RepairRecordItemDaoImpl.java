package com.server.module.system.repairManage.repairRecordItem;

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
import java.util.List;
/**
 * author name: yjr
 * create time: 2019-08-19 15:38:16
 */ 
@Repository
public class  RepairRecordItemDaoImpl extends BaseDao<RepairRecordItemBean> implements RepairRecordItemDao{

private static Log log = LogFactory.getLog(RepairRecordItemDaoImpl.class);
public ReturnDataUtil listPage(RepairRecordItemCondition condition) {
ReturnDataUtil data=new ReturnDataUtil();
StringBuilder sql=new StringBuilder();
sql.append("select id,mid,vid,createTime,updateTime,createUser,number,remark from repair_record_item where 1=1 ");
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
List<RepairRecordItemBean> list=Lists.newArrayList();
while(rs.next()){
RepairRecordItemBean bean = new RepairRecordItemBean();
bean.setId(rs.getLong("id"));
bean.setMid(rs.getLong("mid"));
bean.setVid(rs.getLong("vid"));
bean.setCreateTime(rs.getDate("createTime"));
bean.setUpdateTime(rs.getDate("updateTime"));
bean.setCreateUser(rs.getString("createUser"));
bean.setNumber(rs.getInt("number"));
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

public RepairRecordItemBean get(Object id) {
return super.get(id);
}
public boolean delete(Object id) {
RepairRecordItemBean entity=new RepairRecordItemBean();
return super.del(entity);
}
public boolean update(RepairRecordItemBean entity) {
return super.update(entity);
}
public RepairRecordItemBean insert(RepairRecordItemBean entity) {
return super.insert(entity);
}
public List<RepairRecordItemBean> list(RepairRecordItemCondition condition) {
return null;
}}

