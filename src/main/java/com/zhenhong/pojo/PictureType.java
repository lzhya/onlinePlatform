package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片分类
 */

@Setter
@Getter
public class PictureType {
  /**
   * id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 分类名称
   */
  private String name;
  /**
   * 图片数量
   */
  private Integer count;
  /**
   * 说明
   */
  private String remark;
  /**
   * 创建时间
   */
  @TableField(value = "create_time",fill = FieldFill.INSERT)
  private String createTime;
  /**
   * 状态
   */
  private Integer status;
}
