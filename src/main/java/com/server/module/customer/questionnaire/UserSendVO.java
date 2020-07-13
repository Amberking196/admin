package com.server.module.customer.questionnaire;

import lombok.Data;

@Data
public class UserSendVO {

  private Integer id;
  private String phone;
  private String vmCode;
  private Integer logId;

}
