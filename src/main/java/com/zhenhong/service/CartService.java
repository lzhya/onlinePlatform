package com.zhenhong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhenhong.pojo.Cart;
import com.zhenhong.vo.CartVo;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2021/1/16 16:46
 * @Version 1.0
 */
public interface CartService extends IService<Cart> {
    //购物车详情
    List<CartVo> cartVoList();
    Integer cartCount();
}
