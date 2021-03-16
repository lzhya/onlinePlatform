package com.zhenhong.service;

import com.zhenhong.pojo.User;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/3 19:10
 * @Version 1.0
 */

public interface UserService {
    //查询全部用户
    List<User> queryAll();
    //查询全部用户数量
    Integer queryUserCount();
    //多条件查询
    List<User> getUserByManyConditions(User user);
    //查询用户发布商品的数量
    Integer getGoodsCountByUserId(Integer userId);
}
