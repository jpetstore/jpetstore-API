package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("lineitem")
public class LineItem {

    @TableId(value = "orderId",type = IdType.INPUT)
    private int orderId;

    @TableField(value ="linenum" )
    private int lineNumber;
    @TableField(value ="itemid" )
    private String itemId;
    @TableField(value ="quantity" )
    private BigDecimal unitPrice;
    @TableField(value ="unitprice" )
    private int quantity;


}