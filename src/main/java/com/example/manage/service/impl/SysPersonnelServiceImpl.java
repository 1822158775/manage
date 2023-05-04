package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysManagement;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.SysRole;
import com.example.manage.entity.is_not_null.SysPersonnelNotNull;
import com.example.manage.mapper.IManagementPersonnelMapper;
import com.example.manage.mapper.ISysManagementMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.ISysRoleMapper;
import com.example.manage.service.ISysPersonnelService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */
@Slf4j
@Service
public class SysPersonnelServiceImpl implements ISysPersonnelService {

    @Value("${role.manage4}")
    private Integer roleId4;


    @Value("${role.manage3}")
    private Integer roleId3;


    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (IOException ioException) {
            log.info("捕获异常方法{},捕获异常{}",name,ioException.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, ioException.getMessage());
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
                ReturnEntity returnEntity = add(request, jsonParam);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("edit")){
                SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
                ReturnEntity returnEntity = edit(request, jsonParam);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (IOException ioException) {
            log.info("捕获异常方法{},捕获异常{}",name,ioException.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, ioException.getMessage());
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    //修改人员信息
    private ReturnEntity edit(HttpServletRequest request, SysPersonnel jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysPersonnelNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getId());
        if (!ObjectUtils.isEmpty(jsonParam.getManagementId())){
            SysRole sysRole = iSysRoleMapper.selectById(jsonParam.getRoleId());
            if (sysRole.getLevelSorting() > 2 && jsonParam.getManagementId().length > 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该角色不可以拥有多个项目");
            }
            //存储已有的内容
            Map<Integer,ManagementPersonnel> map = new HashMap<>();
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",jsonParam.getPersonnelCode());
            List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
            //已有的关联
            for (int i = 0; i < selectList.size(); i++) {
                ManagementPersonnel managementPersonnel = selectList.get(i);
                map.put(managementPersonnel.getManagementId(),managementPersonnel);
            }
            //传的关联
            for (int i = 0; i < jsonParam.getManagementId().length; i++) {
                Integer integer = jsonParam.getManagementId()[i];
                //按断是否有重复的经理和区域经理
                HashMap hashMap = new HashMap();
                Integer roleId = sysRole.getId();
                if (ObjectUtils.isEmpty(jsonParam.getRoleId())){
                    hashMap.put("roleId",sysRole.getId());
                }else {
                    hashMap.put("roleId",jsonParam.getRoleId());
                    roleId = jsonParam.getRoleId();
                }
                hashMap.put("employmentStatus",1);
                hashMap.put("managementId",integer);
                hashMap.put("neId",sysPersonnel.getId());
                //判断区域经理是否重复拥有项目
                if (roleId.equals(roleId3)){
                    List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(hashMap);
                    if (sysPersonnels.size() > 0){
                        return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目所属区域经理" + sysPersonnels.get(0).getName());
                    }
                }else if (roleId.equals(roleId4)){//判断经理是否重复拥有项目
                    List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(hashMap);
                    if (sysPersonnels.size() > 0){
                        return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目所属经理" + sysPersonnels.get(0).getName());
                    }
                }
                map.put(integer * -1,new ManagementPersonnel(
                        integer,
                        jsonParam.getPersonnelCode()
                ));
            }
            //查询所有项目
            List<SysManagement> sysManagements = iSysManagementMapper.selectList(null);
            for (int i = 0; i < sysManagements.size(); i++) {
                SysManagement management = sysManagements.get(i);
                Integer id = management.getId();
                ManagementPersonnel managementPersonnel = map.get(id);
                ManagementPersonnel personnel = map.get(id * -1);
                if (ObjectUtils.isEmpty(managementPersonnel) && !ObjectUtils.isEmpty(personnel)){

                    //按断是否有重复的经理和区域经理
                    HashMap hashMap = new HashMap();
                    Integer roleId = sysRole.getId();
                    if (ObjectUtils.isEmpty(jsonParam.getRoleId())){
                        hashMap.put("roleId",sysRole.getId());
                    }else {
                        hashMap.put("roleId",jsonParam.getRoleId());
                        roleId = jsonParam.getRoleId();
                    }
                    hashMap.put("employmentStatus",1);
                    hashMap.put("managementId",id);
                    hashMap.put("neId",sysPersonnel.getId());
                    //判断区域经理是否重复拥有项目
                    if (roleId.equals(roleId3)){
                        List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(hashMap);
                        if (sysPersonnels.size() > 0){
                            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目所属区域经理" + sysPersonnels.get(0).getName());
                        }
                    }else if (roleId.equals(roleId4)){//判断经理是否重复拥有项目
                        List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(hashMap);
                        if (sysPersonnels.size() > 0){
                            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目所属经理" + sysPersonnels.get(0).getName());
                        }
                    }
                    //不存在进行添加
                    iManagementPersonnelMapper.insert(personnel);
                }else if (!ObjectUtils.isEmpty(managementPersonnel) && ObjectUtils.isEmpty(personnel)){
                    //存在但是现传数据里没有需要删除
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("management_id",managementPersonnel.getManagementId());
                    queryWrapper.eq("personnel_code",managementPersonnel.getPersonnelCode());
                    iManagementPersonnelMapper.delete(queryWrapper);
                }
            }
        }
        //账号不可修改
        jsonParam.setUsername(null);
        //离职状态不可修改
        if (!sysPersonnel.getEmploymentStatus().equals(2)){
            jsonParam.setEmploymentStatus(null);
        }
        if (!ObjectUtils.isEmpty(jsonParam.getPassword())){
            //密码进行加密
            jsonParam.setPassword(PanXiaoZhang.getPassword(jsonParam.getPassword()));
        }
        int updateById = iSysPersonnelMapper.updateById(jsonParam);
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


    //添加人员信息
    private ReturnEntity add(HttpServletRequest request, SysPersonnel jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysPersonnelNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysRole sysRole = iSysRoleMapper.selectById(jsonParam.getRoleId());
        //判断等级如果大于2则不可拥有多个项目
        if (sysRole.getLevelSorting() > 2 && jsonParam.getManagementId().length > 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该角色不可以拥有多个项目");
        }
        if (jsonParam.getManagementId().length < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请选择所属项目");
        }
        Map map = new HashMap();
        map.put("roleId",sysRole.getId());
        map.put("employmentStatus",1);
        for (int i = 0; i < jsonParam.getManagementId().length; i++) {
            Integer integer = jsonParam.getManagementId()[i];
            map.put("managementId",integer);
            //判断区域经理是否重复拥有项目
            if (sysRole.getId().equals(roleId3)){
                List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
                if (sysPersonnels.size() > 0){
                    SysPersonnel sysPersonnel = sysPersonnels.get(0);
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目所属区域经理" + sysPersonnel.getName());
                }
            }else if (sysRole.getId().equals(roleId4)){//判断经理是否重复拥有项目
                List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
                if (sysPersonnels.size() > 0){
                    SysPersonnel sysPersonnel = sysPersonnels.get(0);
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目所属经理" + sysPersonnel.getName());
                }
            }
            iManagementPersonnelMapper.insert(new ManagementPersonnel(
                    integer,
                    jsonParam.getPersonnelCode()
            ));
        }
        jsonParam.setId(null);
        //密码进行加密
        jsonParam.setPassword(PanXiaoZhang.getPassword(jsonParam.getPassword()));
        int insert = iSysPersonnelMapper.insert(jsonParam);
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

    //查询人员信息
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                iSysPersonnelMapper.queryAll(map),
                request,
                MsgEntity.CODE_SUCCEED,
                iSysPersonnelMapper.queryCount(map));
    }
}
