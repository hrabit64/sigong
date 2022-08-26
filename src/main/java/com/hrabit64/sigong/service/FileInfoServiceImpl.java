package com.hrabit64.sigong.service;

import com.hrabit64.sigong.domain.file.FileInfo;
import com.hrabit64.sigong.domain.file.FileInfoRepository;
import com.hrabit64.sigong.dto.file.controllerDto.FileInfoResponseControllerDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoAddRequestServiceDto;
import com.hrabit64.sigong.dto.file.serviceDto.FileInfoResponseServiceDto;
import com.hrabit64.sigong.exception.ErrorCode;
import com.hrabit64.sigong.exception.SigongException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hrabit64.sigong.config.SigongConfig.CodeExpireTime;

@RequiredArgsConstructor
@Service
public class FileInfoServiceImpl implements FileInfoService {

    private final FileInfoRepository fileInfoRepository;

    @Override
    @Transactional
    public void add(FileInfoAddRequestServiceDto addRequestDto) {
        fileInfoRepository.save(addRequestDto.toEntity(CodeExpireTime));
    }

    @Override
    @Transactional
    public FileInfoResponseControllerDto revoke(String code) {
        FileInfo target = fileInfoRepository.findById(code)
                .orElseThrow(() -> new SigongException(ErrorCode.INVALID_FILE_CODE));
        fileInfoRepository.delete(target);

        return new FileInfoResponseControllerDto(target);
    }

    @Override
    @Transactional(readOnly = true)
    public FileInfoResponseServiceDto findByCode(String code) {

        return new FileInfoResponseServiceDto(fileInfoRepository.findById(code)
                .orElseThrow(() -> new SigongException(ErrorCode.INVALID_FILE_CODE)));
    }

    @Override
    @Transactional(readOnly = true)
    public FileInfoResponseServiceDto findByFileName(String fileName) {
        return new FileInfoResponseServiceDto(fileInfoRepository.findFileInfoByFileName(fileName)
                .orElse(FileInfo.builder().build()));
    }
}
