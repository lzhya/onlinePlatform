package com.zhenhong.vo;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/17 14:58
 * @Version 1.0
 */
public class ProductCategoryVo1 {
    private String id;
    private String title;
    private boolean last;
    private String parentId;
    private List<ProductCategoryVo1> children;

    public ProductCategoryVo1(String id, String title, boolean last, String parentId) {
        this.id = id;
        this.title = title;
        this.last = last;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<ProductCategoryVo1> getChildren() {
        return children;
    }

    public void setChildren(List<ProductCategoryVo1> children) {
        this.children = children;
    }
}
