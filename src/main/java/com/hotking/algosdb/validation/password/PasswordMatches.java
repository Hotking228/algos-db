package com.hotking.algosdb.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE}) // Валидируем класс целиком
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Пароли не сходятся";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
