package com.etlpat.controller;

import com.etlpat.pojo.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;


// 文件的上传/下载
//
// (1)文件上传
//  ①文件上传介绍
//      文件上传,也称为upload,是指将本地图片、视频、音频等文件上传到服务器上,可以供其他用户浏览或下载的过程。
//      文件上传在项目中应用非常广泛,我们经常发微博、发微信朋友圈都用到了文件上传功能。
//
//  ②对前端的要求
//      文件上传时,对页面的form表单有如下要求:
//          -method="post"                  采用post方式提交数据
//          -enctype="multipart/form-data"  采用multipart格式上传文件
//          -type="file"                    使用input的file控件上传
//
//  ③后端实现方式
//      服务端要接收客户端页面上传的文件,通常都会使用Apache的两个组件:
//          -commons-fileupload
//          -commons-io
//      Spring框架在spring-web包中对文件上传进行了封装,大大简化了服务端代码,
//      我们只需要在Controller的方法中声明一个MultipartFile类型的参数即可接收上传的文件。
//
//
// (2)文件下载
//  ①文件下载介绍
//      文件下载,也称为download,是指将文件从服务器传输到本地计算机的过程。
//      通过浏览器进行文件下载,通常有两种表现形式:
//          -以附件形式下载,弹出保存对话框,将文件保存到指定磁盘目录
//          -直接在浏览器中打开
//      通过浏览器进行文件下载,本质上就是服务端将文件以流的形式写回浏览器的过程。
//


// 用于文件的上传/下载
@RestController
@RequestMapping("/common")
public class UploadController {

    // 获取yml中配置的文件上传路径
    @Value("${yum-dash-ept.upload-path}")
    private String uploadPath;


    // 文件上传（本地->服务器）
    @PostMapping("/upload")
    public R upload(MultipartFile file) {// 注意：参数file是一个临时文件,需要转存到指定位置,否则本次请求完成后临时文件会删除
        // (1)处理文件名
        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 新的文件名
        String newFilename = UUID.randomUUID() + suffix;

        // (2)处理文件路径
        // 创建目录对象
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            // 若目录不存在，则创建该目录
            dir.mkdirs();
        }

        // (3)将上传的file文件，转存到服务器目录中
        try {
            // 将上传的文件，转存到指定路径下
            file.transferTo(new File(uploadPath + newFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 返回新的文件名
        return R.success(newFilename);
    }


    // 文件下载（服务器->浏览器）
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        response.setContentType("image/jpeg");// 响应图片文件

        try {
            // (1)获取输入流/输出流对象
            // 创建name参数对应的服务器中的file对象
            File file = new File(uploadPath + name);
            // 输入流 -- 通过输入流读取文件内容
            fileInputStream = new FileInputStream(file);
            // 输出流 -- 通过输出流将文件写回浏览器
            outputStream = response.getOutputStream();

            // (2)将文件的内容写回浏览器
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                fileInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
