package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhenhong.mapper.PictureMapper;
import com.zhenhong.mapper.PictureTypeMapper;
import com.zhenhong.pojo.Picture;
import com.zhenhong.pojo.PictureType;
import com.zhenhong.vo.PictureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2021/3/13 14:53
 * @Version 1.0
 */
@Controller
public class PictureController {
    @Autowired
    private PictureTypeMapper pictureTypeMapper;
    @Autowired
    private PictureMapper pictureMapper;

    /**
     * 查询全部图片
     */
    @RequestMapping("/picture/queryAll")
    public String queryAll(Model model){
        List<PictureType> pictureTypeList = pictureTypeMapper.selectList(null);
        List<Picture> pictureList = pictureMapper.selectList(null);
        List<PictureVo> pictureVoList = pictureList.stream().map(e -> new PictureVo(e.getId(),
                e.getSort(),
                e.getTypeId(),
                e.getWidth(),
                e.getHeight(),
                e.getUrl(),
                e.getStatus(),
                e.getPictures(),
                e.getCreateTime())).collect(Collectors.toList());
        for (PictureVo pictureVo : pictureVoList) {
            Integer typeId = pictureVo.getTypeId();
            PictureType pictureType = pictureTypeMapper.selectById(typeId);
            pictureVo.setTypeName(pictureType.getName());
        }
        model.addAttribute("pictureTypeList",pictureTypeList);
        model.addAttribute("pictureVoList",pictureVoList);
        return "admin/advertising";
    }

    /**
     * 根据分类查询图片
     */
    @RequestMapping("/picture/selectListByType")
    public String selectListByType(Integer tId,Model model){
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",tId);
        List<Picture> pictureList = pictureMapper.selectList(queryWrapper);

        List<PictureType> pictureTypeList = pictureTypeMapper.selectList(null);
        List<PictureVo> pictureVoList = pictureList.stream().map(e -> new PictureVo(e.getId(),
                e.getSort(),
                e.getTypeId(),
                e.getWidth(),
                e.getHeight(),
                e.getUrl(),
                e.getStatus(),
                e.getPictures(),
                e.getCreateTime())).collect(Collectors.toList());
        for (PictureVo pictureVo : pictureVoList) {
            Integer typeId = pictureVo.getTypeId();
            PictureType pictureType = pictureTypeMapper.selectById(typeId);
            pictureVo.setTypeName(pictureType.getName());
        }
        model.addAttribute("pictureTypeList",pictureTypeList);
        model.addAttribute("pictureVoList",pictureVoList);
        return "admin/advertising";
    }

    /**
     * 添加广告
     */
    @RequestMapping("/picture/add")
    @ResponseBody
    public String add(Integer typeId,double width,double height,Integer sort,Integer status,String url,String pictures){
        Picture picture = new Picture();
        picture.setTypeId(typeId);
        picture.setWidth(width);
        picture.setHeight(height);
        picture.setSort(sort);
        picture.setStatus(status);
        picture.setUrl(url);
        picture.setPictures(pictures);
        int res = pictureMapper.insert(picture);
        if (res==1){
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 修改图片状态
     */
    @RequestMapping("/picture/updateStatus")
    @ResponseBody
    public String updateStatus(Integer id){
        Picture picture = pictureMapper.selectById(id);
        UpdateWrapper<Picture> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        if (picture.getStatus()==0){
            updateWrapper.set("status",1);
        }else {
            updateWrapper.set("status",0);
        }
        int update = pictureMapper.update(null, updateWrapper);
        if (update==1){
            return "";
        }
        return "";
    }

    /**
     * 删除
     */
    @RequestMapping("/picture/deleteById")
    @ResponseBody
    public String deleteById(Integer id){
        int res = pictureMapper.deleteById(id);
        if (res==1){
            return "已删除！";
        }else{
            return "sb";
        }
    }

    @RequestMapping("/picture/selectById")
    public String selectById(Integer id,Model model){
        List<PictureType> pictureTypeList = pictureTypeMapper.selectList(null);

        Picture picture = pictureMapper.selectById(id);

        PictureType pictureType = pictureTypeMapper.selectById(picture.getTypeId());

        PictureVo pictureVo = new PictureVo();
        pictureVo.setId(picture.getId());
        pictureVo.setHeight(picture.getHeight());
        pictureVo.setWidth(picture.getWidth());
        pictureVo.setSort(picture.getSort());
        pictureVo.setStatus(picture.getStatus());
        pictureVo.setUrl(picture.getUrl());
        pictureVo.setPictures(picture.getPictures());
        pictureVo.setTypeName(pictureType.getName());

        model.addAttribute("pictureVo",pictureVo);
        model.addAttribute("pictureTypeList",pictureTypeList);
        return "admin/adv_echo";
    }

    /**
     * 修改图片信息
     */
    @RequestMapping("/picture/update")
    @ResponseBody
    public String updateById(Integer id,Integer typeId,double width,double height,Integer sort,Integer status,String url,String pictures){
        UpdateWrapper<Picture> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.set("sort",sort);
        updateWrapper.set("type_id",typeId);
        updateWrapper.set("width",width);
        updateWrapper.set("height",height);
        updateWrapper.set("url",url);
        updateWrapper.set("status",status);
        updateWrapper.set("pictures",pictures);
        int update = pictureMapper.update(null, updateWrapper);
        if (update==1){
            return "修改成功";
        }else{
            return "修改失败";
        }
    }
}
