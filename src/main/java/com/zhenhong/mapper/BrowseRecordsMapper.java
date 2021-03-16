package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.BrowseRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author lzhya
 * @Date 2021/1/19 17:43
 * @Version 1.0
 */
@Repository
@Mapper
public interface BrowseRecordsMapper extends BaseMapper<BrowseRecords> {
    //查询用户浏览商品次数
    @Select("SELECT COUNT(*) FROM browse_records WHERE user_id =#{user_id}")
    Integer getBrowseCountByUserId(Integer userId);
}
