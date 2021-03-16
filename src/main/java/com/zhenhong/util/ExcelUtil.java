package com.zhenhong.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.zhenhong.pojo.User;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author lzhya
 * @Date 2021/3/16 13:47
 * @Version 1.0
 */

public class ExcelUtil {

    /**
     * excel写操作
     * @param fileName 文件名
     * @param templateFileName 模板名
     * @param list 数据
     */
    public static void writeExcel(HttpServletResponse response, String fileName,String templateFileName, List list) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //模板路径
        String path = "E:\\IntelliJ IDEA\\lzjtu-onlineplatform\\src\\main\\resources\\static\\excel";
        //模板名
        String templateFile = path + File.separator + templateFileName;
        // 防止中文乱码
        String name = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + name + ".xlsx");
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)10);
        headWriteCellStyle.setWriteFont(headWriteFont);

        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,contentWriteCellStyle);

        EasyExcel.write(response.getOutputStream(), User.class)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .withTemplate(templateFile)
                .sheet()
                .doWrite(list);
    }

}
