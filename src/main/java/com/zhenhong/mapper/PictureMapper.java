package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.Picture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author lzhya
 * @Date 2021/3/13 14:51
 * @Version 1.0
 */

@Repository
@Mapper
public interface PictureMapper extends BaseMapper<Picture> {

}
