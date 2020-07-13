package com.server.module.system.baseManager.china;

import com.server.common.persistence.Entity;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name:  china
 * author name: yjr
 * create time: 2018-12-10 09:02:14
 */
@Data
@Entity(tableName = "china", id = "Id", idGenerate = "auto")
public class ChinaBean {


   // @JsonIgnore
   // public String tableName = "china";
   // @JsonIgnore
   // public String selectSql = "select * from china where 1=1 ";
    //@JsonIgnore
   // public String selectSql1 = "select Id,Name,Pid from china where 1=1 ";
    private Long Id;
    private String Name;
    private Long Pid;

}

