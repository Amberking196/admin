package com.server.module.system.memberManage.memberManager;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * author name: why create time: 2018-09-20 17:36:37
 */
@Data
public class MemberBean {

	// 会员id
	public Long customerId;
	// 会员类型id
	public Long memberTypeId;
	// 会员类型
	public String type;
	// 会员电话
	public String phone;
	// 会员开始时间
	public String startTime;
	// 会员结束时间
	public String endTime;
	// 是否是会员 0非会员 1会员
	public Integer isMember;
	// 会员有效期
	private Integer validity;
	// 用户余额
	private Double userBalance;
	// 用户积分
	private Long integral;
	//机器编码
	private String vmCode;
	// 用户状态 0 新增用户,1 一次用户 ,2 活跃用户 ,3 忠实用户 ,4 低频用户 ,5 流失用户 ,6回流用户
	private Integer state;
	// 是否关注公众号 0未关注 1已关注
	private Integer follow;
	// 是否已送券 0无 1有
	private Integer isSend;
	//创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@NotField
	private Date startTimeLabel;
	@NotField
	public Date endTimeLabel;

	@NotField
	private String stateLabel;
	@NotField
	private String followLabel;
	@NotField
	private String isSendLabel;
	@NotField
	private String companyName;
	public String getStateLabel() {
		if (state == 0) {
			stateLabel = "新增用户";
		}
		if (state == 1) {
			stateLabel = "一次用户";
		}
		if (state == 2) {
			stateLabel = "活跃用户";
		}
		if (state == 3) {
			stateLabel = "忠实用户";
		}
		if (state == 4) {
			stateLabel = "低频用户";
		}
		if (state == 5) {
			stateLabel = "流失用户";
		}
		if (state == 6) {
			stateLabel = "回流用户";
		}
		return stateLabel;
	}
	
	public String getFollowLabel() {
		if (follow == 0) {
			followLabel = "未关注";
		}
		if (follow == 1) {
			followLabel = "已关注";
		}
		return followLabel;
	}
	
	public String getIsSendLabel() {
		if (isSend == 0) {
			isSendLabel = "未送券";
		}
		if (isSend == 1) {
			isSendLabel = "已送券";
		}
		return isSendLabel;
	}

}
