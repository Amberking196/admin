package com.server.module.system.replenishManage.replenishCorrect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.DateUtil;

@Service
public class ReplenishCorrectServiceImpl implements ReplenishCorrectService{

	private final static Logger log = LogManager.getLogger(ReplenishCorrectServiceImpl.class);

	@Autowired
	private ReplenishCorrectDao replenishCorrectDao;
	//3小时
	private final static Long LIMIT = 10800000L;
	
	@Override
	public List<ReplenishCollectDto>  getReplenishInfo(ReplenishForm form) {
		log.info("<ReplenishCorrectServiceImpl--getReplenishInfo--start>");
		List<ReplenishDto> replenishInfo = replenishCorrectDao.getReplenishInfo(form);
		Map<String, Map<Integer, Map<Long, List<ReplenishDto>>>> collect = 
				replenishInfo.stream().collect(Collectors.groupingBy(ReplenishDto::getVmCode,
				Collectors.groupingBy(ReplenishDto::getWayNumber,Collectors.groupingBy(ReplenishDto::getBasicItemId))));
		List<ReplenishCollectDto> replenishCollectList = new ArrayList<ReplenishCollectDto>();
		for(Map.Entry<String, Map<Integer, Map<Long, List<ReplenishDto>>>> vmEny : collect.entrySet()){
			String vmCode = vmEny.getKey();
			Map<Integer, Map<Long, List<ReplenishDto>>> wayMap = vmEny.getValue();
			for(Map.Entry<Integer, Map<Long, List<ReplenishDto>>> wayEntry : wayMap.entrySet()){
				Integer wayNumber = wayEntry.getKey();
				Map<Long, List<ReplenishDto>> itemMap = wayEntry.getValue();
				for(Map.Entry<Long, List<ReplenishDto>> itemEntry : itemMap.entrySet()){
					Long basicItemId = itemEntry.getKey();
					List<ReplenishDto> replenishList = itemEntry.getValue();
					int replenishNum = 0;
					int probableNum = 0;
					int firstIndex = 0;
					List<Long> ids = new ArrayList<Long>();
					for (int i=0; i< replenishList.size() ; i++) {
						ReplenishDto replenishDto = replenishList.get(i);
						if(i==firstIndex && replenishDto.getOpType() == 4 && i!=replenishList.size()-1){
							continue;
						}
						probableNum += (replenishDto.getNum() - replenishDto.getPreNum());
						if(replenishDto.getOpType() != 4){
							replenishNum += (replenishDto.getNum() - replenishDto.getPreNum());
						}else{
							ids.add(replenishDto.getReplenishId());
						}
						if((i+1)>=replenishList.size() || replenishDto.getState()==1 || !replenishDto.getNum().equals(replenishList.get(i+1).getPreNum())
								|| (replenishList.get(i+1).getReplenishTime().getTime()-replenishDto.getReplenishTime().getTime())>LIMIT){
							ReplenishCollectDto collectDto = new ReplenishCollectDto();
							collectDto.setPic(replenishDto.getPic());
							if(replenishDto.getState()==1) {
								collectDto.setStateName(replenishDto.getStateName());
							}
							collectDto.setItemName(replenishDto.getItemName());
							collectDto.setVmCode(vmCode);
							collectDto.setWayNumber(wayNumber);
							collectDto.setBasicItemId(basicItemId);
							collectDto.setProbableNum(probableNum);
							collectDto.setReplenishNum(replenishNum);
							collectDto.setReplenishTime(DateUtil.formatYYYYMMDDHHMMSS(replenishDto.getReplenishTime()));
							collectDto.setNeedFrontValue(StringUtils.join(ids, ","));
							collectDto.setReplenishList(replenishList.subList(firstIndex, i+1));
							if(StringUtils.isBlank(form.getVmCode()) ) {
								if(probableNum!=replenishNum) {
									replenishCollectList.add(collectDto);
								}
							}else {
								replenishCollectList.add(collectDto);
							}
							firstIndex = i+1;
							probableNum = replenishNum = 0;
						}
					}
				}
			}
		}
		log.info("<ReplenishCorrectServiceImpl--getReplenishInfo--end>");
		return replenishCollectList;
	}

