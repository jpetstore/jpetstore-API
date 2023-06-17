package org.csu.jpetstoreapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.User;
import org.csu.jpetstoreapi.entity.UserInfo;
import org.csu.jpetstoreapi.persistence.UserInfoMapper;
import org.csu.jpetstoreapi.service.UserInfoService;
import org.csu.jpetstoreapi.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    private static final String salt="1a2b3c4d";

    public CommonResponse<UserInfo> login(String id, String password){
        QueryWrapper<UserInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        queryWrapper.eq("password",password);
        UserInfo userInfo=userInfoMapper.selectOne(queryWrapper);

        if(userInfo==null){
            return CommonResponse.createForError("用户名或密码错误");
        }else {
            return CommonResponse.createForSuccess("登录成功",userInfo);
        }
    }

    @Override
    public CommonResponse<UserInfo> getAccountByUsernameAndPassword(String id,String password) {
        UserInfo userInfo = userInfoMapper.selectById(id);

        String MD5Password = MD5Util.inputPassToDBPass(password,salt);
        System.out.println("*******************************************");
        System.out.println("输入密码： "+password+"  MD5加密后： "+MD5Password);
        System.out.println("*******************************************");
        if(userInfo==null){
            return CommonResponse.createForError("没有该id的user");
        }
        else{
            if(!userInfo.getPassword().equals(MD5Password)){
                return CommonResponse.createForError("password不正确");
            }
            else{
                return CommonResponse.createForSuccess("登录成功",userInfo);
            }
        }
    }

    @Override
    public UserInfo findUserById(String id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        return userInfo;
    }

    @Override
    public CommonResponse<UserInfo> insertUser(UserInfo userInfo) {
        UserInfo userInfo1=findUserById(userInfo.getId());
        if(userInfo.getId()==null||userInfo.getId()==""){
            return CommonResponse.createForError("用户名不能为空");
        } else if (userInfo1!=null) {
            return CommonResponse.createForError("用户名已存在");
        } else if (userInfo.getPassword()==null||userInfo.getPassword()=="") {
            return CommonResponse.createForError("密码不能为空");
        } else if (userInfo.getPhone()==null||userInfo.getPhone()=="") {
            return CommonResponse.createForError("电话不能为空");
        } else{
            String MD5Password = MD5Util.inputPassToDBPass(userInfo.getPassword(),salt);
            userInfo.setPassword(MD5Password);
            userInfoMapper.insert(userInfo);
            return CommonResponse.createForSuccess("注册成功",userInfo);//用户插入成功
        }
    }

    @Override
    public CommonResponse<UserInfo> updateUser(UserInfo userInfo) {
        if(userInfo==null){
            return CommonResponse.createForError("请先登录");
        }
        if (userInfo.getPassword() == null||userInfo.getPassword()=="") {
            return CommonResponse.createForError("密码不能为空");
        } else if (userInfo.getPhone() == null||userInfo.getPhone()=="") {
            return CommonResponse.createForError("电话不能为空");
        } else {
            String MD5Password = MD5Util.inputPassToDBPass(userInfo.getPassword(), salt);
            userInfo.setPassword(MD5Password);
            userInfoMapper.updateById(userInfo);
            return CommonResponse.createForSuccess("更改信息成功",userInfo);
        }
    }

    @Override
    public UserInfo findUserByPhone(String phone) {

        QueryWrapper<UserInfo>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        UserInfo userInfo=userInfoMapper.selectOne(queryWrapper);

        return userInfo;
    }

    @Override
    public CommonResponse<UserInfo> updateUserById(UserInfo userInfo) {
        if(userInfo==null){
            return CommonResponse.createForError("请先登录");
        }
        String MD5Password = MD5Util.inputPassToDBPass(userInfo.getPassword(), salt);
        userInfo.setPassword(MD5Password);
        userInfoMapper.updateById(userInfo);
        return CommonResponse.createForSuccessMessage("新密码以发送至手机，请注意查收！");
    }

    @Override
    public CommonResponse<UserInfo> updateUserById_2(UserInfo userInfo) {
        if(userInfo==null){
            return CommonResponse.createForError("请先登录");
        }
        String MD5Password = MD5Util.inputPassToDBPass(userInfo.getPassword(), salt);
        userInfo.setPassword(MD5Password);
        userInfoMapper.updateById(userInfo);
        return CommonResponse.createForSuccessMessage("新密码以发送至邮箱，请注意查收！");
    }

}
