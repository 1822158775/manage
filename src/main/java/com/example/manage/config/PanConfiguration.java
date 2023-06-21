package com.example.manage.config;

import com.example.manage.job.SchedulingSysManagementService;
import com.example.manage.mapper.ICardTypeMapper;
import com.example.manage.mapper.IPerformanceReportMapper;
import com.example.manage.service.IPunchingCardRecordService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.XlsxReader;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.mythical_creatures.MythicalCreatures;
import com.example.manage.white_list.service.IWhiteSysPersonnelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.*;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

    @Value("${phone.birthday}")
    private String personnelPhone;

    @Resource
    private IWhiteSysPersonnelService iWhiteSysPersonnelService;

    @Resource
    private IPunchingCardRecordService iPunchingCardRecordService;

    @Resource
    private SchedulingSysManagementService schedulingSysManagementService;

    @Resource
    private XlsxReader xlsxReader;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    try {
            //iWhiteSysPersonnelService.ceshi();
            MythicalCreatures.mythical_creatures_1();
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
