package com.example.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.CardType;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/10
 */

public interface WhiteCardTypeMapper extends BaseMapper<CardType> {
    List<CardType> queryAll(Map map);
    Integer queryCount(Map map);
}
