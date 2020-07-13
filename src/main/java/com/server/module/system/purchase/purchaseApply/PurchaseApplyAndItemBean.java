package com.server.module.system.purchase.purchaseApply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.server.module.system.purchase.purchaseApplyItem.PurchaseApplyBillItemBean;

import lombok.Data;
/**
 * 用于接受申请采购单的数据
 * @author 26920
 *
 */
@Data
public class PurchaseApplyAndItemBean extends PurchaseApplyBillBean{
	//创建集合用于接受申请采购单下的商品数据
	private List<PurchaseApplyBillItemBean>items=new ArrayList<PurchaseApplyBillItemBean>();
	private Integer type;//0代表不通过，1.代表通过
}
