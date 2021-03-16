package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片实体类
 */

@Setter
@Getter
public class Picture {
  /**
   * 图片id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 排序
   */
  private Integer sort;

  /**
   * 图片类型id
   */
  @TableField("type_id")
  private Integer typeId;

  /**
   * 宽度
   */
  private double width;

  /**
   * 长度
   */
  private double height;

  /**
   * 链接地址
   */
  private String url;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 图片
   */
  private String pictures;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  private String createTime;

}
