package com.server.module.customer.product;

import lombok.Data;

import java.util.List;
@Data
public class ShoppingGoodsMealList {
    private Long goodsId;
    private List<ShoppingGoodsMeal> shoppingGoodsMeal;
}
