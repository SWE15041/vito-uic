package com.jay.vito.uic.server.web.controller;

import com.jay.vito.common.exception.HttpBadRequestException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证工具
 *
 * @author zhaixm
 */
public class ValidUtil {
	public static void valid(BindingResult result) {
		if (result.hasErrors()) {
			List<ObjectError> allErrors = result.getAllErrors();
			List<String> message = new ArrayList<>();
			for (ObjectError error : allErrors) {
				String errorMessage = error.getDefaultMessage();
				message.add(errorMessage);
			}
			throw new HttpBadRequestException(String.join(",", message), "ERROR_MESSAGE");
		}
	}
}
