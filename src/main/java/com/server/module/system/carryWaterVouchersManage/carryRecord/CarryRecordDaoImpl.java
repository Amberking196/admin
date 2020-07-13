package com.server.module.system.carryWaterVouchersManage.carryRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.util.ReturnDataUtil;
import com.server.util.sqlUtil.MySqlFuns;

@Repository
public class CarryRecordDaoImpl extends MySqlFuns implements CarryRecordDao{

	private final static Logger log = LogManager.getLogger(CarryRecordDaoImpl.class);

	@Override
	public ReturnDataUtil listPage(CarryRecordForm carryRecordForm){
		log.info("<CarryRecordDaoImpl--listPage--start>");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from carry_record cr left join carry_water_vouchers cwv on cr.carryid=cwv.id where 1=1 ");
		if(carryRecordForm.getOrderId()!=null) {
			sql.append(" and orderId ="+carryRecordForm.getOrderId());
		}
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		log.info("订单提水券列表SQL语句：" + sql.toString());

		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			List<CarryRecordBean> list = Lists.newArrayList();
			while (rs.next()) {
				CarryRecordBean bean = new CarryRecordBean();
				bean.setId(rs.getLong("id"));
				bean.setName(rs.getString("name"));
				bean.setNum(rs.getInt("num"));
				list.add(bean);
			}
			data.setTotal((long)list.size());
			data.setReturnObject(list);
			data.setStatus(1);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CarryWaterVouchersDaoImpl>-------<listPage>------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		
		log.info("<CarryRecordDaoImpl--listPage--end>");
		return data;

	}

	
	
	
}
