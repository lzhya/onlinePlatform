package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/3 17:35
 * @Version 1.0
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
    //查询全部用户
    List<User> queryAll();
    //查询全部用户数量
    Integer queryUserCount();
    //多条件查询
    List<User> getUserByManyConditions(User user);
    //各个等级用户数
    Integer getGradeCount(Integer min,Integer max);
    //查询用户发布商品的数量
    Integer getGoodsCountByUserId(Integer userId);
}
