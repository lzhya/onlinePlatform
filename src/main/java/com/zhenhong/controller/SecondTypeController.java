package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.FirstTypeMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.SecondTypeMapper;
import com.zhenhong.pojo.FirstType;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.SecondType;
import com.zhenhong.service.FirstTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/10 22:29
 * @Version 1.0
 */
@Controller
public class SecondTypeController {
    @Autowired
    private FirstTypeService firstTypeService;

    @Autowired
    private SecondTypeMapper secondTypeMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 添加二级分类
     * @param fid 一级分类id
     * @param sname 二级分类名称
     * @return
     */
    @RequestMapping("/secondType/add")
    @ResponseBody
    public String add(String sname,String fid,Model model){
        int id = Integer.parseInt(fid.substring(1));
        //根据名称查询是否存在名称为sname
        //如果存在
        QueryWrapper<SecondType> wrapper = new QueryWrapper<>();
        wrapper.eq("name",sname);
        SecondType st = secondTypeMapper.selectOne(wrapper);
        if (st==null){
            SecondType secondType = new SecondType();
            secondType.setFirstTypeId(id);
            secondType.setName(sname);
            secondTypeMapper.insert(secondType);
            return "添加成功";
        }
        return "该类型已存在！";

    }

    /**
     * 根据id修改二级分类
     * @param sname
     * @param sid
     * @return
     */
    @RequestMapping("/secondType/edit")
    @ResponseBody
    public String edit(String sname,String sid){
        Integer id = Integer.parseInt(sid.substring(4));

        QueryWrapper<SecondType> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);

        SecondType secondType = new SecondType();
        secondType.setId(id);
        secondType.setName(sname);

        secondTypeMapper.update(secondType,wrapper);
        return "修改成功";
    }

    /**
     * 根据id删除二级分类
     * @param sid 二级分类id
     * @return
     */
    @RequestMapping("/secondType/delete")
    @ResponseBody
    public String delete(String sid){
        Integer id = Integer.parseInt(sid.substring(4));
        //根基二级分类查询对应商品
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("id",id);
        List sList = goodsMapper.selectList(wrapper);
        if (sList.isEmpty()){   //如果集合为空，可以删除
            wrapper=new QueryWrapper();
            wrapper.eq("id",id);
            secondTypeMapper.delete(wrapper);
            return "删除成功";
        }
        return "该分类下存在商品，不能删除！";
    }

    @RequestMapping("/getAllLevelById")
    @ResponseBody
    public List<SecondType> getAllLevelById(Integer fid){
        QueryWrapper<SecondType> wrapper=new QueryWrapper<>();
        wrapper.eq("first_type_id", fid);
        List<SecondType> secondTypeList = secondTypeMapper.selectList(wrapper);
        return secondTypeList;
    }
}
