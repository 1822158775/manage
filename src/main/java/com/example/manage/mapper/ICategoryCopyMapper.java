package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.CategoryCopy;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-10 18:28:40
 * 类目关联抄送人管理
 */

public interface ICategoryCopyMapper extends BaseMapper<CategoryCopy> {
    List<CategoryCopy> queryAll(Map map);
    Integer queryCount(Map map);
}
