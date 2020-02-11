package com.server.module.customer.product.goodsBargain;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table name:  goods_bargain
 * author name: yjr
 * create time: 2018-12-21 10:31:41
 */
@Data
@Entity(tableName = "goods_bargain", id = "id", idGenerate = "auto")
public class GoodsBargainBean {


  //  @JsonIgnore
   // public String tableName = "goods_bargain";
   // @JsonIgnore
   // public String selectSql = "select * from goods_bargain where 1=1 ";
   // @JsonIgnore
  //  public String selectSql1 = "select id,goodsId,oneBargainPrice,lowestPrice,bargainCount,startTime,endTime,hourLimit,createTime,createUser,createUserName,updateTime,updateUser,state,deleteFlag from goods_bargain where 1=1 ";
    private Long id;
    private Long goodsId;
    private BigDecimal oneBargainPrice;//砍一刀价格
    private BigDecimal lowestPrice;//砍后最低价格
    private Long bargainCount;//需砍次数
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private Long hourLimit;//x小时要完成全部砍价
    private Long goodsLimit;//
    private Date createTime;//
    private Long createUser;//
    private String createUserName;//
    private Date updateTime;//
    private Long updateUser;//
    private Integer state;//状态 0 正常  1 停用
   // @NotField
   // private String stateLabel;
    private Integer deleteFlag;

   /* public String getStateLabel() {
        if(state==0){
          return "正常";
        }else{
          return "停用";
        }
     }*/
}

