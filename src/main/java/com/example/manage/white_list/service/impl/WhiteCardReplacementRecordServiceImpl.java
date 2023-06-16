package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.CardReplacementRecordNotNull;
import com.example.manage.entity.is_not_null.CardReplacementReimbursementNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.white_list.service.IWhiteCardReplacementRecordService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-05-16 09:47:03
 * 补卡
 */


@Slf4j
@Service
public class WhiteCardReplacementRecordServiceImpl implements IWhiteCardReplacementRecordService {

    @Value("${url.repair_check}")
    private String repairCheck;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Value("${card.number}")
    private Integer cardNumber;

    @Resource
    private ICardReplacementRecordMapper iCardReplacementRecordMapper;

    @Resource
    private ICardReplacementReimbursementMapper iCardReplacementReimbursementMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private WhiteCardReplacementRecordMapper whiteCardReplacementRecordMapper;

    @Resource
    private WhiteCardReplacementReimbursementMapper whiteCardReplacementReimbursementMapper;

    @Resource
    private IPunchingCardRecordMapper iPunchingCardRecordMapper;

    @Resource
    private ICheckInTimeMapper iCheckInTimeMapper;

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
            }else if (name.equals("cat_past_records")){
                return cat_past_records(request);
            }else if (name.equals("cat_collate_past_records")){
                return cat_collate_past_records(request);
            }else if (name.equals("cat_number")){
                return cat_number(request);
            }else if (name.equals("cat_day")){
                return cat_day(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //查询当天补卡情况
    private ReturnEntity cat_day(HttpServletRequest request) throws IOException {
        CardReplacementRecord jsonParam = PanXiaoZhang.getJSONParam(request, CardReplacementRecord.class);
        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",jsonParam.getPersonnelId());
        wrapper.eq("clocking_day_time",DateFormatUtils.format(jsonParam.getReissueTime(), PanXiaoZhang.yMd()));
        wrapper.ne("reissue_state","refuse");
        PunchingCardRecord punchingCardRecord = iPunchingCardRecordMapper.selectOne(wrapper);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,punchingCardRecord,MsgEntity.CODE_SUCCEED);
    }

    //查询补卡还剩次数
    private ReturnEntity cat_number(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(jsonMap.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        //查询是否有当天的打卡记录
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("reissue_time", LocalDate.now().withDayOfMonth(1), LocalDate.now());
        wrapper.eq("personnel_id",jsonMap.get("personnelId"));
        List<CardReplacementRecord> recordList = iCardReplacementRecordMapper.selectList(wrapper);
        int i = cardNumber - recordList.size();
        if (i < 1){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,i,"本月补卡次数为" + i);
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,i,"");
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
        List<CardReplacementRecord> cardReplacementRecords = whiteCardReplacementRecordMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,cardReplacementRecords,"");
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

    //查询历史提交的数据
    private ReturnEntity cat_past_records(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(jsonMap.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        List<CardReplacementRecord> cardReplacementRecords = whiteCardReplacementRecordMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,cardReplacementRecords,"");
    }

    // 修改补卡
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        CardReplacementRecord jsonParam = PanXiaoZhang.getJSONParam(request, CardReplacementRecord.class);
        CardReplacementReimbursement cardReplacementReimbursement = new CardReplacementReimbursement(
                jsonParam.getId(),
                jsonParam.getPersonnelId(),
                jsonParam.getVerifierRemark(),
                jsonParam.getReissueState()
        );
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                cardReplacementReimbursement,
                new CardReplacementReimbursementNotNull(
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
        CardReplacementRecord cardReplacementRecord = iCardReplacementRecordMapper.selectById(jsonParam.getId());
        //判断数据是否存在
        if (ObjectUtils.isEmpty(cardReplacementRecord)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据不存在");
        }
        //判断该数据总状态
        if (!cardReplacementRecord.getReissueState().equals("pending")){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该状态下不可审核");
        }
        //当前时间
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMd());
        //至此拦截机制结束
        if (!ObjectUtils.isEmpty(sysPersonnel)){
            //查询该数据
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_id",sysPersonnel.getId());
            wrapper.eq("reissue_state","pending");
            wrapper.eq("reissue_code",cardReplacementRecord.getReissueCode());
            CardReplacementReimbursement reimbursement = iCardReplacementReimbursementMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(reimbursement)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据已审核过");
            }
            int updateById = iCardReplacementReimbursementMapper.updateById(new CardReplacementReimbursement(
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
            map.put("reissueCode",cardReplacementRecord.getReissueCode());
            map.put("nePersonnelId",jsonParam.getPersonnelId());
            map.put("reissueState","pending");
            List<CardReplacementReimbursement> reimbursements = whiteCardReplacementReimbursementMapper.queryAll(map);
            if (reimbursements.size() > 0){
                Integer integer = whiteCardReplacementReimbursementMapper.queryMax(map);
                if (!integer.equals(cardReplacementRecord.getMaxNumber())){//如果得出的流转等级不一致将进行修改
                    int updateById = iCardReplacementRecordMapper.updateById(new CardReplacementRecord(
                            cardReplacementRecord.getId(),
                            integer
                    ));
                    if (updateById != 1){
                        return new ReturnEntity(CodeEntity.CODE_ERROR,"修改流转等级失败");
                    }
                    //进行消息通知流转下一级
                    for (int i = 0; i < reimbursements.size(); i++) {
                        CardReplacementReimbursement cardReplacementReimbursement1 = reimbursements.get(i);
                        if (cardReplacementReimbursement1.getNumber().equals(integer)){
                            SysPersonnel selectById = iSysPersonnelMapper.selectById(cardReplacementReimbursement1.getPersonnelId());
                            //告知审核人前往审核
                            PanXiaoZhang.postWechatFer(
                                    selectById.getOpenId(),
                                    "补卡申请",
                                    "",
                                    sysPersonnel.getName() + "提交了" + DateFormatUtils.format(cardReplacementRecord.getReissueTime(),PanXiaoZhang.yMd()) + "的补卡申请",
                                    "",
                                    urlTransfer + "?from=zn&redirect_url=" + repairCheck + "?fromDispatchVerify=true"
                            );
                        }
                    }
                }
                return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
            }
            //获取补卡类型
            String reissueType = cardReplacementRecord.getReissueType();
            if (reissueType.equals("3")){
                CheckInTime checkInTime = iCheckInTimeMapper.selectById(cardReplacementRecord.getCheckInTimeId());
                if (ObjectUtils.isEmpty(checkInTime)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"当前打卡规则不存在");
                }
                SysPersonnel personnel = iSysPersonnelMapper.selectById(cardReplacementRecord.getPersonnelId());
                if (ObjectUtils.isEmpty(personnel)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"申请人信息不存在");
                }
                PunchingCardRecord punchingCardRecord = new PunchingCardRecord(
                        null,
                        personnel.getName(),
                        personnel.getPersonnelCode(),
                        null,
                        cardReplacementRecord.getManagementId(),
                        "",
                        "",
                        "补卡",
                        checkInTime.getStartClockOut(),
                        null,
                        null,
                        null,
                        null,
                        DateFormatUtils.format(cardReplacementRecord.getReissueTime(),PanXiaoZhang.yMd()),
                        checkInTime.getStartClockOut(),
                        checkInTime.getStartClockOut(),
                        checkInTime.getId(),
                        checkInTime.getName()
                );
                int insert = iPunchingCardRecordMapper.insert(punchingCardRecord);
                if (insert != 1){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"添加进入记录失败");
                }
            }else{
                SysPersonnel personnel = iSysPersonnelMapper.selectById(cardReplacementRecord.getPersonnelId());
                if (ObjectUtils.isEmpty(personnel)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"申请人信息不存在");
                }
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("personnel_code",personnel.getPersonnelCode());
                wrapper.eq("clocking_day_time",DateFormatUtils.format(cardReplacementRecord.getReissueTime(),PanXiaoZhang.yMd()));
                PunchingCardRecord punchingCardRecord = iPunchingCardRecordMapper.selectOne(wrapper);
                if (ObjectUtils.isEmpty(punchingCardRecord)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"当前打卡规则不存在");
                }
                if (reissueType.equals("1")){
                    punchingCardRecord.setWorkingAttendanceTime(punchingCardRecord.getManagementEndTime());
                    punchingCardRecord.setWorkingClockInState("补卡");
                    punchingCardRecord.setWorkingAgoOpenId(personnel.getOpenId());
                    punchingCardRecord.setWorkingLaterOpenId(personnel.getOpenId());
                }else if (reissueType.equals("2")){
                    punchingCardRecord.setClosedAttendanceTime(punchingCardRecord.getManagementEndTime());
                    punchingCardRecord.setClosedClockInState("补卡");
                    punchingCardRecord.setClosedAgoOpenId(personnel.getOpenId());
                    punchingCardRecord.setClosedLaterOpenId(personnel.getOpenId());
                }
                int updateById = iPunchingCardRecordMapper.updateById(punchingCardRecord);
                if (updateById != 1){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"修改下班补卡失败");
                }
            }
            int updateById = iCardReplacementRecordMapper.updateById(new CardReplacementRecord(
                    cardReplacementRecord.getId(),
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
                    null
            ));
            if (updateById != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"修改数据失败");
            }
            PanXiaoZhang.postWechatFer(
                    sysPersonnel.getOpenId(),
                    "补卡申请",
                    "",
                    DateFormatUtils.format(cardReplacementRecord.getReissueTime(),PanXiaoZhang.yMd()) + "日的补卡申请通过了",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + repairCheck
            );
        }else if (jsonParam.getReissueState().equals("refuse")){//如果拒绝审核
            int updateById = iCardReplacementRecordMapper.updateById(new CardReplacementRecord(
                    cardReplacementRecord.getId(),
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
                    null
            ));
            if (updateById != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"修改数据失败");
            }
            String remark = sysPersonnel.getName() + "拒绝了您的补卡请求";
            if (!ObjectUtils.isEmpty(jsonParam.getVerifierRemark())){
                remark += "，拒绝原因是：" + jsonParam.getVerifierRemark();
            }
            PanXiaoZhang.postWechatFer(
                    sysPersonnel.getOpenId(),
                    "补卡申请",
                    "",
                    remark,
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + repairCheck
            );
        }else {
            return new ReturnEntity(CodeEntity.CODE_ERROR,"审核状态不正确");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
    }

    // 添加补卡
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        CardReplacementRecord jsonParam = PanXiaoZhang.getJSONParam(request, CardReplacementRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new CardReplacementRecordNotNull(
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        ""
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        if (!PanXiaoZhang.compareDate(jsonParam.getReissueTime())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,DateFormatUtils.format(jsonParam.getReissueTime(),PanXiaoZhang.yMd()) + "不可补卡");
        }
        //判断该状态是否进行提交数据
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            //查询关联的项目
            List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(wrapper);
            if (managementPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目组");
            }
            ManagementPersonnel managementPersonnel = managementPersonnels.get(0);
            jsonParam.setManagementId(managementPersonnel.getManagementId());
            wrapper = new QueryWrapper();
        }
        //查询项目
        SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
        if (ObjectUtils.isEmpty(management)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目不存在");
        }
        if (!management.getManagementState().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
        }
        //设置补卡类型1：补卡上班，2；补卡下班
        wrapper.between("reissue_time", LocalDate.now().withDayOfMonth(1), LocalDate.now());
        wrapper.eq("personnel_id",sysPersonnel.getId());
        List<CardReplacementRecord> recordList = iCardReplacementRecordMapper.selectList(wrapper);
        int number = cardNumber - recordList.size() ;
        if (number < 1){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"本月补卡次数为" + number);
        }
        //查询是否是否有相同的补卡
        wrapper = new QueryWrapper();
        if (ObjectUtils.isEmpty(jsonParam.getReissueType())){
            jsonParam.setReissueType("1");
        }
        if (jsonParam.getReissueType().equals("2")){
            wrapper.eq("reissue_type","2");
        }else {
            wrapper.in("reissue_type","1","3");
        }
        wrapper.eq("reissue_state","pending");
        wrapper.eq("personnel_id",sysPersonnel.getId());
        wrapper.apply("DATE_FORMAT(reissue_time,'%Y-%m-%d') = '" + DateFormatUtils.format(jsonParam.getReissueTime(),PanXiaoZhang.yMd()) + "'");
        if (iCardReplacementRecordMapper.selectList(wrapper).size() > 0){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"不可重复提交补卡信息");
        }
        //查询是否有当天的打卡记录
        wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        wrapper.apply("DATE_FORMAT(clocking_day_time,'%Y-%m-%d') = '" + DateFormatUtils.format(jsonParam.getReissueTime(),PanXiaoZhang.yMd()) + "'");
        PunchingCardRecord punchingCardRecord = iPunchingCardRecordMapper.selectOne(wrapper);
        //if (!ObjectUtils.isEmpty(punchingCardRecord) && !ObjectUtils.isEmpty(punchingCardRecord.getClosedClockInState())){
        //    return new ReturnEntity(CodeEntity.CODE_ERROR,DateFormatUtils.format(jsonParam.getReissueTime(), PanXiaoZhang.yMd()) + "已有打卡记录");
        //}
        //jsonParam.setReissueType("1");
        //if (!ObjectUtils.isEmpty(punchingCardRecord.getClosedClockInState())){
        //    jsonParam.setReissueType("2");
        //    //设置关联的打卡类型
        //    jsonParam.setCheckInTimeId(punchingCardRecord.getCheckInTimeId());
        //    //设置关联的打卡名称
        //    jsonParam.setCheckInTimeName(punchingCardRecord.getCheckInTimeName());
        //}else {
        //    CheckInTime checkInTime = iCheckInTimeMapper.selectById(jsonParam.getCheckInTimeId());
        //    if (ObjectUtils.isEmpty(checkInTime)){
        //        return new ReturnEntity(CodeEntity.CODE_ERROR,"当前打卡规则不存在");
        //    }
        //    jsonParam.setReissueType("1");
        //    //设置关联的打卡类型
        //    jsonParam.setCheckInTimeId(checkInTime.getId());
        //    //设置关联的打卡名称
        //    jsonParam.setCheckInTimeName(checkInTime.getName());
        //}
        if (ObjectUtils.isEmpty(punchingCardRecord)){//如果为空则默认上班补卡
            jsonParam.setReissueType("3");
            CheckInTime checkInTime = iCheckInTimeMapper.selectById(jsonParam.getCheckInTimeId());
            if (ObjectUtils.isEmpty(checkInTime)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前打卡规则不存在");
            }
            //设置关联的打卡类型
            jsonParam.setCheckInTimeId(checkInTime.getId());
            //设置关联的打卡名称
            jsonParam.setCheckInTimeName(checkInTime.getName());
        }else {
            //设置关联的打卡类型
            jsonParam.setCheckInTimeId(punchingCardRecord.getCheckInTimeId());
            //设置关联的打卡名称
            jsonParam.setCheckInTimeName(punchingCardRecord.getCheckInTimeName());
        }
        wrapper = new QueryWrapper(); // 重新创建一个新的 QueryWrapper 对象
        //生成编码
        String code = System.currentTimeMillis() + PanXiaoZhang.ran(4);
        //添加申请人
        jsonParam.setPersonnelName(sysPersonnel.getName());
        //添加项目名称
        jsonParam.setManagementName(management.getName());
        //设置初始状态
        jsonParam.setReissueState("pending");
        //设置编码
        jsonParam.setReissueCode(code);
        //从最大的开始审核
        jsonParam.setMaxNumber(0);
        //设置审核职位
        Integer[] integers = {1,3};
        //存储map
        Map<Integer, SysRole> mapRole = new HashMap();
        //存储通知的人
        Map<Integer, SysPersonnel> mapPersonnel = new HashMap();
        //查询职位名
        if (integers.length > 0){
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
                    int insert = iCardReplacementReimbursementMapper.insert(new CardReplacementReimbursement(
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
        //添加申请时间
        jsonParam.setApplicantTime(new Date());
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iCardReplacementRecordMapper.insert(jsonParam);
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
                    "补卡申请",
                    "",
                    sysPersonnel.getName() + "提交了" + DateFormatUtils.format(jsonParam.getReissueTime(),PanXiaoZhang.yMd()) + "补卡信息",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + repairCheck + "?fromDispatchVerify=true"
            );
        });
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,MsgEntity.CODE_SUCCEED);
    }

    public static void main(String[] args) {
        String s = "ahdgsajhgdj1234567454353";
        System.out.println(s.replaceAll("1234567.*",""));
    }
}

