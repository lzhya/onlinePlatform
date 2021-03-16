package com.zhenhong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.FirstTypeMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.SecondTypeMapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.FirstType;
import com.zhenhong.pojo.Goods;
import com.zhenhong.pojo.SecondType;
import com.zhenhong.pojo.User;
import com.zhenhong.service.GoodsService;
import com.zhenhong.vo.ProductDetailsVo;
import com.zhenhong.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2020/12/6 21:39
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FirstTypeMapper firstTypeMapper;
    @Autowired
    private SecondTypeMapper secondTypeMapper;

    @Override
    public int getGoodsTotalNum() {
        return goodsMapper.getGoodsTotalNum();
    }

    @Override
    public int getStopGoodsNum() {
        return goodsMapper.getStopGoodsNum();
    }

    @Override
    public int setCheckToOnline(Integer id) {
        return goodsMapper.setCheckToOnline(id);
    }

    @Override
    public int selectTotalNum() {
        return goodsMapper.selectTotalNum();
    }

    @Override
    public int getCheckNum() {
        return goodsMapper.getCheckNum();
    }

    @Override
    public List<Goods> getGoodsByLevelOne(Integer id) {
        return goodsMapper.getGoodsByLevelOne(id);
    }

    @Override
    public Integer getMaxId() {
        return goodsMapper.getMaxId();
    }

    @Override
    public ProductVo product(Integer id) {
        //根据id查询商品
        QueryWrapper wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        Goods goods = goodsMapper.selectOne(wrapper);
        //获得该商品用户的信息
        Integer userId = goods.getUserId();
        wrapper=new QueryWrapper<>();
        wrapper.eq("id",userId);
        User user = userMapper.selectOne(wrapper);
        //商品分类信息
        Integer firstTypeId = goods.getFirstTypeId();
        FirstType firstType = firstTypeMapper.selectById(firstTypeId);

        ProductVo productVo = new ProductVo();
        productVo.setId(goods.getId());
        productVo.setFName(firstType.getName());
        productVo.setName(goods.getNumber());
        productVo.setTitle(goods.getGoodsTitle());
        productVo.setPageView(goods.getPageView());
        productVo.setCount(goods.getCount());
        productVo.setStock(goods.getStock());
        productVo.setPhotourl(goods.getPhotoUrl());
        productVo.setPrice(goods.getPrice());
        productVo.setDescription(goods.getDescription());
        productVo.setUser(user);

        return productVo;
    }

    @Override
    public Integer checkPageView(Integer id) {
        return goodsMapper.checkPageView(id);
    }

    /**
     * 查询不同状态商品的详情信息
     * @param status 商品状态
     * @return
     */
    @Override
    public List<ProductDetailsVo> productDetailsVoList(Integer status) {
        //查询状态为 status 商品信息
        QueryWrapper<Goods> goodsWrapper = new QueryWrapper<>();
        goodsWrapper.eq("status",status);
        goodsWrapper.gt("count",0);
        List<Goods> goodsList = goodsMapper.selectList(goodsWrapper);
        //将查询结果映射到ProductDetailsVo
        List<ProductDetailsVo> productDetailsVoList = goodsList.stream().map(e -> new ProductDetailsVo(e.getId(),
                e.getNumber(),
                e.getFirstTypeId(),
                e.getSecondTypeId(),
                e.getGoodsTitle(),
                e.getPhotoUrl(),
                e.getPrice(),
                e.getCount(),
                e.getStock(),
                e.getUploadDate(),
                e.getPageView(),
                e.getDescription(),
                e.getUserId())).collect(Collectors.toList());

        for (ProductDetailsVo productDetailsVo : productDetailsVoList) {
            FirstType firstType = firstTypeMapper.selectById(productDetailsVo.getFId());
            productDetailsVo.setFName(firstType.getName());

            SecondType secondType = secondTypeMapper.selectById(productDetailsVo.getSId());
            productDetailsVo.setSName(secondType.getName());

            User user = userMapper.selectById(productDetailsVo.getUserId());
            productDetailsVo.setUser(user);
        }
        return productDetailsVoList;
    }

    @Override
    public ProductDetailsVo productDetailsVo(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        FirstType firstType = firstTypeMapper.selectById(goods.getFirstTypeId());
        SecondType secondType = secondTypeMapper.selectById(goods.getSecondTypeId());
        User user = userMapper.selectById(goods.getUserId());

        ProductDetailsVo productDetailsVo = new ProductDetailsVo();
        productDetailsVo.setId(goods.getId());
        productDetailsVo.setNumber(goods.getNumber());
        productDetailsVo.setFName(firstType.getName());
        productDetailsVo.setSName(secondType.getName());
        productDetailsVo.setCount(goods.getCount());
        productDetailsVo.setStock(goods.getStock());
        productDetailsVo.setGoodsTitle(goods.getGoodsTitle());
        productDetailsVo.setPrice(goods.getPrice());
        productDetailsVo.setPhotoUrl(goods.getPhotoUrl());
        productDetailsVo.setPageView(goods.getPageView());
        productDetailsVo.setDescription(goods.getDescription());
        productDetailsVo.setStatus(goods.getStatus());
        productDetailsVo.setUser(user);
        return productDetailsVo;
    }

    @Override
    public List<Goods> getGoodsListByUserId(Integer userId) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Goods> goodsList = goodsMapper.selectList(wrapper);
        return goodsList;
    }

    @Override
    public Integer updateGoodsStatus(Integer status, Integer id) {
        return goodsMapper.updateGoodsStatus(status,id);
    }

    @Override
    public List<Goods> getGoodsByManyConditions(Goods goods) {
        return goodsMapper.getGoodsByManyConditions(goods);
    }

    @Override
    public Integer releaseGoodsCount(Integer userId) {
        return goodsMapper.releaseGoodsCount(userId);
    }
}
