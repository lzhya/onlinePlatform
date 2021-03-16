package com.zhenhong.controller;
import com.zhenhong.service.SendSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**短信
 * @Author lzhya
 * @Date 2020/12/30 16:59
 * @Version 1.0
 */
@Controller
public class SendSMSController {
    @Autowired
    private SendSMS sendSMS;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RequestMapping("/sendSMS")
    @ResponseBody
    public String sendSMS(String phoneNumber){
        String code = redisTemplate.opsForValue().get(phoneNumber);
        if (code!=null){
            return code+"还没有过期";
        }
        Random r1 = new Random();
        code = "";
        for (int i = 0; i < 6; i++) {
            int i1 = r1.nextInt(10);
            code = code + i1;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code",code);
        boolean isSend = sendSMS.isSend(phoneNumber, map);
        if (isSend){
            redisTemplate.opsForValue().set(phoneNumber,code,60, TimeUnit.SECONDS);
            return "发送成功";
        }else {
            return "发送失败";
        }
    }

}
