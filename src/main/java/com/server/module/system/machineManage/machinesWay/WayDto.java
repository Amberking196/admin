package com.server.module.system.machineManage.machinesWay;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.common.persistence.NotField;

import lombok.Data;
@Data
public class WayDto {
	private Long wayId;
	private Integer wayNumber;
	private Long itemId;
	private Integer state;
	private Integer num;
	private Integer fullNum;
	private BigDecimal price;
	private BigDecimal costPrice;
	private Integer hot;
	private String itemName;
	private String pic;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;
	private String locatoinName;
	private Long basicItemId;
	private String vmiStateName;
	private Long picId;
	private String topLeftPic;
	@NotField
	private String stateName;
	//每个商品的销量
	@NotField
	private Integer salesVolume;
	//货道推荐容量
	@NotField
	private Integer recommendCapacity;
}	


