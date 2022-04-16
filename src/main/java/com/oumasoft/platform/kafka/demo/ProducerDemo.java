package com.oumasoft.platform.kafka.demo;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.oumasoft.platform.kafka.constants.KafkaConstant;
import com.oumasoft.platform.kafka.constants.PlatformConstant;
import com.oumasoft.platform.kafka.entity.MessageFile;
import com.oumasoft.platform.kafka.entity.MessageTemplate;
import com.oumasoft.platform.kafka.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.time.LocalDateTime;
import java.util.*;


/**
 * @author crystal
 */
@Slf4j
public class ProducerDemo {

    public static void main(String[] args) {
        log.info("ProducerDemo main方法启动");
        Properties properties = new Properties();
        //properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.26.188.1:9092,172.26.188.2:9092,172.26.188.3:9092");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.122.171:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Arrays.asList("com.oumasoft.kafka.demo.MessageFormatInterceptor"));
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        Map<String, Object> data = new LinkedHashMap<>(2);
        data.put("ksxmm", "070");
        data.put("ksny", "202206");
        MessageFile messageFile = new MessageFile();
        messageFile.setUrl("070_202206_1_bumoban_20220411103550642.zip");
        messageFile.setSha1("0cc39fea3bbdd646ec8ed5d55712093fffb10f76");
        data.put("file", messageFile);
        MessageTemplate messageTemplate = new MessageTemplate();
        messageTemplate.setVersion("1.0.0.202204");
        messageTemplate.setFrom("gptmis");
        messageTemplate.setUserid("sysadmin");
        messageTemplate.setMessageid("{2D65854F-61A7-45F0-AFF0-DAD8EA44031F}");
        messageTemplate.setTime(DateUtil.formatSplitTime(LocalDateTime.now()));
        messageTemplate.setMessagetype("bumoban");
        messageTemplate.setData(data);
        String messageContent = JSON.toJSONString(messageTemplate);
        log.info("发送message内容：{}", messageContent);
        String sign = SecureUtil.hmacMd5(PlatformConstant.HMACMD5_KEY).digestHex(messageContent);
        log.info("发送message签名：{}", sign);
        String message = sign.concat(messageContent);

        ProducerRecord<String, String> record = new ProducerRecord<String, String>(KafkaConstant.TOPIC_WANGBAO_HIGH, message);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                String result = String.format("消息发送成功，主题Topic: %s,分区Partition: %s,偏移量Offset: %s",
                        metadata.topic(), metadata.partition(), metadata.offset());
                log.info(result);
            } else {
                log.info("消息发送失败:", exception);
                exception.printStackTrace();
            }
        });
        // 5. 关闭连接
        producer.close();
    }
}
