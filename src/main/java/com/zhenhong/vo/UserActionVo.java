package com.zhenhong.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户消费行为
 * @Author lzhya
 * @Date 2021/1/30 21:03
 * @Version 1.0
 */
@Getter
@Setter
public class UserActionVo {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户积分
     */
    private Integer integral;
    /**
     * 发布商品数
     */
    private Integer releaseNum;
    /**
     * 浏览商品数
     */
    private Integer views;
    /**
     * 购物次数
     */
    private Integer shoppingNum;

    /**
     * 购物车商品数
     */
    private Integer cartCount;

    public UserActionVo(Integer id, String username, Integer integral) {
        this.id = id;
        this.username = username;
        this.integral = integral;
    }
}
