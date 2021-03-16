package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.SecondType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author lzhya
 * @Date 2020/12/8 16:15
 * @Version 1.0
 */
@Repository
@Mapper
public interface SecondTypeMapper extends BaseMapper<SecondType> {
}
