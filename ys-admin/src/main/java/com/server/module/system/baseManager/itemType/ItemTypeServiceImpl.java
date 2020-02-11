package com.server.module.system.baseManager.itemType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.server.module.system.adminMenu.AdminMenuBean;
import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-10 14:28:15
 */
@Service
public class ItemTypeServiceImpl implements ItemTypeService {

	public static Logger log = LogManager.getLogger(ItemTypeServiceImpl.class); 	    
	@Autowired
	private ItemTypeDao itemTypeDaoImpl;

    @Autowired
    private ReturnDataUtil returnDataUtil;
	@SuppressWarnings("unchecked")
	public ReturnDataUtil listPage(ItemTypeCondition condition) {
		log.info("ItemTypeDaoImpl---------listPage------ start"); 
		returnDataUtil= itemTypeDaoImpl.listPage(condition);
		log.info("ItemTypeDaoImpl---------listPage------ end"); 
		return returnDataUtil;
	}

	public ItemTypeBean add(ItemTypeBean entity) {
		log.info("ItemTypeDaoImpl---------add------ start"); 
		ItemTypeBean re= itemTypeDaoImpl.insert(entity);
		log.info("ItemTypeDaoImpl---------add------ end"); 
		return re;
	}

	public boolean update(ItemTypeBean entity) {
		log.info("ItemTypeDaoImpl---------update------ start"); 
		boolean re= itemTypeDaoImpl.update(entity);
		log.info("ItemTypeDaoImpl---------update------ end"); 
		return re;
	}

	public boolean del(Object id) {
		log.info("ItemTypeDaoImpl---------del------ start"); 
		boolean re= itemTypeDaoImpl.delete(id);
		log.info("ItemTypeDaoImpl---------del------ end"); 
		return re;
	}

	public List<ItemTypeBean> list(ItemTypeCondition condition) {
		log.info("ItemTypeDaoImpl---------list------ start"); 
		List<ItemTypeBean> list = itemTypeDaoImpl.list(condition);
		//将结果转为树形结构
		List<ItemTypeBean> treeList = formatTreeItemType(list);
		log.info("ItemTypeDaoImpl---------list------ end"); 
		return treeList;
	}

	public ItemTypeBean get(Object id) {
		log.info("ItemTypeDaoImpl---------get------ start"); 
		ItemTypeBean re= itemTypeDaoImpl.get(id);
		log.info("ItemTypeDaoImpl---------get------ end"); 
		return re;
	}

	@Override
	public Long getItemTypeId(String name) {
		log.info("ItemTypeDaoImpl---------getItemTypeId------ start"); 
		Long re=itemTypeDaoImpl.getItemTypeId(name);
		log.info("ItemTypeDaoImpl---------getItemTypeId------ end"); 
		// TODO Auto-generated method stub
		return re;
	}
	//将结果转为树形结构
	public List<ItemTypeBean> formatTreeItemType(List<ItemTypeBean> itemTypeList){
		if(itemTypeList!=null && itemTypeList.size()>0){
			for (ItemTypeBean itemTypeBean : itemTypeList) {
				if(itemTypeBean.getPid()!=0){
					for (ItemTypeBean itemType : itemTypeList) {
						if(itemType.getId().equals(itemTypeBean.getPid())){
							if(itemType.getList()==null){
								itemType.setList(new ArrayList<ItemTypeBean>());
							}
							itemType.getList().add(itemTypeBean);
						}
					}
				}
			}
			Iterator<ItemTypeBean> iterator = itemTypeList.iterator();
			while(iterator.hasNext()){
				ItemTypeBean itemTypeBean = iterator.next();
				if(itemTypeBean.getPid()!=0){
					iterator.remove();
				}
			}
		}
		return itemTypeList;
	}
}
