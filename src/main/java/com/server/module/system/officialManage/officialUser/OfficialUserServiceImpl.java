package com.server.module.system.officialManage.officialUser;

import com.server.util.MD5Utils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "OfficialUserService")
public class OfficialUserServiceImpl implements OfficialUserService{
    public static Logger log = LogManager.getLogger( OfficialUserServiceImpl.class);
    @Autowired
    private OfficialUserDao officialUserDao;
    @Autowired
    private MD5Utils md;
    @Autowired
    private ReturnDataUtil returnDataUtil;


    @Override
    public boolean loginCodeIsOnlyOne( String loginCode ) {
        log.info("<OfficialUserServiceImpl>--<loginCodeIsOnlyOne>--start");
        List<String> loginCodeList = officialUserDao.findAllLoginCode();
        for (String code : loginCodeList) {
            if(loginCode.equals(code)){
                return false;
            }
        }
        return true;
    }

    @Override
    public OfficialUserBean createOfficialUser( OfficialUserBean officialUser ) {
        log.info("<OfficialUserServiceImpl>--<createOfficialUser>--start");
        officialUser.setPassword(md.getMd5(officialUser.getPassword()));
        OfficialUserBean bean = officialUserDao.createOfficialUser(officialUser);
        /*if(officialUser!=null && officialUserDao.createOfficialUser(officialUser)){
            returnData.setStatus(1);
            returnData.setMessage("注册成功");
        }else{
            returnData.setStatus(0);
            returnData.setMessage("注册失败");
        }*/
        log.info("<OfficialUserServiceImpl>--<createOfficialUser>--end");
        return bean;
    }

    @Override
    public ReturnDataUtil officialUserLogin( String loginCode, String password ) {
        log.info("<OfficialUserServiceImpl>--<officialUserLogin>--start");
        password = md.getMd5(password);
        OfficialUserBean loginUser = officialUserDao.officialUserLogin(loginCode, password);

        if (loginUser != null && loginUser.getId() != null) {
            returnDataUtil.setMessage("用户登录成功");
            returnDataUtil.setStatus(1);
            returnDataUtil.setReturnObject(loginUser);
        } else {
            returnDataUtil.setMessage("帐号密码错误");
            returnDataUtil.setStatus(0);
        }
        log.info("<OfficialUserServiceImpl>--<officialUserLogin>--end");
        return returnDataUtil;
    }

    @Override
    public ReturnDataUtil findUserById( Long id ) {
        log.info("<OfficialUserServiceImpl>--<findUserById>--start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        if(id!=null){
            OfficialUserBean userBean = officialUserDao.findUserById(id);
            if(userBean!=null){
                returnData.setStatus(1);
                returnData.setMessage("更新成功");
                returnData.setReturnObject(userBean);
            }else{
                returnData.setStatus(0);
                returnData.setMessage("更新失败");
            }
        }else{
            returnData.setStatus(0);
            returnData.setMessage("参数为空");
        }
        log.info("<OfficialUserServiceImpl>--<findUserById>--end");
        return returnData;
    }

    @Override
    public ReturnDataUtil updatPassword( OfficialUserForm officialForm ) {
        log.info("<OfficialUserServiceImpl>--<updatPassword>--start");
        String password = md.getMd5(officialForm.getNewPassword());
        int re = officialUserDao.updatPassword(officialForm.getId(), password);
        if (re == 1) {
            returnDataUtil.setMessage("修改密码成功");
            returnDataUtil.setStatus(1);
            returnDataUtil.setReturnObject(1);
        } else {
            returnDataUtil.setMessage("修改密码失败");
            returnDataUtil.setStatus(0);
            returnDataUtil.setReturnObject(0);
        }
        log.info("<OfficialUserServiceImpl>--<updatPassword>--end");
        return returnDataUtil;
    }

    /**
     * 根据用户账号查询用户
     * @param loginCode
     * @return
     */
    @Override
    public OfficialUserBean findUserByLoginCode( String loginCode ) {
        log.info("<OfficialUserServiceImpl>--<findUserByLoginCode>--start");
        OfficialUserBean user = officialUserDao.findUserByLoginCode(loginCode);
        log.info("<OfficialUserServiceImpl>--<findUserByLoginCode>--end");
        return user;
    }

    @Override
    public ReturnDataUtil listPage( SearchOffUserForm userForm ) {
        return officialUserDao.listPage(userForm);
    }

    @Override
    public ReturnDataUtil updateOffUser( OfficialUserBean userBean ) {
        log.info("<OfficialUserServiceImpl>--<updateOffUser>--start");
        ReturnDataUtil returnData = new ReturnDataUtil();
        if(StringUtil.isNotBlank(userBean.getPassword())){
            userBean.setPassword(md.getMd5(userBean.getPassword()));
        }
        if(userBean != null && officialUserDao.updateOffUser(userBean)){
            returnData.setStatus(1);
            returnData.setMessage("更新成功");
        }else{
            returnData.setStatus(0);
            returnData.setMessage("更新失败");
        }
        log.info("<OfficialUserServiceImpl>--<updateOffUser>--end");
        return returnData;
    }
}
