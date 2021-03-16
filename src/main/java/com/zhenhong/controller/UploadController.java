package com.zhenhong.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhenhong.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author lzhya
 * @Date 2020/12/20 14:04
 * @Version 1.0
 */
@Controller
public class UploadController {
    /**
     * 商品图片上传
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile mf) throws FileNotFoundException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //上传文件的位置,默认会在项目根目录找static文件夹,需手动创建,不然找到是临时路径。
        String path = "E:\\IntelliJ IDEA\\lzjtu-onlineplatform\\src\\main\\resources\\static\\images\\upload\\product";
        //判断该路径是否存在
        File file = new File(path);
        // 若不存在则创建该文件夹
        if (!file.exists()) {
            file.mkdirs();
        }
        //获取上传文件的后缀名
        String fileType = mf.getOriginalFilename().substring(mf.getOriginalFilename().indexOf("."));
        // 图片名称
        String uuid = "product_"+UUID.randomUUID().toString().replace("-", "").substring(0,10);
        String filename = uuid + fileType;
        // 完成文件上传
        try {
            mf.transferTo(new File(path, filename));
            Map<String,Object> map2=new HashMap<>();
            Map<String,Object> map=new HashMap<>();
            map.put("code",0);
            map.put("msg","");
            map2.put("src","/images/upload/product/"+ filename);
            map.put("data",map2);
            String jsonStr = objectMapper.writeValueAsString(map);
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",1);
        map.put("msg","");
        String jsonStr = objectMapper.writeValueAsString(map);
        return jsonStr;
    }

    /**
     * 用户头像上传
     */
    @RequestMapping("/upload/headPhoto")
    @ResponseBody
    public String headPhoto(MultipartFile headPhoto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //上传文件的位置,默认会在项目根目录找static文件夹,需手动创建,不然找到是临时路径。
        String path = "E:\\IntelliJ IDEA\\lzjtu-onlineplatform\\src\\main\\resources\\static\\images\\upload\\head";
        //判断该路径是否存在
        File file = new File(path);
        // 若不存在则创建该文件夹
        if (!file.exists()) {
            file.mkdirs();
        }
        //获取上传文件的后缀名
        String fileType = headPhoto.getOriginalFilename().substring(headPhoto.getOriginalFilename().indexOf("."));
        // 设置文件名称
            //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        User loginUser=  (User) session.getAttribute("user");
        String filename = loginUser.getNickname() + fileType;
        file = new File(path+"\\"+loginUser.getNickname()+".jpg");
        if (file.exists()){
            file.delete();
        }
        file = new File(path+"\\"+loginUser.getNickname()+".png");
        if (file.exists()){
            file.delete();
        }
        // 完成文件上传
        try {
            headPhoto.transferTo(new File(path, filename));
            Map<String,Object> map2=new HashMap<>();
            Map<String,Object> map=new HashMap<>();
            map.put("code",0);
            map.put("msg","");
            map2.put("src","/images/upload/head/"+ filename);
            map.put("data",map2);
            String jsonStr = objectMapper.writeValueAsString(map);
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",1);
        map.put("msg","");
        String jsonStr = objectMapper.writeValueAsString(map);
        return jsonStr;
    }

    /**
     * 广告图片上传
     */
    @RequestMapping("/upload/advPicture")
    @ResponseBody
    public String advPicture(MultipartFile advPicture) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //上传文件的位置,默认会在项目根目录找static文件夹,需手动创建,不然找到是临时路径。
        String path = "E:\\IntelliJ IDEA\\lzjtu-onlineplatform\\src\\main\\resources\\static\\images\\upload\\advertising";
        //判断该路径是否存在
        File file = new File(path);
        // 若不存在则创建该文件夹
        if (!file.exists()) {
            file.mkdirs();
        }
        //获取上传文件的后缀名
        String fileType = advPicture.getOriginalFilename().substring(advPicture.getOriginalFilename().indexOf("."));
        // 设置文件名称
        String filename = UUID.randomUUID().toString() + fileType;
        // 完成文件上传
        try {
            advPicture.transferTo(new File(path, filename));
            Map<String, Object> map2 = new HashMap<>();
            Map<String, Object> map = new HashMap<>();
            map.put("code", 0);
            map.put("msg", "");
            map2.put("src", "/images/upload/advertising/" + filename);
            map.put("data", map2);
            String jsonStr = objectMapper.writeValueAsString(map);
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("msg", "");
        String jsonStr = objectMapper.writeValueAsString(map);
        return jsonStr;
    }
}
