package com.server.module.system.statisticsManage.customerGroup.customer;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
 import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2018-10-18 09:08:12
 */ 
@Service
public class  CustomerAnalyzeServiceImpl  implements CustomerAnalyzeService{

private static Log log = LogFactory.getLog(CustomerAnalyzeServiceImpl.class);
@Autowired
private CustomerAnalyzeDao customerAnalyzeDaoImpl;
public ReturnDataUtil listPage(CustomerAnalyzeCondition condition){
return customerAnalyzeDaoImpl.listPage(condition);
}
public CustomerAnalyzeBean add(CustomerAnalyzeBean entity) {
return customerAnalyzeDaoImpl.insert(entity);
}

public boolean update(CustomerAnalyzeBean entity) {
return customerAnalyzeDaoImpl.update(entity);
}

public boolean del(Object id) {
return customerAnalyzeDaoImpl.delete(id);
}

public List<CustomerAnalyzeBean> list(CustomerAnalyzeCondition condition) {
return null;
}

public CustomerAnalyzeBean get(Object id) {
return customerAnalyzeDaoImpl.get(id);
}

 public List<StateVO> listState(){
     List list= Lists.newArrayList();
     StateVO vo=new StateVO(CustomerStateConst.STATE_NEW,"新建");
     list.add(vo);
  vo=new StateVO(CustomerStateConst.STATE_ONE,"一次");
  list.add(vo);
  vo=new StateVO(CustomerStateConst.STATE_ACTIVE,"活跃");
  list.add(vo);
  vo=new StateVO(CustomerStateConst.STATE_CRAZE,"忠实");
  list.add(vo);
  vo=new StateVO(CustomerStateConst.STATE_LOWS,"低频");
  list.add(vo);
  vo=new StateVO(CustomerStateConst.STATE_WASTAGE,"流失");
  list.add(vo);
  vo=new StateVO(CustomerStateConst.STATE_BACK,"回流");
  list.add(vo);
  return list;
 }
}

