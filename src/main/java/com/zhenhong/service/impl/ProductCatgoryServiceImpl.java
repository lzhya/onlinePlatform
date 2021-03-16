package com.zhenhong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhenhong.mapper.FirstTypeMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.SecondTypeMapper;
import com.zhenhong.pojo.FirstType;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.SecondType;
import com.zhenhong.service.GoodsService;
import com.zhenhong.service.ProductCatgoryService;
import com.zhenhong.vo.ProductCategoryVo;
import com.zhenhong.vo.ProductCategoryVo1;
import com.zhenhong.vo.ProductDetailsVo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2020/12/17 16:20
 * @Version 1.0
 */
@Service
public class ProductCatgoryServiceImpl implements ProductCatgoryService {
    @Autowired
    FirstTypeMapper firstTypeMapper;
    @Autowired
    SecondTypeMapper secondTypeMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 返回商品分类以及对应的商品信息
     * @return
     */
    @Override
    public List<ProductCategoryVo> catgoryList() {
        //查询所有的一级分类
        List<FirstType> firstTypeList = firstTypeMapper.selectList(null);
        //将查询结果映射到ProductCategoryVo类
        List<ProductCategoryVo> productCategoryVos = firstTypeList.stream().map(e -> new ProductCategoryVo(e.getId(), e.getName())).collect(Collectors.toList());
        //为ProductCategoryVo的productDetailsVoList属性赋值
        for (ProductCategoryVo productCategoryVo : productCategoryVos) {
            List<ProductDetailsVo> productDetailsVoList = goodsService.getGoodsByLevelOne(productCategoryVo.getId()).stream().map(e ->
                    new ProductDetailsVo(e.getId(),
                            e.getGoodsTitle(),
                            e.getPhotoUrl(),
                            e.getPrice(),
                            e.getCount(),
                            e.getDescription(),
                            e.getPageView(),
                            e.getUserId())).collect(Collectors.toList());

            productCategoryVo.setProductDetailsVoList(productDetailsVoList);
        }
        return productCategoryVos;
    }

    @Override
    public ProductCategoryVo productList(Integer id) {
        FirstType firstType = firstTypeMapper.selectById(id);

        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("first_type_id",id);
        queryWrapper.eq("status",1);
        queryWrapper.gt("stock",0);
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);
        List<ProductDetailsVo> productDetailsVoList = goodsList.stream().map(e -> new ProductDetailsVo(e.getId(),
                e.getGoodsTitle(),
                e.getPhotoUrl(),
                e.getPrice(),
                e.getCount(),
                e.getDescription(),
                e.getPageView(),
                e.getUserId())).collect(Collectors.toList());

        ProductCategoryVo productCategoryVo = new ProductCategoryVo();
        productCategoryVo.setId(id);
        productCategoryVo.setName(firstType.getName());
        productCategoryVo.setProductDetailsVoList(productDetailsVoList);
        return productCategoryVo;
    }

    @SneakyThrows
    @Override
    public String catgoryJson()  {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map1=new HashMap<>();
        map1.put("code",200);
        map1.put("message","操作成功");

        List<FirstType> fl = firstTypeMapper.selectList(null);  //查询所有一级分类
        List<ProductCategoryVo1> f1Vo = fl.stream().map(e -> new ProductCategoryVo1("00"+e.getId(), e.getName(),false,"0")).collect(Collectors.toList());
        for (ProductCategoryVo1 productCategoryVo1 : f1Vo) {
            QueryWrapper<SecondType> wrapper = new QueryWrapper<>();
            wrapper.eq("first_type_id",productCategoryVo1.getId());
            List<SecondType> sf = secondTypeMapper.selectList(wrapper);
            if (sf.isEmpty()){
                productCategoryVo1.setChildren(null);
            }
            List<ProductCategoryVo1> children = sf.stream().map(e -> new ProductCategoryVo1("00"+e.getFirstTypeId()+"00"+e.getId(), e.getName(), true, "00"+e.getFirstTypeId())).collect(Collectors.toList());
            productCategoryVo1.setChildren(children);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("status",map1);
        map.put("data",f1Vo);
        String json = objectMapper.writeValueAsString(map);
        return json;

    }
}
