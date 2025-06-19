package com.joshi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadedEvent {
    private String fileName;
    private String fileType;
    private String uploadTime;
    private String s3Path;
}
