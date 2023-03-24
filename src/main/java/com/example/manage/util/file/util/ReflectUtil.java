package com.example.manage.util.file.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @program：潘小章
 * @create：2022-04_17 18:02
 **/
public class ReflectUtil<K, E> {
    public K getBeanValue(E obj, String field){
        try {
            String name = "get"+firstCharUp(field);
            Method method = obj.getClass().getMethod(name);
            method.setAccessible(true);//提供速度 由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的
            Object value = method.invoke(obj);
            method.setAccessible(false);
            return (K)value;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String firstCharUp(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

}
