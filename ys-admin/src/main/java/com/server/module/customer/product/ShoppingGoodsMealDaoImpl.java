package com.server.module.customer.product;

import com.server.common.persistence.BaseDao;
import com.server.module.customer.product.shoppingGoodsProduct.ShoppingGoodsProductBean;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Repository
public class ShoppingGoodsMealDaoImpl extends BaseDao<ShoppingGoodsMeal> implements ShoppingGoodsMealDao {
    @Override
    public boolean update(ShoppingGoodsMeal entity) {
        return super.update(entity);
    }

    @Override
    public ShoppingGoodsMeal insert(ShoppingGoodsMeal entity) {
        return super.insert(entity);
    }


    @Override
    public boolean delete(Object id) {
        log.info("<ShoppingGoodsDaoImpl>-----<delete>----start");
        StringBuilder sql = new StringBuilder();
        sql.append(" delete shopping_goods_meal where id in (" + id + ") ");
        Connection conn = null;
        PreparedStatement pst = null;
        log.info("删除商城商品》sql:" + sql.toString());
        try {
            conn = openConnection();
            pst = conn.prepareStatement(sql.toString());
            int executeUpdate = pst.executeUpdate();
            if (executeUpdate > 0) {
                log.info("<ShoppingGoodsDaoImpl>-----<delete>----end");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.closeConnection(null, pst, conn);
        }
        log.info("<ShoppingGoodsDaoImpl>-----<delete>----end");
        return false;
    }
    
    @Override
	public ShoppingGoodsMeal get(Object id) {
		return super.get(id);
	}
    //public ShoppingGoodsMeal get(Object id);


}
