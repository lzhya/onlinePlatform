package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.FirstType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/5 17:53
 * @Version 1.0
 *
 * 一级分类
 */
@Repository
@Mapper
public interface FirstTypeMapper extends BaseMapper<FirstType> {
   //获取最大id
   int getMaxId();
   //添加商品一级分类
   int saveOne(FirstType firstType);
}
