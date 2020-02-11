package com.server.module.system.statisticsManage.chart;
import lombok.Data;
@Data
public class DateCountVo {
  private int day;
  private int count;
  private Integer companyId;
  private Integer areaId;
  private int orderNum;//用于排序
  private int time;//用于作比较
  private String code;//通用类型，用于筛选重复的记录
}
