package com.example.manage.config;

import com.example.manage.entity.SysPersonnel;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2022/4/25
 */
@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig {
    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;
    @Resource
    private RedisUtil redisUtil;
    //@Scheduled(cron = "0 0 20 ? * FRI") // 每周五晚上8点执行
    //public void SchedulingFRI() {
    //    Boolean aBoolean = theCommanderService.synchronousData();
    //    System.out.println("-------------" + aBoolean);
    //    System.out.println(DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms()));
    //}

    //0    0     12    *   *    ?
    //[秒] [分] [小时] [日] [月] [周] [年]
    @Scheduled(cron = "0 0 10 ? * MON") // 每周一早上10点执行
    public void SchedulingMON() {

    }
    //@Scheduled(cron = "0 0 0 ? * MON") // 每周一晚上0点执行
    //public void SchedulingMON() {
    //
    //}

    //@Scheduled(cron = "0 0 20 ? * WED") // 每周三晚上八点执行
    //public void SchedulingWED() {
    //    Boolean aBoolean = theCommanderService.synchronousData();
    //    System.out.println("-------------" + aBoolean);
    //    System.out.println(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
    //}
    //@Scheduled(fixedDelay = 1*100000)   //定时器定义，设置执行时间 1s
    //private void process1() {
    //    Boolean aBoolean = theCommanderService.synchronousData();
    //    System.out.println("-------------" + aBoolean);
    //}
    //查询2天后的人员生日
    //@Scheduled(cron="0 0/1 * * * ?")
    //public void ExecuteOncePerSecond(){
    //    //获取Redis设置的天数
    //    Map<Object, Object> dateFormatBirthday = redisUtil.getHashEntries("dateFormatBirthday");
    //    //将存储在Redis里的Map存储的值取出并转化为数字类型
    //    Integer birthday = Integer.valueOf(String.valueOf(dateFormatBirthday.get("dateFormatBirthday")));
    //    //获取当前时间
    //    Date date = new Date();
    //    //进行计算n天后的日期
    //    Date calculationDate = PanXiaoZhang.calculationDate(date, birthday);
    //    //进行转化为响应的日期格式
    //    String format = DateFormatUtils.format(calculationDate, "M-d");
    //    log.info("日期：{}",format);
    //    Map map = new HashMap();
    //    map.put("dateFormatBirthday","start");
    //    map.put("agoBirthday", format);
    //    map.put("backBirthday",format);
    //    //进行查询的出符合条件的数据
    //    List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
    //    for (int i = 0; i < sysPersonnels.size(); i++) {
    //        SysPersonnel sysPersonnel = sysPersonnels.get(i);
    //        System.out.println(sysPersonnel);
    //    }
    //}
    //@Scheduled(cron = "59 59 23 * * ?")
    //public void everyDay(){
    //
    //}
}
