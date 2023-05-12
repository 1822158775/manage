package com.example.manage.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.wechat.entity.DataEntity;
import com.example.manage.util.wechat.entity.StateCode;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @avthor 潘小章
 * @date 2021/11/24
 */
public class WechatMsg {

    public static void main(String[] args) {
        //String token = "67_AukzFeFADsw83xuDS08H9dB04gFPkwJf7LaVlAS9_pqca7xX7Un9Ax87MZd3-NcaJChdBXd-PlO9NxzFAsejTslJ5SPezfEdN7nkwj-IU8C3P7HOCCou-ri2OKsEKBjAEAKQH";
        //String s = tuiSongXiaoXi(
        //        "o_QtX5qJzKGc3YmCG2eUb-v5ZEm8",
        //        "任务完成通知",
        //        "延禧攻略下载完成",
        //        "请前往家庭主机查看",
        //        "",
        //        "5_XBlqDRj5EQpliJcjCBoYrrKNiZAdOU54ZTX8H1Dvg",
        //        token
        //);
    }

    /**
     * 获取token
     *
     * @return token
     */
    public static ReturnEntity tuiSongXiaoXi(String openId, String keyword1, String keyword2, String keyword3, String keyword4, String templateId, String token, String pagepath) {
        //消息主题显示相关map
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if("5_XBlqDRj5EQpliJcjCBoYrrKNiZAdOU54ZTX8H1Dvg".equals(templateId)){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED, SendWeChatSignificance(token, openId, keyword1, keyword2, keyword3, keyword4, templateId,pagepath));
        }
        return new ReturnEntity("2","模板ID不正确");
    }
    /**
     * 获取token
     *
     * @return token
     */
    public static String getToken() {
        // 授予形式
        String grant_type = "client_credential";
        //应用ID
        String appid = "wx70a5d7e5c1104f4b";
        //密钥
        String secret = "5861057f2306723de07515c198c7b976";
        // 接口地址拼接参数
        String getTokenApi = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" + appid
                + "&secret=" + secret;

        String tokenJsonStr = doGetPost(getTokenApi, "GET", null);
        JSONObject tokenJson = JSONObject.parseObject(tokenJsonStr);
        System.out.println(tokenJson);
        String token = tokenJson.get("access_token").toString();
        System.out.println("获取到的TOKEN : " + token);
        return token;
    }

    /***
     * 发送消息
     * 重要事件通知消息模版
     * @param token
     */
    public static String SendWeChatSignificance(String token, String openId, String keyword1, String keyword2, String keyword3, String keyword4, String templateId,String pagepath) {
        try {
            // 接口地址
            String sendMsgApi = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
            //openId
            String toUser = openId;
            //消息模板ID:9SuDOYXCQWhlVO9qv_d1WndFBtu8KCCStwdnliyKo4U
            String template_id = templateId;
            //整体参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            // 主要是这里， 设置小程序的appid和转发的页面
            TreeMap<String, String> miniprograms = new TreeMap<String, String>();
            miniprograms.put("appid","wx410b1b17de879b8d");
            miniprograms.put("pagepath",pagepath);// 注意，这里是支持传参的！！！
            paramMap.put("miniprogram",miniprograms);
            //消息主题显示相关map
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //根据自己的模板定义内容和颜色
            System.out.println(keyword1 + "============");
            //标题
            dataMap.put("first",new DataEntity(keyword1,"#173177"));
            //什么类型的工作
            dataMap.put("type",new DataEntity(keyword2,"#173177"));
            //通知内容
            dataMap.put("content",new DataEntity(keyword3,"#173177"));
            //备注
            dataMap.put("remark",new DataEntity(keyword4,"#173177"));
            paramMap.put("touser", toUser);
            paramMap.put("template_id", template_id);
            paramMap.put("data", dataMap);
            String post = doGetPost(sendMsgApi, "POST", paramMap);
            return post;
        }catch(Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errchde","3");
            jsonObject.put("errmsg","异常");
            return jsonObject.toString();
        }
    }
    public String SendWeChatActivity(String token,String openId,String keyword1,String keyword2,String keyword3,String keyword4,String templateId) {
        try {
            // 接口地址
            String sendMsgApi = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
            //openId
            String toUser = openId;
            //消息模板ID:9SuDOYXCQWhlVO9qv_d1WndFBtu8KCCStwdnliyKo4U
            String template_id = templateId;
            //整体参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            // 主要是这里， 设置小程序的appid和转发的页面
            TreeMap<String, String> miniprograms = new TreeMap<String, String>();
            miniprograms.put("appid","wx410b1b17de879b8d");
            miniprograms.put("pagepath","/pages/activities/show/show?id=38");// 注意，这里是支持传参的！！！
            paramMap.put("miniprogram",miniprograms);
            //消息主题显示相关map
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //根据自己的模板定义内容和颜色
            dataMap.put("first",new DataEntity(keyword1,"#173177"));
            //订单
            dataMap.put("keyword1",new DataEntity(keyword2,"#173177"));
            //原价格
            dataMap.put("keyword2",new DataEntity(keyword3,"#173177"));
            //最终成交价
            dataMap.put("keyword3",new DataEntity(keyword4,"#173177"));
            dataMap.put("remark",new DataEntity("即刻参与活动，数量有限，先到先得！","#173177"));
            paramMap.put("touser", toUser);
            paramMap.put("template_id", template_id);
            paramMap.put("data", dataMap);
            String post = doGetPost(sendMsgApi, "POST", paramMap);
            return post;
        }catch(Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errchde","3");
            jsonObject.put("errmsg","异常");
            return jsonObject.toString();
        }
    }
    /**
     * 航班提醒
     */
    public String HangBan(String token,String openId,Map<String,Object> dataMap,String templateId) {
        try {
            // 接口地址
            String sendMsgApi = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
            //openId
            String toUser = openId;
            //消息模板ID:9SuDOYXCQWhlVO9qv_d1WndFBtu8KCCStwdnliyKo4U
            String template_id = templateId;
            //整体参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("touser", toUser);
            paramMap.put("template_id", template_id);
            paramMap.put("data", dataMap);
            String post = doGetPost(sendMsgApi, "POST", paramMap);
            return post;
        }catch(Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errchde","3");
            jsonObject.put("errmsg","异常");
            return jsonObject.toString();
        }
    }
    /**
     * 调用接口 post
     * @param apiPath
     */
    public static String doGetPost(String apiPath, String type, Map<String, Object> paramMap){
        OutputStreamWriter out = null;
        InputStream is = null;
        String result = null;
        try{
            URL url = new URL(apiPath);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(type) ; // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            if(type.equals("POST")){
                out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
                out.append(JSON.toJSONString(paramMap));
                out.flush();
                out.close();
            }
            // 读取响应
            is = connection.getInputStream();
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                result = new String(data, "UTF-8"); // utf-8编码
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }
}
