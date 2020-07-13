package com.server.module.system.officialManage.officialArticle;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: official_article author name: why create time: 2018-08-01
 * 15:07:48
 */
@Data
public class OfficialArticleForm extends PageAssist {

	//文章标题
	String title;
	//栏目Id
	Integer sid;
}
