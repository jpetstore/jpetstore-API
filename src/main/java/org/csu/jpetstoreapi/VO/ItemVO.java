package org.csu.jpetstoreapi.VO;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemVO {
    //Item表中的字段
    private String itemId;
    private String productId;
    private BigDecimal listPrice;
    private BigDecimal unitCost;
    private int supplierId;
    private String status;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;

    //Item所属product的属性
    private String categoryId;
    private String productName;
    private String productDescription;

    //Item的库存，来自inventory表
    private int quantity;
}