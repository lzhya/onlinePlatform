package com.zhenhong.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.zhenhong.mapper.CartMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.pojo.BrowseRecords;
import com.zhenhong.pojo.Cart;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.User;
import com.zhenhong.service.BrowseRecordsService;
import com.zhenhong.service.CartService;
import com.zhenhong.vo.CartVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/** 购物车控制层
 * @Author lzhya
 * @Date 2021/1/16 17:21
 * @Version 1.0
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private BrowseRecordsService browseRecordsService;
    @Autowired
    private GoodsMapper goodsMapper;

    @RequestMapping("/cart/add")
    @ResponseBody
    public String add(Integer goodsId, double price, Integer quantity, Integer userId,Integer stock) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (user.getId() == userId) {
                return "不能添加自己的商品！";
            }else if (quantity > stock){
                return "q>s";
            }
            QueryWrapper<Cart> wrapper = new QueryWrapper<>();
            wrapper.eq("goods_id", goodsId);
            Cart one = cartService.getOne(wrapper);
            /**
             * 如果该商品已经存在，并且购物车数量<库存，数量+1
             */
            if (one != null) {
                if (one.getQuantity() + quantity >= stock){
                    return "商品加购件数(含已加购件数)已超过库存";
                }else {
                    UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("goods_id", goodsId);
                    updateWrapper.set("quantity", one.getQuantity() + 1);
                    updateWrapper.set("cost", one.getCost() + price * quantity);
                    cartService.update(updateWrapper);
                    return "成功加入购物车！";
                }
            }
            Cart cart = new Cart();
            cart.setGoodsId(goodsId);
            cart.setQuantity(quantity);
            cart.setCost(price * quantity);
            cart.setUserId(user.getId());
            boolean save = cartService.save(cart);
            if (save) {
                return "成功加入购物车！";
            } else {
                return "加入购物车失败！";
            }
        } else {
            return "请登录！";
        }
    }

    /**
     * 查询购物车数量
     */
    @RequestMapping("/cart/getCartCount")
    @ResponseBody
    public Integer getCartCount() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return 0;
        }
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        Integer count = cartMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * 删除购物车
     * @param id id
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        cartMapper.deleteById(id);
        return "redirect:/cart.html";
    }

    /**
     * 跳转到购物车
     */
    @RequestMapping("/cart.html")
    public String selectByUserId(Model model) {
        List<CartVo> cartVoList = cartService.cartVoList();
        double totalCost = 0.0;
        for (CartVo cartVo : cartVoList) {
            totalCost = totalCost + cartVo.getCost();
        }
        //查询用户浏览记录
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user = (User) session.getAttribute("user");

        QueryWrapper<BrowseRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.orderByDesc("create_time");
        List<BrowseRecords> browseRecordsList = browseRecordsService.list(queryWrapper);
        List<Goods> goodsList = new ArrayList<>();
        for (BrowseRecords browseRecords : browseRecordsList) {
            Goods goods = goodsMapper.selectById(browseRecords.getGoodsId());
            goodsList.add(goods);
        }
        model.addAttribute("username", user.getNickname());
        model.addAttribute("cartVoList", cartVoList);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("goodsList", goodsList);
        return "user/cart";
    }

    /**
     * 处理购物车结算
     * @param orderArray json数组
     * @throws JsonProcessingException 异常
     */
    @RequestMapping(value = "/cart/update",produces = "application/json")
    @ResponseBody
    public String update(String orderArray) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(orderArray);
        UpdateWrapper<Cart> wrapper ;

        for (JsonNode node : jsonNode) {
            wrapper = new UpdateWrapper<>();
            int goodsId = node.get("goodsId").asInt();
            int count = node.get("count").asInt();
            double price = node.get("price").asDouble();
            wrapper.eq("goods_id",goodsId);
            Cart cart = new Cart();
            cart.setQuantity(count);
            cart.setCost(price*count);
            cartMapper.update(cart,wrapper);
        }
        return orderArray;
    }

}
