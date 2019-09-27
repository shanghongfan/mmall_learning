package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Date: 2019-08-27 11:34
 * @author: Mr.Shang
 * @description: 分类管理
 **/
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,
                                      String categoryName, @RequestParam(value ="parentId",defaultValue = "0") int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USRT);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登录");
        }
       if (iUserService.checkAdminRole(user).isSuccess()){
            //是管理员,执行增加分类
          return iCategoryService.addCategory(categoryName,parentId);

       }else {
            return ServerResponse.createByErrorMessage("不是管理员账户,无权限");
       }
    }
   @RequestMapping("set_categoryname.do")
   @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,int categoryId,String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USRT);
        if (user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录需要登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMessage("不是管理员账户,无权限");
        }
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USRT);
        if (user==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            //查询子节点的category信息,非递归,保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("不是管理员账户,无权限");
        }
    }
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildCategory(HttpSession session,@RequestParam(value = "categoryId") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USRT);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){

            return iCategoryService.selectCategoryAndChildrenByid(categoryId);
        }else {
            return ServerResponse.createByErrorMessage("不是管理员账户,无权限");
        }
    }



}



















































