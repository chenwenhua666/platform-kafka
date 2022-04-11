package com.oumasoft.platform.kafka.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 消息类型
 *
 * @author crystal
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SignUpOnlineMessageType {
    /**
     * ack_bumoban
     */
    ACK_BUMOBAN("ack_bumoban"),

    /**
     * fail_bumoban
     */
    FAIL_BUMOBAN("fail_bumoban"),

    /**
     * un_support
     */
    UN_SUPPORT("un_support");

    private String type;

    public boolean eq(SignUpOnlineMessageType type) {
        for (SignUpOnlineMessageType messageType : SignUpOnlineMessageType.values()) {
            return messageType.equals(type);
        }
        return false;
    }

    public static SignUpOnlineMessageType getType(String type) {
        if (type == null || type.isEmpty()) {
            return UN_SUPPORT;
        }
        for (SignUpOnlineMessageType messageType : SignUpOnlineMessageType.values()) {
            if (messageType.getType().equalsIgnoreCase(type)) {
                return messageType;
            }
        }
        return UN_SUPPORT;
    }
}
