package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhenhong.mapper.*;
import com.zhenhong.pojo.*;
import com.zhenhong.service.BrowseRecordsService;
import com.zhenhong.service.GoodsService;
import com.zhenhong.service.OrderService;
import com.zhenhong.service.ProductCatgoryService;
import com.zhenhong.vo.OrderDetailsVo;
import com.zhenhong.vo.ProductCategoryVo;
import com.zhenhong.vo.UserActionVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2020/12/14 21:28
 * @Version 1.0
 */
@Controller
public class UserController {
    @Autowired
    private ProductCatgoryService productCatgoryService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private BrowseRecordsService browseRecordsService;
    @Autowired
    private OrderService  orderService;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private PictureMapper pictureMapper;
    /**
     * 跳转到后台首页
     */
    @RequestMapping(value = {"/user/toIndex","/user/index.html"})
    public String toIndex(Model model){
        List<ProductCategoryVo> categoryVoList = productCatgoryService.catgoryList();
        model.addAttribute("categoryVoList",categoryVoList);
        //最新上线商品
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        queryWrapper.gt("stock",0);
        queryWrapper.orderByDesc("upload_date");
        queryWrapper.last("limit 6");
        List<Goods> latestGoods = goodsMapper.selectList(queryWrapper);
        //首页轮播图
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper.eq("status",1);
        pictureQueryWrapper.eq("type_id",1);
        pictureQueryWrapper.orderByAsc("sort");
        List<Picture> pictureList = pictureMapper.selectList(pictureQueryWrapper);
        model.addAttribute("latestGoods",latestGoods);
        model.addAttribute("pictureList",pictureList);
        return "user/index";
    }
    /**
     * 跳转到用户注册页面
     */
    @RequestMapping("/user/toRegister")
    public String toRegister(){
        return "redirect:/user/register.html";
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param phone  电话
     * @param yzm  验证码
     */
    @RequestMapping("/user/register")
    public String register(String username,String gender,String password,String phone,String yzm,Model model){
        //验证用户名是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname",username);
        User user = userMapper.selectOne(wrapper);
        if (user!=null){
            model.addAttribute("msg","用户名已存在");
            return "user/register";
        }
        //验证手机号是否存在
        wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        user = userMapper.selectOne(wrapper);
        if (user!=null){
            model.addAttribute("msg1","该手机号码已注册！");
            return "user/register";
        }
        //验证验证码是否正确
        String code = redisTemplate.opsForValue().get(phone);
        if (!yzm.equals(code)){
            model.addAttribute("msg2","验证码错误！");
            return "user/register";
        }
        //保存注册信息
        User user1 = new User();
        user1.setNickname(username);
        if (gender=="男"){
            user1.setPhoto("/images/upload/head/moren_boy.jpg");
        }else{
            user1.setPhoto("/images/upload/head/moren_girl.jpg");
        }
        user1.setGender(gender);
        user1.setPhone(phone);
        user1.setPassword(password);
        user1.setStatus(1);
        userMapper.insert(user1);
        return "user/login";
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping("/user/toLogin")
    public String toLogin(){
        return "redirect:/user/login.html";
    }

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping("/user/login")
    public String login(String username,String password,RedirectAttributes attributes){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        try {
            subject.login(token);
            QueryWrapper<User> wrapper =new QueryWrapper<>();
            wrapper.eq("nickname",username);
            wrapper.eq("password",password);
            User user = userMapper.selectOne(wrapper);

            Session session = subject.getSession();
            session.setAttribute("user",user);
            return "redirect:/user/toIndex";
        } catch (UnknownAccountException e) {  //用户名不存在
            attributes.addFlashAttribute("msg","用户名不存在");
            return "redirect:/user/login.html";
        } catch (IncorrectCredentialsException e){ //密码不存在
            attributes.addFlashAttribute("msg","密码错误");
            return "redirect:/user/login.html";
        }
    }

    /**
     * 短信验证登陆
     * @param phone 手机号
     * @param yzm 验证码
     */
    @RequestMapping("/user/loginForPhone")
    @ResponseBody
    public String loginForPhone(String phone,String yzm){
        UsernamePasswordToken token = new UsernamePasswordToken(phone,yzm);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();
            Session session = subject.getSession();
            session.setAttribute("user",user);
            return "cg";
        } catch (UnknownAccountException e) {  //该手机还未注册
            return "该手机还未注册";
        } catch (IncorrectCredentialsException e){ //验证码错误！
            return "验证码错误！";
        }
    }

    /**
     * 用户注销
     */
    @RequestMapping("/user/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/user/toIndex";
    }

    /**
     * 跳转到商品发布页面
     */
    @RequestMapping("/goods/toRelease")
    public String toRelease(){
        return "redirect:/user/release_goods.html";
    }

    /**
     * 验证用户是否登录
     */
    @RequestMapping("/user/isLogin")
    @ResponseBody
    public String toIsLogin(){
        //验证是否登录
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        if (user!=null){
            return "已登录";
        }
        return "您还未登录！";
    }

    /**
     * 跳转到用户中心
     * @return  返回地址
     */
    @RequestMapping("/user/user_center.html")
    public String toUserCenter(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        User user = userMapper.selectById(loginUser.getId());
        //查询发布商品的数量
        Integer releaseGoodsCount = goodsService.releaseGoodsCount(user.getId());
        //查询买到的商品数量
        Integer boughtGoodsCount = orderService.boughtGoodsCount(user.getId());
        //查询卖出的商品数量
        Integer soldGoodsCount = orderService.soldGoodsCount(user.getId());
        //查询代发货的商品数量
        Integer toBeDeliveredCount = orderService.toBeDeliveredCount(user.getId());
        Integer toBeTransactionCount = orderService.toBeTransaction(user.getId());
        //根据浏览，推荐用户喜欢的商品
        QueryWrapper<BrowseRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<BrowseRecords> browseRecordsList = browseRecordsService.list(queryWrapper);
        List<Goods> goodsList = new ArrayList<>();
        for (BrowseRecords browseRecords : browseRecordsList) {
            Goods goods = goodsMapper.selectById(browseRecords.getGoodsId());
            QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
            goodsQueryWrapper.eq("second_type_id",goods.getSecondTypeId());
            goodsQueryWrapper.eq("status",1);
            List<Goods> list = goodsMapper.selectList(goodsQueryWrapper);
            goodsList.addAll(list);
        }
        model.addAttribute("releaseGoodsCount",releaseGoodsCount);
        model.addAttribute("boughtGoodsCount",boughtGoodsCount);
        model.addAttribute("soldGoodsCount",soldGoodsCount);
        model.addAttribute("toBeDeliveredCount",toBeDeliveredCount);
        model.addAttribute("toBeTransactionCount",toBeTransactionCount);
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsList);
        return "user/user_center";
    }

