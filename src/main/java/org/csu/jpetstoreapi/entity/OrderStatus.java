package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("orderstatus")
public class OrderStatus {

    @TableId(value = "orderId",type = IdType.INPUT)
    private int orderId;

    @TableField(value ="linenum" )
    private int linenum;

    @TableField(value ="timestamp" )
    private Date timestamp;

    @TableField(value ="status" )
    private String status;
}
