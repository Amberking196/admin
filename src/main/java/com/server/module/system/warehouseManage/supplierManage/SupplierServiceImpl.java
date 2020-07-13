package com.server.module.system.warehouseManage.supplierManage;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-05-21 16:58:08
 */ 
@Service
public class  SupplierServiceImpl  implements SupplierService{
	public static Logger log = LogManager.getLogger(SupplierServiceImpl.class); 	 

	@Autowired
	private SupplierDao supplierDaoImpl;
	public ReturnDataUtil listPage(SupplierForm supplierForm){
		log.info("<SupplierServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage=supplierDaoImpl.listPage(supplierForm);
		log.info("<SupplierServiceImpl>------<listPage>-----end");
		return listPage;
	}
	
	public SupplierBean add(SupplierBean entity) {
		log.info("<SupplierServiceImpl>------<add>-----start");
		SupplierBean supplierBean= supplierDaoImpl.insert(entity);
		log.info("<SupplierServiceImpl>------<add>-----end");
		return supplierBean;
	}
	
	public boolean update(SupplierBean entity) {
		log.info("<SupplierServiceImpl>------<update>-----start");
		boolean update=supplierDaoImpl.update(entity);
		log.info("<SupplierServiceImpl>------<update>-----end");
		return update;

	}
	
	public boolean del(Object id) {
		log.info("<SupplierServiceImpl>------<del>-----start");
		boolean del=supplierDaoImpl.delete(id);
		log.info("<SupplierServiceImpl>------<del>-----end");
		return del;

	}
	
	public SupplierBean get(Object id) {
		log.info("<SupplierServiceImpl>------<get>-----start");
		SupplierBean get=supplierDaoImpl.get(id);
		log.info("<SupplierServiceImpl>------<get>-----end");
		return get;

	}

	/**
	 * 模糊搜索供应商
	 */
	@Override
	public List<SupplierBean> findBean(String name) {
		log.info("<SupplierServiceImpl>------<findBean>-----start");
		List<SupplierBean> list = supplierDaoImpl.findBean(name);
		log.info("<SupplierServiceImpl>------<findBean>-----end");
		return list;
	}

	public List<SupplierVoForSelect> listForSelect(){
		return supplierDaoImpl.listForSelect();
	}

	@Override
	public ReturnDataUtil findAll() {
		log.info("<SupplierServiceImpl>------<findAll>-----start");
		ReturnDataUtil listPage=supplierDaoImpl.findAll();
		log.info("<SupplierServiceImpl>------<findAll>-----end");
		return listPage;
	}
}
	
