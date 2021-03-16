package com.zhenhong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author lzhya
 * @Date 2021/1/15 19:34
 * @Version 1.0
 */
@Configuration
public class AdminMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin/category.html").setViewName("admin/Category_Manage");
    }
}
