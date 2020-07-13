package com.server.module.system.machineManage.machineReplenish;

import com.server.util.ReturnDataUtil;

public interface MachineReplenishDao {


    /**
     * 机器补货盘点
     *
     * @param machineReplenishForm
     * @return ReturnDataUtil
     */
    ReturnDataUtil findMachineReplenishSum(MachineReplenishForm machineReplenishForm);


    /**
     * 机器补货详情
     *
     * @param machineReplenishForm
     * @return ReturnDataUtil
     */
    ReturnDataUtil findMachineReplenishDetile(MachineReplenishForm machineReplenishForm);

    /**
     * 将数据全部输出到Excel中
     * @param machineReplenishForm
     * @return
     */
    ReturnDataUtil exportExcel(MachineReplenishForm machineReplenishForm);

}
