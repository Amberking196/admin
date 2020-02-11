package com.server.common.paramconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix="file")
@Component
@Data
public class AlipayConfig {
	
	//商品基础信息图片地址
	private String itemImg;
	//二维码路径
	private String url;
	//二维码路径视觉
	private String visionUrl;
	//二维码图片保存地址
	private String location;
	
	//log图片保存地址
	private String img;

	//下载二维码路径
	private String qrCode;
	
	//公司logo 保存地址
	private String companyLogo;
	
	//商城商品图片保存地址
	private String shoppingGoodsImg;

	private String couponImgPath;
	
	//图片素材
	private String pictureMaterialImg;
		
	//富文本图片保存地址
	private String richTextImg;
	
	//富文本图片访问路径
	private String imageAccessPath;
	
	//支付宝二维码路径
	private  String aliUrl;
	//支付宝二维码生成图片保存地址
	private String aliLocation;
	
	//发布文章 配图图片保存地址
	private String articleImg;
	
	//故障申报 图片保存地址
	private String complainImg;
	
	//用户留言 视频保存地址
	private String videoLocation;
	
	//活动图片 保存地址
	private String activityImg;
	
	//售货机广告图片
	private String vmAdvertisingImg;
	
	//操作说明文档地址
	private String wordUrl;
	
	//机器注册页面地址
	private String smsRegister;
	//临时二维码保存地址
	private String temporary;
	
}
