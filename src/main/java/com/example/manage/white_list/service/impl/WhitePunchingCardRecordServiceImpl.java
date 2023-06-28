package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.PunchingCardRecordNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.JqPoint;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.entity.*;
import com.example.manage.white_list.service.IWhitePunchingCardRecordService;
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
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/5
 */

@Slf4j
@Service
public class WhitePunchingCardRecordServiceImpl implements IWhitePunchingCardRecordService {

    @Value("${role.manage5}")
    private Integer manage5;

    @Value("${role.manage}")
    private Integer manage;

    @Resource
    private IPunchingCardRecordMapper iPunchingCardRecordMapper;

    @Resource
    private IWhitePunchingCardRecordMapper iWhitePunchingCardRecordMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private ICheckInTimeMapper iCheckInTimeMapper;

    @Resource
    private RedisUtil redisUtil;

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

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("area")){
                ReturnEntity area = area(request);
                return area;
            }else if (name.equals("cat_list")){
                return cat_list(request);
            }else if (name.equals("cat_day")){
                return cat_day(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }


    //查询指定日期的数据
    private ReturnEntity cat_day(HttpServletRequest request) throws IOException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        wrapper.eq("clocking_day_time",jsonParam.getClockingDayTime());
        PunchingCardRecord punchingCardRecord = iPunchingCardRecordMapper.selectOne(wrapper);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,punchingCardRecord,MsgEntity.CODE_SUCCEED);
    }

    //查看当前人员打卡历史记录
    private ReturnEntity cat_list(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        SysPersonnel personnel = iSysPersonnelMapper.selectById(String.valueOf(jsonMap.get("personnelId")));
        jsonMap.put("personnelCode",personnel.getPersonnelCode());
        List<PunchingCardRecord> punchingCardRecords = iWhitePunchingCardRecordMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,punchingCardRecords,"");
    }

    //查询是否在打卡范围
    private ReturnEntity area(HttpServletRequest request) throws IOException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断账户是否正常
        ReturnEntity entity = PanXiaoZhang.estimateState(personnel);
        if (entity.getState()){
            return entity;
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员未关联项目组");
        }
        boolean locationInRange = false;
        SysManagement management = new SysManagement();
        for (int i = 0; i < selectList.size(); i++) {
            ManagementPersonnel managementPersonnel = selectList.get(i);
            management =  iSysManagementMapper.selectById(managementPersonnel.getManagementId());
            if (management.getManagementState().equals(1)) {
                //获取项目打卡坐标
                String[] splitSouthLatitude = management.getSouthLatitude().replaceAll("，", ",").split(",");
                String[] splitNorthernLatitude = management.getNorthernLatitude().replaceAll("，", ",").split(",");
                String[] splitEastLongitude = management.getEastLongitude().replaceAll("，", ",").split(",");
                String[] splitWestLongitude = management.getWestLongitude().replaceAll("，", ",").split(",");
                //判断项目的坐标是否有误
                if (
                        splitEastLongitude.length < 2 ||
                                splitNorthernLatitude.length < 2 ||
                                splitSouthLatitude.length < 2 ||
                                splitWestLongitude.length < 2
                ) {
                    return new ReturnEntity(CodeEntity.CODE_ERROR, "项目坐标有误,请联系管理员");
                }
                List<JqPoint> ps = new ArrayList<>();
                JqPoint jqPoint1 = new JqPoint(PanXiaoZhang.stringDouble(splitEastLongitude[0]), PanXiaoZhang.stringDouble(splitEastLongitude[1]));
                JqPoint jqPoint2 = new JqPoint(PanXiaoZhang.stringDouble(splitSouthLatitude[0]), PanXiaoZhang.stringDouble(splitSouthLatitude[1]));
                JqPoint jqPoint3 = new JqPoint(PanXiaoZhang.stringDouble(splitNorthernLatitude[0]), PanXiaoZhang.stringDouble(splitNorthernLatitude[1]));
                JqPoint jqPoint4 = new JqPoint(PanXiaoZhang.stringDouble(splitWestLongitude[0]), PanXiaoZhang.stringDouble(splitWestLongitude[1]));
                ps.add(jqPoint1);
                ps.add(jqPoint2);
                ps.add(jqPoint3);
                ps.add(jqPoint4);
                //判断是否在范围内
                locationInRange = PanXiaoZhang.isPtInPoly(jsonParam.getX(), jsonParam.getY(), ps);
                if (locationInRange) {
                    break;
                }
            }
        }
        UtilEntity utilEntity = new UtilEntity();
        //返回提示语
        String msg;
        if (locationInRange){
            msg = "您已进入打卡范围";
            utilEntity.setLocationInRange(locationInRange);
            utilEntity.setSysManagement(management);
        }else {
            msg = "未进入打卡范围";
            utilEntity.setLocationInRange(locationInRange);
        }
        log.info("打卡范围返回结果:{},打卡数据：{}",msg,jsonParam);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,utilEntity,msg);
    }

    //查询当天打卡记录
    private ReturnEntity cat(HttpServletRequest request) throws IOException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        ""
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断账户是否正常
        ReturnEntity entity = PanXiaoZhang.estimateState(personnel);
        if (entity.getState()){
            return entity;
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联所属项目");
        }
        PunchingCardRecord  punchingCardRecord = new PunchingCardRecord();
        if (!ObjectUtils.isEmpty(jsonParam.getClockingDayTime())){
            wrapper.eq("clocking_day_time", jsonParam.getClockingDayTime());
        }else {
            wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(clocking_day_time) = 0");
        }
        PunchingCardRecord selectOne = iPunchingCardRecordMapper.selectOne(wrapper);

        if (!ObjectUtils.isEmpty(selectOne)){
            punchingCardRecord = selectOne;
            CheckInTime checkInTime = iCheckInTimeMapper.selectById(punchingCardRecord.getCheckInTimeId());
            punchingCardRecord.setAcCheckInTime(checkInTime);
        }
        punchingCardRecord.setSysPersonnel(personnel);
        if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
            ManagementPersonnel managementPersonnel = selectList.get(0);
            jsonParam.setManagementId(managementPersonnel.getManagementId());
        }
        SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
        if (ObjectUtils.isEmpty(management)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目不存在");
        }
        punchingCardRecord.setManagement(management);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("management_id",management.getId());
        punchingCardRecord.setCheckInTimes(iCheckInTimeMapper.selectList(queryWrapper));
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,punchingCardRecord,"");
    }

    //进行打卡
    private ReturnEntity add(HttpServletRequest request) throws IOException, ParseException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //获取用户openID
        String token = request.getHeader("Http-X-User-Access-Token");
        Token parseObject = JSONObject.parseObject(PanXiaoZhang.postOpenId(token), Token.class);
        String openid = parseObject.getResponse().getOpenid();
        jsonParam.setOpenId(openid);

        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断员工是否符合打卡条件
        //if (!personnel.getRoleId().equals(manage5) && !personnel.getRoleId().equals(manage)){
        //    return new ReturnEntity(CodeEntity.CODE_ERROR,"该功能只针对员工或主管开放");
        //}
        //判断账户是否正常
        ReturnEntity entity = PanXiaoZhang.estimateState(personnel);
        if (entity.getState()){
            return entity;
        }

        //String s = personnel.getId() + "check_in";
        //Object o = redisUtil.get(s);
        //if (!ObjectUtils.isEmpty(o)){
        //    return new ReturnEntity(CodeEntity.CODE_ERROR,"请在" + redisUtil.getTime(s) + "秒后操作");
        //}
        //redisUtil.set(s,personnel.getPersonnelCode(),3);

        boolean locationInRange = false;
        //查询人员所在项目组
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员未关联项目组");
        }
        for (int i = 0; i < selectList.size(); i++) {
            ManagementPersonnel managementPersonnel = selectList.get(i);
            SysManagement management = iSysManagementMapper.selectById(managementPersonnel.getManagementId());
            if (management.getManagementState().equals(1)){
                //获取项目打卡坐标
                String[] splitSouthLatitude = management.getSouthLatitude().replaceAll("，",",").split(",");
                String[] splitNorthernLatitude = management.getNorthernLatitude().replaceAll("，",",").split(",");
                String[] splitEastLongitude = management.getEastLongitude().replaceAll("，",",").split(",");
                String[] splitWestLongitude = management.getWestLongitude().replaceAll("，",",").split(",");
                //判断项目的坐标是否有误
                if (
                        splitEastLongitude.length < 2 ||
                        splitNorthernLatitude.length < 2 ||
                        splitSouthLatitude.length < 2 ||
                        splitWestLongitude.length < 2
                ){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"项目坐标有误");
                }
                //获取当前用户的地址
                List<JqPoint> ps = new ArrayList<>();
                JqPoint jqPoint1 = new JqPoint(PanXiaoZhang.stringDouble(splitEastLongitude[0]),PanXiaoZhang.stringDouble(splitEastLongitude[1]));
                JqPoint jqPoint2 = new JqPoint(PanXiaoZhang.stringDouble(splitSouthLatitude[0]),PanXiaoZhang.stringDouble(splitSouthLatitude[1]));
                JqPoint jqPoint3 = new JqPoint(PanXiaoZhang.stringDouble(splitNorthernLatitude[0]),PanXiaoZhang.stringDouble(splitNorthernLatitude[1]));
                JqPoint jqPoint4 = new JqPoint(PanXiaoZhang.stringDouble(splitWestLongitude[0]),PanXiaoZhang.stringDouble(splitWestLongitude[1]));
                ps.add(jqPoint1);
                ps.add(jqPoint2);
                ps.add(jqPoint3);
                ps.add(jqPoint4);
                //判断是否在范围内
                locationInRange = PanXiaoZhang.isPtInPoly(jsonParam.getX(), jsonParam.getY(), ps);
                if (locationInRange){
                    jsonParam.setManagement(management);
                    break;
                }
            }
        }

        if (!locationInRange){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"不在服务范围内，打卡失败");
        }
        //判断当前是上班打卡还是下班打卡
        wrapper = new QueryWrapper();
        wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(clocking_day_time) = 0");
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        PunchingCardRecord punchingCardRecord = iPunchingCardRecordMapper.selectOne(wrapper);
        //获取当前时分秒
        LocalTime localTime = LocalTime.now();
        //获取当前时间
        Date date = new Date();
        String format = DateFormatUtils.format(date, PanXiaoZhang.Hms());
        Integer checkInTimeId = 0;//打卡类型的id
        String startPunchIn = "";//上班打卡时间开始时间
        String endPunchIn = "";//上班打卡时间结束时间
        String startClockOut = "";//下班打卡时间开始时间
        String endClockOut = "";//下班打卡时间结束时间
        String checkInTimeName = "";//打卡项目
        //查询打卡时间
        wrapper = new QueryWrapper();
        wrapper.eq("id",jsonParam.getCheckInId());
        wrapper.eq("management_id",jsonParam.getManagement().getId());
        CheckInTime checkInTime = iCheckInTimeMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(checkInTime)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"未设置该类型的打卡时间");
        }
        startPunchIn = checkInTime.getStartPunchIn();
        startClockOut = checkInTime.getStartClockOut();
        endClockOut = checkInTime.getEndClockOut();
        checkInTimeId = checkInTime.getId();
        checkInTimeName = checkInTime.getName();
        endPunchIn = checkInTime.getEndPunchIn();
        if (ObjectUtils.isEmpty(punchingCardRecord)){//如果不存在则判定为上班打卡
            //不让他打卡
            int time = PanXiaoZhang.compareTime(PanXiaoZhang.dateLocalTime(startPunchIn), localTime);
            if (time == 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"未到上班打卡时间，无法打卡");
            }
            //如果 time1 在 time2 之前，返回-1；如果 time1 在 time2 之后，返回1；如果 time1 和 time2 相等，返回0。
            int compareTime = PanXiaoZhang.compareTime(localTime, PanXiaoZhang.dateLocalTime(endPunchIn));
            //设置当前状态
            String workingClockInState = "打卡成功";
            if (compareTime > 0){
                workingClockInState = "迟到";
            }
            PunchingCardRecord cardRecord = new PunchingCardRecord(
                    null,
                    personnel.getName(),
                    personnel.getPersonnelCode(),
                    null,
                    jsonParam.getManagement().getId(),
                    personnel.getOpenId(),
                    jsonParam.getOpenId(),
                    workingClockInState,
                    format,
                    null,
                    null,
                    null,
                    null,
                    DateFormatUtils.format(date, PanXiaoZhang.yMd()),
                    endPunchIn,
                    startClockOut,
                    checkInTimeId,
                    checkInTimeName
            );
            int insert = iPunchingCardRecordMapper.insert(cardRecord);
            if (insert != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        MsgEntity.CODE_ERROR
                );
            }
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "打卡信息",
                    "",
                    personnel.getName() + ":上班打卡时间" + format + "，状态:" + workingClockInState +"",
                    "",
                    ""
            );
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,workingClockInState);
        }else {//如果存在则判定为下班打卡
            //不让他打卡
            int time = PanXiaoZhang.compareTime(localTime, PanXiaoZhang.dateLocalTime(endClockOut));
            if (time == 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"超出下班打卡时间，无法打卡");
            }
            //如果 time1 在 time2 之前，返回-1；如果 time1 在 time2 之后，返回1；如果 time1 和 time2 相等，返回0。
            int compareTime = PanXiaoZhang.compareTime(PanXiaoZhang.dateLocalTime(startClockOut), localTime);
            //设置当前状态
            String workingClockInState = "打卡成功";
            if (compareTime > 0){
                workingClockInState = "早退";
            }
            //进行下班打卡
            PunchingCardRecord cardRecord = new PunchingCardRecord(
                    punchingCardRecord.getId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    personnel.getOpenId(),
                    jsonParam.getOpenId(),
                    workingClockInState,
                    format,
                    null,
                    null,
                    jsonParam.getManagement().getStartClockOut(),
                    null,
                    null
            );
            int insert = iPunchingCardRecordMapper.updateById(cardRecord);
            if (insert != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        MsgEntity.CODE_ERROR
                );
            }
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "打卡信息",
                    "",
                    personnel.getName() + ":下班打卡时间" + format + "，状态:" + workingClockInState +"",
                    "",
                    ""
            );
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,workingClockInState);
        }
    }

    private ReturnEntity edit(HttpServletRequest request) {
        return null;
    }
}
