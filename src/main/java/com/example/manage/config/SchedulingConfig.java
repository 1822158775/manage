package com.example.manage.config;

import com.example.manage.job.SchedulingSysManagementService;
import com.example.manage.job.SchedulingSysPersonnelService;
import com.example.manage.mapper.IManageReimbursementCategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @avthor 潘小章
 * @date 2022/4/25
 */
@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Resource
    private SchedulingSysManagementService schedulingSysManagement;

    @Resource
    private SchedulingSysPersonnelService schedulingSysPersonnelService;

    @Resource
    private IManageReimbursementCategoryMapper iManageReimbursementCategoryMapper;

    //@Scheduled(cron = "0 0 20 ? * FRI") // 每周五晚上8点执行
    //public void SchedulingFRI() {
    //    Boolean aBoolean = theCommanderService.synchronousData();
    //    System.out.println("-------------" + aBoolean);
    //    System.out.println(DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms()));
    //}

    //0    0     12    *   *    ?
    //[秒] [分] [小时] [日] [月] [周] [年]
    //@Scheduled(cron = "0 0 10 ? * MON") // 每周一早上10点执行
    //public void SchedulingMON() {
    //
    //}
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

    //@Scheduled(cron="0 0/1 * * * ?")
    //private void minute() {
    //    iWhiteSysPersonnelService.birthdayInform();
    //}

    //每天10点查询2天后的人员生日和周年纪念
    @Scheduled(cron="0 0 10 * * ?")
    public void ExecuteOncePerSecond(){
        schedulingSysPersonnelService.birthdayInform();
    }

    //每天23点查询离职
    @Scheduled(cron="0 0 23 * * ?")
    public void DimissionOncePerSecond(){
        schedulingSysPersonnelService.dimissionInform();
    }

    //每月1号凌晨1点执行定时任务
    @Scheduled(cron = "0 0 1 1 * ?")
    public void MonthHoursDayofMonth(){
        schedulingSysManagement.windUpAnAccount();
    }

    //@Scheduled(cron = "59 59 23 * * ?")
    //public void everyDay(){
    //
    //}
}
