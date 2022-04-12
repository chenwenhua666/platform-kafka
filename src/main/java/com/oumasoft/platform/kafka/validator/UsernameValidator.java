package com.oumasoft.platform.kafka.validator;

import com.oumasoft.platform.kafka.annotation.Username;
import com.oumasoft.platform.kafka.constants.RegexpConstant;
import com.oumasoft.platform.kafka.tool.utils.PlatformUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验是否为合法的用户名
 *
 * @author crystal
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {

    @Override
    public void initialize(Username username) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            String regex = RegexpConstant.USERNAME;
            return PlatformUtil.match(regex, s);
        } catch (Exception e) {
            return false;
        }
    }
}
