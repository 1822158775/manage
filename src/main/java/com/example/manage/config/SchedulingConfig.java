package com.example.manage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @avthor 潘小章
 * @date 2022/4/25
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    //@Scheduled(cron = "0 0 20 ? * FRI") // 每周五晚上8点执行
    //public void SchedulingFRI() {
    //    Boolean aBoolean = theCommanderService.synchronousData();
    //    System.out.println("-------------" + aBoolean);
    //    System.out.println(DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms()));
    //}

    //@Scheduled(cron = "0 0 20 ? * MON") // 每周一晚上8点执行
    //public void SchedulingMON() {
    //    Boolean aBoolean = theCommanderService.synchronousData();
    //    System.out.println("-------------" + aBoolean);
    //    System.out.println(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
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
    //public void ExecuteOncePerSecond(){
    //
    //}
    //@Scheduled(cron = "59 59 23 * * ?")
    //public void everyDay(){
    //
    //}
}
