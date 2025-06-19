package com.joshi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="document_metadata")
@Data
public class DocumentMetadata {

    @Id
    @GeneratedValue
    private Long id;
    private String fileName;
    private String fileType;
    private long size;
    private String s3Path;
    private LocalDateTime uploadedAt;

}
