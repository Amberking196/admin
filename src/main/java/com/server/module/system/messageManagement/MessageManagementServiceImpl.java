package com.server.module.system.messageManagement;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.machineManage.machineList.MachinesInfoAndBaseDto;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoCondition;
import com.server.module.system.machineManage.machineType.MachinesTypeBean;
import com.server.redis.RedisClient;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
@Service
public class MessageManagementServiceImpl implements MessageManagementService {
	public static Logger log = LogManager.getLogger(MessageManagementController.class);
	@Autowired
	private MessageManagementDao MessageManagementDaoImpl;
	@Autowired
    private RedisClient redisClient;//redis 操作客户端
	@Override
	public ReturnDataUtil messageListPage(MessageManagementForm form) {
		log.info("<MessageManagementServiceImpl>------<messageListPage>----start");
		ReturnDataUtil data=MessageManagementDaoImpl.messageListPage(form);
		log.info("<MessageManagementServiceImpl>------<messageListPage>----end");
		return data;
	}
	 /**
     *留言 售货机列表 查询
     */
    public ReturnDataUtil listPage(VendingMachinesInfoCondition condition) {
    	log.info("<MessageManagementServiceImpl>------<listPage>----start");
        ReturnDataUtil data = MessageManagementDaoImpl.listPage(condition);
        @SuppressWarnings("unchecked")
        List<MachinesInfoAndBaseDto> list = (List<MachinesInfoAndBaseDto>) data.getReturnObject();
        // 逐个查询机器类型名称 通常走缓存速度快
//        for (MachinesInfoAndBaseDto machinesInfoAndBaseDto : list) {
//            Integer typeId = machinesInfoAndBaseDto.getMachinesTypeId();
//            MachinesTypeBean typeBean = MessageManagementDaoImpl.get(typeId);
//            if (typeBean != null) {
//                machinesInfoAndBaseDto.setMachinesTypeName(typeBean.getName());
//                if (StringUtil.isNotBlank(redisClient.get("enterAli-" + machinesInfoAndBaseDto.getCode()))) {
//                    machinesInfoAndBaseDto.setCanEnter(false);
//                } else {
//                    machinesInfoAndBaseDto.setCanEnter(true);
//                }
//            }
//
//        }
        log.info("<MessageManagementServiceImpl>------<listPage>----end");
        return data;
    }
	@Override
	//评论查询
	public ReturnDataUtil messageCommentListPage(MessageCommentForm form) {
		log.info("<MessageManagementServiceImpl>------<messageCommentListPage>----start");
		ReturnDataUtil data=MessageManagementDaoImpl.messageCommentListPage(form);
		log.info("<MessageManagementServiceImpl>------<messageCommentListPage>----end");
		return data;
	}

}
