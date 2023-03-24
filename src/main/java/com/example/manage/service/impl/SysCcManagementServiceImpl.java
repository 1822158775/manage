package com.example.manage.service.impl;

import com.example.manage.entity.SysCcManagement;
import com.example.manage.entity.is_not_null.SysCcManagementNotNull;
import com.example.manage.mapper.ISysCcManagementMapper;
import com.example.manage.service.ISysCcManagementService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/24
 */
@Slf4j
@Service
public class SysCcManagementServiceImpl implements ISysCcManagementService {
    @Resource
    private ISysCcManagementMapper iMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysCcManagement jsonParam = PanXiaoZhang.getJSONParam(request, SysCcManagement.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysCcManagement jsonParam = PanXiaoZhang.getJSONParam(request, SysCcManagement.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    // 修改角色
    private ReturnEntity edit(HttpServletRequest request, SysCcManagement jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysCcManagementNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        int updateById = iMapper.updateById(jsonParam);
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                jsonParam,
                request,
                MsgEntity.CODE_SUCCEED
        );
    }
    // 添加角色
    private ReturnEntity add(HttpServletRequest request, SysCcManagement jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysCcManagementNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        jsonParam.setId(null);
        int insert = iMapper.insert(jsonParam);
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                jsonParam,
                request,
                MsgEntity.CODE_SUCCEED
        );
    }

    // 查询模块
    public ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                iMapper.queryAll(map),
                request,
                MsgEntity.CODE_SUCCEED,
                iMapper.queryCount(map));
    }
}
