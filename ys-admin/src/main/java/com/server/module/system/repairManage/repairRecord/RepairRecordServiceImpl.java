package com.server.module.system.repairManage.repairRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.runtime.RangeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.server.module.system.repairManage.repairRecordItem.RepairRecordItemBean;
import com.server.module.system.repairManage.repairRecordItem.RepairRecordItemDao;
import com.server.module.system.repairManage.repairRecordVmCode.RepairRecordVmCodeBean;
import com.server.module.system.repairManage.repairRecordVmCode.RepairRecordVmCodeDao;
import com.server.module.system.replenishManage.replenishCorrect.ReplenishDto;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2019-08-14 10:42:05
 */ 
@Service
public class  RepairRecordServiceImpl  implements RepairRecordService{

private static Log log = LogFactory.getLog(RepairRecordServiceImpl.class);
@Autowired
private RepairRecordDao repairRecordDaoImpl;
@Autowired
private RepairRecordVmCodeDao repairRecordVmCodeDao;
@Autowired
private RepairRecordItemDao repairRecordItemDao;

public ReturnDataUtil listPage(RepairRecordCondition condition){
	return repairRecordDaoImpl.listPage(condition);
}
public RepairRecordBean add(RepairRecordBean entity) {
	RepairRecordBean newRb=repairRecordDaoImpl.insert(entity);
	for(RepairRecordVmCodeBean rv:entity.getVmCodeList()){
		rv.setRid(newRb.getId());
		RepairRecordVmCodeBean newRv=repairRecordVmCodeDao.insert(rv);
		for(RepairRecordItemBean ri:rv.getItemList()) {
			ri.setMid(newRb.getId());
			ri.setVid(newRv.getId());
			RepairRecordItemBean newRi=repairRecordItemDao.insert(ri);
		}
	}
	return newRb;
}

public boolean update(RepairRecordBean entity) {
return repairRecordDaoImpl.update(entity);
}

public boolean del(Object id) {
return repairRecordDaoImpl.delete(id);
}

public List<RepairRecordBean> list(RepairRecordCondition condition) {
return null;
}

public RepairRecordBean get(Object id) {
return repairRecordDaoImpl.get(id);
}

	public RepairRecordBean detail(RepairRecordCondition condition) {
		List<RepairRecordDto> repairRecordDtoList =repairRecordDaoImpl.detail(condition);
		//rid vid dto
		Map<Long, Map<Long, List<RepairRecordDto>>> collect = 
		repairRecordDtoList.stream().collect(
		Collectors.groupingBy(RepairRecordDto::getRid,Collectors.groupingBy(RepairRecordDto::getVid)));
		RepairRecordBean rBean=new RepairRecordBean();
		for(Map.Entry<Long,Map<Long, List<RepairRecordDto>>> entry : collect.entrySet()){
			Long rid=entry.getKey();
			Map<Long, List<RepairRecordDto>> m=entry.getValue();
			for(Entry<Long,List<RepairRecordDto>> e:m.entrySet()) {
				RepairRecordVmCodeBean vmCodeBean=new RepairRecordVmCodeBean();
				rBean.getVmCodeList().add(vmCodeBean);
				Long vid=e.getKey();
				List<RepairRecordDto> repairRecordDtoList2=e.getValue();
				for(RepairRecordDto recordDto :repairRecordDtoList2) {
				    RepairRecordItemBean repairRecordItemBean = new RepairRecordItemBean();
				    repairRecordItemBean.setName(recordDto.getName());
				    repairRecordItemBean.setPrice(recordDto.getPerPrice());
				    repairRecordItemBean.setNumber(recordDto.getNumber());
					vmCodeBean.getItemList().add(repairRecordItemBean);
				}
			}
		}
		return rBean;

	}

}

