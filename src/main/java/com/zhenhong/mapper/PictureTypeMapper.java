package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.PictureType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author lzhya
 * @Date 2021/3/1 17:37
 * @Version 1.0
 */
@Repository
@Mapper
public interface PictureTypeMapper extends BaseMapper<PictureType> {
}
