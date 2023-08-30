package com.example.manage.job.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.job.SchedulingSysPersonnelService;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/22
 */

@Slf4j
@Service
public class SchedulingSysPersonnelServiceImpl implements SchedulingSysPersonnelService {


    @Value("${phone.birthday}")
    private String birthdayPhone;

    @Value("${role.manage3}")
    private Integer manage3;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private IDispatchApplicationManagementMapper iDispatchApplicationManagementMapper;

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
        map.put("employmentStatus",1);
        if (true){
            //进行查询的出符合条件的数据
            List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
            for (int i = 0; i < sysPersonnels.size(); i++) {
                SysPersonnel sysPersonnel = sysPersonnels.get(i);
                Integer ageYTime = PanXiaoZhang.ageYTime(new Date().toInstant().atOffset(ZoneOffset.UTC).toLocalDate(), sysPersonnel.getBirthday().toInstant().atOffset(ZoneOffset.UTC).toLocalDate());
                ReturnEntity entity = new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
                if (sysPersonnel.getSysManagement().size() > 0){
                    SysManagement management = sysPersonnel.getSysManagement().get(0);
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("managementId",management.getId());
                    hashMap.put("roleId",manage3);
                    List<SysPersonnel> personnels = iSysPersonnelMapper.queryAll(hashMap);
                    if (personnels.size() > 0){
                        SysPersonnel personnel = personnels.get(0);
                        if (management.getId().equals(1) || management.getId().equals(2)){
                            entity = PanXiaoZhang.postWechatFer(
                                    personnel.getOpenId(),
                                    "",
                                    "",
                                    "生日提醒," + sysPersonnel.getName() + ageYTime + "岁的生日时间是" + DateFormatUtils.format(calculationDate, PanXiaoZhang.yMd()),
                                    "",
                                    ""
                            );
                        }else {
                            QueryWrapper wrapper = new QueryWrapper();
                            wrapper.eq("username",birthdayPhone);
                            entity = PanXiaoZhang.postWechatFer(
                                    iSysPersonnelMapper.selectOne(wrapper).getOpenId(),
                                    "",
                                    "",
                                    "生日提醒," + sysPersonnel.getName() + ageYTime + "岁的生日时间是" + DateFormatUtils.format(calculationDate, PanXiaoZhang.yMd()),
                                    "",
                                    ""
                            );
                        }
                        log.info("发送给区域经理:{},发送结果:{}",personnel.getName(),entity);
                    }
                }
                if (!entity.getCode().equals("0")){
                    log.info("发送给区域经理失败，转发给:{}","煅哥");
                    PanXiaoZhang.postWechat(
                            birthdayPhone,
                            "",
                            "",
                            "生日提醒," + sysPersonnel.getName() + ageYTime + "岁的生日时间是" + DateFormatUtils.format(calculationDate,PanXiaoZhang.yMd()),
                            "",
                            ""
                    );
                }
            }
        }
        map.put("dateFormatBirthday","entryTime");
        if (true){
            //进行查询的出符合条件的数据
            List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
            for (int i = 0; i < sysPersonnels.size(); i++) {
                SysPersonnel sysPersonnel = sysPersonnels.get(i);
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("username",birthdayPhone);
                PanXiaoZhang.postWechatFer(
                        iSysPersonnelMapper.selectOne(wrapper).getOpenId(),
                        "",
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
        //try {
        //    QueryWrapper wrapper = new QueryWrapper();
        //    wrapper.eq("applicant_state","pending");
        //    wrapper.apply(true,"DATE_FORMAT(NOW(),\"%Y-%m-%d\") >= DATE_FORMAT(resignation_time,\"%Y-%m-%d\")");
        //    List<ManageDimission> list = iManageDimissionMapper.selectList(wrapper);
        //    for (int i = 0; i < list.size(); i++) {
        //        QueryWrapper queryWrapper = new QueryWrapper();
        //        ManageDimission manageDimission = list.get(i);
        //        queryWrapper.eq("personnel_code",manageDimission.getPersonnelCode());
        //        iSysPersonnelMapper.update(new SysPersonnel(
        //                0,
        //                manageDimission.getResignationTime()
        //        ),queryWrapper);
        //        iManageDimissionMapper.updateById(new ManageDimission(
        //                manageDimission.getId(),
        //                "agree"
        //        ));
        //    }
        //}catch (Exception e){
        //    log.info("捕获异常：{}",e.getMessage());
        //}

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
                queryWrapper.eq("management_id",applicationManagement.getAgoManagementId());
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
}
