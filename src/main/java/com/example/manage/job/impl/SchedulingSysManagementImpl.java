package com.example.manage.job.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.SysManagement;
import com.example.manage.job.SchedulingSysManagementService;
import com.example.manage.mapper.ICardTypeMapper;
import com.example.manage.mapper.IManageCardTypeMapper;
import com.example.manage.mapper.IPerformanceReportMapper;
import com.example.manage.mapper.ISysManagementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/5/22
 */

@Service
@Slf4j
public class SchedulingSysManagementImpl implements SchedulingSysManagementService {

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ICardTypeMapper iCardTypeMapper;

    @Resource
    private IPerformanceReportMapper iPerformanceReportMapper;

    @Resource
    private IManageCardTypeMapper iManageCardTypeMapper;

    @Override
    public void windUpAnAccount() {
        //查询正在运行的项目
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("management_state","1");
        List<SysManagement> managements = iSysManagementMapper.selectList(wrapper);
        for (int i = 0; i < managements.size(); i++) {
            System.out.println("managements=========>" + managements.get(i));
        }
    }
}
