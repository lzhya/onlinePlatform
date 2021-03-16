package com.zhenhong.service;

import java.util.Map;

/**
 * @Author lzhya
 * @Date 2020/12/30 16:55
 * @Version 1.0
 */
public interface SendSMS {
    boolean isSend(String phoneNumber, Map<String,Object> code);
}
