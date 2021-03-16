package com.zhenhong.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author lzhya
 * @Date 2021/1/17 22:16
 * @Version 1.0
 */
@NoArgsConstructor
@Setter
@Getter
public class CartVo {
    private Integer id;
    private Integer goodsId;
    private String goodsTitle;
    private String photoUrl;
    private double price;
    private Integer stock;
    private Integer quantity;
    private Integer sellerId;
    private String sellerName;
    private double cost;

    public CartVo(Integer id, Integer goodsId, Integer quantity, double cost) {
        this.id = id;
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.cost = cost;
    }
}
