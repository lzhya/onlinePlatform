package com.zhenhong.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户实体类
 */
@NoArgsConstructor
@Setter
@Getter
public class User {
  /**
   * id
   */
  @ExcelIgnore
  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 用户昵称
   */
  @ExcelProperty(value="用户名",index = 0)
  private String nickname;

  /**
   * 用户真实姓名
   */
  @ExcelIgnore
  private String realname;

  /**
   * 电话号码
   */
  @ExcelProperty(value = "手机号码",index = 2)
  private String phone;

  /**
   * 用户邮箱
   */
  @ExcelProperty(value = "邮箱",index = 3)
  private String email;

  /**
   * 性别
   */
  @ExcelProperty(value = "性别",index = 1)
  private String gender;

  /**
   * 用户头像
   */
  @ExcelIgnore
  private String photo;

  /**
   * 注册时间
   */
  @ExcelProperty(value = "注册时间",index = 4)
  @TableField(fill = FieldFill.INSERT)
  private String registerDate;

  /**
   * 用户密码
   */
  @ExcelIgnore
  private String password;

  /**
   * 用户积分
   */
  @ExcelProperty(value = "用户积分",index = 5)
  private Integer integral;

  /**
   * 用户地址
   */
  @ExcelProperty(value = "地址",index = 6)
  private String address;
  /**
   * 用户身份
   */
  @ExcelIgnore
  private Integer identity;
  /**
   * 用户状态
   */
  @ExcelProperty(value = "状态",index = 7)
  private Integer status;

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", nickname='" + nickname + '\'' +
            ", realname='" + realname + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", gender='" + gender + '\'' +
            ", photo='" + photo + '\'' +
            ", registerDate='" + registerDate + '\'' +
            ", password='" + password + '\'' +
            ", integral=" + integral +
            ", address='" + address + '\'' +
            ", identity=" + identity +
            ", status=" + status +
            '}';
  }
}
