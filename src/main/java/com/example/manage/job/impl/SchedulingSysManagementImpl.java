package com.example.manage.job.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.job.SchedulingSysManagementService;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/22
 */

@Service
@Slf4j
public class SchedulingSysManagementImpl implements SchedulingSysManagementService {

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ICardTypeMapper iCardTypeMapper;

    @Resource
    private IPerformanceReportMapper iPerformanceReportMapper;

    @Resource
    private IManageCardTypeMapper iManageCardTypeMapper;

    @Resource
    private WhiteFurloughRecordMapper whiteFurloughRecordMapper;

    @Resource
    private WhiteCardReplacementRecordMapper whiteCardReplacementRecordMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private IDispatchApplicationManagementMapper iDispatchApplicationManagementMapper;

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    //请假
    @Value("${url.rest_list}")
    private String restList;

    //调派
    @Value("${url.dispatch}")
    private String urlDispatch;

    @Value("${url.transfer}")
    private String urlTransfer;

    //补卡
    @Value("${url.repair_check}")
    private String repairCheck;

    //入职审核
    @Value("${url.employer_list}")
    private String employerList;
    
    //离职列表
    @Value("${url.leave_job_list}")
    private String leaveJobList;

    //杨谨帆
    @Value("${phone.personnel}")
    private String personnelPhone;

    //郭东敏
    @Value("${phone.personnel4}")
    private String personnelPhone4;
    @Override
    public void windUpAnAccount() {
        //查询正在运行的项目
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("management_state","1");
        List<SysManagement> managements = iSysManagementMapper.selectList(wrapper);
        for (int i = 0; i < managements.size(); i++) {
            System.out.println("managements=========>" + managements.get(i));
        }
    }

    //请假任务
    @Override
    public void taskNotification() {
        // 查询请假未审核的消息
        Map map = new HashMap();
        map.put("reissueState","pending");
        map.put("applicantState","pending");
        // 请假map数据存储
        Map<Integer, Integer> furloughMap = new HashMap<>();
        List<FurloughRecord> furloughRecords = whiteFurloughRecordMapper.queryAll(map);
        for (int i = 0; i < furloughRecords.size(); i++) {
            FurloughRecord furloughRecord = furloughRecords.get(i);
            //获取审核人的集合
            List<FurloughReimbursement> furloughReimbursements = furloughRecord.getFurloughReimbursements();
            for (int j = 0; j < furloughReimbursements.size(); j++) {
                FurloughReimbursement furloughReimbursement = furloughReimbursements.get(j);
                if (furloughRecord.getMaxNumber().equals(furloughReimbursement.getNumber()) && furloughReimbursement.getReissueState().equals("pending")){
                    Integer integer = furloughMap.get(furloughReimbursement.getPersonnelId());
                    if (ObjectUtils.isEmpty(integer)){
                        furloughMap.put(furloughReimbursement.getPersonnelId(),1);
                    }else {
                        integer += 1;
                        furloughMap.put(furloughReimbursement.getPersonnelId(),integer);
                    }
                    //System.out.println("furloughReimbursement============>" + furloughReimbursement);
                }
            }
        }
        //进行遍历全部
        for(Map.Entry<Integer, Integer> entry:furloughMap.entrySet()){
            //已关联的
            //System.out.println(entry.getKey() + "===========" + entry.getValue());
            SysPersonnel personnel = iSysPersonnelMapper.selectById(entry.getKey());
            //System.out.println("personnel============" + personnel.getOpenId());

            String openId = "";
            if (!ObjectUtils.isEmpty(personnel.getOpenId())){
                openId = personnel.getOpenId();
            }
            //告知审核人前往审核
            PanXiaoZhang.postWechatFer(
                    openId,
                    "",
                    "",
                    personnel.getUsername() + "您有：" + entry.getValue() + "条请假待审核数据",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + restList + "?fromRestVerify=true"
            );
        }

        // 睡眠1分钟,60秒
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            log.info("异常捕获:{]",e.getMessage());
        }

