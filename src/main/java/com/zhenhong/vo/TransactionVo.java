package com.zhenhong.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author lzhya
 * @Date 2021/2/24 21:48
 * @Version 1.0
 */
@Setter
@Getter
@ToString
public class TransactionVo {
    /**
     * 月份
     */
    private Integer month;
    /**
     * 天数
     */
    private Integer day;
    /**
     *  所有订单数
     */
    private Integer allOrderCount;
    /**
     * 待付款订单数
     */
    private Integer cartCount;
    /**
     *  交易订单数
     */
    private Integer toBeTransactionCount;
    /**
     * 代发货订单数
     */
    private Integer forwardingCount;
    /**
     * 当天交易额
     */
    Double transactionAmount;
}
