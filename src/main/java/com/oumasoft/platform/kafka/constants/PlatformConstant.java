package com.oumasoft.platform.kafka.constants;

/**
 * @author crystal
 */
public interface PlatformConstant {
    /**
     * 消息签名key
     */
    String HMACMD5_KEY = "1ffcd65e8e8306df54afbf8d5dd0328c";

    /**
     * 签名长度
     */
    int SIGN_LENGTH = 32;

    /**
     * 异步线程池名称
     */
    String ASYNC_POOL = "platformAsyncThreadPool";

    /**
     * 校验请求头TOKEN名称
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * Basic
     */
    String TOKEN_BASIC_PREFIX = "Basic ";
}
