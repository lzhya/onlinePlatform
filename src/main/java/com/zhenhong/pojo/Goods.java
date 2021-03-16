package com.zhenhong.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品-用户-类型 实体类
 */
@Setter
@Getter
public class Goods {
  //商品id
  private Integer id;
  //商品标号
  private String number;
  //商品所属一级分类id
  private Integer firstTypeId;
  //商品所属二级分类id
  private Integer secondTypeId;
  //商品标题
  private String goodsTitle;
  //图片地址
  private String photoUrl;
  //单价
  private double price;
  //发布数量
  private Integer count;
  //库存
  private Integer stock;
  //上传时间
  private String uploadDate;
  //详情
  private String description;
  //浏览量
  private Integer pageView;
  //评价量
  private Integer evaluateCount;
  //所属用户id
  private Integer userId;
  //商品状态
  private Integer status;

}
