package com.server.module.system.officialManage.officialUser;

import com.server.common.persistence.Entity;
import lombok.Data;

@Data
@Entity(tableName = "official_user", id = "id")
public class OfficialUserBean {
    //自增标识
    private Long id;
    //登录账号
    private String loginCode;
    //密码
    private String password;
    //状态 1=可用 0=禁用
    private int status;
    //名字
    private String name;
    //用戶角色
    private int role;
    //公司默认ID为1
    private int companyId;
}
