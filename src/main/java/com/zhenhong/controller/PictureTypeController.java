package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhenhong.mapper.PictureMapper;
import com.zhenhong.mapper.PictureTypeMapper;
import com.zhenhong.pojo.Picture;
import com.zhenhong.pojo.PictureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2021/3/1 17:38
 * @Version 1.0
 */
@Controller
public class PictureTypeController {
    @Autowired
    private PictureTypeMapper pictureTypeMapper;
    @Autowired
    private PictureMapper pictureMapper;
    /**
     * 分类管理
     */
    @RequestMapping("/pictureType/sortAds")
    public String sortAds(Model model){
        List<PictureType> pictureTypeList = pictureTypeMapper.selectList(null);
        model.addAttribute("pictureTypeList",pictureTypeList);
        return "admin/Sort_ads";
    }

    /**
     * 添加
     */
    @RequestMapping("/pictureType/updateStatus")
    @ResponseBody
    public String updateStatus(Integer id,Integer status){
        UpdateWrapper<PictureType> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        PictureType pictureType = new PictureType();
        pictureType.setStatus(status);
        int res = pictureTypeMapper.update(pictureType, updateWrapper);
        if (res==1){
            return "success";
        }else {
            return "defeat";
        }
    }

    /**
     * 根据id查询
     */
    @RequestMapping("/pictureType/queryById")
    public String queryById(Integer id,Model model){
        PictureType pictureType = pictureTypeMapper.selectById(id);
        model.addAttribute("pictureType",pictureType);
        return "admin/pictureType_echo";
    }

    /**
     * 添加
     */
    @RequestMapping("/pictureType/add")
    @ResponseBody
    public String add(PictureType pictureType){
        QueryWrapper<PictureType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",pictureType.getName());
        PictureType pictureType1 = pictureTypeMapper.selectOne(queryWrapper);
        if (pictureType1!=null){
            return "have";
        }else {
            pictureTypeMapper.insert(pictureType);
            return "添加成功";
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/pictureType/update")
    @ResponseBody
    public String update(PictureType pictureType){
        UpdateWrapper<PictureType> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",pictureType.getId());
        int res = pictureTypeMapper.update(pictureType, updateWrapper);
        if (res==1){
            return "修改成功";
        }else {
            return "修改失败";
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/pictureType/deleteById")
    @ResponseBody
    public String deleteById(Integer id){
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",id);
        List<Picture> pictureList = pictureMapper.selectList(queryWrapper);
        if (pictureList.size()==0){
            pictureTypeMapper.deleteById(id);
            return "已删除！";
        }else{
            return "sb";
        }
    }

}
