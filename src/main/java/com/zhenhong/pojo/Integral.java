package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Integral {
  //id
  @TableId(type = IdType.AUTO)
  private Integer id;
  //用户id
  @TableField("user_id")
  private Integer userId;
  //商品id
  @TableField("goods_id")
  private Integer goodsId;
  //积分变化情况
  private Integer fraction;
  //创建时间
  @TableField(value = "create_time",fill = FieldFill.INSERT)
  private String createTime;
  //备注
  private String remark;
}
