package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.relational.core.sql.In;

@Data
@TableName("review")
public class Review {

    @TableField(value = "id")
    private Integer id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "review")
    private String review;

    @TableField(value = "itemid")
    private String itemid;

    @TableField(value = "grade")
    private Integer grade;

    @TableField(value = "time")
    private String time;

    @TableField(value = "top")
    private Integer top;
}
