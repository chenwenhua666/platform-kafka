package com.oumasoft.platform.kafka.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.oumasoft.platform.kafka.constants.PlatformConstant;
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
    public void send(String topic, String message) {
        String sendMessageSign = SecureUtil.hmacMd5(PlatformConstant.HMACMD5_KEY).digestHex(message);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, sendMessageSign.concat(message));
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("消息发送成功:{},topic=[{}]", message, topic);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("消息发送失败:{},topic=[{}],错误原因:{}", message, topic, ex);
            }
        });
    }
}
