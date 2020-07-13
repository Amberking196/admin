package com.server.module.customer.questionnaire;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.common.persistence.BaseDao;
import com.server.module.customer.order.OrderBean;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.warehouseManage.checkLog.WarehouseCheckLogBean;
import com.server.module.system.warehouseManage.checkLog.WarehouseCheckLogForm;
import com.server.util.ReturnDataUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionnaireDao extends BaseDao<OrderBean> {

    public Test getTest(Integer type){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  * from questionnaire_test where 1=1");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
       // List<Test> testList = new ArrayList<Test>();
        Test test = new Test();

        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while(rs.next()){
               test.setId(rs.getInt("id"));
                test.setRemark(rs.getString("remark"));
                test.setTitle(rs.getString("title"));
                test.setState(rs.getInt("state"));
                return test;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        return null;
        //return testList.get;
    }

    public Test getTestById(Integer id){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  * from questionnaire_test where id="+id);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // List<Test> testList = new ArrayList<Test>();
        Test test = new Test();

        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                test.setId(rs.getInt("id"));
                test.setRemark(rs.getString("remark"));
                test.setTitle(rs.getString("title"));
                test.setState(rs.getInt("state"));
                return test;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        return null;
        //return testList.get;
    }

    public List<Topic> getAllTopic(Integer testId){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  t.* from questionnaire_topic t,questionnaire_test_topic q where t.id=q.topicId and q.testId="+testId+" ");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Topic> topicList = new ArrayList<Topic>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                Topic topic = new Topic();
                topic.setId(rs.getInt("id"));
                topic.setOneAnswer(rs.getInt("oneAnswer"));
                topic.setQuestion(rs.getString("question"));
                topic.setTestId(testId);
                //topic.setOrder(rs.getInt("order"));
                topicList.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        return topicList;
    }

    public List<Option> getAllOption(Integer topicId){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT  * from questionnaire_option where topicId="+topicId);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Option> testList = new ArrayList<Option>();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                Option option = new Option();
                option.setId(rs.getInt("id"));
                option.setContent(rs.getString("content"));
                option.setTitle(rs.getString("title"));
                option.setTopicId(rs.getInt("topicId"));
                testList.add(option);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        return testList;
    }
    public Map<Integer,Integer> getOptionAndAnswer(Integer testId){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT i.optionId,COUNT(id) AS answerSum FROM questionnaire_answer_item i WHERE i.testId="+testId+" GROUP BY i.optionId");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer,Integer> map= Maps.newHashMap();
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                map.put(rs.getInt("optionId"),rs.getInt("answerSum"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
        return map;
    }
    public void saveAnswer(Answer answer){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Option> testList = new ArrayList<Option>();
        try {
            conn = openConnection();
            String sqlAnswer="insert into questionnaire_answer(userId,testId,suggest) values(?,?,?)";
            ps = conn.prepareStatement(sqlAnswer, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,answer.getUserId());
            ps.setInt(2, answer.getTestId());
            ps.setString(3,answer.getSuggest());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Integer id = rs.getInt(1);
                answer.setId(id);
                System.out.println("数据主键：" + id);
            }
            List<AnswerItem> listItem=answer.getItemList();
            String sqlItem="insert into questionnaire_answer_item(topicId,optionId,answerId,testId) values(?,?,?,?)";
            for (AnswerItem answerItem : listItem) {
                answerItem.setAnswerId(answer.getId());
                ps = conn.prepareStatement(sqlItem);
                ps.setInt(1, answerItem.getTopicId());
                ps.setInt(2, answerItem.getOptionId());
                ps.setInt(3,answerItem.getAnswerId());
                ps.setInt(4,answer.getTestId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection(rs, ps, conn);
        }
    }


    public void getUserList(){
        String sql="SELECT c.id,c.phone,c.vmCode,a.state FROM customer_analyze a,tbl_customer c WHERE a.customerId=c.id  AND c.phone IS NOT NULL ";




    }

    public ReturnDataUtil getUserPage(DashboardCondition condition) {
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.id,c.phone,c.vmCode,a.state,l.id AS logId FROM customer_analyze a INNER JOIN tbl_customer c ON a.customerId=c.id LEFT JOIN questionnaire_sendsms_log l ON c.id=l.customerId " +
                " WHERE c.phone IS NOT NULL");

        if(StringUtils.isNotEmpty(condition.getPhone())){
            sql.append(" and c.phone="+condition.getPhone());
        }else{
            if(StringUtils.isNotEmpty(condition.getVmCode())){
                sql.append(" and c.vmCode='"+condition.getVmCode()+"'");
            }
        }
        if(StringUtils.isNotEmpty(condition.getType()))
        sql.append(" and a.state="+condition.getType());
        if(condition.getSendFlag()==1){
            sql.append(" and l.id is not null ");
        }
        if(condition.getSendFlag()==2){
            sql.append(" and l.id is null ");
        }
        if (showSql) {
            log.info(sql);
        }
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            String sqlcount="select count(*) from ("+sql.toString()+") as c";
            System.out.println(sqlcount);
            pst = conn.prepareStatement(sqlcount);

            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());


            rs = pst.executeQuery();
            List<UserSendVO> list = Lists.newArrayList();

            while (rs.next()) {
                UserSendVO vo=new UserSendVO();
                vo.setId(rs.getInt("id"));
                vo.setLogId(rs.getInt("logId"));
                vo.setPhone(rs.getString("phone"));
                vo.setVmCode(rs.getString("vmCode"));
                list.add(vo);
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
}
