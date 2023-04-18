package com.example.manage.mapper;

import com.example.manage.entity.ReimbursementProject;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/17
 */

public interface WhiteReimbursementProjectMapper {
    List<ReimbursementProject> queryAll(Map map);
    List<ReimbursementProject> querySumAmount(Map map);
}
