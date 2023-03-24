package com.example.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.SysManagement;
import com.example.manage.entity.is_not_null.SysManagementNotNull;
import com.example.manage.mapper.ISysManagementMapper;
import com.example.manage.service.ISysManagementService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */
@Slf4j
@Service
public class SysManagementServiceImpl implements ISysManagementService {
    @Resource
    private ISysManagementMapper iSysManagementMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysManagement jsonParam = PanXiaoZhang.getJSONParam(request, SysManagement.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysManagement jsonParam = PanXiaoZhang.getJSONParam(request, SysManagement.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    //编辑板块
    private ReturnEntity edit(HttpServletRequest request,SysManagement jsonParam) {
        //此处判断id不可为空
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysManagementNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //通过id查询数据是否存在
        SysManagement sysManagement = iSysManagementMapper.selectById(jsonParam.getId());
        if (ObjectUtils.isEmpty(sysManagement)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "该数据" + sysManagement.getName() + "不存在"
            );
        }
        //通过名称查询数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name",jsonParam.getName());
        SysManagement management = iSysManagementMapper.selectOne(wrapper);
        //将可用余额设置为空，进行不修改操作
        jsonParam.setManagementState(null);
        //如果这条数据不存在并且两条数据id相同则返回项目名称已存在
        if (!ObjectUtils.isEmpty(management) && !management.getId().equals(sysManagement.getId())){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "项目名称" + management.getName() + "已存在"
            );
        }
        //如果没有问题将进行数据修改
        int updateById = iSysManagementMapper.updateById(jsonParam);
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        //至此业务逻辑执行成功
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                jsonParam,
                request,
                MsgEntity.CODE_SUCCEED
        );
    }

    //添加板块
    private ReturnEntity add(HttpServletRequest request,SysManagement jsonParam) {
        //判断那些字段不可为空
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysManagementNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //判断是否有重复的项目名称
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name",jsonParam.getName());
        SysManagement sysManagement = iSysManagementMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(sysManagement)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "项目名称" + sysManagement.getName() + "已存在"
            );
        }
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iSysManagementMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        //到此业务逻辑结束
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
                iSysManagementMapper.queryAll(map),
                request,
                MsgEntity.CODE_SUCCEED,
                iSysManagementMapper.queryCount(map));
    }
}
