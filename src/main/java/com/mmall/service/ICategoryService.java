package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @description: 实现分类管理接口
 * @author: Mr.Shang
 * @Date: 2019-08-27 14:57
 **/
public interface ICategoryService {
    //添加品类
     ServerResponse addCategory(String categoryName, Integer parentId);
     //更新品类名字
     ServerResponse updateCategoryName(Integer categoryId,String categoryName);
     //查询子分类
     ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);
     //递归查询本节点id和字节点id
     ServerResponse selectCategoryAndChildrenByid(Integer categoryId);

}
