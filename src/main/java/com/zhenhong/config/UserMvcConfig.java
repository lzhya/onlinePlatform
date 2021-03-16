package com.zhenhong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author lzhya
 * @Date 2020/12/4 17:45
 * @Version 1.0
 */
@Configuration
public class UserMvcConfig implements WebMvcConfigurer {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/login.html").setViewName("user/login");  //映射用户登录地址
        registry.addViewController("/user/register.html").setViewName("user/register");  //映射用户注册地址
        registry.addViewController("/user/release_goods.html").setViewName("user/release_goods");  //映射用户注册地址
        registry.addViewController("/user/my_release.html").setViewName("user/my_release");//映射我的打法地址
        /**
         * 购物车
         */
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/upload/**")
                .addResourceLocations("file:E:\\IntelliJ IDEA\\lzjtu-onlineplatform\\src\\main\\resources\\static\\images\\upload\\");
    }
}
