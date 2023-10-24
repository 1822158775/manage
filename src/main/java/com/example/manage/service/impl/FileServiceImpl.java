package com.example.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.manage.service.FileService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.file.config.UploadFilePathConfig;
import com.example.manage.util.file.util.ListUtil;
import com.example.manage.util.file.util.MapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @avthor 潘小章
 * @date 2022/4/24
 */
@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    /*上传目录*/
    @Value("${magicalcoder.file.upload.useDisk.mapping.ap:}")
    private String ap;

    @Resource
    private UploadFilePathConfig uploadFilePathConfig;
    @Override
    public String delFile(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getMap(request);
        //字符串截断，获取图片的名称
        int lastIndexOf = jsonMap.get("realUrl").toString().lastIndexOf("/");
        String img_path = jsonMap.get("realUrl").toString().substring(lastIndexOf + 1, jsonMap.get("realUrl").toString().length());
        //拼接图片的绝对地址
        img_path = ap + img_path;

        File file = new File(img_path);
        if (file.exists()) {//文件是否存在
            if (file.delete()) {//存在就删了
                return PanXiaoZhang.getJsonReturn(new ReturnEntity(
                        CodeEntity.CODE_SUCCEED,
                        PanXiaoZhang.judgmentToken(request),
                        MsgEntity.CODE_SUCCEED
                ));
            } else {
                return PanXiaoZhang.getJsonReturn(new ReturnEntity(
                        CodeEntity.CODE_SUCCEED,
                        "删除失败"
                ));
            }
        }else {
            return PanXiaoZhang.getJsonReturn(new ReturnEntity(
                    CodeEntity.CODE_SUCCEED,
                    "文件不存在"
            ));
        }
    }

    @Override
    public ReturnEntity fileUpload(MultipartFile[] file, String originFile, String from, HttpServletRequest request) {
        System.out.println("================");
        try {
            String originFilePath = uploadFilePathConfig.originFilePath(originFile);
            String realPath = uploadFilePathConfig.getUploadDiskFolder()+ originFilePath ;
            //如果文件夹不存在就新建一个文件夹 聪明的根据当前目录规则来进行上传
            File dirPath = new File(realPath);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            String originalFilename = null;
            //List<Map> returnUrls = new ArrayList<>();
            List<String> returnUrls = new ArrayList<>();
            for (MultipartFile myfile : file) {
                if (!myfile.isEmpty()) {
                    // 获取文件名
                    originalFilename = myfile.getOriginalFilename();
                    String returnUrl = "";
                    boolean streamUsed = false;
                    File storeFile = null;
                    // 文件名后缀处理 使用uuid方式生成新文件名称 绝对唯一
                    String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String newFileName = UUID.randomUUID().toString() + suffix;
                    if(uploadFilePathConfig.getUseDisk()){//本地路径
                        storeFile = new File(realPath, newFileName);
                        if(!storeFile.getParentFile().exists()){
                            storeFile.getParentFile().mkdirs();
                        }
                        FileUtils.copyInputStreamToFile(
                                myfile.getInputStream(), storeFile);//上传文件到磁盘 流会被拷贝走
                        streamUsed = true;
                        String prefix = uploadFilePathConfig.getFileExtraAddPrefix() +originFilePath;
                        String src = prefix+newFileName;
                        while (src.contains("//")){
                            src = src.replace("//","/");
                        }
                        if(uploadFilePathConfig.getUseDiskReturnUrl()){
                            returnUrl = src;
                        }
                        if(uploadFilePathConfig.getUseAliyunOss()){//阿里云路径
                            if(streamUsed){//那就上传本地文件
                                uploadFilePathConfig.ossClient().putObject(uploadFilePathConfig.getBucketName(),newFileName,storeFile);
                            }else {//直接使用客户端流 用户未上传过给本地文件
                                uploadFilePathConfig.ossClient().putObject(uploadFilePathConfig.getBucketName(),newFileName,myfile.getInputStream());
                            }
                            if(uploadFilePathConfig.getUseAliyunOssReturnUrl()){
                                returnUrl = uploadFilePathConfig.getAliyunImgDomain()+newFileName;
                            }
                        }
                    }
                    //returnUrls.add(MapUtil.buildMap("src",returnUrl,"title",originalFilename));
                    returnUrls.add(returnUrl);
                }
            }
            if(ListUtil.isNotBlank(returnUrls)){
                if(returnUrls.size() == 1){//单个文件上传 返回1个地址
                    return new ReturnEntity(
                            CodeEntity.CODE_SUCCEED,
                            returnUrls.get(0),
                            MsgEntity.CODE_SUCCEED);
                }
                //多个文件 返回多个地址
                return new ReturnEntity(
                        CodeEntity.CODE_SUCCEED,
                        returnUrls,
                        MsgEntity.CODE_SUCCEED);
            }
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常{}",e.getMessage());
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    MsgEntity.CODE_ERROR);
        }
    }

    @Override
    public String analysisExcel(MultipartFile file, String str, String array, HttpServletRequest request) {
        try {
            ObjectMapper objectMapper =new ObjectMapper();
            //String nativeStr =str; //获取字符串
            InputStream inputStream = null;
            try {
                //List<String> arrayList =(List)objectMapper.readValue(array, List.class);  //读取前端数组存在list中

                inputStream =file.getInputStream();//获取前端传递过来的文件对象，存储在“inputStream”中
                String fileName = file.getOriginalFilename();//获取文件名

                Workbook workbook =null; //用于存储解析后的Excel文件

                //判断文件扩展名为“.xls还是xlsx的Excel文件”,因为不同扩展名的Excel所用到的解析方法不同
                String fileType = fileName.substring(fileName.lastIndexOf("."));
                if(".xls".equals(fileType)){
                    workbook= new HSSFWorkbook(inputStream);//HSSFWorkbook专门解析.xls文件
                }else if(".xlsx".equals(fileType)){
                    workbook = new XSSFWorkbook(inputStream);//XSSFWorkbook专门解析.xlsx文件
                }

                ArrayList<ArrayList<Object>>list =new ArrayList<>();

                Sheet sheet; //工作表
                Row row;      //行
                Cell cell;    //单元格

                //循环遍历，获取数据
                for(int i=0;i<workbook.getNumberOfSheets();i++){
                    sheet=workbook.getSheetAt(i);//获取sheet
                    for(int j=sheet.getFirstRowNum();j<=sheet.getLastRowNum();j++){//从有数据的第行开始遍历
                        row=sheet.getRow(j);
                        if(row!=null&&row.getFirstCellNum()!=j){ //row.getFirstCellNum()!=j的作用是去除首行，即标题行，如果无标题行可将该条件去掉
                            ArrayList tempList =new ArrayList();
                            for(int k=row.getFirstCellNum();k<row.getLastCellNum();k++){//这里需要注意的是getLastCellNum()的返回值为“下标+1”
                                cell =row.getCell(k);
                                tempList.add(cell);
                            }
                            list.add(tempList);
                        }
                    }
                }
                return PanXiaoZhang.getJsonReturn(new ReturnEntity(
                        CodeEntity.CODE_SUCCEED,
                        null,
                        request,
                        MsgEntity.CODE_SUCCEED,
                        1
                ));
            } catch (IOException e) {
                log.info("捕获异常{}",e.getMessage());
                return PanXiaoZhang.getJsonReturn(new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        MsgEntity.CODE_ERROR
                ));
            }finally {
                inputStream.close();
            }
        }catch (Exception e){
            log.info("捕获异常{}",e.getMessage());
            return PanXiaoZhang.getJsonReturn(new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    MsgEntity.CODE_ERROR
            ));
        }
    }
}
