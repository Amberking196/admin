package com.server.module.system.officialManage.officialUser;

import com.server.util.ReturnDataUtil;
import org.springframework.stereotype.Service;

@Service
public interface OfficialUserService {
    /**
     * 查询所有的loginCode
     * @param loginCode
     * @return
     */
    boolean loginCodeIsOnlyOne( String loginCode );

    /**
     * 注册用户信息
     * @param officialUser
     * @return
     */
    OfficialUserBean createOfficialUser( OfficialUserBean officialUser );

    /**
     * 用户登录
     * @param loginCode
     * @param password
     * @return  ReturnDataUtil  返回固定格式数据
     */
    ReturnDataUtil officialUserLogin( String loginCode, String password );

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    ReturnDataUtil findUserById( Long id);

    /**
     * 用户变更密码
     * @param userId  用户ID
     * @param newPassword  新密码
     * @return ReturnDataUtil 更新标记 1成功，0失败
     */
    ReturnDataUtil updatPassword( OfficialUserForm officialForm );

    /**
     * 根据登录账号查询所有用户
     * @param loginCode
     * @return
     */
    OfficialUserBean findUserByLoginCode( String loginCode );

    ReturnDataUtil listPage( SearchOffUserForm userForm );

    ReturnDataUtil updateOffUser( OfficialUserBean userBean );
}
