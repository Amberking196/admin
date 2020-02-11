package com.server.module.system.statisticsManage.userState;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.server.module.system.statisticsManage.areaShoppingDay.AreaShoppingDayDTO;
import com.server.module.system.statisticsManage.payRecordPerDay.PayRecordPerDayForm;
import com.server.module.system.statisticsManage.userAction.UserChart;
import com.server.module.system.statisticsManage.userActiveDegree.UserActiveDegreeDao;
import com.server.module.system.statisticsManage.userActiveDegree.UserActiveDegreeDaoImpl;
import com.server.util.DateUtil;
@Service
public class UserStateServiceImpl implements UserStateService {

	@Autowired
	private UserStateDao userStateDao;
	private final static Logger log = LogManager.getLogger(UserActiveDegreeDaoImpl.class);

	@Override
	public UserStateBean stateCompare(PayRecordPerDayForm payDayForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserStateVo> userStateNum(PayRecordPerDayForm payDayForm) {
		List<UserStateBean> list=Lists.newArrayList();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-1);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
		calendar.get(Calendar.DATE), 0, 0, 0);
		payDayForm.setStartDate(calendar.getTime());

		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
		calendar.get(Calendar.DATE), 23, 59, 59);
		payDayForm.setEndDate(calendar.getTime());
		UserStateBean userStateNum=userStateDao.userStateNum(payDayForm);
		
		//注册人数
		Integer registerNum=userStateDao.customerRegisterNum(payDayForm);
		
		long a=System.currentTimeMillis();
		//用户当前状态总数量
		UserStateBean userCurrStateNum=userStateDao.userCurrStateNum(payDayForm);
		
		//用户原状态总数量
		UserStateBean userFromStateNum=userStateDao.userFromStateNum(payDayForm);
		userStateNum.setOne(userStateNum.getOne()-userCurrStateNum.getOne()+userFromStateNum.getOne());
		userStateNum.setTwo(userStateNum.getTwo()-userCurrStateNum.getTwo()+userFromStateNum.getTwo());
		userStateNum.setThree(userStateNum.getThree()-userCurrStateNum.getThree()+userFromStateNum.getThree());
		userStateNum.setFour(userStateNum.getFour()-userCurrStateNum.getFour()+userFromStateNum.getFour());
		userStateNum.setFive(userStateNum.getFive()-userCurrStateNum.getFive()+userFromStateNum.getFive());
		userStateNum.setSix(userStateNum.getSix()-userCurrStateNum.getSix()+userFromStateNum.getSix());
		userStateNum.setRegisterNum(registerNum);
		userStateNum.setDate(payDayForm.getStartDate());
		userStateNum.setSumNum(userStateNum.getOne()+userStateNum.getTwo()+userStateNum.getThree()+userStateNum.getFour()+userStateNum.getFive()+userStateNum.getSix()+userStateNum.getRegisterNum());
		userStateNum.setName("目前用户数据");
		list.add(userStateNum);

		//一周前
		UserStateBean userStateNumWeek=userStateDao.userStateNum(payDayForm);

		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.DATE,-8);
		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 
		calendar1.get(Calendar.DATE), 0, 0, 0);
		payDayForm.setStartDate(calendar1.getTime());

		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 
		calendar1.get(Calendar.DATE), 23, 59, 59);
		payDayForm.setEndDate(calendar1.getTime());
		
		Integer registerNumWeek=userStateDao.customerRegisterNum(payDayForm);

		payDayForm.setEndDate(calendar.getTime());
		UserStateBean userCurrStateNumWeek=userStateDao.userCurrStateNum(payDayForm);
		UserStateBean userFromStateNumWeek=userStateDao.userFromStateNum(payDayForm);
		
		//add(list,userStateNumWeekWeek,userCurrStateNumWeekWeek,userFromStateNumWeekWeek,payDayForm,registerNumWeek);
		userStateNumWeek.setOne(userStateNum.getOne()-userCurrStateNumWeek.getOne()+userFromStateNumWeek.getOne());
		userStateNumWeek.setTwo(userStateNum.getTwo()-userCurrStateNumWeek.getTwo()+userFromStateNumWeek.getTwo());
		userStateNumWeek.setThree(userStateNum.getThree()-userCurrStateNumWeek.getThree()+userFromStateNumWeek.getThree());
		userStateNumWeek.setFour(userStateNum.getFour()-userCurrStateNumWeek.getFour()+userFromStateNumWeek.getFour());
		userStateNumWeek.setFive(userStateNum.getFive()-userCurrStateNumWeek.getFive()+userFromStateNumWeek.getFive());
		userStateNumWeek.setSix(userStateNum.getSix()-userCurrStateNumWeek.getSix()+userFromStateNumWeek.getSix());
		userStateNumWeek.setRegisterNum(registerNumWeek);
		userStateNumWeek.setDate(payDayForm.getStartDate());
		userStateNumWeek.setSumNum(userStateNumWeek.getOne()+userStateNumWeek.getTwo()+userStateNumWeek.getThree()+userStateNumWeek.getFour()+userStateNumWeek.getFive()+userStateNumWeek.getSix()+userStateNumWeek.getRegisterNum());
		userStateNumWeek.setName("上周用户数据");
		list.add(userStateNumWeek);
		
		UserStateBean u=new UserStateBean();	
		u.setOne(new BigDecimal((userStateNum.getOne()-userStateNumWeek.getOne())).divide(new BigDecimal(userStateNumWeek.getOne()==0?1:userStateNumWeek.getOne()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u.setTwo(new BigDecimal((userStateNum.getTwo()-userStateNumWeek.getTwo())).divide(new BigDecimal(userStateNumWeek.getTwo()==0?1:userStateNumWeek.getTwo()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u.setThree(new BigDecimal((userStateNum.getThree()-userStateNumWeek.getThree())).divide(new BigDecimal(userStateNumWeek.getThree()==0?1:userStateNumWeek.getThree()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u.setFour(new BigDecimal((userStateNum.getFour()-userStateNumWeek.getFour())).divide(new BigDecimal(userStateNumWeek.getFour()==0?1:userStateNumWeek.getFour()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u.setFive(new BigDecimal((userStateNum.getFive()-userStateNumWeek.getFive())).divide(new BigDecimal(userStateNumWeek.getFive()==0?1:userStateNumWeek.getFive()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u.setSix(new BigDecimal((userStateNum.getSix()-userStateNumWeek.getSix())).divide(new BigDecimal(userStateNumWeek.getSix()==0?1:userStateNumWeek.getSix()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u.setRegisterNum(new BigDecimal((userStateNum.getRegisterNum()-userStateNumWeek.getRegisterNum())).divide(new BigDecimal(userStateNumWeek.getRegisterNum()==0?1:userStateNumWeek.getRegisterNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u.setSumNum(new BigDecimal((userStateNum.getSumNum()-userStateNumWeek.getSumNum())).divide(new BigDecimal(userStateNumWeek.getSumNum()==0?1:userStateNumWeek.getSumNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());

		u.setName("对比");
		list.add(u);

		//一月前
		UserStateBean userStateNumMonth=userStateDao.userStateNum(payDayForm);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DATE,-1);
		calendar2.add(Calendar.MONTH, -1);
		calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), 
		calendar2.get(Calendar.DATE), 0, 0, 0);
		payDayForm.setStartDate(calendar2.getTime());

		calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), 
		calendar2.get(Calendar.DATE), 23, 59, 59);
		payDayForm.setEndDate(calendar2.getTime());
		
		Integer registerNumMonth=userStateDao.customerRegisterNum(payDayForm);

		payDayForm.setEndDate(calendar.getTime());
		UserStateBean userCurrStateNumMonth=userStateDao.userCurrStateNum(payDayForm);
		UserStateBean userFromStateNumMonth=userStateDao.userFromStateNum(payDayForm);
		//add(list,userStateNumMonth,userCurrStateNumMonth,userFromStateNumMonth,payDayForm,registerNumMonth);
		userStateNumMonth.setOne(userStateNum.getOne()-userCurrStateNumMonth.getOne()+userFromStateNumMonth.getOne());
		userStateNumMonth.setTwo(userStateNum.getTwo()-userCurrStateNumMonth.getTwo()+userFromStateNumMonth.getTwo());
		userStateNumMonth.setThree(userStateNum.getThree()-userCurrStateNumMonth.getThree()+userFromStateNumMonth.getThree());
		userStateNumMonth.setFour(userStateNum.getFour()-userCurrStateNumMonth.getFour()+userFromStateNumMonth.getFour());
		userStateNumMonth.setFive(userStateNum.getFive()-userCurrStateNumMonth.getFive()+userFromStateNumMonth.getFive());
		userStateNumMonth.setSix(userStateNum.getSix()-userCurrStateNumMonth.getSix()+userFromStateNumMonth.getSix());
		userStateNumMonth.setRegisterNum(registerNumMonth);
		userStateNumMonth.setDate(payDayForm.getStartDate());
		userStateNumMonth.setSumNum(userStateNumMonth.getOne()+userStateNumMonth.getTwo()+userStateNumMonth.getThree()+userStateNumMonth.getFour()+userStateNumMonth.getFive()+userStateNumMonth.getSix()+userStateNumMonth.getRegisterNum());
		userStateNumMonth.setName("上周用户数据");
		list.add(userStateNumMonth);
		
		UserStateBean u1=new UserStateBean();	
		u1.setOne(new BigDecimal((userStateNum.getOne()-userStateNumMonth.getOne())).divide(new BigDecimal(userStateNumMonth.getOne()==0?1:userStateNumWeek.getOne()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u1.setTwo(new BigDecimal((userStateNum.getTwo()-userStateNumMonth.getTwo())).divide(new BigDecimal(userStateNumMonth.getTwo()==0?1:userStateNumWeek.getTwo()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u1.setThree(new BigDecimal((userStateNum.getThree()-userStateNumMonth.getThree())).divide(new BigDecimal(userStateNumMonth.getThree()==0?1:userStateNumWeek.getThree()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u1.setFour(new BigDecimal((userStateNum.getFour()-userStateNumMonth.getFour())).divide(new BigDecimal(userStateNumMonth.getFour()==0?1:userStateNumWeek.getFour()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u1.setFive(new BigDecimal((userStateNum.getFive()-userStateNumMonth.getFive())).divide(new BigDecimal(userStateNumMonth.getFive()==0?1:userStateNumWeek.getFive()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u1.setSix(new BigDecimal((userStateNum.getSix()-userStateNumMonth.getSix())).divide(new BigDecimal(userStateNumMonth.getSix()==0?1:userStateNumWeek.getSix()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u1.setRegisterNum(new BigDecimal((userStateNum.getRegisterNum()-userStateNumMonth.getRegisterNum())).divide(new BigDecimal(userStateNumMonth.getRegisterNum()==0?1:userStateNumWeek.getRegisterNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
		u1.setSumNum(new BigDecimal((userStateNum.getSumNum()-userStateNumMonth.getSumNum())).divide(new BigDecimal(userStateNumMonth.getSumNum()==0?1:userStateNumWeek.getSumNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());

		u1.setName("对比");
		list.add(u1);
		NumberFormat nf   =   NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");

        List<UserStateVo> list2=Lists.newArrayList();
        for(UserStateBean usbBean:list) {
        	UserStateVo usv=new UserStateVo();
        	usv.setName(usbBean.getName());
        	if(usbBean.getDate()!=null){
        		usv.setDate(DateUtil.formatYYYYMMDD(usbBean.getDate()));
        	}
        	if(usbBean.getSumNum()<1 && usbBean.getSumNum()!=0) {
        		usv.setSumNum(nf.format(usbBean.getSumNum()));
        	}else{usv.setSumNum(decimalFormat.format(usbBean.getSumNum()));}
        	if(usbBean.getOne()<1 && usbBean.getOne()!=0) {
        		usv.setOne(nf.format(usbBean.getOne()));
        	}else{usv.setOne(decimalFormat.format(usbBean.getOne()));}
        	if(usbBean.getTwo()<1 && usbBean.getTwo()!=0) {
        		usv.setTwo(nf.format(usbBean.getTwo()));
        	}else{usv.setTwo(decimalFormat.format(usbBean.getTwo()));}
           	if(usbBean.getThree()<1 && usbBean.getThree()!=0) {
        		usv.setThree(nf.format(usbBean.getThree()));
        	}else{usv.setThree(decimalFormat.format(usbBean.getThree()));}
           	if(usbBean.getFour()<1 && usbBean.getFour()!=0) {
        		usv.setFour(nf.format(usbBean.getFour()));
        	}else{usv.setFour(decimalFormat.format(usbBean.getFour()));}
           	if(usbBean.getFive()<1 && usbBean.getFive()!=0) {
        		usv.setFive(nf.format(usbBean.getFive()));
        	}else{usv.setFive(decimalFormat.format(usbBean.getFive()));}
           	if(usbBean.getSix()<1 && usbBean.getSix()!=0) {
        		usv.setSix(nf.format(usbBean.getSix()));
        	}else{usv.setSix(decimalFormat.format(usbBean.getSix()));}
           	if(usbBean.getRegisterNum()<1 && usbBean.getRegisterNum()!=0) {
        		usv.setRegisterNum(nf.format(usbBean.getRegisterNum()));
        	}else{usv.setRegisterNum(decimalFormat.format(usbBean.getRegisterNum()));}
        	list2.add(usv);
        }
		return list2;
	}

	public void add(List<UserStateBean> list,UserStateBean usb,UserStateBean usbCurr,UserStateBean usbFrom,PayRecordPerDayForm payDayForm,Integer registerNum) {
		usb.setOne(usb.getOne()-usbCurr.getOne()+usbFrom.getOne());
		usb.setTwo(usb.getTwo()-usbCurr.getTwo()+usbFrom.getTwo());
		usb.setThree(usb.getThree()-usbCurr.getThree()+usbFrom.getThree());
		usb.setFour(usb.getFour()-usbCurr.getFour()+usbFrom.getFour());
		usb.setFive(usb.getFive()-usbCurr.getFive()+usbFrom.getFive());
		usb.setSix(usb.getSix()-usbCurr.getSix()+usbFrom.getSix());
		usb.setRegisterNum(registerNum);
		usb.setDate(payDayForm.getStartDate());
		usb.setSumNum(usb.getOne()+usb.getTwo()+usb.getThree()+usb.getFour()+usb.getFive()+usb.getSix()+usb.getRegisterNum());
		list.add(usb);
		for(UserStateBean usblist:list) {
			log.info("-------------------------------------------------------");
			log.info(usblist.toString());
		}
	}
	
	
	
	
	
	
	public Map<String,Object> userStateNumChart(PayRecordPerDayForm payDayForm){
		Date startDate=payDayForm.getStartDate();
		Date endDate=payDayForm.getEndDate();
		
		List<UserStateBean> list=Lists.newArrayList();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-1);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
		calendar.get(Calendar.DATE), 0, 0, 0);
		Date yesStartDate=calendar.getTime();

		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
		calendar.get(Calendar.DATE), 23, 59, 59);
		Date yesEndDate=calendar.getTime();
		
		DateTime dt2 = new DateTime(endDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		Date a=dt2.toDate();
		payDayForm.setStartDate(a);
		payDayForm.setEndDate(yesEndDate);
		UserStateBean userCurrStateNum=userStateDao.userCurrStateNum(payDayForm);
		UserStateBean userFromStateNum=userStateDao.userFromStateNum(payDayForm);
		
		dt2=dt2.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		payDayForm.setEndDate(dt2.toDate());
		Integer registerNum=userStateDao.customerRegisterNum(payDayForm);
		//取endDate的用户状态数据
		UserStateBean userStateNum=userStateDao.userStateNum(payDayForm);//当前用户状态
		userStateNum.setOne(userStateNum.getOne()-userCurrStateNum.getOne()+userFromStateNum.getOne());
		userStateNum.setTwo(userStateNum.getTwo()-userCurrStateNum.getTwo()+userFromStateNum.getTwo());
		userStateNum.setThree(userStateNum.getThree()-userCurrStateNum.getThree()+userFromStateNum.getThree());
		userStateNum.setFour(userStateNum.getFour()-userCurrStateNum.getFour()+userFromStateNum.getFour());
		userStateNum.setFive(userStateNum.getFive()-userCurrStateNum.getFive()+userFromStateNum.getFive());
		userStateNum.setSix(userStateNum.getSix()-userCurrStateNum.getSix()+userFromStateNum.getSix());
		userStateNum.setRegisterNum(registerNum);
		userStateNum.setDate(endDate);
		userStateNum.setSumNum(userStateNum.getOne()+userStateNum.getTwo()+userStateNum.getThree()+userStateNum.getFour()+userStateNum.getFive()+userStateNum.getSix()+userStateNum.getRegisterNum());
		list.add(userStateNum);
		
		payDayForm.setStartDate(new DateTime(startDate).withMinuteOfHour(0).withSecondOfMinute(0).toDate());
		payDayForm.setEndDate(new DateTime(endDate).withMinuteOfHour(0).withSecondOfMinute(0).minusDays(1).toDate());
		LinkedHashMap hp=userStateDao.userStateGroupByDate(payDayForm);
		UserStateBean virUserStateNum = new UserStateBean();
		BeanUtils.copyProperties(userStateNum, virUserStateNum);
		payDayForm.setStartDate(startDate);payDayForm.setEndDate(endDate);
		List<CustomerRegisterNumDto> crList=userStateDao.customerRegisterNumGroupByDay(payDayForm);
		
		Iterator<Date> dateIterator=hp.keySet().iterator();
		while(dateIterator.hasNext()) {
			UserStateBean newUserStateBean=new UserStateBean();
			Date date2= dateIterator.next();
			UserStateBean usb=(UserStateBean) hp.get(date2);
			newUserStateBean.setOne(virUserStateNum.getOne()+usb.getOne());
			newUserStateBean.setTwo(virUserStateNum.getTwo()+usb.getTwo());
			newUserStateBean.setThree(virUserStateNum.getThree()+usb.getThree());
			newUserStateBean.setFour(virUserStateNum.getFour()+usb.getFour());
			newUserStateBean.setFive(virUserStateNum.getFive()+usb.getFive());
			newUserStateBean.setSix(virUserStateNum.getSix()+usb.getSix());
			newUserStateBean.setDate(date2);
			for(CustomerRegisterNumDto cr :crList) {
				if(cr.getDate().compareTo(date2)==0) {
					newUserStateBean.setRegisterNum(cr.getNum());
					break;
				}
			}
			newUserStateBean.setSumNum(newUserStateBean.getOne()+newUserStateBean.getTwo()+newUserStateBean.getThree()+newUserStateBean.getFour()+newUserStateBean.getFive()+newUserStateBean.getSix()+newUserStateBean.getRegisterNum());
			BeanUtils.copyProperties(newUserStateBean, virUserStateNum);
			list.add(newUserStateBean);
		}
        Collections.sort(list, new Comparator<UserStateBean>() {
            public int compare(UserStateBean o1, UserStateBean o2) {
            	if(o1.getDate().compareTo(o2.getDate())>0) {
                    return 1;
            	}
            	else {
            		return -1;
            	}
            }
        });
		NumberFormat nf   =   NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        List<UserStateVo> list2=Lists.newArrayList();
        for(UserStateBean usbBean:list) {
        	UserStateVo usv=new UserStateVo();
        	usv.setName(usbBean.getName());
        	if(usbBean.getDate()!=null){
        		usv.setDate(DateUtil.formatYYYYMMDD(usbBean.getDate()));
        	}
        	if(usbBean.getSumNum()<1 && usbBean.getSumNum()!=0) {
        		usv.setSumNum(nf.format(usbBean.getSumNum()));
        	}else{usv.setSumNum(decimalFormat.format(usbBean.getSumNum()));}
        	if(usbBean.getOne()<1 && usbBean.getOne()!=0) {
        		usv.setOne(nf.format(usbBean.getOne()));
        	}else{usv.setOne(decimalFormat.format(usbBean.getOne()));}
        	if(usbBean.getTwo()<1 && usbBean.getTwo()!=0) {
        		usv.setTwo(nf.format(usbBean.getTwo()));
        	}else{usv.setTwo(decimalFormat.format(usbBean.getTwo()));}
           	if(usbBean.getThree()<1 && usbBean.getThree()!=0) {
        		usv.setThree(nf.format(usbBean.getThree()));
        	}else{usv.setThree(decimalFormat.format(usbBean.getThree()));}
           	if(usbBean.getFour()<1 && usbBean.getFour()!=0) {
        		usv.setFour(nf.format(usbBean.getFour()));
        	}else{usv.setFour(decimalFormat.format(usbBean.getFour()));}
           	if(usbBean.getFive()<1 && usbBean.getFive()!=0) {
        		usv.setFive(nf.format(usbBean.getFive()));
        	}else{usv.setFive(decimalFormat.format(usbBean.getFive()));}
           	if(usbBean.getSix()<1 && usbBean.getSix()!=0) {
        		usv.setSix(nf.format(usbBean.getSix()));
        	}else{usv.setSix(decimalFormat.format(usbBean.getSix()));}
           	if(usbBean.getRegisterNum()<1 && usbBean.getRegisterNum()!=0) {
        		usv.setRegisterNum(nf.format(usbBean.getRegisterNum()));
        	}else{usv.setRegisterNum(decimalFormat.format(usbBean.getRegisterNum()));}
        	list2.add(usv);
        }
//        List<Double> y=new ArrayList<Double>();
//        List<Double> y1=new ArrayList<Double>();
//        List<Double> y2=new ArrayList<Double>();
//        List<Double> y3=new ArrayList<Double>();
//        List<Double> y4=new ArrayList<Double>();
//        List<Double> y5=new ArrayList<Double>();
//        List<Double> y6=new ArrayList<Double>();
//        List<Double> y0=new ArrayList<Double>();
//		List<String> x = new ArrayList<String>();
//        for (int i = 0; i < list.size(); i++) {
//        	UserStateBean u=list.get(i);
//			x.add(DateUtil.formatYYYYMMDD(u.getDate()));
//			y.add(u.getSumNum());
//			y0.add(u.getRegisterNum());
//			y1.add(u.getOne());
//			y2.add(u.getTwo());
//			y3.add(u.getThree());
//			y4.add(u.getFour());
//			y5.add(u.getFive());
//			y6.add(u.getSix());
//        }
//        UserChart uc=new UserChart("总用户数","",y);
//        UserChart uc0=new UserChart("新用户数","",y0);
//        UserChart uc1=new UserChart("一次","",y1);
//        UserChart uc2=new UserChart("活跃","",y2);
//        UserChart uc3=new UserChart("忠实","",y3);
//        UserChart uc4=new UserChart("低频","",y4);
//        UserChart uc5=new UserChart("流失","",y5);
//        UserChart uc6=new UserChart("回流","",y6);
//		List<UserChart> u=new ArrayList<UserChart>();
//        u.add(uc);u.add(uc0);u.add(uc1);
//        u.add(uc2);u.add(uc3);u.add(uc4);
//        u.add(uc5);u.add(uc6);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("list",list2);
//		map.put("chartList", u);
//		map.put("x", x);
		return map;
	}
	
	@Override
	public List<UserStateVo> userStateNum2(PayRecordPerDayForm payDayForm) {
		List<UserStateBean> list=Lists.newArrayList();
		UserStateBean userStateNum=userStateDao.userStateNum(payDayForm);

		PayRecordPerDayForm payDayFormWeek=new PayRecordPerDayForm();
		PayRecordPerDayForm payDayFormMonth=new PayRecordPerDayForm();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-1);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
		calendar.get(Calendar.DATE), 0, 0, 0);
		payDayForm.setStartDate(calendar.getTime());
		Date yesStartDate=calendar.getTime();
		
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
		calendar.get(Calendar.DATE), 23, 59, 59);
		payDayForm.setEndDate(calendar.getTime());
		Date yesEndDate=calendar.getTime();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.DATE,-8);
		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 
		calendar1.get(Calendar.DATE), 0, 0, 0);
		payDayFormWeek.setStartDate(calendar1.getTime());

		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 
		calendar1.get(Calendar.DATE), 23, 59, 59);
		payDayFormWeek.setEndDate(calendar1.getTime());
		

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DATE,-1);
		calendar2.add(Calendar.MONTH, -1);
		calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), 
		calendar2.get(Calendar.DATE), 0, 0, 0);
		payDayFormMonth.setStartDate(calendar2.getTime());

		calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), 
		calendar2.get(Calendar.DATE), 23, 59, 59);
		payDayFormMonth.setEndDate(calendar2.getTime());


		payDayForm.setStartDate(payDayFormMonth.getStartDate());
		payDayForm.setEndDate(payDayForm.getEndDate());
		LinkedHashMap hp=userStateDao.userStateGroupByDate(payDayForm);
		UserStateBean virUserStateNum = new UserStateBean();
		BeanUtils.copyProperties(userStateNum, virUserStateNum);
		
		payDayForm.setStartDate(yesStartDate);
		payDayForm.setEndDate(yesEndDate);
		List<CustomerRegisterNumDto> crList=userStateDao.customerRegisterNumGroupByDay(payDayForm,payDayFormWeek,payDayFormMonth);
		
		Iterator<Date> dateIterator=hp.keySet().iterator();
	
		int k=0;
		while(dateIterator.hasNext()) {
			UserStateBean newUserStateBean=new UserStateBean();
			Date date2= dateIterator.next();
			UserStateBean usb=(UserStateBean) hp.get(date2);
			newUserStateBean.setOne(virUserStateNum.getOne()+usb.getOne());
			newUserStateBean.setTwo(virUserStateNum.getTwo()+usb.getTwo());
			newUserStateBean.setThree(virUserStateNum.getThree()+usb.getThree());
			newUserStateBean.setFour(virUserStateNum.getFour()+usb.getFour());
			newUserStateBean.setFive(virUserStateNum.getFive()+usb.getFive());
			newUserStateBean.setSix(virUserStateNum.getSix()+usb.getSix());
			newUserStateBean.setDate(date2);
			for(CustomerRegisterNumDto cr :crList) {
				if(cr.getDate().compareTo(date2)==0) {
					newUserStateBean.setRegisterNum(cr.getNum());
					newUserStateBean.setSumNum(newUserStateBean.getOne()+newUserStateBean.getTwo()+newUserStateBean.getThree()+newUserStateBean.getFour()+newUserStateBean.getFive()+newUserStateBean.getSix()+newUserStateBean.getRegisterNum());
					if(k==0) {
						newUserStateBean.setSort(1);
						newUserStateBean.setName("目前用户数据");
					}else if(k==1) {
						newUserStateBean.setSort(2);
						newUserStateBean.setName("上周用户数据");
					}else if(k==2) {
						newUserStateBean.setSort(4);
						newUserStateBean.setName("上月用户数据");
					}
					list.add(newUserStateBean);
					k++;
					break;
				}
			}
			BeanUtils.copyProperties(newUserStateBean, virUserStateNum);
		}

		if(list.size()>0) {
			UserStateBean userStateNumYes=list.get(0);
			UserStateBean userStateNumWeek=list.get(1);
			UserStateBean userStateNumMonth=list.get(2);
				
			UserStateBean u=new UserStateBean();	
			u.setOne(new BigDecimal((userStateNumYes.getOne()-userStateNumWeek.getOne())).divide(new BigDecimal(userStateNumWeek.getOne()==0?1:userStateNumWeek.getOne()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setTwo(new BigDecimal((userStateNumYes.getTwo()-userStateNumWeek.getTwo())).divide(new BigDecimal(userStateNumWeek.getTwo()==0?1:userStateNumWeek.getTwo()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setThree(new BigDecimal((userStateNumYes.getThree()-userStateNumWeek.getThree())).divide(new BigDecimal(userStateNumWeek.getThree()==0?1:userStateNumWeek.getThree()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setFour(new BigDecimal((userStateNumYes.getFour()-userStateNumWeek.getFour())).divide(new BigDecimal(userStateNumWeek.getFour()==0?1:userStateNumWeek.getFour()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setFive(new BigDecimal((userStateNumYes.getFive()-userStateNumWeek.getFive())).divide(new BigDecimal(userStateNumWeek.getFive()==0?1:userStateNumWeek.getFive()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setSix(new BigDecimal((userStateNumYes.getSix()-userStateNumWeek.getSix())).divide(new BigDecimal(userStateNumWeek.getSix()==0?1:userStateNumWeek.getSix()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setRegisterNum(new BigDecimal((userStateNumYes.getRegisterNum()-userStateNumWeek.getRegisterNum())).divide(new BigDecimal(userStateNumWeek.getRegisterNum()==0?1:userStateNumWeek.getRegisterNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setSumNum(new BigDecimal((userStateNumYes.getSumNum()-userStateNumWeek.getSumNum())).divide(new BigDecimal(userStateNumWeek.getSumNum()==0?1:userStateNumWeek.getSumNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u.setSort(3);
			u.setName("对比");
			list.add(u);
	
			UserStateBean u1=new UserStateBean();	
			u1.setOne(new BigDecimal((userStateNumYes.getOne()-userStateNumMonth.getOne())).divide(new BigDecimal(userStateNumMonth.getOne()==0?1:userStateNumMonth.getOne()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setTwo(new BigDecimal((userStateNumYes.getTwo()-userStateNumMonth.getTwo())).divide(new BigDecimal(userStateNumMonth.getTwo()==0?1:userStateNumMonth.getTwo()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setThree(new BigDecimal((userStateNumYes.getThree()-userStateNumMonth.getThree())).divide(new BigDecimal(userStateNumMonth.getThree()==0?1:userStateNumMonth.getThree()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setFour(new BigDecimal((userStateNumYes.getFour()-userStateNumMonth.getFour())).divide(new BigDecimal(userStateNumMonth.getFour()==0?1:userStateNumMonth.getFour()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setFive(new BigDecimal((userStateNumYes.getFive()-userStateNumMonth.getFive())).divide(new BigDecimal(userStateNumMonth.getFive()==0?1:userStateNumMonth.getFive()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setSix(new BigDecimal((userStateNumYes.getSix()-userStateNumMonth.getSix())).divide(new BigDecimal(userStateNumMonth.getSix()==0?1:userStateNumMonth.getSix()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setRegisterNum(new BigDecimal((userStateNumYes.getRegisterNum()-userStateNumMonth.getRegisterNum())).divide(new BigDecimal(userStateNumMonth.getRegisterNum()==0?1:userStateNumMonth.getRegisterNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setSumNum(new BigDecimal((userStateNumYes.getSumNum()-userStateNumMonth.getSumNum())).divide(new BigDecimal(userStateNumMonth.getSumNum()==0?1:userStateNumMonth.getSumNum()),4, BigDecimal.ROUND_HALF_UP).doubleValue());
			u1.setSort(5);
			u1.setName("对比");
			list.add(u1);
			
	        Collections.sort(list, new Comparator<UserStateBean>() {
	            public int compare(UserStateBean o1, UserStateBean o2) {
	            	if(new BigDecimal(o1.getSort()).compareTo(new BigDecimal(o2.getSort()))>0) {
	                    return 1;
	            	}
	            	else {
	            		return -1;
	            	}
	            }
	        });
		}
		NumberFormat nf   =   NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        List<UserStateVo> list2=Lists.newArrayList();
        for(UserStateBean usbBean:list) {
        	UserStateVo usv=new UserStateVo();
        	usv.setSort(usbBean.getSort());
        	usv.setName(usbBean.getName());
        	if(usbBean.getDate()!=null){
        		usv.setDate(DateUtil.formatYYYYMMDD(usbBean.getDate()));
        	}
        	if(usbBean.getSumNum()<1 && usbBean.getSumNum()!=0) {
        		usv.setSumNum(nf.format(usbBean.getSumNum()));
        	}else{usv.setSumNum(decimalFormat.format(usbBean.getSumNum()));}
        	if(usbBean.getOne()<1 && usbBean.getOne()!=0) {
        		usv.setOne(nf.format(usbBean.getOne()));
        	}else{usv.setOne(decimalFormat.format(usbBean.getOne()));}
        	if(usbBean.getTwo()<1 && usbBean.getTwo()!=0) {
        		usv.setTwo(nf.format(usbBean.getTwo()));
        	}else{usv.setTwo(decimalFormat.format(usbBean.getTwo()));}
           	if(usbBean.getThree()<1 && usbBean.getThree()!=0) {
        		usv.setThree(nf.format(usbBean.getThree()));
        	}else{usv.setThree(decimalFormat.format(usbBean.getThree()));}
           	if(usbBean.getFour()<1 && usbBean.getFour()!=0) {
        		usv.setFour(nf.format(usbBean.getFour()));
        	}else{usv.setFour(decimalFormat.format(usbBean.getFour()));}
           	if(usbBean.getFive()<1 && usbBean.getFive()!=0) {
        		usv.setFive(nf.format(usbBean.getFive()));
        	}else{usv.setFive(decimalFormat.format(usbBean.getFive()));}
           	if(usbBean.getSix()<1 && usbBean.getSix()!=0) {
        		usv.setSix(nf.format(usbBean.getSix()));
        	}else{usv.setSix(decimalFormat.format(usbBean.getSix()));}
           	if(usbBean.getRegisterNum()<1 && usbBean.getRegisterNum()!=0) {
        		usv.setRegisterNum(nf.format(usbBean.getRegisterNum()));
        	}else{usv.setRegisterNum(decimalFormat.format(usbBean.getRegisterNum()));}
        	list2.add(usv);
        }

		return list2;
	}
}
