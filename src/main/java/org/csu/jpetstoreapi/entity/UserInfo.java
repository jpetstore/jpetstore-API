package org.csu.jpetstoreapi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class UserInfo {
    private String id;
//    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String status;
    private String languagepre;
    private String favoritecata;
    private String iflist;
    private String ifbanner;
}
