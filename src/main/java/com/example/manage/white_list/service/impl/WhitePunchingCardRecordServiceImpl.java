package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.PunchingCardRecordNotNull;
import com.example.manage.entity.number.ManagementPunching;
import com.example.manage.entity.number.ManagementPunchingNumber;
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
import java.util.*;

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

    @Resource
    private WhiteManagementPunching whiteManagementPunching;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private WhitePersonnelDetails whitePersonnelDetails;

    @Resource
    private ILoginRecordMapper loginRecordMapper;

    @Resource
    private IReimbursementImageMapper iReimbursementImageMapper;

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
            }else if (name.equals("video_check_in")){
                return video_check_in(request);
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }
    // 视频签到
    private ReturnEntity video_check_in(HttpServletRequest request) throws IOException, ParseException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
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
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //获取用户openID
        String token = request.getHeader("Http-X-User-Access-Token");
        Token parseObject = JSONObject.parseObject(PanXiaoZhang.postOpenId(token), Token.class);
        String openid = "未获取到用户的openId";
        if (!ObjectUtils.isEmpty(parseObject.getResponse())){
            openid = parseObject.getResponse().getOpenid();
        }
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

        String s = personnel.getId() + "check_in";
        Object o = redisUtil.get(s);
        if (!ObjectUtils.isEmpty(o)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请在" + redisUtil.getTime(s) + "秒后操作");
        }
        redisUtil.set(s,personnel.getPersonnelCode(),3);
        //查询人员所在项目组
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员未关联项目组");
        }
        SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
        //判断项目是否停止运营
        if (!management.getManagementState().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
        }
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
        boolean locationInRange = PanXiaoZhang.isPtInPoly(jsonParam.getX(), jsonParam.getY(), ps);

        if (locationInRange){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"在服务范围内，请前往定位打卡");
        }
        //存储项目信息
        jsonParam.setManagement(management);
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
        CheckInTime checkInTime = iCheckInTimeMapper.selectById(jsonParam.getCheckInId());
        if (ObjectUtils.isEmpty(checkInTime)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"未设置该类型的打卡时间");
        }
        startPunchIn = checkInTime.getStartPunchIn();
        startClockOut = checkInTime.getStartClockOut();
        endClockOut = checkInTime.getEndClockOut();
        checkInTimeId = checkInTime.getId();
        checkInTimeName = checkInTime.getName();
        endPunchIn = checkInTime.getEndPunchIn();

        wrapper = new QueryWrapper();
        // 设置条件，如当天日期的范围
        wrapper.apply("DATE(login_time) = CURDATE()");
        // 按照时间降序排序
        wrapper.orderByDesc("login_time");
        // 获取最大的一条数据
        wrapper.last("LIMIT 1");
        // 当前用户
        wrapper.eq("username",personnel.getUsername());
        LoginRecord loginRecord = loginRecordMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(loginRecord)){
            loginRecord = new LoginRecord();
        }
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
            //获取编码
            String uuid = PanXiaoZhang.getID();
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
                    checkInTimeName,
                    loginRecord.getLoginTime(),
                    null,
                    jsonParam.getModel(),
                    null,
                    uuid,
                    "上班视频签到",
                    null,
                    jsonParam.getRemark(),
                    null
            );
            int insert = iPunchingCardRecordMapper.insert(cardRecord);
            int insertImage = iReimbursementImageMapper.insert(new ReimbursementImage(
                    jsonParam.getVideoPath(),
                    uuid,
                    "上班签到"
            ));
            if (insert != 1 || insertImage != 1){
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
                    null,
                    null,
                    loginRecord.getLoginTime(),
                    null,
                    jsonParam.getModel(),
                    null,
                    null,
                    "下班视频收工",
                    null,
                    jsonParam.getRemark()
            );
            int insert = iPunchingCardRecordMapper.updateById(cardRecord);
            int insertImage = iReimbursementImageMapper.insert(new ReimbursementImage(
                    jsonParam.getVideoPath(),
                    punchingCardRecord.getPunchingCardRecordCode(),
                    "下班收工"
            ));
            if (insert != 1 || insertImage != 1){
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
                    personnel.getName() + ":下班打卡时间" + format + "，状态:" + workingClockInState +"",
                    "",
                    ""
            );
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,workingClockInState);
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
            }else if (name.equals("clocking_situation")){
                return clocking_situation(request);
            }else if (name.equals("clocking_situation_particulars")){
                return clocking_situation_particulars(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }
    
    //关联项目组打卡情况
    private ReturnEntity clocking_situation_particulars(HttpServletRequest request) throws IOException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
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
        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员不存在");
        }
        SysRole sysRole = iSysRoleMapper.selectById(sysPersonnel.getRoleId());
        if (sysRole.getLevelSorting() > 3){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"权限不足");
        }

        String thisStartTime = jsonParam.getStartTime();
        if (ObjectUtils.isEmpty(thisStartTime)){
            thisStartTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
        }

        String thisEndTime = jsonParam.getEndTime();
        if (ObjectUtils.isEmpty(thisEndTime)){
            thisEndTime = PanXiaoZhang.GetNextDay(String.valueOf(thisStartTime), 0);
        }

        Long dayTime = PanXiaoZhang.getDayTime(String.valueOf(thisStartTime), String.valueOf(thisEndTime));

        if (dayTime > 31){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"查询天数最多一个月");
        }

        if (dayTime < 0){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"开始时间不可以大于结束时间");
        }

        Map map = new HashMap();
        map.put("startTime",thisStartTime + " 00:00:00");
        map.put("endTime",thisEndTime + " 23:59:59");
        map.put("managementId",jsonParam.getManagementId());
        map.put("inRoleId",new Integer[]{1,5});
        map.put("sysPersonnelId",jsonParam.getSysPersonnelId());
        List<PunchingCardRecord> recordList = whitePersonnelDetails.queryAll(map);
        if (!ObjectUtils.isEmpty(jsonParam.getSysPersonnelId())){
            List<String> days = PanXiaoZhang.getDays(thisStartTime, thisEndTime);
            //存储数据内容
            Map<String, PunchingCardRecord> recordMap = new HashMap<>();
            for (int i = 0; i < recordList.size(); i++) {
                PunchingCardRecord punchingCardRecord = recordList.get(i);
                recordMap.put(punchingCardRecord.getClockingDayTime(),punchingCardRecord);
            }
            recordList.clear();
            for (int i = 0; i < days.size(); i++) {
                String s = days.get(i);
                PunchingCardRecord punchingCardRecord = recordMap.get(s);
                if (ObjectUtils.isEmpty(punchingCardRecord)){
                    PunchingCardRecord cardRecord = new PunchingCardRecord();
                    cardRecord.setClockingDayTime(s);
                    recordList.add(cardRecord);
                }else {
                    recordList.add(punchingCardRecord);
                }
            }
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, recordList,"");
    }

    //关联项目组打卡情况
    private ReturnEntity clocking_situation(HttpServletRequest request) throws IOException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员不存在");
        }
        SysRole sysRole = iSysRoleMapper.selectById(sysPersonnel.getRoleId());
        if (sysRole.getLevelSorting() > 3){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"权限不足");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);

        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (int i = 0; i < selectList.size(); i++) {
            integerArrayList.add(selectList.get(i).getManagementId());
        }

        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目组");
        }

        String thisStartTime = jsonParam.getStartTime();
        if (ObjectUtils.isEmpty(thisStartTime)){
            thisStartTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
        }

        String thisEndTime = jsonParam.getEndTime();
        if (ObjectUtils.isEmpty(thisEndTime)){
            thisEndTime = PanXiaoZhang.GetNextDay(String.valueOf(thisStartTime), 0);
        }

        Long dayTime = PanXiaoZhang.getDayTime(String.valueOf(thisStartTime), String.valueOf(thisEndTime));

        if (dayTime > 31){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"查询天数最多一个月");
        }

        if (dayTime < 0){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"开始时间不可以大于结束时间");
        }
        Map map = new HashMap();
        map.put("startTime",thisStartTime + " 00:00:00");
        map.put("endTime",thisEndTime + " 23:59:59");
        map.put("inManagementId",integerArrayList.toArray(new Integer[integerArrayList.size()]));
        map.put("inRoleId",new Integer[]{1,5});
        List<ManagementPunching> managementPunchings = whiteManagementPunching.queryAll(map);
        //存储打卡状态
        String[] stringList = {"迟到", "早退"};
        //存储相关数量
        Map<String, Integer> integerMap = new HashMap<>();
        //存储名称相关数据
        Map<String, String> stringMap = new HashMap<>();
        //设置采集的数据
        for (int i = 0; i < stringList.length; i++) {
            stringMap.put(stringList[i],stringList[i]);
        }
        //存储数据
        Map<String, ManagementPunchingNumber> punchingNumberMap = new HashMap<>();
        for (int i = 0; i < managementPunchings.size(); i++) {
            ManagementPunching managementPunching = managementPunchings.get(i);
            //名称key
            String nameKey = managementPunching.getId() + "-name";
            //存储项目名称
            String managementName = stringMap.get(nameKey);
            //判断是否存在
            if (ObjectUtils.isEmpty(managementName)){
                stringMap.put(nameKey,managementPunching.getName());
            }
            if (!managementPunching.getPunchInState().equals("上班未打卡") && managementPunching.getPunchInCount() > 0){
                //用户key
                String userId = managementPunching.getId() + "-" + managementPunching.getPersonnelId() + "-punchIn";
                //项目实际签到人数
                String punchInKey = managementPunching.getId() + "-punchIn";
                //存储出勤人数
                Integer integer = integerMap.get(userId);
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(userId,1);
                    integerMap.put(punchInKey,ObjectUtils.isEmpty(integerMap.get(punchInKey)) ? managementPunching.getPunchInCount() : (integerMap.get(punchInKey) + managementPunching.getPunchInCount()));
                }
            }
            //项目组人数
            if (managementPunching.getEmploymentStatus().equals(1)){
                //用户key
                String userId = managementPunching.getId() + "-" + managementPunching.getPersonnelId() + "-employment";
                //项目实际签到人数
                String punchInKey = managementPunching.getId() + "-employment";
                //存储出勤人数
                Integer integer = integerMap.get(userId);
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(userId,1);
                    integerMap.put(punchInKey,ObjectUtils.isEmpty(integerMap.get(punchInKey)) ? 1 : (integerMap.get(punchInKey) + 1));
                }
            }
            //请假数量
            if (managementPunching.getNumberOfLeave() > 0){
                //用户key
                String userId = managementPunching.getId() + "-" + managementPunching.getPersonnelId() + "-numberOfLeave";
                //项目请假人数
                String punchInKey = managementPunching.getId() + "-numberOfLeave";
                //存储出勤人数
                Integer integer = integerMap.get(userId);
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(userId,1);
                    integerMap.put(punchInKey,ObjectUtils.isEmpty(integerMap.get(punchInKey)) ? managementPunching.getNumberOfLeave() : (integerMap.get(punchInKey) + managementPunching.getNumberOfLeave()));
                }
            }
            //存储上下班打卡状态
            String[] strings = {managementPunching.getPunchInState(), managementPunching.getClockInState()};
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j];
                if (!ObjectUtils.isEmpty(stringMap.get(string))){
                    //生成key
                    String id = managementPunching.getId() + "-" + string;
                    //通过key获取值
                    ManagementPunchingNumber punchingNumber = punchingNumberMap.get(id);
                    //如果值不存在
                    if (ObjectUtils.isEmpty(punchingNumber)){
                        //存入数据Map中
                        punchingNumberMap.put(id,new ManagementPunchingNumber(
                                managementPunching.getId(),//存入项目id
                                string,//存入打卡状态
                                managementPunching.getPunchInCount()//存入打卡数量
                        ));
                    }else {//如果已存在
                        punchingNumber.setNumber(punchingNumber.getNumber() + managementPunching.getPunchInCount());
                        //存入数据Map中
                        punchingNumberMap.put(id,punchingNumber);
                    }
                }
            }
        }
        //查询项目
        wrapper = new QueryWrapper();
        wrapper.in("id",integerArrayList);
        wrapper.eq("management_state","1");
        List<SysManagement> sysManagementList = iSysManagementMapper.selectList(wrapper);
        //返回值
        List<ManagementPunching> managementPunchingList = new ArrayList<>();
        //进行循环遍历已关联项目
        for (int i = 0; i < sysManagementList.size(); i++) {
            Integer integer = sysManagementList.get(i).getId();
            List<ManagementPunchingNumber> punchingNumberList = new ArrayList<>();
            for (int j = 0; j < stringList.length; j++) {
                String str = stringList[j];
                ManagementPunchingNumber punchingNumber = punchingNumberMap.get(integer + "-" + str);
                if (!ObjectUtils.isEmpty(punchingNumber)){
                    punchingNumberList.add(punchingNumber);
                }else {
                    //存入数据Map中
                    punchingNumberList.add(new ManagementPunchingNumber(
                            integer,//存入项目id
                            str,//存入打卡状态
                            0//存入打卡数量
                    ));
                }
            }
            //名称key
            String nameKey = integer + "-name";
            //存储项目名称
            String managementName = stringMap.get(nameKey);
            //项目实际签到人数
            String punchInKey = integer + "-punchIn";
            //项目实际签到人数
            String employmentInKey = integer + "-employment";
            //项目请假人数
            String numberOfLeave = integer + "-numberOfLeave";

            managementPunchingList.add(new ManagementPunching(
                integer,
                managementName,
                ObjectUtils.isEmpty(integerMap.get(punchInKey)) ? 0 : integerMap.get(punchInKey),//存储出勤人数
                ObjectUtils.isEmpty(integerMap.get(employmentInKey)) ? 0 : integerMap.get(employmentInKey),//存储出勤人数
                ObjectUtils.isEmpty(integerMap.get(numberOfLeave)) ? 0 : integerMap.get(numberOfLeave),//请假人数
                punchingNumberList
            ));
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, managementPunchingList,"");
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
        }else {
            msg = "未进入打卡范围";
        }
        utilEntity.setLocationInRange(locationInRange);
        utilEntity.setSysManagement(management);
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

        //上班打卡时间
        if (!ObjectUtils.isEmpty(punchingCardRecord.getWorkingAttendanceTime())){
            punchingCardRecord.setWorkingAttendanceTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMd()) + " " + punchingCardRecord.getWorkingAttendanceTime());
        }

        //下班
        if (!ObjectUtils.isEmpty(punchingCardRecord.getClosedAttendanceTime())){
            punchingCardRecord.setClosedAttendanceTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMd()) + " " + punchingCardRecord.getClosedAttendanceTime());
        }

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
                        "isNotNullAndIsLengthNot0",
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
        String openid = "未获取到用户的openId";
        if (!ObjectUtils.isEmpty(parseObject.getResponse())){
            openid = parseObject.getResponse().getOpenid();
        }
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

        String s = personnel.getId() + "check_in";
        Object o = redisUtil.get(s);
        if (!ObjectUtils.isEmpty(o)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请在" + redisUtil.getTime(s) + "秒后操作");
        }
        redisUtil.set(s,personnel.getPersonnelCode(),3);
        //查询人员所在项目组
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员未关联项目组");
        }
        SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
        //判断项目是否停止运营
        if (!management.getManagementState().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
        }
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
        boolean locationInRange = PanXiaoZhang.isPtInPoly(jsonParam.getX(), jsonParam.getY(), ps);

        if (!locationInRange){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"不在服务范围内，打卡失败");
        }
        //存储项目信息
        jsonParam.setManagement(management);
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
        CheckInTime checkInTime = iCheckInTimeMapper.selectById(jsonParam.getCheckInId());
        if (ObjectUtils.isEmpty(checkInTime)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"未设置该类型的打卡时间");
        }
        startPunchIn = checkInTime.getStartPunchIn();
        startClockOut = checkInTime.getStartClockOut();
        endClockOut = checkInTime.getEndClockOut();
        checkInTimeId = checkInTime.getId();
        checkInTimeName = checkInTime.getName();
        endPunchIn = checkInTime.getEndPunchIn();

        wrapper = new QueryWrapper();
        // 设置条件，如当天日期的范围
        wrapper.apply("DATE(login_time) = CURDATE()");
        // 按照时间降序排序
        wrapper.orderByDesc("login_time");
        // 获取最大的一条数据
        wrapper.last("LIMIT 1");
        // 当前用户
        wrapper.eq("username",personnel.getUsername());
        LoginRecord loginRecord = loginRecordMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(loginRecord)){
            loginRecord = new LoginRecord();
        }
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
                    checkInTimeName,
                    loginRecord.getLoginTime(),
                    null,
                    jsonParam.getModel(),
                    null
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
                    "",
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
                    null,
                    null,
                    loginRecord.getLoginTime(),
                    null,
                    jsonParam.getModel()
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
                    "",
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
