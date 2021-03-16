package com.zhenhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhenhong.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author lzhya
 * @Date 2020/12/6 21:11
 * @Version 1.0
 */
@Repository
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
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
    //商品浏览数+1
    Integer checkPageView(Integer id);
    //修改商品状态
    Integer updateGoodsStatus(Integer status,Integer id);
    //多条件查询
    List<Goods> getGoodsByManyConditions(Goods goods);
    //根据登录用户查询发布商品的数量
    @Select("SELECT COUNT(*) FROM goods WHERE user_id =#{userId}")
    Integer releaseGoodsCount(Integer userId);

    //根据时间查询商品数
    @Select("SELECT COUNT(*) FROM goods WHERE upload_date LIKE concat('2021-',#{date},'%') and status = #{status}")
    Integer productCount(String date,int status);

    // 根据一级分类查询商品数
    @Select("SELECT COUNT(*) FROM goods WHERE first_type_id =#{id} and status=1")
    Integer productCountByFirstTypeId(Integer id);


}
