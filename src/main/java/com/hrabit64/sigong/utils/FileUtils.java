package com.hrabit64.sigong.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class FileUtils {

    @Value("${sigong.filesave.path}")
    private String savePath;

    public File convert(MultipartFile file,String code) throws IOException {

        File newFile = new File(savePath+code+"_"+file.getOriginalFilename()+".sigong");
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return newFile;
    }

    public File convertOrigin(File file,String originName) throws IOException {

        File newFile = new File(savePath+originName);
        file.renameTo(newFile);
        return newFile;
    }

    public File find(String fileName){

        return new File(savePath+fileName);
    }

    public boolean delete(String fileName){

        return this.find(fileName).delete();
    }
}
