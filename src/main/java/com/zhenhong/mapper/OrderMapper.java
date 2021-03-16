package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 订单
 * @Author lzhya
 * @Date 2021/1/21 18:51
 * @Version 1.0
 */
@Repository
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    //查询卖出的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE seller_id = #{seller_id} AND STATUS =1")
    Integer soldGoodsCount(Integer userId);
    //查询待交易的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE seller_id = #{seller_id} AND STATUS =0")
    Integer toBeTransaction(Integer userId);
    //查询买到的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE customer_id = #{customerId} AND STATUS =1")
    Integer boughtGoodsCount(Integer userId);
    //查询代发货的商品数量
    @Select("SELECT COUNT(*) FROM order_item WHERE customer_id = #{customerId} AND STATUS =0")
    Integer toBeDeliveredCount(Integer userId);
    //根据月份查询已交易的订单数
    @Select("SELECT COUNT(*) FROM order_item WHERE update_time LIKE concat('2021-',#{date},'%') and status = 1")
    Integer toBeTransactionCount(String date);

    @Select("SELECT COUNT(*) FROM order_item WHERE update_time LIKE concat('2021-',#{month},'%') and status = 0")
    Integer forwardingCount(String month);

    //查询交易全部订单数量
    @Select("SELECT COUNT(*) FROM order_item")
    Integer orderTotalCount();

    //查询交易全部订单数量
    @Select("SELECT COUNT(*) FROM order_item where status = #{status}")
    Integer transactionCount(Integer status);

}
