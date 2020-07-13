package com.server.module.system.itemManage.TblStatisticsItemSale;

import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-02 09:36:44
 */
@Service
public class TblStatisticsItemSaleServiceImpl implements TblStatisticsItemSaleService {

	public static Logger log = LogManager.getLogger(TblStatisticsItemSaleServiceImpl.class); 	 
	
	@Autowired
	private TblStatisticsItemSaleDao tblStatisticsItemSaleDaoImpl;

	public ReturnDataUtil listPage(TblStatisticsItemSaleCondition condition) {
		return tblStatisticsItemSaleDaoImpl.listPage(condition);
	}

	public TblStatisticsItemSaleBean add() {
		TblStatisticsItemSaleBean tblStatisticsItemSaleBean=new TblStatisticsItemSaleBean();
		List<TblStatisticsItemSaleBean> itemSaleBean = tblStatisticsItemSaleDaoImpl.getItemSaleBean();
		TblStatisticsItemSaleBean insert=null;
		for (TblStatisticsItemSaleBean bean : itemSaleBean) {
			tblStatisticsItemSaleBean.setBasicItemId(bean.getBasicItemId());
			tblStatisticsItemSaleBean.setCompanyId(bean.getCompanyId());
			tblStatisticsItemSaleBean.setItemOnVmNum(bean.getItemOnVmNum());
			tblStatisticsItemSaleBean.setReportDate(DateUtil.formatYYYYMMDD(new Date()));
			tblStatisticsItemSaleBean.setCreateTime(new Date());
			insert = tblStatisticsItemSaleDaoImpl.insert(tblStatisticsItemSaleBean);
		}
		return insert;
	}

	public boolean update(TblStatisticsItemSaleBean entity) {
		return tblStatisticsItemSaleDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return tblStatisticsItemSaleDaoImpl.delete(id);
	}

	public List<TblStatisticsItemSaleBean> list(TblStatisticsItemSaleCondition condition) {
		return null;
	}

	public TblStatisticsItemSaleBean get(Object id) {
		return tblStatisticsItemSaleDaoImpl.get(id);
	}
	
	/**
	 * 查询商品上架机器数
	 */
	public List<TblStatisticsItemSaleBean> getItemSaleBean(){
		return tblStatisticsItemSaleDaoImpl.getItemSaleBean();
	}
}
