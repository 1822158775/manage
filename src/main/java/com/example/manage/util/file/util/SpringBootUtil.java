package com.example.manage.util.file.util;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * @program：潘小章
 * @create：2022-04_17 18:21
 **/
public class SpringBootUtil {
    //获取jar的所在的路径文件
    public static File getJarDir() {
        ApplicationHome home = new ApplicationHome(SpringBootUtil.class);
        return home.getDir();
    }
    //获取jar的所在的路径文件字符串
    public static String getJarDirPath() {
        ApplicationHome home = new ApplicationHome(SpringBootUtil.class);
        return home.getDir().getAbsolutePath();
    }
}

