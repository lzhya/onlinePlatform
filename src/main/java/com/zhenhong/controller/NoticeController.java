package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.pojo.Goods;
import com.zhenhong.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lzhya
 * @Date 2020/12/9 23:25
 * @Version 1.0
 * 通知管理
 */
@RestController
public class NoticeController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/notice/send")
    public String messageSending(Integer id,String message){
        String key="product"+id;
        //将拒绝原因保存在redis中
        redisTemplate.opsForValue().set(key,message);
        //修改商品状态
        goodsService.updateGoodsStatus(4, id);
        return "已发送";
    }

    /**
     *
     */
    @RequestMapping("/ask")
    @ResponseBody
    public String ask(Integer id){
        String key="product"+id;
        String reason = redisTemplate.opsForValue().get(key);
        return reason;
    }
}
