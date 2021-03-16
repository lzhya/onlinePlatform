package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 用户商品浏览记录
 */
public class BrowseRecords {
  @TableId(type = IdType.AUTO)
  private Integer id;
  @TableField("user_id")
  private Integer userId;
  @TableField("goods_id")
  private Integer goodsId;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String createTime;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public Integer getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(Integer goodsId) {
    this.goodsId = goodsId;
  }


  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String creatTime) {
    this.createTime = creatTime;
  }

}
