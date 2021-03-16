package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.IntegralMapper;
import com.zhenhong.mapper.OrderMapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.*;
import com.zhenhong.service.CartService;
import com.zhenhong.service.OrderService;
import com.zhenhong.vo.CartVo;
import com.zhenhong.vo.OrderDetailsVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2021/1/21 18:54
 * @Version 1.0
 */
@Controller
public class OrderController {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private IntegralMapper integralMapper;

    /**
     * 查询全部订单
     * @param model model
     * @return 返回地址
     */
    @RequestMapping("/admin/getAllOrderList")
    public String getAllOrderList(Model model){
        Map<String, Integer> map = orderService.firstTypeToOrder();
        Set<String> strings = map.keySet();
        int total = 0;
        for (String string : strings) {
            Integer integer = map.get(string);
            total = total +integer;
        }
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailVoList();
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        model.addAttribute("total",total);
        model.addAttribute("map",map);
        return "admin/order_list";
    }

    @RequestMapping("/admin/orderDetailById")
    public String orderDetailById(Integer id,Model model){
        OrderDetailsVo orderDetailsVo = orderService.orderDetailsVo(id);
        model.addAttribute("orderDetailsVo",orderDetailsVo);
        return "admin/order_detailed";
    }

    /**
     * 模糊查询 根据订单号查询订单
     * @param orderCode 订单号
     */
    @RequestMapping("/admin/selectOrderByCode")
    public String selectOrderByCode(String orderCode,Model model){
        Map<String, Integer> map = orderService.firstTypeToOrder();
        Set<String> strings = map.keySet();
        int total = 0;
        for (String string : strings) {
            Integer integer = map.get(string);
            total = total +integer;
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft("order_code",orderCode);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderToOrderDetailVoList(orderList);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        model.addAttribute("total",total);
        model.addAttribute("map",map);
        return "admin/order_list";
    }

    @RequestMapping("/admin/transaction")
    public String transaction(Model model){
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
        return "admin/transaction";
    }

    /**
     *  交易金额
     */
    @RequestMapping("/admin/amounts")
    public String amount(Model model){
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("status",1);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderToOrderDetailVoList(orderList);

        Double transactionTotalAmount = orderService.transactionTotalAmount();

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Double transactionDayAmount = orderService.transactionDayAmount(date);

        model.addAttribute("transactionTotalAmount",transactionTotalAmount);
        model.addAttribute("transactionDayAmount",transactionDayAmount);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        model.addAttribute("date",date);
        return "admin/amount";
    }

    @RequestMapping("/admin/amountsByDay")
    public String amountsByDay(Model model){
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("status",1);
        orderQueryWrapper.likeRight("update_time",date);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderToOrderDetailVoList(orderList);

        Double transactionTotalAmount = orderService.transactionTotalAmount();


        Double transactionDayAmount = orderService.transactionDayAmount(date);

        model.addAttribute("transactionTotalAmount",transactionTotalAmount);
        model.addAttribute("transactionDayAmount",transactionDayAmount);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        model.addAttribute("date",date);
        return "admin/amount";
    }

    @RequestMapping("/admin/amountsByMonth")
    public String amountsByMonth(Model model){
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("status",1);
        orderQueryWrapper.likeRight("update_time",date);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderToOrderDetailVoList(orderList);

        Double transactionTotalAmount = orderService.transactionTotalAmount();

        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-DD"));
        Double transactionDayAmount = orderService.transactionDayAmount(date);

        model.addAttribute("transactionTotalAmount",transactionTotalAmount);
        model.addAttribute("transactionDayAmount",transactionDayAmount);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        model.addAttribute("date",date);
        return "admin/amount";
    }

    /**
     * 根据商品类型查询订单
     */
    @RequestMapping("/admin/getOrderByFirstType")
    public String getOrderByFirstType(String name, Model model){
        Map<String, Integer> map = orderService.firstTypeToOrder();
        Set<String> strings = map.keySet();
        int total = 0;
        for (String string : strings) {
            Integer integer = map.get(string);
            total = total +integer;
        }
        List<OrderDetailsVo> orderDetailsVoList = orderService.getOrderByFirstType(name);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        model.addAttribute("total",total);
        model.addAttribute("map",map);
        return "admin/order_list";
    }

    @RequestMapping(value = "/user/order.html",produces = "application/json")
    public String add(String orderArray, Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(orderArray);
        List<CartVo> cartVoList = new ArrayList<>();
        //订单总价
        double totalCost = 0;
        for (JsonNode node : jsonNode) {
            //购物车id
            Integer cartId = node.get("cartId").asInt();
            //商品id
            int goodsId = node.get("goodsId").asInt();
            //购买数量
            int count = node.get("count").asInt();
            Goods goods = goodsMapper.selectById(goodsId);
            CartVo cartVo = new CartVo();
            cartVo.setId(cartId);
            cartVo.setGoodsId(goodsId);
            cartVo.setPhotoUrl(goods.getPhotoUrl());
            cartVo.setGoodsTitle(goods.getGoodsTitle());
            cartVo.setPrice(goods.getPrice());
            cartVo.setQuantity(count);
            cartVo.setStock(goods.getCount());
            cartVo.setCost(goods.getPrice()*count);
            cartVoList.add(cartVo);
            User seller = userMapper.selectById(goods.getUserId());
            cartVo.setSellerName(seller.getNickname());
            cartVo.setSellerId(seller.getId());
            totalCost = totalCost + goods.getPrice()*count;
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");

        model.addAttribute("cartVoList",cartVoList);
        model.addAttribute("totalCost",totalCost);
        model.addAttribute("user",user);
        return "user/order";
    }

    @RequestMapping(value = "/order/add",produces = "application/json")
    @ResponseBody
    public String add(String orderArray) throws JsonProcessingException {

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(orderArray);
        for (JsonNode node : jsonNode) {
            /**
             * 订单编号 年份+月+日 + 7位随机数
             */
            String yyMMdd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            String random = "";
            Random r1 = new Random();
            for (int i = 0; i < 7; i++) {
                int i1 = r1.nextInt(10);
                random = random + i1;
            }
            String orderCode = yyMMdd+random;
            /**
             * 清空已经结算的购物车信息
             */
            int cartId = node.get("cartId").asInt();
            if (cartId != 0) {
                cartService.removeById(cartId);
            }

            //商品id
            int goodsId = node.get("goodsId").asInt();
            Goods goods = goodsMapper.selectById(goodsId);
            //卖家id
            int sellerId = node.get("sellerId").asInt();
            //购买数量
            int quantity = node.get("quantity").asInt();
            if (goods.getCount()>=quantity){
                UpdateWrapper<Goods> goodsUpdateWrapper = new UpdateWrapper<>();
                goodsUpdateWrapper.eq("id",goodsId);
                goodsUpdateWrapper.set("stock",goods.getStock()-quantity);
                goodsMapper.update(null,goodsUpdateWrapper);
            }else {
                return "商品"+goods.getGoodsTitle()+"库存不足";
            }
            //配送方式
            int delivery = node.get("delivery").asInt();
            //买家留言
            String remark = node.get("remark").toString();
            Order order = new Order();
            order.setOrderCode(orderCode);
            order.setGoodsId(goodsId);
            order.setSellerId(sellerId);
            order.setOrderCount(quantity);
            order.setDelivery(delivery);
            order.setRemark(remark);
            order.setCustomerId(user.getId());
            orderMapper.insert(order);
        }
        return "下单成功";
    }

    @RequestMapping("/success.html")
    public String success(){
        return "user/success";
    }

    /**
     * 查询订单详情
     * @param id 订单id
     * @param model model
     * @return 返回地址
     */
    @RequestMapping("/user/order_detail.html")
    public String getOrderById(Integer id,Model model){
        OrderDetailsVo orderDetailsVo = orderService.orderDetailsVo(id);
        model.addAttribute("orderDetailsVo",orderDetailsVo);
        return "user/order_detail";
    }

    @RequestMapping("/user/getOrderDetailBySellerId")
    public String getOrderDetailBySellerId(Integer id,Model model){
        Order order = orderMapper.selectById(id);
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        User custom = userMapper.selectById(order.getCustomerId());
        OrderDetailsVo orderDetailsVo = new OrderDetailsVo();
        orderDetailsVo.setId(order.getId());
        orderDetailsVo.setDelivery(order.getDelivery());
        orderDetailsVo.setOrderCode(order.getOrderCode());
        orderDetailsVo.setOrderCount(order.getOrderCount());
        orderDetailsVo.setCreateTime(order.getCreateTime());
        orderDetailsVo.setRemark(order.getRemark());
        orderDetailsVo.setUpdateTime(order.getUpdateTime());
        orderDetailsVo.setStatus(order.getStatus());
        orderDetailsVo.setGoods(goods);
        orderDetailsVo.setCustomer(custom);
        model.addAttribute("orderDetailsVo",orderDetailsVo);
        return "user/order-detail";
    }


    /**
     * 确认收货
     * @param id 订单id
     * @return 返回地址
     */
    @RequestMapping("/order/receipt")
    @ResponseBody
    public String receipt(Integer id,Integer integral){
        Order byId = orderService.getById(id);
        //修改订单状态
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        Order order = new Order();
        order.setStatus(1);
        updateWrapper.eq("id",id);
        int res = orderMapper.update(order, updateWrapper);
        if (res==1){
            // 积分
            User user = userMapper.selectById(byId.getCustomerId());
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",byId.getCustomerId());
            User customer = new User();
            customer.setIntegral(user.getIntegral()+integral);
            userMapper.update(customer,userUpdateWrapper);
            // 添加积分变化情况
            Integral integral1 = new Integral();
            integral1.setGoodsId(byId.getGoodsId());
            integral1.setUserId(user.getId());
            integral1.setFraction(integral);
            integral1.setRemark("交易获得");
            integralMapper.insert(integral1);
            return "收货成功";
        }else {
            return "收货失败";
        }
    }
}
