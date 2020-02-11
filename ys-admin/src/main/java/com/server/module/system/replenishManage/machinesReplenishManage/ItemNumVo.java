package com.server.module.system.replenishManage.machinesReplenishManage;
import com.server.common.utils.excel.annotation.ExcelField;
import org.apache.commons.lang.StringUtils;

import lombok.Data;

@Data
public class ItemNumVo {
	String vmCode;
	int wayNumber;
	@ExcelField(title = "产品/规格",align=2)
	String itemName;
	String simpleName;
	//@ExcelField(title = "容量",align=2)
	int fullNum;
	//@ExcelField(title = "数量",align=2)
	int num;
	@ExcelField(title = "缺货量",align=2)
	int needNum;


	
	double rate;

	public double getRate() {
		if(this.getFullNum()!=0)
		rate=(this.getFullNum()-this.getNum())/this.getFullNum();
	
		return rate*100;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getSimpleName() {
		if(StringUtils.isNotBlank(simpleName)){
			return simpleName;
		} else {
			return this.getItemName();
		}
		
	}
	public int getNeedNum(){
		return this.fullNum-this.num;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	
	
}
