package com.zhenhong.vo;

import com.zhenhong.pojo.SecondType;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/17 14:58
 * @Version 1.0
 */
public class ProductCategoryVo {
    private Integer id;
    private String name;
    private List<ProductCategoryVo> children;
    private List<ProductDetailsVo> productDetailsVoList;

    public ProductCategoryVo() {
    }

    public ProductCategoryVo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public List<ProductCategoryVo> getChildren() {
        return children;
    }

    public void setChildren(List<ProductCategoryVo> children) {
        this.children = children;
    }

    public List<ProductDetailsVo> getProductDetailsVoList() {
        return productDetailsVoList;
    }

    public void setProductDetailsVoList(List<ProductDetailsVo> productDetailsVoList) {
        this.productDetailsVoList = productDetailsVoList;
    }

}
