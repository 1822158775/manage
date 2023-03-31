package com.example.manage.controller;

import com.example.manage.service.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

@Slf4j
@RestController
@RequestMapping(value = "system")
public class SystemController {
    private IConfigService configService;

    /**
     * 验证通用开发参数及应用回调
     * @param: request
     * @param: response
     * @returns: void
     */
    @GetMapping(value = "getEchostr")
    public void doGetCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 微信加密签名
        String msgSignature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        // 如果是刷新，需返回原echostr
        String echoStr = request.getParameter("echostr");
        String sEchoStr=  "";
        PrintWriter out;
        log.debug("msgSignature: " + msgSignature+"timestamp="+timestamp+"nonce="+nonce+"echoStr="+echoStr);
        try {
            sEchoStr = configService.doGetCallback(msgSignature,timestamp,nonce,echoStr); //需要返回的明文;
            log.debug("doGetCallback-> echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            out = response.getWriter();
            out.print(sEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
    }

    /**
     * 刷新ticket，AuthCode
     */
    @PostMapping(value = "getEchostr")
    public String doPostCallback(HttpServletRequest request) throws Exception {
        // 微信加密签名
        String msgSignature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 类型
        String type = request.getParameter("type");
        // 企业id
        String corpId = request.getParameter("corpid");
        ServletInputStream in = request.getInputStream();
        // 刷新ticket，AuthCode
        String success = configService.doPostCallback(msgSignature, timestamp, nonce, type, corpId, in);
        return success;
    }
}
