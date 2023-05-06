package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("inventory")
public class ItemInventory {
    @TableId("itemid")
    private String itemId;
    @TableField("qty")
    private int quantity;
}

