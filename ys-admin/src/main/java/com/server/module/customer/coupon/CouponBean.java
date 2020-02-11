package com.server.module.customer.coupon;

import java.util.Date;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * table name:  coupon
 * author name: yjr
 * create time: 2018-07-06 14:05:39
 */ 
@Data
@Entity(tableName="coupon",id="id",idGenerate="auto")
public class CouponBean{

	//@JsonIgnore	public String tableName="coupon";
//@JsonIgnore	public String selectSql="select * from coupon where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,name,way,money,deductionMoney,startTime,endTime,createTime,createUser,updateTime,updateUser,deleteFlag from coupon where 1=1 ";
	private Long id;
	private String name;//券名称
	private Integer type;// 优惠券类型    1 满减券  2  固定券  3 折扣券  4固定折扣券

	private Integer periodType;//有效时间类型：0 绝对时间 1 相对时间
	private Integer periodDay;// 有效天数 当periodType=1 时 该字段有意义，优惠的有效时间从领取当天now 到now+periodDay 为有效期
	//注:用户的所属优惠劵有效期统一保存到coupon_customer 的 startTime,endTime ，判断有效期统一从这里取数
	private Integer canSend;// 可以发送： 0 可以发送 1 不可以发送 不可以领取

	private Integer way;//赠券方式 1：购买返券，2：自助领券，3：活动赠券  4:注册返券  6:关注赠券7:会员赠券 8:邀请赠券
	private Integer useWhere;// 1 机器优惠劵   2 商城优惠劵
	private Integer target;// 机器优惠劵的适用范围  1 公司  2 区域 3 机器
	private Long companyId;
	private String companyName;
	private Long areaId;
	private String areaName;
	private String vmCode;
	private Integer sendMax;//购买返的劵数量
	private Double money;//满X元  满减券不需要设置
	private Double deductionMoney;//优惠金额
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;//开始时间
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;//结束时间
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Long createUser;
	private Date updateTime;
	private Long updateUser;
	private Integer deleteFlag;
	private String  remark;
	private String pic;

	private Integer bindProduct;//0 不绑定商品   1 绑定商品
	private Integer formulaMode; //优惠券结算方式    0 按订单结算   1 按桶结算
	//最大优惠金额
	private double maximumDiscount;

	@NotField
	private String stateLabel;// 已开始 或 已结束 未开始
	@NotField
	private String typeLabel;
	@NotField
	private String wayLabel;
	@NotField
	private String useWhereLabel;
	@NotField
	private String targetLabel;
	@NotField
	private Long quantity;//用户拥有优惠券数量
	@NotField
	private Long useQuantity;//已使用优惠券数量

	public String getUseWhereLabel() {
		if(useWhere==1)
			useWhereLabel="机器优惠劵";
		if(useWhere==2)
			useWhereLabel="商城优惠劵";
		return useWhereLabel;
	}

	public String getTargetLabel() {
		// 1 公司  2 区域 3 机器
		if(target==0)
			targetLabel="";
		if(target==1)
			targetLabel="公司";
		if(target==2)
			targetLabel="区域";
		if(target==3)
			targetLabel="机器";
		return targetLabel;
	}

	public String getStateLabel() {

		return getStateByDate(getStartTime(),getEndTime());
	}
	private String getStateByDate(Date startTime,Date endTime){
		Long now=System.currentTimeMillis();
		if(now <startTime.getTime()){
			return "未开始";
		}
		if(now >endTime.getTime()){
			return "已结束";
		}
		if(now >startTime.getTime() && now<endTime.getTime()){
			return "已开始";
		}
		return "";

	}
	public String getWayLabel() {
		if(way==1){
			return "购买返券";
		}
		if(way==2){
			return "自助领券";
		}
		if(way==3){
			return "活动赠券";
		}
		if(way==4){
			return "注册返券";
		}
		if(way==6) {
			return "关注赠券";
		}
		if(way==7) {
			return "会员赠券";
		}
		if(way==8) {
			return "邀请赠券";
		}
		return "";
	}

	public String getTypeLabel() {
		if(type==1){
			typeLabel="满减券";
		}
		if(type==2){
			typeLabel="固定券";
		}
		if(type==3){
			typeLabel="折扣券";
		}
		if(type==4) {
			typeLabel="固定折扣券";
		}
		return typeLabel;
	}
	@JsonIgnore
	public Date getLogicEndTime(){
		if(this.getPeriodType()==1){//非绝对时间
			int day=this.getPeriodDay();
			DateTime dt2 = new DateTime(System.currentTimeMillis());
			dt2=dt2.plusDays(day);
			dt2=dt2.withHourOfDay(23);
			dt2=dt2.withMinuteOfHour(59);
			dt2=dt2.withSecondOfMinute(59);
			return dt2.toDate();
		}else{
			return this.endTime;
		}
	}
	@JsonIgnore
	public Date getLogicStartTime(){
		if(this.getPeriodType()==1){//非绝对时间
			DateTime dt2 = new DateTime(System.currentTimeMillis());
			dt2=dt2.withHourOfDay(0);
			dt2=dt2.withMinuteOfHour(0);
			dt2=dt2.withSecondOfMinute(0);
			return dt2.toDate();
		}else{
			return this.startTime;
		}
	}
}

