package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhenhong.mapper.FirstTypeMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.SecondTypeMapper;
import com.zhenhong.pojo.FirstType;
import com.zhenhong.pojo.SecondType;
import com.zhenhong.service.FirstTypeService;
import com.zhenhong.service.ProductCatgoryService;
import com.zhenhong.vo.CategoryVo;
import com.zhenhong.vo.ProductCategoryVo;
import com.zhenhong.vo.ProductCategoryVo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2020/12/5 17:55
 * @Version 1.0
 */

@Controller
public class FirstTypeController {
    @Autowired
    FirstTypeMapper firstTypeMapper;
    @Autowired
    FirstTypeService firstTypeService;
    @Autowired
    private SecondTypeMapper secondTypeMapper;
    @Autowired
    private ProductCatgoryService productCatgoryService;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 商品分类查询
     * @return
     */
    @RequestMapping("/firstType/select")
    public String querySecondByFrist() {
        return "redirect:/admin/category.html";
    }

    /**
     * 查询商品分类关系 树形结构
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/select")
    @ResponseBody
    public String  select() throws JsonProcessingException {
        String catgoryJson = productCatgoryService.catgoryJson();
        return catgoryJson;
    }

    @RequestMapping("/catgory/manage")
    public String toCatgory(Model model){
        String catgoryJson = productCatgoryService.catgoryJson();
        model.addAttribute("catgoryJson",catgoryJson);
        return "admin/Category_Manage";
    }

    /**
     * 跳转到商品类别添加页面
     */
    @RequestMapping("/firstType/toAdd")
    public String toAdd(){
        return "admin/product-category-add";
    }

    /**
     * 添加一级分类
     * @param name  名称
     * @param remark  备注
     * @param model
     * @return
     */
    @RequestMapping("/firstType/add")
    @ResponseBody
    public String add(String name,String remark,Model model){
        QueryWrapper<FirstType> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        FirstType type = firstTypeMapper.selectOne(wrapper);
        if (type==null){
            int maxId = firstTypeMapper.getMaxId();
            FirstType firstType = new FirstType();
            firstType.setId(maxId+1);
            firstType.setName(name);
            firstType.setRemark(remark);
            firstTypeService.saveOne(firstType);
            return "添加成功";
        }else{
            return "该类型已经存在";
        }

    }

    /**
     * 根据id修改一级分类
     * @param fname 名称
     * @param fid  id
     * @return
     */
    @RequestMapping("/firstType/edit")
    @ResponseBody
    public String edit(String fname,String fid){
        Integer id = Integer.parseInt(fid.substring(1));

        QueryWrapper<FirstType> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);

        FirstType firstType = new FirstType();
        firstType.setId(id);
        firstType.setName(fname);

        firstTypeMapper.update(firstType,wrapper);
        return "修改成功";
    }

    @RequestMapping("/firstType/delete")
    @ResponseBody
    public String delete(String fid){
        QueryWrapper wrapper=new QueryWrapper();
        Integer id = Integer.parseInt(fid.substring(1));

        //根基一级分类查询对应商品
        wrapper=new QueryWrapper();
        wrapper.eq("first_type_id",id);
        List fList = goodsMapper.selectList(wrapper);
        if (fList.isEmpty()){   //如果集合为空，可以删除
            wrapper=new QueryWrapper();
            wrapper.eq("id",id);
            firstTypeMapper.delete(wrapper);  //删除一级分类
            wrapper=new QueryWrapper();
            wrapper.eq("first_type_id",id);  //删除一级分类所对应的二级分类
            secondTypeMapper.delete(wrapper);
            return "删除成功";
        }
        return "该分类下存在商品，不能删除！";
    }

    @RequestMapping("/getAllLevelOne")
    @ResponseBody
    public List<FirstType> getAllLevelOne(){
        List<FirstType> data = firstTypeMapper.selectList(null);
        return data;
    }

    @RequestMapping("/category")
    @ResponseBody
    public List<CategoryVo> categoryVoList(){
        List<CategoryVo> categoryVos = firstTypeService.categoryVoList();
        return categoryVos;
    }
}
