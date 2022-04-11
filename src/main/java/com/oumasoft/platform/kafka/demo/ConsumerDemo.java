package com.oumasoft.platform.kafka.demo;

import com.oumasoft.platform.kafka.constants.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author crystal
 */
@Slf4j
public class ConsumerDemo {
    public static void main(String[] args) throws InterruptedException {
        log.info("ConsumerDemo main方法启动");
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.122.171:9092");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer");
        // sh bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic wangbao-high --from-beginning
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(KafkaConstant.TOPIC_WANGBAO_HIGH));
        while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                log.info("消费者拉取消息内容：{}",consumerRecord.toString());
            }
            consumer.commitAsync((offsets, exception) -> {
                if (exception == null) {
                    log.info("ack提交offset: " + offsets + "成功");
                } else {
                    log.error("ack提交失败",exception);
                    exception.printStackTrace();
                }
            });
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
