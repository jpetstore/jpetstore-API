package org.csu.jpetstoreapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.User;
import org.csu.jpetstoreapi.entity.UserInfo;
import org.csu.jpetstoreapi.persistence.UserInfoMapper;
import org.csu.jpetstoreapi.persistence.UserMapper;
import org.csu.jpetstoreapi.service.UserService;
import org.csu.jpetstoreapi.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    private static final String salt="1a2b3c4d";

    public CommonResponse<User>login(String id,String password){
        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        queryWrapper.eq("password",password);
        User user=userMapper.selectOne(queryWrapper);

        if(user==null){
            return CommonResponse.createForError("用户名或密码错误");
        }else {
            return CommonResponse.createForSuccess("登录成功",user);
        }
    }

    @Override
    public CommonResponse<User> getAccountByUsernameAndPassword(String id,String password) {
        User user = userMapper.selectById(id);

        String MD5Password = MD5Util.inputPassToDBPass(password,salt);
        System.out.println("*******************************************");
        System.out.println("输入密码： "+password+"  MD5加密后： "+MD5Password);
        System.out.println("*******************************************");
        if(user==null){
            return CommonResponse.createForError("没有该id的user");
        }
        else{
            if(!user.getPassword().equals(MD5Password)){
                return CommonResponse.createForError("password不正确");
            }
            else{
                return CommonResponse.createForSuccess("登录成功",user);
            }
        }
    }

    @Override
    public User findUserById(String id) {
        User user = userMapper.selectById(id);
        return user;
    }


    public CommonResponse<User> insertUser(User user) {
        User user1=findUserById(user.getId());
        if(user.getId()==null||user.getId()==""){
            return CommonResponse.createForError("用户名不能为空");
        } else if (user1!=null) {
            return CommonResponse.createForError("用户名已存在");
        } else if (user.getPassword()==null||user.getPassword()=="") {
            return CommonResponse.createForError("密码不能为空");
        } else if (user.getPhone()==null||user.getPhone()=="") {
            return CommonResponse.createForError("电话不能为空");
        } else{
            String MD5Password = MD5Util.inputPassToDBPass(user.getPassword(),salt);
            user.setPassword(MD5Password);
            userMapper.insert(user);
            return CommonResponse.createForSuccess("注册成功",user);//用户插入成功
        }
    }

    @Override
    public CommonResponse<User> updateUser(User user) {
        if(user==null){
            return CommonResponse.createForError("请先登录");
        }
        if (user.getPassword() == null||user.getPassword()=="") {
            return CommonResponse.createForError("密码不能为空");
        } else if (user.getPhone() == null||user.getPhone()=="") {
            return CommonResponse.createForError("电话不能为空");
        } else {
            String MD5Password = MD5Util.inputPassToDBPass(user.getPassword(), salt);
            user.setPassword(MD5Password);
            userMapper.updateById(user);
            return CommonResponse.createForSuccess("更改信息成功",user);
        }
    }
    @Override
    public CommonResponse<User> updateUserExceptPwd(User user) {
        if(user==null){
            return CommonResponse.createForError("请先登录");
        }
        if (user.getPassword() == null||user.getPassword()=="") {
            return CommonResponse.createForError("密码不能为空");
        } else if (user.getPhone() == null||user.getPhone()=="") {
            return CommonResponse.createForError("电话不能为空");
        } else {
//            String MD5Password = MD5Util.inputPassToDBPass(userInfo.getPassword(), salt);
//            userInfo.setPassword(MD5Password);
            userMapper.updateById(user);
            return CommonResponse.createForSuccess("更改信息成功",user);
        }
    }

    @Override
    public User findUserByPhone(String phone) {

        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        User user=userMapper.selectOne(queryWrapper);

        return user;
    }

    @Override
    public CommonResponse<User> updateUserById(User user) {
        if(user==null){
            return CommonResponse.createForError("请先登录");
        }
        String MD5Password = MD5Util.inputPassToDBPass(user.getPassword(), salt);
        user.setPassword(MD5Password);
        userMapper.updateById(user);
        return CommonResponse.createForSuccessMessage("新密码以发送至手机，请注意查收！");
    }

    @Override
    public CommonResponse<User> updateUserById_2(User user) {
        if(user==null){
            return CommonResponse.createForError("请先登录");
        }
        String MD5Password = MD5Util.inputPassToDBPass(user.getPassword(), salt);
        user.setPassword(MD5Password);
        userMapper.updateById(user);
        return CommonResponse.createForSuccessMessage("新密码以发送至邮箱，请注意查收！");
    }
}
