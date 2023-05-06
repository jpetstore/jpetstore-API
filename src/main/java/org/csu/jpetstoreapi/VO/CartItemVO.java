package org.csu.jpetstoreapi.VO;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemVO {

    private String username;
    private ItemVO itemVO;
    private boolean instock;
    private int quantity;
    private BigDecimal totalCost;
    private boolean pay;

    //计算当前购物车商品总价
    private void calculateTotal(){
        if(itemVO != null && itemVO.getListPrice() != null){
            totalCost = itemVO.getListPrice().multiply(new BigDecimal(quantity));
        }
        else{
            totalCost = null;
        }
    }

    //购物车商品数量加1
    public void incrementQuantity() {
        quantity++;
        calculateTotal();
    }

    //更新购物车商品数量
    public void updateQuantity(int quantity){
        this.quantity = quantity;
        calculateTotal();
    }

}