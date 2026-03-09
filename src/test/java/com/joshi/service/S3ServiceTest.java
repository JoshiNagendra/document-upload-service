package com.joshi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    @Test
    void testUploadFile_success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test cases".getBytes()
        );

        String resultKey = s3Service.uploadFile(file);

        assertEquals("raw/test.txt", resultKey);

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> bodyCaptor = ArgumentCaptor.forClass(RequestBody.class);

        verify(s3Client, times(1)).putObject(requestCaptor.capture(), bodyCaptor.capture());

        PutObjectRequest capturedRequest = requestCaptor.getValue();
        assertEquals("joshi-learning-bucket-2025", capturedRequest.bucket());
        assertEquals("raw/test.txt", capturedRequest.key());
        assertEquals("text/plain", capturedRequest.contentType());
    }
    @Test
    void testUploadFile_emptyFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[]{}
        );

        String resultKey = s3Service.uploadFile(file);

        assertEquals("raw/empty.txt", resultKey);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }
//    @Test
//    void testUploadFile_unsupportedTypeThrowsException() {
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "data.zip", "application/zip", new byte[]{1, 2, 3}
//        );
//
//        IllegalArgumentException ex = assertThrows(
//                IllegalArgumentException.class,
//                () -> s3Service.uploadFile(file)
//        );
//
//        assertTrue(ex.getMessage().contains("Unsupported file type"));
//        verifyNoInteractions(s3Client); // ensure S3 was never called
//    }
}