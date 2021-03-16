package com.zhenhong.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhenhong.vo.ProductCategoryVo;
import com.zhenhong.vo.ProductCategoryVo1;

import java.util.List;

/**商品分类
 * @Author lzhya
 * @Date 2020/12/17 16:18
 * @Version 1.0
 */
public interface ProductCatgoryService {
    //返回商品分类以及对应的商品信息
    List<ProductCategoryVo> catgoryList();
    //返回特定商品分类以及对应的商品信息
    ProductCategoryVo productList(Integer id);
    //返回商品分类关系 Json
    String catgoryJson();
    //
}
