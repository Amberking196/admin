package com.server.module.system.adminUser;

import com.server.common.persistence.Entity;
import lombok.Data;

@Data
@Entity(tableName = "login_info_machine", id = "id", idGenerate = "auto")

public class AdminMachine {
    //机器编号
    private String vmCode;

    //自增ID
    private int id;

    //货道号
    private int way;

    //员工ID
    private int userId;
}
