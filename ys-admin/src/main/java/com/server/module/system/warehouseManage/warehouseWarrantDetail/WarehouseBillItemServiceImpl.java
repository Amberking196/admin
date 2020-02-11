package com.server.module.system.warehouseManage.warehouseWarrantDetail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.server.module.system.itemManage.itemBasic.ItemBasicBean;
import com.server.module.system.itemManage.itemBasic.ItemBasicDao;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-17 03:15:57
 */
@Service
public class WarehouseBillItemServiceImpl implements WarehouseBillItemService {

	public static Logger log = LogManager.getLogger(WarehouseBillItemServiceImpl.class);
	@Autowired
	private WarehouseBillItemDao warehouseBillItemDaoImpl;
	@Autowired
	private ItemBasicDao itemBasicDaoImpl;

	public WarehouseBillItemBean add(WarehouseBillItemBean entity) {
		return warehouseBillItemDaoImpl.insert(entity);
	}

	public boolean update(WarehouseBillItemBean entity) {
		return warehouseBillItemDaoImpl.update(entity);
	}
	
	public boolean delete(Object id) {
		return warehouseBillItemDaoImpl.delete(id);
	}

	public List<WarehouseBillItemBean> get(int billId) {
		log.info("<WarehouseWarrantDetailServiceImpl>----<get>----start");
		List<WarehouseBillItemBean> list = warehouseBillItemDaoImpl.get(billId);
		log.info("<WarehouseWarrantDetailServiceImpl>----<get>----end");
		return list;
	}

	public List<WarehouseBillItemBean> getByBillId(int billId) {
		List<WarehouseBillItemBean> list = warehouseBillItemDaoImpl.getByBillId(billId);
		return list;
	}

	/**
	 * 出入库 编辑后保存 审核
	 */
	@Override
	public void modification(WarehouseOutputBillBean bean) {
		//用来保存查询出来的结果Id
		Set<Integer> oldList=new HashSet<Integer>();
		//用来保存查询传入的结果Id
		Set<Integer> newList=new HashSet<Integer>();
		//得到数据库查询出来的结果
		List<WarehouseBillItemBean> list = get(bean.getId().intValue());
		//遍历传入的集合
		for (WarehouseBillItemBean warehouseBillItemBean : bean.getList()) {
			//传入是否为空
			if(warehouseBillItemBean.getId()==null) {
				ItemBasicBean checkBarcode = itemBasicDaoImpl.checkBarcode(warehouseBillItemBean.getBarCode());
				warehouseBillItemBean.setBillId(bean.getId().intValue());
				warehouseBillItemBean.setItemId(checkBarcode.getId());
				add(warehouseBillItemBean);
			}else {
				newList.add(warehouseBillItemBean.getId().intValue());
				for (WarehouseBillItemBean warehouseBillItemBean2 : list) {
					oldList.add(warehouseBillItemBean2.getId().intValue());
					if (warehouseBillItemBean.getId().equals(warehouseBillItemBean2.getId())) {
						update(warehouseBillItemBean);
					}
				}
			}
		}
		//求差集  进行删除
		oldList.removeAll(newList);
		for (Integer i : oldList) {
			delete(i);
		}
	}
	public ReturnDataUtil listPage(WarehouseBillItemForm condition){
		return warehouseBillItemDaoImpl.listPage(condition);
	}
}
