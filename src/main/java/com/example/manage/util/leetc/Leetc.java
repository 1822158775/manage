package com.example.manage.util.leetc;

import java.util.HashMap;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/24
 */

public class Leetc {
    public static void main(String[] args) {


        System.out.println(15 % 2);
    }
    /**
     * 第三题
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        Map<String,Integer> map = new HashMap<>();

        String[] str = s.split("");

        Integer min_number = 0;
        Integer max_number = 0;
        for(int i = 0;i < s.length();i++){
            String string = str[i];
            Integer number = map.get(string);
            if(number != null){
                min_number = Math.max(min_number,number + 1);
            }
            map.put(string,i);
            max_number = Math.max((i - min_number) + 1,max_number);
        }
        return max_number;
    }

}
