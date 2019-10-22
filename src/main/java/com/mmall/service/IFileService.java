package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 文件上传
 * @author: Mr.Shang
 * @Date: 2017-10-19 13:53
 **/
public interface IFileService {
    String upload(MultipartFile file, String path);

}
