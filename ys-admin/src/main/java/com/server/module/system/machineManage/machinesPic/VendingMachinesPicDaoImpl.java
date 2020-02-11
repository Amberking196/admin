package com.server.module.system.machineManage.machinesPic;

import com.server.common.persistence.BaseDao;
import com.server.dbpool.DBPool;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesBean;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesCondition;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.server.common.persistence.BaseDao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Lists;
import com.server.util.ReturnDataUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
@Repository
public class VendingMachinesPicDaoImpl extends BaseDao<VendingMachinesPicBean>
		implements VendingMachinesPicDao {

	private static Logger log = LogManager.getLogger(VendingMachinesPicDaoImpl.class);
	
	@Autowired
	private CompanyDao companyDao;

	/**
	 * 查询售货机广告
	 */
	public ReturnDataUtil listPage(VendingMachinesPic vendingMachinesPic) {
		log.info("<VendingPicDaoImpl>----------<listPage>-------start");
		ReturnDataUtil data = new ReturnDataUtil();
		StringBuilder sql = new StringBuilder();
		sql.append("select vma.basicItemId,vma.picType,vma.target,vma.id,vma.companyId,vma.homeImg,vma.createTime,vma.createUser,vma.updateTime,vma.updateUser,vma.deleteFlag ");
		sql.append(",vma.state,vma.height,vma.width,vma.picType,vma.name  from vending_machines_pic vma");
		
		sql.append(" where vma.deleteFlag=0");
		if(vendingMachinesPic.getCompanyId()!=null) {
			sql.append(" and vma.companyId='"+vendingMachinesPic.getCompanyId()+"'");
		}
		if(StringUtils.isNotBlank(vendingMachinesPic.getState())) {
			sql.append(" and vma.state='"+vendingMachinesPic.getState()+"'");
		}
		sql.append(" order by vma.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("查询售货机广告sql语句："+sql.toString());
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(super.countSql(sql.toString()));
			rs = pst.executeQuery();
			long count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(vendingMachinesPic.getIsShowAll()==0) {
				long off = (vendingMachinesPic.getCurrentPage() - 1) * vendingMachinesPic.getPageSize();
				pst = conn.prepareStatement(sql.toString() + " limit " + off + "," + vendingMachinesPic.getPageSize());
			}else {
				pst = conn.prepareStatement(sql.toString());
			}
			rs = pst.executeQuery();
			List<VendingMachinesPicBean> list = Lists.newArrayList();
			while (rs.next()) {
				VendingMachinesPicBean bean = new VendingMachinesPicBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setHomeImg(rs.getString("homeImg"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				//bean.setCompanyName(rs.getString("companyName"));
				//bean.setTarget(rs.getInt("target"));
				bean.setName(rs.getString("name"));
				//bean.setItemName(rs.getString("itemName"));
				bean.setHeight(rs.getInt("height"));
				bean.setWidth(rs.getInt("width"));
				bean.setState(rs.getInt("state"));
				bean.setPicType(rs.getInt("picType"));
				//bean.setBasicItemId(rs.getLong("basicItemId"));
				list.add(bean);
			}
			data.setCurrentPage(vendingMachinesPic.getCurrentPage());
			data.setTotal(count);
			data.setReturnObject(list);
			data.setStatus(1);
			log.info("<VendingPicDaoImpl>----------<listPage>-------end");
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<VendingPicDaoImpl>----------<listPage>-------end");
			return data;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	public VendingMachinesPicBean get(Object id) {
		return super.get(id);
	}

	/**
	 * 删除售货机广告
	 */
	public boolean delete(Object id) {
		log.info("<VendingPicDaoImpl>-----<delete>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" update  vending_machines_Pic set deleteFlag= 1 where id='"+id+"' ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("删除售货机广告sql语句："+sql.toString());
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			int executeUpdate = pst.executeUpdate();
			if(executeUpdate>0) {
				log.info("<VendingPicDaoImpl>-----<delete>-----end");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		return false;
	}

	/**
	 * 修改售货机广告
	 */
	public boolean update(VendingMachinesPicBean entity) {
		log.info("<VendingPicDaoImpl>----------<update>-------start");
		boolean update = super.update(entity);
		log.info("<VendingPicDaoImpl>----------<update>-------end");
		return update;
	}

	/**
	 * 增加售货机广告
	 */
	public VendingMachinesPicBean insert(VendingMachinesPicBean entity) {
		log.info("<VendingPicDaoImpl>----------<insert>-------start");
		VendingMachinesPicBean bean = super.insert(entity);
		log.info("<VendingPicDaoImpl>----------<insert>-------end");
		return bean;
	}

	/**
	 * 手机端广告页查询
	 */
	public List<VendingMachinesPicBean> list(VendingMachinesPic vendingMachinesPic) {
		log.info("<VendingPicDaoImpl>----------<list>-------start");
		List<VendingMachinesPicBean> list = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append("select vma.id,vma.companyId,vma.homeImg,vma.themeImg,vma.homeUrl,vma.themeUrlOne,vma.themeUrlTwo,vma.createTime,vma.createUser,vma.updateTime,vma.updateUser,vma.deleteFlag,vma.basicItemId ");
		sql.append("  from vending_machines_Pic vma  ");
		sql.append("  where vma.deleteFlag=0 ");
		if(StringUtil.isNotBlank(vendingMachinesPic.getVmCode())) {
			sql.append("  and FIND_IN_SET((select companyId from vending_machines_info where code='"+vendingMachinesPic.getVmCode()+"'),getChildList(vma.companyId)) ");
		}
		sql.append(" order by vma.createTime desc ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		if (showSql) {
			log.info("手机端广告页查询sql语句："+sql.toString());
		}
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				VendingMachinesPicBean bean = new VendingMachinesPicBean();
				bean.setId(rs.getLong("id"));
				bean.setCompanyId(rs.getLong("companyId"));
				bean.setHomeImg(rs.getString("homeImg"));
				bean.setCreateTime(rs.getDate("createTime"));
				bean.setCreateUser(rs.getLong("createUser"));
				bean.setUpdateTime(rs.getDate("updateTime"));
				bean.setUpdateUser(rs.getLong("updateUser"));
				bean.setDeleteFlag(rs.getInt("deleteFlag"));
				list.add(bean);
			}
			log.info("<VendingPicDaoImpl>----------<list>-------end");
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("<VendingPicDaoImpl>----------<list>-------end");
			return list;
		} finally {
			this.closeConnection(rs, pst, conn);
		}
	}

	@Override
	public String findVendingSlideshow(String vmCode) {
		log.info("<VendingPicDaoImpl>----------<findVendingSlideshow>-------start");
		StringBuilder sql = new StringBuilder();
		sql.append("select vma.id,vma.companyId,vma.homeImg,vma.themeImg,vma.homeUrl,vma.themeUrlOne,vma.themeUrlTwo,vma.createTime,vma.createUser,vma.updateTime,vma.updateUser,vma.deleteFlag ");
		sql.append("  from vending_machines_Pic vma  ");
		sql.append("  where vma.deleteFlag=0  and vma.state=0 and vma.advType=1");
		sql.append("  and FIND_IN_SET((select companyId from vending_machines_info where code='"+vmCode+"'),getChildList(vma.companyId)) ");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("查询机器首页的充值图片sql语句："+sql.toString());
		String homeImg=null;
		try {
			conn = openConnection();
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				homeImg=rs.getString("homeImg");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			this.closeConnection(rs, pst, conn);
		}
		log.info("<VendingPicDaoImpl>----------<findVendingSlideshow>-------end");
		return homeImg;
	}
	
	@Override
	public List<VendingMachinesPicBean> findVendingPicMachinesBean(String vmCode) {
		log.info("<VendingPicDaoImpl>-----<findVendingPicMachinesBean>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append("select v.vmCode,a.id PicId ,a.homeImg,a.themeImg,a.staySecond,a.showStyle,a.height,a.width ");
		sql.append(" from vending_Pic_machines v left join vending_machines_Pic a on v.PicId=a.id ");
		sql.append(" where a.state=0  and a.deleteFlag=0 and a.advType=0 and v.vmCode='"+vmCode+"' ");
		sql.append(" order by a.createTime desc  ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("查询售货机广告sql语句"+sql.toString());
        List<VendingMachinesPicBean> list= Lists.newArrayList();
        VendingMachinesPicBean bean=null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs!=null&&rs.next()) {
            	bean=new VendingMachinesPicBean();
            	bean.setHomeImg(rs.getString("homeImg"));
            	bean.setHeight(rs.getInt("height"));
            	bean.setWidth(rs.getInt("width"));
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.closeConnection(rs, pst, conn);
        }
        log.info("<VendingPicDaoImpl>-----<findVendingPicMachinesBean>-----end");
		return list;
	}

	@Override
	public boolean findPicMachinesBeanByCompanyIdAndItemId(Long companyId,Long basicItemId) {
		log.info("<VendingPicDaoImpl>-----<findPicMachinesBeanByCompanyId>-----start");
		StringBuilder sql = new StringBuilder();
		sql.append(" select *  from vending_machines_Pic ");
		sql.append(" where basicItemId="+basicItemId+"  and deleteFlag=0 and  FIND_IN_SET(companyId,getChildList("+companyId+")) ");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        log.info("查询公司下  是否已经存在公司弹窗了"+sql.toString());
        boolean flag=false;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs!=null&&rs.next()) {
            	flag=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.closeConnection(rs, pst, conn);
        }
        log.info("<VendingPicDaoImpl>-----<findPicMachinesBeanByCompanyId>-----end");
		return flag;
	}
	
    @Override
    public void addAll(Long advertisingId, List<String> codeList) {
        for (int i = 0; i < codeList.size(); i++) {
        	VendingPicMachinesBean entity = new VendingPicMachinesBean();
            entity.setPicId(advertisingId);
            entity.setVmCode(codeList.get(i));
            insertPicVmCode(entity);
        }
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            if(id==null)
                continue;
            VendingPicMachinesBean entity = new VendingPicMachinesBean();
            entity.setId(id);
            deletePicVmCode(entity);
        }
    }
    public List<VendingPicMachinesBean> list(VendingPicMachinesCondition condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.code as vmCode,a.id,a.picId,i.locatoinName as address FROM vending_machines_info i LEFT JOIN (select * from vending_pic_machines where 1=1 and picId="+condition.getPicId()+") a ON i.code=a.vmCode where 1=1 and i.state=20001 ");

        //(SELECT * FROM vending_advertising_machines WHERE advertisingId=1) a
        /*if(condition.getAdvertisingId()!=null){
            sql.append(" and advertisingId="+condition.getAdvertisingId());
        }*/
        if(StringUtil.isNotBlank(condition.getVmCode())){
            sql.append(" and i.code='"+condition.getVmCode()+"'");
        }else{
            if(condition.getAreaId()!=null){
                sql.append(" and i.areaId="+condition.getAreaId());
            }else{
                if(condition.getCompanyId()!=null){
                    String sqlIn=companyDao.findAllSonCompanyIdForInSql(condition.getCompanyId());
                    sql.append(" and i.companyId in "+sqlIn);
                }
            }
        }
        List<VendingPicMachinesBean> list = Lists.newArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
            	VendingPicMachinesBean bean = new VendingPicMachinesBean();
                bean.setId(rs.getLong("id"));
                bean.setPicId(rs.getLong("picId"));
                bean.setVmCode(rs.getString("vmCode"));
                bean.setAddress(rs.getString("address"));
                if(condition.getRange()==0)
                   list.add(bean);
                else if(condition.getRange()==1){
                    if(bean.getId()!=0)
                        list.add(bean);
                }else{
                    if(bean.getId()==0){
                        list.add(bean);
                    }
                }
            }
            if (showSql) {
                log.info(sql);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return list;
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
    @Override
	public boolean insertPicVmCode(VendingPicMachinesBean entity) {
    	 StringBuilder sql = new StringBuilder();
         sql.append("insert into vending_pic_machines(picId,vmCode) values("+entity.getPicId()+","+entity.getVmCode()+")");
         Connection conn = null;
         PreparedStatement pst = null;
         Integer rs = 0;
         try {
             conn = openConnection();
             pst = conn.prepareStatement(sql.toString());
             rs = pst.executeUpdate();
             while (rs!=null && rs>0) {
            	 return true;
             }
             if (showSql) {
                 log.info(sql);
             }
             return false;
         } catch (SQLException e) {
             e.printStackTrace();
             log.error(e.getMessage());
             return false;
         } finally {
             try {
                 pst.close();
                 closeConnection(conn);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

    }
    
    @Override
	public boolean deletePicVmCode(VendingPicMachinesBean entity) {
    	 StringBuilder sql = new StringBuilder();
         sql.append("delete from vending_pic_machines where id = "+entity.getId());
         Connection conn = null;
         PreparedStatement pst = null;
         Integer rs = 0;
         try {
             conn = openConnection();
             pst = conn.prepareStatement(sql.toString());
             rs = pst.executeUpdate();
             while (rs!=null && rs>0) {
            	 return true;
             }
             if (showSql) {
                 log.info(sql);
             }
             return false;
         } catch (SQLException e) {
             e.printStackTrace();
             log.error(e.getMessage());
             return false;
         } finally {
             try {
                 pst.close();
                 closeConnection(conn);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

    }
    
	public VendingPicMachinesBean getPicVmCode(Object id) {
		 StringBuilder sql = new StringBuilder();
	        sql.append("SELECT * from vending_pic_machines where id ="+id);
	        Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
        	VendingPicMachinesBean bean = new VendingPicMachinesBean();
	        try {
	            conn = openConnection();
	            pst = conn.prepareStatement(sql.toString());
	            rs = pst.executeQuery();
	            while (rs.next()) {
	                bean.setId(rs.getLong("id"));
	                bean.setPicId(rs.getLong("picId"));
	                bean.setVmCode(rs.getString("vmCode"));
	            }
	            if (showSql) {
	                log.info(sql);
	            }

	            return bean;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            return bean;
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

	public VendingMachinesPicDto checkRepeat(Long basicItemId) {
		StringBuilder sql = new StringBuilder();
	    sql.append(" select vmp.companyId,vmCode,vmi.companyId as vmiCompanyId from vending_machines_pic vmp"); 
	    sql.append(" left JOIN vending_pic_machines vpm on vpm.picId = vmp.id");
	    sql.append(" left join vending_machines_info vmi on vmi.code = vpm.vmCode");
	    sql.append(" where vmp.basicItemId='"+basicItemId+"' and vmp.deleteFlag=0");
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    VendingMachinesPicDto bean = new VendingMachinesPicDto();
	    Set<Integer> companyIdsSet=new HashSet();
	    Set<Integer> vmiCompanyIdsSet=new HashSet();
	    Set<String> vmCodesSet=new HashSet();
	    bean.setVmCodes(vmCodesSet);
	    bean.setVmiCompanyIds(vmiCompanyIdsSet);
	    bean.setCompanyIds(companyIdsSet);

	    try {
	            conn = openConnection();
	            pst = conn.prepareStatement(sql.toString());
	            rs = pst.executeQuery();
	            if (showSql) {
	                log.info(sql);
	            }
	            while (rs.next()) {
	            	if(rs.getInt("companyId")!=0) {
	            		companyIdsSet.add(rs.getInt("companyId"));
	            	}
	            	if(StringUtils.isNotBlank(rs.getString("vmCode"))) {
	            		vmCodesSet.add(rs.getString("vmCode"));
	            	}
	            	if(rs.getInt("vmiCompanyId")!=0) {
	            		vmiCompanyIdsSet.add(rs.getInt("vmiCompanyId"));
	            	}
	            }
	 

	            return bean;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            return bean;
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
	
	public String  getChildList(Long companyId) {
		StringBuilder sql = new StringBuilder();
	    sql.append("select getChildList("+companyId+") as companyIds from dual"); 

	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try {
	            conn = openConnection();
	            pst = conn.prepareStatement(sql.toString());
	            rs = pst.executeQuery();
	            while (rs.next()) {
	            	return rs.getString("companyIds");
	            }
	            if (showSql) {
	                log.info(sql);
	            }

	            return null;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            return null;
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
	
	public VendingMachinesPicDto vmCodeTranCompanyId(String vmCodes){
		StringBuilder sql = new StringBuilder();
	    sql.append("select code,companyId from vending_machines_info where 1=1 and code in ("+vmCodes+")"); 

	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    VendingMachinesPicDto bean = new VendingMachinesPicDto();
	    Set<Integer> companyIdsSet=new HashSet();
	    bean.setCompanyIds(companyIdsSet);
	    try {
	            conn = openConnection();
	            pst = conn.prepareStatement(sql.toString());
	            rs = pst.executeQuery();
	            while (rs.next()) {
	            	if(rs.getInt("companyId")!=0) {
	            		bean.getCompanyIds().add(rs.getInt("companyId"));
	            	}	           
	            }
	            if (showSql) {
	                log.info(sql);
	            }

	            return bean;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            log.error(e.getMessage());
	            return bean;
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
