package com.server.module.customer.product;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity(tableName = "shopping_goods_meal", id = "id", idGenerate = "auto")
public class ShoppingGoodsMeal {
    private Long id;
    Integer goodsId;
    String name;
    String pic;
    Integer num;
    BigDecimal price;
}
