package com.server.module.customer.userInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TblCustomerServiceImpl implements TblCustomerService{
    private static Logger log = LogManager.getLogger(TblCustomerServiceImpl.class);
    @Autowired
    private TblCustomerDao tblCustomerDaoImpl;


    public TblCustomerBean getCustomerById(Long customerId) {
        log.info("<TblCustomerServiceImpl--getCustomerById--start>");
        TblCustomerBean tblCustomerBean = tblCustomerDaoImpl.getCustomerById(customerId);
        log.info("<TblCustomerServiceImpl--getCustomerById--end>");
        return tblCustomerBean;
    }
}
