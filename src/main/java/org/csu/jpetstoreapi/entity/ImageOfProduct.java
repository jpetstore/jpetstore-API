package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("imageofproduct")
public class ImageOfProduct {
    @TableField(value ="productid" )
    private String productId;
    @TableField(value ="image" )
    private byte[] image;
}
