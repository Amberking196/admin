package com.server.module.system.statisticsManage.customerGroup.customerEvent;

import com.server.module.system.statisticsManage.customerGroup.customer.CustomerAnalyzeService;
import com.server.module.system.statisticsManage.customerGroup.customer.StateVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.google.common.collect.Lists;
import com.server.module.system.statisticsManage.chart.ChartForm;
import com.server.module.system.statisticsManage.chart.ChartUtil;
import com.server.module.system.statisticsManage.chart.DateCountVo;
import com.server.module.system.statisticsManage.chart.Grouping;
import com.server.module.system.statisticsManage.chart.MultipleResponseData;
import com.server.module.system.statisticsManage.chart.ResponseData;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-10-18 09:10:39
 */
@Service
public class CustomerAnalyzeEventServiceImpl implements CustomerAnalyzeEventService {

	private static Log log = LogFactory.getLog(CustomerAnalyzeEventServiceImpl.class);
	@Autowired
	private CustomerAnalyzeEventDao customerAnalyzeEventDaoImpl;
	@Autowired
			private CustomerAnalyzeService customerAnalyzeService;

	List<CustomerEventStateVo> listStateChange=Lists.newArrayList();
	public ReturnDataUtil listPage(CustomerAnalyzeEventCondition condition) {
		return customerAnalyzeEventDaoImpl.listPage(condition);
	}

	public CustomerAnalyzeEventBean add(CustomerAnalyzeEventBean entity) {
		return customerAnalyzeEventDaoImpl.insert(entity);
	}

	public boolean update(CustomerAnalyzeEventBean entity) {
		return customerAnalyzeEventDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return customerAnalyzeEventDaoImpl.delete(id);
	}

	public List<CustomerAnalyzeEventBean> list(CustomerAnalyzeEventCondition condition) {
		return null;
	}

	public CustomerAnalyzeEventBean get(Object id) {
		return customerAnalyzeEventDaoImpl.get(id);
	}

	@Override
	public List<DateCountVo> listCustomerAnalyzeEventInfo(ChartForm form) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<CustomerEventStateVo> listStateChange(){
		if(listStateChange.size()==0){
			CustomerEventStateVo vo1=new CustomerEventStateVo("1to2","一次->活跃");
			CustomerEventStateVo vo2=new CustomerEventStateVo("1to4","一次->低频");
			CustomerEventStateVo vo3=new CustomerEventStateVo("2to3","活跃->忠实");
			CustomerEventStateVo vo4=new CustomerEventStateVo("2to4","活跃->低频");
			CustomerEventStateVo vo5=new CustomerEventStateVo("4to6","低频->回流");
			CustomerEventStateVo vo6=new CustomerEventStateVo("4to5","低频->流失");
			CustomerEventStateVo vo7=new CustomerEventStateVo("5to6","流失->回流");
			CustomerEventStateVo vo8=new CustomerEventStateVo("3to2","忠实->活跃");
			listStateChange.add(vo1);listStateChange.add(vo2);listStateChange.add(vo3);listStateChange.add(vo4);
			listStateChange.add(vo5);listStateChange.add(vo6);listStateChange.add(vo7);listStateChange.add(vo8);
		}
		
		 
		return listStateChange;
	}
	public MultipleResponseData customerStateChangeStatistics(ChartForm form) {

		MultipleResponseData data=new MultipleResponseData();
		List<ResponseData> listResponse=Lists.newArrayList();
		// TODO Auto-generated method stub
		//type = 0 日  1 旬 2 月  3 季度
		List<DateCountVo> list=null;
		ResponseData temp=null;
		String[]  funs=form.getFunctions();
	    for(int i=0;i<funs.length;i++){
	    	String fun=funs[i];
	    	System.out.println(fun);
	    	String[] states=fun.split("to");
	    	int fromState=Integer.parseInt(states[0]);
	    	int currState=Integer.parseInt(states[1]);
			list=customerAnalyzeEventDaoImpl.listCustomerAnalyzeEventInfo(form, fromState, currState);
			temp=Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()),form, list);
			temp.setName(getFunName(fun));
			temp.setUnit("人次");
			listResponse.add(temp);
	    	
	    }
		
		
		data.setList(listResponse);
        data.formalize();   
        
       return data;
	}
	public MultipleResponseData customerStateStatistics(ChartForm form) {

		MultipleResponseData data=new MultipleResponseData();
		List<ResponseData> listResponse=Lists.newArrayList();
		// TODO Auto-generated method stub
		List<DateCountVo> list=null;
		ResponseData temp=null;
		String[]  funs=form.getFunctions();
		for(int i=0;i<funs.length;i++){
			String fun=funs[i];
			System.out.println(fun);
			int currState=Integer.parseInt(fun);
			list=customerAnalyzeEventDaoImpl.listCustomerStateInfo(form,currState);
			temp=Grouping.getResponseData(Grouping.getGroupingInstanceByType(form.getType()),form, list);
			temp.setName(getFunStateName(fun));
			temp.setUnit("人次");
			listResponse.add(temp);

		}


		data.setList(listResponse);
		data.formalize();

		return data;
	}
	private String getFunName(String fun){
		List<CustomerEventStateVo> list=listStateChange();
		
		for(CustomerEventStateVo vo : list){
			if(vo.getFromTo().equals(fun)){
				return vo.getName();
			}
		}
		return null;
	}
	private String getFunStateName(String fun){
		List<StateVO> list=customerAnalyzeService.listState();

		for(StateVO vo : list){
			if(vo.getState().equals(Integer.parseInt(fun))){
				return vo.getLabel();
			}
		}
		return null;
	}
}