        // 请假map数据存储
        furloughMap.clear();
        List<CardReplacementRecord> recordList = whiteCardReplacementRecordMapper.queryAll(map);
        for (int i = 0; i < recordList.size(); i++) {
            CardReplacementRecord cardReplacementRecord = recordList.get(i);
            //获取审核人的集合
            List<CardReplacementReimbursement> replacementReimbursements = cardReplacementRecord.getReplacementReimbursements();
            for (int j = 0; j < replacementReimbursements.size(); j++) {
                CardReplacementReimbursement reimbursement = replacementReimbursements.get(j);
                if (cardReplacementRecord.getMaxNumber().equals(reimbursement.getNumber()) && reimbursement.getReissueState().equals("pending")){
                    Integer integer = furloughMap.get(reimbursement.getPersonnelId());
                    if (ObjectUtils.isEmpty(integer)){
                        furloughMap.put(reimbursement.getPersonnelId(),1);
                    }else {
                        integer += 1;
                        furloughMap.put(reimbursement.getPersonnelId(),integer);
                    }
                    //System.out.println("furloughReimbursement============>" + furloughReimbursement);
                }
            }
        }
        //进行遍历全部
        for(Map.Entry<Integer, Integer> entry:furloughMap.entrySet()){
            //已关联的
            //System.out.println(entry.getKey() + "===========" + entry.getValue());
            SysPersonnel personnel = iSysPersonnelMapper.selectById(entry.getKey());
            //System.out.println("personnel============" + personnel.getOpenId());

            String openId = "";
            if (!ObjectUtils.isEmpty(personnel.getOpenId())){
                openId = personnel.getOpenId();
            }
            //告知审核人前往审核
            PanXiaoZhang.postWechatFer(
                    openId,
                    "",
                    "",
                    personnel.getUsername() + "您有：" + entry.getValue() + "条补卡待审核数据",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + repairCheck + "?fromRepairCheckverify=true"
            );
        }

