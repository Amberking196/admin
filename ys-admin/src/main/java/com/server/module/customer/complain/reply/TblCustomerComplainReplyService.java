package com.server.module.customer.complain.reply;

import java.util.List;

import com.server.module.customer.complain.TblCustomerComplainBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-04 11:27:38
 */
public interface TblCustomerComplainReplyService {

	/**
	 * 故障申报回复列表查询
	 * @author why
	 * @date 2019年2月15日 下午3:11:21 
	 * @param condition
	 * @return
	 */
    public ReturnDataUtil listPage(TblCustomerComplainReplyCondition condition);

    /**
     * 修改故障申报列表
     * @author why
     * @date 2019年2月15日 下午3:11:36 
     * @param entity
     * @return
     */
    public boolean update(TblCustomerComplainReplyBean entity);

    /**
     * 删除故障申报列表
     * @author why
     * @date 2019年2月15日 下午3:11:45 
     * @param id
     * @return
     */
    public boolean del(Object id);

    /**
     * 查询故障申报回复信息
     * @author why
     * @date 2019年2月15日 下午3:13:36 
     * @param id
     * @return
     */
    public TblCustomerComplainReplyBean get(Object id);

    /**
     * 增加故障申报回复信息
     * @author why
     * @date 2019年2月15日 下午3:13:49 
     * @param entity
     * @return
     */
    public TblCustomerComplainReplyBean add(TblCustomerComplainReplyBean entity);

    /**
     * 故障申诉回复列表
     * @author why
     * @date 2019年2月15日 下午3:12:19 
     * @param id
     * @return
     */
    public TblCustomerComplainBean getComplain(Integer id);
    
    /**
     * 客户回复列表
     * @author why
     * @date 2019年2月15日 下午3:12:33 
     * @param complainId
     * @return
     */
    public List<TblCustomerComplainReplyBean> listAllCustomerReply(Integer complainId);
    
    /**
     * 根据故障申报id 查询回复信息
     * @author why
     * @date 2018年12月6日 上午9:19:35 
     * @param complainId
     * @return
     */
    public List<TblCustomerComplainReplyBean> listReplyBean(Long complainId);

}

