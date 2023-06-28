package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.FurloughRecordNotNull;
import com.example.manage.entity.is_not_null.FurloughReimbursementNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteFurloughRecordService;
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
import java.time.LocalDate;
import java.util.*;

/**
 * @avthor 潘小章
 * @date 2023/5/17
 */

@Slf4j
@Service
public class WhiteFurloughRecordServiceImpl implements IWhiteFurloughRecordService {

    @Value("${url.rest_list}")
    private String urlDispatch;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Value("${role.manage}")
    private Integer manage;

    @Value("${role.manage3}")
    private Integer manage3;

    @Value("${role.manage4}")
    private Integer manage4;

    @Value("${role.manage5}")
    private Integer manage5;

    @Value("${role.dageid}")
    private Integer dageid;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private IFurloughRecordMapper iFurloughRecordMapper;

    @Resource
    private IFurloughReimbursementMapper iFurloughReimbursementMapper;

    @Resource
    private WhiteFurloughReimbursementMapper whiteFurloughReimbursementMapper;

    @Resource
    private WhiteFurloughRecordMapper whiteFurloughRecordMapper;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private IWhiteSysPersonnelService iWhiteSysPersonnelService;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private IReimbursementImageMapper iReimbursementImageMapper;

    @Resource
    private IDivisionPersonnelMapper iDivisionPersonnelMapper;

    @Resource
    private IDivisionManagementPersonnelMapper iDivisionManagementPersonnelMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
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

    //查询历史审核的数据
    private ReturnEntity cat_collate_past_records(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(jsonMap.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        jsonMap.put("type","gt");
        jsonMap.put("auditorPersonnelId",jsonMap.get("personnelId"));
        jsonMap.remove("personnelId");
        List<FurloughRecord> furloughRecords = whiteFurloughRecordMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,furloughRecords,"");
    }

