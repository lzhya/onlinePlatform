package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author lzhya
 * @Date 2021/1/16 16:45
 * @Version 1.0
 */
@Repository
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    @Select("SELECT COUNT(*) FROM cart WHERE create_time LIKE concat('2021-',#{month},'%') ")
    Integer cartCountByMonth(String month);

    @Select("SELECT COUNT(*) FROM cart")
    Integer cartCount();

    /**
     *根据用户查询当前购物车数量
     */
    @Select("SELECT COUNT(*) FROM cart where user_id=#{user_id}")
    Integer cartCountByUserId(Integer userId);



}
