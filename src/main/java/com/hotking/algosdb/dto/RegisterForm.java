package com.hotking.algosdb.dto;

import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.enums.Role;
import com.hotking.algosdb.validation.password.PasswordMatches;
import com.hotking.algosdb.validation.username.UserExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class RegisterForm {
    @NotBlank(message = "Требуется электронная почта")
    @Email(message = "Введите электронную почту правильно")
    private String email;

    @NotBlank(message = "Требуется имя пользователя")
    @Size(min = 1, max = 20, message = "Имя пользователя должно быть от 1 до 20 символов")
    @UserExists
    private String username;

    @NotBlank(message = "Требуется пароль")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    private String confirmPassword;

    public User toUser() {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
