package com.zhenhong.vo;

import com.zhenhong.pojo.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2021/1/19 17:47
 * @Version 1.0
 */
@Setter
@Getter
public class BrowseRecordsVo {
    private Integer id;
    private Integer user_id;
    private Integer goods_id;
    private String createTime;
    private List<Goods> goodsList;

    public BrowseRecordsVo(Integer id, Integer user_id, Integer goods_id, String createTime) {
        this.id = id;
        this.user_id = user_id;
        this.goods_id = goods_id;
        this.createTime = createTime;
    }
}
