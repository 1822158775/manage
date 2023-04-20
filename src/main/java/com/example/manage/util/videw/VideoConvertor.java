package com.example.manage.util.videw;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoConvertor implements Runnable {

    private String sourceVideoPath;//源视频路径 ，包括文件名
    private File videoFile;//需要转换的视频文件
    private boolean videoExist;//需要转换的视频文件
    private String fileRealName; // 文件名 不包括扩展名  
    private String targetFolder; // 存放转换后的视频文件夹路径
    private String targetName; // 转换后的名字，不包含后缀
    private String ffmpegPath; // ffmpeg.exe的目录  
    private String mencoderPath; // mencoder的目录  
    private String targetExtension;
    private boolean delSourceFile;
    private boolean screenshot;

    /**
     * 能解析的格式：asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等；
     * wmv9，rm，rmvb 将调用 mencoder 先转换成 avi 再调用 ffmpeg 转换，耗时较长<br>
     * 注意！！！这里的方法只能单任务执行啊！执行结束后才能进行下一次的转换
     * @param sourceVideoPath 源视频路径 ，包括文件名
     * @param targetFolder 输出目标文件夹，以\结尾
     * @param targetName 输出目标文件名
     * @param targetExtension 目标格式，只对视频有效，如果只是做视频截取第一帧不做转换的话就 null 吧；
     * @param delSourceFile 是否删除源文件，用途是只截取视频第一帧的话是不会删除源文件的
     * @param screenshot 转换时是否截取第一帧
     */
    public VideoConvertor(String sourceVideoPath, String targetFolder, String targetName, String targetExtension, boolean delSourceFile, boolean screenshot) {
        setSourceVideoPath(sourceVideoPath);
        this.targetFolder = targetFolder;
        this.targetName = targetName;
        this.targetExtension = targetExtension;
        this.delSourceFile = delSourceFile;
        this.screenshot = screenshot;
        // 这里的 ffmpeg 和 mencoder 的路径使用配置文件读取的方式
        this.ffmpegPath = "D:\\home\\xiangmu\\wechat-one\\影视\\行尸之惧第一季\\ffmpeg-6.0-essentials_build\\bin\\ffmpeg.exe"; // ffmpeg.exe的目录
        this.mencoderPath = "D:\\home\\xiangmu\\wechat-one\\影视\\行尸之惧第一季\\mencoder\\mencoder.exe"; // mencoder.exe的目录
    }

    /**
     * 能解析的格式：asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等
     * wmv9，rm，rmvb 将调用 mencoder 先转换成 avi 再调用 ffmpeg 转换，耗时较长
     * @return
     */
    public boolean convert() {
        if (!videoExist) {
            System.out.println(sourceVideoPath + " is not file");
            return false;
        }
        System.out.println("----开始转文件(" + sourceVideoPath + ")-------------------------- ");
        long beginTime = System.currentTimeMillis();
        if (process(targetExtension)) {
            long timecha = (System.currentTimeMillis() - beginTime);
            System.out.println("转换成功 ,耗时：" + sumTime(timecha));
            if (delSourceFile && videoFile.delete()) {
                System.out.println("文件" + sourceVideoPath + "已删除");
            }
            if (screenshot) {
                screenshot();
            }
            Integer integer = Integer.valueOf(fileRealName);
            integer++;
            if (integer < 17){
                ConverVideoTest.start(integer);
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 对视频进行截图 
     * @return
     */
    public boolean screenshot() {
        if (!videoExist) {
            System.out.println(sourceVideoPath + " is not file");
            return false;
        }
        List<String> commend = new java.util.ArrayList<String>();
        // 第一帧： 00:00:01，取第一秒那一帧
        // time ffmpeg -ss 00:00:01 -i test1.flv -f image2 -y test1.jpg
        commend.add(ffmpegPath);
        commend.add("-ss");
        commend.add("00:00:01");
        commend.add("-i");
        commend.add(sourceVideoPath);
        commend.add("-f");
        commend.add("image2");
        commend.add("-y");
        commend.add(targetFolder + fileRealName + ".jpg");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.start();
            System.out.println("截图成功了！ ");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("截图失败了！ ");
            return false;
        }
    }

    /**
     * 实际转换视频格式的方法 
     * @param targetExtension 目标视频扩展名 
     * @return
     */
    private boolean process(String targetExtension) {
        int type = checkContentType();
        boolean status = false;
        if (type == 1) {// 如果 type 为 1，将其他文件先转换为 avi ，然后在用 ffmpeg 转换为指定格式  
            System.out.println("非 ffmpeg 支持格式，先转换为 avi，开始转换");
            status = processAVI();
            if (!status){// avi文件没有得到 ,也就是转换失败了
                System.out.println("转换成 avi 格式失败了~~");
                return false;
            }
        }
        if (type == 0 || status) {// 如果 type 为 0 用 ffmpeg 直接转换  
            System.out.println("ffmpeg 开始转换:");
            status = processVideoFormat(targetExtension);
        }
        return status;
    }

    /**
     * 转换为指定格式 
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等） 
     * @param targetExtension 目标格式扩展名 .xxx 
     * @return
     */
    private boolean processVideoFormat(String targetExtension) {
        if (!videoExist) {
            System.out.println(sourceVideoPath + " is not file");
            return false;
        }
        //ffmpeg -i FILE_NAME.flv -ar 22050 NEW_FILE_NAME.mp4  
        List<String> commend = new java.util.ArrayList<>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(sourceVideoPath);
        commend.add("-ar");
        commend.add("22050");
        commend.add(targetFolder + targetName + "." + targetExtension);
        try {
            exeCommond(commend);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式. 
     * @return
     */
    private boolean processAVI() {
        List<String> commend = new java.util.ArrayList<String>();
        String convertTo = targetFolder + fileRealName + ".avi";
        commend.add(mencoderPath);
        commend.add(sourceVideoPath);
        commend.add("-oac");
        commend.add("mp3lame");
        commend.add("-lameopts");
        commend.add("preset=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add(convertTo);
        // 命令类型：mencoder 1.rmvb -oac mp3lame -lameopts preset=64 -ovc xvid  
        // -xvidencopts bitrate=600 -of avi -o rmvb.avi  
        try {
            exeCommond(commend);
            setSourceVideoPath(convertTo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 执行命令行
     * @param commend
     * @return
     */
    private int exeCommond(List<String> commend) {
        ProcessBuilder builder = new ProcessBuilder();
        String cmd = commend.toString();
        builder.command(commend);
        builder.redirectErrorStream(true);//合并错误流到普通的流中，便于一起输出
        Process p = null;
        int exitValue = 1;
        InputStream in = null;
        try {
            p = builder.start();
            in = p.getInputStream();
            int read = 0;
            while (in.read() != -1) {
                read = in.read();
                System.out.println(in.read());
            }
            // 用这个来监控命令行的执行，当视频转换完毕，命令行那边的也就没有东西输出了
            while (exitValue != 0) {// 如果需要看到 ffmpeg 和 mencoder 的输出信息可以使用这种，注释解开就行
            	try {
            		while (in.available() > 0) {
            			Character c = new Character((char) in.read());
                        System.out.print(c);
            		}
            		exitValue = p.exitValue();
            	} catch (IllegalThreadStateException e) {
            		p.waitFor(1, TimeUnit.SECONDS);
            	}
            }
        } catch (Exception e) {
            System.err.println("exeCommond: unexpected exception - " + e.getMessage());
        }
        try {
            exitValue = p.exitValue();
        } catch (IllegalThreadStateException e) {
            e.printStackTrace();
        }
        p.destroy();
        return exitValue;
    }

    /**
     * 检查文件类型 
     * @return
     */
    private int checkContentType() {
        String type = sourceVideoPath.substring(sourceVideoPath.lastIndexOf(".") + 1, sourceVideoPath.length()).toLowerCase();
        String ffmpegFormat = "avi|mpg|wmv|3gp|mov|mp4|asf|asx|flv";
        String mencoderFormat = "wmv9|rm|rmvb";
        if (ffmpegFormat.contains(type)) {// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）  
            return 0;
        }else if (mencoderFormat.contains(type)) {// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式
            return 1;
        }
        return 9;
    }

    private String sumTime(long ms) {
        int ss = 1000;
        long mi = ss * 60;
        long hh = mi * 60;
        long dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        String strDay = day < 10 ? "0" + day + "天" : "" + day + "天";
        String strHour = hour < 10 ? "0"+ hour + "小时" : "" + hour + "小时";
        String strMinute = minute < 10 ?"0" + minute + "分" : "" + minute + "分";
        String strSecond = second < 10 ?"0" + second + "秒" : "" + second + "秒";
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond + "毫秒" : "" + strMilliSecond + " 毫秒";
        return strDay + " " + strHour + ":" + strMinute + ":" + strSecond + " " + strMilliSecond;
    }

    private void setSourceVideoPath(String sourceVideoPath) {
        this.sourceVideoPath = sourceVideoPath;
        this.videoFile = Paths.get(sourceVideoPath).toFile();
        this.videoExist = videoFile.exists() && videoFile.isFile() ? true : false;
        String fileName = videoFile.getName();
        this.fileRealName = fileName.substring(0, fileName.lastIndexOf(".")).toLowerCase();
    }

    // 不知道这样做对不对，但是在实际开发中不可能一直等着当前线程转码完成，
    // 遇到要使用 mencoder 才能转换的格式的话还要等待， 所以就开条线程咯~~，
    @Override
    public void run() {
        this.convert();
    }
}