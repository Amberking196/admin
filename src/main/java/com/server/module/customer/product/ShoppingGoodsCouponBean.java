package com.server.module.customer.product;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;

import lombok.Data;
@Data
public class ShoppingGoodsCouponBean {
	// 商品id
		private Long id;
		//商品基础信息Id
		private Long basicItemId;
		// 商品名称
		private String name;
		// 所属公司
		private Long companyId;
		// 商品类型id
		private Long typeId;
		// 商品图片
		private String pic;
		// 商品条形码
		private String barCode;
		// 商品状态
		private Long state;
		// 采购方式
		private String purchaseWay;
		// 商品品牌
		private String brand;
		// 商品规格
		private String standard;
		// 商品单位
		private Long unit;
		// 商品包装规格
		private String pack;
		// 商品详情
		private String details;
		// 成本价
		private BigDecimal costPrice;
		// 销售价
		private BigDecimal salesPrice;
		// 优惠价
		private BigDecimal preferentialPrice;
		// 创建时间
		private Date createTime;
		// 创建用户
		private Long createUser;
		// 更新时间
		private Date updateTime;
		// 更新用户
		private Long updateUser;
		// 是否删除 0 未删除 1 已删除
		private Integer deleteFlag;
		//动态（ 便于扩展  1.最新      2.热销      3.秒杀）
		private String dynamic;
		//商品数量
		private Long quantity;
		//商品参数
		private String commodityParameters;
		//购买须知
		private String purchaseNotes;
		//商品类型   0 首页商品   1 广告商品  2 其他
		private Integer isHomeShow;
		//广告商品图片
		private String advertisingPic;
		// 序号
		@NotField
		private Integer number;
		// 公司名称
		@NotField
		private String companyName;
		// 单位名称
		@NotField
		private String unitName;
		// 状态名称
		@NotField
		private String stateName;
		// 类型名称
		@NotField
		private String typeName;
		//是否关联
		@NotField
		private Integer isRelevance;
		//销售数量
		@NotField
		private Long salesQuantity;
		//购买人数
		@NotField
		private Long  purchaseTime;
		
		private Integer target;// 机器优惠劵的适用范围  1 公司  2 区域 3 机器
		private String vmCode;//机器编号
		private Long areaId;//区域id
		private String areaName;//区域id

		private Long couponId;//优惠券id
		
		private Integer periodType;//有效时间类型：0 绝对时间 1 相对时间
		private Integer periodDay;// 有效天数 当periodType=1 时 该字段有意义，优惠的有效时间从领取当天now 到now+periodDay 为有效期
		//注:用户的所属优惠劵有效期统一保存到coupon_customer 的 startTime,endTime ，判断有效期统一从这里取数
		private Integer canSend;// 可以发送： 0 可以发送 1 不可以发送 不可以领取

		private Integer way;//赠券方式 1：购买返券，2：自助领券，3：活动赠券   4：注册返券
		private Integer type;//优惠券类型

		private Integer useWhere;// 1 机器优惠劵   2 商城优惠劵
		private Integer bindProduct;//0 不绑定  1 绑定
		private Integer sendMax;//购买返的劵数量
		private Double money;//满X元  满减券不需要设置
		private Double deductionMoney;//优惠金额
		private String couponPic;//优惠劵图片
		
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
		private Date startTime;//开始时间
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
		private Date endTime;//结束时间
		
		@NotField
		private String targetLabel;
		public String getTargetLabel() {
			// 1 公司  2 区域 3 机器
			if(target==0)
				targetLabel="全部";
			if(target==1)
				targetLabel="公司";
			if(target==2)
				targetLabel="区域";
			if(target==3)
				targetLabel="机器";
			return targetLabel;
		}

}
