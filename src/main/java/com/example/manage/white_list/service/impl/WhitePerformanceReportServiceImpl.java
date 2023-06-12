package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.PerformanceReportNotNull;
import com.example.manage.entity.number.AuditDataNumber;
import com.example.manage.entity.number.PerformanceReportNumber;
import com.example.manage.mapper.*;
import com.example.manage.util.HttpUtil;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.*;
import com.example.manage.util.wechat.WechatMsg;
import com.example.manage.white_list.service.IWhitePerformanceReportService;
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
 * @date 2023/3/31
 */

@Slf4j
@Service
public class WhitePerformanceReportServiceImpl implements IWhitePerformanceReportService {

    @Value("${role.manage}")
    private Integer roleId;

    @Value("${role.manage5}")
    private Integer manage5;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Value("${url.performance}")
    private String urlPerformance;

    @Resource
    private IPerformanceReportMapper iPerformanceReportMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private NumberPerformanceReportMapper numberPerformanceReportMapper;

    @Resource
    private INumberAuditDataMapper iNumberAuditDataMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ICardTypeMapper iCardTypeMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("cat_number")){
                return cat_number(request);
            }else if (name.equals("cat_audit_number")){
                return cat_audit_number(request);
            }else if (name.equals("cat_audit")){
                return cat_audit(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }


    //方法总管外加事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("all_edit")){
                ReturnEntity returnEntity = all_edit(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("edit")){
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
            }else if (name.equals("update")){
                ReturnEntity returnEntity = update(request);
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

    //一键审核所有数据
    private ReturnEntity all_edit(HttpServletRequest request) throws IOException {
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }

        if (ObjectUtils.isEmpty(jsonParam.getApproverState())){
            jsonParam.setApproverState("agree");
        }

        QueryWrapper wrapper = new QueryWrapper();

        ArrayList<Integer> integerArrayList = new ArrayList<>();

        if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
            for (int i = 0; i < selectList.size(); i++) {
                integerArrayList.add(selectList.get(i).getManagementId());
            }
        }else {
            integerArrayList.add(jsonParam.getManagementId());
        }

        wrapper = new QueryWrapper();
        wrapper.eq("approver_state","pending");
        wrapper.in("management_id",integerArrayList);
        List<PerformanceReport> performanceReports = iPerformanceReportMapper.selectList(wrapper);
        if (performanceReports.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"没有需要审核的数据");
        }
        int numberUpdate = 0;
        for (int i = 0; i < performanceReports.size(); i++) {
            PerformanceReport performanceReport = performanceReports.get(i);
            //进行审批修改
            int updateById = iPerformanceReportMapper.updateById(new PerformanceReport(
                    performanceReport.getId(),
                    jsonParam.getCommentsFromReviewers(),
                    jsonParam.getApproverState(),
                    DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms())
            ));
            //当返回值不为1的时候判断修改失败
            if (updateById != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        "报告编码为" + performanceReport.getId() + "审核失败"
                );
            }
            numberUpdate += updateById;
        }
        for (int i = 0; i < performanceReports.size(); i++) {
            PerformanceReport performanceReport = performanceReports.get(i);
            wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",performanceReport.getPersonnelCode());
            SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "",
                    "",
                    performanceReport.getReportTime() + "提交的业绩信息,已审核",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlPerformance
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功(" + numberUpdate + "/" + performanceReports.size() + ")");
    }

    //更正
    private ReturnEntity update(HttpServletRequest request) throws IOException {
        //获取提交数据
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        if (
                jsonParam.getEntryNumber() < 1
                        || jsonParam.getApprovedNumber() < 0
                        || jsonParam.getValidNumber() < 0
                        || jsonParam.getRefuseNumber() < 0
        ){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非法填写参数");
        }
        //判断填报数量是否正确
        if (jsonParam.getEntryNumber() < (jsonParam.getApprovedNumber() + jsonParam.getRefuseNumber())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"批核加拒绝总数量不能大于进件数");
        }
        if (jsonParam.getApprovedNumber() < jsonParam.getValidNumber()){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"有效数不能大于批核数");
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询该数据
        PerformanceReport performanceReport = iPerformanceReportMapper.selectById(jsonParam.getId());
        if (ObjectUtils.isEmpty(performanceReport.getId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该业绩信息不存在");
        }
        if (performanceReport.getApproverState().equals("pending")){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该业绩信息处于审核状态中，不可修改");
        }
        //查询当前是否有提交的数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",performanceReport.getPersonnelCode());
        wrapper.apply(true, "report_time = '" + performanceReport.getReportTime() + "'");
        wrapper.eq("card_type_id",jsonParam.getCardTypeId());
        wrapper.ne("approver_state","refuse");
        List<PerformanceReport> selectList = iPerformanceReportMapper.selectList(wrapper);
        if (selectList.size() > 0){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"数据不可重复");
        }
        //将当前所属项目加入
        wrapper = new QueryWrapper();
        if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(wrapper);
            if (managementPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目");
            }
            ManagementPersonnel managementPersonnel = managementPersonnels.get(0);
            jsonParam.setManagementId(managementPersonnel.getManagementId());
        }
        SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
        if (!management.getManagementState().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
        }
        //查询该项目主管
        Map map = new HashMap();
        map.put("managementId",jsonParam.getManagementId());
        map.put("roleId",roleId);
        map.put("employmentStatus","1");
        List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
        if (sysPersonnels.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目无人审核，无法提交");
        }
        SysPersonnel personnel = sysPersonnels.get(0);

        jsonParam.setApproverPersonnelId(personnel.getId());

        jsonParam.setReportTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));

        jsonParam.setApproverState("pending");
        //没有任何问题将数据录入进数据库
        int updateById = iPerformanceReportMapper.updateById(new PerformanceReport(
            performanceReport.getId(),
            null,
            null,
            DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()),//提交修改报告时间
            null,
            null,
            personnel.getId(),//添加最新审核人编码
            null,
            null,
            "pending",//添加默认状态
            null,
            null,
            null,
            jsonParam.getEntryNumber(),//进件数
            jsonParam.getApprovedNumber(),//批核数
            jsonParam.getValidNumber(),//有效数
            jsonParam.getRefuseNumber()//拒绝数
        ));
        //如果返回值不能鱼1则判断失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
       PanXiaoZhang.postWechatFer(
                personnel.getOpenId(),
                "",
                "",
                sysPersonnel.getName() + ":修改了业绩信息,请前往审核",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlPerformance
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"修改成功,请等待审核");
    }

    //查询审核数据数字
    private ReturnEntity cat_audit(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        map.put("approverPersonnelId",map.get("personnelId"));
        map.remove("personnelId");
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, iPerformanceReportMapper.queryAll(map),"");
    }

    //审核状态数据统计
    private ReturnEntity cat_audit_number(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        List<MapEntity> entityList = new ArrayList<>();
        AuditDataNumber auditDataNumber = iNumberAuditDataMapper.queryOne(map);
        //全部
        entityList.add(new MapEntity(
                "",
                "全部",
                auditDataNumber.getAll()
        ));
        //批核未激活
        entityList.add(new MapEntity(
                "pending",
                "等待审核",
                auditDataNumber.getPending()
        ));
        //激活
        entityList.add(new MapEntity(
                "agree",
                "审核通过",
                auditDataNumber.getAgree()
        ));
        //拒绝
        entityList.add(new MapEntity(
                "refuse",
                "审核拒绝",
                auditDataNumber.getRefuse()
        ));
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, entityList,"");
    }

    //更改信息
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new PerformanceReportNotNull(
                        "isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        PerformanceReport performanceReport = iPerformanceReportMapper.selectById(jsonParam.getId());
        if (!performanceReport.getApproverState().equals("pending")){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"该状态下不可修改信息");
        }
        //进行审批修改
        int updateById = iPerformanceReportMapper.updateById(new PerformanceReport(
                jsonParam.getId(),
                jsonParam.getCommentsFromReviewers(),
                jsonParam.getApproverState(),
                DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms())
        ));
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "审核失败"
            );
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",performanceReport.getPersonnelCode());
        SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
        ReturnEntity entity = PanXiaoZhang.postWechatFer(
                personnel.getOpenId(),
                "",
                "",
                performanceReport.getReportTime() + "提交的业绩信息,已审核",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlPerformance
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
    }

    //查询各个数据状态
    private ReturnEntity cat_number(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        List<List<MapEntity>> listList = new ArrayList<>();
        List<MapEntity> entityList = new ArrayList<>();

        Object startTime = map.get("startTime");
        if (!ObjectUtils.isEmpty(startTime)){
            map.put("startTime",startTime + " 00:00:00");
        }
        Object endTime = map.get("endTime");
        if (!ObjectUtils.isEmpty(endTime)){
            map.put("endTime",endTime + " 23:59:59");
        }

        PerformanceReportNumber performanceReportNumber = numberPerformanceReportMapper.queryOne(map);
        //全部
        entityList.add(new MapEntity(
                "进件",
                "进件",
                performanceReportNumber.getAll()
        ));
        //批核未激活
        entityList.add(new MapEntity(
                "批核",
                "批核",
                performanceReportNumber.getApprove()
        ));
        //激活
        entityList.add(new MapEntity(
                "有效",
                "有效",
                performanceReportNumber.getActive()
        ));
        //拒绝
        entityList.add(new MapEntity(
                "拒绝",
                "拒绝",
                performanceReportNumber.getRefuse()
        ));
        //转人工
        entityList.add(new MapEntity(
                "转人工",
                "转人工",
                performanceReportNumber.getPendding()
        ));
        listList.add(entityList);
        List<MapEntity> list = new ArrayList<>();
        //当月激活
        list.add(new MapEntity(
                "当月进件",
                "当月进件",
                performanceReportNumber.getThisMonthActive()
        ));
        listList.add(list);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, listList,"");
    }

    //员工提交报告
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        //获取提交数据
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new PerformanceReportNotNull(
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        if (
            jsonParam.getEntryNumber() < 1
            || jsonParam.getApprovedNumber() < 0
            || jsonParam.getValidNumber() < 0
            || jsonParam.getRefuseNumber() < 0
        ){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非法填写参数");
        }
        //判断填报数量是否正确
        if (jsonParam.getEntryNumber() < (jsonParam.getApprovedNumber() + jsonParam.getRefuseNumber())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"批核加拒绝总数量不能大于进件数");
        }
        if (jsonParam.getApprovedNumber() < jsonParam.getValidNumber()){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"有效数不能大于批核数");
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //如果角色不是员工或者不是销售岗位
        if (!sysPersonnel.getPositionPost().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非业务岗人员不可提交");
        }
        //判断是否为员工
        if (!sysPersonnel.getRoleId().equals(manage5) && !sysPersonnel.getRoleId().equals(roleId)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非员工主管不可提交");
        }
        //查询是否有该卡种
        CardType cardType = iCardTypeMapper.selectById(jsonParam.getCardTypeId());
        if (ObjectUtils.isEmpty(cardType)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该卡种不存在");
        }
        //查询当前是否有提交的数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(report_time) = 0");
        wrapper.eq("card_type_id",jsonParam.getCardTypeId());
        wrapper.ne("approver_state","refuse");
        List<PerformanceReport> selectList = iPerformanceReportMapper.selectList(wrapper);
        if (selectList.size() > 0){
            PerformanceReport performanceReport = selectList.get(0);
            return new ReturnEntity(CodeEntity.CODE_ERROR,performanceReport,"当天已提交过" + cardType.getName() + "数据");
        }
        //查询当前个人的信息
        if (!returnEntity.getState()){
            //如果查不到人员信息
            if (ObjectUtils.isEmpty(sysPersonnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"人员信息不存在");
            }
            //将人员资源代码加入进去
            jsonParam.setPersonnelCode(sysPersonnel.getPersonnelCode());
            //将当前所属项目加入
            if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
                wrapper = new QueryWrapper();
                wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
                List<ManagementPersonnel> list = iManagementPersonnelMapper.selectList(wrapper);
                if (list.size() < 1){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目组");
                }
                ManagementPersonnel managementPersonnel = list.get(0);
                SysManagement management = iSysManagementMapper.selectById(managementPersonnel.getManagementId());
                if (!management.getManagementState().equals(1)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
                }
                jsonParam.setManagementId(managementPersonnel.getManagementId());
            }
            //查询该项目主管
            Map map = new HashMap();
            map.put("managementId",jsonParam.getManagementId());
            map.put("roleId",roleId);
            map.put("employmentStatus","1");
            List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
            if (sysPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目无人审核，无法提交");
            }
            SysPersonnel personnel = sysPersonnels.get(0);
            //添加审核人编码
            jsonParam.setApproverPersonnelId(personnel.getId());
            jsonParam.setSysPersonnel(personnel);
            //设置该条数据唯一编码
            jsonParam.setReportCoding("coding" + System.currentTimeMillis() + PanXiaoZhang.ran(2));
        }
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms());
        //添加当前报告时间
        jsonParam.setReportTime(format);
        //添加修改时间
        jsonParam.setUpdateTime(format);
        //添加默认状态
        jsonParam.setApproverState("pending");
        //没有任何问题将数据录入进数据库
        int insert = iPerformanceReportMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        ReturnEntity entity = PanXiaoZhang.postWechatFer(
                jsonParam.getSysPersonnel().getOpenId(),
                "",
                "",
                sysPersonnel.getName() + ":提交业绩信息,请前往审核",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlPerformance
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"上报成功");
    }
    //查询提交的表单
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        Object startTime = map.get("startTime");
        if (!ObjectUtils.isEmpty(startTime)){
            map.put("startTime",startTime + " 00:00:00");
        }
        Object endTime = map.get("endTime");
        if (!ObjectUtils.isEmpty(endTime)){
            map.put("endTime",endTime + " 23:59:59");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, iPerformanceReportMapper.queryAll(map),"");
    }

    public static void main(String[] args) {
        ReturnEntity entity = PanXiaoZhang.postWechat(
                "15297599442",
                "提交信息",
                "",
                "请前往审核",
                "",
                "/pages/activities/show/show?from=zn&redirect_url="
        );
        System.out.println(entity);
    }
}
