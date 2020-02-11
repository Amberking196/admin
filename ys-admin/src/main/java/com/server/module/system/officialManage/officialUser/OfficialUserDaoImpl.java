package com.server.module.system.officialManage.officialUser;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.util.ReturnDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "OfficialUserDaoImpl")
public class OfficialUserDaoImpl extends BaseDao<OfficialUserBean> implements OfficialUserDao {

    public static Logger log = LogManager.getLogger(OfficialUserDaoImpl.class);

    /**
     * 查找所有用户名
     * @return
     */
    @Override
    public List<String> findAllLoginCode() {
        log.info("<OfficialUserDaoImpl>--<findAllLoginCode>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" select loginCode from official_user");
        log.info("sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> loginCodeList = new ArrayList<String>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                loginCodeList.add(rs.getString("loginCode"));
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<OfficialUserDaoImpl>--<findAllLoginCode>--end");
        return loginCodeList;
    }

    /**
     *
     * @param entity
     * @return
     * 添加用户
     */
    @Override
    public OfficialUserBean createOfficialUser( OfficialUserBean entity ) {
        log.info("<OfficialUserDaoImpl>--<createOfficialUser>--start");
        OfficialUserBean bean = super.insert(entity);
        log.info("<OfficialUserDaoImpl>--<createOfficialUser>--end");
        return bean;
    }

    /**
     * 登录时查询
     * @param loginCode 登录名字 /手机号码
     * @param password  密码
     * @return
     */
    @Override
    public OfficialUserBean officialUserLogin( String loginCode, String password ) {
        log.info("<OfficialUserDaoImpl>--<officialUserLogin>--start");
        String sql = "select id,loginCode,name,role,companyId "
                + " from official_user "
                + " where"
                + " loginCode='" + loginCode + "'"
                + " and password='" + password + "' and status = 1 ";
        log.info("<OfficialUserDaoImpl>--<officialUserLogin>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OfficialUserBean loginUser = new OfficialUserBean();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                loginUser.setId(rs.getLong("id"));
                loginUser.setLoginCode(rs.getString("loginCode"));
                loginUser.setRole(rs.getInt("role"));
                loginUser.setName(rs.getString("name"));
                loginUser.setCompanyId(rs.getInt("companyId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<OfficialUserDaoImpl>--<officialUserLogin>--end");
        return loginUser;
    }

    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    @Override
    public OfficialUserBean findUserById( Long id ) {
        log.info("<OfficialUserDaoImpl>--<findUserById>--start");
        String sql = "select id,loginCode,name,status,role,companyId,password"
                + " from official_user "
                + " where "
                + " id=" + id;
        log.info("<OfficialUserDaoImpl>--<findUserById>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OfficialUserBean official = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                official = new OfficialUserBean();
                official.setLoginCode(rs.getString("loginCode"));
                official.setId(rs.getLong("id"));
                official.setName(rs.getString("name"));
                official.setStatus(rs.getInt("status"));
                official.setRole(rs.getInt("role"));
                official.setCompanyId(rs.getInt("companyId"));
                official.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<OfficialUserDaoImpl>--<findUserById>--end");
        return official;
    }

    /**
     * 修改密码
     * @param userId  用户ID
     * @param newPassword  新密码
     * @return
     */
    @Override
    public int updatPassword( Long userId, String newPassword ) {
        log.info("<OfficialUserDaoImpl>--<updatPassword>--start");
        String sql = "update official_user set password='" + newPassword + "'"
                + " where id=" + userId;
        log.info("<OfficialUserDaoImpl>--<updatPassword>--addsql:" + sql);
        int updateFlag = upate(sql);
        log.info("<OfficialUserDaoImpl>--<updatPassword>--end");
        return updateFlag;
    }

    /**
     * 根据用户账号查询用户
     * @param loginCode
     * @return
     */
    @Override
    public OfficialUserBean findUserByLoginCode( String loginCode ) {
        String sql = "select id,loginCode,name,status,companyId"
                + " from official_user "
                + " where "
                + " loginCode=" + loginCode;
        log.info("<OfficialUserDaoImpl>--<findUserById>--sql:" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OfficialUserBean official = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                official = new OfficialUserBean();
                official.setLoginCode(rs.getString("loginCode"));
                official.setId(rs.getLong("id"));
                official.setName(rs.getString("name"));
                official.setStatus(rs.getInt("status"));
                official.setCompanyId(rs.getInt("companyId"));
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        log.info("<OfficialUserDaoImpl>--<findUserById>--end");
        return official;
    }

    /**
     * 查询当前所有用户
     * @param userForm
     * @return
     */
    @Override
    public ReturnDataUtil listPage( SearchOffUserForm userForm ) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuffer sql = new StringBuffer();
        sql.append("select id,name,loginCode,status,password,role,companyId from official_user order by id desc");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("sql语句："+sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (userForm.getCurrentPage() - 1) * userForm.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + userForm.getPageSize());

            rs = pst.executeQuery();
            List<OfficialUserBean> list = Lists.newArrayList();
            while (rs.next()) {
                OfficialUserBean bean = new OfficialUserBean();
                bean.setId(rs.getLong("id"));
                bean.setLoginCode(rs.getString("loginCode"));
                bean.setName(rs.getString("name"));
                bean.setPassword(rs.getString("password"));
                bean.setStatus(rs.getInt("status"));
                bean.setRole(rs.getInt("role"));
                bean.setCompanyId(rs.getInt("companyId"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
            }
            data.setCurrentPage(userForm.getCurrentPage());
            data.setPageSize(userForm.getPageSize());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return data;
        } finally {
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean updateOffUser( OfficialUserBean userBean ) {
        log.info("<OfficialUserDaoImpl>--<updateOffUser>--start");
        StringBuffer sql = new StringBuffer();
        sql.append(" update official_user set password =?,status=?,role=? where id =?");
        List<Object> param = new ArrayList<Object>();
        param.add(userBean.getPassword());
        param.add(userBean.getRole());
        param.add(userBean.getStatus());
        param.add(userBean.getId());
        int result = upate(sql.toString(), param);
        log.info("<OfficialUserDaoImpl>--<updateOffUser>--end");
        if (result != 0) {
            return true;
        }
        return false;
    }
}