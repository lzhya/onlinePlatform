package com.zhenhong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhenhong.mapper.CartMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.Cart;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.User;
import com.zhenhong.service.CartService;
import com.zhenhong.vo.CartVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2021/1/16 16:47
 * @Version 1.0
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper,Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<CartVo> cartVoList() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser = (User) session.getAttribute("user");

        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",loginUser.getId());
        List<Cart> cartList = cartMapper.selectList(queryWrapper);
        List<CartVo> cartVoList = cartList.stream().map(e -> new CartVo(e.getId(), e.getGoodsId(),e.getQuantity(), e.getCost())).collect(Collectors.toList());
        for (CartVo cartVo : cartVoList) {
            Goods goods = goodsMapper.selectById(cartVo.getGoodsId());
            cartVo.setPhotoUrl(goods.getPhotoUrl());
            cartVo.setGoodsTitle(goods.getGoodsTitle());
            cartVo.setPrice(goods.getPrice());
            cartVo.setStock(goods.getCount());
            User seller = userMapper.selectById(goods.getUserId());
            cartVo.setSellerId(seller.getId());
            cartVo.setSellerName(seller.getNickname());
        }
        return cartVoList;
    }

    @Override
    public Integer cartCount() {
        return cartMapper.cartCount();
    }

}
