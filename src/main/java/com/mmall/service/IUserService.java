package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @description: 登录接口
 * @author: Mr.Shang
 * @Date: 2017-08-14 20:39
 **/
public interface IUserService {
  //用户登录
    ServerResponse<User> login(String username, String password);
   //用户注册
    ServerResponse<String> register(User user);
    //校验用户名和邮箱
    ServerResponse<String> checkValid(String str,String type);

    ServerResponse selectQuestion(String username);
  //查询回答的问题答案
    ServerResponse<String> checkAuswer(String username, String question, String answer);
    //忘记密码中的重置密码
     ServerResponse<String> forgetResetPassword(String username,
                                                     String passworldNew,String forgetToken);
     //登录状态下重置密码
     ServerResponse<String> resetPassword(String oldPassword, String newPassword,User user);
   //更新用户个人信息
     ServerResponse<User> updateInformation(User user);
    //获取用户详细信息
     ServerResponse<User> getInformation(Integer userId);
    //检验账号是否是管理员
     ServerResponse checkAdminRole(User user);





}
