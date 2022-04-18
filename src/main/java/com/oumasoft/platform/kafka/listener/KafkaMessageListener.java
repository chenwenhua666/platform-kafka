package com.oumasoft.platform.kafka.listener;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oumasoft.platform.kafka.constants.StringConstant;
import com.oumasoft.platform.kafka.entity.MessageTemplate;
import com.oumasoft.platform.kafka.properties.PlatformRedirectUriProperties;
import com.oumasoft.platform.kafka.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

import static com.oumasoft.platform.kafka.constants.KafkaConstant.*;
import static com.oumasoft.platform.kafka.constants.PlatformConstant.HMACMD5_KEY;
import static com.oumasoft.platform.kafka.constants.PlatformConstant.SIGN_LENGTH;

/**
 * @author crystal
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final PlatformRedirectUriProperties redirectUriProperties;
    private final SendMessageService sendMessageService;

    @KafkaListener(topics = TOPIC_WANGBAO, containerFactory = "ackContainerFactory")
    public void handleMessageSignUpOnline(ConsumerRecord record, Acknowledgment acknowledgment) {
        String message = null;
        String messageContent = null;
        try {
            message = (String) record.value();
            messageContent = message.substring(SIGN_LENGTH, message.length());
            log.info("topic: {},监听收到消息: {}", TOPIC_WANGBAO, message);
            Map<String, String> mapProperties = redirectUriProperties.getWangbao();
            messageRedirect(TOPIC_WANGBAO, message, mapProperties, SYSTEM_WANGBAO);
        } catch (Exception e) {
            log.error("消息监听出错：{}", e);
            sendAckMessage(messageContent, FAIL_MESSAGE, e.getMessage(), SYSTEM_WANGBAO);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    @KafkaListener(topics = TOPIC_WANGBAO_HIGH, containerFactory = "ackContainerFactory")
    public void handleMessageSignUpOnlineHign(ConsumerRecord record, Acknowledgment acknowledgment) {
        String message = null;
        String messageContent = null;
        try {
            message = (String) record.value();
            messageContent = message.substring(SIGN_LENGTH, message.length());
            log.info("topic: {},监听收到消息: {}", TOPIC_WANGBAO_HIGH, message);
            Map<String, String> mapProperties = redirectUriProperties.getWangbaoHigh();
            messageRedirect(TOPIC_WANGBAO_HIGH, message, mapProperties, SYSTEM_WANGBAO);
        } catch (Exception e) {
            log.error("消息监听出错：{}", e);
            sendAckMessage(messageContent, FAIL_MESSAGE, e.getMessage(), SYSTEM_WANGBAO);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    @KafkaListener(topics = TOPIC_CERTIFICATE, containerFactory = "ackContainerFactory")
    public void handleMessageCertificate(ConsumerRecord record, Acknowledgment acknowledgment) {
        String message = null;
        String messageContent = null;
        try {
            message = (String) record.value();
            messageContent = message.substring(SIGN_LENGTH, message.length());
            log.info("topic: {},监听收到消息: {}", TOPIC_CERTIFICATE, message);
            Map<String, String> mapProperties = redirectUriProperties.getZhengshu();
            messageRedirect(TOPIC_CERTIFICATE, message, mapProperties, SYSTEM_CERTIFICATE);
        } catch (Exception e) {
            log.error("消息监听出错：{}", e);
            sendAckMessage(messageContent, FAIL_MESSAGE, e.getMessage(), SYSTEM_CERTIFICATE);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    private void messageRedirect(String topic, String message, Map<String, String> mapProperties, String system) {
        if (!CollectionUtils.isEmpty(mapProperties)) {
            String messageSign = message.substring(0, SIGN_LENGTH);
            String messageContent = message.substring(SIGN_LENGTH, message.length());
            JSONObject paramsObj = JSONObject.parseObject(messageContent);
            String from = paramsObj.getString("from");
            if (SYSTEM_GPTMIS.equalsIgnoreCase(from)) {
                String localMessageSign = SecureUtil.hmacMd5(HMACMD5_KEY).digestHex(messageContent);
                if (!messageSign.equals(localMessageSign)) {
                    sendAckMessage(messageContent, NACK_MESSAGE, "消息验签失败", system);
                    log.error("消息验签错误,消息:{},本地签名:{}", message, localMessageSign);
                    return;
                }
                String messagetype = paramsObj.getString("messagetype");
                String uri = mapProperties.get(messagetype);
                if (StringUtils.isBlank(uri)) {
                    log.error("消息回调地址为空,topic:{},消息:{}", topic, message);
                    return;
                }
                HashMap<String, Object> paramMap = new HashMap<>(1);
                paramMap.put("params", messageContent);
                String requestResult = HttpUtil.post(uri, paramMap);
                if (SUCCESS.equalsIgnoreCase(requestResult)) {
                    log.info("业务系统返回地址:{},结果:{}", uri, requestResult);
                    sendAckMessage(messageContent, ACK_MESSAGE, null, system);
                } else {
                    String base64Decrypt = Base64.decodeStr(requestResult);
                    log.info("业务系统返回地址:{},结果:{},base64解密后:{}", uri, base64Decrypt, requestResult);
                    if (StringUtils.isBlank(base64Decrypt)) {
                        base64Decrypt = requestResult;
                    }
                    sendAckMessage(messageContent, NACK_MESSAGE, base64Decrypt, system);
                }
            }
        }
    }

    private void sendAckMessage(String messageContent, String ackType, String requestResult, String system) {
        JSONObject paramsObj = JSONObject.parseObject(messageContent);
        String from = paramsObj.getString("from");
        if (SYSTEM_GPTMIS.equalsIgnoreCase(from)) {
            String userid = paramsObj.getString("userid");
            String version = paramsObj.getString("version");
            String messageid = paramsObj.getString("messageid");
            String time = paramsObj.getString("time");
            String messagetype = paramsObj.getString("messagetype");

            MessageTemplate messageTemplate = new MessageTemplate();
            messageTemplate.setVersion(version);
            messageTemplate.setFrom(system);
            messageTemplate.setUserid(userid);
            messageTemplate.setMessageid(messageid);
            messageTemplate.setTime(time);
            messageTemplate.setMessagetype(ackType.concat(StringConstant.UNDER_LINE).concat(messagetype));
            Map<String, String> data = new HashMap<>(1);
            if (FAIL_MESSAGE.equals(ackType) || NACK_MESSAGE.equals(ackType)) {
                data.put("error", requestResult);
                messageTemplate.setData(data);
            } else {
                messageTemplate.setData(data);
            }
            String sendMessageContent = JSON.toJSONString(messageTemplate);
            sendMessageService.send(TOPIC_GPTMIS, sendMessageContent);
        }
    }

}
