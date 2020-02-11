package com.server.module.system.statisticsManage.purchaseItemStatistics;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemDao;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemForm;
import com.server.module.system.warehouseManage.supplierManage.SupplierServiceImpl;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: hjc
 * create time: 2018-08-24 11:02:40
 */
@Service
public class PurchaseItemStatisticsServiceImpl implements PurchaseItemStatisticsService {

	public static Logger log = LogManager.getLogger(SupplierServiceImpl.class); 	 

    @Autowired
    private PurchaseBillItemDao purchaseBillItemDaoImpl;
    @Autowired
    private PurchaseItemStatisticsDao purchaseItemStatisticsDaoImpl;
    
    public ReturnDataUtil listPage(PurchaseItemStatisticsCondition condition) {
        return purchaseItemStatisticsDaoImpl.listPage(condition);
    }

    public PurchaseItemStatisticsBean add(PurchaseItemStatisticsBean entity) {
        return purchaseItemStatisticsDaoImpl.insert(entity);
    }

    public boolean update(PurchaseItemStatisticsBean entity) {
        return purchaseItemStatisticsDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return purchaseItemStatisticsDaoImpl.delete(id);
    }

    public List<PurchaseItemStatisticsBean> list(PurchaseItemStatisticsCondition condition) {
        return null;
    }

    public PurchaseItemStatisticsBean get(Long id) {
        return purchaseItemStatisticsDaoImpl.get(id);
    }

	@Override
	public boolean addToStatistics(PurchaseBillItemBean entity) {
		log.info("<PurchaseItemStatisticsServiceImpl>----<addToStatistics>-------start");
		BigDecimal d = new BigDecimal(0);
		PurchaseItemStatisticsCondition purchaseItemStatisticsCondition=new PurchaseItemStatisticsCondition();
		PurchaseBillItemForm purchaseBillItemForm=new PurchaseBillItemForm();
		purchaseBillItemForm.setIsShowAll(1);
		purchaseBillItemForm.setItemId(entity.getItemId());
		//purchaseItemStatisticsCondition.setSupplierId(entity.getSupplierId());
		purchaseItemStatisticsCondition.setItemId(entity.getItemId());
		
		List<PurchaseItemStatisticsBean> purchaseItemStatisticsBeanList=(List<PurchaseItemStatisticsBean>) purchaseItemStatisticsDaoImpl.listPage(purchaseItemStatisticsCondition).getReturnObject();
		if(purchaseItemStatisticsBeanList.size()>0) {
			Double sumPrice=0d;Long sumQuantity=0l;
			List<PurchaseBillItemBean> purchaseBillItemBeanList=(List<PurchaseBillItemBean>) purchaseBillItemDaoImpl.successListPage(purchaseBillItemForm).getReturnObject();
			if(purchaseBillItemBeanList.size()>0) {
				for(PurchaseBillItemBean pib:purchaseBillItemBeanList) {
					sumPrice=sumPrice+pib.getPrice()*pib.getQuantity();
					sumQuantity=sumQuantity+pib.getQuantity();
				}
				d = d.add(new BigDecimal(sumPrice));
				PurchaseItemStatisticsBean pisb=purchaseItemStatisticsBeanList.get(0);
				pisb.setAvgPrice(d.divide(new BigDecimal(sumQuantity), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
				pisb.setSumQuantity(sumQuantity);
				return(purchaseItemStatisticsDaoImpl.update(pisb));
			}
		}else {
			PurchaseItemStatisticsBean pisb=new PurchaseItemStatisticsBean();
			pisb.setAvgPrice(entity.getPrice());
			pisb.setSumQuantity(entity.getQuantity());
			pisb.setItemId(entity.getItemId());
			//pisb.setSupplierId(entity.getSupplierId());
			if(purchaseItemStatisticsDaoImpl.insert(pisb)!=null) {
				return true;
			}
			
		}
		return false;
	}

	@Override
	public boolean addToStatistics2(WarehouseBillItemBean entity) {
		log.info("<PurchaseItemStatisticsServiceImpl>----<addToStatistics>-------start");
		BigDecimal d = new BigDecimal(0);
		PurchaseItemStatisticsCondition purchaseItemStatisticsCondition=new PurchaseItemStatisticsCondition();
		PurchaseBillItemForm purchaseBillItemForm=new PurchaseBillItemForm();
		purchaseBillItemForm.setIsShowAll(1);
		purchaseBillItemForm.setItemId(entity.getItemId());
		//purchaseItemStatisticsCondition.setSupplierId(entity.getSupplierId());
		purchaseItemStatisticsCondition.setItemId(entity.getItemId());
		
		List<PurchaseItemStatisticsBean> purchaseItemStatisticsBeanList=(List<PurchaseItemStatisticsBean>) purchaseItemStatisticsDaoImpl.listPage(purchaseItemStatisticsCondition).getReturnObject();
		if(purchaseItemStatisticsBeanList.size()>0) {
			BigDecimal sumPrice=new BigDecimal(0);
			Long sumQuantity=0l;

			PurchaseItemStatisticsBean pisb=purchaseItemStatisticsBeanList.get(0);
			if(entity.getOutput()==1) {
				sumPrice=new BigDecimal(pisb.getAvgPrice()*pisb.getSumQuantity()).subtract(entity.getMoney());
				sumQuantity=pisb.getSumQuantity()-entity.getQuantity();
				
			}else {
				sumPrice=new BigDecimal(pisb.getAvgPrice()*pisb.getSumQuantity()).add(entity.getMoney());
				sumQuantity=pisb.getSumQuantity()+entity.getQuantity();
			}
			d = d.add(sumPrice);
			pisb.setAvgPrice(d.divide(new BigDecimal(sumQuantity), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
			pisb.setSumQuantity(sumQuantity);
			return(purchaseItemStatisticsDaoImpl.update(pisb));
			
		}else {
			PurchaseItemStatisticsBean pisb=new PurchaseItemStatisticsBean();
			pisb.setAvgPrice(entity.getPrice().doubleValue());
			pisb.setSumQuantity(entity.getQuantity().longValue());
			pisb.setItemId(entity.getItemId());
			//pisb.setSupplierId(entity.getSupplierId());
			if(purchaseItemStatisticsDaoImpl.insert(pisb)!=null) {
				return true;
			}
			
		}
		return false;
	}
	
	public PurchaseItemStatisticsBean getBeanByItemId(Long itemId) {
		return purchaseItemStatisticsDaoImpl.getBeanByItemId(itemId);
	}

}

