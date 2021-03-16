package com.zhenhong.service;

import com.zhenhong.pojo.Goods;
import com.zhenhong.vo.ProductDetailsVo;
import com.zhenhong.vo.ProductVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/6 21:38
 * @Version 1.0
 */
public interface GoodsService {
    //查询在线商品数量
    int getGoodsTotalNum();
    //查询已下架商品数量
    int getStopGoodsNum();
    //改变商品状态  待审核->上线
    int setCheckToOnline(Integer id);
    //查询全部商品数量
    int selectTotalNum();
    //查询全部待审核商品数量
    int getCheckNum();
    //根据一级分类查询商品
    List<Goods> getGoodsByLevelOne(Integer id);
    //查询商品最大id
    Integer getMaxId();
    //根据id 查询用户商品详情
    ProductVo product(Integer id);
    //商品浏览数+1
    Integer checkPageView(Integer id);
    //查询全部商品详情信息
    List<ProductDetailsVo> productDetailsVoList(Integer status);
    //根据id查询商品详情信息
    ProductDetailsVo productDetailsVo(Integer id);
    //根据用户id查询商品
    List<Goods> getGoodsListByUserId(Integer userId);
    //修改商品状态
    Integer updateGoodsStatus(Integer status,Integer id);
    //多条件查询
    List<Goods> getGoodsByManyConditions(Goods goods);
    //根据登录用户查询发布商品的数量
    @Select("SELECT COUNT(*) FROM goods WHERE user_id =#{userId}")
    Integer releaseGoodsCount(Integer userId);
}
