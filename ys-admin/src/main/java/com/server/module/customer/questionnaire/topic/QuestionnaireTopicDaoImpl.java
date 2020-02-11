package com.server.module.customer.questionnaire.topic;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.server.common.persistence.BaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: yjr
 * create time: 2019-01-25 16:01:45
 */
@Repository
public class QuestionnaireTopicDaoImpl extends BaseDao<QuestionnaireTopicBean> implements QuestionnaireTopicDao {

    private static Log log = LogFactory.getLog(QuestionnaireTopicDaoImpl.class);
    
    @Autowired
    private CompanyDao companyDaoImpl;

    public ReturnDataUtil listPage(QuestionnaireTopicCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append(" select id,question,oneAnswer,companyId from questionnaire_topic where 1=1 ");
        sql.append(" and companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" ");
        List<Object> plist = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("问题库管理 SQL"+sql.toString());
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
            List<QuestionnaireTopicBean> list = Lists.newArrayList();
            while (rs.next()) {
                QuestionnaireTopicBean bean = new QuestionnaireTopicBean();
                bean.setId(rs.getLong("id"));
                bean.setQuestion(rs.getString("question"));
               // bean.setOrder(rs.getInt("order"));
                bean.setOneAnswer(rs.getInt("oneAnswer"));
                bean.setCompanyId(rs.getLong("companyId"));
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

    public QuestionnaireTopicBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Long id) {
        QuestionnaireTopicBean entity = new QuestionnaireTopicBean();
        entity.setId((Long)id);
        return super.del(entity);
    }

    public boolean update(QuestionnaireTopicBean entity) {
        return super.update(entity);
    }

    public QuestionnaireTopicBean insert(QuestionnaireTopicBean entity) {
        return super.insert(entity);
    }

    public List<QuestionnaireTopicBean> list(QuestionnaireTopicCondition condition) {
        return null;
    }

    @Override
    public List<TopicVO> listTopic(QuestionnaireTopicCondition questionnaireTopicCondition) {
        List<TopicVO> list=Lists.newArrayList();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT t.id,t.question,t.oneAnswer,tt.topicId FROM questionnaire_topic t LEFT JOIN ");
        if(questionnaireTopicCondition.getTestId()!=null) {
        	 sql.append(" (SELECT * FROM questionnaire_test_topic  WHERE testId="+questionnaireTopicCondition.getTestId()+") tt ON t.id=tt.topicId WHERE 1=1 ");
        }
        if(questionnaireTopicCondition.getCompanyId()!=null) {
        	sql.append(" and t.companyId = '"+questionnaireTopicCondition.getCompanyId()+"' ");
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                TopicVO bean = new TopicVO();
                bean.setId(rs.getLong("id"));
                bean.setQuestion(rs.getString("question"));
                bean.setOneAnswer(rs.getInt("oneAnswer"));
                bean.setTopicId(rs.getLong("topicId"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return list;
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
}

