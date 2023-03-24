package com.example.manage.util.file.util;

import java.util.List;

/**
 * @program：潘小章
 * @create：2022-04_17 18:01
 **/
public class ListUtil {
    public static boolean isBlank(List ls){
        return ls==null || ls.isEmpty();
    }
    public static boolean isNotBlank(List ls){
        return !isBlank(ls);
    }
}