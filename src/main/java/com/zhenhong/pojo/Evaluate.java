package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Evaluate {
  @TableId(type = IdType.AUTO)
  private Integer id;
  private Integer orderId;
  private double serviceAttitude;
  private double deliverySpeed;
  private double goodsDescription;
  private String content;
  @TableField(value = "create_time",fill = FieldFill.INSERT)
  private String createTime;
  private Integer status;


}
