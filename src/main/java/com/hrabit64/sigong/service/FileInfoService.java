package com.hrabit64.sigong.service;

import com.hrabit64.sigong.dto.file.controllerDto.FileInfoResponseControllerDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoAddRequestServiceDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoResponseServiceDto;
import org.springframework.stereotype.Service;

@Service
public interface FileInfoService {

    void add(FileInfoAddRequestServiceDto addRequestDto);
    FileInfoResponseControllerDto revoke(String code);
    FileInfoResponseServiceDto findByCode(String code);
    FileInfoResponseServiceDto findByFileName(String fileName);

}
