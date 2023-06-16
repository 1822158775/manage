package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.PerformanceReportNotNull;
import com.example.manage.entity.is_not_null.ReadingRecordNotNull;
import com.example.manage.entity.ranking_list.RankingList;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteReadingRecordService;
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
 * @date 2023/6/1
 * 阅读记录
 */

@Slf4j
@Service
public class WhiteReadingRecordServiceImpl implements IWhiteReadingRecordService {

    @Value("${role.manage3}")
    private Integer manage3;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Value("${url.achievement_day_list}")
    private String urlPerformance;

    @Resource
    private IReadingRecordMapper iReadingRecordMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private WhiteRankingListMapper whiteRankingListMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("edit")){
                ReadingRecord jsonParam = PanXiaoZhang.getJSONParam(request, ReadingRecord.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    //事务总管
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("add")){
                ReadingRecord jsonParam = PanXiaoZhang.getJSONParam(request, ReadingRecord.class);
                ReturnEntity returnEntity = add(request,jsonParam);
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

    // 修改阅读记录
    private ReturnEntity edit(HttpServletRequest request, ReadingRecord jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ReadingRecordNotNull(
                        "isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                        ,""
                        ,""
                        ,"isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        ReadingRecord readingRecord = iReadingRecordMapper.selectById(jsonParam.getId());
        if (!readingRecord.getReadingState().equals("待阅读")){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"已阅读该内容");
        }
        int updateById = iReadingRecordMapper.updateById(new ReadingRecord(
            jsonParam.getId(),
            jsonParam.getReadingState()
        ));
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,MsgEntity.CODE_SUCCEED);
    }

    // 添加阅读记录
    private ReturnEntity add(HttpServletRequest request, ReadingRecord jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new ReadingRecordNotNull(
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        ""
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        QueryWrapper wrapper = new QueryWrapper();
        //该人员关联了几个项目
        List<Integer> managementNumber = new ArrayList<>();
        //如果没有项目id
        if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
            //将当前所属项目加入
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(wrapper);
            if (managementPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目");
            }
            for (int i = 0; i < managementPersonnels.size(); i++) {
                ManagementPersonnel managementPersonnel = managementPersonnels.get(i);
                managementNumber.add(managementPersonnel.getManagementId());
            }
        }else {
            managementNumber.add(jsonParam.getManagementId());
        }
        for (int i = 0; i < managementNumber.size(); i++) {
            Integer integer = managementNumber.get(i);
            jsonParam.setManagementId(integer);
            //如果没有选择日期
            if (ObjectUtils.isEmpty(jsonParam.getDayTime())){
                wrapper = new QueryWrapper();
                //时间
                jsonParam.setDayTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMd()));
                wrapper.apply("DATE_FORMAT(day_time,'%Y-%m-%d') = '" + jsonParam.getDayTime() + "'");
                wrapper.eq("management_id",jsonParam.getManagementId());
                ReadingRecord readingRecord = iReadingRecordMapper.selectOne(wrapper);
                if (!ObjectUtils.isEmpty(readingRecord)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"当日不可重复提交");
                }
            }
            //查询该项目主管
            Map map = new HashMap();
            map.put("managementId",jsonParam.getManagementId());
            map.put("roleId",manage3);
            map.put("employmentStatus","1");
            List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
            if (sysPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目无人审核，无法提交");
            }
            SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
            SysPersonnel personnel = sysPersonnels.get(0);
            //存储
            jsonParam.setPersonnelId(sysPersonnel.getId());
            //状态
            jsonParam.setReadingState("待阅读");
            //项目名称
            jsonParam.setManagementName(management.getName());
            //审核人
            jsonParam.setApproverPersonnelId(personnel.getId());
            //申请人名称
            jsonParam.setPersonnelName(sysPersonnel.getName());
            //审核人名称
            jsonParam.setApproverPersonnelName(personnel.getName());
            //提交时间
            jsonParam.setReportTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
            map.remove("managementId");
            map.put("inManagementId",new Integer[]{jsonParam.getManagementId()});
            map.put("thisStartTime",jsonParam.getDayTime() + " 00:00:00");
            map.put("thisEndTime",jsonParam.getDayTime() + " 23:59:59");
            List<RankingList> rankingLists = whiteRankingListMapper.queryAll(map);
            if (rankingLists.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,management.getName() + "项目异常");
            }
            RankingList rankingList = rankingLists.get(0);
            //进件
            jsonParam.setEntryNumber(rankingList.getCountNumber());
            //批核
            jsonParam.setApprovedNumber(rankingList.getApproved());
            //激活
            jsonParam.setValidNumber(rankingList.getActivation());
            //拒绝
            jsonParam.setRefuseNumber(rankingList.getRefuse());
            //将数据唯一标识设置为空，由系统生成
            jsonParam.setId(null);
            //没有任何问题将数据录入进数据库
            int insert = iReadingRecordMapper.insert(jsonParam);
            //如果返回值不能鱼1则判断失败
            if (insert != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        MsgEntity.CODE_ERROR
                );
            }
            ReturnEntity entity = PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "业绩阅读提醒",
                    "",
                    management.getName()+ "提交了业绩,请前往阅读",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlPerformance
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }
        map.put("approverPersonnelId",map.get("personnelId"));
        ReturnEntity returnEntity = new ReturnEntity(CodeEntity.CODE_SUCCEED, iReadingRecordMapper.queryAll(map), MsgEntity.CODE_SUCCEED);
        returnEntity.setCount(iReadingRecordMapper.queryCount(map));
        return returnEntity;
    }
}
