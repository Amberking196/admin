package com.server.module.system.baseManager.china;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * author name: yjr
 * create time: 2018-12-10 09:02:14
 */
@Api(value = "ChinaController", description = "行政区域")
@RestController
@RequestMapping("/china")
public class ChinaController {


    @Autowired
    private ChinaService chinaServiceImpl;

    @ApiOperation(value = "行政区域列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(ChinaCondition condition) {
        return chinaServiceImpl.listPage(condition);
    }

   /* @ApiOperation(value = "行政区域添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody ChinaBean entity) {
        return new ReturnDataUtil(chinaServiceImpl.add(entity));
    }*/
/*

    @ApiOperation(value = "行政区域修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody ChinaBean entity) {
        return new ReturnDataUtil(chinaServiceImpl.update(entity));
    }

    @ApiOperation(value = "行政区域删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Object id) {
        return new ReturnDataUtil(chinaServiceImpl.del(id));
    }
*/


    @ApiOperation(value = "市级区域列表", notes = "cityList", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/cityList", produces = "application/json;charset=UTF-8")
    public List<ChinaBean> cityList(){
         return chinaServiceImpl.cityList();
    }


    @ApiOperation(value = "区域列表", notes = "areaList", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/areaList", produces = "application/json;charset=UTF-8")
    public List<ChinaBean> areaList(Long pid){
          return chinaServiceImpl.areaList(pid);
    }


    /**
     *
     * @param level  0 省 1 市 2 区
     * @param pid
     * @return
     */
    @ApiOperation(value = "地理位置查询", notes = "list", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public List<ChinaBean> list(int level,Long pid){
        List<ChinaBean> list= Lists.newArrayList();
        if(level==0){
            list = chinaServiceImpl.provinceList();
        }else if(level==1){
            list = chinaServiceImpl.cityList(pid);
        }else{
            list = chinaServiceImpl.areaList(pid);
        }
        return list;
    }
}

