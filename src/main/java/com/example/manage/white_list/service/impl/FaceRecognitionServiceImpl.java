//package com.example.manage.white_list.service.impl;
//
//
//import com.example.manage.util.PanXiaoZhang;
//import com.example.manage.util.entity.ReturnEntity;
//import com.example.manage.white_list.service.FaceRecognitionService;
//import lombok.extern.slf4j.Slf4j;
//import org.opencv.core.*;
//import org.opencv.highgui.HighGui;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.objdetect.CascadeClassifier;
//import org.springframework.stereotype.Service;
//
///**
// * @avthor 潘小章
// * @date 2024/4/1
// */
//@Service
//@Slf4j
//public class FaceRecognitionServiceImpl implements FaceRecognitionService {
//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }
//    @Override
//    public boolean detectFace(String imagePath) {
//        CascadeClassifier classifier = new CascadeClassifier("D:\\home\\equity\\manage\\src\\main\\resources\\path\\to\\haarcascade_frontalface_default.xml");
//        if (classifier.empty()) {
//            System.out.println("--(!)Error loading cascade classifier");
//            return false;
//        }
//
//        Mat image = Imgcodecs.imread(imagePath);
//        if (image.empty()) {
//            System.out.println("--(!)Error reading image");
//            return false;
//        }
//
//        MatOfRect faceDetections = new MatOfRect();
//        classifier.detectMultiScale(image, faceDetections);
//
//        //for (Rect rect : faceDetections.toArray()) {
//        //    Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 0, 255));
//        //
//        //    // 画出脸部矩形框
//        //    Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);
//        //
//        //    // 加载脸部特征点检测器
//        //    CascadeClassifier eyeClassifier = new CascadeClassifier("D:\\home\\equity\\manage\\src\\main\\resources\\path\\to\\haarcascade_eye.xml");
//        //    CascadeClassifier mouthClassifier = new CascadeClassifier("D:\\home\\equity\\manage\\src\\main\\resources\\path\\to\\haarcascade_mcs_mouth.xml");
//        //
//        //    Mat face = new Mat(image, rect);
//        //    // 检测嘴
//        //    MatOfRect mouths = new MatOfRect();
//        //    mouthClassifier.detectMultiScale(face, mouths);
//        //    for (Rect rect_item : mouths.toArray()) {
//        //        log.info("嘴巴");
//        //        Imgproc.rectangle(image, rect_item.tl(), rect_item.br(), new Scalar(0, 0, 255), 3);
//        //    }
//        //    // 检测眼睛
//        //    MatOfRect eyes = new MatOfRect();
//        //    eyeClassifier.detectMultiScale(face, eyes);
//        //    for (Rect rect_item : eyes.toArray()) {
//        //        log.info("眼睛");
//        //        Imgproc.rectangle(image, rect_item.tl(), rect_item.br(), new Scalar(255, 0, 0), 3);
//        //    }
//        //
//        //}
//        // 显示或保存带有检测框的图像
//        // Imgcodecs.imwrite("detected_faces.jpg", image);
//        // Imgproc.imshow("Detected Faces", image);
//        // Imgproc.waitKey(0);
//
//        return faceDetections.total() > 0;
//    }
//
//    public static void main(String[] args) {
//        String znrlsp369 = PanXiaoZhang.getPassword("zhangxun123");
//        System.out.println(znrlsp369);
//    }
//}
