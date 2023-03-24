package com.example.manage.service.impl;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.SysTableName;
import com.example.manage.mapper.ISysTableNameMapper;
import com.example.manage.service.ISysTableNameService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-03-24 15:05:37
 * 数据表名称
 */

@Slf4j
@Service
public class SysTableNameServiceImpl implements ISysTableNameService {
    @Resource
    private ISysTableNameMapper iSysTableNameMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysTableName jsonParam = PanXiaoZhang.getJSONParam(request, SysTableName.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysTableName jsonParam = PanXiaoZhang.getJSONParam(request, SysTableName.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    // 修改角色
    private ReturnEntity edit(HttpServletRequest request, SysTableName jsonParam) {
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 添加角色
    private ReturnEntity add(HttpServletRequest request, SysTableName jsonParam) {
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iSysTableNameMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iSysTableNameMapper.queryCount(map));
    }
}
