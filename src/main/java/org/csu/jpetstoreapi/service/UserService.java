package org.csu.jpetstoreapi.service;

import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.User;

public interface UserService {

    CommonResponse<User> login(String id, String password);

    CommonResponse<User> insertUser(User user);
    CommonResponse<User> getAccountByUsernameAndPassword(String username,String password);
    User findUserById(String id);
    CommonResponse<User> updateUser(User user);
    User findUserByPhone(String phone);

    CommonResponse<User> updateUserById(User user);
    CommonResponse<User> updateUserById_2(User user);

}
