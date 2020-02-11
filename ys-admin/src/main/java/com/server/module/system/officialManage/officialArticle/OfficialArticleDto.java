package com.server.module.system.officialManage.officialArticle;

import java.util.Date;
import com.server.common.persistence.NotField;

import lombok.Data;

/**
 * table name: official_article author name: why create time: 2018-08-13
 * 16:39:48
 */
@Data
public class OfficialArticleDto {

	// 主键id
	private Long id;
	// 标题
	private String title;
	// 内容
	private String content;
	// 配图地址
	private String url;
	// 创建时间
	private Date createTime;

	

}
