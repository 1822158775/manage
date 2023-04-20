package com.example.manage.util.videw;

public class ConverVideoTest {

    public static void main(String args[]) {
        start(2);
    }
    public static void start(Integer name){
        String path = "D:\\home\\equity\\manage\\target\\classes\\upload\\";
        VideoConvertor cv = new VideoConvertor(path + name + "\\" + name + ".mp4"
                , path + name + "\\", name + "", "MOV", false, false);
        new Thread(cv).start();
    }
}
