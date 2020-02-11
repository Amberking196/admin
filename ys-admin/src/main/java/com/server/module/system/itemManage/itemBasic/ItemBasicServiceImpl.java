package com.server.module.system.itemManage.itemBasic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
/**
 * author name: why
 * create time: 2018-04-10 14:22:54
 */ 
@Service
public class  ItemBasicServiceImpl  implements ItemBasicService{


	public static Logger log = LogManager.getLogger(ItemBasicServiceImpl.class); 		
	@SuppressWarnings("unused")
	@Autowired
	private ItemBasicDao ItemBasicDaoImpl ;
	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private VendingMachinesInfoDao vendingMachinesInfoDaoImpl;

    @Autowired
    private ReturnDataUtil returnDataUtil;
	/**
	 * 添加商品
	 */
	@Override
	public ItemBasicBean insert(ItemBasicBean bean) {
		// TODO Auto-generated method stub
		log.info("<ItemBasicServiceImpl>--<insert>--start");  
		ItemBasicBean re=ItemBasicDaoImpl.insert(bean);
		log.info("<ItemBasicServiceImpl>--<insert>--end");  
		return re;
	}
	
	/**
	 * 查询商品基础信息列表
	 */
	@Override
	public  ReturnDataUtil listPage(ItemBasicCondition condition) {
		// TODO Auto-generated method stub
		log.info("<ItemBasicServiceImpl>--<listPage>--start");  
		returnDataUtil =ItemBasicDaoImpl.listPage(condition);
		log.info("<ItemBasicServiceImpl>--<listPage>--end");  
		return returnDataUtil;
	}

	/**
	 * 查询 我的商品库
	 */
	@Override
	public ReturnDataUtil myItemList(MyItemCondition condition) {

		log.info("<ItemBasicServiceImpl>--<myItemList>--start");  
		if(condition.getType()==1){ //我的商品库 
			String vmCode=condition.getVmCode();
			VendingMachinesInfoBean bean=vendingMachinesInfoDaoImpl.findVendingMachinesByCode(vmCode);
			Integer companyId=bean.getCompanyId();
			String companyIds=companyDaoImpl.findAllSonCompanyIdForInSql(companyId);
			log.info(companyIds);
			returnDataUtil= ItemBasicDaoImpl.myOwnListPage(condition,companyIds);
		} else if(condition.getType()==2){//基础库
			returnDataUtil= ItemBasicDaoImpl.myListPage(condition);
		} else {//所有商品
			
			
		} 
		log.info("<ItemBasicServiceImpl>--<myItemList>--end");  
		return returnDataUtil;
	}
	
	
	/**
	 * 通过商品ID 得到商品信息 
	 */
	@Override
	public ItemBasicBean getItemBasic(Object id) {
		log.info("<ItemBasicServiceImpl>--<getItemBasic>--start"); 
		ItemBasicBean re= ItemBasicDaoImpl.getItemBasic(id);
		log.info("<ItemBasicServiceImpl>--<getItemBasic>--end"); 
		// TODO Auto-generated method stub
		return re;
	}

	/**
	 * 修改商品信息
	 */
	
	public boolean updateEntity(ItemBasicBean entity) {
		log.info("<ItemBasicServiceImpl>--<updateEntity>--start");
		boolean re=ItemBasicDaoImpl.updateEntity(entity);
		log.info("<ItemBasicServiceImpl>--<updateEntity>--end"); 
		// TODO Auto-generated method stub
		return re;
	}
	
	/**
	 * 判断商品条形码是否存在
	 */
	@Override
	public  ItemBasicBean checkBarcode(String barCode) {
		log.info("<ItemBasicServiceImpl>--<checkBarcode>--start");
		ItemBasicBean re= ItemBasicDaoImpl.checkBarcode(barCode);
		log.info("<ItemBasicServiceImpl>--<checkBarcode>--end");
		return re;
	}
	
	
	/**
	 * 查询图片路径 进行下载
	 */
	@Override
	public List<String> getPic(){
		return ItemBasicDaoImpl.getPic();
	}
	
	/**
	 * 根据条形码 查询商品信息
	 */
	public List<ItemBasicBean> findItemBasic(ItemBasicCondition condition) {
		log.info("<ItemBasicServiceImpl>--<findItemBasic>--start");
		List<ItemBasicBean> list = ItemBasicDaoImpl.findItemBasic(condition);
		log.info("<ItemBasicServiceImpl>--<findItemBasic>--end");
		return list;
	}
	
	/**
	 * 处理图片名称
	 * @return
	 */
	public String findImgName(String fileName) {
		String imgName=null;
		String date = DateUtil.formatYYYYMMDDHHMMSS(new Date());
		String[] split = date.split(" ");
		String[] split2 = split[0].split("-");
		String[] split3 = split[1].split(":");
		String name=fileName.substring(fileName.length()-4, fileName.length());
		imgName=split2[0]+split2[1]+split2[2]+"_"+split3[0]+split3[1]+split3[2]+name;
		return imgName;
	}
	/**
	 * 查询所有商品  下拉框用
	 * @return
	 */
	public List<Map<String,Object>> listAllItem(){
		return  ItemBasicDaoImpl.listAllItem();
	}

	/**
	 * 通过商品名称 模糊查询
	 */
	@Override
	public List<ItemBasicBean> getItemBasic(ItemBasicCondition itemcondition) {
		// TODO Auto-generated method stub
		return ItemBasicDaoImpl.getItemBasic(itemcondition);
	}

	@Override
	public List<ItemBasicBean> findItemBasicByCode(ItemBasicCondition condition) {
		// TODO Auto-generated method stub
		return ItemBasicDaoImpl.findItemBasicByCode(condition);
	}
	
	@Override
    public void addAll(Integer basicItemId,List<Integer> basicItemIds) {
		ItemBasicDaoImpl.addAll(basicItemId,basicItemIds);
	}

    public void delAll(Integer basicItemId,List<Integer> basicItemIds) {
		ItemBasicDaoImpl.delAll(basicItemId,basicItemIds);
	}
    
	public List<ItemBasicBean> getItemConnect(ItemBasicCondition condition){
		return ItemBasicDaoImpl.getItemConnect(condition);
	}

}

