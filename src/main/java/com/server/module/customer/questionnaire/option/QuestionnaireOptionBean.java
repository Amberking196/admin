package com.server.module.customer.questionnaire.option;

import com.server.common.persistence.Entity;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name:  questionnaire_option
 * author name: yjr
 * create time: 2019-01-25 16:04:27
 */
@Data
@Entity(tableName = "questionnaire_option", id = "id", idGenerate = "auto")
public class QuestionnaireOptionBean {


    /*@JsonIgnore
    public String tableName = "questionnaire_option";
    @JsonIgnore
    public String selectSql = "select * from questionnaire_option where 1=1 ";
    @JsonIgnore
    public String selectSql1 = "select id,content,title,topicId from questionnaire_option where 1=1 ";
    */

    private Long id;
    private String content;
    private String title;
    private Long topicId;

}

