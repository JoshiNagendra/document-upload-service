package com.joshi.event;

import com.joshi.dto.DocumentUploadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentEventPublisher {

    @Autowired
    private KafkaTemplate<String, DocumentUploadedEvent> kafkaTemplate;

    public void publish(DocumentUploadedEvent event) {
        kafkaTemplate.send("document.uploaded", event);
    }
}
