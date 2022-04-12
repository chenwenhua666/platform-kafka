package com.oumasoft.platform.kafka.constants;

/**
 * 正则常量
 *
 * @author crystal
 */
public interface RegexpConstant {

    /**
     * 手机号正则
     */
    String MOBILE = "^((\\+|00)86)?((134\\d{4})|((13[0-3|5-9]|14[1|5-9]|15[0-9]|16[2|5|6|7]|17[0-8]|18[0-9]|19[0-2|5-9])\\d{8}))$";

    /**
     * 密码正则
     * 数字+字母，数字+特殊字符，字母+特殊字符，数字+字母+特殊字符组合，而且不能是纯数字，纯字母，纯特殊字符 8-16位
     */
    String PASSWORD = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![!@_#$%^&*()-+=,.?]+$)[\\da-zA-Z!@_#$%^&*()-+=,.?]{8,16}$";

    /**
     * 用户名正则
     */
    String USERNAME = "^[0-9a-zA-Z_]{1,}$";

    /**
     * 身份证号码 IdcardUtil.isValidCard
     * 简单正则 (^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)
     *
     *  // String IDCARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
     */
    String IDCARD = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

}
