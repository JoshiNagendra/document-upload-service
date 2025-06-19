package com.joshi.repository;


import com.joshi.model.DocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata,Long> {
}
