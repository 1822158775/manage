package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.CardType;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-30 14:11:53
 * 卡种管理
 */

public interface ICardTypeMapper extends BaseMapper<CardType> {
    List<CardType> queryAll(Map map);
    Integer queryCount(Map map);
}
