package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

/**
 * 一级分类实体
 */
public class FirstType {

  private  Integer id; //id
  private String name; //名称
  private String remark; //说明

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public String toString() {
    return "FirstType{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", remark='" + remark + '\'' +
            '}';
  }
}
