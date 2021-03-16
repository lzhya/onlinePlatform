package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.Integral;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author lzhya
 * @Date 2021/2/18 12:58
 * @Version 1.0
 */
@Repository
@Mapper
public interface IntegralMapper extends BaseMapper<Integral> {
}
