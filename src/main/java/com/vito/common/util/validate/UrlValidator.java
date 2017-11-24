package com.vito.common.util.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator implements ConstraintValidator<UrlPattern, String> {

    //5~10位的数字与字母组合
    private static Pattern pattern = Pattern.compile("^(http[s]?:\\/\\/)*([\\w-]+\\.)+[\\w-]+([\\w-./?%&=:#,]*)?$");

    private boolean allowEmpty = true;

    public void initialize(UrlPattern urlAnnotation) {
        allowEmpty = urlAnnotation.empty();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Validator.isNull(value)) {
            if (allowEmpty) {
                return true;
            } else {
                return false;
            }
        }
        Matcher m = pattern.matcher(value);
        return m.matches();
    }
}