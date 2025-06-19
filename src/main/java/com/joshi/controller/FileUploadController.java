package com.joshi.controller;

import com.joshi.dto.DocumentUploadedEvent;
import com.joshi.event.DocumentEventPublisher;
import com.joshi.model.DocumentMetadata;
import com.joshi.repository.DocumentMetadataRepository;
import com.joshi.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private DocumentMetadataRepository repository;

    @Autowired
    private DocumentEventPublisher eventPublisher;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){
        try{
            //1. Upload to S3;
            String s3Path = s3Service.uploadFile(file);

            //Save metadata in DB
            DocumentMetadata metadata =  new DocumentMetadata();
            metadata.setFileName(file.getOriginalFilename());
            metadata.setFileType(file.getContentType());
            metadata.setSize(file.getSize());
            metadata.setS3Path(s3Path);
            metadata.setUploadedAt(LocalDateTime.now());
            repository.save(metadata);

            //Publish Kafka Event
            DocumentUploadedEvent event = new DocumentUploadedEvent(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    LocalDateTime.now().toString(),
                    s3Path
            );
            eventPublisher.publish(event);
            return ResponseEntity.ok("File uploaded Succesfully");


        }catch (IOException e){
            return ResponseEntity.status(500).body("Upload failed : "+e.getMessage());
        }
    }


}
