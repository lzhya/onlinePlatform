package com.zhenhong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhenhong.mapper.FirstTypeMapper;
import com.zhenhong.mapper.SecondTypeMapper;
import com.zhenhong.pojo.FirstType;
import com.zhenhong.pojo.SecondType;
import com.zhenhong.service.FirstTypeService;
import com.zhenhong.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lzhya
 * @Date 2020/12/5 19:45
 * @Version 1.0
 */
@Service
public class FirstTypeServiceImpl implements FirstTypeService {
    @Autowired
    private FirstTypeMapper firstTypeMapper;
    @Autowired
    private SecondTypeMapper secondTypeMapper;

    @Override
    public int getMaxId() {
        return firstTypeMapper.getMaxId();
    }

    @Override
    public int saveOne(FirstType firstType) {
        return firstTypeMapper.saveOne(firstType);
    }

    @Override
    public List<CategoryVo> categoryVoList() {
        List<FirstType> firstTypeList = firstTypeMapper.selectList(null);
        List<CategoryVo> CategoryVoList = firstTypeList.stream().map(e -> new CategoryVo(e.getId(), e.getName())).collect(Collectors.toList());
        for (CategoryVo categoryVo : CategoryVoList) {
            QueryWrapper<SecondType> wrapper=new QueryWrapper<>();
            wrapper.eq("first_type_id",categoryVo.getId());
            List<SecondType> secondTypeList = secondTypeMapper.selectList(wrapper);
            categoryVo.setSecondTypeList(secondTypeList);
        }
        return CategoryVoList;
    }

}
