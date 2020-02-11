package com.server.module.system.baseManager.china;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-10 09:02:14
 */
@Service
public class ChinaServiceImpl implements ChinaService {

    private static Log log = LogFactory.getLog(ChinaServiceImpl.class);
    @Autowired
    private ChinaDao chinaDaoImpl;

    private List<ChinaBean> provinceList;

    public ReturnDataUtil listPage(ChinaCondition condition) {
        return chinaDaoImpl.listPage(condition);
    }

    public ChinaBean add(ChinaBean entity) {
        return chinaDaoImpl.insert(entity);
    }

    public boolean update(ChinaBean entity) {
        return chinaDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return chinaDaoImpl.delete(id);
    }

    public List<ChinaBean> list(ChinaCondition condition) {
        return null;
    }

    public ChinaBean get(Object id) {
        return chinaDaoImpl.get(id);
    }

    public List<ChinaBean> areaList(Long pid){
        List<ChinaBean> list = chinaDaoImpl.areaList(pid);
        List<ChinaBean> list1 = null;
        if(list.size()>0)
        for(ChinaBean bean : list){
            if(bean.getName().equals("市辖区")){
                list1 = chinaDaoImpl.areaList(bean.getId());
            }
        }
        if(list1!=null){
            list.addAll(list1);
        }
        for (int i = 0; i < list.size() ; i++) {
            if(list.get(i).getName().equals("市辖区")){
                list.remove(list.get(i));
            }
        }
        return list;

    }

    public List<ChinaBean> cityList(){
        return chinaDaoImpl.cityList();
    }


    public List<ChinaBean> cityList(Long pid){
       return  chinaDaoImpl.areaList(pid);
    }

    public List<ChinaBean> provinceList(){
        if(provinceList==null){
            log.info("初始化province");
            List<ChinaBean> list= chinaDaoImpl.list();
            provinceList= Lists.newArrayList();

            for (ChinaBean obj : list) {
                if(obj.getId()%10000==0)
                    provinceList.add(obj);
            }
            list.remove(0);

        }
            return provinceList;

    }


}