        // 睡眠1分钟,60秒
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            log.info("异常捕获:{]",e.getMessage());
        }

        // 调派任务
        // 请假map数据存储
        furloughMap.clear();
        List<DispatchApplicationManagement> dispatchApplicationManagements = iDispatchApplicationManagementMapper.queryAll(map);
        for (int i = 0; i < dispatchApplicationManagements.size(); i++) {
            DispatchApplicationManagement applicationManagement = dispatchApplicationManagements.get(i);
            //获取审核人的集合
            List<DispatchApplicationReimbursement> dispatchApplicationReimbursements = applicationManagement.getDispatchApplicationReimbursements();
            for (int j = 0; j < dispatchApplicationReimbursements.size(); j++) {
                DispatchApplicationReimbursement reimbursement = dispatchApplicationReimbursements.get(j);
                if (applicationManagement.getMaxNumber().equals(reimbursement.getNumber()) && reimbursement.getVerifierState().equals("pending")){
                    Integer integer = furloughMap.get(reimbursement.getPersonnelId());
                    if (ObjectUtils.isEmpty(integer)){
                        furloughMap.put(reimbursement.getPersonnelId(),1);
                    }else {
                        integer += 1;
                        furloughMap.put(reimbursement.getPersonnelId(),integer);
                    }
                    //System.out.println("furloughReimbursement============>" + furloughReimbursement);
                }
            }
        }
        //进行遍历全部
        for(Map.Entry<Integer, Integer> entry:furloughMap.entrySet()){
            //已关联的
            //System.out.println(entry.getKey() + "===========" + entry.getValue());
            SysPersonnel personnel = iSysPersonnelMapper.selectById(entry.getKey());
            //System.out.println("personnel============" + personnel.getOpenId());
            String openId = "";
            if (!ObjectUtils.isEmpty(personnel.getOpenId())){
                openId = personnel.getOpenId();
            }
            //告知审核人前往审核
            PanXiaoZhang.postWechatFer(
                    openId,
                    "",
                    "",
                    personnel.getUsername() + "您有：" + entry.getValue() + "条调派待审核数据",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlDispatch + "?fromDispatchVerify=true"
            );
        }

        // 入职审核提醒
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("employment_status","2");
        List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.selectList(wrapper);
        for (int i = 0; i < sysPersonnels.size(); i++) {
            SysPersonnel personnel = sysPersonnels.get(i);
            wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",personnel.getPersonnelCode());
            List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(wrapper);
            for (int j = 0; j < managementPersonnels.size(); j++) {
                ManagementPersonnel managementPersonnel = managementPersonnels.get(j);
                if (j == 0){
                    SysManagement sysManagement = iSysManagementMapper.selectById(managementPersonnel.getManagementId());
                    wrapper = new QueryWrapper();
                    if (sysManagement.getId() == 1 || sysManagement.getId() == 2){
                        wrapper.eq("username",personnelPhone4);
                    }else {
                        wrapper.eq("username",personnelPhone);
                    }
                    SysPersonnel sysPersonnel = iSysPersonnelMapper.selectOne(wrapper);
                    PanXiaoZhang.postWechatFer(
                            sysPersonnel.getOpenId(),
                            "",
                            "",
                            sysManagement.getName() + ":" + sysManagement.getName() + "提交了入职申请",
                            "",
                            urlTransfer + "?from=zn&redirect_url=" + employerList + sysManagement.getId()
                    );
                    break;
                }
            }
        }

        // 离职提醒
        wrapper = new QueryWrapper();
        wrapper.eq("applicant_state","pending");
        List<ManageDimission> manageDimissions = iManageDimissionMapper.selectList(wrapper);
        wrapper = new QueryWrapper();
        for (int i = 0; i < manageDimissions.size(); i++) {
            ManageDimission manageDimission = manageDimissions.get(i);
            if (manageDimission.getManagementId() == 1 || manageDimission.getManagementId() == 2){
                wrapper.eq("username",personnelPhone4);
            }else {
                wrapper.eq("username",personnelPhone);
            }
            SysPersonnel sysPersonnel = iSysPersonnelMapper.selectOne(wrapper);
            SysManagement sysManagement = iSysManagementMapper.selectById(manageDimission.getManagementId());
            // 发送人事
            ReturnEntity entity = PanXiaoZhang.postWechatFer(
                    sysPersonnel.getOpenId(),
                    "",
                    "",
                    sysManagement.getName() + ":" + manageDimission.getApplicant() + "提交了离职申请",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + leaveJobList
            );
        }
    }

    //补卡任务
    @Override
    public void taskNotificationCardReplacement() {

        // 查询请假未审核的消息
        Map map = new HashMap();
        map.put("reissueState","pending");
        // 请假map数据存储
        Map<Integer, Integer> furloughMap = new HashMap<>();
        List<CardReplacementRecord> recordList = whiteCardReplacementRecordMapper.queryAll(map);
        for (int i = 0; i < recordList.size(); i++) {
            CardReplacementRecord cardReplacementRecord = recordList.get(i);
            //获取审核人的集合
            List<CardReplacementReimbursement> replacementReimbursements = cardReplacementRecord.getReplacementReimbursements();
            for (int j = 0; j < replacementReimbursements.size(); j++) {
                CardReplacementReimbursement reimbursement = replacementReimbursements.get(j);
                if (cardReplacementRecord.getMaxNumber().equals(reimbursement.getNumber()) && reimbursement.getReissueState().equals("pending")){
                    Integer integer = furloughMap.get(reimbursement.getPersonnelId());
                    if (ObjectUtils.isEmpty(integer)){
                        furloughMap.put(reimbursement.getPersonnelId(),1);
                    }else {
                        integer += 1;
                        furloughMap.put(reimbursement.getPersonnelId(),integer);
                    }
                    //System.out.println("furloughReimbursement============>" + furloughReimbursement);
                }
            }
        }
        //进行遍历全部
        for(Map.Entry<Integer, Integer> entry:furloughMap.entrySet()){
            //已关联的
            //System.out.println(entry.getKey() + "===========" + entry.getValue());
            SysPersonnel personnel = iSysPersonnelMapper.selectById(entry.getKey());
            //System.out.println("personnel============" + personnel.getOpenId());

            //告知审核人前往审核
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "",
                    "",
                    personnel.getUsername() + "您有：" + entry.getValue() + "条补卡待审核数据",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + repairCheck + "?fromRestVerify=true"
            );
        }
    }
}
