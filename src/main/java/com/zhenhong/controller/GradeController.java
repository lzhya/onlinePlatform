package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.User;
import com.zhenhong.vo.GradeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 用户等级
 * @Author lzhya
 * @Date 2021/1/28 20:00
 * @Version 1.0
 */
@Controller
public class GradeController {
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/grade")
    @ResponseBody
    public List<GradeVo> grade(){
        Integer ordinary = userMapper.getGradeCount(100, 300);
        Integer gold = userMapper.getGradeCount(301, 500);
        Integer diamond = userMapper.getGradeCount(501, 700);
        Integer blueDiamond = userMapper.getGradeCount(701, 900);
        Integer blackDiamond = userMapper.getGradeCount(901, 100000);

        GradeVo ordinaryVo = new GradeVo(ordinary,"普通用户");
        GradeVo goldVo = new GradeVo(gold,"金牌用户");
        GradeVo diamondVo = new GradeVo(diamond,"钻石用户");
        GradeVo blueDiamondVo = new GradeVo(blueDiamond,"蓝钻用户");
        GradeVo blackDiamondVo = new GradeVo(blackDiamond,"黑钻用户");

        ArrayList<GradeVo> list = new ArrayList<>();
        list.add(ordinaryVo);
        list.add(goldVo);
        list.add(diamondVo);
        list.add(blueDiamondVo);
        list.add(blackDiamondVo);
        return list;
    }

    @RequestMapping("selectUserByGrade")
    public String selectUserByGrade(String grade, Model model){
        QueryWrapper<User> queryWrapper ;
        List<User> userList ;
        switch (grade){
            case "普通用户":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("status",1);
                queryWrapper.between("integral",100,300);
                userList = userMapper.selectList(queryWrapper);
                break;
            case "金牌用户":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("status",1);
                queryWrapper.between("integral",301,500);
                userList = userMapper.selectList(queryWrapper);
                break;
            case "钻石用户":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("status",1);
                queryWrapper.between("integral",501,700);
                userList = userMapper.selectList(queryWrapper);
                break;
            case "蓝钻用户":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("status",1);
                queryWrapper.between("integral",701,900);
                userList = userMapper.selectList(queryWrapper);
                break;
            case "黑钻用户":
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("status",1);
                queryWrapper.gt("integral",900);
                userList = userMapper.selectList(queryWrapper);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + grade);
        }
        model.addAttribute("userList",userList);
        return "admin/user_grade";
    }
}
