package com.oumasoft.platform.kafka.service;

/**
 * @author crystal
 */
public interface SendMessageService {

    /**
     *  发送消息
     * @param message 消息
     */
    void send(String message);
}
