package com.server.module.system.officialManage.officialUser;

import lombok.Data;

@Data
public class OfficialUserForm {
    //用户ID
    private Long id;
    //登录账号
    private String loginCode;
    //密码
    private String password;
    //新密码
    private String newPassword;
}
