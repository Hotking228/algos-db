package com.hotking.algosdb.validation.password;

import com.hotking.algosdb.dto.RegisterForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm value, ConstraintValidatorContext context) {
        if(value.getPassword() == null || value.getPassword().isEmpty() || value.getPassword().isBlank()) return false;
        if(value.getConfirmPassword() == null || value.getConfirmPassword().isEmpty() || value.getConfirmPassword().isBlank()) return false;
        return value.getPassword().equals(value.getConfirmPassword());
    }
}
