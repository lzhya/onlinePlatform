package com.zhenhong.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.zhenhong.mapper.UserMapper;
import com.zhenhong.pojo.User;
import com.zhenhong.util.ExcelUtil;
import org.apache.poi.xssf.usermodel.TextFontAlign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author lzhya
 * @Date 2021/2/3 19:58
 * @Version 1.0
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "user/evaluation";
    }
}