    /**
     *查询用户足迹
     */
    @RequestMapping("/user/footmarket.html")
    public String footMarket(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //获取用户浏览记录
        QueryWrapper<BrowseRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<BrowseRecords> browseRecordsList = browseRecordsService.list(queryWrapper);
        List<Goods> goodsList = new ArrayList<>();
        for (BrowseRecords browseRecords : browseRecordsList) {
            Goods goods = goodsMapper.selectById(browseRecords.getGoodsId());
            goodsList.add(goods);
        }
        model.addAttribute("goodsList",goodsList);
        return "user/footmarket";
    }


    /**
     * 用户中心-》我发布的
     * @param model model
     * @return 返回地址
     */
    @RequestMapping("/user/my_release.html")
    public String myRelease(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //根据用户id查询该用户所发布的商品信息
        int userId = user.getId();
        List<Goods> goodsListByUserId = goodsService.getGoodsListByUserId(userId);
        model.addAttribute("goodsList",goodsListByUserId);
        return "user/my_release";
    }

    /**
     * 用户中心-》待收货
     * @param model model
     * @return 地址
     */
    @RequestMapping("/user/toBeReceived.html")
    public String toBeReceived(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //根据用户id查询该用户所发布的商品信息
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsVoList(user.getId(), 0);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/toBeReceived";
    }

