package org.csu.jpetstoreapi.service;

import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.UserInfo;

public interface UserInfoService {

    CommonResponse<UserInfo> login(String id, String password);

    CommonResponse<UserInfo> getAccountByUsernameAndPassword(String id,String password);

    UserInfo findUserById(String id);
    CommonResponse<UserInfo> insertUser(UserInfo userInfo);
    CommonResponse<UserInfo> updateUser(UserInfo userInfo);

    UserInfo findUserByPhone(String phone);

    CommonResponse<UserInfo> updateUserById(UserInfo userInfo);

    CommonResponse<UserInfo> updateUserById_2(UserInfo userInfo);
}
