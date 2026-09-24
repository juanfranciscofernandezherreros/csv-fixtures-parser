package com.fernandez.fixtures.consumer;
import com.example.csvwatcher.watcher.FileEventKey;
import com.example.csvwatcher.watcher.FileEventValue;
import com.fernandez.fixtures.service.FixturesPublishService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FileReadyConsumer {
    private final FixturesPublishService publisher;
    public FileReadyConsumer(FixturesPublishService publisher){this.publisher=publisher;}

    @KafkaListener(topics="${app.kafka.topics.file-ready}",groupId="${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<FileEventKey,FileEventValue> record){
        FileEventValue value=record.value();
        if(value!=null && "FIXTURES".equalsIgnoreCase(value.getFileType())){
            publisher.publish(value.getFilePath());
        }
    }
}
