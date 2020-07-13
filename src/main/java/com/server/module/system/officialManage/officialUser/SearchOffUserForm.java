package com.server.module.system.officialManage.officialUser;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

@Data
public class SearchOffUserForm extends PageAssist {
    //登录账号
    private String loginCode;
    //状态 1=可用 0=禁用
    private int status;
    //名字
    private String name;
}
