package com.example.manage.job;

/**
 * @avthor 潘小章
 * @date 2023/5/22
 */

public interface SchedulingSysManagementService {
    void windUpAnAccount();
    void taskNotification();//日常请假任务提醒
    void taskNotificationCardReplacement();//日常补卡任务提醒
}
