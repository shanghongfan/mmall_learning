package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

/**
 * @Date: 2017-08-14 20:30
 * @author: Mr.Shang
 * @description:
 **/
@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){

        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USRT,response.getData());
        }
        return response;
    }

    /**
     * 用户退出
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){

        session.removeAttribute(Const.CURRENT_USRT);

        return ServerResponse.createBySuccess();

    }
    /**
     * 用户注册
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return  iUserService.register(user);

    }
    /**
     * 校验用户名和邮箱
     * str指用户名或邮箱账号
     * type指username或email
     */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type){
        return iUserService.checkValid(str,type);
    }

    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USRT);
        if (user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
    }
    /**
     * 忘记密码
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){

        return iUserService.selectQuestion(username);

    }

    /**
     *判断问题答案
     */
    @RequestMapping(value = "forget_check_auswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAuswer(String username, String question, String answer){

        return  iUserService.checkAuswer(username, question, answer);
    }

    /**
     * 忘记密码中的重置密码
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,
                                                     String passworldNew,String forgetToken){

        return iUserService.forgetResetPassword(username, passworldNew, forgetToken);


    }

    /**
     * 登录状态下重置密码
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String oldPassword,String newPassword){

        User user = (User) session.getAttribute(Const.CURRENT_USRT);

        if (user==null){

            return  ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(oldPassword,newPassword,user);

    }

    /**
     * 更新用户个人信息
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USRT);

        if (currentUser==null){

            return  ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        ServerResponse<User> response = iUserService.updateInformation(user);
        //更新session
        if (response.isSuccess()){

            session.setAttribute(Const.CURRENT_USRT,response.getData());
        }
      return response;
    }

    /**
     *
     * 获取当前用户详细信息,并强制登录
     */
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USRT);

        if (currentUser==null){

            return  ServerResponse.createByErrorCodeMessage
                    (ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }

        return iUserService.getInformation(currentUser.getId());

    }




}
