package com.hrabit64.sigong.domain.file;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo,String> {

    Optional<FileInfo> findFileInfoByFileName(String fileName);

}
