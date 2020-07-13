package com.server.module.system.itemManage.itemBasic;

import com.server.common.persistence.Entity;

import lombok.Data;

@Data
@Entity(tableName="item_basic_vision",id="id",idGenerate="auto")
public class ItemBasicVisionBean {

	private Integer firstBasicItemId;
	private Integer secondBasicItemId;
}
