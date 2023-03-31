package com.example.manage.service;

import javax.servlet.ServletInputStream;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

public interface IConfigService {
    /**
     * 验证通用开发参数及应用回调
     * @returns: java.lang.String
     */
    String doGetCallback(String msgSignature, String timestamp, String nonce, String echoStr);

    /**
     * 获取SuiteTicket，AuthCode
     */
    String doPostCallback(String msgSignature, String timestamp, String nonce, String type, String corpId, ServletInputStream in);
}