    /**
     * 用户中心-》我买到的
     * @param model model
     * @return 返回地址
     */
    @RequestMapping("/user/to_bought.html")
    public String toBought(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //根据用户id查询该用户所发布的商品信息
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsVoList(user.getId(), 1);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/toBeReceived";
    }

    /**
     * 用户中心-》我卖出的
     * @param model model
     * @return 返回地址
     */
    @RequestMapping("/user/to_sold.html")
    public String toSold(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //根据用户id查询该用户所发布的商品信息
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsBySeller(user.getId(), 1);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/to_sold";
    }

    @RequestMapping("/user/toBeTransaction.html")
    public String transaction(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //根据用户id查询该用户所发布的商品信息
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsBySeller(user.getId(), 0);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/to_transaction";
    }
    /**
     * 用户消费行为
     */
    @RequestMapping("/admin/userAction")
    public String userAction(Model model){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<User> userList = userMapper.selectList(queryWrapper);
        List<UserActionVo> userActionVoList = userList.stream().map(e -> new UserActionVo(e.getId(), e.getNickname(), e.getIntegral())).collect(Collectors.toList());
        for (UserActionVo userActionVo : userActionVoList) {
            //用户发布商品数量
            Integer goodsCount = userMapper.getGoodsCountByUserId(userActionVo.getId());
            userActionVo.setReleaseNum(goodsCount==null?0:goodsCount);
            //用户浏览商品次数
            Integer browseCount = browseRecordsService.getBrowseCountByUserId(userActionVo.getId());
            userActionVo.setViews(browseCount==null?0:browseCount);
            // 用户购物车数
            Integer cartCount = cartMapper.cartCountByUserId(userActionVo.getId());
            userActionVo.setCartCount(cartCount);
            // 用户购物数
            Integer boughtGoodsCount = orderService.boughtGoodsCount(userActionVo.getId());
            userActionVo.setShoppingNum(boughtGoodsCount);
        }
        model.addAttribute("userActionVoList",userActionVoList);
        return "admin/integration";
    }

    @RequestMapping("/user/information")
    public String info(Model model){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        User user = userMapper.selectById(loginUser.getId());
        model.addAttribute("user",user);
        return "user/user_info";
    }

    /**
     * 修改用户信息
     * @param nickname 用户名
     * @param gender 相别
     * @param email 邮箱
     * @param address 地址
     * @return 返回信息
     */
    @RequestMapping("/user/modify")
    @ResponseBody
    public String modify(String nickname,String gender,String email,String address){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",loginUser.getId());
        User user = new User();
        user.setNickname(nickname);
        user.setGender(gender);
        user.setEmail(email);
        user.setAddress(address);
        int res = userMapper.update(user, updateWrapper);
        if (res==1){
            return "修改成功";
        }else {
            return "修改失败";
        }
    }

    /**
     * 修改用户头像
     * @param url 地址
     * @return 返回信息
     */
    @RequestMapping("/user/modifyHeadPhoto")
    @ResponseBody
    public String modifyHeadPhoto(String url){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",loginUser.getId());
        User user = new User();
        user.setPhoto(url);
        int res = userMapper.update(user, updateWrapper);
        if (res==1){
            return "修改成功";
        }else {
            return "修改失败";
        }
    }

    @RequestMapping("/user/modifyPassword.html")
    public String toModify(){
        return "user/modify_password";
    }

    @RequestMapping("/user/modifyPassword")
    @ResponseBody
    public String modifyPassword(String oldPwd,String newPwd){
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        if (!loginUser.getPassword().equals(oldPwd)){
            return "密码错误！";
        }else {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",loginUser.getId());
            User user = new User();
            user.setPassword(newPwd);
            int res = userMapper.update(user, updateWrapper);
            if (res==1){
                subject.logout();
                return "ok";
            }else {
                return "修改失败";
            }
        }
    }

}
