package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.IntegralMapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.Integral;
import com.zhenhong.pojo.User;
import com.zhenhong.vo.IntegralVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户积分
 * @Author lzhya
 * @Date 2021/2/18 13:35
 * @Version 1.0
 */
@Controller
public class IntegralController {
    @Autowired
    private IntegralMapper integralMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户积分详情
     */
    @RequestMapping("/user/integral")
    public String integral(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        User user = userMapper.selectById(loginUser.getId());
        QueryWrapper<Integral> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<Integral> integralList = integralMapper.selectList(queryWrapper);
        List<IntegralVo> integralVoList = integralList.stream().map(e -> new IntegralVo(e.getId(),
                e.getGoodsId(),
                e.getFraction(),
                e.getCreateTime(),
                e.getRemark())).collect(Collectors.toList());

        for (IntegralVo integralVo : integralVoList) {
            Goods goods = goodsMapper.selectById(integralVo.getGoodsId());
            integralVo.setGoods(goods);
        }
        model.addAttribute("integralVoList",integralVoList);
        model.addAttribute("integral",user.getIntegral());
        return "user/integral";
    }
}
