package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@TableName("cart")
@Getter
public class Cart {

    @TableId(value = "username",type = IdType.INPUT)
    private String username;

    @TableField(value = "itemid")
    private String itemId;

    @TableField(value = "instock")
    private boolean instock;

    @TableField(value = "quantity")
    private int quantity;

    @TableField(value = "totalcost")
    private BigDecimal totalCost;

    @TableField(value = "pay")
    private boolean pay;

    public boolean getPay() {
        return pay;
    }


}