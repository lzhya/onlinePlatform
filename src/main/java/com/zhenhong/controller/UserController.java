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
    @Autowired
    private EvaluationMapper evaluationMapper;
    /**
     * ?????????????????????
     */
    @RequestMapping(value = {"/user/toIndex","/user/index.html"})
    public String toIndex(Model model){
        List<ProductCategoryVo> categoryVoList = productCatgoryService.catgoryList();
        model.addAttribute("categoryVoList",categoryVoList);
        //??????????????????
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        queryWrapper.gt("stock",0);
        queryWrapper.orderByDesc("upload_date");
        queryWrapper.last("limit 6");
        List<Goods> latestGoods = goodsMapper.selectList(queryWrapper);
        //???????????????
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
     * ???????????????????????????
     */
    @RequestMapping("/user/toRegister")
    public String toRegister(){
        return "redirect:/user/register.html";
    }

    /**
     * ????????????
     * @param username ?????????
     * @param password ??????
     * @param phone  ??????
     * @param yzm  ?????????
     */
    @RequestMapping("/user/register")
    public String register(String username,String gender,String password,String phone,String yzm,Model model){
        //???????????????????????????
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname",username);
        User user = userMapper.selectOne(wrapper);
        if (user!=null){
            model.addAttribute("msg","??????????????????");
            return "user/register";
        }
        //???????????????????????????
        wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        user = userMapper.selectOne(wrapper);
        if (user!=null){
            model.addAttribute("msg1","???????????????????????????");
            return "user/register";
        }
        //???????????????????????????
        String code = redisTemplate.opsForValue().get(phone);
        if (!yzm.equals(code)){
            model.addAttribute("msg2","??????????????????");
            return "user/register";
        }
        //??????????????????
        User user1 = new User();
        user1.setNickname(username);
        if (gender=="???"){
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
     * ?????????????????????
     */
    @RequestMapping("/user/toLogin")
    public String toLogin(){
        return "redirect:/user/login.html";
    }

    /**
     * ??????????????????
     * @param username ?????????
     * @param password ??????
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
        } catch (UnknownAccountException e) {  //??????????????????
            attributes.addFlashAttribute("msg","??????????????????");
            return "redirect:/user/login.html";
        } catch (IncorrectCredentialsException e){ //???????????????
            attributes.addFlashAttribute("msg","????????????");
            return "redirect:/user/login.html";
        }
    }

    /**
     * ??????????????????
     * @param phone ?????????
     * @param yzm ?????????
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
        } catch (UnknownAccountException e) {  //?????????????????????
            return "?????????????????????";
        } catch (IncorrectCredentialsException e){ //??????????????????
            return "??????????????????";
        }
    }

    /**
     * ????????????
     */
    @RequestMapping("/user/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/user/toIndex";
    }

    /**
     * ???????????????????????????
     */
    @RequestMapping("/goods/toRelease")
    public String toRelease(){
        return "redirect:/user/release_goods.html";
    }

    /**
     * ????????????????????????
     */
    @RequestMapping("/user/isLogin")
    @ResponseBody
    public String toIsLogin(){
        //??????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        if (user!=null){
            return "?????????";
        }
        return "??????????????????";
    }

    /**
     * ?????????????????????
     * @return  ????????????
     */
    @RequestMapping("/user/user_center.html")
    public String toUserCenter(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        User user = userMapper.selectById(loginUser.getId());
        //???????????????????????????
        Integer releaseGoodsCount = goodsService.releaseGoodsCount(user.getId());
        //???????????????????????????
        Integer boughtGoodsCount = orderService.boughtGoodsCount(user.getId());
        //???????????????????????????
        Integer soldGoodsCount = orderService.soldGoodsCount(user.getId());
        //??????????????????????????????
        Integer toBeDeliveredCount = orderService.toBeDeliveredCount(user.getId());
        Integer toBeTransactionCount = orderService.toBeTransaction(user.getId());
        //??????????????????????????????????????????
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
     *??????????????????
     */
    @RequestMapping("/user/footmarket.html")
    public String footMarket(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //????????????????????????
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
     * ????????????-???????????????
     * @param model model
     * @return ????????????
     */
    @RequestMapping("/user/my_release.html")
    public String myRelease(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //????????????id???????????????????????????????????????
        int userId = user.getId();
        List<Goods> goodsListByUserId = goodsService.getGoodsListByUserId(userId);
        model.addAttribute("goodsList",goodsListByUserId);
        return "user/my_release";
    }

    /**
     * ????????????-????????????
     * @param model model
     * @return ??????
     */
    @RequestMapping("/user/toBeReceived.html")
    public String toBeReceived(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //????????????id???????????????????????????????????????
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsVoList(user.getId(), 0);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/toBeReceived";
    }

    /**
     * ????????????-???????????????
     * @param model model
     * @return ????????????
     */
    @RequestMapping("/user/to_bought.html")
    public String toBought(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //????????????id???????????????????????????????????????
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsVoList(user.getId(), 1);
        for (OrderDetailsVo orderDetailsVo : orderDetailsVoList) {
            QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id",orderDetailsVo.getId());
            Evaluate evaluate = evaluationMapper.selectOne(queryWrapper);
            if (evaluate==null){
                orderDetailsVo.setIsEvaluate(false);
            }else{
                orderDetailsVo.setIsEvaluate(true);
            }
        }
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/toBeReceived";
    }

    /**
     * ????????????-???????????????
     * @param model model
     * @return ????????????
     */
    @RequestMapping("/user/to_sold.html")
    public String toSold(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //????????????id???????????????????????????????????????
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsBySeller(user.getId(), 1);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/to_sold";
    }

    @RequestMapping("/user/toBeTransaction.html")
    public String transaction(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User user=  (User) session.getAttribute("user");
        //????????????id???????????????????????????????????????
        List<OrderDetailsVo> orderDetailsVoList = orderService.orderDetailsBySeller(user.getId(), 0);
        model.addAttribute("orderDetailsVoList",orderDetailsVoList);
        return "user/to_transaction";
    }
    /**
     * ??????????????????
     */
    @RequestMapping("/admin/userAction")
    public String userAction(Model model){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        List<User> userList = userMapper.selectList(queryWrapper);
        List<UserActionVo> userActionVoList = userList.stream().map(e -> new UserActionVo(e.getId(), e.getNickname(), e.getIntegral())).collect(Collectors.toList());
        for (UserActionVo userActionVo : userActionVoList) {
            //????????????????????????
            Integer goodsCount = userMapper.getGoodsCountByUserId(userActionVo.getId());
            userActionVo.setReleaseNum(goodsCount==null?0:goodsCount);
            //????????????????????????
            Integer browseCount = browseRecordsService.getBrowseCountByUserId(userActionVo.getId());
            userActionVo.setViews(browseCount==null?0:browseCount);
            // ??????????????????
            Integer cartCount = cartMapper.cartCountByUserId(userActionVo.getId());
            userActionVo.setCartCount(cartCount);
            // ???????????????
            Integer boughtGoodsCount = orderService.boughtGoodsCount(userActionVo.getId());
            userActionVo.setShoppingNum(boughtGoodsCount);
        }
        model.addAttribute("userActionVoList",userActionVoList);
        return "admin/integration";
    }

    @RequestMapping("/user/information")
    public String info(Model model){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        User user = userMapper.selectById(loginUser.getId());
        model.addAttribute("user",user);
        return "user/user_info";
    }

    /**
     * ??????????????????
     * @param nickname ?????????
     * @param gender ??????
     * @param email ??????
     * @param address ??????
     * @return ????????????
     */
    @RequestMapping("/user/modify")
    @ResponseBody
    public String modify(String nickname,String gender,String email,String address){
        //??????????????????????????????
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
            return "????????????";
        }else {
            return "????????????";
        }
    }

    /**
     * ??????????????????
     * @param url ??????
     * @return ????????????
     */
    @RequestMapping("/user/modifyHeadPhoto")
    @ResponseBody
    public String modifyHeadPhoto(String url){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",loginUser.getId());
        User user = new User();
        user.setPhoto(url);
        int res = userMapper.update(user, updateWrapper);
        if (res==1){
            return "????????????";
        }else {
            return "????????????";
        }
    }

    @RequestMapping("/user/modifyPassword.html")
    public String toModify(){
        return "user/modify_password";
    }

    @RequestMapping("/user/modifyPassword")
    @ResponseBody
    public String modifyPassword(String oldPwd,String newPwd){
        //??????????????????????????????
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        if (!loginUser.getPassword().equals(oldPwd)){
            return "???????????????";
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
                return "????????????";
            }
        }
    }

}
