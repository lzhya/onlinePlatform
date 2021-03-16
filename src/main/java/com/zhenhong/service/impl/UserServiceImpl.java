package com.zhenhong.service.impl;

import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.User;
import com.zhenhong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/3 19:11
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    @Override
    public Integer queryUserCount() {
        return userMapper.queryUserCount();
    }

    @Override
    public List<User> getUserByManyConditions(User user) {
        return userMapper.getUserByManyConditions(user);
    }

    @Override
    public Integer getGoodsCountByUserId(Integer userId) {
        return userMapper.getGoodsCountByUserId(userId);
    }
}
