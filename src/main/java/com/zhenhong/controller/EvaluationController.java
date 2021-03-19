package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.EvaluationMapper;
import com.zhenhong.pojo.Evaluate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author lzhya
 * @Date 2021/3/18 20:59
 * @Version 1.0
 */
@Controller
public class EvaluationController {
    @Autowired
    private EvaluationMapper evaluationMapper;
    @RequestMapping("/evaluation/add")
    @ResponseBody
    public String add(Integer orderId,double goodsDes,double serviceAttitude,double deliverySpeed,String context){
        QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        Evaluate one = evaluationMapper.selectOne(queryWrapper);
        if (one==null){
            Evaluate evaluate = new Evaluate();
            evaluate.setContent(context);
            evaluate.setDeliverySpeed(deliverySpeed);
            evaluate.setGoodsDescription(goodsDes);
            evaluate.setServiceAttitude(serviceAttitude);
            evaluate.setOrderId(orderId);
            int insert = evaluationMapper.insert(evaluate);
            if (insert==1){
                return "发表成功！";
            }else {
                return "发表失败";
            }
        }else {
            return "不能重复评论！";
        }
    }
}
