package com.goalkeepers.server.config;

import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FirebaseConfig {
    
    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount =
            new FileInputStream(firebaseConfigPath);

            FirebaseOptions options = FirebaseOptions.builder()
                                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                        .setStorageBucket(firebaseBucket)
                                        .build();
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase has been initialized");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
