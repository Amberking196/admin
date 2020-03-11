package com.server.module.system.shoppingManager.customerOrder;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.userManage.CustomerDaoImpl;
import com.server.util.DateUtil;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class CustomerOrderDaoImpl extends BaseDao<CustomerOrderBean> implements CustomerOrderDao {
    public static Logger log = LogManager.getLogger(CustomerDaoImpl.class);

    @Autowired
    private CompanyDao companyDaoImpl;
    
    /**
	 * 商城订单列表查询
	 */
    @Override
    public ReturnDataUtil findCustomerByForm(CustomerOrderForm orderForm) {
        log.info("<CustomerOrderDaoImpl--findOrderByForm--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        CustomerOrderForm customerOrderForm=new CustomerOrderForm();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT c.name as companyName,so.id,ptCode,payCode,so.type,so.state,payType,product,so.location,so.createTime,wxCouponCount,wxCouponOneFee,payTime,affirmTime,vendingMachinesCode,coupon,couponPrice,tca.phone,tca.name addressName,tca.receiver  ");
        sql.append(" from store_order so left join tbl_customer_address tca on so.addressId=tca.id   ");
        sql.append(" left join company c on c.id=so.companyId   ");
       

        if (orderForm != null) {
        	if(StringUtil.isNotBlank(orderForm.getPhone())) {
           	 sql.append(" left join tbl_customer  t on so.customerId=t.id  ");
        	}
        	if(StringUtil.isNotBlank(orderForm.getItemName()) || UserUtils.getUser().getId()==401L || UserUtils.getUser().getId()==736L)
        	{
             sql.append(" left join store_order_detile  sod on so.id=sod.orderId ");
            }
        	sql.append(" where 1=1 ");
        	if(orderForm.getCompanyId()!=null) {
       		    sql.append(" and FIND_IN_SET(c.id,getChildList("+orderForm.getCompanyId()+"))   ");
        	}
            if (orderForm.getStartDate() != null) {
                sql.append(" and so.createTime>='" + DateUtil.formatYYYYMMDDHHMMSS(orderForm.getStartDate()) + "'");
            }
            if (orderForm.getEndDate() != null) {
                sql.append(" and so.createTime<'" + DateUtil.formatLocalYYYYMMDDHHMMSS(orderForm.getEndDate(), 1) + "'");
            }
            if (orderForm.getPayType() != null) {
                sql.append(" and payType=" + orderForm.getPayType());
            }
            if (orderForm.getType() != null) {
                sql.append(" and so.type=" + orderForm.getType());//已修改
            }
            if(orderForm.getState() != null) {
            	sql.append(" and so.state=" +orderForm.getState());
            }
            if(StringUtil.isNotBlank(orderForm.getConsigneePhone())) {
            	sql.append(" and tca.phone='"+orderForm.getConsigneePhone().trim()+"' ");
            }
            if(StringUtil.isNotBlank(orderForm.getPhone())) {
              	 sql.append("  and  t.phone='"+orderForm.getPhone()+"' ");
            }
            
            if(StringUtil.isNotBlank(orderForm.getItemName())) {
             	sql.append(" and sod.itemName like '%"+orderForm.getItemName()+"%'");
            }
        }
        //权限控制
        if( UserUtils.getUser().getId()==736L){
            sql.append(" AND sod.itemName LIKE '%乔府%' group by so.id ");
        }else{
            sql.append(" and so.customerId in (select id from tbl_customer t left join vending_machines_info  vmi on  t.vmCode=vmi.code where vmi.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId())+" )");
        }

        sql.append(" order by createTime desc");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("商城订单列表查询sql>>>:" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getRow();
            }
            long off = (orderForm.getCurrentPage() - 1) * orderForm.getPageSize();
            pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + orderForm.getPageSize());
            rs = pst.executeQuery();
            List<CustomerOrderBean> list = Lists.newArrayList();
            int id = 0;
            while (rs.next()) {
                id++;
                CustomerOrderBean customer = new CustomerOrderBean();
                customer.setNum(id);
                customer.setCompanyName(rs.getString("companyName"));
                customer.setProduct(rs.getString("product"));
                customer.setOrderId(rs.getInt("id"));
                customer.setType(rs.getInt("type"));
                customer.setPayType(rs.getInt("payType"));
                customer.setPtCode(rs.getString("ptCode"));
                customer.setPayCode(rs.getString("payCode"));
                customer.setLocation(rs.getString("addressName"));
                customer.setCreateTime(rs.getTimestamp("createTime"));
                customer.setVmCode(rs.getString("vendingMachinesCode"));
                customer.setWxCouponCount(rs.getInt("wxCouponCount"));
                customer.setWxCouponOneFee(rs.getInt("wxCouponOneFee"
                        + ""));
                customer.setPayTime(rs.getTimestamp("payTime"));
                customer.setCoupon(rs.getInt("coupon"));
                customer.setCouponprice(rs.getDouble("couponprice"));
                customer.setAffirmTime(rs.getTimestamp("affirmTime"));
                customer.setPayCode(rs.getString("payCode"));
                customer.setConsigneePhone(rs.getString("phone"));
                customer.setStateName(PayStateEnum.findStateName(Integer.valueOf(rs.getString("state"))));
                customer.setReceiver(rs.getString("receiver"));
                String substring = rs.getString("createTime").substring(rs.getString("createTime").length()-rs.getString("createTime").lastIndexOf("."),rs.getString("createTime").indexOf("."));
                customer.setTime(substring);
                customer.setPayName(customer.getPayName());
                customer.setOrderName(customer.getOrderName());
                
               //详情查询
                customerOrderForm.setProduct(rs.getString("id"));
                List<ShoppingBean> shopppingList = findCustomerByProduce(customerOrderForm);
				customer.setList(shopppingList);
                list.add(customer);
            }
            if(showSql) {
            	log.info(sql);
            }
            data.setCurrentPage(orderForm.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            data.setMessage("查询成功");
            log.info("<CustomerOrderDaoImpl--findOrderByForm----end>");
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return data;
        } finally {
           this.closeConnection(rs, pst, conn);
        }
        
    }

    /**
     * 订单详情查询
     */
    @Override
    public List<ShoppingBean> findCustomerByProduce(CustomerOrderForm orderForm){
        log.info("<CustomerOrderDaoImpl--findCustomerByProduce--start>");
        StringBuffer sql = new StringBuffer();
        List<ShoppingBean> list = Lists.newArrayList();
        sql.append(" select tc.id,tc.phone,sod.itemName,sod.price,sod.num,so.location,sod.createTime  from store_order  so  ");
        sql.append(" left join store_order_detile sod on so.id=sod.orderId ");
        sql.append(" left join tbl_customer tc on sod.customerId=tc.id ");
        sql.append(" where sod.orderId='" + orderForm.getProduct()+ "' ");
        if(StringUtil.isNotBlank(orderForm.getItemName())) {
        	sql.append(" and sod.itemName like '%"+orderForm.getItemName().trim()+"%' ");
        }
        if(StringUtil.isNotBlank(orderForm.getPhone())) {
        	sql.append(" and tc.phone='"+orderForm.getPhone().trim()+"' ");
        }
        sql.append(" order by sod.createTime desc ");
        log.info("用户商城订单-商品详情-sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            int id = 0;
            while (rs.next()&&rs!=null) {
                id++;
                ShoppingBean customer = new ShoppingBean();
                customer = new ShoppingBean();
                customer.setCustomerId(rs.getInt("id"));
                customer.setItemName(rs.getString("itemName"));
                customer.setNum(rs.getInt("num"));
                customer.setPrice(rs.getDouble("price"));
                customer.setPhone(rs.getString("phone"));
                customer.setTotalPrice(rs.getBigDecimal("price").multiply(BigDecimal.valueOf(rs.getInt("num"))));
                list.add(customer);
            }
            log.info("<CustomerOrderDaoImpl--findOrderByForm----end>");
            return list;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("<CustomerOrderDaoImpl--findOrderByForm----end>");
            return list;
        } finally {
           this.closeConnection(rs, ps, conn);
        }
    }

    @Override
    public ReturnDataUtil statisticsConsumptionRecord(UsersConsumptionRecordForm form) {

        log.info("<CustomerOrderDaoImpl--statisticsConsumptionRecord--start>");
        ReturnDataUtil data = new ReturnDataUtil();
        StringBuffer sql = new StringBuffer();




        // 是否有金额查询条件
        if (form.getPrice() != null) {
            sql.append("SELECT * from ( SELECT pr.customerId AS customerId,tc.phone as phone ,sum(pr.price)  AS sum, count(pr.customerId) AS count ,  tc.nickname as nickName ,tc.vmCode ");

            sql.append(" FROM pay_record pr, vending_machines_info vi,tbl_customer tc");

            sql.append(" WHERE vi.code = pr.vendingMachinesCode AND pr.state = 10001 AND tc.id = pr.customerId");

            // 如果没有把公司Id作为查询条件，则查询当前公司以及子公司
            if (form.getCompanyId() != null) {
                sql.append(" and vi.companyId = " + form.getCompanyId() + "");

            } else {
                sql.append(" and vi.companyId in (" + form.getCompanyIds() + ")");
            }
            if (form.getPhone() != null) {
                sql.append(" and tc.phone like '%" + form.getPhone() + "%' ");
            }
            if (form.getVmCode() != null) {
                sql.append(" and tc.vmCode  like '%" + form.getVmCode().trim() + "%' ");
            }
            if (form.getStartTime() != null && form.getEndTime() != null) {

                String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(form.getStartTime());
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(form.getEndTime());
                sql.append(" AND pr.finishTime BETWEEN '" + startTime + "' AND '" + endTime + "'");
            }
            if (form.getState() != null) {
                // 根据状态值，来决定是查询大于，还是查询小于
                if (form.getState() == 1) {
                    sql.append(" GROUP BY pr.customerId) temp  where temp.sum <= " + form.getPrice() + "");
                } else {
                    sql.append(" GROUP BY pr.customerId) temp  where temp.sum >= " + form.getPrice() + "");

                }
            } else {
                sql.append(" GROUP BY pr.customerId) temp  where temp.sum = " + form.getPrice() + "");
            }

            sql.append("  ORDER BY temp.sum DESC, temp.count DESC");

        } else {
            sql.append(" SELECT pr.customerId AS customerId,tc.phone as phone ,sum(pr.price)  AS sum, count(pr.customerId) AS count, tc.nickname as nickName,tc.vmCode ");

            sql.append(" FROM pay_record pr, vending_machines_info vi,tbl_customer tc");

            sql.append(" WHERE vi.code = pr.vendingMachinesCode AND pr.state = 10001 AND tc.id = pr.customerId");

            // 如果没有把公司Id作为查询条件，则查询当前公司以及子公司
            if (form.getCompanyId() != null) {
                sql.append(" and vi.companyId = " + form.getCompanyId() + "");

            } else {
                sql.append(" and vi.companyId in (" + form.getCompanyIds() + ")");
            }

            if (form.getPhone() != null) {
                sql.append(" and tc.phone like '%" + form.getPhone() + "%'");
            }
            if (form.getVmCode() != null) {
                sql.append(" and tc.vmCode  like '%" + form.getVmCode().trim() + "%' ");
            }
            if (form.getStartTime() != null && form.getEndTime() != null) {

                String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(form.getStartTime());
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(form.getEndTime());
                sql.append(" AND pr.finishTime BETWEEN '" + startTime + "' AND '" + endTime + "'");
            }
            sql.append(" GROUP BY pr.customerId");
            sql.append("  ORDER BY sum(pr.price)  DESC ,count(pr.customerId) DESC");
        }
        log.info("统计用户消费记录-sql语句：" + sql);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            long count = 0;
            while (rs.next()) {
                count = rs.getRow();
            }
            if(form.getIsShowAll()!=1) {
                long off = (form.getCurrentPage() - 1) * form.getPageSize();
                ps = conn.prepareStatement(sql.toString() + " limit " + off + "," + form.getPageSize());
            }
            rs = ps.executeQuery();
            List<UsersConsumptionRecordVo> list = Lists.newArrayList();


            // 返回查询条件的时间回去
            String allStartTime;
            String allEndTime;
            if (form.getStartTime() != null && form.getEndTime() != null) {
                allStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(form.getStartTime());
                allEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(form.getEndTime());
            } else {
                allStartTime = "2017-12-16 18:39:01";
                allEndTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            }

            while (rs.next()) {
                UsersConsumptionRecordVo vo = new UsersConsumptionRecordVo();
                vo.setCustomerId(rs.getLong("customerId"));
                vo.setNickName(EmojiUtil.getEmoji(rs.getString("nickName")));
                vo.setCount(rs.getBigDecimal("count"));
                vo.setPrice(rs.getBigDecimal("sum"));
                vo.setPhone(rs.getLong("phone"));
                vo.setStartTime(allStartTime);
                vo.setEndTime(allEndTime);
                vo.setVmCode(rs.getString("vmCode"));

                list.add(vo);
            }
            data.setCurrentPage(form.getCurrentPage());
            data.setTotal(count);
            data.setReturnObject(list);
            data.setStatus(1);
            data.setMessage("查询成功");
            log.info("<CustomerOrderDaoImpl--statisticsConsumptionRecord----end>");
            return data;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            data.setStatus(0);
            data.setMessage("查询失败！");
            return data;
        } finally {
            try {
                rs.close();
                ps.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


