package com.zhenhong.controller;

import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.User;
import com.zhenhong.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author lzhya
 * @Date 2021/3/16 16:28
 * @Version 1.0
 */
@Controller
public class ExcelController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/user/export")
    @ResponseBody
    public void userExport(HttpServletResponse response) throws IOException {
        List<User> userList = userMapper.selectList(null);
        ExcelUtil.writeExcel(response,"用户表","用户名单模板.xlsx",userList);
    }
}
