package com.zhenhong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhenhong.mapper.BrowseRecordsMapper;
import com.zhenhong.pojo.BrowseRecords;
import com.zhenhong.service.BrowseRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lzhya
 * @Date 2021/1/19 17:45
 * @Version 1.0
 */
@Service
public class BrowseRecordsServiceImpl extends ServiceImpl<BrowseRecordsMapper, BrowseRecords> implements BrowseRecordsService {
    @Autowired
    private BrowseRecordsMapper browseRecordsMapper;
    @Override
    public Integer getBrowseCountByUserId(Integer userId) {
        return browseRecordsMapper.getBrowseCountByUserId(userId);
    }
}
