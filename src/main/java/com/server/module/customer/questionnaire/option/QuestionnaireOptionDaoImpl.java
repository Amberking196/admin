package com.server.module.customer.questionnaire.option;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.customer.questionnaire.Option;
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
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: yjr
 * create time: 2019-01-25 16:04:27
 */
@Repository
public class QuestionnaireOptionDaoImpl extends BaseDao<QuestionnaireOptionBean> implements QuestionnaireOptionDao {

    private static Log log = LogFactory.getLog(QuestionnaireOptionDaoImpl.class);

    public ReturnDataUtil listPage(QuestionnaireOptionCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("select id,content,title,topicId from questionnaire_option where 1=1 ");
        List<Object> plist = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
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
            List<QuestionnaireOptionBean> list = Lists.newArrayList();
            while (rs.next()) {
                QuestionnaireOptionBean bean = new QuestionnaireOptionBean();
                bean.setId(rs.getLong("id"));
                bean.setContent(rs.getString("content"));
                bean.setTitle(rs.getString("title"));
                bean.setTopicId(rs.getLong("topicId"));
                list.add(bean);
            }
            if (showSql) {
                log.info(sql);
                log.info(plist.toString());
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
            try {
                rs.close();
                pst.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public QuestionnaireOptionBean get(Object id) {
        return super.get(id);
    }

    public boolean delete(Long id) {
        QuestionnaireOptionBean entity = new QuestionnaireOptionBean();
        entity.setId((Long)id);
        return super.del(entity);
    }

    public boolean update(QuestionnaireOptionBean entity) {
        return super.update(entity);
    }

    public QuestionnaireOptionBean insert(QuestionnaireOptionBean entity) {
        return super.insert(entity);
    }

    public List<QuestionnaireOptionBean> list(QuestionnaireOptionCondition condition) {
        return null;
    }


    public List<QuestionnaireOptionBean> listOptionsByTopicId(Long topicId){
        String sql="select id,content,title,topicId from questionnaire_option where topicId="+topicId;
        List<QuestionnaireOptionBean> list = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString() );
            rs = pst.executeQuery();
            while (rs.next()) {
                QuestionnaireOptionBean bean = new QuestionnaireOptionBean();
                bean.setId(rs.getLong("id"));
                bean.setContent(rs.getString("content"));
                bean.setTitle(rs.getString("title"));
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

