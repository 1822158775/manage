package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.SysTableName;
import com.example.manage.entity.TableAuthorityName;
import com.example.manage.entity.is_not_null.SysTableAuthorityNotNull;
import com.example.manage.mapper.ISysTableNameMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.SysTableAuthority;
import com.example.manage.mapper.ISysTableAuthorityMapper;
import com.example.manage.service.ISysTableAuthorityService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-03-24 15:26:30
 * 角色权限
 */

@Slf4j
@Service
public class SysTableAuthorityServiceImpl implements ISysTableAuthorityService {
    @Resource
    private ISysTableAuthorityMapper iSysTableAuthorityMapper;
    @Resource
    private ISysTableNameMapper iSysTableNameMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysTableAuthority jsonParam = PanXiaoZhang.getJSONParam(request, SysTableAuthority.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysTableAuthority jsonParam = PanXiaoZhang.getJSONParam(request, SysTableAuthority.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    // 修改权限表单
    private ReturnEntity edit(HttpServletRequest request, SysTableAuthority jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysTableAuthorityNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询是否有相同的权限存在
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("table_name_id",jsonParam.getSysTableName());
        wrapper.eq("role_id",jsonParam.getRoleId());
        SysTableAuthority sysTableAuthority = iSysTableAuthorityMapper.selectOne(wrapper);
        if (!jsonParam.getId().equals(sysTableAuthority.getId())){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    "该角色的" + PanXiaoZhang.authority_name(sysTableAuthority.getAuthority()) + "权限已重复"
            );
        }
        int updateById = iSysTableAuthorityMapper.updateById(jsonParam);
        //如果不为1则判断该数据修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        if (returnEntity.getState()){
            return returnEntity;
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 添加权限设置
    private ReturnEntity add(HttpServletRequest request, SysTableAuthority jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysTableAuthorityNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询是否有相同的权限存在
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("role_id",jsonParam.getRoleId());
        List<SysTableAuthority> selectList = iSysTableAuthorityMapper.selectList(wrapper);
        //加入map
        Map<Integer, SysTableAuthority> map = new HashMap();
        for (SysTableAuthority sysTableAuthority :
                selectList) {
            map.put(sysTableAuthority.getTableNameId(),sysTableAuthority);
        }
        //if (!ObjectUtils.isEmpty(sysTableAuthority)){
        //    return new ReturnEntity(
        //            CodeEntity.CODE_ERROR,
        //            "该角色的" + PanXiaoZhang.authority_name(sysTableAuthority.getAuthority()) + "权限已存在"
        //    );
        //}
        //添加没有添加的
        if (jsonParam.getTableNameIds() != null){
            for (int i = 0; i < jsonParam.getTableNameIds().length; i++) {
                Integer integer = jsonParam.getTableNameIds()[i];
                SysTableAuthority sysTableAuthority = map.get(integer);
                map.put(integer * -1,sysTableAuthority);
                if (ObjectUtils.isEmpty(sysTableAuthority)){
                    int insert = iSysTableAuthorityMapper.insert(new SysTableAuthority(
                            integer,
                            1,
                            jsonParam.getRoleId()
                    ));
                    //如果不为1则判断该数据添加失败
                    if (insert != 1){
                        return new ReturnEntity(
                                CodeEntity.CODE_ERROR,
                                jsonParam,
                                "编码为" + integer + "的数据添加失败"
                        );
                    }
                }else {
                    sysTableAuthority.setAuthorityState(1);
                    iSysTableAuthorityMapper.updateById(sysTableAuthority);
                }
            }
        }
        if (jsonParam.getTableNameIds() != null){
            //获取所有表名称
            List<SysTableName> sysTableNames = iSysTableNameMapper.selectList(null);
            // 进行数据对比
            for (int i = 0; i < sysTableNames.size(); i++) {
                SysTableName sysTableName = sysTableNames.get(i);
                SysTableAuthority a_sysTableAuthority = map.get(sysTableName.getId());
                SysTableAuthority b_sysTableAuthority = map.get(sysTableName.getId() * -1);
                if (!ObjectUtils.isEmpty(a_sysTableAuthority) && ObjectUtils.isEmpty(b_sysTableAuthority)){
                    a_sysTableAuthority.setAuthorityState(2);
                    iSysTableAuthorityMapper.updateById(a_sysTableAuthority);
                }
            }
        }
        //至此代码业务结束
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }
    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        map.put("authorityState",1);
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                new TableAuthorityName(iSysTableAuthorityMapper.queryAll(map),iSysTableNameMapper.selectList(null)),
                request,
                MsgEntity.CODE_SUCCEED,iSysTableAuthorityMapper.queryCount(map)
        );
    }
}
