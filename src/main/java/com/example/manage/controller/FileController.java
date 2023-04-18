package com.example.manage.controller;

import com.example.manage.service.FileService;
import com.example.manage.util.entity.ReturnEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @avthor 潘小章
 * @date 2022/4/20
 */

@RestController
@RequestMapping(value = "/api/file/")
public class FileController {

    @Resource
    private FileService fileService;
    /**
     *
     * @param file 上传的新文件流
     * @param originFile 原始文件路径
     * @param from 来源是否是富文本  可以自行处理返回给富文本的值
     * @return
     * @throws IOException
     */
    @PostMapping(value = "add")
    public ReturnEntity fileUpload(@RequestParam("file") MultipartFile[] file,
                                   @RequestParam(required = false,value = "originFile") String originFile,
                                   @RequestParam(required = false,value = "from") String from,
                                   HttpServletRequest request) {
        ReturnEntity entity = fileService.fileUpload(file, originFile, from, request);
        return entity;
    }


    //读取excel并解析
    @RequestMapping("analysisExcel")
    public String analysisExcel(MultipartFile file,String str,String array,HttpServletRequest request) throws IOException { //注意和前端名字保持一致
        return fileService.analysisExcel(file,str,array,request);
    }
    //从文件中删除图片，并删除数据库信息
    @PostMapping("deleteImage")
    public String delFile(HttpServletRequest request) {
        return fileService.delFile(request);
    }
}
