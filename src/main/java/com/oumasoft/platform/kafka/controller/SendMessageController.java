package com.oumasoft.platform.kafka.controller;

import com.oumasoft.platform.kafka.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

import static com.oumasoft.platform.kafka.constants.KafkaConstant.SUCCESS;

/**
 * @author crystal
 */
@Validated
@RestController
@RequiredArgsConstructor
public class SendMessageController {

    private final SendMessageService sendMessageService;

    @PostMapping("send")
    public String postSend(@NotBlank(message = "{required}") String topic, @NotBlank(message = "{required}") String message) {
        sendMessageService.send(topic, message);
        return SUCCESS;
    }
}
