package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @description:
 * @author: Mr.Shang
 * @Date: 2017-09-05 14:00
 **/
public interface IProductService {
    //新增和更新产品
    ServerResponse saveOrUpdateProduct(Product product);

    //产品上下架
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
     //获取商品详情
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    //商品列表动态分页显示功能
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    //商品搜索
     ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}