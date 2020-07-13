package com.server.module.system.warehouseManage.stock;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillDao;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.dbpool.BaseDB;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogBean;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogDao;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillService;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemService;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr 
 * create time: 2018-05-22 10:49:58
 */
@Service
public class WarehouseStockServiceImpl implements WarehouseStockService {
	private static Logger log = LogManager.getLogger(WarehouseStockServiceImpl.class);
	@Autowired
	private WarehouseStockDao warehouseStockDaoImpl;
	@Autowired
	private WarehouseOutputBillService warehouseOutputBillService;
	@Autowired
	private WarehouseBillItemService warehouseBillItemService;
	@Autowired
	private WarehouseBillItemDao warehouseBillItemDao;
	@Autowired
	private WarehouseOutputBillDao warehouseOutputBillDao;
	@Autowired
	private WarehouseStockLogDao stockLogDao;
	public ReturnDataUtil listPage(WarehouseStockForm condition) {
		return warehouseStockDaoImpl.listPage(condition);
	}
	public WarehouseStockBean add(WarehouseStockBean entity) {
		return warehouseStockDaoImpl.insert(entity);
	}
	public boolean update(WarehouseStockBean entity) {
		return warehouseStockDaoImpl.update(entity);
	}
	public boolean del(Object id) {
		return warehouseStockDaoImpl.delete(id);
	}
	public List<WarehouseStockBean> list(WarehouseStockForm condition) {
		return null;
	}
	public WarehouseStockBean get(Object id) {
		return warehouseStockDaoImpl.get(id);
	}
    /**
     * 入库出库库存操作  
     */
	//@Override
	public synchronized void putStorage(Long billId) {
		//根据billId查出该单据
		WarehouseOutputBillBean bill= warehouseOutputBillService.getById(billId);
		if(bill.getOutput()==0){
			if(bill.getState().intValue()==60203)//已入库
				return ;
		}
		if(bill.getOutput()==1)
			if(bill.getState().intValue()==60403)//已出库
				return ;
		//查出该单据下的商品项目
		List<WarehouseBillItemBean> list=warehouseBillItemService.getByBillId(bill.getId().intValue());
		Long warehouseId=bill.getWarehouseId();
		System.out.println(bill.getOutput()+"   output");
		int output=bill.getOutput();// 0 入库  1 出库
		Connection conn = BaseDB.openConnection();
		try {
			conn.setAutoCommit(false);
			if(bill.getState()==60203){//
				log.info("已入库");
				return ;
			}
			if(bill.getState()==60403){
				log.info("已出库");
				return ;
			}
			log.info("size=="+list.size());
			for (WarehouseBillItemBean item : list) {
				Long itemId=item.getItemId();
                //根据商品id查询该商品的库存记录
				WarehouseStockBean stock=warehouseStockDaoImpl.getStock(warehouseId, itemId);
				long preQuantity=0;
				long quantity=0;
				if(output==0){//入库

					if(stock==null){//仓库没存在该商品,需要插入
						stock=new WarehouseStockBean();
						stock.setQuantity(item.getQuantity().longValue());
						stock.setItemId(itemId);
						stock.setWarehouseId(warehouseId);
						stock.setItemName(item.getItemName());
						stock.setCostPrice(item.getPrice().doubleValue());
						stock.setPrice(0.0d);
						quantity=item.getQuantity().longValue();
						stock.setCompanyId(bill.getCompanyId()*1l);
						stock.setCompanyName(bill.getCompanyName());
						stock.setWarehouseId(bill.getWarehouseId());
						stock.setWarehouseName(bill.getWarehouseName());
						stock.setBarCode(item.getBarCode());
						stock.setUnitName(item.getUnitName());
						warehouseStockDaoImpl.insert(conn,stock);
						item.setAveragePrice(item.getPrice());
					}else{//已存在该商品库存 更改数量 价格
						preQuantity=stock.getQuantity();
						stock.setQuantity(stock.getQuantity()+item.getQuantity());
						//动态计算价格
						//double average=warehouseStockDaoImpl.getAveragePrice(bill.getWarehouseId(),item.getItemId(),item.getQuantity(),item.getPrice());
						//stock.setCostPrice(average);
						//item.setAveragePrice(average);//记录此时的平均价格
						warehouseStockDaoImpl.update(conn,stock);
					}
					warehouseBillItemDao.update(conn,item);//记录此时的平均价格

				}else if(output==1){//出库 修改库存
					preQuantity=stock.getQuantity();
					System.out.println(stock.getQuantity()+"    "+item.getQuantity());
					System.out.println("==="+stock.getItemName()+"   qu="+(stock.getQuantity()-item.getQuantity()));
					stock.setQuantity(stock.getQuantity()-item.getQuantity());
					warehouseStockDaoImpl.update(conn,stock);

				}
				//日志保存
				WarehouseStockLogBean warehouseStockLogBean = new WarehouseStockLogBean();
				warehouseStockLogBean.setBillId(bill.getId());
				warehouseStockLogBean.setBillItemId(itemId);
				warehouseStockLogBean.setItemName(item.getItemName());
				warehouseStockLogBean.setPreQuantity(preQuantity);
				warehouseStockLogBean.setNum(item.getQuantity().longValue());
				warehouseStockLogBean.setOutput(bill.getOutput());
				warehouseStockLogBean.setStockId(stock.getId());
				warehouseStockLogBean.setItemId(itemId);
				warehouseStockLogBean.setWarehouseName(bill.getWarehouseName());
				warehouseStockLogBean.setQuantity(stock.getQuantity());
				warehouseStockLogBean.setWarehouseId(bill.getWarehouseId());
				warehouseStockLogBean.setType(bill.getType());
				stockLogDao.insert(conn,warehouseStockLogBean);
			}
			if(bill.getOutput()==0)
				bill.setState(60203);//已入库
			if(bill.getOutput()==1)
				bill.setState(60403);//已出库

			warehouseOutputBillDao.update(conn,bill);//更新单据状态
			log.info("更新完毕===");



			try {
				conn.commit();
			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			BaseDB.closeConnection(conn);
		}



	}
	
	/**
	 * 查询仓库现有的商品 
	 */
	@Override
	public List<WarehouseStockBean> getItemInfo(Long warehouseId,Integer type) {
		log.info("<WarehouseStockServiceImpl>-----<getItemInfo>----start");
		List<WarehouseStockBean> list = warehouseStockDaoImpl.getItemInfo(warehouseId,type);
		log.info("<WarehouseStockServiceImpl>-----<getItemInfo>----end");
		return list;
	}
	@Override
	public ReturnDataUtil checkOnlyOne(WarehouseStockBean bean) {
		log.info("<WarehouseStockServiceImpl>-----<checkOnlyOne>----start");
		ReturnDataUtil data = warehouseStockDaoImpl.checkOnlyOne(bean);
		log.info("<WarehouseStockServiceImpl>-----<checkOnlyOne>----end");
		return data;
	}
	@Override
	public ReturnDataUtil addItemToWarehouse(WarehouseStockBean bean) {
		log.info("<WarehouseStockServiceImpl>-----<addItemToWarehouse>----start");
		bean.setCreateTime(new Date());
		bean.setQuantity(0L);//设置库存数量
		bean.setCostPrice(0.0);//设置进货单价
		bean.setPrice(0.0);//设置销售单价
		ReturnDataUtil data = warehouseStockDaoImpl.addItemToWarehouse(bean);
		log.info("<WarehouseStockServiceImpl>-----<addItemToWarehouse>----end");
		return data;
	}
	@Override
	public ReturnDataUtil updateItemToWarehouse(WarehouseStockBean bean) {
		log.info("<WarehouseStockServiceImpl>-----<updateItemToWarehouse>----start");
		//先根据Id查询WarehouseStockBean的信息
		ReturnDataUtil data=new ReturnDataUtil();
		WarehouseStockBean warehouseStockBean = warehouseStockDaoImpl.get(bean.getId());
		//对数据进行修改并保存
		warehouseStockBean.setPurchaseNumber(bean.getPurchaseNumber());
		warehouseStockBean.setLowerLimit(bean.getLowerLimit());
		warehouseStockBean.setHigherLimit(bean.getHigherLimit());
		boolean flag = warehouseStockDaoImpl.update(warehouseStockBean);
		if(flag) {//保存成功
			data.setStatus(0);
		}
		log.info("<WarehouseStockServiceImpl>-----<updateItemToWarehouse>----end");
		return data;
	}
	@Override
	public ReturnDataUtil getWarehouseItem(Long warehouseId, String barCode) {
		// TODO Auto-generated method stub
		ReturnDataUtil data=new ReturnDataUtil();
		if(warehouseId==null){
			 data.setStatus(0);
			 data.setMessage("您还没选择仓库");
		}
		List<WarehouseStockBean> list = warehouseStockDaoImpl.getWarehouseItems(warehouseId,barCode);
		
		if ( list.size()==1){
			WarehouseStockBean warehouseStockBean=list.get(0);
			data.setReturnObject(warehouseStockBean);
		} else if(list.size()>1){
		    data.setStatus(0);
		    data.setMessage("查找到多个商品,请输入更详细的条形码");
		} else {
			data.setStatus(0);
		    data.setMessage("该仓库不存在该商品");
		}
		return null;
	}


	public WarehouseStockBean getWarehouseItem(Long warehouseId, Long itemId){
		return warehouseStockDaoImpl.getWarehouseItem(warehouseId, itemId);
	}

}