    //查询历史提交的数据
    private ReturnEntity cat_past_records(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(jsonMap.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        List<FurloughRecord> furloughRecords = whiteFurloughRecordMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,furloughRecords,"");
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        return null;
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
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //请假
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        FurloughRecord jsonParam = PanXiaoZhang.getJSONParam(request, FurloughRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new FurloughRecordNotNull(
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        ""
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //判断该状态是否进行提交数据
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }

        Integer integer = PanXiaoZhang.eqTime(jsonParam.getStartTime(), jsonParam.getEndTime());
        if (integer != -1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请假开始时间要小于结束时间");
        }
        //查询职位等级
        SysRole role = iSysRoleMapper.selectById(sysPersonnel.getRoleId());
        QueryWrapper wrapper = new QueryWrapper();
        //查询是否有请假的记录
        wrapper.apply("(start_time between '" + DateFormatUtils.format(jsonParam.getStartTime(),PanXiaoZhang.yMdHms()) + "' AND " +
                "'" + DateFormatUtils.format(jsonParam.getEndTime(),PanXiaoZhang.yMdHms()) + "'\n" +
                "\tOR\n" +
                "\tend_time between '" + DateFormatUtils.format(jsonParam.getStartTime(),PanXiaoZhang.yMdHms()) + "' AND " +
                "'" + DateFormatUtils.format(jsonParam.getEndTime(),PanXiaoZhang.yMdHms()) + "')" +
                "AND\n" +
                "personnel_id = "+ sysPersonnel.getId());
        List<FurloughRecord> furloughRecords = iFurloughRecordMapper.selectList(wrapper);
        if (furloughRecords.size() > 0){
            return new ReturnEntity(CodeEntity.CODE_ERROR,DateFormatUtils.format(jsonParam.getStartTime(), PanXiaoZhang.yMd()) + "~" + DateFormatUtils.format(jsonParam.getEndTime(), PanXiaoZhang.yMd()) + "已有请假记录");
        }
        //生成编码
        String code = System.currentTimeMillis() + PanXiaoZhang.ran(4);
        //添加申请人
        jsonParam.setPersonnelName(sysPersonnel.getName());
        //设置初始状态
        jsonParam.setReissueState("pending");
        //设置编码
        jsonParam.setReissueCode(code);
        //存储map
        Map<Integer, SysRole> mapRole = new HashMap();
        //存储通知的人
        Map<Integer, SysPersonnel> mapPersonnel = new HashMap();
        //从最大的开始审核
        jsonParam.setMaxNumber(0);
        //区域经理层请假
        if (role.getId().equals(manage3) || role.getId().equals(manage4)){
            wrapper = new QueryWrapper();
            wrapper.eq("personnel_id",jsonParam.getPersonnelId());
            List<DivisionManagementPersonnel> selectList = iDivisionManagementPersonnelMapper.selectList(wrapper);
            List<DivisionPersonnel> list = new ArrayList<>();
            if (selectList.size() > 0){
                DivisionManagementPersonnel divisionManagementPersonnel = selectList.get(0);
                wrapper = new QueryWrapper();
                wrapper.eq("division_management_id",divisionManagementPersonnel.getDivisionManagementId());
                list = iDivisionPersonnelMapper.selectList(wrapper);
            }
            list.add(new DivisionPersonnel(
                null,
                null,
                dageid
            ));
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                integers.add(list.get(i).getPersonnelId());
            }
            //查询人员信息
            wrapper = new QueryWrapper();
            wrapper.in("id",integers.toArray(new Integer[integers.size()]));
            List<SysPersonnel> personnels = iSysPersonnelMapper.selectList(wrapper);
            for (int i = 0; i < personnels.size(); i++) {
                SysPersonnel personnel = personnels.get(i);
                SysRole sysRole = iSysRoleMapper.selectById(personnel.getRoleId());
                mapRole.put(sysRole.getId(),sysRole);
                if (sysRole.getLevelSorting() > jsonParam.getMaxNumber()){
                    jsonParam.setMaxNumber(sysRole.getLevelSorting());
                    mapPersonnel.clear();
                }
                if (ObjectUtils.isEmpty(mapPersonnel.get(personnel.getId()))){
                    //添加当前项目主管
                    Integer id = personnel.getId();
                    //添加默认审核状态
                    String pending = "pending";
                    //记录审核人审核人
                    mapPersonnel.put(personnel.getId(),personnel);
                    int insert = iFurloughReimbursementMapper.insert(new FurloughReimbursement(
                            null,
                            id,
                            null,
                            null,
                            jsonParam.getReissueCode(),
                            pending,
                            sysRole.getLevelSorting()
                    ));
                    //如果返回值不能于1则判断失败
                    if (insert != 1){
                        return new ReturnEntity(
                                CodeEntity.CODE_ERROR,
                                "添加审核人" + personnel.getName() + "失败"
                        );
                    }
                }
            }
        }else if (role.getId().equals(manage) || role.getId().equals(manage5)){
            //员工请假
            wrapper = new QueryWrapper();
            //查询绑定项目
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> list = iManagementPersonnelMapper.selectList(wrapper);
            if (list.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目");
            }
            jsonParam.setManagementId(list.get(0).getManagementId());
            //查询项目
            SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
            if (ObjectUtils.isEmpty(management)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"项目不存在");
            }
            if (!management.getManagementState().equals(1)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
            }
            //添加项目名称
            jsonParam.setManagementName(management.getName());
            //设置审核职位
            Integer[] integers = {1,3};
            //查询职位名
            if (integers.length > 0){
                wrapper = new QueryWrapper();
                wrapper.in("id",integers);
                List<SysRole> selectList = iSysRoleMapper.selectList(wrapper);
                for (int i = 0; i < selectList.size(); i++) {
                    SysRole sysRole = selectList.get(i);
                    mapRole.put(sysRole.getId(),sysRole);
                    if (sysRole.getLevelSorting() > jsonParam.getMaxNumber()){
                        jsonParam.setMaxNumber(sysRole.getLevelSorting());
                        mapPersonnel.clear();
                    }
                    List<SysPersonnel> sysPersonnels = iWhiteSysPersonnelService.myLeader(sysRole.getId(), management.getId());
                    if (sysPersonnels.size() < 1) {
                        return new ReturnEntity(CodeEntity.CODE_ERROR, "当前项目现没有" + sysRole.getName() + "无法提交");
                    }
                    SysPersonnel personnel = sysPersonnels.get(0);
                    if (ObjectUtils.isEmpty(mapPersonnel.get(personnel.getId()))){
                        //添加当前项目主管
                        Integer id = personnel.getId();
                        //添加默认审核状态
                        String pending = "pending";
                        //记录审核人审核人
                        mapPersonnel.put(personnel.getId(),personnel);
                        int insert = iFurloughReimbursementMapper.insert(new FurloughReimbursement(
                                null,
                                id,
                                null,
                                null,
                                jsonParam.getReissueCode(),
                                pending,
                                sysRole.getLevelSorting()
                        ));
                        //如果返回值不能鱼1则判断失败
                        if (insert != 1){
                            return new ReturnEntity(
                                    CodeEntity.CODE_ERROR,
                                    "添加审核人" + personnel.getName() + "失败"
                            );
                        }
                    }
                }
            }
        }
        //添加申请时间
        jsonParam.setApplicantTime(new Date());
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //录入附件
        List<ReimbursementImage> reimbursementImages = jsonParam.getReimbursementImages();
        for (int i = 0; i < reimbursementImages.size(); i++) {
            ReimbursementImage reimbursementImage = reimbursementImages.get(i);
            int insertImage = iReimbursementImageMapper.insert(new ReimbursementImage(
                    reimbursementImage.getPathUrl(),
                    jsonParam.getReissueCode()
            ));
            if (insertImage != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        "附件添加失败"
                );
            }
        }
        //没有任何问题将数据录入进数据库
        int insert = iFurloughRecordMapper.insert(jsonParam);
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
                    "请假信息",
                    "",
                    sysPersonnel.getName() + "提交了请假信息",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlDispatch + "?fromDispatchVerify=true"
            );
        });
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"申请成功");
    }

    //审核
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        FurloughRecord jsonParam = PanXiaoZhang.getJSONParam(request, FurloughRecord.class);
        FurloughReimbursement furloughReimbursement = new FurloughReimbursement(
                jsonParam.getId(),
                jsonParam.getPersonnelId(),
                jsonParam.getVerifierRemark(),
                jsonParam.getReissueState()
        );
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                furloughReimbursement,
                new FurloughReimbursementNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        ""
                ));
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
        //查询当前该条信息
        FurloughRecord furloughRecord = iFurloughRecordMapper.selectById(jsonParam.getId());
        //判断数据是否存在
        if (ObjectUtils.isEmpty(furloughRecord)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据不存在");
        }
        //判断该数据总状态
        if (!furloughRecord.getReissueState().equals("pending")){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该状态下不可审核");
        }
        //当前时间
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms());
        //至此拦截机制结束
        if (!ObjectUtils.isEmpty(sysPersonnel)){
            //查询该数据
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_id",sysPersonnel.getId());
            wrapper.eq("reissue_state","pending");
            wrapper.eq("reissue_code",furloughRecord.getReissueCode());
            FurloughReimbursement reimbursement = iFurloughReimbursementMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(reimbursement)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据已审核过");
            }
            int updateById = iFurloughReimbursementMapper.updateById(new FurloughReimbursement(
                    reimbursement.getId(),
                    null,
                    jsonParam.getVerifierRemark(),
                    format,
                    null,
                    jsonParam.getReissueState(),
                    null
            ));
            if (updateById != 1) {
                return new ReturnEntity(CodeEntity.CODE_ERROR, "审核数据更改失败");
            }
        }
        if (jsonParam.getReissueState().equals("agree")){
            //查询除了当前用户其他是否还有未审核的
            Map map = new HashMap();
            map.put("reissueCode",furloughRecord.getReissueCode());
            map.put("nePersonnelId",jsonParam.getPersonnelId());
            map.put("reissueState","pending");
            List<FurloughReimbursement> reimbursements = whiteFurloughReimbursementMapper.queryAll(map);
            if (reimbursements.size() > 0){
                Integer integer = whiteFurloughReimbursementMapper.queryMax(map);
                if (!integer.equals(furloughRecord.getMaxNumber())){//如果得出的流转等级不一致将进行修改
                    int updateById = iFurloughRecordMapper.updateById(new FurloughRecord(
                            furloughRecord.getId(),
                            integer
                    ));
                    if (updateById != 1){
                        return new ReturnEntity(CodeEntity.CODE_ERROR,"修改流转等级失败");
                    }
                    //进行消息通知流转下一级
                    for (int i = 0; i < reimbursements.size(); i++) {
                        FurloughReimbursement furloughReimbursement1 = reimbursements.get(i);
                        if (furloughReimbursement1.getNumber().equals(integer)){
                            SysPersonnel selectById = iSysPersonnelMapper.selectById(furloughReimbursement1.getPersonnelId());
                            //告知审核人前往审核
                            PanXiaoZhang.postWechatFer(
                                    selectById.getOpenId(),
                                    "请假信息",
                                    "",
                                    sysPersonnel.getName() + "提交了的请假信息",
                                    "",
                                    urlTransfer + "?from=zn&redirect_url=" + urlDispatch + "?fromDispatchVerify=true"
                            );
                        }
                    }
                }
                return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
            }
            int updateById = iFurloughRecordMapper.updateById(new FurloughRecord(
                    furloughRecord.getId(),
                    null,
                    null,
                    null,
                    null,
                    "agree",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
            if (updateById != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"修改数据失败");
            }
            PanXiaoZhang.postWechatFer(
                    sysPersonnel.getOpenId(),
                    "请假信息",
                    "",
                    "请假申请通过",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlDispatch
            );
        }else if (jsonParam.getReissueState().equals("refuse")){//如果拒绝审核
            int updateById = iFurloughRecordMapper.updateById(new FurloughRecord(
                    furloughRecord.getId(),
                    null,
                    null,
                    null,
                    null,
                    "refuse",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
            if (updateById != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"修改数据失败");
            }
            String remark = sysPersonnel.getName() + "拒绝了您的假期请求";
            if (!ObjectUtils.isEmpty(jsonParam.getVerifierRemark())){
                remark += "，拒绝原因是：" + jsonParam.getVerifierRemark();
            }
            PanXiaoZhang.postWechatFer(
                    sysPersonnel.getOpenId(),
                    "请假信息",
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
}
