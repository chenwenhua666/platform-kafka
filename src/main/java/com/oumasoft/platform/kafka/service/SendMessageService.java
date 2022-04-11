package com.oumasoft.platform.kafka.service;

/**
 * @author crystal
 */
public interface SendMessageService {

    /**
     *  发送消息
     * @param topic topic
     * @param message 消息
     */
    void send(String topic, String message);
}
