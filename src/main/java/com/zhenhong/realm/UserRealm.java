package com.zhenhong.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author lzhya
 * @Date 2020/12/17 20:52
 * @Version 1.0
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
       UsernamePasswordToken userToken= (UsernamePasswordToken) token;
        String username = userToken.getUsername();
        if (username.length()==11&&username.startsWith("1")){ //手机号登录
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("status",1);
            wrapper.eq("phone",username);
            User user = userMapper.selectOne(wrapper);
            if (user==null){
                return null;
            }
            String code = redisTemplate.opsForValue().get(username);
            if (code == null){
                return new SimpleAuthenticationInfo("","null","");
            }
            return new SimpleAuthenticationInfo(user,code,"");
        }else {  //密码登录
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("status",1);
            wrapper.eq("nickname",userToken.getUsername());
            User user = userMapper.selectOne(wrapper);
            if (user==null){
                return null;
            }
            return new SimpleAuthenticationInfo("",user.getPassword(),"");
        }
    }
}
