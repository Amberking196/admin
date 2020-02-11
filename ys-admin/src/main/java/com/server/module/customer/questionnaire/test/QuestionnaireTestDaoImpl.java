package com.server.module.customer.questionnaire.test;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.customer.questionnaire.AnswerItem;
import com.server.module.customer.questionnaire.Option;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import java.sql.*;

import com.server.common.persistence.BaseDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author name: yjr
 * create time: 2019-01-25 17:02:19
 */
@Repository
public class QuestionnaireTestDaoImpl extends BaseDao<QuestionnaireTestBean> implements QuestionnaireTestDao {

    private static Log log = LogFactory.getLog(QuestionnaireTestDaoImpl.class);
    
    @Autowired
    private CompanyDao companyDaoImpl;

    public ReturnDataUtil listPage(QuestionnaireTestCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select qt.id,qt.title,qt.remark,qt.types,qt.state,qt.smsContent,qt.createTime,qt.createUserId,qt.updateUserId,qt.updateTime,qt.companyId,c.name from questionnaire_test  qt ");
        sql.append(" left join company c on qt.companyId=c.id where 1=1 ");
        sql.append(" and qt.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" ");
        List<Object> plist = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("问卷调查管理SQL"+sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(super.countSql(sql.toString()));
            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());

            if (plist != null && plist.size() > 0)
                for (int i = 0; i < plist.size(); i++) {
                    pst.setObject(i + 1, plist.get(i));
                }
            rs = pst.executeQuery();
            List<QuestionnaireTestBean> list = Lists.newArrayList();
            while (rs.next()) {
                QuestionnaireTestBean bean = new QuestionnaireTestBean();
                bean.setId(rs.getLong("id"));
                bean.setTitle(rs.getString("title"));
                bean.setRemark(rs.getString("remark"));
                bean.setTypes(rs.getString("types"));
                bean.setState(rs.getInt("state"));
                bean.setSmsContent(rs.getString("smsContent"));
                bean.setCreateTime(rs.getDate("createTime"));
                bean.setCreateUserId(rs.getLong("createUserId"));
                bean.setUpdateUserId(rs.getLong("updateUserId"));
                bean.setUpdateTime(rs.getDate("updateTime"));
                bean.setCompanyId(rs.getLong("companyId"));
                bean.setCompanyName(rs.getString("name"));
                list.add(bean);
            }
            data.setCurrentPage(condition.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return data;
        } finally {
           this.closeConnection(rs, pst, conn);
        }
    }

    public QuestionnaireTestBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Object id) {
        QuestionnaireTestBean entity = new QuestionnaireTestBean();
        return super.del(entity);
    }

    public boolean update(QuestionnaireTestBean entity) {
        return super.update(entity);
    }

    public QuestionnaireTestBean insert(QuestionnaireTestBean entity) {
        return super.insert(entity);
    }

    public List<QuestionnaireTestBean> list(QuestionnaireTestCondition condition) {
        return null;
    }

    public void addTopics(Long testId, Long[] topics){
        String delSql="delete from questionnaire_test_topic where testId="+testId;
        String sql="insert into questionnaire_test_topic(testId,topicId) values(?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Option> testList = new ArrayList<Option>();
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            ps=conn.prepareStatement(delSql);
            ps.executeUpdate();
            ///
            ps = conn.prepareStatement(sql);
            for (Long topic : topics) {
                ps.setLong(1,testId);
                ps.setLong(2,topic);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            this.closeConnection(rs, ps, conn);
        }

    }
}

