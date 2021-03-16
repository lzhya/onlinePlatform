package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhenhong.mapper.*;
import com.zhenhong.pojo.*;
import com.zhenhong.service.BrowseRecordsService;
import com.zhenhong.service.GoodsService;
import com.zhenhong.service.ProductCatgoryService;
import com.zhenhong.vo.ProductCategoryVo;
import com.zhenhong.vo.ProductDetailsVo;
import com.zhenhong.vo.ProductVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2020/12/6 21:40
 * @Version 1.0
 */
@Controller
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    FirstTypeMapper firstTypeMapper;

    @Autowired
    SecondTypeMapper secondTypeMapper;

    @Autowired
    private ProductCatgoryService productCatgoryService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BrowseRecordsService browseRecordsService;
    @Autowired
    private IntegralMapper integralMapper;


    /**
     * 查询全部在线商品
     * @param model model
     */
    @RequestMapping("/goods/onlineGoods")
    public String selectAll(Model model){
        //查询所有已上线商品信息
        List<ProductDetailsVo> onLineGoodsList = goodsService.productDetailsVoList(1);
        model.addAttribute("goodsList",onLineGoodsList);
        return "admin/goods_list";
    }

    /**
     * 查询全部下架商品信息
     * @param model model
     */
    @RequestMapping("/goods/getStopGoods")
    public String getStopGoods(Model model){
        List<ProductDetailsVo> stopGoodsList = goodsService.productDetailsVoList(3);
        model.addAttribute("goodsList",stopGoodsList);
        return "admin/goods_stop_list";
    }

    /**
     * 查询全部待审核商品
     * @param model model
     */
    @RequestMapping("/goods/check")
    public String getCheckGoods(Model model){
        List<ProductDetailsVo> checkGoodsList = goodsService.productDetailsVoList(0);
        model.addAttribute("goodsList",checkGoodsList);
        return "admin/product_check";
    }

    /**
     * 根据id查询商品详情
     * @param model model
     * @param id  商品id
     */
    @RequestMapping("/goods/detail")
    public String getGoodsById(Model model,Integer id){
        ProductDetailsVo productDetailsVo = goodsService.productDetailsVo(id);
        model.addAttribute("productDetailsVo", productDetailsVo);
        return "admin/goods-detail";
    }

    /**
     * 多条件查询
     * @param goods 商品
     * @param model model
     */
    @RequestMapping("/goods/manyCondition")
    public String manyCondition(Goods goods,Model model){
        List<Goods> list = goodsService.getGoodsByManyConditions(goods);
        List<ProductDetailsVo> productDetailsVoList = list.stream().map(e -> new ProductDetailsVo(e.getId(),
                e.getNumber(),
                e.getGoodsTitle(),
                e.getPhotoUrl(),
                e.getPrice(),
                e.getCount(),
                e.getStock(),
                e.getUploadDate(),
                e.getDescription(),
                e.getPageView(),
                e.getUserId())).collect(Collectors.toList());
        for (ProductDetailsVo productDetailsVo : productDetailsVoList) {
            Integer userId = productDetailsVo.getUserId();
            User user = userMapper.selectById(userId);
            productDetailsVo.setUser(user);
        }
        model.addAttribute("goodsList",productDetailsVoList);
        if (goods.getStatus()==0){
            return "admin/product_check";
        }else {
            return "admin/goods_list";
        }

    }

    /**
     * 根据二级分类查询商品信息
     * @param id 二级分类id
     * @param model model
     */
    @RequestMapping("/goods/getAllGoodsByFirstType")
    public String allGoodsByFirstType(Integer id, Model model){
        ProductCategoryVo productCategoryVo = productCatgoryService.productList(id);
        model.addAttribute("productCategoryVo",productCategoryVo);
        return "user/more";
    }

    /**
     * 根据二级分类查询商品信息
     * @param id 二级分类id
     * @param key 搜索关键字
     * @param model model
     */
    @RequestMapping("/goods/getAllGoodsBySecondType")
    public String allGoodsBySecondType(Integer id, String key,Model model){
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("stock",0);
        queryWrapper.eq("second_type_id",id);
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("key",key);
        return "user/find_goods";
    }

    @RequestMapping("/user/myReleaseView")
    public String myReleaseView(Model model,Integer id){
        ProductDetailsVo productDetailsVo = goodsService.productDetailsVo(id);
        model.addAttribute("productDetailsVo", productDetailsVo);
        return "user/my-release-view";
    }


    /**
     * 修改商品状态 待审核-》上线
     */
    @RequestMapping("/goods/accept")
    @ResponseBody
    public String accept(Integer[] ids) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(date);
        for (Integer id : ids) {
            // 修改商品状态
            Goods goods = goodsMapper.selectById(id);
            UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            updateWrapper.set("status",1);
            updateWrapper.set("stock",goods.getCount());
            updateWrapper.set("upload_date",dateTime);
            goodsMapper.update(null, updateWrapper);
            // 为发布者添加积分
            User user = userMapper.selectById(goods.getUserId());
            double ceil = Math.ceil(goods.getPrice() * 0.05 * goods.getCount());
            int addIntegral = new Double(ceil).intValue();
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("id",user.getId());
            userUpdateWrapper.set("integral",user.getIntegral()+addIntegral);
            userMapper.update(null,userUpdateWrapper);
            // 添加记录
            Integral integral = new Integral();
            integral.setUserId(user.getId());
            integral.setGoodsId(id);
            integral.setRemark("上传商品获得");
            integral.setFraction(addIntegral);
            integralMapper.insert(integral);
        }
        return "已上线";
    }

    /**
     * 修改商品状态 上架-》下架
     * @param ids 商品id
     */
    @RequestMapping("/goods/stop")
    @ResponseBody
    public String stopGoods(Integer[] ids){
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        //条件
        updateWrapper.in("id",ids);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(date);
        //修改商品时间
        updateWrapper.set("status",3);
        updateWrapper.set("upload_date",dateTime);
        int res = goodsMapper.update(null, updateWrapper);
        if (res==1){
            return "已下架!";
        }
        return "";
    }

    /**
     * 修改商品状态 下架-》上架
     * @param ids 商品id
     */
    @RequestMapping("/goods/online")
    @ResponseBody
    public String onlineGoods(Integer[] ids){
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        //条件
        updateWrapper.in("id",ids);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(date);
        //修改商品时间
        updateWrapper.set("status",1);
        updateWrapper.set("upload_date",dateTime);
        goodsMapper.update(null, updateWrapper);
        return "已上架";
    }

    @RequestMapping("/goods/analysis")
    public String analysis(Model model){
        return "admin/product_analysis";
    }



    /**
     * 添加商品
     * @param level1 一级分类
     * @param level2 二级分类
     * @param title  商品标题
     * @param price  商品单价
     * @param count 发布数目
     * @param photoUrl 图片储存地址
     */
    @RequestMapping("/goods/add")
    @ResponseBody
    public String addGoods(Integer level1,Integer level2,String title,double price,Integer count,String photoUrl,String remark){
        //商品id
        Integer id = goodsService.getMaxId();
        if (id==null) {
            id = 1;
        }
        id=id+1;
        //商品编号
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = dateFormat.format(date);
        String number=format+level1+level2;

        //发布时间
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uploadDate=dateFormat1.format(date);

        //卖家id
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        Goods goods = new Goods();
        goods.setId(id);
        goods.setFirstTypeId(level1);
        goods.setSecondTypeId(level2);
        goods.setGoodsTitle(title);
        goods.setPrice(price);
        goods.setNumber(number);
        goods.setCount(count);
        goods.setPhotoUrl(photoUrl);
        goods.setUploadDate(uploadDate);
        goods.setDescription(remark);
        goods.setUserId(userId);
        goods.setStatus(0);

        int res = goodsMapper.insert(goods);
        if (res==1){
            return "发布成功,请耐心等待管理员审核";
        }
        return "失败";
    }

    /**
     * 跳转到商品详情页
     * @param id 商品id
     * @param model model
     */
    @RequestMapping("/user/toGoodsDetail")
    public String toGoodsDetail( Integer id, Model model){
        //该商品浏览量+1
        goodsService.checkPageView(id);
        //添加商品浏览记录
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        QueryWrapper<BrowseRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id",id);
        BrowseRecords browseRecords = browseRecordsService.getOne(queryWrapper);
        if (browseRecords != null) {
            UpdateWrapper<BrowseRecords> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("goods_id",id);
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            updateWrapper.set("create_time",dateTime);
            browseRecordsService.update(updateWrapper);
        }else {
            browseRecords = new BrowseRecords();
            browseRecords.setUserId(user.getId());
            browseRecords.setGoodsId(id);
            browseRecordsService.save(browseRecords);
        }

        //获取当前商品信息
        ProductVo product = goodsService.product(id);
        /**
         * 推荐商品（商品的一级，二级分类相同）
         */
        QueryWrapper<Goods> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        Goods goods = goodsMapper.selectOne(wrapper);
        Integer firstTypeId = goods.getFirstTypeId();
        Integer secondTypeId = goods.getSecondTypeId();
        wrapper=new QueryWrapper<>();
        wrapper.eq("first_type_id",firstTypeId);
        wrapper.eq("second_type_id",secondTypeId);
        wrapper.ne("id",id);
        wrapper.eq("status",1);
        wrapper.gt("stock",0);
        wrapper.orderByAsc("upload_date");
        wrapper.last("limit 5");
        List<Goods> recommendGoods = goodsMapper.selectList(wrapper);

        model.addAttribute("product",product);
        model.addAttribute("userId",user.getId());
        model.addAttribute("recommendGoods",recommendGoods);
        return "user/goods_detail";
    }

    /**
     * 用户删除发布的商品信息
     * @param id 商品id
     * @return
     */
    @RequestMapping("/goods/delete")
    @ResponseBody
    public String deleteById(Integer id){
        int res = goodsMapper.deleteById(id);
        if (res==1){
            return "已删除";
        }
        return "删除失败";
    }

    /**
     * 用户修改商品时数据的回显
     * @param id 商品id
     */
    @RequestMapping("/goods/echo")
    public String echoById(Integer id,Model model){
        ProductDetailsVo productDetailsVo = goodsService.productDetailsVo(id);
        model.addAttribute("product",productDetailsVo);
        return "user/goods_echo";
    }

    @RequestMapping("/goods/update")
    @ResponseBody
    public String update(Integer id,Integer level1,Integer level2,String title,double price,Integer count,String photoUrl,String remark){
        //发布时间
        Date date = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uploadDate=dateFormat1.format(date);

        Goods goods = new Goods();
        goods.setId(id);
        goods.setFirstTypeId(level1);
        goods.setSecondTypeId(level2);
        goods.setGoodsTitle(title);
        goods.setPrice(price);
        goods.setCount(count);
        goods.setPageView(0);
        goods.setPhotoUrl(photoUrl);
        goods.setUploadDate(uploadDate);
        goods.setDescription(remark);
        goods.setStatus(0);
        int res = goodsMapper.updateById(goods);
        if (res==1){
            return "修改成功!";
        }
        return "修改失败!";
    }

    /***
     * 搜索商品
     * @param key 关键字
     */
    @RequestMapping("/user/search")
    public String search(String key, Model model){
        List<Goods> goodsList =new LinkedList<>();
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("name",key);
        FirstType firstType = firstTypeMapper.selectOne(wrapper);
        List<Goods> goodsListByFirst ;
        if (firstType !=null){
            queryWrapper.gt("stock",0);
            queryWrapper.eq("first_type_id",firstType.getId());
            goodsListByFirst = goodsMapper.selectList(queryWrapper);
            goodsList.addAll(goodsListByFirst);
        }else{
            goodsListByFirst = null;
        }

        List<Goods> goodsListBySecond = new ArrayList<>();
        List<SecondType> secondTypeList = secondTypeMapper.selectList(wrapper);
        if (secondTypeList!=null){
            for (SecondType secondType : secondTypeList) {
                queryWrapper = new QueryWrapper<>();
                queryWrapper.gt("stock",0);
                queryWrapper.eq("second_type_id",secondType.getId());
                List<Goods> goodsListSecond = goodsMapper.selectList(queryWrapper);
                goodsListBySecond.addAll(goodsListSecond);
            }
        }else {
            goodsListBySecond=null;
        }
        goodsList.addAll(goodsListBySecond);

        queryWrapper.like("goods_title",key);
        queryWrapper.gt("stock",0);
        List<Goods> goodsListByTitle = goodsMapper.selectList(queryWrapper);
        if (goodsListByTitle !=null){
            goodsList.addAll(goodsListByTitle);
        }else {
            goodsListByTitle = null;
        }
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("key",key);
        return "user/find_goods";
    }

    /**
     * 管理员根据一级分类和商品状态查询商品
     * @param fid 一级分类
     * @param status 商品状态
     */
    @RequestMapping("/admin/getGoodsByFirst")
    public String getGoodsByFirst(String fid,Integer status,Model model){
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.gt("stock",0);
        wrapper.eq("status",status);
        Integer id =null;
        if (fid.length()<=4){
            id = Integer.parseInt(fid.substring(1));
            wrapper.eq("first_type_id",id);
        }else {
            id = Integer.parseInt(fid.substring(4));
            wrapper.eq("second_type_id",id);
        }
        List<Goods> list = goodsMapper.selectList(wrapper);
        List<ProductDetailsVo> productDetailsVoList = list.stream().map(e -> new ProductDetailsVo(e.getId(),
                e.getNumber(),
                e.getGoodsTitle(),
                e.getPhotoUrl(),
                e.getPrice(),
                e.getCount(),
                e.getStock(),
                e.getUploadDate(),
                e.getDescription(),
                e.getPageView(),
                e.getUserId())).collect(Collectors.toList());
        for (ProductDetailsVo productDetailsVo : productDetailsVoList) {
            Integer userId = productDetailsVo.getUserId();
            User user = userMapper.selectById(userId);
            productDetailsVo.setUser(user);
        }
        model.addAttribute("goodsList",productDetailsVoList);
        if (status==0){
            return "admin/product_check";
        }else {
            return "admin/goods_list";
        }
    }
}
