package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("account")
public class User {

//    private Integer id;
//    @NotBlank(message = "用户名不能为空")
//    private String username;
//    @NotBlank(message = "密码不能为空")
//    private String password;
//    @Email(message = "邮箱格式不合法")
//    private String email;
//
//    @Digits(integer = 3,fraction = 0,message = "年龄必须为整数")
//    private int age;
//    private boolean isAdmin;

    private String id;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String status;

}
