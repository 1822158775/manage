package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageRC;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 14:25:03
 * 报销类目关联特殊条件管理
 */

public interface IManageRCMapper extends BaseMapper<ManageRC> {
    List<ManageRC> queryAll(Map map);
    Integer queryCount(Map map);
}
