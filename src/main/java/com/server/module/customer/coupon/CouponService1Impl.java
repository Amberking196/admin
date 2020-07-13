package com.server.module.customer.coupon;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.server.module.customer.CustomerUtil;
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-07-06 14:05:39
 */
@Service
public class CouponService1Impl implements CouponService {

    private static Logger log = LogManager.getLogger(CouponService1Impl.class);
    @Autowired
    private CouponDao couponDaoImpl;
    @Autowired
    private CouponCustomerDao couponCustomerDao;
    @Autowired
    private CouponProduct1Dao couponProduct1Dao;
    @Autowired
    private CustomerService customerServiceImpl;
   
    /**
     * 手机端 显示用户优惠券
     */
    public ReturnDataUtil listPage(CouponCondition condition) {
    	log.info("手机端显示用户优惠券");
    	/*Long customerId= CustomerUtil.getCustomerId();
    	log.info("customerId------"+customerId);
		CustomerBean customerBean=customerServiceImpl.findCustomerById(customerId);
    	log.info("customerBean---"+customerBean);
    	*/
//		if(condition.getState()==5) {//可领取
//			if(customerBean.getPhone()!=null && customerBean.getOpenId()!=null) {//用户拥有微信号和手机号
//				returnDataUtil.setReturnObject(null);
//				returnDataUtil.setStatus(1);
//				return returnDataUtil;
//			}else {//支付宝用户/新用户  查询商城关注返券 点击领取优惠券->获取验证码页
//				log.info("查询商城关注返券");
////				couponForm.setWay(6);//商城关注返券
////				couponForm.setState(1);//已开始
//				Map<String,Object> m=new HashMap<String,Object>();
//				m.put("couponList",couponDaoImpl.shopListPage(6,1,CompanyEnum.YOUSHUIDAOJIA.getIndex()).getReturnObject());
//				m.put("openId",customerBean.getOpenId());
//				returnDataUtil.setReturnObject(m);
//				returnDataUtil.setStatus(2);
//				return  returnDataUtil;
//			}
//		}
		return couponDaoImpl.listPage(condition);
       
    }

    public CouponBean add(CouponBean entity) {
        return couponDaoImpl.insert(entity);
    }

    public boolean update(CouponBean entity) {
        return couponDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return couponDaoImpl.delete(id);
    }



    public CouponBean get(Object id) {

        return couponDaoImpl.get(id);
    }

    @Override
    public ReturnDataUtil addCouponToCustomer(Integer couponId) {
        ReturnDataUtil re=new ReturnDataUtil();
        CouponBean coupon=couponDaoImpl.get(couponId);
        CouponCustomerBean bean=new CouponCustomerBean();
        bean.setCouponId(couponId*1l);
        Long customerId= CustomerUtil.getCustomerId();
        bean.setCustomerId(customerId);
        bean.setState(1);
        bean.setStartTime(coupon.getLogicStartTime());
        bean.setEndTime(coupon.getLogicEndTime());
        bean.setCreateUser(customerId);
        bean.setUpdateUser(customerId);
        bean.setQuantity(coupon.getSendMax().longValue());
        couponCustomerDao.insert(bean);

        return re;
    }
    //白写，没用起来
    @Override
    public List<CouponVo> usableCoupons(Long[] productIds, Double[] prices) {
        List<CouponVo> list=couponCustomerDao.usableCoupons(productIds,prices);

        for (int i = 0; i < list.size(); i++) {
            CouponVo vo=list.get(i);
            if(vo.getBindProduct()==1) {//绑定商品
                List<CouponProduct1Bean> list1= couponProduct1Dao.list(vo.getId().intValue());
                Long[] ids=new Long[list1.size()];
                for (int i1 = 0; i1 < list1.size(); i1++) {
                    ids[i1]=list1.get(i1).getProductId();
                }
                vo.setProductIds(ids);
            }

        }
        //过虑不符合的coupon
        return filterCouponList(list,productIds,prices);

    }

    public  static List<CouponVo> filterCouponList(List<CouponVo> list,Long[] productIds, Double[] prices){
        List<CouponVo> listFilter= Lists.newArrayList();
        //判断哪些优惠劵可用
        long now=System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            CouponVo vo=list.get(i);
            //判断时间
           /* if(now>vo.getEndTime().getTime() || now>vo.getStartTime().getTime()){
                continue;
            }*/

            if(vo.getBindProduct()==0){//不绑定商品
                double price=0d;
                for (Double temp : prices) {
                    price=price+temp;
                }
                boolean flag=judgePrice(vo,price);
                if(flag)
                    listFilter.add(vo);

            }else{
                //  productIds
                boolean flag=false;
                for (int j=0;j< productIds.length;j++) {
                    Long productId=productIds[j];
                    boolean flag1=false;
                    for (Long pid : vo.getProductIds()) {
                        if(productId.intValue()==pid.intValue()){
                            double price=prices[j];
                            flag1=judgePrice(vo,price);
                            if(flag1)//该劵符合条件
                                break;
                        }
                    }
                    if(flag1){
                        flag=flag1;
                        break;
                    }
                }
                if(flag){//该劵可用

                    listFilter.add(vo);
                }
            }


        }

