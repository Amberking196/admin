package com.server.module.customer.complain.reply;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-04 11:27:38
 */
public interface TblCustomerComplainReplyDao {

	/**
	 * 查询故障申报回复列表
	 * @author why
	 * @date 2019年2月15日 下午3:03:32 
	 * @param condition
	 * @return
	 */
    public ReturnDataUtil listPage(TblCustomerComplainReplyCondition condition);

    /**
     * 查询故障申报回复信息
     * @author why
     * @date 2019年2月15日 下午3:04:13 
     * @param complainId
     * @param pid
     * @return
     */
    public List<TblCustomerComplainReplyBean> list(Integer complainId,Integer pid);

    /**
     * 修改故障申报回复信息
     * @author why
     * @date 2019年2月15日 下午3:04:29 
     * @param entity
     * @return
     */
    public boolean update(TblCustomerComplainReplyBean entity);

    /**
     * 删除故障申报回复信息
     * @author why
     * @date 2019年2月15日 下午3:07:24 
     * @param id
     * @return
     */
    public boolean delete(Object id);

    /**
     * 查询故障申报回复信息
     * @author why
     * @date 2019年2月15日 下午3:07:37 
     * @param id
     * @return
     */
    public TblCustomerComplainReplyBean get(Object id);

    /**
     * 增加故障申报回复信息
     * @author why
     * @date 2019年2月15日 下午3:08:06 
     * @param entity
     * @return
     */
    public TblCustomerComplainReplyBean insert(TblCustomerComplainReplyBean entity);
    
    /**
     * 根据故障申报id 查询回复信息
     * @author why
     * @date 2018年12月6日 上午9:16:15 
     * @param complainId
     * @return
     */
    public List<TblCustomerComplainReplyBean> listReplyBean(Long complainId);
}

