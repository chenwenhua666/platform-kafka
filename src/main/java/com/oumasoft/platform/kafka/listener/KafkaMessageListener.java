package com.oumasoft.platform.kafka.listener;

import cn.hutool.http.HttpRequest;
import com.oumasoft.platform.kafka.constants.ClientConstant;
import com.oumasoft.platform.kafka.constants.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author crystal
 */
@Slf4j
@Component
public class KafkaMessageListener {

    @KafkaListener(topics = KafkaConstant.TOPIC_TEST, containerFactory = "ackContainerFactory")
    public void handleMessage(ConsumerRecord record, Acknowledgment acknowledgment) {
        try {
            String message = (String) record.value();
            /*String requestResult = HttpRequest.post(ClientConstant.EXAM_ADMIN_RECEIVE_URL)
                    .header(ClientConstant.KAFKA_HEADER, ClientConstant.KAFKA_HEADER)
                    .body(message)
                    .execute().body();
            System.out.println(requestResult);*/
            log.info("topic: {},收到消息: {}",KafkaConstant.TOPIC_TEST, message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 手动提交 offset
            acknowledgment.acknowledge();
        }
    }

    @KafkaListener(topics = KafkaConstant.TOPIC_WANGBAO, containerFactory = "ackContainerFactory")
    public void handleMessage2(ConsumerRecord record, Acknowledgment acknowledgment) {
        try {
            String message = (String) record.value();
            /*String requestResult = HttpRequest.post(ClientConstant.EXAM_ADMIN_RECEIVE_URL)
                    .header(ClientConstant.KAFKA_HEADER, ClientConstant.KAFKA_HEADER)
                    .body(message)
                    .execute().body();
            System.out.println(requestResult);*/
            log.info("topic: {},收到消息: {}",KafkaConstant.TOPIC_WANGBAO, message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 手动提交 offset
            acknowledgment.acknowledge();
        }
    }

}
