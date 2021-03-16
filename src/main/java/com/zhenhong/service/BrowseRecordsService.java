package com.zhenhong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhenhong.pojo.BrowseRecords;

/**
 * @Author lzhya
 * @Date 2021/1/19 17:45
 * @Version 1.0
 */
public interface BrowseRecordsService extends IService<BrowseRecords> {
    Integer getBrowseCountByUserId(Integer userId);
}