	@Override
	public boolean updateReplenishInfo(String ids) {
		log.info("<ReplenishCorrectServiceImpl--updateReplenishInfo--start>");
		boolean result = replenishCorrectDao.updateReplenishInfo(ids);
		log.info("<ReplenishCorrectServiceImpl--updateReplenishInfo--end>");
		return result;
	}
	
	@Override
	public List<ReplenishCollectDto> getReplenishProcess(ReplenishForm form) {
		log.info("<ReplenishCorrectServiceImpl--getReplenishInfo--start>");
		List<ReplenishDto> replenishInfo = replenishCorrectDao.getReplenishProcess(form);
		Map<String, Map<Integer, Map<Long, List<ReplenishDto>>>> collect = 
				replenishInfo.stream().collect(Collectors.groupingBy(ReplenishDto::getVmCode,
				Collectors.groupingBy(ReplenishDto::getWayNumber,Collectors.groupingBy(ReplenishDto::getBasicItemId))));
		List<ReplenishCollectDto> replenishCollectList = new ArrayList<ReplenishCollectDto>();
		for(Map.Entry<String, Map<Integer, Map<Long, List<ReplenishDto>>>> vmEny : collect.entrySet()){
			String vmCode = vmEny.getKey();
			Map<Integer, Map<Long, List<ReplenishDto>>> wayMap = vmEny.getValue();
			for(Map.Entry<Integer, Map<Long, List<ReplenishDto>>> wayEntry : wayMap.entrySet()){
				Integer wayNumber = wayEntry.getKey();
				Map<Long, List<ReplenishDto>> itemMap = wayEntry.getValue();
				for(Map.Entry<Long, List<ReplenishDto>> itemEntry : itemMap.entrySet()){
					Long basicItemId = itemEntry.getKey();
					List<ReplenishDto> replenishList = itemEntry.getValue();
					int replenishNum = 0;
					int firstIndex = 0;
					List<Long> ids = new ArrayList<Long>();
					for (int i=0; i< replenishList.size() ; i++) {
						ReplenishDto replenishDto = replenishList.get(i);
//						if(i==firstIndex && replenishDto.getOpType() == 4 && i!=replenishList.size()-1){
//							continue;
//						}
						//probableNum += (replenishDto.getNum() - replenishDto.getPreNum());
						if(replenishDto.getOpType() != 4){
							replenishNum += (replenishDto.getNum() - replenishDto.getPreNum());
						}else{
							ids.add(replenishDto.getReplenishId());
						}
						//log.info(replenishDto.getItemName()+"补货数量"+replenishNum);
						if((i+1)>=replenishList.size() || !DateUtils.isSameDay(replenishList.get(i+1).getReplenishTime(), replenishDto.getReplenishTime()) ||
								replenishList.get(i+1).getOpType() ==4 ){
							ReplenishCollectDto collectDto = new ReplenishCollectDto();
							collectDto.setPic(replenishDto.getPic());
							collectDto.setItemName(replenishDto.getItemName());
							collectDto.setVmCode(vmCode);
							collectDto.setWayNumber(wayNumber);
							collectDto.setBasicItemId(basicItemId);
							//collectDto.setProbableNum(probableNum);
							collectDto.setReplenishNum(replenishNum);
							collectDto.setReplenishTime(DateUtil.formatYYYYMMDDHHMMSS(replenishDto.getReplenishTime()));
							collectDto.setNeedFrontValue(StringUtils.join(ids, ","));
							collectDto.setReplenishList(replenishList.subList(firstIndex, i+1));
							replenishCollectList.add(collectDto);
							firstIndex = i+1;
							replenishNum = 0;
						}
					}
				}
			}
		}
		log.info("<ReplenishCorrectServiceImpl--getReplenishInfo--end>");
		return replenishCollectList;
	}
}