        return listFilter;
    }

    /**
     * 判断金额是否可以优惠
     * @param vo
     * @param price
     * @return
     */
    private static boolean judgePrice(CouponVo vo,double price){
        int type=vo.getType();// 优惠券类型    1 满减券  2  固定券
        boolean flag=false;
        if(type==1){
            if(vo.getMoney()<price){
                flag=true;
            }
        }else if(type==2){
            if(vo.getDeductionMoney()<price){
                flag=true;
            }
        }
        return flag;
    }

    public static String toString(Long[] ids){
        String temp="";
        for (Long id : ids) {
            temp=temp+id+",";
        }
        return temp;
    }

    public static void main(String[] args) {
        List<CouponVo> list=Lists.newArrayList();
           /* private Long id;
            private String name;//券名称
            private Integer type;// 优惠券类型    1 满减券  2  固定券
            private Integer way;//赠券方式 1：购买返券，2：自助领券，3：活动赠券
            private Integer useWhere;// 1 机器优惠劵   2 商城优惠劵
            private Double money;//满X元  满减券不需要设置
            private Double deductionMoney;//优惠金额*/
            CouponVo vo=new CouponVo();
            vo.setBindProduct(0);
            vo.setType(1);
            vo.setMoney(10d);
            vo.setDeductionMoney(5d);
            vo.setName("不绑定商品，满"+vo.getMoney()+" 减"+vo.getDeductionMoney());

            list.add(vo);

        CouponVo vo1=new CouponVo();
        vo1.setBindProduct(1);
        vo1.setType(2);
        vo1.setDeductionMoney(5d);
        Long[] p1s={1l,2l,3l};
        vo1.setName("绑定商品 减"+vo1.getDeductionMoney());
        vo1.setProductIds(p1s);
        list.add(vo1);

        CouponVo vo2=new CouponVo();
        vo2.setBindProduct(1);
        vo2.setType(2);
        vo2.setDeductionMoney(4d);
        Long[] p2s={4l,6l,5l};
        vo2.setName("绑定商品 减"+vo2.getDeductionMoney());
        vo2.setProductIds(p2s);
        list.add(vo2);


        Long[] productIds={1l,2l,3l};
        Double[] prices={20d,6d,12d};

        List<CouponVo> listFilter= filterCouponList(list,productIds,prices);

        for (int i = 0; i <listFilter.size() ; i++) {
            CouponVo t=listFilter.get(i);
            String pids="";
            if(t.getProductIds()!=null){
                pids=toString(t.getProductIds());
            }
            System.out.println("money="+t.getMoney()+"减="+t.getDeductionMoney()+"绑定商品="+pids+"    "+t.getName());
        }




    }

    /**
     * 查询优惠券下绑定的商品
     */
	@Override
	public List<ShoppingGoodsBean> findShoppingGoodsBean(int couponId) {
		log.info("<CouponService1Impl>----<findShoppingGoodsBean>----start");
		List<ShoppingGoodsBean> list = couponProduct1Dao.findShoppingGoodsBean(couponId);
		log.info("<CouponService1Impl>----<findShoppingGoodsBean>----end");
		return list;
	}

	
	@Override
	public Integer insertCouponCustomer(CouponCustomerBean couCusBean) {
		log.info("<CouponServiceImpl--insertCouponCustomer--start>");
		Integer insertCouponCustomer = couponDaoImpl.insertCouponCustomer(couCusBean);
		log.info("<CouponServiceImpl--insertCouponCustomer--start>");
		return insertCouponCustomer;
	}


	@Override
	public List<CouponDto> getGameCoupon(CouponForm couponForm) {
		log.info("<CouponService1Impl>----<getGameCoupon>----start");
		List<CouponDto> gameCoupon = couponDaoImpl.getGameCoupon(couponForm);
		log.info("<CouponService1Impl>----<getGameCoupon>----end");
		return gameCoupon;
	}

	@Override
	public CouponBean getCouponInfoByProduct(ShoppingGoodsBean shoppingGoodsBean) {
		log.info("<CouponService1Impl>----<getGameCoupon>----start");
		CouponBean bean = couponDaoImpl.getCouponInfoByProduct(shoppingGoodsBean);
		log.info("<CouponService1Impl>----<getGameCoupon>----end");
		return bean;
	}

	@Override
	public CouponCustomerBean getCouponCustomerBean(Long customerId, Long couponId) {
		log.info("<CouponService1Impl>----<getCouponCustomerBean>----start");
		CouponCustomerBean bean = couponDaoImpl.getCouponCustomerBean(customerId, couponId);
		log.info("<CouponService1Impl>----<getCouponCustomerBean>----end");
		return bean;
	}

	@Override
	public Boolean updateCouponCustomerBean(CouponCustomerBean entity) {
		log.info("<CouponService1Impl>----<updateCouponCustomerBean>----start");
		Boolean flag = couponDaoImpl.updateCouponCustomerBean(entity);
		log.info("<CouponService1Impl>----<updateCouponCustomerBean>----end");
		return flag;
	}

	@Override
	public List<CouponBean> getPresentCoupon(CouponForm couponForm) {
		log.info("<CouponServiceImpl>-----<getPresentCoupon>-----start");
		List<CouponBean> presentCoupon = couponDaoImpl.getPresentCoupon(couponForm);
		log.info("<CouponServiceImpl>-----<getPresentCoupon>-----end");
		return presentCoupon;
	}
	
	@Override
	public boolean isReceive(Long customerId, Long couponId) {
		log.info("<CouponServiceImpl>-----<isReceive>-----start");
		boolean receiveNum = couponDaoImpl.isReceive(customerId, couponId);
		log.info("<CouponServiceImpl>-----<isReceive>-----end");
		return receiveNum;
	}
}

