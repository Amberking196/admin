package com.server.module.system.messageManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.server.common.persistence.BaseDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.machineList.MachinesInfoAndBaseDto;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoCondition;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
@Repository
public class MessageManagementDaoImpl extends BaseDao<CustomerMessageBean> implements MessageManagementDao {
	public static Logger log = LogManager.getLogger(MessageManagementDaoImpl.class);
	 @Autowired
	  private CompanyDao companyDaoImpl;
	@Override
	//留言查询
	public ReturnDataUtil messageListPage(MessageManagementForm form) {
		log.info("<MessageManagementDaoImpl>------<messageListPage>----start");
		//创建一个data
		ReturnDataUtil data=new ReturnDataUtil();
		//书写sql语句
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,vmCode,type,phone,content,picName,video,createTime,createUser,updateTime,updateUser,deleteFlag from customer_message c where 1=1 and deleteFlag=0 ");
		List<Object> plist = Lists.newArrayList();//创建预处理容器
		  if (StringUtils.isNotBlank(form.getVmCode())) {
	            sql.append(" and c.vmCode = ? ");
	            plist.add( form.getVmCode());
	        }else {//如果没有vmCode 就直接返回
	        	data.setMessage("参数错误");
	        	data.setStatus(505);
	        	return data;
	        }
		  sql.append("order by c.updateTime desc");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("用户留言查询列表 SQL:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			//添加预处理参数
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();//执行sql语句，查询总数
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			long off = (form.getCurrentPage() - 1) * form.getPageSize();
			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + form.getPageSize());//分页查询
			//添加预处理参数
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();//执行sql语句，获取分页查询结果
			List<CustomerMessageBean> list = Lists.newArrayList();
			while (rs.next()) {
				//封装查询结果
				CustomerMessageBean bean = new CustomerMessageBean();
				bean.setId(rs.getLong("id"));
				bean.setPhone(rs.getString("phone"));
				bean.setContent(rs.getString("content"));
				bean.setPicName(rs.getString("picName"));
				bean.setVideo(rs.getString("video"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				bean.setVmCode(rs.getString("vmCode"));
				//对留言类型进行处理
				if(rs.getShort("type")==0) {
					//好评
					bean.setType("好评");
				}else if(rs.getShort("type")==1) {
					//差评
					bean.setType("差评");
				}else {
					bean.setType("投诉");
				}
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(form.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CustomerMessageDaoImpl>----<listPage>-------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<CustomerMessageDaoImpl>----<listPage>-------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}
	@Override
	//查询售货机列表
	public ReturnDataUtil listPage(VendingMachinesInfoCondition condition) {
		log.info("<MessageManagementDaoImpl>------<listPage>----start");
		//创建一个data
		ReturnDataUtil data=new ReturnDataUtil();
		//书写sql语句
		StringBuilder sql = new StringBuilder();
		sql.append(
	               "select  i.code,i.companyId,i.locatoinName");
	        sql.append(
	               " from customer_message c INNER JOIN vending_machines_info i on c.vmCode=i.code ");
	       sql.append(
	               " where 1=1 and c.deleteFlag=0 ");
	     
	       sql.append(" and i.companyId in "
	               + companyDaoImpl.findAllSonCompanyIdForInSql(UserUtils.getUser().getCompanyId()));
	       List<Object> plist = Lists.newArrayList();
	        if (condition.getCompanyId() != null && condition.getCompanyId() > 1) {
	            sql.append(" and i.companyId in " + companyDaoImpl.findAllSonCompanyIdForInSql(condition.getCompanyId()));
	        }
	        if (StringUtil.isNotEmpty(condition.getCode())) {
	            sql.append(" and i.code like ? ");
	            plist.add("%" + condition.getCode() + "%");
	        }
	        if (StringUtil.isNotEmpty(condition.getLocatoinName())) {
	            sql.append(" and i.locatoinName like ? ");
	            plist.add("%" + condition.getLocatoinName() + "%");
	        }
	        if (condition.getIsShowAll() == 1) {
	            sql.append(" and i.state=20001 ");//售货机状态正常的
	        }
	        sql.append(" GROUP BY i.code ");//分组去掉重复的售货机
	        sql.append("order by c.updateTime desc");//根据留言更新时间进行倒叙
	        Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
	        StringBuilder sb=new StringBuilder();
	        sb.append("select count(1) from ("+sql.toString()+") as co ");
	        log.info("数量：" + sb.toString());
	        log.info("留言售货机列表sql语句：" + sql.toString());
	        try {
	            conn = openConnection();
	            pst = conn.prepareStatement(sb.toString());
	            //添加预处理参数
	            if (plist != null && plist.size() > 0)
	                for (int i = 0; i < plist.size(); i++) {
	                    pst.setObject(i + 1, plist.get(i));
	                }
	            rs = pst.executeQuery();//执行sql，查询总数
	            long count = 0;
	            while (rs.next()) {
	                count = rs.getInt(1);
	            }
	            if (condition.getIsShowAll() == 1) {
	                pst = conn.prepareStatement(sql.toString());
	            } else {
	                long off = (condition.getCurrentPage() - 1) * condition.getPageSize();
	                pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + condition.getPageSize());
	            }
	            //添加预处理参数
	            if (plist != null && plist.size() > 0)
	                for (int i = 0; i < plist.size(); i++) {
	                    pst.setObject(i + 1, plist.get(i));
	                }
	            rs = pst.executeQuery();//执行sql，查询分页结果
	            List<MessageVmBean> list = Lists.newArrayList();
	            while (rs.next()) {
	            	MessageVmBean bean = new MessageVmBean();
	                bean.setCode(rs.getString("code"));
	                bean.setCompanyId(rs.getInt("companyId"));
	                
	                bean.setLocatoinName(rs.getString("locatoinName"));
	                CompanyBean companyBean = companyDaoImpl.findCompanyById(rs.getInt("companyId"));
	                bean.setCompanyName(companyBean.getName());
	                list.add(bean);
	            }
	            if (showSql) {
	                log.info(sql);
	                log.info(plist.toString());
	            }
	            data.setCurrentPage(condition.getCurrentPage());
	            data.setPageSize(condition.getPageSize());
	            data.setTotal(count);
	            data.setReturnObject(list);
	            data.setStatus(1);
	            log.info("<MessageManagementDaoImpl--listPage--end>");
	            return data;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            data.setMessage("查询失败");
	            data.setStatus(500);
	            return data;
	        } finally {
	            this.closeConnection(rs, pst, conn);
	        }
	}
	@Override
	//评论查询
	public ReturnDataUtil messageCommentListPage(MessageCommentForm form) {
		log.info("<MessageManagementDaoImpl>------<messageCommentListPage>----start");
		//创建一个data
		ReturnDataUtil data=new ReturnDataUtil();
		//书写sql语句
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select id,messageId,commentPhone,commentContent,byCommentPhone,createTime,createUser,updateTime,updateUser,deleteFlag from customer_message_comment c where 1=1  and deleteFlag=0 ");
		List<Object> plist = Lists.newArrayList();//创建预处理容器
		  if (form.getMessageId()!=null) {
	            sql.append(" and c.messageId = ? ");
	            plist.add( form.getMessageId());
	        }else {//如果没有vmCode 就直接返回
	        	data.setMessage("参数错误");
	        	data.setStatus(505);
	        	return data;
	        }
		  sql.append(" order by c.updateTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("用户留言查询列表 SQL:"+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			//添加预处理参数
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();//执行sql语句，查询总数
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
//			long off = (form.getCurrentPage() - 1) * form.getPageSize();
//			pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + form.getPageSize());//分页查询
			pst=conn.prepareStatement(sql.toString());
			//添加预处理参数
			if (plist != null && plist.size() > 0)
				for (int i = 0; i < plist.size(); i++) {
					pst.setObject(i + 1, plist.get(i));
				}
			rs = pst.executeQuery();//执行sql语句，获取分页查询结果
			List<MessageCommentBean> list = Lists.newArrayList();
			while (rs.next()) {
				//封装查询结果
				MessageCommentBean bean = new MessageCommentBean();
				bean.setId(rs.getInt("id"));
				bean.setMessageId(rs.getInt("messageId"));
				bean.setCommentPhone(rs.getString("commentPhone"));
				bean.setByCommentPhone(rs.getString("byCommentPhone"));
				bean.setCommentContent(rs.getString("commentContent"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getInt("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getInt("updateUser"));
				bean.setDeleteFlag(rs.getShort("deleteFlag"));
				list.add(bean);
			}
			if (showSql) {
				log.info(sql);
				log.info(plist.toString());
			}
			data.setCurrentPage(form.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<CustomerMessageDaoImpl>----<messageCommentListPage>-------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			 data.setMessage("查询失败");
	         data.setStatus(500);
			log.info("<CustomerMessageDaoImpl>----<messageCommentListPage>-------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

}
