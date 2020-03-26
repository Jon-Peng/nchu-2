package com.pjy.nchu2.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class MyFileUtil {
    public static String uploadFile(byte[] file,String filePath,String fileName,String ext) throws IOException {
        File targetFile = new File(filePath);
        if (!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(filePath+fileName+"."+ext);
        fos.write(file);
        fos.flush();
        fos.close();
        return filePath+fileName+"."+ext;
    }
}
