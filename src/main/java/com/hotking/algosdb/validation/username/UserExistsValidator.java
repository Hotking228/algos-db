package com.hotking.algosdb.validation.username;

import com.hotking.algosdb.enums.Status;
import com.hotking.algosdb.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistsValidator
        implements ConstraintValidator<UserExists, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(userService.isUserExists(value) == Status.REGISTERED) return false;
        return true;
    }
}
