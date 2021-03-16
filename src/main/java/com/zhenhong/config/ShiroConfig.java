package com.zhenhong.config;

import com.zhenhong.realm.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.GenericFilter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author lzhya
 * @Date 2020/12/4 21:50
 * @Version 1.0
 */
@Configuration
public class ShiroConfig {
    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("webSecurityManager") DefaultWebSecurityManager webSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(webSecurityManager);

        Map<String,String> filterMap = new HashMap<>();
        filterMap.put("/cart/select","authc");
        filterMap.put("/cart.html","authc");
        filterMap.put("/user/toGoodsDetail","authc");
        bean.setFilterChainDefinitionMap(filterMap);
        //设置登录的请求
        bean.setLoginUrl("/user/toLogin");
        return bean;
    }
    //DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager webSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    //创建userRealm对象
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
