package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.DispatchApplicationManagementNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.PhoneConfig;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteDispatchApplicationManagementService;
import com.example.manage.white_list.service.IWhiteSysPersonnelService;
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
 * @date 2023/4/7
 * 调派
 */

@Slf4j
@Service
public class WhiteDispatchApplicationManagementServiceImpl implements IWhiteDispatchApplicationManagementService {

    @Value("${phone.personnel}")
    private String phone;

    @Value("${phone.personnel2}")
    private String phone2;

    @Value("${phone.personnel3}")
    private String phone3;

    @Value("${role.manage}")
    private Integer roleId;

    @Value("${role.manage2}")
    private Integer manage2;

    @Value("${url.dispatch}")
    private String urlDispatch;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Resource
    private IDispatchApplicationManagementMapper iDispatchApplicationManagementMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private IWhiteSysPersonnelService iWhiteSysPersonnelService;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private IDispatchApplicationReimbursementMapper iDispatchApplicationReimbursementMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("cat_list")){
                return cat_list(request);
            }else if (name.equals("add")){
                return add(request);
            }else if (name.equals("cat_past_records")){
                return cat_past_records(request);
            }else if (name.equals("cat_collate_past_records")){
                return cat_collate_past_records(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //查询所有调派信息
    private ReturnEntity cat_list(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);

        //查询该人员信息
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(String.valueOf(jsonMap.get("personnelId")));
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //匹配角色职位
        if (!sysPersonnel.getRoleId().equals(manage2)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"暂无权限查看");
        }
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();

        // 获取本月的年份
        int year = calendar.get(Calendar.YEAR);
        // 获取本月的月份（注意：月份从0开始，所以要加1）
        int month = calendar.get(Calendar.MONTH) + 1;

        // 设置为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 获取本月第一天的日期
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // 设置为本月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        if (ObjectUtils.isEmpty(jsonMap.get("startTime"))){
            jsonMap.put("startTime",year + "-" + month + "-" + firstDayOfMonth);
        }
        if (ObjectUtils.isEmpty(jsonMap.get("endTime"))){
            jsonMap.put("endTime",DateFormatUtils.format(new Date(),PanXiaoZhang.yMd()));
        }
        jsonMap.put("startTime",jsonMap.get("startTime") + " 00:00:00");
        jsonMap.put("endTime",jsonMap.get("endTime") + " 23:59:59");
        //删除用户id
        jsonMap.remove("personnelId");
        ReturnEntity returnEntity = new ReturnEntity(CodeEntity.CODE_SUCCEED, iDispatchApplicationManagementMapper.queryAll(jsonMap), "");
        return returnEntity;
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
            }else if (name.equals("add")){
                ReturnEntity returnEntity = add(request);
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
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    //同意
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        DispatchApplicationManagement jsonParam = PanXiaoZhang.getJSONParam(request, DispatchApplicationManagement.class);
        //查询该人员信息
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询当前该条信息
        DispatchApplicationManagement dispatchApplicationManagement = iDispatchApplicationManagementMapper.selectById(jsonParam.getId());
        //判断数据是否存在
        if (ObjectUtils.isEmpty(dispatchApplicationManagement)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据不存在");
        }
        //判断该数据总状态
        if (!dispatchApplicationManagement.getApplicantState().equals("pending")){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该状态下不可审核");
        }
        //查询申请人信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("personnel_code",dispatchApplicationManagement.getPersonnelCode());
        SysPersonnel personnel = iSysPersonnelMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(personnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该调派人员不存在");
        }
        //当前时间
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms());
        //那些字段不可为空
        DispatchApplicationManagementNotNull dispatchApplicationManagementNotNull = new DispatchApplicationManagementNotNull(
                "isNotNullAndIsLengthNot0",
                "isNotNullAndIsLengthNot0"
        );
        //可以更改那些字段
        DispatchApplicationManagement applicationManagement = new DispatchApplicationManagement(
                jsonParam.getId(),
                jsonParam.getVerifierRemark(),
                jsonParam.getVerifierState()
        );
        //进行判断那些字段不可为空
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                applicationManagement,
                dispatchApplicationManagementNotNull
        );
        if (!ObjectUtils.isEmpty(sysPersonnel)){
            //查询该数据
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_id",sysPersonnel.getId());
            wrapper.eq("verifier_state","pending");
            wrapper.eq("dispatch_code",dispatchApplicationManagement.getDispatchCode());
            DispatchApplicationReimbursement reimbursement = iDispatchApplicationReimbursementMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(reimbursement)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据已审核过");
            }
            int updateById = iDispatchApplicationReimbursementMapper.updateById(new DispatchApplicationReimbursement(
                    reimbursement.getId(),
                    null,
                    jsonParam.getVerifierRemark(),
                    jsonParam.getVerifierState(),
                    format,
                    null,
                    null,
                    null
            ));
            if (updateById != 1) {
                return new ReturnEntity(CodeEntity.CODE_ERROR, "审核数据更改失败");
            }
        }
        if (returnEntity.getState()){
            return returnEntity;
        }
        //至此拦截机制结束
        if (jsonParam.getVerifierState().equals("agree")){
            Map map = new HashMap();
            map.put("reimbursementRecordCode",dispatchApplicationManagement.getDispatchCode());
            map.put("approvalState","pending");
            map.put("nePersonnelId",sysPersonnel.getId());//不等于当前审核人的资源编码
            List<DispatchApplicationReimbursement> dispatchApplicationReimbursements = iDispatchApplicationReimbursementMapper.queryAll(map);
            if (dispatchApplicationReimbursements.size() > 0){
                Integer integer = iDispatchApplicationReimbursementMapper.queryMax(map);
                if (!integer.equals(dispatchApplicationManagement.getMaxNumber())){//如果得出的流转等级不一致将进行修改
                    int updateById = iDispatchApplicationManagementMapper.updateById(new DispatchApplicationManagement(
                            dispatchApplicationManagement.getId(),
                            integer
                    ));
                    if (updateById != 1){
                        return new ReturnEntity(CodeEntity.CODE_ERROR,"修改流转等级失败");
                    }
                    //进行消息通知流转下一级
                    for (int i = 0; i < dispatchApplicationReimbursements.size(); i++) {
                        DispatchApplicationReimbursement reimbursement = dispatchApplicationReimbursements.get(i);
                        if (reimbursement.getNumber().equals(integer)){
                            SysPersonnel selectById = iSysPersonnelMapper.selectById(reimbursement.getPersonnelId());
                            //告知审核人前往审核
                            PanXiaoZhang.postWechatFer(
                                    selectById.getOpenId(),
                                    "调派申请",
                                    "",
                                    personnel.getName() + "提交了调派申请",
                                    "",
                                    urlTransfer + "?from=zn&redirect_url=" + urlDispatch + "?fromDispatchVerify=true"
                            );
                        }
                    }
                }
                return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
            }
            int updateById = iDispatchApplicationManagementMapper.updateById(new DispatchApplicationManagement(
                    dispatchApplicationManagement.getId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "agree",
                    new Date()
            ));
            if (updateById != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"修改数据失败");
            }
            Integer integer = PanXiaoZhang.eqDate(dispatchApplicationManagement.getDispathchTime());
            if (integer < 1){
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("personnel_code",dispatchApplicationManagement.getPersonnelCode());
                queryWrapper.eq("management_id",dispatchApplicationManagement.getAgoManagementId());
                int update = iManagementPersonnelMapper.update(new ManagementPersonnel(
                        null,
                        dispatchApplicationManagement.getLaterManagementId(),
                        null
                ), queryWrapper);
                if (update != 1){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"修改项目组失败");
                }
            }
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "调派信息",
                    "",
                    "同意调派",
                    "",
                    ""
            );
            String[] phone = PhoneConfig.opendId();
            SysManagement management = iSysManagementMapper.selectById(dispatchApplicationManagement.getLaterManagementId());
            for (int i = 0; i < phone.length; i++) {
                String openId = phone[i];
                PanXiaoZhang.postWechatFer(
                        openId,
                        "调派信息",
                        "",
                        personnel.getName() + "已调派到" + management.getName(),
                        "",
                        ""
                );
            }
        }else if (jsonParam.getVerifierState().equals("refuse")){//如果拒绝审核
            int updateById = iDispatchApplicationManagementMapper.updateById(new DispatchApplicationManagement(
                    dispatchApplicationManagement.getId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "refuse",
                    new Date()
            ));
            if (updateById != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"修改数据失败");
            }
            String remark = sysPersonnel.getName() + "拒绝了您的请求";
            if (!ObjectUtils.isEmpty(jsonParam.getRemark())){
                remark += "，拒绝原因是：" + jsonParam.getRemark();
            }
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "调派信息",
                    "",
                    remark,
                    "",
                    ""
            );
        }else {
            return new ReturnEntity(CodeEntity.CODE_ERROR,"审核状态不正确");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
    }

    //查看历史审核数据
    private ReturnEntity cat_collate_past_records(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(jsonMap.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        jsonMap.put("type","gt");
        jsonMap.put("queryAll","yes");
        List<DispatchApplicationManagement> dispatchApplicationManagements = iDispatchApplicationManagementMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,dispatchApplicationManagements,"");
    }


    //查看历史提交的数据
    private ReturnEntity cat_past_records(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(jsonMap.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        //查询该人员信息
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(String.valueOf(jsonMap.get("personnelId")));
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"查无此人");
        }
        //删除用户id
        jsonMap.remove("personnelId");
        jsonMap.put("personnelCode",sysPersonnel.getPersonnelCode());
        List<DispatchApplicationManagement> dispatchApplicationManagements = iDispatchApplicationManagementMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,dispatchApplicationManagements,"");
    }

    //提交调派申请信息
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        DispatchApplicationManagement jsonParam = PanXiaoZhang.getJSONParam(request, DispatchApplicationManagement.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new DispatchApplicationManagementNotNull(
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );

        if (returnEntity.getState()){
            return returnEntity;
        }

        //查询该人员信息
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        if (!ObjectUtils.isEmpty(sysPersonnel)){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            wrapper.eq("applicant_state","pending");
            List selectList = iDispatchApplicationManagementMapper.selectList(wrapper);
            if (selectList.size() > 0){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前已存在调派申请");
            }
        }

        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询当前是否已有调派信息
        //QueryWrapper queryWrapper = new QueryWrapper();
        //queryWrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        //queryWrapper.ne("ago_verifier_state","pending");
        //queryWrapper.ne("later_verifier_state","pending");
        //List<DispatchApplicationManagement> list = iDispatchApplicationManagementMapper.selectList(queryWrapper);
        //if (list.size() > 1){
        //    return new ReturnEntity(CodeEntity.CODE_ERROR,"当前已有调派申请");
        //}
        if (!ObjectUtils.isEmpty(sysPersonnel)){
            //添加申请人
            jsonParam.setApplicant(sysPersonnel.getName());
            //添加申请人手机号
            jsonParam.setPhone(sysPersonnel.getPhone());
            //添加申请人资源代码
            jsonParam.setPersonnelCode(sysPersonnel.getPersonnelCode());
            //查询申请人当前项目
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
            if (selectList.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前暂无项目，不符合调派逻辑,无法使用改功能");
            }else if (selectList.size() > 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"您有多个所属项目，不符合调派逻辑,无法使用改功能");
            }
            ManagementPersonnel managementPersonnel = selectList.get(0);
            if (managementPersonnel.getManagementId().equals(jsonParam.getLaterManagementId())){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"调派后项目重复");
            }
            SysManagement management = iSysManagementMapper.selectById(jsonParam.getLaterManagementId());
            if (!management.getManagementState().equals(1)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
            }
            //添加当前项目数据编码
            jsonParam.setAgoManagementId(managementPersonnel.getManagementId());
            //添加调派代码
            jsonParam.setDispatchCode(System.currentTimeMillis() + PanXiaoZhang.ran(4));
            //设置初始状态
            jsonParam.setApplicantState("pending");
        }
        //记录调派前人员手机号
        String agoOpenId = "";
        //记录调派后人员手机号
        String laterOpenId = "";
        //从最大的开始审核
        jsonParam.setMaxNumber(0);
        //设置审核职位
        Integer[] integers = {1,3,4};
        //存储map
        Map<Integer, SysRole> mapRole = new HashMap();
        //存储通知的人
        Map<Integer, SysPersonnel> mapPersonnel = new HashMap();
        //是否有审核人
        Integer appNumber = 0;
        //查询职位名
        if (integers.length > 0){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.in("id",integers);
            List<SysRole> selectList = iSysRoleMapper.selectList(wrapper);
            for (int i = 0; i < selectList.size(); i++) {
                SysRole sysRole = selectList.get(i);
                mapRole.put(sysRole.getId(),sysRole);
                if (sysRole.getLevelSorting() > jsonParam.getMaxNumber()){
                    jsonParam.setMaxNumber(sysRole.getLevelSorting());
                    mapPersonnel.clear();
                }

                if (!ObjectUtils.isEmpty(jsonParam.getAgoManagementId())) {
                    appNumber++;
                    List<SysPersonnel> sysPersonnels = iWhiteSysPersonnelService.myLeader(sysRole.getId(), jsonParam.getAgoManagementId());
                    //if (sysPersonnels.size() < 1) {
                    //    return new ReturnEntity(CodeEntity.CODE_ERROR, "当前项目现没有" + sysRole.getName() + "无法提交");
                    //}
                    for (int j = 0; j < sysPersonnels.size(); j++) {
                        SysPersonnel personnel = sysPersonnels.get(j);
                        if (ObjectUtils.isEmpty(mapPersonnel.get(personnel.getId()))){
                            //添加当前项目主管
                            jsonParam.setAgoPersonnelId(personnel.getId());
                            //添加默认审核状态
                            jsonParam.setAgoVerifierState("pending");
                            //记录审核人审核人
                            mapPersonnel.put(personnel.getId(),personnel);
                            int insert = iDispatchApplicationReimbursementMapper.insert(new DispatchApplicationReimbursement(
                                    null,
                                    personnel.getId(),
                                    null,
                                    "pending",
                                    null,
                                    jsonParam.getDispatchCode(),
                                    "调派前审核人",
                                    sysRole.getLevelSorting()
                            ));
                            //如果返回值不能鱼1则判断失败
                            if (insert != 1){
                                return new ReturnEntity(
                                        CodeEntity.CODE_ERROR,
                                        "添加当前项目审核人失败"
                                );
                            }
                        }
                    }
                }

                //调派后项目数据编码
                if (!ObjectUtils.isEmpty(jsonParam.getLaterManagementId())){
                    appNumber++;
                    List<SysPersonnel> sysPersonnels = iWhiteSysPersonnelService.myLeader(sysRole.getId(), jsonParam.getLaterManagementId());
                    //if (sysPersonnels.size() < 1){
                    //    return new ReturnEntity(CodeEntity.CODE_ERROR,"调派后项目没有" + sysRole.getName() + "无法提交");
                    //}
                    for (int j = 0; j < sysPersonnels.size(); j++) {
                        SysPersonnel personnel = sysPersonnels.get(j);
                        if (ObjectUtils.isEmpty(mapPersonnel.get(personnel.getId()))) {
                            //添加调派后项目主管
                            jsonParam.setLaterPersonnelId(personnel.getId());
                            //添加默认审核状态
                            jsonParam.setLaterVerifierState("pending");
                            //记录审核人审核人
                            mapPersonnel.put(personnel.getId(), personnel);
                            int insert = iDispatchApplicationReimbursementMapper.insert(new DispatchApplicationReimbursement(
                                    null,
                                    personnel.getId(),
                                    null,
                                    "pending",
                                    null,
                                    jsonParam.getDispatchCode(),
                                    "调派后审核人",
                                    sysRole.getLevelSorting()
                            ));
                            //如果返回值不能鱼1则判断失败
                            if (insert != 1) {
                                return new ReturnEntity(
                                        CodeEntity.CODE_ERROR,
                                        "添加调派后项目审核人失败"
                                );
                            }
                        }
                    }
                }
            }
        }
        //判断是否有审核人
        if (appNumber < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"暂无审核人");
        }
        //添加申请时间
        jsonParam.setApplicantTime(new Date());
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iDispatchApplicationManagementMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        mapPersonnel.forEach((key,value)->{
            //告知审核人前往审核
            PanXiaoZhang.postWechatFer(
                    value.getOpenId(),
                    "调派信息",
                    "",
                    sysPersonnel.getName() + "提交了调派申请",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlDispatch + "?fromDispatchVerify=true"
            );
        });
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"申请成功");
    }

    //查询当前申请人信息
    private ReturnEntity cat(HttpServletRequest request) throws IOException {
        DispatchApplicationManagement jsonParam = PanXiaoZhang.getJSONParam(request, DispatchApplicationManagement.class);
        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        Map map = new HashMap();
        map.put("id",jsonParam.getPersonnelId());
        List<SysPersonnel> sysPersonnels = iWhiteSysPersonnelService.queryAll(map);
        jsonParam.setSysPersonnels(sysPersonnels);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, jsonParam,"");
    }
}
