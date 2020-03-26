package com.pjy.nchu2.service;

import com.pjy.nchu2.utils.MyFileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    public String uploadImg(MultipartFile file, String filePath) throws IOException {

        byte[] fileBytes = file.getBytes();
        String ext = file.getContentType().split("/")[1].toLowerCase();//后缀
        String fileName = System.currentTimeMillis()*1000+"";

        if ("png/gif/jpg/jpeg/bmp".contains(ext)) {
            String newPath = MyFileUtil.uploadFile(fileBytes,filePath,fileName,ext);
            return fileName+"."+ext;
        }
        return null;

    }
}
