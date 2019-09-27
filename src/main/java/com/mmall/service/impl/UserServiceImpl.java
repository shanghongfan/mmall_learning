package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Date: 2019-08-14 20:40
 * @author: Mr.Shang
 * @description:
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount==0){

            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String md5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5password);
        if (user==null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功",user);
    }



    @Override
    public ServerResponse<String> register(User user) {

        int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount>0){
            return ServerResponse.createByErrorMessage("用户名已存在");
        }

        int checkEmail = userMapper.checkEmail(user.getEmail());

        if (checkEmail>0){
            return  ServerResponse.createByErrorMessage("邮箱已经存在");
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);//用户权限

        //密码加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int insertUser = userMapper.insert(user);

        if (insertUser==0){

            return ServerResponse.createByErrorMessage("用户注册失败");
        }

        return ServerResponse.createBySuccessMessage("用户注册成功");
    }

    /**
     * 校验用户名和密码
     */

    public ServerResponse<String> checkValid(String str, String type){

        //判断是否为空
        if (StringUtils.isNotBlank(type)){

            if (Const.USERNAME.equals(type)){

                int checkUsername = userMapper.checkUsername(str);

                if (checkUsername>0){

                    return ServerResponse.createByErrorMessage("用户名已经存在");
                }

            }

            if (Const.EMAIL.equals(type)){

                int checkUsername = userMapper.checkEmail(str);

                if (checkUsername>0){

                    return ServerResponse.createByErrorMessage("邮箱已经存在");
                }
            }
        }else {

            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功,"+str+"不存在,可以使用");
    }


    @Override
    public ServerResponse selectQuestion(String username) {

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);

        if (validResponse.isSuccess()){
            //用户名不存在
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);

        if (StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /**
     * 查询回答的问题
     */
    @Override
    public ServerResponse<String> checkAuswer(String username, String question, String answer){

        int checkAuswer = userMapper.checkAuswer(username, question, answer);
        if (checkAuswer>0){
          //问题答案正确
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);

           return ServerResponse.createBySuccess(forgetToken);

        }
        return  ServerResponse.createByErrorMessage("问题的答案错误");

    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passworldNew, String forgetToken) {

      if (StringUtils.isBlank(forgetToken)){

          return ServerResponse.createByErrorMessage("参数错误,tolen需要传递");
      }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()){
            //用户名不存在
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){

            return ServerResponse.createByErrorMessage("参数无效或过期");
        }

        //判断是否相等
        if (StringUtils.equals(forgetToken,token)){

            //更新密码
            String md5password = MD5Util.MD5EncodeUtf8(passworldNew);

            int rowCount = userMapper.updatePasswordByUsername(username, md5password);

            if (rowCount>0){


                return ServerResponse.createBySuccessMessage("密码重置成功");
            }

        }else {
         return ServerResponse.createByErrorMessage("token参数错误,请重新获取");

        }

        return ServerResponse.createByErrorMessage("密码重置失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword,User user) {

        int resultCount= userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());

            if (resultCount==0){
                return  ServerResponse.createByErrorMessage("输入的旧密码错误");
                }

                user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);

        if (updateCount>0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }

        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //username不能被更新
        //email是否存在和与新的email是否相同
        int resultCount= userMapper.checkEmailByUserId(user.getEmail(),user.getId());

        if (resultCount>0){
            return ServerResponse.createByErrorMessage("email已经被占用");

        }

        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);

        if (updateCount>0){
          //  return ServerResponse.createBySuccessMessage("更新个人信息成功");

            return  ServerResponse.createBySuccess("更新个人信息成功",updateUser);


        }

        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {

        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        //将密码置空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员登录
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user!=null&&user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }




}
