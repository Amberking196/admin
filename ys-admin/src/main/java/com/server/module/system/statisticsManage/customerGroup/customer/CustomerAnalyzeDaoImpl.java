package com.server.module.system.statisticsManage.customerGroup.customer;

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
 * create time: 2018-10-18 09:08:12
 */ 
@Repository
public class  CustomerAnalyzeDaoImpl extends BaseDao<CustomerAnalyzeBean> implements CustomerAnalyzeDao{

private static Log log = LogFactory.getLog(CustomerAnalyzeDaoImpl.class);
public ReturnDataUtil listPage(CustomerAnalyzeCondition condition) {
ReturnDataUtil data=new ReturnDataUtil();
StringBuilder sql=new StringBuilder();
sql.append("select customerId,mobile,state,buyTime,buyMoney,registerTime,createTime,updateTime from customer_analyze where 1=1 ");
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
List<CustomerAnalyzeBean> list=Lists.newArrayList();
while(rs.next()){
CustomerAnalyzeBean bean = new CustomerAnalyzeBean();
bean.setCustomerId(rs.getLong("customerId"));
bean.setMobile(rs.getString("mobile"));
bean.setState(rs.getInt("state"));
bean.setBuyTime(rs.getLong("buyTime"));
bean.setBuyMoney(rs.getDouble("buyMoney"));
bean.setRegisterTime(rs.getDate("registerTime"));
bean.setCreateTime(rs.getDate("createTime"));
bean.setUpdateTime(rs.getDate("updateTime"));
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

public CustomerAnalyzeBean get(Object id) {
return super.get(id);
}
public boolean delete(Object id) {
CustomerAnalyzeBean entity=new CustomerAnalyzeBean();
return super.del(entity);
}
public boolean update(CustomerAnalyzeBean entity) {
return super.update(entity);
}
public CustomerAnalyzeBean insert(CustomerAnalyzeBean entity) {
return super.insert(entity);
}
public List<CustomerAnalyzeBean> list(CustomerAnalyzeCondition condition) {
return null;
}}

