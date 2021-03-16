package com.zhenhong.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhenhong.pojo.Order;
import com.zhenhong.vo.OrderDetailsVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 订单
 * @Author lzhya
 * @Date 2021/1/21 18:51
 * @Version 1.0
 */
public interface OrderService extends IService<Order> {
    // 将order 转化为 orderDetailVo
    List<OrderDetailsVo> orderToOrderDetailVoList(List<Order> orderList);
    //根据用户订单详情
    List<OrderDetailsVo> orderDetailsVoList(Integer userId,Integer status);
    //根据销售者id查看订单详情
    List<OrderDetailsVo> orderDetailsBySeller(Integer sellerId,Integer status);
    //根据id查看订单详情
    OrderDetailsVo orderDetailsVo(Integer id);
    //查询卖出的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE seller_id = #{customerId} AND STATUS =1")
    Integer soldGoodsCount(Integer userId);
    //查询待交易的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE seller_id = #{customerId} AND STATUS =0")
    Integer toBeTransaction(Integer userId);
    //查询买到的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE customer_id = #{customerId} AND STATUS =1")
    Integer boughtGoodsCount(Integer userId);
    //查询代发货的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE customer_id = #{customerId} AND STATUS =0")
    Integer toBeDeliveredCount(Integer userId);
    // 查询各个类型的订单数
    Map<String,Integer> firstTypeToOrder();
    // 查询全部订单详情
    List<OrderDetailsVo> orderDetailVoList();
    // 根据一级分类查询商品订单
    List<OrderDetailsVo> getOrderByFirstType(String name);
    // 查询已交易金额
    Double transactionTotalAmount();
    // 根据时间查询当天交易金额
    Double transactionDayAmount(String date);

}
