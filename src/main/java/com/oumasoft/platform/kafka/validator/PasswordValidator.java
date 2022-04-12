package com.oumasoft.platform.kafka.validator;


import com.oumasoft.platform.kafka.annotation.Password;
import com.oumasoft.platform.kafka.constants.RegexpConstant;
import com.oumasoft.platform.kafka.tool.utils.PlatformUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验是否为合法的密码
 *
 * @author crystal
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password password) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            String regex = RegexpConstant.PASSWORD;
            return PlatformUtil.match(regex, s);
        } catch (Exception e) {
            return false;
        }
    }
}
