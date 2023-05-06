package org.csu.jpetstoreapi.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LineItemVO {

    private int orderId;
    private int lineNumber;
    private int quantity;
    private String itemId;
    private BigDecimal unitPrice;
    private ItemVO item;
    private BigDecimal total;

    public LineItemVO(int lineNumber, CartItemVO cartItem) {
        this.lineNumber = lineNumber;
        this.quantity = cartItem.getQuantity();
        this.itemId = cartItem.getItemVO().getItemId();
        this.unitPrice = cartItem.getItemVO().getListPrice();
        this.item = cartItem.getItemVO();
        this.total=BigDecimal.valueOf(this.quantity * this.unitPrice.floatValue());

    }

    private void calculateTotal() {
        if (item != null && item.getListPrice() != null) {
            total = item.getListPrice().multiply(new BigDecimal(quantity));
        } else {
            total = null;
        }
    }
}