package com.zhenhong.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author lzhya
 * @Date 2021/2/27 14:47
 * @Version 1.0
 */
@Setter
@Getter
public class ProductAnalysisVo {
    private Integer month;
    private Integer day;
    private Integer passCount;
    private Integer defeatCount;
    private Integer totalCount;
}
