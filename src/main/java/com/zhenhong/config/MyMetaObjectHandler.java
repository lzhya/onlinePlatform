package com.zhenhong.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author lzhya
 * @Date 2021/1/16 18:51
 * @Version 1.0
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时间
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //根据属性名称设置值
        this.strictInsertFill(metaObject, "createTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.strictInsertFill(metaObject, "updateTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.strictInsertFill(metaObject, "registerDate", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //根据属性名称设置值
        this.strictUpdateFill(metaObject, "updateTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.strictInsertFill(metaObject, "createTime", String.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
