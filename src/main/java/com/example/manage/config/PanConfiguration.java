package com.example.manage.config;

import com.example.manage.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.*;
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

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            Map<String,Object> map = new HashMap();
            map.put("dateFormatBirthday",2);
            redisUtil.add("dateFormatBirthday",map);
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
                        System.out.println();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
}
