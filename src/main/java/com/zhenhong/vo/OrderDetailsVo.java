package com.zhenhong.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author lzhya
 * @Date 2021/2/5 22:01
 * @Version 1.0
 */
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailsVo {
    private Integer id;
    private String orderCode;
    private Integer customerId;
    private Integer sellerId;
    private User customer;
    private User seller;
    private Integer goodsId;
    private Goods goods;
    private String firstTypeName;
    private Integer delivery;
    private Integer orderCount;
    private Integer status;
    private String remark;
    private String createTime;
    private String updateTime;
    private Boolean isEvaluate;

    public OrderDetailsVo(Integer id, String orderCode, Integer customerId, Integer sellerId, Integer goodsId, Integer orderCount, Integer delivery, Integer status, String remark, String createTime, String updateTime) {
        this.id = id;
        this.orderCode = orderCode;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.goodsId = goodsId;
        this.orderCount = orderCount;
        this.delivery = delivery;
        this.status = status;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
