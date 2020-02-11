package com.server.module.customer.product.goodsBargain;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-21 10:31:41
 */
@Service
public class GoodsBargainServiceImpl implements GoodsBargainService {

    private static Logger log = LogManager.getLogger(GoodsBargainServiceImpl.class);
    @Autowired
    private GoodsBargainDao goodsBargainDaoImpl;

    public ReturnDataUtil listPage(GoodsBargainCondition condition) {
    	log.info("<GoodsBargainServiceImpl>-----<listPage>------start");
    	ReturnDataUtil listPage = goodsBargainDaoImpl.listPage(condition);
    	log.info("<GoodsBargainServiceImpl>-----<listPage>------end");
        return listPage;
    }

    public GoodsBargainBean add(GoodsBargainBean entity) {
    	log.info("<GoodsBargainServiceImpl>-----<add>------start");
    	GoodsBargainBean bean = goodsBargainDaoImpl.insert(entity);
    	log.info("<GoodsBargainServiceImpl>-----<add>------end");
        return bean;
    }

    public boolean update(GoodsBargainBean entity) {
    	log.info("<GoodsBargainServiceImpl>-----<update>------start");
    	boolean update = goodsBargainDaoImpl.update(entity);
    	log.info("<GoodsBargainServiceImpl>-----<update>------end");
        return update;
    }

    public boolean del(Object id) {
    	log.info("<GoodsBargainServiceImpl>-----<del>------start");
    	boolean delete = goodsBargainDaoImpl.delete(id);
    	log.info("<GoodsBargainServiceImpl>-----<del>------end");
        return delete;
    }

    public List<GoodsBargainBean> list(GoodsBargainCondition condition) {
        return null;
    }

    public GoodsBargainBean get(Object id) {
    	log.info("<GoodsBargainServiceImpl>-----<get>------start");
    	GoodsBargainBean bean = goodsBargainDaoImpl.get(id);
    	log.info("<GoodsBargainServiceImpl>-----<get>------end");
        return bean;
    }
}

