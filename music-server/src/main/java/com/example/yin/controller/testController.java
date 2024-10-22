package com.example.yin.controller;

import com.example.yin.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("test")
@RestController
public class testController {
    @Value("${minio.bucket-name}")
    private String bucketName;
    @PostMapping("/upload")
    public R uploadFile(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        String s = MinioUploadController.uploadFile(file);
        String storeUrlPath = "/" + bucketName + "/" + fileName;
        if (s.equals("File uploaded successfully!")) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }
}
