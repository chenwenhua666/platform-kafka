package com.oumasoft.platform.kafka.service.impl;

import cn.hutool.http.HttpRequest;
import com.oumasoft.platform.kafka.constants.ClientConstant;
import com.oumasoft.platform.kafka.constants.KafkaConstant;
import com.oumasoft.platform.kafka.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author crystal
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageServiceImpl implements SendMessageService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(KafkaConstant.TOPIC_WANGBAO, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("成功发送消息：{}，offset=[{}]", message, result.getRecordMetadata().offset());

                /*String requestResult = HttpRequest.post(ClientConstant.EXAM_ADMIN_RECEIVE_URL)
                        .header(ClientConstant.KAFKA_HEADER, ClientConstant.KAFKA_HEADER)
                        .body(message)
                        .execute().body();
                System.out.println(requestResult);*/
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("消息：{} 发送失败，原因：{}", message, ex.getMessage());
            }
        });
    }
}
