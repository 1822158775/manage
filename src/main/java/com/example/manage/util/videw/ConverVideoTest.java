package com.example.manage.util.videw;

public class ConverVideoTest {

    public static void main(String args[]) {
        for (int i = 2; i < 17; i++) {
            String path = "D:\\home\\equity\\manage\\target\\classes\\upload\\";
            String name = String.valueOf(i);
            VideoConvertor cv = new VideoConvertor(path + name + "\\" + name + ".mp4"
                    , path + name + "\\", name, "MOV", false, false);
            new Thread(cv).start();
        }
    }
}
