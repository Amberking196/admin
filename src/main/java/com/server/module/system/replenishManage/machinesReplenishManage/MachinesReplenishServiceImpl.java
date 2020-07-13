package com.server.module.system.replenishManage.machinesReplenishManage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.module.system.machineManage.machineReplenish.ReplenishDetailVo;
import com.server.module.system.machineManage.machineReplenish.ReplenishDetailVo1;
import jersey.repackaged.com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-04-23 16:19:47
 */
@Service
public class MachinesReplenishServiceImpl implements MachinesReplenishService {

	public static Logger log = LogManager.getLogger(MachinesReplenishServiceImpl.class);
	@Autowired
	private MachinesReplenishDao machinesReplenishDaoImpl;

	/**
	 * 补货管理 的列表查询
	 */
	public ReturnDataUtil listPage(MachinesReplenishCondition condition) {

		ReturnDataUtil data=null;
		List<MachinesReplenishBean> list=null;
		if(condition.getVersion()==1) {
			data=machinesReplenishDaoImpl.listPage(condition);
		} else if(condition.getVersion()==2){
			data=machinesReplenishDaoImpl.listPage1(condition);
		}else{
			list=Lists.newArrayList();
		}
		list=(List<MachinesReplenishBean>)data.getReturnObject();
		Map errorMap=machinesReplenishDaoImpl.listAllErrorMachine();
		if(errorMap.size()!=0)
		for (MachinesReplenishBean obj : list) {
			if(errorMap.get(obj.getCode())!=null){
				obj.setErrorInfo((String)errorMap.get(obj.getCode()));
			}
		}

		return data;
	}

	/**
	 * 根据 公司 区域  查询 线路以及线路下的收货机编号
	 */
	@Override
	public List<MachinesReplenishBean> getLine() {
		// TODO Auto-generated method stub
		return machinesReplenishDaoImpl.getLine();
	}

	
	/**
	 * 根据售货机编号 查询该售货机详细缺货信息
	 */
	public List<ReplenishmentDetailsBean> getDetails(String code,int version){

		List<ReplenishmentDetailsBean> list=Lists.newArrayList();

		if(version==1){
			list=machinesReplenishDaoImpl.getDetails(code);
		}else if(version==2){
			list=machinesReplenishDaoImpl.getDetails1(code);
		}
		return list;
	}
	
	/**
	 * 根据线路ID 查询这条线路上的售货机的缺货信息
	 */
	@Override
	public List<ReplenishmentDetailsBean> getPreview(String lineId,String code,int version) {
		return machinesReplenishDaoImpl.getPreview(lineId,code,version);
	}

	@Override
	public List<ReplenishDetailVo1> genReplenishDetail(List<MachinesReplenishBean> list,int version) {
		List<ReplenishDetailVo1> listDetail= Lists.newArrayList();

		if(list.size()==0) {
			return listDetail;
		}

		List<ReplenishDetailVo> listVo=null;
		if(version==1)
			listVo=machinesReplenishDaoImpl.selectReplenishDetail(list);
		else
			listVo=machinesReplenishDaoImpl.selectReplenishDetail1(list);

		Map<String,String> mapCodeToAddress= Maps.newHashMap();
		for (MachinesReplenishBean machinesReplenishBean : list) {
			mapCodeToAddress.put(machinesReplenishBean.getCode(),machinesReplenishBean.getLocatoinName());
		}

		Set<String> codeSet= Sets.newHashSet();
		Set<Long> itemSet=Sets.newHashSet();
		for (int i = 0; i < listVo.size(); i++) {
			ReplenishDetailVo vo=listVo.get(i);
			codeSet.add(vo.getCode());
			itemSet.add(vo.getItemId());
		}
		Iterator<Long> it1=itemSet.iterator();
		Long[] itemIds=new Long[itemSet.size()];
        itemSet.toArray(itemIds);
		Iterator<String> it=codeSet.iterator();
		int [] sum=new int[itemIds.length];
		String[] titles=new String[itemIds.length];
		ReplenishDetailVo1 title=new ReplenishDetailVo1();
		title.setCode("");
		title.setAddress("");
		title.setDetails(titles);
		listDetail.add(title);
		while (it.hasNext()){
			String code=it.next();
			ReplenishDetailVo1 vo1=new ReplenishDetailVo1();
			vo1.setCode(code);
			String  [] details=new String[itemIds.length];
			for (int i = 0; i < listVo.size(); i++) {
				ReplenishDetailVo vo=listVo.get(i);
				if(code.equals(vo.getCode())){
					vo1.setAddress(vo.getAddress());
					Long id=vo.getItemId();
					for (int i1 = 0; i1 < itemIds.length; i1++) {
						if(id.intValue()==itemIds[i1].intValue()){
							int temp=0;
							String str=details[i1];
							if(str!=null && !str.equals(""))
							temp=Integer.parseInt(str);
							details[i1]=(temp+vo.getFullNum()-vo.getNum())+"";
							sum[i1]=sum[i1]+(vo.getFullNum()-vo.getNum());
							titles[i1]=vo.getItemName();
						}
					}
				}
			}
			vo1.setDetails(details);
			vo1.setAddress(mapCodeToAddress.get(code));
			listDetail.add(vo1);
		}

		ReplenishDetailVo1 foot=new ReplenishDetailVo1();
		foot.setCode("");
		foot.setAddress("");
		String[] nums=new String[sum.length];
		for (int i = 0; i < nums.length; i++) {
			nums[i]=sum[i]+"";
		}
		foot.setDetails(nums);
		listDetail.add(foot);


		return listDetail;
	}

	@Override
	public List<MachinesReplenishBean> listDetailVm(String[] codes,int version) {

		return machinesReplenishDaoImpl.listDetailVm(codes,version);
	}
	public List<ItemNumVo> getItemsByCode(List<String> listCode,int version){
		return machinesReplenishDaoImpl.getItemsByCode(listCode,version);
	}
    public static void main(String[] args) {
		int [] nums={1,2,3,4};
		//Arrays.stream(nums).
	}
}
