package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @description:
 * @author: Mr.Shang
 * @Date: 2019-09-05 14:00
 **/
public interface IProductService {
    //新增和更新产品
    ServerResponse saveOrUpdateProduct(Product product);

    //产品上下架
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
     //获取商品详情
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

}