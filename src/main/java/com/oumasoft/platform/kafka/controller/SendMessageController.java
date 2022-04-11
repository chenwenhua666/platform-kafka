package com.oumasoft.platform.kafka.controller;

import com.oumasoft.platform.kafka.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.oumasoft.platform.kafka.constants.KafkaConstant.SUCCESS;

/**
 * @author crystal
 */
@RestController
@RequiredArgsConstructor
public class SendMessageController {

    private final SendMessageService sendMessageService;

    @PostMapping("send")
    public String postSend(String topic, String message, HttpServletRequest request) {
        sendMessageService.send(topic, message);
        return SUCCESS;
    }
}
