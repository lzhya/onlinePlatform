package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.Evaluate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author lzhya
 * @Date 2021/3/18 20:58
 * @Version 1.0
 */
@Repository
@Mapper
public interface EvaluationMapper extends BaseMapper<Evaluate> {
}
