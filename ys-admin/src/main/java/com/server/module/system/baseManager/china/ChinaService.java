package com.server.module.system.baseManager.china;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-10 09:02:14
 */
public interface ChinaService {


    public ReturnDataUtil listPage(ChinaCondition condition);

    public List<ChinaBean> list(ChinaCondition condition);

    public boolean update(ChinaBean entity);

    public boolean del(Object id);

    public ChinaBean get(Object id);

    public ChinaBean add(ChinaBean entity);
	/**
     * 区列表查询
     * @return List<ChinaBean>
     */
    public List<ChinaBean> areaList(Long pid);
	/**
     * 市列表  广州市/佛山市/武汉市/珠海市
     * @return List<ChinaBean>
     */
    public List<ChinaBean> cityList();
	/**
     * 市列表查询
     * @return List<ChinaBean>
     */
    public List<ChinaBean> cityList(Long pid);

    public List<ChinaBean> provinceList();

}

