package com.goalkeepers.server.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class FirebaseStorageService {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    private final Bucket bucket;

    public FirebaseStorageService() {
        initialize();;
        this.bucket = StorageClient.getInstance().bucket();
    }
    
    private void initialize() {
        try {
            System.out.println(firebaseConfigPath);
            Resource resource = new ClassPathResource(firebaseConfigPath);
            InputStream serviceAccount = resource.getInputStream();


            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                                        .setCredentials(credentials)
                                        .setStorageBucket(firebaseBucket)
                                        .build();
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase has been initialized");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String upload(MultipartFile file, String dirName) throws IOException, FirebaseException{
        InputStream content = new ByteArrayInputStream(file.getBytes());
        BlobInfo blobInfo = bucket.create(convertName(file, dirName), content, file.getContentType());
        return blobInfo.getMediaLink();
    }

    public void deleteFile(String fileName) {
        bucket.get(fileName).delete();
    }
    
    private String convertName(MultipartFile uploadFile, String dirName) {
        String newName = UUID.randomUUID() + "_" + uploadFile.getName();
        return dirName + "/" + newName;
    }
}