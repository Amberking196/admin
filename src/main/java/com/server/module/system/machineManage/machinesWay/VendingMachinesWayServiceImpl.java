package com.server.module.system.machineManage.machinesWay;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.machineManage.machineItem.VendingMachinesItemBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoDao;
import com.server.module.system.statisticsManage.payRecord.ListSaleNumCondition;
import com.server.module.system.statisticsManage.payRecord.ListSaleNumDTO;
import com.server.module.system.statisticsManage.payRecord.PayRecordDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr 
 * create time: 2018-04-12 14:04:38
 */
@Service
public class VendingMachinesWayServiceImpl implements VendingMachinesWayService {

	public static Logger log = LogManager.getLogger(VendingMachinesWayServiceImpl.class);
	@Autowired
	private VendingMachinesWayDao vendingMachinesWayDaoImpl;
	@Autowired
	private VendingMachinesInfoDao vendingMachinesInfoDaoImpl;
	@Autowired
	private PayRecordDao payRecordDaoImpl;

	public ReturnDataUtil listPage(VendingMachinesWayCondition condition) {
		return vendingMachinesWayDaoImpl.listPage(condition);
	}

	public VendingMachinesWayBean add(VendingMachinesWayBean entity) {
		entity.setItemId(0l);
		entity.setState(30001);
		entity.setFullNum(0);
		entity.setNum(0);
		entity.setCreateTime(new Date());
		return vendingMachinesWayDaoImpl.insert(entity);
	}
	
	public ReturnDataUtil checkAdd(VendingMachinesWayBean entity){
		
		String sql="select count(*) from vending_machines_way where vendingMachinesCode=? and wayNumber=?";
		List<Object> list=Lists.newArrayList();
		list.add(entity.getVendingMachinesCode());
		list.add(entity.getWayNumber());
		long count=vendingMachinesWayDaoImpl.selectCount(sql, list);
		ReturnDataUtil re=new ReturnDataUtil();
		if(count>0){
			re.setStatus(0);
			re.setMessage("该货道已创建");
			re.setReturnObject(false);
			return re;
			
		}
		return re;
		
	}

	@Override
	public VendingMachinesWayBean findByVmCodeAndWayNumber(String vmCode, Integer wayNum) {
		return vendingMachinesWayDaoImpl.findByVmCodeAndWayNumber(vmCode, wayNum);
	}

    @Override
    public Integer findwayNumberByVmCode(String vmCode) {

		return vendingMachinesWayDaoImpl.findwayNumberByVmCode(vmCode);
    }

    public boolean update(VendingMachinesWayBean entity) {
		return vendingMachinesWayDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return vendingMachinesWayDaoImpl.delete(id);
	}

	public List<VendingMachinesWayBean> list(VendingMachinesWayCondition condition) {
		return null;
	}

	public VendingMachinesWayBean get(Object id) {
		return vendingMachinesWayDaoImpl.get(id);
	}

	@Override
	public List<WayDto> listAll(String vmCode) {
		List<WayDto> list = vendingMachinesWayDaoImpl.listAll(vmCode);	
		if(list!=null&&list.size()>0){//计算最优货道容量推荐值
			//查询该机器商品种类
			int count = vendingMachinesWayDaoImpl.findItemCoutItem(vmCode,1);
			log.info("商品种类=="+count);
			//查询售货机一个月所有商品的销量总和
			ListSaleNumCondition condition=new ListSaleNumCondition();
			condition.setVmCode(vmCode);
			Date date = new Date();
			 Calendar rightNow = Calendar.getInstance();  
	        rightNow.setTime(date);  
	        rightNow.add(Calendar.MONTH, -1);  
		    Date dt1 = rightNow.getTime();  
		    String startTime = DateUtil.formatYYYYMMDD(dt1); 
			condition.setStartTime(startTime);
			condition.setEndTime(DateUtil.formatYYYYMMDD(date));
			ReturnDataUtil returnData = payRecordDaoImpl.listSaleNum(condition);
	        List<ListSaleNumDTO> list1 = (List<ListSaleNumDTO>)returnData.getReturnObject();
	        //商品的销量总和
	        int coutSalesVolume=0;
	       for (ListSaleNumDTO listSaleNumDTO : list1) {
	    	   coutSalesVolume+=listSaleNumDTO.getNum();
	       }
	       log.info("商品的销量总和=="+coutSalesVolume);
	       for (ListSaleNumDTO listSaleNumDTO : list1) {
	    	   	for (WayDto wayDto : list) {
	    	   		//判断货道是否相等
	    	   		if(listSaleNumDTO.getWayNumber()==wayDto.getWayNumber()&&listSaleNumDTO.getBasicItemId().equals(wayDto.getBasicItemId())) {
						//计算最优货道容量推荐值
	    	   		//计算最优货道容量推荐值
						BigDecimal a = new BigDecimal((listSaleNumDTO.getNum()*wayDto.getFullNum().intValue()*count));
						BigDecimal b = new BigDecimal(coutSalesVolume);
						BigDecimal newNumber=a.divide(b,0,BigDecimal.ROUND_HALF_UP);
						wayDto.setRecommendCapacity(newNumber.intValue());
					}
	    	   	}
				
			}
			
		}
		
		return list;
	}

	@Override
	public List<WayDto> listAllForExport(String vmCode) {
		List<WayDto> list = vendingMachinesWayDaoImpl.listAll(vmCode);	
		return list;
	}
	
	@Override
	public ReturnDataUtil bindItem(BindItemDto dto) {
		VendingMachinesItemBean newItem = new VendingMachinesItemBean();
		newItem.setBasicItemId(dto.getBasicItemId());
		Long companyId = vendingMachinesInfoDaoImpl.findVendingMachinesByCode(dto.getVmCode()).getCompanyId() * 1l;
		newItem.setCompanyId(companyId);
		newItem.setCostPrice(new BigDecimal(dto.getCostPrice()));
		newItem.setPrice(new BigDecimal(dto.getPrice()));
		newItem.setCreateTime(new Date());
		//newItem.setEndTime(dto.getEndTime());
		newItem.setHot(0);
		newItem.setUpdateTime(new Date());
		boolean b=vendingMachinesWayDaoImpl.bindItem(dto,newItem);
		return new ReturnDataUtil(b);
	}
	
	public ReturnDataUtil editWayAndItem(BindItemDto dto) {
		boolean b=vendingMachinesWayDaoImpl.editWayAndItem(dto);
		return new ReturnDataUtil(b);
	}
	/**
	 * 货道商品统计
	 */
	public ReturnDataUtil statisticsWayNum(StatisticsWayNumCondition condition){
		
		return vendingMachinesWayDaoImpl.statisticsWayNum(condition);
	}
	 //根据vmcode  货道编码查货道
	 public List<VendingMachinesWayBean> listWay(String vmCode,Integer wayNumber){
		 return vendingMachinesWayDaoImpl.listWay(vmCode, wayNumber);
	 }



}
