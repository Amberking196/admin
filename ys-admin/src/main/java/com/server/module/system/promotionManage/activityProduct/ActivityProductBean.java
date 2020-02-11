package com.server.module.system.promotionManage.activityProduct;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: activity_product author name: why create time: 2018-08-23
 * 18:07:34
 */
@Data
@Entity(tableName = "activity_product", id = "id", idGenerate = "auto")
public class ActivityProductBean {

	//主键id
	private Long id;
	//活动id
	private Long activityId;
	//商品id
	private Long productId;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;

}
