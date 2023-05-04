package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.DispatchApplicationManagement;
import com.example.manage.entity.ManageDimission;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.SysPersonnelNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
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
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

@Slf4j
@Service
public class WhiteSysPersonnelServiceImpl implements IWhiteSysPersonnelService {

    @Value("${phone.birthday}")
    private String birthdayPhone;

    @Value("${phone.personnel}")
    private String personnelPhone;

    @Value("${role.manage5}")
    private Integer manage5;

    @Value("${role.manage}")
    private Integer manage;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private IDispatchApplicationManagementMapper iDispatchApplicationManagementMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
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
            if (name.equals("add")){
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
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }
    //添加员工所属项目组员工
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysPersonnelNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询当前填写信息的人
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        if (!sysPersonnel.getRoleId().equals(manage)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非主管不可添加人员");
        }
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询主管所属项目
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请联系后台人员关联自己的项目");
        }
        //查询资源代码
        if (true){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("personnel_code",jsonParam.getPersonnelCode());
            SysPersonnel personnel = iSysPersonnelMapper.selectOne(queryWrapper);
            if (!ObjectUtils.isEmpty(personnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该资源代码已绑定人员");
            }
        }
        //查询账号
        if (true){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("username",jsonParam.getPhone());
            SysPersonnel personnel = iSysPersonnelMapper.selectOne(queryWrapper);
            if (!ObjectUtils.isEmpty(personnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该账号已存在");
            }
        }
        ManagementPersonnel managementPersonnel = selectList.get(0);
        //设置员工账号
        jsonParam.setUsername(jsonParam.getPhone());
        //设置员工职位
        jsonParam.setRoleId(manage5);
        //密码加密
        jsonParam.setPassword(PanXiaoZhang.getPassword(jsonParam.getPassword()));
        //设置员工任职状态
        jsonParam.setEmploymentStatus(2);
        //设置员工所属项目
        int insert = iManagementPersonnelMapper.insert(new ManagementPersonnel(
                managementPersonnel.getManagementId(),
                jsonParam.getPersonnelCode()
        ));
        if (insert != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目关联失败");
        }
        int insertPersonnel = iSysPersonnelMapper.insert(jsonParam);
        if (insertPersonnel != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"员工录入失败");
        }
        PanXiaoZhang.postWechat(
                personnelPhone,
                "入职提醒",
                "",
                jsonParam.getName() + "提交了入职申请,请前往后台审核",
                "",
                ""
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"录入成功");
    }

    @Override
    public List<SysPersonnel> myLeader(Integer roleId, Integer managementId) {
        Map map = new HashMap();
        map.put("managementId",managementId);
        map.put("roleId",roleId);
        map.put("employmentStatus","1");
        return whiteSysPersonnelMapper.queryAll(map);
    }

    @Override
    public List<SysPersonnel> queryAll(Map map) {
        return iSysPersonnelMapper.queryAll(map);
    }

    /**
     * 生日查询通知
     */
    @Override
    public void birthdayInform() {
        //获取Redis设置的天数
        Map<Object, Object> dateFormatBirthday = redisUtil.getHashEntries("dateFormatBirthday");
        //将存储在Redis里的Map存储的值取出并转化为数字类型
        Integer birthday = Integer.valueOf(String.valueOf(dateFormatBirthday.get("dateFormatBirthday")));
        //获取当前时间
        Date date = new Date();
        //进行计算n天后的日期
        Date calculationDate = PanXiaoZhang.calculationDate(date, birthday);
        //进行转化为响应的日期格式
        String format = DateFormatUtils.format(calculationDate, "MM-dd");
        log.info("日期：{}",format);
        Map map = new HashMap();
        map.put("dateFormatBirthday","start");
        map.put("agoBirthday", format);
        map.put("backBirthday",format);
        if (true){
            //进行查询的出符合条件的数据
            List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
            for (int i = 0; i < sysPersonnels.size(); i++) {
                SysPersonnel sysPersonnel = sysPersonnels.get(i);
                System.out.println(sysPersonnel.getBirthday() + "=======================");
                Integer ageYTime = PanXiaoZhang.ageYTime(new Date().toInstant().atOffset(ZoneOffset.UTC).toLocalDate(), sysPersonnel.getBirthday().toInstant().atOffset(ZoneOffset.UTC).toLocalDate());
                System.out.println(ageYTime);
                PanXiaoZhang.postWechat(
                        birthdayPhone,
                        "生日提醒",
                        "",
                        sysPersonnel.getName() + ageYTime + "岁的生日时间是" + DateFormatUtils.format(calculationDate,PanXiaoZhang.yMd()),
                        "",
                        ""
                );
            }
        }
        map.put("dateFormatBirthday","entryTime");
        if (true){
            //进行查询的出符合条件的数据
            List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
            for (int i = 0; i < sysPersonnels.size(); i++) {
                SysPersonnel sysPersonnel = sysPersonnels.get(i);
                PanXiaoZhang.postWechat(
                        birthdayPhone,
                        "入职周年庆提醒",
                        "",
                        sysPersonnel.getName() + "入职第" +
                                PanXiaoZhang.ageYTime(new Date().toInstant().atOffset(ZoneOffset.UTC).toLocalDate() ,sysPersonnel.getEntryTime().toInstant().atOffset(ZoneOffset.UTC).toLocalDate()) + "年，" + DateFormatUtils.format(calculationDate,PanXiaoZhang.yMd()),
                        "",
                        ""
                );
            }
        }
    }

    /**
     * 离职操作
     */
    @Override
    public void dimissionInform() {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("applicant_state","pending");
            wrapper.apply(true,"DATE_FORMAT(NOW(),\"%Y-%m-%d\") >= DATE_FORMAT(resignation_time,\"%Y-%m-%d\")");
            List<ManageDimission> list = iManageDimissionMapper.selectList(wrapper);
            for (int i = 0; i < list.size(); i++) {
                QueryWrapper queryWrapper = new QueryWrapper();
                ManageDimission manageDimission = list.get(i);
                queryWrapper.eq("personnel_code",manageDimission.getPersonnelCode());
                iSysPersonnelMapper.update(new SysPersonnel(
                        0,
                        manageDimission.getResignationTime()
                ),queryWrapper);
                iManageDimissionMapper.updateById(new ManageDimission(
                        manageDimission.getId(),
                        "agree"
                ));
            }
        }catch (Exception e){
            log.info("捕获异常：{}",e.getMessage());
        }

        try {
            //获取Redis设置的天数
            Map<Object, Object> dateFormatBirthday = redisUtil.getHashEntries("dateFormatBirthday");
            //将存储在Redis里的Map存储的值取出并转化为数字类型
            Integer birthday = Integer.valueOf(String.valueOf(dateFormatBirthday.get("dateFormatDispatchApplication")));
            //获取当前时间
            Date date = new Date();
            //进行计算n天后的日期
            Date calculationDate = PanXiaoZhang.calculationDate(date, birthday);
            //进行转化为响应的日期格式
            String format = DateFormatUtils.format(calculationDate, "yyyy-MM-dd");
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("applicant_state","agree");
            wrapper.apply(true,"DATE_FORMAT('" + format + "',\"%Y-%m-%d\") = DATE_FORMAT(dispathch_time,\"%Y-%m-%d\")");
            List<DispatchApplicationManagement> selectList = iDispatchApplicationManagementMapper.selectList(wrapper);
            for (int i = 0; i < selectList.size(); i++) {
                DispatchApplicationManagement applicationManagement = selectList.get(i);
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("personnel_code",applicationManagement.getPersonnelCode());
                ManagementPersonnel managementPersonnel = iManagementPersonnelMapper.selectOne(queryWrapper);
                iManagementPersonnelMapper.updateById(new ManagementPersonnel(
                    managementPersonnel.getId(),
                    applicationManagement.getLaterManagementId(),
                    null
                ));
            }
        }catch (Exception e){
            log.info("捕获异常：{}",e.getMessage());
        }
    }

    //根据人员查询当下的卡种
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }
        SysPersonnel sysPersonnel = whiteSysPersonnelMapper.queryOne(map);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,sysPersonnel,"");
    }
}
