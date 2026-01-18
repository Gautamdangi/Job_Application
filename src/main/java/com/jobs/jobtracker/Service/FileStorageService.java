package com.jobs.jobtracker.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {
    private final String UPLOAD_DIR = "uploads/resume/";

    private Path fileStorageLocation;

    public String storeFile(MultipartFile file, Long userId){
        if(file.isEmpty()){
            throw new RuntimeException("Resume file is empty");

        }

        if(!file.getContentType().equals("application/pdf")){
            throw new RuntimeException("Only PDF format is allowed");

        }
        try{
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileName = UUID.randomUUID() + "_"+ file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR);
            Files.write(path , file.getBytes());
            return path.toString();


        } catch (IOException e) {
            throw new RuntimeException("Failed to store resume");
        }

    }
}
