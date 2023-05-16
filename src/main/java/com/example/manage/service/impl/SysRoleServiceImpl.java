package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.SysRole;
import com.example.manage.entity.is_not_null.SysRoleNotNull;
import com.example.manage.mapper.ISysRoleMapper;
import com.example.manage.service.ISysRoleService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysRole jsonParam = PanXiaoZhang.getJSONParam(request, SysRole.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysRole jsonParam = PanXiaoZhang.getJSONParam(request, SysRole.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    // 修改角色
    private ReturnEntity edit(HttpServletRequest request, SysRole jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysRoleNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //通过id查询数据是否存在
        SysRole sysRole = iSysRoleMapper.selectById(jsonParam.getId());
        if (ObjectUtils.isEmpty(sysRole)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "该数据" + sysRole.getName() + "不存在"
            );
        }
        //通过名称查询数据
        if (StringUtils.isNotBlank(jsonParam.getName())){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("name",jsonParam.getName());
            SysRole role = iSysRoleMapper.selectOne(wrapper);
            //如果这条数据不存在并且两条数据id相同则返回项目名称已存在
            if (!ObjectUtils.isEmpty(role) && !role.getId().equals(sysRole.getId())){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        "校色" + role.getName() + "已存在"
                );
            }
        }
        //判断是否更改等级了
        if (!ObjectUtils.isEmpty(jsonParam.getLevelSorting())){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("level_sorting",jsonParam.getLevelSorting());
            SysRole role = iSysRoleMapper.selectOne(wrapper);
            if (!role.getId().equals(sysRole.getId())){
                iSysRoleMapper.updateInsertLevelAugment(jsonParam.getLevelSorting());
            }
        }
        //如果没有问题将进行数据修改
        int updateById = iSysRoleMapper.updateById(jsonParam);
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        //至此业务逻辑执行成功
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }
    // 添加角色
    private ReturnEntity add(HttpServletRequest request, SysRole jsonParam) {
        //判断那些字段不可为空
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysRoleNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //判断是否有重复的角色
        if (!ObjectUtils.isEmpty(jsonParam.getName())){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("name",jsonParam.getName());
            SysRole sysRole = iSysRoleMapper.selectOne(wrapper);
            if (!ObjectUtils.isEmpty(sysRole)){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        "角色" + sysRole.getName() + "已存在"
                );
            }
        }
        //判断如果有重复的角色等级将所有大于等于该数字的加1
        if (!ObjectUtils.isEmpty(jsonParam.getLevelSorting())){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("level_sorting",jsonParam.getName());
            SysRole sysRole = iSysRoleMapper.selectOne(wrapper);
            if (!ObjectUtils.isEmpty(sysRole)){
                iSysRoleMapper.updateInsertLevelAugment(jsonParam.getLevelSorting());
            }
        }
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //如果没有问题将进行添加工作
        int insert = iSysRoleMapper.insert(jsonParam);
        //如果不为1则判断该数据添加失败
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iSysRoleMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iSysRoleMapper.queryCount(map));
    }
}
