package com.zhenhong.service;

import com.zhenhong.pojo.FirstType;
import com.zhenhong.vo.CategoryVo;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/5 19:45
 * @Version 1.0
 */
public interface FirstTypeService {
    //获取最大id
    int getMaxId();
    //添加商品一级分类
    int saveOne(FirstType firstType);
    //分类情况
    List<CategoryVo> categoryVoList();
}
