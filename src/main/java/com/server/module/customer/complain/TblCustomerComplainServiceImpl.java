package com.server.module.customer.complain;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.userManage.CustomerDao;
import com.server.util.EmojiUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: yjr create time: 2018-08-17 08:48:16
 */
@Service
public class TblCustomerComplainServiceImpl implements TblCustomerComplainService {

	private static Logger log = LogManager.getLogger(TblCustomerComplainServiceImpl.class);
	@Autowired
	private TblCustomerComplainDao tblCustomerComplainDaoImpl;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private CustomerDao customerDaoImpl;
	
	/**
	 * 用户故障申报列表
	 */
	public ReturnDataUtil listPage(TblCustomerComplainForm tblCustomerComplainForm) {
		log.info("<TblCustomerComplainServiceImpl>----<listPage>------start");
		ReturnDataUtil listPage = tblCustomerComplainDaoImpl.listPage(tblCustomerComplainForm);
		log.info("<TblCustomerComplainServiceImpl>----<listPage>------end");
		return listPage;
	}

	/**
	 * 增加故障申报
	 */
	public TblCustomerComplainBean add(TblCustomerComplainBean entity) {
		log.info("<CustomerMessageServiceImpl>----<add>------start");
		// 得到当前 投诉的用户 的手机号
		Long customerId = userUtils.getSmsUser().getId();
		if(!(StringUtil.isNotBlank(entity.getPhone()))){
			String phone = customerDaoImpl.findCustomerById(customerId).getPhone();
			entity.setPhone(phone);
		}
		if(!(StringUtil.isNotBlank(entity.getContent()))){
			entity.setContent(EmojiUtil.getString(entity.getContent()));
		}
		if(StringUtil.isBlank(entity.getVmCode()) || entity.getVmCode().equals("1988000080") || entity.getVmCode().equals("undefined")){
			//售货机编号异常时查询用户最近一次购买售货机的编号
			String vmCode = customerDaoImpl.getCustomerLastVmcode(customerId);
			if(vmCode!=null) {
				entity.setVmCode(vmCode);
			}
		}
		entity.setCustomerId(customerId);
		entity.setCreateUser(customerId);
		TblCustomerComplainBean tblCustomerComplainBean = tblCustomerComplainDaoImpl.insert(entity);
		log.info("<CustomerMessageServiceImpl>----<add>------end");
		return tblCustomerComplainBean;
	}

	public boolean update(TblCustomerComplainBean entity) {
		return tblCustomerComplainDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return tblCustomerComplainDaoImpl.delete(id);
	}

	

	public TblCustomerComplainBean get(Object id) {
		return tblCustomerComplainDaoImpl.get(id);
	}

	public List<TblCustomerComplainBean> myDeclaration(TblCustomerComplainForm tblCustomerComplainForm){
		log.info("<CustomerMessageServiceImpl>----<myDeclaration>------start");
		List<TblCustomerComplainBean> list = tblCustomerComplainDaoImpl.myDeclaration(tblCustomerComplainForm);
		log.info("<CustomerMessageServiceImpl>----<myDeclaration>------end");
		return  list;
	}

	public  Integer findComplaintsNumberById(){
		log.info("<CustomerMessageServiceImpl>----<findComplaintsNumberById>------start");
		Integer count = tblCustomerComplainDaoImpl.findComplaintsNumberById();
		log.info("<CustomerMessageServiceImpl>----<findComplaintsNumberById>------end");
		return count;
	}
}
