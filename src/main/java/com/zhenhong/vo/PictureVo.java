package com.zhenhong.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author lzhya
 * @Date 2021/3/14 15:58
 * @Version 1.0
 */
@Setter
@Getter
public class PictureVo {
    private Integer id;
    private Integer sort;
    private Integer typeId;
    private String typeName;
    private double width;
    private double height;
    private String url;
    private Integer status;
    private String pictures;
    private String createTime;

    public PictureVo() {
    }

    public PictureVo(Integer id, Integer sort, Integer typeId, double width, double height, String url, Integer status, String pictures, String createTime) {
        this.id = id;
        this.sort = sort;
        this.typeId = typeId;
        this.width = width;
        this.height = height;
        this.url = url;
        this.status = status;
        this.pictures = pictures;
        this.createTime = createTime;
    }
}
