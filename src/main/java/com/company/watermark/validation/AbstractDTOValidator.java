package com.company.watermark.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractDTOValidator<T> implements SmartValidator {

    public static final String CODE_FIELD_REQUIRED = "field.required";

    @Override
    public void validate(Object target, Errors errors) {
        validate(target, errors, new Object[0]);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {

        Set<Class<?>> hints = new HashSet<>();
        for (Object h : validationHints) {
            if (h instanceof Class) {
                hints.add((Class<?>) h);
            }
        }

        validate((T) target, errors, hints);
    }

    protected abstract void validate(T dto, Errors errors, Set<Class<?>> hints);

    @Override
    public boolean supports(Class<?> clazz) {
        return ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).isAssignableFrom(clazz);
    }
}
