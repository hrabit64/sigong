package com.hrabit64.sigong.task;

import com.hrabit64.sigong.dto.file.serviceDto.FileInfoResponseServiceDto;
import com.hrabit64.sigong.service.FileInfoServiceImpl;
import com.hrabit64.sigong.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class FileRemoveScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FileInfoServiceImpl fileInfoService;
    private final FileUtils fileUtils;

    @Value("${sigong.filesave.path}")
    private String savePath;

    @Scheduled(fixedDelay = 60000)
    public void run(){
        Integer removeCnt = 0;

        logger.info("File Remove Scheduler Run Start");

        logger.info("Get File List......");

        List<String> fileNames = Arrays.asList(Objects.requireNonNull(new File(savePath).list()));

        if(fileNames.size() == 0){
            logger.info("Storage is clean. stop running");
            return;
        }

        logger.info("find {} files. now checking start",fileNames.size());

        for (String fileName: fileNames) {

            File targetFile = fileUtils.find(fileName);

            if(!targetFile.exists()){
                logger.warn("find {} file, but it's not exist. pass",fileName);
                continue;
            }

            FileInfoResponseServiceDto fileInfoResponseServiceDto = fileInfoService.findByFileName(fileName);

            if(fileInfoResponseServiceDto.getFileName() != null) continue;

            if(!targetFile.delete()){
                logger.warn("try delete {} file, but not working",fileName);
                continue;
            }

            removeCnt++;
        }

        logger.info("File Remove Scheduler Run Fin. delete files count : {}",removeCnt);

    }
}
