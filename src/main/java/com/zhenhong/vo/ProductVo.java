package com.zhenhong.vo;

import com.zhenhong.pojo.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author lzhya
 * @Date 2020/12/18 20:22
 * @Version 1.0
 */
@Setter
@Getter
public class ProductVo {
    private Integer id;
    private String fName;
    private String name;
    private String title;
    private double price;
    private Integer count;
    private Integer stock; //库存
    private String photourl;
    private Integer pageView;
    private String description;
    private User user;

    public ProductVo() {
    }

    public ProductVo(Integer id, String title, double price, Integer count, String photourl, Integer pageView, String description,User user) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.count = count;
        this.photourl = photourl;
        this.pageView = pageView;
        this.description = description;
        this.user = user;
    }

    public ProductVo(Integer id, String name, String title, double price, Integer count, String photourl, Integer pageView, String description) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.price = price;
        this.count = count;
        this.photourl = photourl;
        this.pageView = pageView;
        this.description = description;
    }
}
