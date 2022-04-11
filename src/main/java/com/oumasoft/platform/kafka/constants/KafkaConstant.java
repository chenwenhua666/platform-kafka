package com.oumasoft.platform.kafka.constants;

/**
 * @author crystal
 */
public interface KafkaConstant {

    /**
     * 默认分区大小
     */
    Integer DEFAULT_PARTITION_NUM = 3;

    /**
     * Topic 名称
     */
    String TOPIC_TEST = "test";

    /**
     * 网报 topic
     */
    String TOPIC_WANGBAO= "wangbao";

    /**
     * 网报 hign topic
     */
    String TOPIC_WANGBAO_HIGH= "wangbao-high";

    /**
     * 证书 topic
     */
    String TOPIC_CERTIFICATE = "zhengshu";

    /**
     * gptmis 系统
     */
    String SYSTEM_GPTMIS = "gptmis";

    /**
     *  网报系统
     */
    String SYSTEM_WANGBAO = "wangbao";

    /**
     * 证书系统
     */
    String SYSTEM_CERTIFICATE = "zhengshu";

    /**
     * 调用成功标识
     */
    String SUCCESS = "success";

    /**
     * 确认消息
     */
    String ACK_MESSAGE = "ack";

    /**
     * 失败消息
     */
    String FAIL_MESSAGE = "fail";

}
