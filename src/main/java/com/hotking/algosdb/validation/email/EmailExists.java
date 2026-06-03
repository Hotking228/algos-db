package com.hotking.algosdb.validation.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailExistsValidator.class)
@Documented
public @interface EmailExists {
    String message() default "Пользователь с этой почтой уже зарегистрирован";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
