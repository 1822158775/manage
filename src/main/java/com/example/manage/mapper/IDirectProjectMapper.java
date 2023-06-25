package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.DirectProject;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-06-25 17:25:35
 * 部门直管项目表
 */

public interface IDirectProjectMapper extends BaseMapper<DirectProject> {
    List<DirectProject> queryAll(Map map);
    Integer queryCount(Map map);
}
