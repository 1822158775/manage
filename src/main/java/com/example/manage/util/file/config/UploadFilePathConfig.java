package com.example.manage.util.file.config;

import com.aliyun.oss.OSSClient;
import com.example.manage.util.file.util.SpringBootUtil;
import com.example.manage.util.file.util.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PreDestroy;


/**
 * @program：潘小章
 * @create：2022-04_17 17:45
 **/
@Slf4j
@Configuration
@Getter
public class UploadFilePathConfig extends WebMvcConfigurerAdapter {

    /*使用本地硬盘上传*/
    @Value("${magicalcoder.file.upload.useDisk.open:false}")
    private Boolean useDisk;
    /*使用本地虚拟路径返回前端*/
    @Value("${magicalcoder.file.upload.useDisk.returnUrl:false}")
    private Boolean useDiskReturnUrl;
    /*上传目录*/
    @Value("${magicalcoder.file.upload.useDisk.mapping.uploadDiskFolder:}")
    private String uploadDiskFolder;
    /*虚拟前缀*/
    @Value("${magicalcoder.file.upload.useDisk.mapping.requestPrefix:}")
    private String requestPrefix;
    /*文件上传额外添加的前缀*/
    @Value("${magicalcoder.file.upload.useDisk.mapping.fileExtraAddPrefix:}")
    private String fileExtraAddPrefix;
    public String getFileExtraAddPrefix() {
        return fileExtraAddPrefix;
    }
    public String originFilePath(String originFile){
        if(StringUtil.isBlank(originFile) || originFile.startsWith("http://") || originFile.startsWith("https://")){
            return "";
        }
        String[] arr = originFile.split("/");
        StringBuilder pathBuilder = new StringBuilder();
        for(int i=0;i<arr.length;i++){
            if(i!=arr.length-1){
                pathBuilder.append(arr[i]).append("/");
            }
        }

        String path = pathBuilder.toString();
        String extraPrefix = getFileExtraAddPrefix();

        String prefix = extraPrefix;
        if(StringUtil.isNotBlank(prefix)){
            if(path.startsWith(prefix)){
                path = path.substring(prefix.length());//把虚拟前缀都去掉
            }
        }
        if("/".equals(path)){
            path = "";
        }
        return path;
    }
    private String lastUploadPath(String uploadDiskFolder){
        if(StringUtil.isBlank(uploadDiskFolder)){
            uploadDiskFolder = SpringBootUtil.getJarDirPath();
            log.info("jar file path:"+uploadDiskFolder);
//            uploadDiskFolder = UploadFilePathConfig.class.getResource("/").getPath();
            if(!uploadDiskFolder.equals("\\")){
                uploadDiskFolder +="\\";
            }
            uploadDiskFolder +="upload\\";
        }
        uploadDiskFolder = uploadDiskFolder.replaceAll("\\\\","/");
        if(!uploadDiskFolder.startsWith("/")){
            uploadDiskFolder = "/"+uploadDiskFolder;
        }
        if(!uploadDiskFolder.endsWith("/")){
            uploadDiskFolder = uploadDiskFolder+"/";
        }
        return uploadDiskFolder;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        uploadDiskFolder = lastUploadPath(uploadDiskFolder);
        if(!requestPrefix.startsWith("/")){
            requestPrefix = "/"+requestPrefix;
        }
        if(!requestPrefix.endsWith("/")){
            requestPrefix = requestPrefix+"/";
        }
        log.info("文件映射路径"+uploadDiskFolder);
        registry.addResourceHandler(requestPrefix+"**").addResourceLocations("file:" + uploadDiskFolder);
    }

    /*阿里云oss上传文件组件封装*/
    @Value("${magicalcoder.file.upload.useAliyunOss.open:false}")
    private Boolean useAliyunOss;
    /*使用本地虚拟路径返回前端*/
    @Value("${magicalcoder.file.upload.useAliyunOss.returnUrl:false}")
    private Boolean useAliyunOssReturnUrl;
    @Value("${magicalcoder.file.upload.useAliyunOss.endpoint}")
    private String endpoint;
    @Value("${magicalcoder.file.upload.useAliyunOss.accessKeyId}")
    private String accessKeyId;
    @Value("${magicalcoder.file.upload.useAliyunOss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${magicalcoder.file.upload.useAliyunOss.bucketName}")
    private String bucketName;
    @Value("${magicalcoder.file.upload.useAliyunOss.aliyunImgDomain}")
    private String aliyunImgDomain;

    private volatile OSSClient ossClient;

    public OSSClient ossClient(){
        if(ossClient==null){//单例双锁
            synchronized (UploadFilePathConfig.class){
                if(ossClient==null){
                    ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
                }
            }
        }
        return ossClient;
    }
    @PreDestroy
    public void destroy(){
        if(ossClient!=null){
            ossClient.shutdown();
        }
    }
}
