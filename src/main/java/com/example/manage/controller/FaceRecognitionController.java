//package com.example.manage.controller;
//
//import com.example.manage.white_list.service.FaceRecognitionService;
//import com.example.manage.white_list.service.impl.FaceRecognitionServiceImpl;
//import org.opencv.core.*;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.objdetect.CascadeClassifier;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
///**
// * @avthor 潘小章
// * @date 2024/4/1
// */
//@RestController
//public class FaceRecognitionController {
//
//    @Resource
//    private FaceRecognitionService faceRecognitionService;
//
//    @PostMapping("/detect-face")
//    public ResponseEntity<Boolean> detectFace(@RequestParam("image") MultipartFile image) {
//        try {
//            // 保存上传的图片到临时文件
//            Path tempPath = Paths.get("D:\\home\\equity\\manage\\src\\main\\resources\\path\\to\\save\\temp\\image.jpg");
//            Files.write(tempPath, image.getBytes());
//
//            // 检测人脸
//            boolean faceDetected = faceRecognitionService.detectFace(tempPath.toString());
//
//            // 返回检测结果
//            return ResponseEntity.ok(faceDetected);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().build();
//        }
//    }
//}
