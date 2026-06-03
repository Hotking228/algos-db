package com.hotking.algosdb.controller;

import com.hotking.algosdb.dto.RegisterForm;
import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.enums.Role;
import com.hotking.algosdb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @GetMapping("/register")
    public String showRegister(Model model,
                               @RequestParam(name = "registerForm", required = false) RegisterForm registerForm){
        if(registerForm == null) {
            registerForm = new RegisterForm();
        }
        model.addAttribute("registerForm", registerForm);

        return "management/register";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @Valid @ModelAttribute RegisterForm registerForm,
                           BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
                System.out.println(error.getField() + " " + error.getDefaultMessage());
            }

            if(bindingResult.hasGlobalErrors()){
                errors.put("global", bindingResult.getGlobalError().getDefaultMessage());
                System.out.println("global" + " " + bindingResult.getGlobalError().getDefaultMessage());
            }
            return "management/register";
        }

        User user = User.builder()
                .username(registerForm.getUsername())
                .email(registerForm.getEmail())
                .password(encoder.encode(registerForm.getPassword()))
                .role(Role.USER)
                .build();

        registerForm = null;

        userService.save(user);
        return "redirect:/login";
    }
}
