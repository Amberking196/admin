package com.server.module.system.memberManage.memberTypeManage;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: member_type author name: why create time: 2018-09-20 16:08:54
 */
@Data
@Entity(tableName = "member_type", id = "id", idGenerate = "auto")
public class MemberTypeBean {

	// 主键ID
	private Long id;
	// 会员类型名
	private String type;
	// 会员折扣
	private Double discount;
	// 会员充值金额
	private Double money;
	// 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	// 创建用户
	private Long createUser;
	// 更新时间
	private Date updateTime;
	// 更新用户
	private Long updateUser;
	// 是否删除 0 未删除 1 已删除
	private Integer deleteFlag;
	//有效期  1: 一个月 2：一年 
	private Integer validity;
	//备注
	private String remark;

	@NotField
	private String validityLabel;
	
	public String getValidityLabel() {
		if(validity==1) {
			return " 一个月";
		}
		if(validity==2) {
			return " 一年";
		}
		return "";
	}

}
