package com.example.manage.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.PunchingCardRecord;
import com.example.manage.entity.SignInReview;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.job.SchedulingSysManagementService;
import com.example.manage.mapper.IManagementPersonnelMapper;
import com.example.manage.mapper.ISignInReviewMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.service.IPunchingCardRecordService;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.XlsxReader;
import com.example.manage.white_list.service.IWhiteSysPersonnelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2022/10/21
 */

@Component
@Slf4j
public class PanConfiguration implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    private RedisUtil redisUtil;

    @Value("${server.port}")
    private String port;

    @Value("${url.dispatch}")
    private String urlDispatch;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Value("${url.leave_job_list}")
    private String leaveJobList;

    @Resource
    private ISignInReviewMapper iSignInReviewMapper;

    @Resource
    private XlsxReader xlsxReader;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    try {
//        QueryWrapper<SysPersonnel> personnelQueryWrapper = new QueryWrapper<>();
//        personnelQueryWrapper.eq("role_id", 2);
//        personnelQueryWrapper.ne("id",804);
//        personnelQueryWrapper.ne("id",806);
//        Integer[] integers = {55,56,57,58};
//        List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.selectList(personnelQueryWrapper);
//        for (SysPersonnel sysPersonnel : sysPersonnels) {
//            System.out.println(sysPersonnel);
//            for (Integer integer : integers) {
//                iManagementPersonnelMapper.insert(new ManagementPersonnel(
//                        integer,
//                        sysPersonnel.getPersonnelCode()
//                ));
//            }
//        }
        //iWhiteSysPersonnelService.ceshi();
            //MythicalCreatures.mythical_creatures_1();
            //schedulingSysManagementService.windUpAnAccount();
            //Map<String,Object> map = new HashMap();
            //iWhiteSysPersonnelService.birthdayInform();
            //map.put("dateFormatBirthday",2);
            //map.put("dateFormatDispatchApplication",1);
            //redisUtil.add("dateFormatBirthday",map);
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                Enumeration<InetAddress> address = nif.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();
                    if (addr instanceof Inet4Address) {
                        log.info("网卡名称:{},请求链接:{}", nif.getName(),"http://" + addr.getHostAddress()+ ":" + port);
                    }
                }
            }
            //iWhiteSysPersonnelService.dimissionInform();
            //map.put("startTime","2023-05-01");
            //map.put("endTime","2023-05-31");
            //map.put("pageNum",10);
            //map.put("index",0);
            //ReturnEntity statistics = iPunchingCardRecordService.ceshi(map, "statistics");
            //System.out.println(statistics + "=======================");
            //xlsxReader.add(34,"兰州机场");
            //schedulingSysManagementService.taskNotification();
            //schedulingSysManagementService.taskNotificationCardReplacement();
            //iWhiteSysPersonnelService.ceshi();
            //xlsxReader.add2(1,"");
    } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                Enumeration<InetAddress> address = nif.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();
                    if (addr instanceof Inet4Address) {
                        System.out.println("网卡名称：" + nif.getName());
                        System.out.println("网络接口地址：" + addr.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
}
