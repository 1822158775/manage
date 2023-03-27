package com.example.manage.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/3/8
 */
@Slf4j
public class LangDu {
    //用电脑自带的语音读字符串str
    public static void main(String[] args) {
        start(2);
    }

    public static List<String> urlS(String path){
        File file = new File(path);
        List<String> list = txt2String(file);
        return list;
    }
    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static List<String> txt2String(File file){
        List<String> strings = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                strings.add(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return strings;
    }
    public static void start(Integer number){
        log.info("播放第" + number + "集");
        List<String> stringList = urlS("D:\\home\\xiangmu\\wechat-one\\小说\\民间风水奇谭\\" + number + ".txt");
        //List<String> stringList = Ceshi.urlS("C:\\Users\\Administrator\\AppData\\Local\\Temp\\baiduyunguanjia\\onlinedit\\cache\\efc6c08a899eb6a8a2626c0df8da4800\\1.txt");
        String str = "";

        for (String string :
                stringList) {
            str += string;
        }

        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        Dispatch sapo = sap.getObject();
        try {
            // 音量 0-100
            sap.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            sap.setProperty("Rate", new Variant(2));
            // 执行朗读
            Dispatch.call(sapo, "Speak", new Variant(str));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sapo.safeRelease();
            sap.safeRelease();
        }
        number++;
        start(number);
    }
    public static void HuiFu(String str){
        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        Dispatch sapo = sap.getObject();
        try {
            // 音量 0-100
            sap.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            sap.setProperty("Rate", new Variant(2));
            // 执行朗读
            Dispatch.call(sapo, "Speak", new Variant(str));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sapo.safeRelease();
            sap.safeRelease();
        }
    }
}
