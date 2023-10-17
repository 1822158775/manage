package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.ManageDimissionNotNull;
import com.example.manage.entity.is_not_null.SysPersonnelNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.entity.Token;
import com.example.manage.util.wechat.WechatMsg;
import com.example.manage.white_list.service.IWhiteManageDimissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @avthor 潘小章
 * @date 2023/4/3
 */

@Slf4j
@Service
public class WhiteManageDimissionServiceImpl implements IWhiteManageDimissionService {

    @Value("${url.transfer}")
    private String urlTransfer;

    @Value("${url.leave_job_list}")
    private String leaveJobList;

    @Value("${phone.personnel}")
    private String phone;

    @Value("${role.manage3}")
    private String manage3;

    @Value("${role.manage2}")
    private Integer manage2;

    @Value("${role.manage}")
    private String manage;

    @Value("${url.dimission}")
    private String urlDimission;

    @Value("${url.system}")
    private String urlSystem;

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private WhiteManageDimissionMapper whiteManageDimissionMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                return add(request);
            }else if (name.equals("cat_leave")){
                return cat_leave(request);
            }else if (name.equals("cat_list")){
                return cat_list(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    // 查询该项目组离职人员
    private ReturnEntity cat_list(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }
        //查询当前登录的人员
        SysPersonnel personnel = whiteSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));
        //查询当前人员你的角色
        SysRole sysRole = iSysRoleMapper.selectById(personnel.getRoleId());
        //删除map中的查询人的信息
        map.remove("personnelId");
        //设置小于当前人的人员
        map.put("gtLevelSorting",sysRole.getLevelSorting());
        QueryWrapper wrapper = new QueryWrapper();
        //查询当前人员关联的项目组
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(wrapper);
        List<Integer> integerList = new ArrayList<>();
        //进行遍历
        for (int i = 0; i < managementPersonnels.size(); i++) {
            ManagementPersonnel managementPersonnel = managementPersonnels.get(i);
            integerList.add(managementPersonnel.getManagementId());
        }
        //查询小于当前角色的人员
        wrapper = new QueryWrapper();
        wrapper.gt("level_sorting",sysRole.getLevelSorting());
        List<SysRole> sysRoles = iSysRoleMapper.selectList(wrapper);
        List<Integer> integerRole = new ArrayList<>();
        for (int i = 0; i < sysRoles.size(); i++) {
            integerRole.add(sysRoles.get(i).getId());
        }

        Integer[] toArray = integerList.toArray(new Integer[integerList.size()]);
        Integer[] roleArray = integerRole.toArray(new Integer[integerRole.size()]);
        map.put("inManagementId",toArray);
        map.put("inRoleId",roleArray);
        List<ManageDimission> manageDimissions = whiteManageDimissionMapper.queryAll(map);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,manageDimissions,"");
    }

    private ReturnEntity cat_leave(HttpServletRequest request) {
        return null;
    }

    //添加数据
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageDimissionNotNull(
                        "isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel selectById = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(selectById);
        if (estimateState.getState()){
            return estimateState;
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getId());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        ManageDimission manageDimission = iManageDimissionMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(manageDimission)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"不可重复提交");
        }
        //审核人
        List<SysPersonnel> sysPersonnels = new ArrayList<>();
        //查询当前个人的信息
        if (!returnEntity.getState()){
            //如果查不到人员信息
            if (ObjectUtils.isEmpty(sysPersonnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"人员信息不存在");
            }
            //申请人
            jsonParam.setApplicant(sysPersonnel.getName());
            //将人员资源代码加入进去
            jsonParam.setPersonnelCode(sysPersonnel.getPersonnelCode());
            //将当前所属项目加入
            if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
                QueryWrapper<ManagementPersonnel> personnelQueryWrapper = new QueryWrapper<>();
                personnelQueryWrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
                List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(personnelQueryWrapper);
                if (managementPersonnels.size() < 1){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目");
                }
                ManagementPersonnel managementPersonnel = managementPersonnels.get(0);
                jsonParam.setManagementId(managementPersonnel.getManagementId());
            }
            //查询该项目主管
            Map map = new HashMap();
            map.put("managementId",jsonParam.getManagementId());
            String[] strings = {manage3, manage};
            map.put("inRoleId",strings);
            map.put("employmentStatus","1");
            sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
            if (sysPersonnels.size() > 0){
                SysPersonnel personnel = sysPersonnels.get(0);
                //添加审核人编码
                jsonParam.setApproverPersonnelId(personnel.getId());
                //审核人数据
                jsonParam.setSysPersonnel(personnel);
            }
            //设置该条数据唯一编码
            jsonParam.setReportCoding("coding" + System.currentTimeMillis() + PanXiaoZhang.ran(2));
        }
        //添加当前报告时间
        jsonParam.setSubmissionTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        //添加默认状态
        jsonParam.setApproverState("pending");
        //没有任何问题将数据录入进数据库
        int insert = iManageDimissionMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        wrapper = new QueryWrapper();
        wrapper.eq("username",phone);
        SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
        //查询项目
        SysManagement sysManagement = iSysManagementMapper.selectById(jsonParam.getManagementId());
        ReturnEntity entity = new ReturnEntity();
        if (sysManagement.getId() != 1 && sysManagement.getId() != 2){
            // 发送人事
            entity = PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "",
                    "",
                    sysManagement.getName() + ":" + sysPersonnel.getName() + "提交了离职申请",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + leaveJobList
            );
        }
        // 如果有上一级
        for (int i = 0; i < sysPersonnels.size(); i++) {
            SysPersonnel sysP = sysPersonnels.get(i);
            // 发送上级领导
            PanXiaoZhang.postWechatFer(
                    sysP.getOpenId(),
                    "",
                    "",
                    sysPersonnel.getName() + "提交了离职申请",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + leaveJobList
            );
        }
        log.info("entity:{}",entity);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"上报成功");
    }

    private ReturnEntity cat(HttpServletRequest request) throws IOException {
        ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //查询当前用户是否存在
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前用户不存在");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        ManageDimission manageDimission = iManageDimissionMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(manageDimission)){
            jsonParam = manageDimission;
        }
        jsonParam.setSysPersonnel(sysPersonnel);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, jsonParam,"");
    }



    //方法总管外加事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("edit")){
                ReturnEntity returnEntity = edit(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    // 操作离职
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
        ManageDimissionNotNull isNotNullAndIsLengthNot0 = new ManageDimissionNotNull(
                "isNotNullAndIsLengthNot0"
        );
        isNotNullAndIsLengthNot0.setApplicantState("isNotNullAndIsLengthNot0");
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                isNotNullAndIsLengthNot0
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询当前填写信息的人
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        if (!sysPersonnel.getRoleId().equals(manage2)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"权限不足");
        }
        //查询当前数据
        ManageDimission manageDimission = iManageDimissionMapper.selectById(jsonParam.getId());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",manageDimission.getPersonnelCode());
        iSysPersonnelMapper.update(new SysPersonnel(
                0,
                manageDimission.getResignationTime()
        ),wrapper);
        iManageDimissionMapper.updateById(new ManageDimission(
                manageDimission.getId(),
                jsonParam.getApplicantState()
        ));
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"信息修改成功");
    }
}
