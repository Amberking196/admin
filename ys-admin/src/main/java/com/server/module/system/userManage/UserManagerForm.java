package com.server.module.system.userManage;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.server.module.commonBean.PageAssist;

public class UserManagerForm extends PageAssist {
     
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date startDate;  //查询起始时间
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date endDate;    //查询结束时间
		private Long userId;    //用户id
		private String phone;   //电话
		private Long inviterId;  //邀请人id
		
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public Long getInviterId() {
			return inviterId;
		}
		public void setInviterId(Long inviterId) {
			this.inviterId = inviterId;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
	    
}
