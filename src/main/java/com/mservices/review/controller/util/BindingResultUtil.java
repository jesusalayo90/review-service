package com.mservices.review.controller.util;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class BindingResultUtil {

    public static String formatErrors(BindingResult result) {
        List<String> message = result.getFieldErrors().stream()
                .map(f -> String.format("Field '%s' %s.", f.getField(), f.getDefaultMessage())).collect(Collectors.toList());
        return message.toString();
    }

}
