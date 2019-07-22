package org.springframework.nanotrader.web.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Currency;
import java.util.Locale;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { LocaleHasCurrencyValidator.class })
public @interface HasCurrency {
    String message() default "Address is incorrect";
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
}

class LocaleHasCurrencyValidator implements ConstraintValidator<HasCurrency, Locale> {
    @Override
    public void initialize(HasCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(Locale value, ConstraintValidatorContext context) {
        try {
            Currency.getInstance(value);
        } catch(Exception ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid Locale").addConstraintViolation();
            return false;
        }
        return true;
    }
}