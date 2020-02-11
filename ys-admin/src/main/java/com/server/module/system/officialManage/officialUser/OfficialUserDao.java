package com.server.module.system.officialManage.officialUser;

import com.server.util.ReturnDataUtil;

import java.util.List;

public interface OfficialUserDao {
    /**
     * 查询所有的loginCode
     * @param
     * @return
     */
    List<String> findAllLoginCode();

    /**
     * 注册用户信息
     * @param officialUser
     * @return
     */
    OfficialUserBean createOfficialUser( OfficialUserBean officialUser );

    /**
     * 用户登录
     * @param loginCode 登录名字 /手机号码
     * @param password  密码
     * @return  登录用户信息，如没有该用户返回null
     */
    OfficialUserBean officialUserLogin( String loginCode, String password );


    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    OfficialUserBean findUserById( Long id );

    /**
     * 用户变更密码
     * @param userId  用户ID
     * @param newPassword  新密码
     * @return  更新标记 1成功，0失败
     */
    int updatPassword( Long userId, String newPassword );

    /**
     * 根据用户账号查询用户
     * @param loginCode
     * @return
     */
    OfficialUserBean findUserByLoginCode( String loginCode );

    ReturnDataUtil listPage( SearchOffUserForm userForm );

    public boolean updateOffUser( OfficialUserBean userBean );
}
