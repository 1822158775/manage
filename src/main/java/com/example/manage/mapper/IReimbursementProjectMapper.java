package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ReimbursementProject;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 15:27:20
 * 报销记录关联项目
 */

public interface IReimbursementProjectMapper extends BaseMapper<ReimbursementProject> {
    List<ReimbursementProject> queryAll(Map map);
    Integer queryCount(Map map);
}
