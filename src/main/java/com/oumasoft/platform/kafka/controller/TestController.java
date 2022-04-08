package com.oumasoft.platform.kafka.controller;

import com.oumasoft.platform.kafka.constants.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author crystal
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("producer/test/{message}")
    public void test1(@PathVariable String message) {
        log.info("ProducerDemo main方法启动");
        // 1. 生产者配置
        Properties properties = new Properties();
        // 指定kafka地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.26.188.1:9092,172.26.188.2:9092,172.26.188.3:9092");
        //properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.122.171:9092");
        // 指定ack等级
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // 指定重试次数，即生产者发送数据后没有收到ack应答时的重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 指定批次大小 16k = 16 * 1024
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 指定等待时间，单位毫秒
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 指定RecordAccumulator缓冲区大小 32m = 32 * 1024 * 1024
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 指定k-v序列化规则
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        // 指定过滤器链
        //properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Arrays.asList("com.oumasoft.kafka.demo.MessageFormatInterceptor"));

        // 2. 创建生产者
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // 3. 准备数据
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(KafkaConstant.TOPIC_WANGBAO, "testmessage" + UUID.randomUUID().toString());
        // 4. 发送数据（不带回调）
        //producer.send(record);
        // 4. 发送数据（带回调）
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    // 回调函数，该方法会在 Producer 收到 ack 时调用，为异步调用
                    String result = String.format("消息发送成功，主题Topic: %s,分区Partition: %s,偏移量Offset: %s",
                            metadata.topic(), metadata.partition(), metadata.offset());
                    log.info(result);
                } else {
                    log.error("消息发送失败:{}" , exception);
                    exception.printStackTrace();
                }
            }
        });
        // 5. 关闭连接
        producer.close();
    }

    @GetMapping("consumer/test/{message}")
    public void test2(@PathVariable String message) throws Exception{
        log.info("ConsumerDemo main方法启动");
        // 1. 消费者配置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.26.188.1:9092,172.26.188.2:9092,172.26.188.3:9092");
        //properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.122.171:9092");
        // 自动提交offset
        // properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 关闭自动提交offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 提交offset的时间，单位ms，即1秒钟提交一次
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 指定k-v反序列化规则
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 指定消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer");

        // 2. 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 订阅主题
        consumer.subscribe(Collections.singletonList(KafkaConstant.TOPIC_WANGBAO));
        while (true){
            // 拉取数据，指定轮询时间为1秒
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(consumerRecord.toString());
            }
            // consumer.commitAsync();
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    if (exception == null) {
                        log.info("提交offset: " + offsets + "成功");
                    } else {
                        log.error("提交失败",exception);
                        exception.printStackTrace();
                    }
                }
            });
            TimeUnit.SECONDS.sleep(1);
       }
    }
}
