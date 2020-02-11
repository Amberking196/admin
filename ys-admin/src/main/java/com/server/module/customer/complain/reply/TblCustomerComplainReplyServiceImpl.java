package com.server.module.customer.complain.reply;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.customer.complain.TblCustomerComplainBean;
import com.server.module.customer.complain.TblCustomerComplainService;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-04 11:27:38
 */
@Service
public class TblCustomerComplainReplyServiceImpl implements TblCustomerComplainReplyService {

    private static Logger log = LogManager.getLogger(TblCustomerComplainReplyServiceImpl.class);
    @Autowired
    private TblCustomerComplainReplyDao tblCustomerComplainReplyDaoImpl;
    @Autowired
    private TblCustomerComplainService customerComplainService;
    public ReturnDataUtil listPage(TblCustomerComplainReplyCondition condition) {
        return tblCustomerComplainReplyDaoImpl.listPage(condition);
    }

    public TblCustomerComplainReplyBean add(TblCustomerComplainReplyBean entity) {
        return tblCustomerComplainReplyDaoImpl.insert(entity);
    }

    public boolean update(TblCustomerComplainReplyBean entity) {
        return tblCustomerComplainReplyDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return tblCustomerComplainReplyDaoImpl.delete(id);
    }

    public TblCustomerComplainReplyBean get(Object id) {
        return tblCustomerComplainReplyDaoImpl.get(id);
    }

    public TblCustomerComplainBean getComplain(Integer id){
            TblCustomerComplainBean complainBean = customerComplainService.get(id);
            List<TblCustomerComplainReplyBean> list=tblCustomerComplainReplyDaoImpl.list(id,null);
            complainBean.getListReply().addAll(list);
            return complainBean;
    }

    public List<TblCustomerComplainReplyBean> listAllCustomerReply(Integer complainId){
        List<TblCustomerComplainReplyBean> list=tblCustomerComplainReplyDaoImpl.list(complainId,null);
        return list;
    }
    
    public List<TblCustomerComplainReplyBean> listReplyBean(Long complainId) {
        log.info("<TblCustomerComplainReplyServiceImpl>------<listReplyBean>-----start");
        List<TblCustomerComplainReplyBean> list = tblCustomerComplainReplyDaoImpl.listReplyBean(complainId);
        log.info("<TblCustomerComplainReplyServiceImpl>------<listReplyBean>-----end");
        return list;
    }
}

