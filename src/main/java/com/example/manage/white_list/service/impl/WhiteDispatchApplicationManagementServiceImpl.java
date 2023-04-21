package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.DispatchApplicationManagement;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.DispatchApplicationManagementNotNull;
import com.example.manage.mapper.IDispatchApplicationManagementMapper;
import com.example.manage.mapper.IManagementPersonnelMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.WhiteSysPersonnelMapper;
import com.example.manage.util.PanXiaoZhang;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/7
 */

@Slf4j
@Service
public class WhiteDispatchApplicationManagementServiceImpl implements IWhiteDispatchApplicationManagementService {

    @Value("${role.manage}")
    private Integer roleId;

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

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
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
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
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
                return add(request);
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
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
        if (dispatchApplicationManagement.getAgoPersonnelId().equals(jsonParam.getPersonnelId())){//如果相等则为当前项目主管
            //判断当前是否待审批状态
            if (!dispatchApplicationManagement.getAgoVerifierState().equals("pending")){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前状态不可更改");
            }
            //修改调派前项目内容
            applicationManagement.setAgoAuditTime(format);
            applicationManagement.setAgoVerifierRemark(jsonParam.getVerifierRemark());
            applicationManagement.setAgoVerifierState(jsonParam.getVerifierState());
            //保持调派后项目内容
            applicationManagement.setLaterAuditTime(dispatchApplicationManagement.getLaterAuditTime());
            applicationManagement.setLaterVerifierRemark(dispatchApplicationManagement.getLaterVerifierRemark());
            applicationManagement.setLaterVerifierState(dispatchApplicationManagement.getLaterVerifierState());
        }else if (dispatchApplicationManagement.getLaterPersonnelId().equals(jsonParam.getPersonnelId())){//如果相等则为调派后项目主管
            //判断当前是否待审批状态
            if (!dispatchApplicationManagement.getLaterVerifierState().equals("pending")){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前状态不可更改");
            }
            //修改调派后项目内容
            applicationManagement.setLaterAuditTime(format);
            applicationManagement.setLaterVerifierRemark(jsonParam.getVerifierRemark());
            applicationManagement.setLaterVerifierState(jsonParam.getVerifierState());
            //保持调派前项目内容
            applicationManagement.setAgoAuditTime(dispatchApplicationManagement.getLaterAuditTime());
            applicationManagement.setAgoVerifierRemark(dispatchApplicationManagement.getAgoVerifierRemark());
            applicationManagement.setAgoVerifierState(dispatchApplicationManagement.getAgoVerifierState());
        }else {
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该条数据异常");
        }
        //进行判断那些字段不可为空
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                applicationManagement,
                dispatchApplicationManagementNotNull
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //至此拦截机制结束
        int updateById = iDispatchApplicationManagementMapper.updateById(applicationManagement);
        //查询申请人信息
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",dispatchApplicationManagement.getPersonnelCode());
        SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(personnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该调派人员不存在");
        }
        if (dispatchApplicationManagement.getAgoVerifierState().equals("pending") && !applicationManagement.getAgoVerifierState().equals("pending")){
            if (applicationManagement.getAgoVerifierState().equals("refuse")){
                SysPersonnel byId = iSysPersonnelMapper.selectById(dispatchApplicationManagement.getAgoPersonnelId());
                String remark = "";
                if (!ObjectUtils.isEmpty(applicationManagement.getAgoVerifierRemark())){
                    remark = applicationManagement.getAgoVerifierRemark();
                }
                PanXiaoZhang.postWechatFer(
                        personnel.getOpenId(),
                        "调派结果通知",
                        "",
                        byId.getName() + "拒绝了您的请求，" + "拒绝原因是：" + remark,
                        "",
                        ""
                );
            }
        }
        if (dispatchApplicationManagement.getLaterVerifierState().equals("pending") && !applicationManagement.getLaterVerifierState().equals("pending")) {
            if (applicationManagement.getLaterVerifierState().equals("refuse")) {
                SysPersonnel byId = iSysPersonnelMapper.selectById(dispatchApplicationManagement.getLaterPersonnelId());
                String remark = "";
                if (!ObjectUtils.isEmpty(applicationManagement.getLaterVerifierRemark())){
                    remark = applicationManagement.getLaterVerifierRemark();
                }
                PanXiaoZhang.postWechatFer(
                        personnel.getOpenId(),
                        "调派结果通知",
                        "",
                        byId.getName() + "拒绝了您的请求，" + "拒绝原因是：" + remark,
                        "",
                        ""
                );
            }
        }
        if (applicationManagement.getAgoVerifierState().equals("agree") && applicationManagement.getLaterVerifierState().equals("agree")){
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "调派结果通知",
                    "",
                    "同意调派",
                    "",
                    ""
            );
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("personnel_code",dispatchApplicationManagement.getPersonnelCode());
            int byId = iManagementPersonnelMapper.update(new ManagementPersonnel(
                    dispatchApplicationManagement.getLaterManagementId()
            ),queryWrapper);
            if (byId != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"用户项目更改失败");
            }
        }
        if (updateById != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"数据更改失败");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"提交成功");
    }

    //查看历史审核数据
    private ReturnEntity cat_collate_past_records(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(jsonMap.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
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
        List<SysPersonnel> sysPersonnels = iWhiteSysPersonnelService.queryAll(jsonMap);
        if (sysPersonnels.size() != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        //删除用户id
        jsonMap.remove("personnelId");
        SysPersonnel sysPersonnel = sysPersonnels.get(0);
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
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询该人员信息
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询当前是否已有调派信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        queryWrapper.ne("ago_verifier_state","pending");
        queryWrapper.ne("later_verifier_state","pending");
        List<DispatchApplicationManagement> list = iDispatchApplicationManagementMapper.selectList(queryWrapper);
        if (list.size() > 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前已有调派申请");
        }
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
            //添加当前项目数据编码
            jsonParam.setAgoManagementId(managementPersonnel.getManagementId());
            //添加调派代码
            jsonParam.setDispatchCode(System.currentTimeMillis() + PanXiaoZhang.ran(4));
        }
        //记录调派前人员手机号
        String agoOpenId = "";
        //记录调派后人员手机号
        String laterOpenId = "";
        //调派前项目数据编码
        if (!ObjectUtils.isEmpty(jsonParam.getAgoManagementId())){
            List<SysPersonnel> sysPersonnels = iWhiteSysPersonnelService.myLeader(roleId, jsonParam.getAgoManagementId());
            if (sysPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目现没有负责人无法提交");
            }
            SysPersonnel personnel = sysPersonnels.get(0);
            //添加当前项目主管
            jsonParam.setAgoPersonnelId(personnel.getId());
            //添加默认审核状态
            jsonParam.setAgoVerifierState("pending");
            //记录审核人审核人
            agoOpenId = personnel.getOpenId();
        }
        //调派后项目数据编码
        if (!ObjectUtils.isEmpty(jsonParam.getLaterManagementId())){
            List<SysPersonnel> sysPersonnels = iWhiteSysPersonnelService.myLeader(roleId, jsonParam.getLaterManagementId());
            if (sysPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"调派后项目没有负责人无法提交");
            }
            SysPersonnel personnel = sysPersonnels.get(0);
            //添加调派后项目主管
            jsonParam.setLaterPersonnelId(personnel.getId());
            //添加默认审核状态
            jsonParam.setLaterVerifierState("pending");
            //记录审核人手机号
            laterOpenId = personnel.getOpenId();
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
        //告知审核人前往审核
        PanXiaoZhang.postWechatFer(
                agoOpenId,
                sysPersonnel.getName() + "提交了调派申请",
                "",
                "请及时前往核实",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlDispatch + "from_dispatch_verify=" + true
        );
        //告知审核人前往审核
        PanXiaoZhang.postWechatFer(
                laterOpenId,
                sysPersonnel.getName() + "提交了调派申请",
                "",
                "请及时前往核实",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlDispatch + "from_dispatch_verify=" + true
        );
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
