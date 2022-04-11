package com.oumasoft.platform.kafka.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author crystal
 */
@Data
public class MessageFile implements Serializable {

    private String url;

    private String sha1;

}
