package com.example.yin.controller;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.InputStream;


@Controller
public class MinioController {
    @Value("${minio.bucket-name}") // @Value注解注入配置文件中的minio.bucket-name，用于指定MinIO的存储桶名称
    private String bucketName;

    @Autowired
    private MinioClient minioClient;

    /**
     * 根据文件名获取歌曲文件
     * 通过HTTP GET请求从MinIO对象存储中获取并返回音乐文件
     *
     * @param fileName 文件名
     * @return 返回值的类型是 ResponseEntity，其中包含一个字节数组，用来存储获取到的文件内容
     */
    @GetMapping("/user01/{fileName:.+}") // 定义了URL模板，其中{fileName:.+}表示可以接受任意文件名作为路径变量，包括包含点的文件名
    public ResponseEntity<byte[]> getMusic(@PathVariable String fileName) {
        try {
            // 使用这个构建者模式来设置从MinIO存储中获取对象所需的参数，包括存储桶名称（bucketName）和对象名称（fileName）
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();

            // 调用客户端方法获取文件的输入流
            InputStream inputStream = minioClient.getObject(args);

            // 将文件内容读取为字节数组
            byte[] bytes = IOUtils.toByteArray(inputStream);

            // 设置响应头，指示浏览器如何处理响应内容
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName); // 如果需要下载文件，可以使用此头部

            // 返回字节数组作为响应
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 从 MinIO 对象存储中获取指定歌手的图片文件
     * @param fileName 文件名
     * @return ResponseEntity 封装返回的字节数组和响应头，以及HTTP状态码
     * @throws Exception 异常
     */
    @GetMapping("/user01/singer/img/{fileName:.+}") // 表示可以匹配任意文件名，包括带有点（.）的文件名
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws Exception {
        // getObject方法获取存储桶bucketName中指定路径"singer/img/" + fileName的对象
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object("singer/img/" + fileName)
                        .build()
        );

        byte[] bytes = IOUtils.toByteArray(stream);

        // 创建一个HttpHeaders对象，并设置其内容类型为MediaType.IMAGE_JPEG，表明响应内容为JPEG格式的图片。这将告知客户端（如浏览器）如何处理返回的数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 设置响应内容类型为图片类型，根据实际情况修改

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /**
     * 获取歌单图片
     * @param fileName
     * @return
     * @throws Exception
     */
    @GetMapping("/user01/songlist/{fileName:.+}")
    public ResponseEntity<byte[]> getImage1(@PathVariable String fileName) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object("songlist/" + fileName)
                        .build()
        );

        byte[] bytes = IOUtils.toByteArray(stream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 设置响应内容类型为图片类型，根据实际情况修改

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /**
     * 获取歌的图片
     * user01/singer/song/98329722.jfif
     * @param fileName
     * @return
     * @throws Exception
     */
    @GetMapping("/user01/singer/song/{fileName:.+}")
    public ResponseEntity<byte[]> getImage2(@PathVariable String fileName) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object("singer/song/" + fileName)
                        .build()
        );

        byte[] bytes = IOUtils.toByteArray(stream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 设置响应内容类型为图片类型，根据实际情况修改

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /**
     * 获取头像
     * img/avatorImages/
     * @param fileName
     * @return
     * @throws Exception
     */
    @GetMapping("/img/avatorImages/{fileName:.+}")
    public ResponseEntity<byte[]> getImage3(@PathVariable String fileName) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object("img/avatorImages/" + fileName)
                        .build()
        );

        byte[] bytes = IOUtils.toByteArray(stream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 设置响应内容类型为图片类型，根据实际情况修改

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
