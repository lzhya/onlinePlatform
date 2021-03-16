package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单实体类
 */
@TableName(value = "order_item")
@Setter
@Getter
public class Order {
  /**
   * 订单id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 订单编号
   */
  @TableField("order_code")
  private String orderCode;
  /**
   * 买家id
   */
  @TableField("customer_id")
  private Integer customerId;
  /**
   * 卖家id
   */
  @TableField("seller_id")
  private Integer sellerId;
  /**
   * 商品id
   */
  @TableField("goods_id")
  private Integer goodsId;
  /**
   * 配送方式
   */
  private Integer delivery;
  /**
   * 订单商品数量
   */
  @TableField("order_count")
  private Integer orderCount;
  /**
   * 订单状态（0：创建  1：已交易）
   */
  private Integer status;
  /**
   * 订单备注
   */
  private String remark;
  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  private String createTime;
  /**
   * 修改时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateTime;
}
