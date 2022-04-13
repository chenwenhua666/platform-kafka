package com.oumasoft.platform.kafka.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author crystal
 */
@Data
public class MessageTemplate implements Serializable {

    /**
     * 是消息格式版本号，目前使用1.0.0.202101
     */
    private String version;

    /**
     * 消息来源方
     */
    private String from;

    /**
     * user id
     */
    private String userid;

    /**
     * 消息编号
     */
    private String messageid;

    /**
     * 消息的产生时间
     */
    private String time;

    /**
     * 消息类别名
     */
    private String messagetype;

    /**
     * 数据
     */
    private Map data;

}
