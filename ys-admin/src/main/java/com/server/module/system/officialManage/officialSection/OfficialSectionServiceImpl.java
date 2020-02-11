package com.server.module.system.officialManage.officialSection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminMenu.AdminMenuBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-08-01 10:02:19
 */ 
@Service
public class  OfficialSectionServiceImpl  implements OfficialSectionService{

	public static Logger log = LogManager.getLogger(OfficialSectionServiceImpl.class);
	@Autowired
	private OfficialSectionDao officialSectionDaoImpl;

	/**
	 * 官网栏目列表
	 * @param officialSectionForm
	 * @return
	 */
	public ReturnDataUtil listPage(OfficialSectionForm officialSectionForm){
		log.info("<OfficialSectionServiceImpl>------<listPage>-----start");
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<OfficialSectionBean> officialSectionBeanList = officialSectionDaoImpl.listPage(officialSectionForm);
		
		if(officialSectionBeanList!=null && officialSectionBeanList.size()>0){
			for (OfficialSectionBean officialBean : officialSectionBeanList) {
				if(officialBean.getPid()!=0){
					for (OfficialSectionBean officialSecondBean : officialSectionBeanList) {
						if(officialSecondBean.getId().equals(officialBean.getPid())){
							officialBean.setName(officialSecondBean.getName()+"/"+officialBean.getName());
							//officialSecondBean.getList().add(officialBean);
						}
					}
				}
			}
//			Iterator<OfficialSectionBean> iterator = officialSectionBeanList.iterator();
//			while(iterator.hasNext()){
//				OfficialSectionBean officialSectionBean = iterator.next();
//				if(officialSectionBean.getPid()!=0){
//					iterator.remove();
//				}
//			}
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setTotal(new Long((long)officialSectionBeanList.size()));
			returnData.setReturnObject(officialSectionBeanList);
		}else{
			returnData.setStatus(0);
			returnData.setMessage("无栏目");
			returnData.setReturnObject(officialSectionBeanList);
		}
		log.info("<OfficialSectionServiceImpl>------<listPage>-----end");
		return returnData;
	}

	/**
	 * 官网栏目修改
	 * @param officialSectionBean
	 * @return
	 */
	public ReturnDataUtil updateSonSection(OfficialSectionBean officialSectionBean) {
		log.info("<OfficialSectionServiceImpl>------<updateSonSection>-----start");
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<OfficialSectionBean> officialSectionSonBeanList=officialSectionBean.getList();
		Boolean flag=true;
		for(OfficialSectionBean osb:officialSectionSonBeanList) {
			if(osb.getId()==null) {
				osb.setPid(officialSectionBean.getId());
				osb.setCreateUser(UserUtils.getUser().getId());
				OfficialSectionBean newOfficialSectionBean=officialSectionDaoImpl.insert(osb);
				if(newOfficialSectionBean==null){
					flag=false;
					break;
				}
			}
		}
		if(flag==false) {
			returnData.setStatus(0);
			returnData.setMessage("新增失败");
		}
		else {
			returnData.setStatus(1);
			returnData.setMessage("新增成功");
		}
		log.info("<OfficialSectionServiceImpl>------<updateSonSection>-----end");
		return returnData;
	}

	/**
	 * 官网栏目添加
	 * @param entity
	 * @return
	 */
	
	public OfficialSectionBean add(OfficialSectionBean entity) {
		return officialSectionDaoImpl.insert(entity);
	}

	/**
	 * 官网栏目修改
	 * @param entity
	 * @return
	 */
	public boolean update(OfficialSectionBean entity) {
		Boolean flag=true;
		entity.setUpdateUser(UserUtils.getUser().getId());
		//用来保存查询出来的结果Id
		Set<Integer> oldList=new HashSet<Integer>();
		//用来保存查询传入的结果Id
		Set<Integer> newList=new HashSet<Integer>();
		OfficialSectionForm officialSectionForm=new OfficialSectionForm();
		officialSectionForm.setPid(entity.getId().intValue());
		List<OfficialSectionBean> list = officialSectionDaoImpl.listPage(officialSectionForm);

		if(officialSectionDaoImpl.update(entity)) {
			if(entity.getList()!=null && entity.getList().size()>0) {
				for(OfficialSectionBean secondBean:entity.getList()) {
					if(secondBean.getId()==null) {
						secondBean.setPid(entity.getId());
						secondBean.setCreateUser(UserUtils.getUser().getId());
						officialSectionDaoImpl.insert(secondBean);
					}
					else {
						newList.add(secondBean.getId().intValue());
						for (OfficialSectionBean officialSectionBean : list) {
							oldList.add(officialSectionBean.getId().intValue());
							if (secondBean.getId().equals(officialSectionBean.getId())) {
								secondBean.setUpdateUser(UserUtils.getUser().getId());
								update(secondBean);
							}
						}
					}

				}
			}
		}
		else {
			flag=false;
		}
		oldList.removeAll(newList);
		for (Integer i : oldList) {
			del(i);
		}
		return flag;
	}

	/**
	 * 官网栏目删除
	 * @param id
	 * @return
	 */
	public ReturnDataUtil del(Object id) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		OfficialSectionBean osb=officialSectionDaoImpl.get(id);
		List<OfficialSectionBean> list=officialSectionDaoImpl.findSonSectionList(osb);
		Boolean flag=true;
		osb.setDeleteFlag(1);
		osb.setUpdateUser(UserUtils.getUser().getId());
		flag=officialSectionDaoImpl.update(osb);

		for(OfficialSectionBean o:list) {
			o.setDeleteFlag(1);
			o.setUpdateUser(UserUtils.getUser().getId());
			officialSectionDaoImpl.update(o);
		}
		if(flag==false) {
			returnData.setStatus(0);
			returnData.setMessage("删除失败");
		}
		else {
			returnData.setStatus(1);
			returnData.setMessage("删除成功");
		}
		return returnData;
	}

	/**
	 * 官网栏目获取
	 * @param id
	 * @return
	 */
	public OfficialSectionBean get(Object id) {
		return officialSectionDaoImpl.get(id);
	}
}

