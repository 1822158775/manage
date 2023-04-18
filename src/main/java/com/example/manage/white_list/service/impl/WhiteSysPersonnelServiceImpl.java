package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.CardType;
import com.example.manage.entity.DispatchApplicationManagement;
import com.example.manage.entity.ManageDimission;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.mapper.IDispatchApplicationManagementMapper;
import com.example.manage.mapper.IManageDimissionMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.WhiteSysPersonnelMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteSysPersonnelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

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
        String format = DateFormatUtils.format(calculationDate, "M-d");
        log.info("日期：{}",format);
        Map map = new HashMap();
        map.put("dateFormatBirthday","start");
        map.put("agoBirthday", format);
        map.put("backBirthday",format);
        //进行查询的出符合条件的数据
        List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
        for (int i = 0; i < sysPersonnels.size(); i++) {
            SysPersonnel sysPersonnel = sysPersonnels.get(i);
            PanXiaoZhang.postWechat(
                    "15297599442",
                    "生日提醒",
                    "",
                    sysPersonnel.getName() + "的生日是" + DateFormatUtils.format(sysPersonnel.getBirthday(),PanXiaoZhang.yMd()),
                    "",
                    ""
            );
        }
    }

    /**
     * 离职操作
     */
    @Override
    public void dimissionInform() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("applicant_state","pending");
        wrapper.apply(true,"DATE_FORMAT(NOW(),\"%m-%d\") >= DATE_FORMAT(resignation_time,\"%m-%d\")");
        List<ManageDimission> list = iManageDimissionMapper.selectList(wrapper);
        for (int i = 0; i < list.size(); i++) {
            QueryWrapper queryWrapper = new QueryWrapper();
            ManageDimission manageDimission = list.get(i);
            queryWrapper.eq("personnel_code",manageDimission.getPersonnelCode());
            iSysPersonnelMapper.update(new SysPersonnel(
                false,
                manageDimission.getResignationTime()
            ),queryWrapper);
            iManageDimissionMapper.updateById(new ManageDimission(
                manageDimission.getId(),
                "agree"
            ));
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
