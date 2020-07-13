package com.server.module.system.repairManage.repairMaterial;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  repair_material
 * author name: yjr
 * create time: 2019-08-13 14:38:18
 */ 
@Data
@Entity(tableName="repair_material",id="id",idGenerate="auto")
public class RepairMaterialBean{


	private Long id;
	private String name;
	private BigDecimal price;
	private String standard;
	private Integer deleteFlag;
}

