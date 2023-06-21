package com.example.manage.service.impl;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.DivisionTypeManagement;
import com.example.manage.mapper.IDivisionTypeManagementMapper;
import com.example.manage.service.IDivisionTypeManagementService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-06-19 11:02:18
 * 部门类型关联部门管理表
 */

@Slf4j
@Service
public class DivisionTypeManagementServiceImpl implements IDivisionTypeManagementService {
    @Resource
    private IDivisionTypeManagementMapper iDivisionTypeManagementMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                DivisionTypeManagement jsonParam = PanXiaoZhang.getJSONParam(request, DivisionTypeManagement.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                DivisionTypeManagement jsonParam = PanXiaoZhang.getJSONParam(request, DivisionTypeManagement.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    // 修改部门类型关联部门管理表
    private ReturnEntity edit(HttpServletRequest request, DivisionTypeManagement jsonParam) {
        int updateById = iDivisionTypeManagementMapper.updateById(jsonParam);
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 添加部门类型关联部门管理表
    private ReturnEntity add(HttpServletRequest request, DivisionTypeManagement jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iDivisionTypeManagementMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iDivisionTypeManagementMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iDivisionTypeManagementMapper.queryCount(map));
    }
}
