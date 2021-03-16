package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 二级分类实体
 */
public class SecondType {
  @TableId(type = IdType.AUTO)
  private Integer id; //id
  @TableField("first_type_id")
  private Integer firstTypeId; //所属一级分类id
  private String name; //二级分类名称

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getFirstTypeId() {
    return firstTypeId;
  }

  public void setFirstTypeId(Integer firstTypeId) {
    this.firstTypeId = firstTypeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "SecondType{" +
            "id=" + id +
            ", firstTypeId=" + firstTypeId +
            ", name='" + name + '\'' +
            '}';
  }
}
