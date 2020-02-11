package com.server.module.system.baseManager.china;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-10 09:02:14
 */
public interface ChinaDao {

    public ReturnDataUtil listPage(ChinaCondition condition);

    public List<ChinaBean> list(ChinaCondition condition);

    public boolean update(ChinaBean entity);

    public boolean delete(Object id);

    public ChinaBean get(Object id);
    public List<ChinaBean> cityList();
    public List<ChinaBean> areaList(Long pid);
    public ChinaBean insert(ChinaBean entity);
    public List<ChinaBean> list();
}

