package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

/**
 * 购物车实体类
 */
@TableName(value = "cart")
@NoArgsConstructor
@Setter
@Getter
public class Cart {
  //id
  @TableId(type = IdType.AUTO)
  private Integer id;
  //用户id
  @TableField("user_id")
  private Integer userId;
  //商品id
  @TableField("goods_id")
  private Integer goodsId;
  //数量
  private Integer quantity;
  //花费
  private double cost;
  //创建时间
  @TableField(value = "create_time",fill = FieldFill.INSERT)
  private String createTime;
  @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
  private String updateTime;

}
