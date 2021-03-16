package com.zhenhong.vo;

import com.zhenhong.pojo.SecondType;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2021/1/5 17:39
 * @Version 1.0
 */
public class CategoryVo {
    private Integer id;
    private String name;
    private List<SecondType> secondTypeList;

    public CategoryVo(Integer id, String name) {
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

    public List<SecondType> getSecondTypeList() {
        return secondTypeList;
    }

    public void setSecondTypeList(List<SecondType> secondTypeList) {
        this.secondTypeList = secondTypeList;
    }
}
