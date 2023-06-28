package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.GoOutToWork;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-26 15:52:39
 * 出差记录表
 */

public interface IGoOutToWorkMapper extends BaseMapper<GoOutToWork> {
    List<GoOutToWork> queryAll(Map map);
    Integer queryCount(Map map);
}
