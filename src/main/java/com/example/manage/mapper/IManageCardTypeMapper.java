package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageCardType;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-03-30 16:23:53
 * 项目关联卡种
 */

public interface IManageCardTypeMapper extends BaseMapper<ManageCardType> {
    List<ManageCardType> queryAll(Map map);
    Integer queryCount(Map map);
}
