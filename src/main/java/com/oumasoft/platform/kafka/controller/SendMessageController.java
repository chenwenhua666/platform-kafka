package com.oumasoft.platform.kafka.controller;

import com.oumasoft.platform.kafka.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author crystal
 */
@RestController
@RequiredArgsConstructor
public class SendMessageController {

    private final SendMessageService sendMessageService;

    @GetMapping("send/{message}")
    public void getSend(@PathVariable String message) {
        sendMessageService.send(message);
    }

    @PostMapping("send/{message}")
    public void postSend(@PathVariable String message, HttpServletRequest request) {
        sendMessageService.send(message);
    }

}
