package com.zhenhong.vo;

import com.zhenhong.pojo.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**商品详情
 * @Author lzhya
 * @Date 2021/1/5 14:51
 * @Version 1.0
 */
@Setter
@Getter
public class ProductDetailsVo {
    private Integer id;  //商品id
    private String number;  //商品编号
    private Integer fId;  //所属一级分类id
    private Integer sId; //所属二级分类id
    private String fName;  //所属一级分类名称
    private String sName;  //所属二级分类名称
    private String goodsTitle;  //商品标题
    private String photoUrl;  //图片地址
    private double price;   //价格
    private Integer stock; //库存
    private Integer count;  //数量
    private String uploadDate;  //时间
    private String description;  //商品详情
    private Integer pageView;  //浏览量
    private Integer evaluateCount;  //评价量
    private Integer userId; //所属用户id
    private User user;  //所属用户信息
    private Integer status;  //商品状态

    public ProductDetailsVo() {
    }

    public ProductDetailsVo(Integer id, String goodsTitle, String photoUrl, double price, Integer count, String description, Integer pageView, Integer userId) {
        this.id = id;
        this.goodsTitle = goodsTitle;
        this.photoUrl = photoUrl;
        this.price = price;
        this.count = count;
        this.description = description;
        this.pageView = pageView;
        this.userId = userId;
    }

    public ProductDetailsVo(Integer id, String number, String goodsTitle, String photoUrl, double price, Integer count, Integer stock,String uploadDate, String description, Integer pageView, Integer userId) {
        this.id = id;
        this.number = number;
        this.goodsTitle = goodsTitle;
        this.photoUrl = photoUrl;
        this.price = price;
        this.count = count;
        this.stock = stock;
        this.uploadDate = uploadDate;
        this.description = description;
        this.pageView = pageView;
        this.userId = userId;
    }

    public ProductDetailsVo(Integer id, String number, Integer fId, Integer sId, String goodsTitle, String photoUrl, double price, Integer count, Integer stock, String uploadDate, Integer pageView, String description, Integer userId) {
        this.id = id;
        this.number = number;
        this.fId = fId;
        this.sId = sId;
        this.goodsTitle = goodsTitle;
        this.photoUrl = photoUrl;
        this.price = price;
        this.count = count;
        this.stock = stock;
        this.uploadDate = uploadDate;
        this.pageView = pageView;
        this.description = description;
        this.userId = userId;
    }
}
