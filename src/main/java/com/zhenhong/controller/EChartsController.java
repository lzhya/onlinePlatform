package com.zhenhong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhenhong.mapper.CartMapper;
import com.zhenhong.mapper.FirstTypeMapper;
import com.zhenhong.mapper.GoodsMapper;
import com.zhenhong.mapper.OrderMapper;
import com.zhenhong.pojo.FirstType;
import com.zhenhong.pojo.Goods;
import com.zhenhong.service.OrderService;
import com.zhenhong.vo.ProductAnalysisVo;
import com.zhenhong.vo.ProductProportionVo;
import com.zhenhong.vo.TransactionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author lzhya
 * @Date 2021/2/24 21:35
 * @Version 1.0
 */
@Controller
public class EChartsController {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private FirstTypeMapper firstTypeMapper;
    /**
     * 查询 交易订单
     */
    @RequestMapping("/echarts/transaction")
    @ResponseBody
    public String transaction() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TransactionVo> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        int monthValue = now.getMonthValue();

        for (int i = 1; i <= monthValue; i++) {
            TransactionVo transactionVo = new TransactionVo();
            Integer cartCount;
            Integer transactionCount;
            Integer forwardingCount;
            if (i<=9){
                cartCount = cartMapper.cartCountByMonth("0" + i); //待付款
                transactionCount = orderMapper.toBeTransactionCount("0" + i);
                forwardingCount = orderMapper.forwardingCount("0" + i);
            }else{
                cartCount = cartMapper.cartCountByMonth(String.valueOf(i));
                transactionCount = orderMapper.toBeTransactionCount(String.valueOf(i));
                forwardingCount = orderMapper.forwardingCount(String.valueOf(i));
            }
            transactionVo.setMonth(i);
            transactionVo.setAllOrderCount(cartCount+transactionCount+forwardingCount);
            transactionVo.setCartCount(cartCount);
            transactionVo.setForwardingCount(forwardingCount);
            transactionVo.setToBeTransactionCount(transactionCount);
            list.add(transactionVo);
        }
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",list);
        String jsonData = objectMapper.writeValueAsString(map);
        return jsonData;
    }
    /**
     * 天-订单量-交易额
     */
    @RequestMapping("/echarts/amount")
    @ResponseBody
    public String amount() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TransactionVo> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int day;
        int monthValue = now.getMonthValue();
        String month = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"));

        if (monthValue==2){
            day = 28;
        }else if(monthValue==1||monthValue==3||monthValue==5||monthValue==7||monthValue==8||monthValue==10||monthValue==12){
            day = 31;
        }else{
            day = 30;
        }

        for (int i = 1; i <= day; i++) {
            Double dayAmount;
            Integer transactionCount;
            if (i <= 9) {
                dayAmount = orderService.transactionDayAmount(year + "-" + month + "-0" + i);
                transactionCount = orderMapper.toBeTransactionCount(month + "-0" + i);
            } else {
                dayAmount = orderService.transactionDayAmount(year + "-" + month + "-" + i);
                transactionCount = orderMapper.toBeTransactionCount(month + "-" + i);
            }
            TransactionVo transactionVo = new TransactionVo();
            transactionVo.setDay(i);
            transactionVo.setToBeTransactionCount(transactionCount);
            transactionVo.setTransactionAmount(dayAmount);
            list.add(transactionVo);
        }

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",list);
        String data = objectMapper.writeValueAsString(map);
        return data;
    }

    @RequestMapping("/eCharts/productAnalysisByDay")
    @ResponseBody
    public String productAnalysisByDay() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        String month = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"));
        List<ProductAnalysisVo> list = new ArrayList<>();
        int totalCount = 0;
        for (int i = 1; i <= day; i++) {
            ProductAnalysisVo productAnalysisVo = new ProductAnalysisVo();
            int passCount ;
            int defeatCount ;
            if (i <= 9) {
                passCount = goodsMapper.productCount(month + "-0" + i, 1);
                defeatCount = goodsMapper.productCount(month + "-0" + i, 0);
            } else {
                passCount = goodsMapper.productCount(month + "-" + i, 1);
                defeatCount = goodsMapper.productCount(month + "-" + i, 0);
            }
            totalCount = totalCount + passCount;
            productAnalysisVo.setDay(i);
            productAnalysisVo.setPassCount(passCount);
            productAnalysisVo.setDefeatCount(defeatCount);
            productAnalysisVo.setTotalCount(totalCount);
            list.add(productAnalysisVo);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",list);
        String data = objectMapper.writeValueAsString(map);
        return data;
    }

    @RequestMapping("/eCharts/productAnalysisByMonth")
    @ResponseBody
    public String productAnalysisByMonth() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        List<ProductAnalysisVo> list = new ArrayList<>();
        int totalCount = 0;
        for (int i = 1; i <= month; i++) {
            ProductAnalysisVo productAnalysisVo = new ProductAnalysisVo();
            int passCount ;
            int defeatCount ;
            if (i <= 9) {
                passCount = goodsMapper.productCount("0"+i, 1);
                defeatCount = goodsMapper.productCount("0"+i, 0);
            } else {
                passCount = goodsMapper.productCount(""+i, 1);
                defeatCount = goodsMapper.productCount(""+i, 0);
            }
            totalCount = totalCount + passCount;
            productAnalysisVo.setMonth(i);
            productAnalysisVo.setPassCount(passCount);
            productAnalysisVo.setDefeatCount(defeatCount);
            productAnalysisVo.setTotalCount(totalCount);
            list.add(productAnalysisVo);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",list);
        String data = objectMapper.writeValueAsString(map);
        return data;
    }

    /**
     * 商品类型比例
     */
    @RequestMapping("/eCharts/productProportion")
    @ResponseBody
    public String productProportion() throws JsonProcessingException {
        List<ProductProportionVo> list = new ArrayList<>();
        List<FirstType> firstTypeList = firstTypeMapper.selectList(null);
        for (FirstType firstType : firstTypeList) {
            Integer count = goodsMapper.productCountByFirstTypeId(firstType.getId());
            ProductProportionVo proportionVo = new ProductProportionVo();
            proportionVo.setName(firstType.getName());
            proportionVo.setValue(count);
            list.add(proportionVo);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",list);
        String data = objectMapper.writeValueAsString(map);
        return data;
    }
}
