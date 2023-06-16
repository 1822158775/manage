package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.CardTypeSales;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-16 15:07:18
 * 信用卡关联权益
 */

public interface ICardTypeSalesMapper extends BaseMapper<CardTypeSales> {
    List<CardTypeSales> queryAll(Map map);
    Integer queryCount(Map map);
}
