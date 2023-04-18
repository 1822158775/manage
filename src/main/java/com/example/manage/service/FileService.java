package com.example.manage.service;

import com.example.manage.util.entity.ReturnEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @avthor 潘小章
 * @date 2022/4/24
 */

public interface FileService {
    String delFile(HttpServletRequest request);
    ReturnEntity fileUpload(@RequestParam("file") MultipartFile[] file,
                            @RequestParam(required = false,value = "originFile") String originFile,
                            @RequestParam(required = false,value = "from") String from,
                            HttpServletRequest request);
    String analysisExcel(MultipartFile file,String str,String array,HttpServletRequest request);
}
