package com.hotking.algosdb.validation.email;

import com.hotking.algosdb.enums.Status;
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
        if(userService.isEmailExists(value) == Status.REGISTERED) return false;
        return true;
    }
}
