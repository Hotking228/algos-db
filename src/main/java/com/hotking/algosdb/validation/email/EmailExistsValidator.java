package com.hotking.algosdb.validation.email;

import com.hotking.algosdb.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailExistsValidator
        implements ConstraintValidator<EmailExists, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(userService.isEmailExists(value)) return false;
        return true;
    }
}
