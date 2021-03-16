package com.zhenhong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhenhong.mapper.FirstTypeMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.OrderMapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.FirstType;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.Order;
import com.zhenhong.pojo.User;
import com.zhenhong.service.OrderService;
import com.zhenhong.vo.OrderDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2021/1/21 18:52
 * @Version 1.0
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private FirstTypeMapper firstTypeMapper;

    @Override
    public List<OrderDetailsVo> orderToOrderDetailVoList(List<Order> orderList) {
        List<OrderDetailsVo> orderDetailsVoList = orderList.stream().map(e -> new OrderDetailsVo(
                e.getId(),
                e.getOrderCode(),
                e.getCustomerId(),
                e.getSellerId(),
                e.getGoodsId(),
                e.getOrderCount(),
                e.getDelivery(),
                e.getStatus(),
                e.getRemark(),
                e.getCreateTime(),
                e.getUpdateTime()
        )).collect(Collectors.toList());
        for (OrderDetailsVo orderDetailsVo : orderDetailsVoList) {
            User custom = userMapper.selectById(orderDetailsVo.getCustomerId());
            User seller = userMapper.selectById(orderDetailsVo.getSellerId());
            orderDetailsVo.setCustomer(custom);
            orderDetailsVo.setSeller(seller);
            Integer goodsId = orderDetailsVo.getGoodsId();
            Goods goods = goodsMapper.selectById(goodsId);
            FirstType firstType = firstTypeMapper.selectById(goods.getFirstTypeId());
            orderDetailsVo.setFirstTypeName(firstType.getName());
            orderDetailsVo.setGoods(goods);
        }
        return orderDetailsVoList;
    }

    @Override
    public List<OrderDetailsVo> orderDetailsVoList(Integer userId, Integer status) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("customer_id",userId);
        orderQueryWrapper.eq("status",status);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        List<OrderDetailsVo> orderDetailsVoList = orderToOrderDetailVoList(orderList);
        return orderDetailsVoList;
    }

    @Override
    public List<OrderDetailsVo> orderDetailsBySeller(Integer sellerId, Integer status) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("seller_id",sellerId);
        orderQueryWrapper.eq("status",status);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        List<OrderDetailsVo> orderDetailsVoList = orderToOrderDetailVoList(orderList);
        return orderDetailsVoList;
    }

    @Override
    public OrderDetailsVo orderDetailsVo(Integer id) {
        Order order = orderMapper.selectById(id);
        Goods goods = goodsMapper.selectById(order.getGoodsId());
        User seller = userMapper.selectById(order.getSellerId());
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
        orderDetailsVo.setSeller(seller);
        orderDetailsVo.setCustomer(custom);
        return orderDetailsVo;
    }

    @Override
    public Integer soldGoodsCount(Integer userId) {
        return orderMapper.soldGoodsCount(userId);
    }

    @Override
    public Integer toBeTransaction(Integer userId) {
        return orderMapper.toBeTransaction(userId);
    }

    @Override
    public Integer boughtGoodsCount(Integer userId) {
        return orderMapper.boughtGoodsCount(userId);
    }

    @Override
    public Integer toBeDeliveredCount(Integer userId) {
        return orderMapper.toBeDeliveredCount(userId);
    }

    @Override
    public Map<String, Integer> firstTypeToOrder() {
        Map<String,Integer> map = new LinkedHashMap<>();
        Integer count = 0;
        List<FirstType> firstTypeList = firstTypeMapper.selectList(null);
        for (FirstType firstType : firstTypeList) {
            List<Order> orderList = orderMapper.selectList(null);
            for (Order order : orderList) {
                Goods goods = goodsMapper.selectById(order.getGoodsId());
                if (goods.getFirstTypeId()==firstType.getId()){
                    count++;
                }
            }
            map.put(firstType.getName(),count);
            count = 0;
        }
        return map;
    }

    @Override
    public List<OrderDetailsVo> orderDetailVoList() {
        List<Order> orderList = orderMapper.selectList(null);
        List<OrderDetailsVo> orderDetailsVoList = orderToOrderDetailVoList(orderList);
        return orderDetailsVoList;
    }

    @Override
    public List<OrderDetailsVo> getOrderByFirstType(String name) {
        List<OrderDetailsVo> orderDetailsVos = orderDetailVoList();
        List<OrderDetailsVo> orderDetailsVoList = new ArrayList<>();
        QueryWrapper<FirstType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        FirstType firstType = firstTypeMapper.selectOne(queryWrapper);
        for (OrderDetailsVo orderDetailsVo : orderDetailsVos) {
            Integer firstTypeId = orderDetailsVo.getGoods().getFirstTypeId();
            if (firstTypeId == firstType.getId()){
                orderDetailsVoList.add(orderDetailsVo);
            }
        }
        return orderDetailsVoList;
    }

    @Override
    public Double transactionTotalAmount() {
        double transactionTotalAmount = 0.0;
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("status",1);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        for (Order order : orderList) {
            Goods goods = goodsMapper.selectById(order.getGoodsId());
            double cost = goods.getPrice() * order.getOrderCount();
            transactionTotalAmount = transactionTotalAmount + cost;
        }
        return transactionTotalAmount;
    }

    @Override
    public Double transactionDayAmount(String date) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("status",1);
        orderQueryWrapper.likeRight("update_time",date);
        double transactionDayAmount = 0.0;
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        for (Order order : orderList) {
            Goods goods = goodsMapper.selectById(order.getGoodsId());
            double cost = goods.getPrice() * order.getOrderCount();
            transactionDayAmount = transactionDayAmount + cost;
            BigDecimal decimal = new BigDecimal(transactionDayAmount);
            transactionDayAmount = decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return transactionDayAmount;
    }
}

