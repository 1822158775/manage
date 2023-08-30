package com.example.manage.config;

import com.example.manage.job.SchedulingSysManagementService;
import com.example.manage.job.SchedulingSysPersonnelService;
import com.example.manage.mapper.IManageReimbursementCategoryMapper;
import com.example.manage.util.PanXiaoZhang;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2022/4/25
 */
@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig {
    /*上传目录*/
    @Value("${magicalcoder.file.upload.useDisk.mapping.ap:}")
    private String ap;

    @Resource
    private SchedulingSysManagementService schedulingSysManagement;

    @Resource
    private SchedulingSysPersonnelService schedulingSysPersonnelService;

    @Resource
    private IManageReimbursementCategoryMapper iManageReimbursementCategoryMapper;

    @Resource
    private SchedulingSysManagementService schedulingSysManagementService;

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
    //
    //}

    //每天10点查询2天后的人员生日和周年纪念
    @Scheduled(cron="0 0 10 * * ?")
    public void ExecuteOncePerSecond(){
        schedulingSysPersonnelService.birthdayInform();
        schedulingSysManagementService.taskNotification();
    }

    //每天13点查询2天后的人员生日和周年纪念
    @Scheduled(cron="0 0 13 * * ?")
    public void ExecuteOncePerSecond13(){
        schedulingSysManagementService.taskNotification();
    }

    //每天13点查询2天后的人员生日和周年纪念
    @Scheduled(cron="0 0 16 * * ?")
    public void ExecuteOncePerSecond16(){
        schedulingSysManagementService.taskNotification();
    }

    //每天23点查询离职
    @Scheduled(cron="0 0 23 * * ?")
    public void DimissionOncePerSecond(){
        schedulingSysPersonnelService.dimissionInform();
        //删除每天的日报文件
        // 获取当前时间
        Calendar cal = Calendar.getInstance();
        // 将当前时间减去一天
        cal.add(Calendar.DATE, -1);
        // 获取昨天的日期
        Date yesterday = cal.getTime();
        System.out.println(ap + DateFormatUtils.format(yesterday, PanXiaoZhang.yMd(1)));
        File dir = new File(ap + DateFormatUtils.format(yesterday, PanXiaoZhang.yMd(1)));
        PanXiaoZhang.deleteFile(dir);
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
