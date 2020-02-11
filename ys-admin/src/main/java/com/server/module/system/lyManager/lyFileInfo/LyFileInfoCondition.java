package com.server.module.system.lyManager.lyFileInfo;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: ly_file_info
 * author name: why
 * create time: 2018-04-04 09:54:45
 */
@Data
public class LyFileInfoCondition extends PageAssist {

	String name;
	String version;
	
}
