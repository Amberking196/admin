package com.server.module.system.statisticsManage.merchandiseSalesStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyDao;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-05-09 21:15:27
 */
@Repository
public class MerchandiseSalesStatisticsDaoImpl extends BaseDao<MerchandiseSalesStatisticsBean>
		implements MerchandiseSalesStatisticsDao {

	public static Logger log = LogManager.getLogger(MerchandiseSalesStatisticsDaoImpl.class);
	

	@Autowired
	private CompanyDao companyDaoImpl;
	/**
	 * 商品销售统计列表查询 
	 */
	public ReturnDataUtil listPage(MerchandiseSalesStatisticsCondition condition) {
		log.info("<MerchandiseSalesStatisticsImpl>----<listPage>----start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append(" select basicItemId,name,barCode,vmCode,sum(finishedItemNum) salesQuantity,sum(finishedMoney) allPrice,round(sum(finishedMoney)/sum(finishedItemNum),2) avgPrice,tp.createTime from tbl_statistics_pay_per_day tp inner join item_basic ib  on tp.basicItemId=ib.id LEFT JOIN vending_machines_info vmi ON vmi.`code`=tp.`vmCode` where 1=1  ");
		if (condition.getAreaId() != null && condition.getAreaId()>0) {
			sql.append(" and vmi.areaId = '" + condition.getAreaId() + "' ");
		}
		if(condition.getItemId() != null) {
			sql.append(" and basicItemId='"+condition.getItemId()+"'");
		}
		if(condition.getBasicItemId() != null) {
			sql.append(" and basicItemId='"+condition.getBasicItemId()+"'");
		}
		if(StringUtil.isNotBlank(condition.getVmCode())) {
			sql.append(" and vmCode like '%"+condition.getVmCode()+"%'"); 
		}
		if (condition.getCompanyId() != null) {
			sql.append(" and tp.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(condition.getCompanyId()));
		}else {
			sql.append(" and tp.companyId in "+companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
		}
		if (condition.getStartTime() != null) {
			sql.append(" and tp.reportDate >= '"+DateUtil.formatYYYYMMDDHHMMSS(condition.getStartTime())+"'");
		}
		if (condition.getEndTime() != null) {
			sql.append(" and tp.reportDate < '"+DateUtil.formatLocalYYYYMMDDHHMMSS(condition.getEndTime(), 1)+"'");
		}
		sql.append(" group by basicItemId,vmCode order by tp.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("商品销售列表sql>>>:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getRow();
			}
			long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
			rs = pst.executeQuery();
			List<MerchandiseSalesStatisticsBean> list = Lists.newArrayList();
			int id=0;
			while (rs.next()) {
				id++;
				MerchandiseSalesStatisticsBean bean = new MerchandiseSalesStatisticsBean();
				bean.setId(id);
				bean.setBasicItemId(rs.getInt("basicItemId"));
				bean.setItemName(rs.getString("name"));
				bean.setBarCode(rs.getString("barCode"));
				bean.setVmCode(rs.getString("vmCode"));
				bean.setSalesQuantity(rs.getInt("salesQuantity"));
				bean.setAvgPrice(rs.getDouble("avgPrice"));
				bean.setAllPrice(rs.getDouble("allPrice"));
				bean.setTime(rs.getString("createTime").substring(0, rs.getString("createTime").length()-2));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
			}
			data.setCurrentPage(condition.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<MerchandiseSalesStatisticsImpl>----<listPage>----end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return data;
		} finally {
			try {
				rs.close();
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
