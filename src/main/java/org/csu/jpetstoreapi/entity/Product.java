package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product")
public class Product {

    @TableId(value = "productid",type = IdType.INPUT)
    private String productId;
    @TableField(value ="category" )
    private String categoryId;
    @TableField(value ="name")
    private String name;
    @TableField(value = "descn")
    private String description;
}
