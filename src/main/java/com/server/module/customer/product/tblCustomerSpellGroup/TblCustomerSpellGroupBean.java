package com.server.module.customer.product.tblCustomerSpellGroup;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.util.StringUtil;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: tbl_customer_spellGroup author name: why create time: 2018-10-17
 * 11:06:09
 */
@Data
@Entity(tableName = "tbl_customer_spellGroup", id = "id", idGenerate = "auto")
public class TblCustomerSpellGroupBean {

	// 主键id
	private Long id;
	// 商品id
	private Long goodsId;
	// 发起拼团人id
	private Long startCustomerId;
	// 参与拼图人id
	private String participationCustomerId;
	// 创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	// 更新时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	//拼团的状态  是否拼团成功 0失败 1成功 2拼团中
	private Integer state;
	//拼团活动id（shopping_goods_spellgroup表id）
	private Long spellGroupId;
	
	//拼团结束时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	//拼图商品名称
	@NotField
	private String spellProductName;
	//用户的省份
	@NotField
	private String province;
	//用户所在的城市
	@NotField
	private String city;
	//用户名称
	@NotField
	private String userName;
	// 用户手机号
	@NotField
	private String phone;
	// 参团人数
	@NotField
	private Integer participationNUm;
	// 商品成团总人数
	@NotField
	private Integer totalStaff;
	// 团购剩余时间
	@NotField
	private Long hour;

	//用户头像
	@NotField
	private String headimgurl;


	@NotField
	private Integer startState;//初始拼团状态
	

	public int getParticipationNUm() {
		if (StringUtil.isNotBlank(this.participationCustomerId)) {
			String[] split = this.participationCustomerId.split(",");
			return split.length;
		}
		return 0;
	}
	public String getState() {
		if(this.state!=null) {
			if(this.state==0) //0失败 1成功 2拼团中'
				return "拼团失败";
			if(this.state==1)
				return "拼团成功";
			if(this.state==2)
				return "拼团中";
			if(this.state==3)
				return "拼团未开始";
		}
		return "";
	}

}
