package com.oumasoft.platform.kafka.schedule;

import com.oumasoft.platform.kafka.constants.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;

/**
 * @author crystal
 */
@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleTask {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron = "0/30 * * * * ?")
    private void configureTasks() {

        log.info("执行时任务时间: {}", LocalDateTime.now());
        String data = "testMessage" + System.currentTimeMillis();
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(KafkaConstant.TOPIC_WANGBAO, data);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("定时任务成功发送消息：{}，offset=[{}]", data, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("定时任务消息：{} 发送失败，原因：{}", data, ex.getMessage());
            }
        });

    }
}
