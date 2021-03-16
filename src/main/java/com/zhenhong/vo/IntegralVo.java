package com.zhenhong.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zhenhong.pojo.Goods;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author lzhya
 * @Date 2021/2/18 13:37
 * @Version 1.0
 */
@Setter
@Getter
public class IntegralVo {
    private Integer id;
    private Integer goodsId;
    private Goods goods;
    private Integer fraction;
    private String createTime;
    private String remark;

    public IntegralVo(Integer id, Integer goodsId, Integer fraction, String createTime, String remark) {
        this.id = id;
        this.goodsId = goodsId;
        this.fraction = fraction;
        this.createTime = createTime;
        this.remark = remark;
    }
}
