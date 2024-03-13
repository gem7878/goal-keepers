package com.goalkeepers.server.service;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@DependsOn("firebaseConfig")
public class FirebaseStorageService {

    private final Bucket bucket;

    public FirebaseStorageService() {
        this.bucket = StorageClient.getInstance().bucket();
    }

    @PostConstruct
    public void postConstruct() {
        log.info("FirebaseStorageService initialized");
    }

    // 파일 업로드
    public String upload(MultipartFile file, String dirName) throws IOException, StorageException{
        InputStream content = new ByteArrayInputStream(file.getBytes());
        String fileName = convertName(file, dirName);
        bucket.create(fileName, content, file.getContentType());
        return fileName;
    }

    // 파일 삭제
    public void deleteFile(String fileName) throws StorageException{
        if (Objects.nonNull(fileName) && !fileName.isEmpty()) {
            bucket.get(fileName).delete();
        }
    }

    // 목표 담기할 때 이미지 복제
    public String copyAndRenameFile(String fileName, String dirName) throws StorageException{
        Blob blob = bucket.get(fileName);
        String newName = rename(fileName, dirName);
        bucket.create(newName, blob.getContent());
        return newName;
    }

    // 이미지 링크 가져오기
    public String showFile(String fileName) {
        if (Objects.isNull(fileName)) {
            return null;
        }
        Blob blob = bucket.get(fileName);
        URL signedUrl = blob.signUrl(30, TimeUnit.MINUTES);
        return signedUrl.toString();
    }
    
    // 난수 + 이미지 이름
    private String convertName(MultipartFile uploadFile, String dirName) {
        String originName = uploadFile.getOriginalFilename();
        String subOriginName = originName.substring(0, Math.min(originName.length(), 13));
        String newName = UUID.randomUUID() + "_" + subOriginName.replaceAll("_","");
        return dirName + "/" + newName;
    }

    // 이미지 복제할 때 난수 부분 바꾸기
    private String rename(String fileName, String dirName) {
        String[] nameArray = fileName.split("_");
        String originName = nameArray[nameArray.length - 1];
        String newName = UUID.randomUUID() + "_" + originName;
        return dirName + "/" + newName;
    }
}