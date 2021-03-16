package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.OrderMapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.Order;
import com.zhenhong.pojo.User;
import com.zhenhong.service.CartService;
import com.zhenhong.service.GoodsService;
import com.zhenhong.service.OrderService;
import com.zhenhong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/4 21:05
 * @Version 1.0
 */
@Controller
public class AdminController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsMapper goodsMapper;


    /**
     * 处理管理员登录请求，并返回到首页
     * @return
     */
    @RequestMapping("/admin/login.html")
    public String toLogin(){
        return "admin/login";
    }

    /**
     * 登录验证
     */
    @RequestMapping("/admin/login")
    public String login(){
        return "redirect:/admin/index.html";
    }

    @RequestMapping("/admin/index.html")
    public String toIndex(){
        return "admin/index";
    }

    @RequestMapping("/admin/home")
    public String toHome(Model model){
        Integer userCount = userService.queryUserCount(); //查询平台用户数
        int totalNum = goodsService.selectTotalNum();//全部商品数
        int goodsTotalNum = goodsService.getGoodsTotalNum(); //全部上架商品数量
        int checkNum = goodsService.getCheckNum();//全部商品待审核数量
        int stopGoodsNum = goodsService.getStopGoodsNum(); //全部已下架商品数量
        //获取系统时间
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        String dateTime=""+year+"年"+monthValue+"月"+day+"日"+hour+"时"+minute+"分";
        // 获取本机ip地址
        try {
            InetAddress host = InetAddress.getLocalHost();
            String hostAddress = host.getHostAddress();
            model.addAttribute("host",hostAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        /**
         * 订单
         */
        double toBeTransactionAmount = 0.0;
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        for (Order order : orderList) {
            Goods goods = goodsMapper.selectById(order.getGoodsId());
            double cost = goods.getPrice() * order.getOrderCount();
            toBeTransactionAmount = toBeTransactionAmount + cost;
        }
        //全部订单数量
        Integer orderTotalCount = orderMapper.orderTotalCount();
        //交易成功的订单数量
        Integer toBeTransactionCount = orderMapper.transactionCount(1);
        //待付款数量
        Integer cartCount = cartService.cartCount();

        model.addAttribute("toBeTransactionAmount",toBeTransactionAmount);
        model.addAttribute("orderTotalCount",orderTotalCount+cartCount);
        model.addAttribute("toBeTransactionCount",toBeTransactionCount);
        model.addAttribute("cartCount",cartCount);

        model.addAttribute("userCount",userCount);
        model.addAttribute("dateTime",dateTime);
        model.addAttribute("totalNum",totalNum);
        model.addAttribute("goodsTotalNum",goodsTotalNum);
        model.addAttribute("checkNum",checkNum);
        model.addAttribute("stopGoodsNum",stopGoodsNum);
        return "admin/home";
    }

    /**
     * 查询全部用户
     * @return
     */
    @RequestMapping("/admin/getAllUser")
    public String getAllUser(Model model){
        List<User> userList = userMapper.selectList(null);
        model.addAttribute("userList",userList);
        return "admin/user_list";
    }

    /**
     * 多条件查询
     * @param user user
     * @param model model
     */
    @RequestMapping("/getUserByManyConditions")
    public String getUserByMany(User user,Model model){
        String nickname = user.getNickname();
        user.setNickname("");
        if (nickname.length()==11 && nickname.startsWith("1")){
            user.setPhone(nickname);
            List<User> userList = userService.getUserByManyConditions(user);
            model.addAttribute("userList",userList);
            return "admin/user_list";
        }else if(nickname.endsWith(".com")){
            user.setEmail(nickname);
            List<User> userList = userService.getUserByManyConditions(user);
            model.addAttribute("userList",userList);
            return "admin/user_list";
        }else{
            user.setNickname(nickname);
            List<User> userList = userService.getUserByManyConditions(user);
            model.addAttribute("userList",userList);
            return "admin/user_list";
        }
    }

    @RequestMapping("/admin/grade")
    public String grade(Model model){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<User> userList = userMapper.selectList(queryWrapper);
        model.addAttribute("userList",userList);
        return "admin/user_grade";
    }
}
