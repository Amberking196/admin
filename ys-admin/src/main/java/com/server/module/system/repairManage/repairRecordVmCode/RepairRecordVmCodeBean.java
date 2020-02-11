package com.server.module.system.repairManage.repairRecordVmCode;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.system.repairManage.repairRecordItem.RepairRecordItemBean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Entity(tableName="repair_record_vmcode",id="id",idGenerate="auto")
public class RepairRecordVmCodeBean{

	private Long id;
	private Long rid;

	private String remark;
	private String vmCode;
	private BigDecimal carPrice;
	private Date createTime;
	private Date updateTime;
	private Long createUser;
	private String reason;
	private String plan;
	private Integer state;
	private List<RepairRecordItemBean> itemList;


}

