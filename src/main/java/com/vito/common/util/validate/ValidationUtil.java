package com.vito.common.util.validate;

import org.apache.log4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhaixm on 2015/1/16.
 */
public class ValidationUtil {
	
	protected final static Logger logger = Logger.getLogger(ValidationUtil.class);

    public static <T> Set<ConstraintViolation<T>> validate(T entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        return constraintViolations;
    }
    
    public static void validateEntity(Object obj) {
		Set<ConstraintViolation<Object>> violations = ValidationUtil.validate(obj);
		if (violations.size() > 0) {
			StringBuilder sb = new StringBuilder();
			Iterator<ConstraintViolation<Object>> iterator = violations.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<Object> violation = iterator.next();
				logger.error("实体对象验证失败：" + violation);
				sb.append(violation.getMessage()).append(";");
			}
			throw new RuntimeException(sb.toString());
		}
	}

}
