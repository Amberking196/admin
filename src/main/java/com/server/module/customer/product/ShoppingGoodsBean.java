package com.server.module.customer.product;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.module.customer.product.tblCustomerSpellGroup.TblCustomerSpellGroupBean;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * table name: shopping_goods author name: why create time: 2018-06-29 11:17:55
 */
@Data
@Entity(tableName = "shopping_goods", id = "id", idGenerate = "auto")
public class ShoppingGoodsBean {
	// 商品id
	private Long id;
	// 商品基础信息Id
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
	// 动态（ 便于扩展 1.最新 2.热销 3.秒杀）
	private String dynamic;
	// 商品数量
	private Long quantity;
	// 商品参数
	private String commodityParameters;
	// 购买须知
	private String purchaseNotes;
	// 商品类型 0 首页商品 1 广告商品 2 其他
	private Integer isHomeShow;
	// 广告商品图片
	private String advertisingPic;
	//限购次数
	private Integer purchaseLimitation;
	
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
	// 是否关联
	@NotField
	private Integer isRelevance;
	// 销售数量
	@NotField
	private Long salesQuantity;
	// 购买人数
	@NotField
	private Long purchaseTime;

	private Integer target;// 机器优惠劵的适用范围 1 公司 2 区域 3 机器
	private String vmCode;// 机器编号
	private Long areaId;// 区域id
	private String areaName;// 区域名称
	// 商品是否支持自取 0 不支持 1 支持
	private Integer isHelpOneself;

	@NotField
	private Integer currentPage;
	@NotField
	private String isHelpOneselfLabel;
	//用户拼团信息
	@NotField
	private List<TblCustomerSpellGroupBean> list;
	//是否为拼团商品    0.非拼团  1 为拼团  
	@NotField
	private  Integer isConglomerateCommodity;
	//几人团
	@NotField
	private Long num;
	//团购价
	@NotField
	private BigDecimal groupPurchasePrice;
	//券id
	private Long vouchersId;

	//团购活动名称
	@NotField
	private String spellgroupName;
	//团购限制次数
	@NotField
	private Integer numberLimit;

	//券ids
	private String vouchersIds;

	
	//砍价活动id
	private Long goodsBargainId;
	
	//商品团购id
	private Long spellgroupId;
	

	private Integer activityId;//0拼团 1普通 2砍价


	
	public String getIsHelpOneselfLabel() {
		if (isHelpOneself == null) {
			isHelpOneselfLabel = "";
		}
		else if (isHelpOneself == 0) {
			isHelpOneselfLabel = "不支持机器自取";
		}
		else if (isHelpOneself == 1) {
			isHelpOneselfLabel = "支持机器自取";
		}
		return isHelpOneselfLabel;
	}
}
